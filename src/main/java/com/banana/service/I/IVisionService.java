package com.banana.service.I;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.google.cloud.vision.v1.AnnotateImageResponse;

import java.io.IOException;
import java.util.List;

public interface IVisionService {
    List<AnnotateImageResponse> vision(List<Photo> photos) throws IOException, FlickrException;

}
