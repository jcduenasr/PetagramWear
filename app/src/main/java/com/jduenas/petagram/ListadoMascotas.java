package com.jduenas.petagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jduenas.petagram.adapter.MascotaAdaptador;
import com.jduenas.petagram.pojo.Mascota;
import com.jduenas.petagram.presenter.IListadoMascotasFavoritasPresenter;
import com.jduenas.petagram.presenter.IRecyclerViewFragmentPresenter;
import com.jduenas.petagram.presenter.ListadoMascotasPresenter;

import java.util.ArrayList;

public class ListadoMascotas extends AppCompatActivity implements IListadoMascotasView {

    ArrayList<Mascota> mascotas;
    private RecyclerView listaMascotas;
    private IListadoMascotasFavoritasPresenter presenter;
    private IRecyclerViewFragmentPresenter presenter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_mascotas);
        Toolbar miActionBar = (Toolbar) findViewById(R.id.toolbar);
        if (miActionBar!=null){
            setSupportActionBar(miActionBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listaMascotas = (RecyclerView) findViewById(R.id.rvMascotasFav);

        presenter = new ListadoMascotasPresenter(this,this);

    }



    @Override
    public void generarLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaMascotas.setLayoutManager(llm);
    }

    @Override
    public MascotaAdaptador crearAdaptador(ArrayList<Mascota> mascotas) {
        MascotaAdaptador adaptador = new MascotaAdaptador(mascotas,this,presenter2);
        return adaptador;
    }

    @Override
    public void inicializarAdaptadorRV(MascotaAdaptador adaptador) {
        listaMascotas.setAdapter(adaptador);
    }
}
