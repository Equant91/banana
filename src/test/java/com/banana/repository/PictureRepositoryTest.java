package com.banana.repository;

import com.banana.api.dto.NsfwFilterSettingDto;
import com.banana.api.request.PictureSearchRequest;
import com.banana.api.response.OwnerResponse;
import com.banana.api.response.PictureResponse;
import com.banana.model.Picture;
import com.flickr4java.flickr.photos.Photo;
import com.google.cloud.vision.v1.AnnotateImageResponse;

import com.google.gson.Gson;
import org.javalite.activejdbc.Base;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class PictureRepositoryTest {
    @Rule
    public PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:10");

    @AfterEach
    void tearDown() {
        Base.close();
    }

    @BeforeEach
    public void setUp() {
        postgres.start();
        System.out.println("111111111111111111111111111111111111111111111111111------" + postgres.getFirstMappedPort() + postgres.getUsername() + postgres.getPassword());
        try {
            String content = Files.readString(Paths.get("src/test/java/com/banana/repository/test.sql"), StandardCharsets.UTF_8);
            Base.open("org.postgresql.Driver", "jdbc:postgresql://localhost:" + postgres.getFirstMappedPort() + "/postgres", "test", "test");
            Base.exec(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final PictureRepository pictureRepository;

    PictureRepositoryTest() {
        this.pictureRepository = new PictureRepository();
    }

    @Test
    void findAllLabelOrderByCount() {
        List<String> allLabelOrderByCount = pictureRepository.findAllLabelOrderByCount();
        assertEquals(14, allLabelOrderByCount.size());
    }

    @Test
    void findAllOwner() {
        List<OwnerResponse> allOwner = pictureRepository.findAllOwner();
        assertEquals(2, allOwner.size());
    }

    @Test
    void updaterNsfwSetting() {
        NsfwFilterSettingDto request = new NsfwFilterSettingDto();
        request.setAdult(1);
        request.setMedical(1);
        request.setRacy(1);
        request.setSpoof(1);
        request.setViolence(1);
        pictureRepository.updaterNsfwSetting(request);
        NsfwFilterSettingDto response = pictureRepository.getNsfwSetting();
        assertEquals(request.getAdult(), response.getAdult());
        assertEquals(request.getMedical(), response.getMedical());
        assertEquals(request.getRacy(), response.getRacy());
        assertEquals(request.getSpoof(), response.getSpoof());
        assertEquals(request.getViolence(), response.getViolence());
    }

    @Test
    void findPictureById() {
        PictureResponse pictureById = pictureRepository.findPictureById("49659426712");
        assertEquals("https://flickr.com/photos/25034321@N05/49659426712", pictureById.getUrl());
    }

    @Test
    void findPictureByQuery() {
        PictureSearchRequest request = new PictureSearchRequest(null , "Cat" ,null, null);
        List<PictureResponse> pictures = pictureRepository.findPictureByQuery(request);
        assertEquals(2,pictures.size());
    }

    @Test
    void savePicture() {
        AnnotateImageResponse annotateImageResponse = new Gson().fromJson(TestUtils.ANNOTATE_IMAGE_RESPONSE_JSON, AnnotateImageResponse.class);
        Photo photo = new Gson().fromJson(TestUtils.PHOTO_JSON, Photo.class);
        String id = photo.getId();
        String responseId = pictureRepository.savePicture(annotateImageResponse, photo);
        assertEquals(id, responseId);
        assertNotNull(pictureRepository.findPictureById(id));
    }
}