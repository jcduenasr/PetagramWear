package com.jduenas.petagram.restApi.model;

/**
 * Created by jduenas on 18/01/2017.
 */

public class UsuarioResponse {
    private String id_dispositivo;
    private String id_usuario_instagram;

    public UsuarioResponse(String id_dispositivo, String id_usuario_instagram) {
        this.id_dispositivo = id_dispositivo;
        this.id_usuario_instagram = id_usuario_instagram;
    }

    public UsuarioResponse() {
    }

    public String getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(String id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }

    public String getId_usuario_instagram() {
        return id_usuario_instagram;
    }

    public void setId_usuario_instagram(String id_usuario_instagram) {
        this.id_usuario_instagram = id_usuario_instagram;
    }
}
