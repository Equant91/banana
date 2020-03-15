package com.banana.mapper;

import com.banana.api.response.LabelAnnotationResponse;
import com.banana.api.response.OwnerResponse;
import com.banana.api.response.PictureResponse;
import com.banana.api.response.SafeAnnotationResponse;
import com.banana.model.LabelAnnotation;
import com.banana.model.Owner;
import com.banana.model.Picture;
import com.banana.model.SafeAnnotation;
import org.javalite.activejdbc.LazyList;

import java.util.stream.Collectors;

public class Mapper {

    public static PictureResponse transform(Picture picture) {
        PictureResponse pictureResponse = new PictureResponse();
        pictureResponse.setId(picture.getId().toString());
        pictureResponse.setUrl(picture.getUrl());
        LazyList<LabelAnnotation> all = picture.getAll(LabelAnnotation.class);
        pictureResponse.setOwnerResponse(transform(picture.getOwner()));
        pictureResponse.setLabelAnnotationResponses(picture.getAll(LabelAnnotation.class)
                .stream()
                .map(Mapper::transform)
                .collect(Collectors.toList()));
        pictureResponse.setSafeAnnotationResponse(transform(picture.getSafeAnnotation()));
        return pictureResponse;
    }

    public static LabelAnnotationResponse transform(LabelAnnotation labelAnnotation) {
        LabelAnnotationResponse labelAnnotationResponse = new LabelAnnotationResponse();
        labelAnnotationResponse.setName(labelAnnotation.getName());
        labelAnnotationResponse.setScore(labelAnnotation.getScore());
        return labelAnnotationResponse;
    }

    public static SafeAnnotationResponse transform(SafeAnnotation safeAnnotation) {
        SafeAnnotationResponse safeAnnotationResponse = new SafeAnnotationResponse();
        safeAnnotationResponse.setAdult(safeAnnotation.getAdult());
        safeAnnotationResponse.setMedical(safeAnnotation.getMedical());
        safeAnnotationResponse.setRacy(safeAnnotation.getRacy());
        safeAnnotationResponse.setSpoof(safeAnnotation.getSpoof());
        safeAnnotationResponse.setViolence(safeAnnotation.getViolence());
        return safeAnnotationResponse;
    }

    public static OwnerResponse transform(Owner owner) {
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setOwnerName(owner.getUserName());
        ownerResponse.setOwnerId(owner.getId().toString());
        return ownerResponse;
    }

}
