package com.banana.service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class FlickrService {
    @Inject
    Flickr flickr;

    public Photo flickr() throws FlickrException {


        PhotosInterface photos = flickr.getPhotosInterface();
        SearchParameters params = new SearchParameters();
        params.setMedia("photos"); // One of "photos", "videos" or "all"
        params.setExtras(Stream.of("media").collect(Collectors.toSet()));
        params.setText("dog");
        //params.setUserId();
        PhotoList<Photo> results = photos.search(params, 0, 0);
        results.get(0);


        return results.get(0);

    }

    public void findByUserId(String userId) {

    }
}
