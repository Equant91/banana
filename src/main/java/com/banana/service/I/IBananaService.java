package com.banana.service.I;

import com.banana.api.dto.NsfwFilterSettingDto;
import com.banana.api.request.PictureSearchRequest;
import com.banana.api.response.OwnerResponse;
import com.banana.api.response.PictureResponse;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.google.cloud.vision.v1.AnnotateImageResponse;

import java.io.IOException;
import java.util.List;

public interface IBananaService {

    List<String> loadInfoBySearch(String search, int perPage, int page) throws FlickrException, IOException;

    List<String> loadInfoByOwner(String userId, int perPage, int page) throws FlickrException, IOException;

    List<String> findAllLabel();

    List<OwnerResponse> findAllOwner();

    PictureResponse findById(String id);

    List<PictureResponse> findPictureByQuery(PictureSearchRequest request);

    void updaterNsfwSetting(NsfwFilterSettingDto request);

    NsfwFilterSettingDto getNsfwSetting();

    List<String> savePictures(List<AnnotateImageResponse> annotateImageResponses, List<Photo> photos);
}
