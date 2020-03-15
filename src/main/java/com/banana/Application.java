package com.banana;

import com.banana.config.GuiceConfig;
import com.banana.controller.Controller;
import com.beerboy.ss.SparkSwagger;
import com.flickr4java.flickr.FlickrException;
import com.google.inject.Guice;
import spark.Service;

import java.io.IOException;
import java.util.Arrays;


public class Application {

    public static void main(String[] args) throws FlickrException, IOException {
        Controller controller = Guice.createInjector(new GuiceConfig())
                .getInstance(Controller.class);
        Service spark = Service.ignite().port(8080);

        SparkSwagger.of(spark, "spark-swagger.conf").endpoints(() -> Arrays.asList(controller)).generateDoc();
    }
}
