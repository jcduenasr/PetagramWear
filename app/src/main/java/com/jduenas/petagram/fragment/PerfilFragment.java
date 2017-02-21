package com.jduenas.petagram.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jduenas.petagram.ConfigureAccount;
import com.jduenas.petagram.R;
import com.jduenas.petagram.adapter.MascotaAdaptador;
import com.jduenas.petagram.adapter.PerfilMascotaAdaptador;
import com.jduenas.petagram.pojo.Mascota;
import com.jduenas.petagram.restApi.ConstantesRestApi;
import com.jduenas.petagram.restApi.EndpointsApi;
import com.jduenas.petagram.restApi.adapter.RestApiAdapter;
import com.jduenas.petagram.restApi.model.MascotaResponse;
import com.jduenas.petagram.restApi.model.UserResponse;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    ArrayList<Mascota> mascotas = new ArrayList<>();
    private RecyclerView rvMascotas;
    private CircularImageView circularImageView;
    private  TextView tvNombrePerfil;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_perfil, container, false);
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        tvNombrePerfil = (TextView) v.findViewById(R.id.tvNombrePerfil);
        circularImageView = (CircularImageView) v.findViewById(R.id.circularImageView);
        tvNombrePerfil.setText("Nombre");
        rvMascotas = (RecyclerView) v.findViewById(R.id.rvPerfilMascotas);

        GridLayoutManager glm = new GridLayoutManager(getActivity(),3);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        rvMascotas.setLayoutManager(glm);
        inicializarListaMascotas();

        return  v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (isVisibleToUser) {
            inicializarListaMascotas();
        }*/
    }

    public PerfilMascotaAdaptador adaptador;
    public void inicializarAdaptador(){
        adaptador = new PerfilMascotaAdaptador(mascotas,getActivity());
        rvMascotas.setAdapter(adaptador);
    }

    public void inicializarListaMascotas(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("ConfigureAccount",getActivity().MODE_PRIVATE);
        String accountId = "";
        String profile_picture = "";
        String accountName = "";
        try{
            accountId = sharedPref.getString(getString(R.string.account_id), "");
        }catch (Exception e){
            accountId = "";
        }
        try{
            profile_picture = sharedPref.getString(getString(R.string.profile_picture), "");
            accountName = sharedPref.getString(getString(R.string.account_name), "");
        }catch (Exception e){
            profile_picture = "";
            accountName = "";
        }
        if (!profile_picture.equals("")){
            Picasso.with(getActivity())
                    .load(profile_picture)
                    .placeholder(R.drawable.ic_dog)
                    .into(circularImageView);
            tvNombrePerfil.setText(accountName);
        }


        if(!accountId.equals("")){
            RestApiAdapter restApiAdapter = new RestApiAdapter();
            Gson gsonMediaRecent = restApiAdapter.construyeGsonDeserializadorMediaRecent();
            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApiInstagram(gsonMediaRecent);

            Call<MascotaResponse> mascotaResponseCall = endpointsApi.getRecentMediaUserId(accountId);
            mascotaResponseCall.enqueue(new Callback<MascotaResponse>() {
                @Override
                public void onResponse(Call<MascotaResponse> call, Response<MascotaResponse> response) {
                    MascotaResponse mascotaResponse = response.body();
                    mascotas = mascotaResponse.getMascotas();
                    inicializarAdaptador();
                }

                @Override
                public void onFailure(Call<MascotaResponse> call, Throwable t) {
                    Toast.makeText(getContext(),"Error en la conexion. Intenta de nuevo", Toast.LENGTH_LONG).show();
                    Log.i("Error getRecentMedia","FALLO LA CONEXION: "+t.toString());
                }
            });
        }else{
            Toast.makeText(getContext(),"No hay cuenta configurada",Toast.LENGTH_LONG).show();
        }
    }
}
