package com.banana.service;

import com.banana.model.Picture;
import com.banana.repository.PictureRepository;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.util.List;

@Singleton
public class BananaService {
    private final FlickrService flickrService;
    private final VisionService visionService;
    private final PictureRepository pictureRepository;

    @Inject
    public BananaService(FlickrService flickrService, VisionService visionService, PictureRepository pictureRepository) {
        this.flickrService = flickrService;
        this.visionService = visionService;
        this.pictureRepository = pictureRepository;
    }

    public void loadInfoBySearch(String search, int perPage, int page) throws FlickrException, IOException {
        PhotoList<Photo> photos = flickrService.search(search, perPage, page);
        List<AnnotateImageResponse> imageResponses = visionService.vision(photos);
        pictureRepository.savePictures(imageResponses, photos);
    }

    public List<String> findAllLabel() {
        return pictureRepository.findAllLabelOrderByCount();
    }

  public List<Picture> findPictureByLabel(String label) {
        return pictureRepository.findPictureByLabel(label);
    }

}
