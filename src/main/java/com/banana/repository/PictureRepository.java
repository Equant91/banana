package com.banana.repository;

import com.banana.annotation.DbOpenWithTransaction;
import com.banana.model.LabelAnnotation;
import com.banana.model.Owner;
import com.banana.model.Picture;
import com.banana.model.SafeAnnotation;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.google.inject.Singleton;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Singleton
public class PictureRepository {
public static final String FIND_ALL_LABEL_ORDER_BY_COUNT = "Select label_annotation.name from label_annotation group by name order by count(*) desc";
public static final String FIND_ALL_BY_LABEL = "Select * from picture inner join label_annotation as la on picture.id = la.picture_id and la.name = ?";

    public void savePictures(List<AnnotateImageResponse> annotateImageResponses, PhotoList<Photo> photos) {
        for (int x = 0; x < annotateImageResponses.size(); x++)
            savePicture(annotateImageResponses.get(x), photos.get(x));
    }

    @DbOpenWithTransaction
    public List<String> findAllLabelOrderByCount(){
        LabelAnnotation.findAll().collectDistinct("name");
        LazyList<LabelAnnotation> labels = LabelAnnotation.findBySQL(FIND_ALL_LABEL_ORDER_BY_COUNT);
        return labels.stream().map(labelAnnotation -> labelAnnotation.getName()).collect(Collectors.toList());
    }

    @DbOpenWithTransaction
    public List<Picture> findPictureByLabel(String label){
        LazyList<Picture> pictures =  Picture.findBySQL(FIND_ALL_BY_LABEL, label);
        return pictures;
    }

    @DbOpenWithTransaction
    private void savePicture(AnnotateImageResponse response, Photo photo) {
        Owner owner = saveOwner(photo);
        Picture picture = savePicture(photo);
        owner.addPicture(picture);
        owner.saveIt();

        List<EntityAnnotation> labelAnnotationsList = response.getLabelAnnotationsList();
        for (EntityAnnotation entityAnnotation : labelAnnotationsList) {
            picture.addLabel(saveLabelAnnotation(entityAnnotation));
        }
        SafeAnnotation safeAnnotation = saveSafeSearchAnnotation(response);
        picture.setSafeAnnotation(safeAnnotation);
        picture.saveIt();
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

    private Picture savePicture(Photo photo) {
        String pictureId = photo.getId();
        Picture picture = Picture.findById(pictureId);
        if (isNull(picture)) {
            picture = new Picture();
            picture.setId(pictureId);
            picture.setUrl(photo.getUrl());
            picture.insert();

        }
        return picture;
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
}
