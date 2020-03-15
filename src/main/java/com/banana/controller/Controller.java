package com.banana.controller;

import com.banana.api.dto.NsfwFilterSettingDto;
import com.banana.api.request.PictureSearchRequest;
import com.banana.api.response.OwnerResponse;
import com.banana.api.response.PictureResponse;
import com.banana.service.BananaService;
import com.banana.service.I.IBananaService;
import com.beerboy.spark.typify.route.GsonRoute;
import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import com.beerboy.ss.rest.RestResponse;
import com.flickr4java.flickr.FlickrException;
import com.google.common.graph.Network;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.List;

import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import static com.beerboy.ss.descriptor.MethodDescriptor.path;
import static com.beerboy.ss.rest.RestResponse.ok;

@Singleton
public class Controller implements Endpoint {
    private static final Logger LOGGER = LogManager.getLogger(Controller.class.getName());

    private final IBananaService bananaService;

    @Inject
    public Controller(IBananaService bananaService) {
        this.bananaService = bananaService;
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath("")
                .withDescription("Controller"), (req, res) -> LOGGER.info(req.requestMethod() + ": " + req.uri()))
                .post(path("/visionByQuery")
                        .withDescription("Поиск изображений по тегу на Flickr и их обработка")
                        .withQueryParam().withName("label").withRequired(true).and()
                        .withQueryParam().withName("perPage").withRequired(true).and()
                        .withQueryParam().withName("page").withRequired(true).and()
                        .withResponseType(Network.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        String label = request.queryParams("label");
                        int perPage = Integer.parseInt(request.queryParams("perPage"));
                        int page = Integer.parseInt(request.queryParams("page"));
                        List<String> ids;
                        try {
                            ids = bananaService.loadInfoBySearch(label, perPage, page);
                        } catch (FlickrException | IOException e) {
                            throw new RuntimeException(e);
                        }
                        return ok(response, new Gson().toJsonTree(ids));
                    }
                })

                .post(path("/visionByOwnerId")
                        .withDescription("Поиск изображений по автору на Flickr и их обработка")
                        .withQueryParam().withName("id").withRequired(true).and()
                        .withQueryParam().withName("perPage").withRequired(true).and()
                        .withQueryParam().withName("page").withRequired(true).and()
                        .withResponseType(Network.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        String id = request.queryParams("id");
                        int perPage = Integer.parseInt(request.queryParams("perPage"));
                        int page = Integer.parseInt(request.queryParams("page"));
                        List<String> ids;
                        try {
                            ids = bananaService.loadInfoByOwner(id, perPage, page);
                        } catch (FlickrException | IOException e) {
                            throw new RuntimeException(e);
                        }

                        return ok(response, new Gson().toJsonTree(ids));
                    }
                })

                .get(path("/label")
                        .withDescription("Вывод списка label")
                        .withResponseType(String[].class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        List<String> allLabel = bananaService.findAllLabel();
                        return ok(response, new Gson().toJsonTree(allLabel));
                    }
                })

                .get(path("/owner")
                        .withDescription("Вывод списка owner")
                        .withResponseType(OwnerResponse.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        List<OwnerResponse> allOwner = bananaService.findAllOwner();
                        return ok(response, new Gson().toJsonTree(allOwner));
                    }
                })

                .get(path("/picture")
                        .withDescription("Поиск изображений")
                        .withQueryParam().withName("label").and()
                        .withQueryParam().withName("owner").and()
                        .withQueryParam().withName("limit").and()
                        .withQueryParam().withName("offset").and()
                        .withResponseType(PictureResponse[].class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        List<PictureResponse> pictures = bananaService.findPictureByQuery(getPictureSearchRequest(request));
                        return ok(response, new Gson().toJsonTree(pictures));
                    }

                })

                .get(path("/picture/:id")
                        .withDescription("Поиск изображений")
                        .withPathParam().withName("id").and()
                        .withResponseType(PictureResponse.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        PictureResponse picture = bananaService.findById(request.params("id"));
                        return ok(response, new Gson().toJsonTree(picture));
                    }
                })

                .post(path("/nsfwSetting")
                        .withDescription("Поиск изображений")
                        .withQueryParam().withName("adult").withRequired(true).and()
                        .withQueryParam().withName("medical").withRequired(true).and()
                        .withQueryParam().withName("racy").withRequired(true).and()
                        .withQueryParam().withName("spoof").withRequired(true).and()
                        .withQueryParam().withName("violence").withRequired(true).and()
                        .withResponseType(RestResponse.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        bananaService.updaterNsfwSetting(getNsfwFilterSettingDto(request));
                        return ok(response);
                    }
                })
                .get(path("/nsfwSetting")
                        .withResponseType(NsfwFilterSettingDto.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        NsfwFilterSettingDto nsfwSetting = bananaService.getNsfwSetting();
                        return ok(response, new Gson().toJsonTree(nsfwSetting));
                    }
                });

        sparkSwagger.exception(Exception.class, (e, request, response) ->

        {
            response.status(400);
            LOGGER.error(e.getMessage(), e);
            response.body(e.getClass().getSimpleName() + ": " + e.getMessage());
        });
    }

    private PictureSearchRequest getPictureSearchRequest(Request request) {
        String label = request.queryParams("label");
        String owner = request.queryParams("owner");
        String limit = request.queryParams("limit");
        String offset = request.queryParams("offset");
        return new PictureSearchRequest(owner, label, offset, limit);
    }

    private NsfwFilterSettingDto getNsfwFilterSettingDto(Request request) {
        NsfwFilterSettingDto nsfwFilterSettingDto = new NsfwFilterSettingDto();
        nsfwFilterSettingDto.setAdult(Integer.valueOf(request.queryParams("adult")));
        nsfwFilterSettingDto.setMedical(Integer.valueOf(request.queryParams("medical")));
        nsfwFilterSettingDto.setRacy(Integer.valueOf(request.queryParams("racy")));
        nsfwFilterSettingDto.setSpoof(Integer.valueOf(request.queryParams("spoof")));
        nsfwFilterSettingDto.setViolence(Integer.valueOf(request.queryParams("violence")));
        return nsfwFilterSettingDto;
    }
}
