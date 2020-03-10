package com.banana.controller;

import com.banana.model.Picture;
import com.banana.service.BananaService;
import com.banana.service.FlickrService;
import com.banana.service.VisionService;
import com.beerboy.spark.typify.route.GsonRoute;
import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import com.flickr4java.flickr.FlickrException;
import com.google.common.graph.Network;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.List;

import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import static com.beerboy.ss.descriptor.MethodDescriptor.path;
import static com.beerboy.ss.rest.RestResponse.ok;

public class Controller implements Endpoint {
    private static final Logger LOGGER = LogManager.getLogger(Controller.class.getName());

    private final VisionService visionService;
    private final FlickrService flickrService;
    private final BananaService bananaService;

    @Inject
    public Controller(VisionService visionService, FlickrService flickrService, BananaService bananaService) {
        this.visionService = visionService;
        this.flickrService = flickrService;
        this.bananaService = bananaService;
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath("")
                .withDescription("Поиск изображений"), (req, res) -> LOGGER.info(req.requestMethod() + ": " + req.uri()))
                .get(path("/asdasdasdad")
                        .withDescription("Gets the whole Network")
                        .withQueryParam().withName("name").and()
                        .withResponseType(Network.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        String name = request.queryParams("name");
                        try {
                            //  visionService.vision("/home/alexey/Изображения" + name, System.out);
                        } catch (Exception e) {
                            e.printStackTrace();  //////////////////////////////////////////////////////////////////////////////////////////////////////////
                        }
                        return ok(response, "Ok");
                    }
                });

        sparkSwagger.endpoint(endpointPath("")
                .withDescription("vision"), (req, res) -> LOGGER.info(req.requestMethod() + ": " + req.uri()))
                .get(path("/vision")
                        .withDescription("Gets the whole Network")
                        .withQueryParam().withName("name").and()
                        .withQueryParam().withName("perPage").and()
                        .withQueryParam().withName("page").and()
                        .withResponseType(Network.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        String name = request.queryParams("name");
                        Integer perPage = Integer.valueOf(request.queryParams("perPage"));
                        Integer page = Integer.valueOf(request.queryParams("page"));
                        try {
                            bananaService.loadInfoBySearch(name, perPage, page);
                        } catch (FlickrException | IOException e) {
                            throw new RuntimeException(e);
                        }

                        return ok(response);//new
                    }
                });
        sparkSwagger.exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getClass().getSimpleName() + ": " + e.getMessage());
        });


        sparkSwagger.endpoint(endpointPath("")
                .withDescription("label"), (req, res) -> LOGGER.info(req.requestMethod() + ": " + req.uri()))
                .get(path("/label")
                        .withDescription("Gets the whole Network")
                        .withResponseType(Network.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        String name = request.queryParams("name");

                        List<String> allLabel = bananaService.findAllLabel();
                        return ok(response, new Gson().toJsonTree(allLabel));
                    }
                });

        sparkSwagger.endpoint(endpointPath("")
                .withDescription("Поиск изображений по Label"), (req, res) -> LOGGER.info(req.requestMethod() + ": " + req.uri()))
                .get(path("/picture")
                        .withDescription("Gets the whole Network")
                        .withQueryParam().withName("label").and()
                        .withResponseType(Network.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        String label = request.queryParams("label");

                        List<Picture> pictures = bananaService.findPictureByLabel(label);
                        return ok(response, new Gson().toJsonTree(pictures));
                    }
                });

    }
}