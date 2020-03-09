package com.banana.service;

import com.banana.repository.PictureRepository;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class FlickrService {

    private final Flickr flickr;;

    @Inject
    public FlickrService(Flickr flickr) {
        this.flickr = flickr;

    }


    public Photo flickr() throws FlickrException, IOException {
        PhotosInterface photos = flickr.getPhotosInterface();
        SearchParameters params = new SearchParameters();
        params.setMedia("photos"); // One of "photos", "videos" or "all"
        params.setExtras(Stream.of("media").collect(Collectors.toSet()));
        params.setText("dog");
        //params.setUserId();
        PhotoList<Photo> results = photos.search(params, 0, 0);
        Photo photo = results.get(1);
        Size mediumSize = photo.getMediumSize();
        InputStream stream = photos.getImageAsStream(photo, mediumSize.getLabel());


//        photo = results.get(4);
//        BufferedImage photo1 = photos.getImage(photo, Size.MEDIUM);
//        File outputfile = new File("image.jpg");
//        ImageIO.write(photo1, "jpg", outputfile);

        return results.get(0);

    }

    public PhotoList<Photo> findByUserId(String userId, int perPage, int page) throws FlickrException, IOException {
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        SearchParameters params = getSearchParams();
        params.setUserId(userId);
        return photosInterface.search(params, perPage, page);

    }

    public PhotoList<Photo> search(String search, int perPage, int page) throws FlickrException {
        PhotosInterface photos = flickr.getPhotosInterface();
        SearchParameters params = getSearchParams();
        params.setText(search);
        return photos.search(params, perPage, page);
    }

    private SearchParameters getSearchParams() throws FlickrException {
        SearchParameters params = new SearchParameters();
        params.setMedia("photos");
        params.setExtras(Stream.of("media").collect(Collectors.toSet()));
        return params;
    }

}
