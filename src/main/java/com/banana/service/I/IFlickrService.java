package com.banana.service.I;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;

import java.io.IOException;

public interface IFlickrService {

    PhotoList<Photo> findByUserId(String userId, int perPage, int page) throws FlickrException, IOException;

    PhotoList<Photo> findByQuery(String query, int perPage, int page) throws FlickrException;

    PhotosInterface getPhotosInterface();
}
