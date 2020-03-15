package com.banana.service;

import com.banana.service.I.IFlickrService;
import com.banana.service.I.IVisionService;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.Size;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.protobuf.ByteString;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class VisionService implements IVisionService {

    private static final Logger LOGGER = LogManager.getLogger(VisionService.class.getName());
    private final IFlickrService flickrService;

    @Inject
    public VisionService(IFlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @Override
    public List<AnnotateImageResponse> vision(List<Photo> photos) throws IOException, FlickrException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        for (Photo photo : photos) {
            AnnotateImageRequest annotateImageRequest = getAnnotateImageRequest(photo);
            requests.add(annotateImageRequest);
        }
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create(ImageAnnotatorSettings.newBuilder().build())) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    LOGGER.error(res.getError().getMessage());
                    return responses;
                }
                return responses;
            }
        }
        return null;
    }

    private AnnotateImageRequest getAnnotateImageRequest(Photo photo) throws IOException, FlickrException {
        InputStream stream = flickrService.getPhotosInterface().getImageAsStream(photo, Size.MEDIUM);
        ByteString imgBytes = ByteString.readFrom(stream);
        Image img = Image.newBuilder().setContent(imgBytes).build();
        return getAnnotateImageRequest(img);
    }

    private AnnotateImageRequest getAnnotateImageRequest(Image image) {
        Feature featLogo = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
        Feature featSafe = Feature.newBuilder().setType(Type.SAFE_SEARCH_DETECTION).build();
        List<Feature> features = new ArrayList<>();
        features.add(featSafe);
        features.add(featLogo);
        return AnnotateImageRequest.newBuilder()
                .addAllFeatures(features)
                .setImage(image)
                .build();
    }
}
