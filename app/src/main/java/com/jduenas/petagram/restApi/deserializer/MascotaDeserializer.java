package com.jduenas.petagram.restApi.deserializer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jduenas.petagram.pojo.Mascota;
import com.jduenas.petagram.restApi.JsonKeys;
import com.jduenas.petagram.restApi.model.MascotaResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jduenas on 12/12/2016.
 */

public class MascotaDeserializer implements JsonDeserializer<MascotaResponse>{

    @Override
    public MascotaResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        MascotaResponse mascotaResponse = gson.fromJson(json, MascotaResponse.class);
        try {
            JsonArray mascotaResponseData = json.getAsJsonObject().getAsJsonArray(JsonKeys.MEDIA_RESPONSE_ARRAY);
            mascotaResponse.setMascotas(deserializerMascotaFromJson(mascotaResponseData));
        }catch (Exception e){
            try{
                JsonObject mascotaResponseData = json.getAsJsonObject().getAsJsonObject(JsonKeys.MEDIA_RESPONSE_ARRAY);

                JsonArray mascotaResponseDataA = new JsonArray();
                mascotaResponseDataA.add(mascotaResponseData);
                mascotaResponse.setMascotas(deserializerMascotaFromJson(mascotaResponseDataA));
            }catch (Exception f) {
                Log.d("Exception des", "-> " + e.toString());
                Log.d("data NULL", "-> " + json);
            }
        }
        return mascotaResponse;
    }

    private ArrayList<Mascota> deserializerMascotaFromJson(JsonArray mascotaResponseData){
        ArrayList<Mascota> mascotas = new ArrayList<>();
        for (int i = 0; i < mascotaResponseData.size(); i++) {
            JsonObject mascotaResponseDataObject = mascotaResponseData.get(i).getAsJsonObject();
            JsonObject userJson     = mascotaResponseDataObject.getAsJsonObject(JsonKeys.USER);
            String id               = userJson.get(JsonKeys.USER_ID).getAsString();
            String nombreCompleto   = userJson.get(JsonKeys.USER_FULL_NAME).getAsString();
            String username = "";
            try {
                username   = userJson.get(JsonKeys.USERNAME).getAsString();
            }catch (Exception e){
                username   = "";
            }

            JsonObject imageJson            = mascotaResponseDataObject.getAsJsonObject(JsonKeys.MEDIA_IMAGES);
            JsonObject stdResolutionJson    = imageJson.getAsJsonObject(JsonKeys.MEDIA_STANDARD_RESOLUTION);
            String url                      = stdResolutionJson.get(JsonKeys.MEDIA_URL).getAsString();
            String id_foto                  = mascotaResponseDataObject.get(JsonKeys.USER_ID).getAsString();

            JsonObject likesJson = mascotaResponseDataObject.getAsJsonObject(JsonKeys.MEDIA_LIKES);
            int likes            = likesJson.get(JsonKeys.MEDIA_LIKES_COUNT).getAsInt();

            Mascota currentMascota = new Mascota();
            currentMascota.setId(id);
            currentMascota.setNombreCompleto(nombreCompleto);
            currentMascota.setUrl_foto(url);
            currentMascota.setLikes(likes);
            currentMascota.setId_foto(id_foto);
            currentMascota.setUsername(username);

            mascotas.add(currentMascota);
        }
        return mascotas;
    }
}
