package com.banana.repository;

import com.banana.repository.I.IPictureRepository;
import com.banana.utils.PictureSqlBuilder;
import com.banana.annotation.DbOpenWithTransaction;
import com.banana.api.request.PictureSearchRequest;
import com.banana.api.dto.NsfwFilterSettingDto;
import com.banana.api.response.OwnerResponse;
import com.banana.api.response.PictureResponse;
import com.banana.mapper.Mapper;
import com.banana.model.*;
import com.flickr4java.flickr.photos.Photo;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.javalite.activejdbc.LazyList;

import java.util.List;
import java.util.stream.Collectors;

import static com.banana.utils.PictureSqlBuilder.Criteria;
import static java.util.Objects.isNull;

@Singleton
public class PictureRepository implements IPictureRepository {
    public static final String FIND_ALL_LABEL_ORDER_BY_COUNT = "Select label_annotation.name from label_annotation group by name order by count(*) desc;";

    @DbOpenWithTransaction
    @Override
    public List<String> findAllLabelOrderByCount() {
        LabelAnnotation.findAll().collectDistinct("name");
        LazyList<LabelAnnotation> labels = LabelAnnotation.findBySQL(FIND_ALL_LABEL_ORDER_BY_COUNT);
        return labels.stream().map(LabelAnnotation::getName).collect(Collectors.toList());
    }

    @DbOpenWithTransaction
    @Override
    public List<OwnerResponse> findAllOwner() {
        LazyList<Owner> owners = Owner.findAll();
        return owners.stream().map(Mapper::transform).collect(Collectors.toList());
    }

    @DbOpenWithTransaction
    @Override
    public void updaterNsfwSetting(NsfwFilterSettingDto request) {
        NsfwFilterSetting setting = NsfwFilterSetting.getSetting();
        setting.setAdult(request.getAdult());
        setting.setMedical(request.getMedical());
        setting.setRacy(request.getRacy());
        setting.setSpoof(request.getSpoof());
        setting.setViolence(request.getViolence());
        setting.saveIt();
    }

    @DbOpenWithTransaction
    @Override
    public NsfwFilterSettingDto getNsfwSetting() {
        NsfwFilterSetting setting = NsfwFilterSetting.getSetting();
        NsfwFilterSettingDto response = new NsfwFilterSettingDto();
        response.setAdult(setting.getAdult());
        response.setMedical(setting.getMedical());
        response.setRacy(setting.getRacy());
        response.setSpoof(setting.getSpoof());
        response.setViolence(setting.getViolence());
        return response;
    }

    @DbOpenWithTransaction
    @Override
    public PictureResponse findPictureById(String id) {
        Picture picture = Picture.findById(id);
        if (picture == null) {
            return null;
        }
        return Mapper.transform(picture);
    }

    @DbOpenWithTransaction
    @Override
    public List<PictureResponse> findPictureByQuery(PictureSearchRequest request) {
        PictureSqlBuilder query = new PictureSqlBuilder();
        if (!StringUtils.isBlank(request.getLabel())) {
            query.where(Criteria.LABEL, request.getLabel());
        }
        if (!StringUtils.isBlank(request.getOwner())) {
            query.where(Criteria.OWNER, request.getOwner());
        }
        query.limit(request.getLimit());
        query.offset(request.getOffset());
        LazyList<Picture> pictures = findBySqlQueryWithFilter(query.build());
        return pictures.stream().peek(System.out::println).map(Mapper::transform).collect(Collectors.toList());
    }

    @DbOpenWithTransaction
    @Override
    public String savePicture(AnnotateImageResponse response, Photo photo) {
        Owner owner = saveOwner(photo);
        String pictureId = photo.getId();
        Picture picture = Picture.findById(pictureId);
        if (isNull(picture)) {
            picture = new Picture();
            picture.setId(pictureId);
            picture.setUrl(photo.getUrl());
            picture.insert();
            owner.addPicture(picture);
            owner.saveIt();
            List<EntityAnnotation> labelAnnotationsList = response.getLabelAnnotationsList();
            for (EntityAnnotation entityAnnotation : labelAnnotationsList) {
                picture.addLabel(saveLabelAnnotation(entityAnnotation));
            }
            SafeAnnotation safeAnnotation = saveSafeSearchAnnotation(response);
            picture.setSafeAnnotation(safeAnnotation);
            picture.saveIt();
            return pictureId;
        }
        return null;
    }

    private Owner saveOwner(Photo photo) {
        String ownerId = photo.getOwner().getId();
        Owner owner = Owner.findById(ownerId);
        if (isNull(owner)) {
            owner = new Owner();
            owner.setId(ownerId);
            owner.setUserName(photo.getOwner().getUsername());
            owner.insert();
        }
        return owner;
    }

    private SafeAnnotation saveSafeSearchAnnotation(AnnotateImageResponse response) {
        SafeSearchAnnotation safeSearchAnnotation = response.getSafeSearchAnnotation();
        SafeAnnotation safeAnnotation = new SafeAnnotation();
        safeAnnotation.setAdult(safeSearchAnnotation.getAdultValue());
        safeAnnotation.setMedical(safeSearchAnnotation.getMedicalValue());
        safeAnnotation.setRacy(safeSearchAnnotation.getRacyValue());
        safeAnnotation.setSpoof(safeSearchAnnotation.getSpoofValue());
        safeAnnotation.setViolence(safeSearchAnnotation.getViolenceValue());
        safeAnnotation.insert();
        return safeAnnotation;
    }

    private LabelAnnotation saveLabelAnnotation(EntityAnnotation entityAnnotation) {
        LabelAnnotation labelAnnotation = new LabelAnnotation();
        labelAnnotation.setName(entityAnnotation.getDescription());
        labelAnnotation.setScore(entityAnnotation.getScore());
        labelAnnotation.insert();
        return labelAnnotation;
    }

    private LazyList<Picture> findBySqlQueryWithFilter(String sql) {
        NsfwFilterSetting s = NsfwFilterSetting.getSetting();
        return Picture.findBySQL(sql, s.getAdult(), s.getMedical(), s.getRacy(), s.getSpoof(), s.getViolence());
    }
}
