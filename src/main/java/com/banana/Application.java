package com.banana;

import com.banana.config.GuiceConfig;
import com.banana.controller.Controller;
import com.beerboy.ss.SparkSwagger;
import com.flickr4java.flickr.FlickrException;
import com.google.inject.Guice;
import org.javalite.activejdbc.Base;
import spark.Service;

import java.io.IOException;
import java.util.Arrays;

import static spark.Spark.after;
import static spark.Spark.before;


public class Application {

    public static void main(String[] args) throws FlickrException, IOException {
//        DbConfiguration dbConfiguration = new DbConfiguration();
//        dbConfiguration.loadConfiguration("/database.properties");
//        PictureRepository pictureRepository = new PictureRepository();
//        pictureRepository.getDbConnection();

        //    doFilter();


        Controller controller = Guice.createInjector(new GuiceConfig())
                .getInstance(Controller.class);
        Service spark = Service.ignite().port(8080);

        SparkSwagger.of(spark, "spark-swagger.conf").endpoints(() -> Arrays.asList(controller)).generateDoc();

    }

    private static void doFilter() {
        before((request, response) -> {
            Base.open();
            Base.openTransaction();

        });

        after((request, response) -> {
            Base.commitTransaction();
            Base.close();
        });
    }
}
