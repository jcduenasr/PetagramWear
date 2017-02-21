package com.jduenas.petagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.jduenas.petagram.adapter.MascotaAdaptador;
import com.jduenas.petagram.adapter.PerfilMascotaAdaptador;
import com.jduenas.petagram.fragment.IRecyclerViewFragment;
import com.jduenas.petagram.pojo.Mascota;
import com.jduenas.petagram.presenter.IPresenterVerUsuario;
import com.jduenas.petagram.presenter.IRecyclerViewFragmentPresenter;
import com.jduenas.petagram.presenter.PresenterVerUsuario;
import com.jduenas.petagram.presenter.RecyclerViewFragmentPresenter;
import com.jduenas.petagram.restApi.IVerUsuario;

import java.util.ArrayList;

public class VerUsuario extends AppCompatActivity implements IVerUsuario {
    ArrayList<Mascota> mascotas;
    private RecyclerView rvMascotas;
    private IPresenterVerUsuario presenter;
    String var = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);
        rvMascotas = (RecyclerView) findViewById(R.id.rvMascotasVU);
        Toolbar miActionBar = (Toolbar) findViewById(R.id.toolbar);
        if (miActionBar!=null){
            setSupportActionBar(miActionBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Bundle b = getIntent().getExtras();

        if (b!=null){
            var = b.getString("user-id");
            Log.i("varuserid",""+var);
        }
        presenter = new PresenterVerUsuario(this,this,var);

    }

    @Override
    public void generarLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvMascotas.setLayoutManager(llm);
    }

    @Override
    public void generarGridLayout() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvMascotas.setLayoutManager(gridLayoutManager);
    }

    @Override
    public PerfilMascotaAdaptador crearAdaptador(ArrayList<Mascota> mascotas) {
        PerfilMascotaAdaptador adaptador = new PerfilMascotaAdaptador(mascotas,this);
        return adaptador;
    }

    @Override
    public void inicializarAdaptadorRV(PerfilMascotaAdaptador adaptador) {
        rvMascotas.setAdapter(adaptador);
    }
}
