/*
 * Copyright 2013 Uwe Trottmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.uwetrottmann.tmdb;

import com.squareup.okhttp.*;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.uwetrottmann.tmdb.services.CollectionService;
import com.uwetrottmann.tmdb.services.ConfigurationService;
import com.uwetrottmann.tmdb.services.DiscoverService;
import com.uwetrottmann.tmdb.services.FindService;
import com.uwetrottmann.tmdb.services.MoviesService;
import com.uwetrottmann.tmdb.services.PeopleService;
import com.uwetrottmann.tmdb.services.SearchService;
import com.uwetrottmann.tmdb.services.TvEpisodesService;
import com.uwetrottmann.tmdb.services.TvSeasonsService;
import com.uwetrottmann.tmdb.services.TvService;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * Helper class for easy usage of the TMDB v3 API using retrofit.
 * <p>
 * Create an instance of this class, {@link #setApiKey(String)} and then call any of the service methods.
 * <p>
 * The service methods take care of constructing the required {@link retrofit.Retrofit} and creating the service. You
 * can customize the {@link retrofit.Retrofit} by overriding {@link #newRestAdapterBuilder()} and setting e.g.
 * your own HTTP client instance or thread executor.
 * <p>
 * Only one {@link retrofit.Retrofit} instance is created upon the first and re-used for any consequent service
 * method call.
 */
public class Tmdb {

    /**
     * Tmdb API URL.
     */
    public static final String API_URL = "https://api.themoviedb.org/3/";

    /**
     * API key query parameter name.
     */
    public static final String PARAM_API_KEY = "api_key";

    private String apiKey;
    private boolean isDebug;
    private Retrofit retrofit;

    /**
     * Create a new manager instance.
     */
    public Tmdb() {
    }

    /**
     * Set the TMDB API key.
     * <p>
     * The next service method call will trigger a rebuild of the {@link retrofit.Retrofit}. If you have cached any
     * service instances, get a new one from its service method.
     *
     * @param value Your TMDB API key.
     */
    public Tmdb setApiKey(String value) {
        this.apiKey = value;
        retrofit = null;
        return this;
    }

    /**
     * Set the {@link retrofit.Retrofit} log level.
     * The Retrofit instance is reset to recreate the client
     */
    public Tmdb setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        this.retrofit = null;
        return this;
    }

    /**
     * Create a new {@link retrofit.Retrofit.Builder}. Override this to e.g. set your own client or executor.
     *
     * @return A {@link retrofit.Retrofit.Builder} with no modifications.
     */
    protected Retrofit.Builder newRestAdapterBuilder() {
        return new Retrofit.Builder();
    }

    /**
     * Return the current {@link retrofit.Retrofit} instance. If none exists (first call, API key changed, debug changed),
     * builds a new one.
     * <p>
     * When building, sets the base URL, a custom converter ({@link TmdbHelper#getGsonBuilder()})
     * and a {@link com.squareup.okhttp.OkHttpClient} which adds the API key as query param and set log level
     */
    protected Retrofit getRetrofit() {
        if (retrofit == null) {
            Retrofit.Builder builder = newRestAdapterBuilder();

            builder.baseUrl(API_URL);
            builder.addConverterFactory(GsonConverterFactory.create(TmdbHelper.getGsonBuilder().create()));
            builder.client(getHttpClient());

            retrofit = builder.build();
        }

        return retrofit;
    }

    /**
     * Return a {@link com.squareup.okhttp.OkHttpClient} which adds an Interceptor that adds the API key
     * as query param and set log level, and if debug is enabled it adds an
     * {@link com.squareup.okhttp.logging.HttpLoggingInterceptor} with debug level BODY
     */
    protected OkHttpClient getHttpClient(){
        OkHttpClient client = new OkHttpClient();

        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                HttpUrl url = chain.request().httpUrl().newBuilder()
                        .addQueryParameter(PARAM_API_KEY, apiKey)
                        .build();
                Request request = chain.request().newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
        client.interceptors().add(requestInterceptor);

        /*
        if(isDebug){
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(logger);
        }
        */

        return client;
    }

    public ConfigurationService configurationService() {
        return getRetrofit().create(ConfigurationService.class);
    }

    public FindService findService() {
        return getRetrofit().create(FindService.class);
    }

    public MoviesService moviesService() {
        return getRetrofit().create(MoviesService.class);
    }

    public PeopleService personService() {
        return getRetrofit().create(PeopleService.class);
    }

    public SearchService searchService() {
        return getRetrofit().create(SearchService.class);
    }

    public TvService tvService() {
        return getRetrofit().create(TvService.class);
    }

    public TvSeasonsService tvSeasonsService() {
        return getRetrofit().create(TvSeasonsService.class);
    }
    
    public TvEpisodesService tvEpisodesService() {
        return getRetrofit().create(TvEpisodesService.class);
    }
    
    public DiscoverService discoverService() {
        return getRetrofit().create(DiscoverService.class);
    }

    public CollectionService collectionService() {
        return getRetrofit().create(CollectionService.class);
    }
}
