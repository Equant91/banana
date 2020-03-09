package com.banana.config;

import com.banana.Application;
import com.banana.annotation.DbOpenWithTransaction;
import com.banana.annotation.DbOpenWithTransactionAnnotationInterceptor;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

public class GuiceConfig extends AbstractModule {

    String apiKey = "4099854756ade272ecff5cbd7d932104";
    String sharedSecret = "5c830c98f593ea80";

    @Override
    protected void configure() {
        bind(Application.class).in(Singleton.class);
        bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(DbOpenWithTransaction.class),
                new DbOpenWithTransactionAnnotationInterceptor());
    }

    @Provides
    @Singleton
    private Flickr flickr(){
        return new Flickr(apiKey, sharedSecret, new REST());
    }

}
