package com.banana;

import com.banana.config.GuiceConfig;
import com.banana.controller.Controller;
import com.banana.repository.PictureRepository;
import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import com.flickr4java.flickr.FlickrException;
import com.google.inject.Guice;
import com.google.inject.Inject;
import spark.Service;

import java.io.IOException;
import java.util.Arrays;


public class Application {

    public static void main(String[] args) throws FlickrException, IOException {
        PictureRepository pictureRepository = new PictureRepository();
        pictureRepository.getDbConnection();
        Controller controller = Guice.createInjector(new GuiceConfig())
                .getInstance(Controller.class);
        Service spark = Service.ignite().port(8080);
        SparkSwagger.of(spark, "spark-swagger.conf").endpoints(() -> Arrays.asList(controller)).generateDoc();

    }


}
