package com.banana.service;

import com.banana.api.dto.NsfwFilterSettingDto;
import com.banana.api.request.PictureSearchRequest;
import com.banana.api.response.OwnerResponse;
import com.banana.api.response.PictureResponse;
import com.banana.repository.I.IPictureRepository;
import com.banana.repository.PictureRepository;
import com.banana.service.I.IBananaService;
import com.banana.service.I.IFlickrService;
import com.banana.service.I.IVisionService;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class BananaService implements IBananaService {
    private final IFlickrService flickrService;
    private final IVisionService visionService;
    private final IPictureRepository pictureRepository;

    @Inject
    public BananaService(IFlickrService flickrService, IVisionService visionService, IPictureRepository pictureRepository) {
        this.flickrService = flickrService;
        this.visionService = visionService;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public List<String> loadInfoBySearch(String search, int perPage, int page) throws FlickrException, IOException {
        PhotoList<Photo> photos = flickrService.findByQuery(search, perPage, page);
        return visionAndSave(photos);
    }

    @Override
    public List<String> loadInfoByOwner(String userId, int perPage, int page) throws FlickrException, IOException {
        PhotoList<Photo> photos = flickrService.findByUserId(userId, perPage, page);
        return visionAndSave(photos);
    }

    private List<String> visionAndSave(List<Photo> photos) throws IOException, FlickrException {
        List<Photo> photosFiltred = filterSaved(photos);
        if (photosFiltred == null || photos.isEmpty()) {
            throw new RuntimeException("All photo already exist");
        }
        List<AnnotateImageResponse> imageResponses = visionService.vision(photosFiltred);
        return savePictures(imageResponses, photosFiltred);
    }


    public List<String> findAllLabel() {
        return pictureRepository.findAllLabelOrderByCount();

    }

    public List<OwnerResponse> findAllOwner() {
        return pictureRepository.findAllOwner();
    }

    public PictureResponse findById(String id) {
        return pictureRepository.findPictureById(id);
    }

    public List<PictureResponse> findPictureByQuery(PictureSearchRequest request) {
        return pictureRepository.findPictureByQuery(request);
    }

    public void updaterNsfwSetting(NsfwFilterSettingDto request) {
        pictureRepository.updaterNsfwSetting(request);
    }

    public NsfwFilterSettingDto getNsfwSetting() {
        return pictureRepository.getNsfwSetting();
    }

    public List<String> savePictures(List<AnnotateImageResponse> annotateImageResponses, List<Photo> photos) {
        List<String> ids = new ArrayList<>();
        for (int x = 0; x < annotateImageResponses.size(); x++) {
            String id = pictureRepository.savePicture(annotateImageResponses.get(x), photos.get(x));
            if (id != null) {
                ids.add(id);
            }
        }
        return ids;
    }

    private List<Photo> filterSaved(List<Photo> photos) {
        return photos.stream().filter(photo -> pictureRepository.findPictureById(photo.getId()) == null).collect(Collectors.toList());
    }
}
