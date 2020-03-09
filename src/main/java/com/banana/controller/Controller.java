package com.banana.controller;

import com.banana.service.FlickrService;
import com.banana.service.VisionService;
import com.beerboy.spark.typify.route.GsonRoute;
import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.google.api.gax.rpc.NotFoundException;
import com.google.common.graph.Network;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

import java.io.IOException;

import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import static com.beerboy.ss.descriptor.MethodDescriptor.path;
import static com.beerboy.ss.rest.RestResponse.ok;

public class Controller implements Endpoint {
    private static final Logger LOGGER = LogManager.getLogger(Controller.class.getName());

    private final VisionService visionService;
    private final FlickrService flickrService;

    @Inject
    public Controller(VisionService visionService, FlickrService flickrService) {
        this.visionService = visionService;
        this.flickrService = flickrService;
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath("")
                .withDescription("Hammer REST API exposing all Thor utilities "), (q, a) -> LOGGER.info("Received request for Hammer Rest API"))
                .get(path("/export")
                        .withDescription("Gets the whole Network")
                        .withQueryParam().withName("name").and()
                        .withResponseType(Network.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        String name = request.queryParams("name");
                        try {
                            visionService.vision("/home/alexey/Изображения" + name, System.out);
                        } catch (Exception e) {
                            e.printStackTrace();  //////////////////////////////////////////////////////////////////////////////////////////////////////////
                        }
                        return ok(response, "Ok");
                    }
                });

        sparkSwagger.endpoint(endpointPath("")
                .withDescription("Hammer REST API exposing all Thor utilities "), (q, a) -> LOGGER.info("Received request for Hammer Rest API"))
                .get(path("/flick")
                        .withDescription("Gets the whole Network")
                        .withQueryParam().withName("name").and()
                        .withResponseType(Network.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        String name = request.queryParams("name");
                        Photo flickr = null;
                        try {
                            flickr = flickrService.flickr();
                            if(true)
                            throw new FlickrException("asadfadsfadfadf");

                        } catch (FlickrException | IOException e) {
                           throw new RuntimeException(e);
                        }

                        return new Gson().toJsonTree(flickr);
                    }
                });
        sparkSwagger.exception(Exception.class, (e, request, response) -> {
            response.status(400);
            response.body(e.getMessage());
        });

    }
}