package com.banana.repository.I;

import com.banana.api.dto.NsfwFilterSettingDto;
import com.banana.api.request.PictureSearchRequest;
import com.banana.api.response.OwnerResponse;
import com.banana.api.response.PictureResponse;
import com.flickr4java.flickr.photos.Photo;
import com.google.cloud.vision.v1.AnnotateImageResponse;

import java.util.List;

public interface IPictureRepository {

    List<String> findAllLabelOrderByCount();

    List<OwnerResponse> findAllOwner();

    void updaterNsfwSetting(NsfwFilterSettingDto request);

    NsfwFilterSettingDto getNsfwSetting();

    PictureResponse findPictureById(String id);

    List<PictureResponse> findPictureByQuery(PictureSearchRequest request);

    String savePicture(AnnotateImageResponse response, Photo photo);

}
