package com.jduenas.petagram.pojo;

/**
 * Created by jduenas on 25/10/2016.
 */
public class Mascota {

    private String id;
    private String nombreCompleto;
    private int likes;
    private String url_foto;
    private String id_foto;

    private String username;
    private String bio;
    private String website;
    private String profile_picture;

    public Mascota() {
    }

    public Mascota(String nombreCompleto, int likes, String url_foto) {
        this.nombreCompleto = nombreCompleto;
        this.likes = likes;
        this.url_foto = url_foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getId_foto() {
        return id_foto;
    }

    public void setId_foto(String id_foto) {
        this.id_foto = id_foto;
    }
}
