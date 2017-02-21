package com.jduenas.petagram.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.jduenas.petagram.DetalleMascota;
import com.jduenas.petagram.R;
import com.jduenas.petagram.db.ConstructorMascotas;
import com.jduenas.petagram.pojo.Mascota;
import com.jduenas.petagram.presenter.IRecyclerViewFragmentPresenter;
import com.jduenas.petagram.restApi.ConstantesRestApi;
import com.jduenas.petagram.restApi.EndpointsApi;
import com.jduenas.petagram.restApi.adapter.RestApiAdapter;
import com.jduenas.petagram.restApi.model.MascotaResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jduenas on 25/10/2016.
 */
public class MascotaAdaptador extends RecyclerView.Adapter<MascotaAdaptador.MascotaViewHolder>{

    ArrayList<Mascota> mascotas;
    ArrayList<Mascota> mascotas2;
    Activity activity;
    IRecyclerViewFragmentPresenter presenter;

    public MascotaAdaptador(ArrayList<Mascota> mascotas, Activity activity,IRecyclerViewFragmentPresenter presenter) {
        this.mascotas = mascotas;
        this.activity = activity;
        this.presenter = presenter;
    }

    @Override
    public MascotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_grid_mascota,parent,false);
        return new MascotaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MascotaViewHolder mascotaViewHolder, int position) {
        final Mascota mascota = mascotas.get(position);
        mascotaViewHolder.setTvNombreCompletoCV(mascota.getNombreCompleto());
        Picasso.with(activity)
                .load(mascota.getUrl_foto())
                .placeholder(R.drawable.ic_dog)
                .into(mascotaViewHolder.imgFoto);
        mascotaViewHolder.tvLikesCV.setText(String.valueOf(mascota.getLikes()));

        mascotaViewHolder.imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetalleMascota.class);
                intent.putExtra("url", mascota.getUrl_foto());
                intent.putExtra("likes", mascota.getLikes());
                activity.startActivity(intent);
            }
        });

        mascotaViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestApiAdapter restApiAdapter2 = new RestApiAdapter();
                Gson gsonMediaRecent = restApiAdapter2.construyeGsonDeserializadorDarLike();
                EndpointsApi endpointsApi = restApiAdapter2.establecerConexionRestApiInstagram(gsonMediaRecent);

                Call<MascotaResponse> mascotaResponseCall = endpointsApi.setLikesMediaID(mascota.getId_foto(), ConstantesRestApi.ACCESS_TOKEN);
                mascotaResponseCall.enqueue(new Callback<MascotaResponse>() {
                    @Override
                    public void onResponse(Call<MascotaResponse> call, Response<MascotaResponse> response) {
                        RestApiAdapter restApiAdapter3 = new RestApiAdapter();
                        Gson gsonMediaRecent = restApiAdapter3.construyeGsonDeserializadorDarLike();
                        EndpointsApi endpointsApi = restApiAdapter3.establecerConexionRestApiInstagram(gsonMediaRecent);

                        Call<MascotaResponse> mascotaResponseCall = endpointsApi.getInfoMediaID(mascota.getId_foto());
                        mascotaResponseCall.enqueue(new Callback<MascotaResponse>() {
                            @Override
                            public void onResponse(Call<MascotaResponse> call, Response<MascotaResponse> response) {
                                MascotaResponse mascotaResponse = response.body();
                                mascotas2 = mascotaResponse.getMascotas();
                                if (mascotas2.size()>0){
                                    mascotaViewHolder.tvLikesCV.setText(String.valueOf(mascotas2.get(0).getLikes()));
                                }
                                String token = FirebaseInstanceId.getInstance().getToken();
                                presenter.setLikesOnFirebase(mascota.getUsername(),token,mascota.getId_foto());
                            }

                            @Override
                            public void onFailure(Call<MascotaResponse> call, Throwable t) {
                                Toast.makeText(activity,"Error en la conexion. Intenta de nuevo", Toast.LENGTH_LONG).show();
                                Log.i("Error getInfoMediaID","FALLO LA CONEXION: "+t.toString());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<MascotaResponse> call, Throwable t) {
                        Toast.makeText(activity,"Error en la conexion. Intenta de nuevo", Toast.LENGTH_LONG).show();
                        Log.i("Error setLikes","FALLO LA CONEXION: "+t.toString());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgFoto;
        private TextView tvNombreCompletoCV;
        private TextView tvLikesCV;
        private ImageButton btnLike;

        public String getTvNombreCompletoCV() {
            return tvNombreCompletoCV.getText().toString();
        }
        public void setTvNombreCompletoCV(String tvNombreCompletoCV) {
            this.tvNombreCompletoCV.setText(tvNombreCompletoCV);
        }
        public MascotaViewHolder(View itemView) {
            super(itemView);
            imgFoto = (ImageView) itemView.findViewById(R.id.imgFotoCV);
            tvNombreCompletoCV = (TextView) itemView.findViewById(R.id.tvNombreCompletoCV);
            tvLikesCV = (TextView) itemView.findViewById(R.id.tvLikesCV);
            btnLike = (ImageButton) itemView.findViewById(R.id.btnLike);
        }
    }
}
