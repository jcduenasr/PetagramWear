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
import com.jduenas.petagram.restApi.ConstantesRestApi;
import com.jduenas.petagram.restApi.EndpointsApi;
import com.jduenas.petagram.restApi.adapter.RestApiAdapter;
import com.jduenas.petagram.restApi.model.MascotaResponse;
import com.jduenas.petagram.restApi.model.UsuarioResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jduenas on 09/11/2016.
 */

public class RecyclerViewFragmentPresenter implements IRecyclerViewFragmentPresenter {

    private IRecyclerViewFragment iRecyclerViewFragment;
    private Context context;
    private ConstructorMascotas constructorMascotas;
    private ArrayList<Mascota> mascotas;
    private ArrayList<Mascota> mascotasFotos = new ArrayList<>();

    public RecyclerViewFragmentPresenter(IRecyclerViewFragment iRecyclerViewFragment, Context context) {
        this.iRecyclerViewFragment = iRecyclerViewFragment;
        this.context = context;
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
        Gson gsonMediaRecent = restApiAdapter.construyeGsonDeserializadorDataUserSearched();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApiInstagram(gsonMediaRecent);

        Call<MascotaResponse> mascotaResponseCall = endpointsApi.getFollowedsSelf();
        mascotaResponseCall.enqueue(new Callback<MascotaResponse>() {
            @Override
            public void onResponse(Call<MascotaResponse> call, Response<MascotaResponse> response) {
                MascotaResponse mascotaResponse = response.body();
                mascotas = mascotaResponse.getMascotas();

                for (int i = 0; i < mascotas.size(); i++) {
                    RestApiAdapter restApiAdapter2 = new RestApiAdapter();
                    Gson gsonMediaRecent = restApiAdapter2.construyeGsonDeserializadorMediaRecent();
                    EndpointsApi endpointsApi = restApiAdapter2.establecerConexionRestApiInstagram(gsonMediaRecent);

                    Call<MascotaResponse> mascotaResponseCall = endpointsApi.getRecentMediaUserId(mascotas.get(i).getId());
                    mascotaResponseCall.enqueue(new Callback<MascotaResponse>() {
                        @Override
                        public void onResponse(Call<MascotaResponse> call, Response<MascotaResponse> response) {
                            MascotaResponse mascotaResponse = response.body();
                            mascotasFotos.addAll(mascotaResponse.getMascotas());
                            mostrarMascotasRV();
                        }
                        @Override
                        public void onFailure(Call<MascotaResponse> call, Throwable t) {
                            Toast.makeText(context,"Error en la conexion. Intenta de nuevo", Toast.LENGTH_LONG).show();
                            Log.i("Error getRecentMedia","FALLO LA CONEXION: "+t.toString());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<MascotaResponse> call, Throwable t) {
                Toast.makeText(context,"Error en la conexion. Intenta de nuevo", Toast.LENGTH_LONG).show();
                Log.i("Error getFollowedsSelf","FALLO LA CONEXION: "+t.toString());
            }
        });
    }
    @Override
    public void setLikesOnFirebase(String username, String id_dispositivo, String id_foto_instagram) {
        SharedPreferences sharedPref = context.getSharedPreferences("ConfigureAccount",context.MODE_PRIVATE);
        String accountName = "";
        String accountID = "";
        try{
            accountName = sharedPref.getString(context.getString(R.string.account_name), "");
            accountID = sharedPref.getString(context.getString(R.string.account_id), "");
        }catch (Exception e){
            accountName = "";
            accountID = "";
        }
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestAPIHeroku();

        Call<UsuarioResponse> mascotaResponseCall = endpointsApi.registrarLikesToFirebase(username,id_dispositivo,id_foto_instagram,accountName, accountID);
        mascotaResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {

            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                Toast.makeText(context,"Error en la conexion. Intenta de nuevo", Toast.LENGTH_LONG).show();
                Log.i("registrarLikesToFire","FALLO LA CONEXION: "+t.toString());
            }
        });
    }

    @Override
    public void mostrarMascotasRV() {
        iRecyclerViewFragment.inicializarAdaptadorRV(iRecyclerViewFragment.crearAdaptador(mascotasFotos));
        iRecyclerViewFragment.generarGridLayout();
    }
}
