package com.jduenas.petagram.presenter;

/**
 * Created by jduenas on 09/11/2016.
 */

public interface IRecyclerViewFragmentPresenter {

    void obtenerMascotasDB();
    void obtenerMediosRecientes();

    void mostrarMascotasRV();
    void setLikesOnFirebase(String username, String id_dispositivo, String id_foto_instagram);

}
