package com.jduenas.petagram.restApi.model;

import com.jduenas.petagram.pojo.Relationship;

import java.util.ArrayList;

/**
 * Created by jduenas on 30/01/2017.
 */

public class RelationshipResponse {

  ArrayList<Relationship> relationships;

    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(ArrayList<Relationship> relationships) {
        this.relationships = relationships;
    }
}
