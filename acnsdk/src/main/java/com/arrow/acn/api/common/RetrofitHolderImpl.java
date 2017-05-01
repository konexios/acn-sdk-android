/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.common;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.arrow.acn.api.Constants;
import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.mqtt.common.NoSSLv3SocketFactory;
import com.arrow.acn.api.rest.IotConnectAPIService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executor;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by osminin on 3/15/2016.
 */
public class RetrofitHolderImpl implements RetrofitHolder {
    @NonNull
    private final ApiRequestSigner mRequestSigner = new ApiRequestSigner();
    private Retrofit mRetrofit;
    private String mApiKey;
    private String mApiSecret;
    private final Executor mExecutor;
    @NonNull
    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS'Z'");
                    df.setTimeZone(tz);
                    String nowAsISO = df.format(new Date()).replace(" ", "T");

                    String body = bodyToString(chain.request().body());
                    if (TextUtils.isEmpty(mRequestSigner.getSecretKey()) &&
                            !TextUtils.isEmpty(mApiSecret)) {
                        mRequestSigner.setSecretKey(mApiSecret);
                    }
                    String apiKey = TextUtils.isEmpty(mRequestSigner.getApiKey()) ?
                            mApiKey : mRequestSigner.getApiKey();
                    String signature = mRequestSigner.method(chain.request().method())
                            .canonicalUri(chain.request().url().uri().getPath())
                            .setApiKey(apiKey).timestamp(nowAsISO).payload(body).signV1();
                    Request request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .addHeader(Constants.Api.X_ARROW_APIKEY, apiKey)
                            .addHeader(Constants.Api.X_ARROW_DATE, nowAsISO)
                            .addHeader(Constants.Api.X_ARROW_SIGNATURE, signature)
                            .addHeader(Constants.Api.X_ARROW_VERSION, Constants.Api.X_ARROW_VERSION_1)
                            .build();

                    okhttp3.Response response = chain.proceed(request);
                    return response;
                }
            })
            .sslSocketFactory(new NoSSLv3SocketFactory())
            .build();


    public RetrofitHolderImpl(Executor executor) {
        mExecutor = executor;
    }

    @Override
    public void setDefaultApiKey(String apiKey) {
        mApiKey = apiKey;
    }

    @Override
    public void setDefaultApiSecret(String apiSecret) {
        mApiSecret = apiSecret;
    }

    @Override
    public String getSecretKey() {
        return mRequestSigner.getSecretKey();
    }

    @Override
    public void setSecretKey(String secretKey) {
        mRequestSigner.setSecretKey(secretKey);
    }

    @Override
    public String getApiKey() {
        return mRequestSigner.getApiKey();
    }

    @Override
    public void setApiKey(String apiKey) {
        mRequestSigner.setApiKey(apiKey);
    }

    @Override
    public IotConnectAPIService getIotConnectAPIService(@NonNull String endpoint) {
        if (mRetrofit == null || !mRetrofit.baseUrl().toString().equals(endpoint)) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient);
            if (mExecutor != null) {
                builder.callbackExecutor(mExecutor);
            }
            mRetrofit = builder.build();

        }
        return mRetrofit.create(IotConnectAPIService.class);
    }

    @Override
    public ApiError convertToApiError(@NonNull Response<?> response) {
        return ErrorUtils.parseError(response, mRetrofit);
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (@NonNull final IOException e) {
            Timber.e(e);
            return "did not work";
        }
    }
}
