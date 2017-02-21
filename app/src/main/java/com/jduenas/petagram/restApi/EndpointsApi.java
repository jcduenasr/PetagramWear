package com.jduenas.petagram.restApi;

import com.jduenas.petagram.restApi.model.MascotaResponse;
import com.jduenas.petagram.restApi.model.RelationshipResponse;
import com.jduenas.petagram.restApi.model.UserResponse;
import com.jduenas.petagram.restApi.model.UsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jduenas on 12/12/2016.
 */

public interface EndpointsApi {

    @GET(ConstantesRestApi.URL_GET_RECENT_MEDIA_USER)
    Call<MascotaResponse> getRecentMedia();

    @GET(ConstantesRestApi.URL_GET_FOLOWEDS_SELF)
    Call<MascotaResponse> getFollowedsSelf();

    @GET(ConstantesRestApi.URL_SEARCH_USER)
    Call<MascotaResponse> getUsersSearch(@Query("q") String user,
                                      @Query("access_token") String token);

    @GET(ConstantesRestApi.URL_GET_RECENT_MEDIA_USER_ID)
    Call<MascotaResponse> getRecentMediaUserId(@Path("user-id") String userid);

    @GET(ConstantesRestApi.URL_GET_INFO_MEDIA_ID)
    Call<MascotaResponse> getInfoMediaID(@Path("media-id") String mediaid);

    @POST(ConstantesRestApi.KEY_POST_SET_LIKES_MEDIA_ID)
    Call<MascotaResponse> setLikesMediaID(@Path("media-id") String mediaid, @Query("access_token") String token);

    @FormUrlEncoded
    @POST(ConstantesRestApi.KEY_POST_ID_TOKEN)
    Call<UsuarioResponse> registrarTokenID(@Field("token") String token, @Field("username") String username);

    @FormUrlEncoded
    @POST(ConstantesRestApi.KEY_POST_SET_LIKES)
    Call<UsuarioResponse> registrarLikesToFirebase(@Field("username") String username,
                                                   @Field("id_dispositivo") String id_dispositivo,
                                                   @Field("id_foto_instagram") String id_foto_instagram,
                                                   @Field("usuario_instagram") String usuario_instagram,
                                                   @Field("id_usuario_instagram") String id_usuario_instagram);

    @GET(ConstantesRestApi.URL_KEY_GET_RELATIONSHIP)
    Call<RelationshipResponse> getRelationshipToUser(@Path("user-id") String userid);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_KEY_GET_RELATIONSHIP)
    Call<RelationshipResponse> setRelationshipToUser(@Path("user-id") String userid,
                                                     @Field("action") String action);
}
