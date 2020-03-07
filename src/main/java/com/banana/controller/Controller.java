package com.banana.controller;

import com.banana.service.VisionService;
import com.beerboy.spark.typify.route.GsonRoute;
import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.descriptor.ParameterDescriptor;
import com.beerboy.ss.rest.Endpoint;
import com.google.common.graph.Network;
import com.google.inject.Inject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import static com.beerboy.ss.descriptor.MethodDescriptor.path;
import static com.beerboy.ss.rest.RestResponse.ok;

public class Controller implements Endpoint {
    private static final Logger LOGGER = LogManager.getLogger(Controller.class.getName());

    private final VisionService visionService;

    @Inject
    public Controller(VisionService visionService) {
        this.visionService = visionService;
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
                            visionService.vision("/home/equant/Изображения/" + name, System.out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return ok(response, "Ok");
                    }
                });
    }
}
