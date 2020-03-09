package com.banana.service;

import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.inject.Singleton;
import com.google.protobuf.ByteString;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class VisionService {
    private static final Logger LOGGER = LogManager.getLogger(VisionService.class.getName());

    public static List<AnnotateImageResponse> vision(String filePath, PrintStream out) throws Exception, IOException {
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
        Image img = Image.newBuilder().setContent(imgBytes).build();
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Feature featLogo = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
        Feature featSafe = Feature.newBuilder().setType(Type.SAFE_SEARCH_DETECTION).build();
        List<Feature> features = new ArrayList<>();
        features.add(featSafe);
        features.add(featLogo);
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addAllFeatures(features)
                .setImage(img)
                .build();
        requests.add(request);
        LOGGER.debug("Create request: " + request);
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create(ImageAnnotatorSettings.newBuilder().build())) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            return responses;
//            for (AnnotateImageResponse res : responses) {
//                if (res.hasError()) {
//                    out.printf("Error: %s\n", res.getError().getMessage());
//                    return;
//                }
////
//                // For full list of available annotations, see http://g.co/cloud/vision/docs
//                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//                    out.printf("Text: %s\n", annotation.getDescription());
//                    out.printf("Position : %s\n", annotation.getBoundingPoly());
//                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
