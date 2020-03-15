package com.banana.service;

import com.banana.service.I.IFlickrService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class FlickrService implements IFlickrService {

    private final Flickr flickr;

    @Inject
    public FlickrService(Flickr flickr) {
        this.flickr = flickr;
    }

    @Override
    public PhotoList<Photo> findByUserId(String userId, int perPage, int page) throws FlickrException, IOException {
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        SearchParameters params = getSearchParams();
        params.setUserId(userId);
        return photosInterface.search(params, perPage, page);
    }

    @Override
    public PhotoList<Photo> findByQuery(String query, int perPage, int page) throws FlickrException {
        PhotosInterface photos = flickr.getPhotosInterface();
        SearchParameters params = getSearchParams();
        params.setText(query);
        return photos.search(params, perPage, page);
    }

    private SearchParameters getSearchParams() throws FlickrException {
        SearchParameters params = new SearchParameters();
        params.setMedia("photos");
        params.setExtras(Stream.of("media").collect(Collectors.toSet()));
        return params;
    }

    public PhotosInterface getPhotosInterface() {
        return flickr.getPhotosInterface();
    }
}
