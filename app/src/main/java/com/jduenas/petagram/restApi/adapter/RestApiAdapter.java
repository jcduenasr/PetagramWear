package com.jduenas.petagram.restApi.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jduenas.petagram.restApi.ConstantesRestApi;
import com.jduenas.petagram.restApi.EndpointsApi;
import com.jduenas.petagram.restApi.deserializer.MascotaDeserializer;
import com.jduenas.petagram.restApi.deserializer.RelationshipDeserializer;
import com.jduenas.petagram.restApi.deserializer.UserDeserializer;
import com.jduenas.petagram.restApi.model.MascotaResponse;
import com.jduenas.petagram.restApi.model.RelationshipResponse;
import com.jduenas.petagram.restApi.model.UserResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jduenas on 12/12/2016.
 */

public class RestApiAdapter {

    public EndpointsApi establecerConexionRestAPIHeroku(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.ROOT_URLFIREBASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                ;

        return retrofit.create(EndpointsApi.class);
    }

    public EndpointsApi establecerConexionRestApiInstagram(Gson gson){
        if (gson==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantesRestApi.ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(EndpointsApi.class);
        }else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantesRestApi.ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit.create(EndpointsApi.class);
        }

    }

    public Gson construyeGsonDeserializadorMediaRecent(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(MascotaResponse.class, new MascotaDeserializer());

        return gsonBuilder.create();
    }

    public Gson construyeGsonDeserializadorDataUserSearched(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(MascotaResponse.class, new UserDeserializer());

        return gsonBuilder.create();
    }

    public Gson construyeGsonDeserializadorDarLike(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(MascotaResponse.class, new MascotaDeserializer());

        return gsonBuilder.create();
    }

    public Gson construyeGsonDeserializadorRelationship(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RelationshipResponse.class, new RelationshipDeserializer());

        return gsonBuilder.create();
    }
}
