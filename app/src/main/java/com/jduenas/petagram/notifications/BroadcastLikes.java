package com.jduenas.petagram.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jduenas.petagram.MainActivity;
import com.jduenas.petagram.R;
import com.jduenas.petagram.VerUsuario;
import com.jduenas.petagram.fragment.PerfilFragment;
import com.jduenas.petagram.pojo.Relationship;
import com.jduenas.petagram.restApi.EndpointsApi;
import com.jduenas.petagram.restApi.adapter.RestApiAdapter;
import com.jduenas.petagram.restApi.model.RelationshipResponse;
import com.jduenas.petagram.restApi.model.UsuarioResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jduenas on 30/01/2017.
 */

public class BroadcastLikes extends BroadcastReceiver {
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        String ACCION_KEY1 = "ver_mi_perfil";
        String ACCION_KEY21 = "follow";
        String ACCION_KEY22 = "unfollow";
        String ACCION_KEY3 = "ver_usuario";
        String accion = intent.getAction();
        String userid = intent.getStringExtra("user-id");
        Log.i("user-id1",""+userid);
        this.context = context;
        if (ACCION_KEY1.equals(accion)){
            Intent perfil = new Intent(context,MainActivity.class);
            perfil.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(perfil);
            Toast.makeText(context, "ver_mi_perfil ", Toast.LENGTH_SHORT).show();
        }
        if (ACCION_KEY21.equals(accion) || ACCION_KEY22.equals(accion)){
            Toast.makeText(context, ""+accion, Toast.LENGTH_SHORT).show();

            Log.i("user-id2",""+userid);
            followUnfollowUser(userid,accion);
        }
        if (ACCION_KEY3.equals(accion)){

            Intent verUsuario = new Intent(context,VerUsuario.class);
            Log.i("user-id3",""+userid);
            verUsuario.putExtra("user-id",userid);
            verUsuario.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(verUsuario);
            Toast.makeText(context, "ver_usuario ", Toast.LENGTH_SHORT).show();
        }
    }

    private void followUnfollowUser(String userid, String actionRelationship) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Gson gsonRelationShip = restApiAdapter.construyeGsonDeserializadorRelationship();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApiInstagram(gsonRelationShip);

        Call<RelationshipResponse> mascotaResponseCall = endpointsApi.setRelationshipToUser(userid,actionRelationship);
        mascotaResponseCall.enqueue(new Callback<RelationshipResponse>() {
            @Override
            public void onResponse(Call<RelationshipResponse> call, Response<RelationshipResponse> response) {
                RelationshipResponse relationshipResponse = response.body();
                ArrayList<Relationship> relationships = relationshipResponse.getRelationships();
                Toast.makeText(context, "follow/unfollow"+relationships.get(0).getOutgoing_status(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RelationshipResponse> call, Throwable t) {
                Toast.makeText(context,"Error en la conexion. Intenta de nuevo", Toast.LENGTH_LONG).show();
                Log.i("followUnfollowUser","FALLO LA CONEXION: "+t.toString());
            }
        });
    }
}
