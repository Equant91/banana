package com.banana.service;

import com.google.cloud.vision.v1.*;
import com.google.inject.Singleton;
import com.google.protobuf.ByteString;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Singleton
public class VisionService {
    private static final Logger LOGGER = LogManager.getLogger(VisionService.class.getName());

    public static List<AnnotateImageResponse> vision(String filePath, PrintStream out) throws Exception, IOException {


        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);
        ByteString imgBytes = ByteString.copyFrom(data);

        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature featLogo = Feature.newBuilder().setType(Feature.Type.LOGO_DETECTION).build();
        Feature featSafe = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addAllFeatures(Arrays.asList(featLogo, featSafe))
                .setImage(img)
                .build();
        requests.add(request);
        LOGGER.debug("Create request: " + request);
        // ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
//
//        Image img = Image.newBuilder().setContent(imgBytes).build();
//        Feature feat = Feature.newBuilder().setType(Feature.Type.LOGO_DETECTION).build();
//        AnnotateImageRequest request =
//                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
//        requests.add(request);


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
