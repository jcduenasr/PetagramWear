package com.jduenas.petagram.restApi;

import com.jduenas.petagram.adapter.MascotaAdaptador;
import com.jduenas.petagram.adapter.PerfilMascotaAdaptador;
import com.jduenas.petagram.pojo.Mascota;

import java.util.ArrayList;

/**
 * Created by jduenas on 21/02/2017.
 */

public interface IVerUsuario {
    public void generarLinearLayoutVertical();
    public void generarGridLayout();

    public PerfilMascotaAdaptador crearAdaptador(ArrayList<Mascota> mascotas);

    public void inicializarAdaptadorRV(PerfilMascotaAdaptador adaptador);
}
