package com.jduenas.petagram.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jduenas.petagram.R;
import com.jduenas.petagram.db.ConstructorMascotas;
import com.jduenas.petagram.fragment.IRecyclerViewFragment;
import com.jduenas.petagram.pojo.Mascota;
import com.jduenas.petagram.restApi.EndpointsApi;
import com.jduenas.petagram.restApi.IVerUsuario;
import com.jduenas.petagram.restApi.adapter.RestApiAdapter;
import com.jduenas.petagram.restApi.model.MascotaResponse;
import com.jduenas.petagram.restApi.model.UsuarioResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jduenas on 21/02/2017.
 */

public class PresenterVerUsuario implements IPresenterVerUsuario {

    private IVerUsuario iVerUsuario;
    private Context context;
    private ConstructorMascotas constructorMascotas;
    private ArrayList<Mascota> mascotas;
    private ArrayList<Mascota> mascotasFotos = new ArrayList<>();
    private String userid="";

    public PresenterVerUsuario(IVerUsuario iVerUsuario, Context context, String userid) {
        this.iVerUsuario = iVerUsuario;
        this.context = context;
        this.userid=userid;
        //obtenerMascotasDB();
        obtenerMediosRecientes();
    }

    @Override
    public void obtenerMascotasDB() {
        constructorMascotas = new ConstructorMascotas(context);
        mascotas = constructorMascotas.obtenerDatos();
        mostrarMascotasRV();
    }

    @Override
    public void obtenerMediosRecientes() {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Gson gsonMediaRecent = restApiAdapter.construyeGsonDeserializadorMediaRecent();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApiInstagram(gsonMediaRecent);

        Call<MascotaResponse> mascotaResponseCall = endpointsApi.getRecentMediaUserId(userid);
        mascotaResponseCall.enqueue(new Callback<MascotaResponse>() {
            @Override
            public void onResponse(Call<MascotaResponse> call, Response<MascotaResponse> response) {
                MascotaResponse mascotaResponse = response.body();
                mascotasFotos.addAll(mascotaResponse.getMascotas());
                Log.i("VerU getRecentMedia","FALLO LA CONEXION: "+mascotasFotos.toString());
                mostrarMascotasRV();
            }

            @Override
            public void onFailure(Call<MascotaResponse> call, Throwable t) {
                Toast.makeText(context,"Error en la conexion. Intenta de nuevo", Toast.LENGTH_LONG).show();
                Log.i("VerU getRecentMedia","FALLO LA CONEXION: "+t.toString());
            }
        });
    }

    @Override
    public void mostrarMascotasRV() {
        iVerUsuario.inicializarAdaptadorRV(iVerUsuario.crearAdaptador(mascotasFotos));
        iVerUsuario.generarGridLayout();
    }
}