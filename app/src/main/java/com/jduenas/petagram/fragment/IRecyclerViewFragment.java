package com.jduenas.petagram.fragment;

import com.jduenas.petagram.adapter.MascotaAdaptador;
import com.jduenas.petagram.pojo.Mascota;

import java.util.ArrayList;

/**
 * Created by jduenas on 09/11/2016.
 */

public interface IRecyclerViewFragment {

    public void generarLinearLayoutVertical();
    public void generarGridLayout();

    public MascotaAdaptador crearAdaptador(ArrayList<Mascota> mascotas);

    public void inicializarAdaptadorRV(MascotaAdaptador adaptador);

}
