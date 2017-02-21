package com.jduenas.petagram.restApi.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jduenas.petagram.pojo.Relationship;
import com.jduenas.petagram.restApi.JsonKeys;
import com.jduenas.petagram.restApi.model.RelationshipResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jduenas on 10/02/2017.
 */

public class RelationshipDeserializer implements JsonDeserializer<RelationshipResponse> {

    @Override
    public RelationshipResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        RelationshipResponse relationshipResponse = gson.fromJson(json, RelationshipResponse.class);
        JsonObject relationshipResponseData = json.getAsJsonObject().getAsJsonObject(JsonKeys.MEDIA_RESPONSE_ARRAY);
        relationshipResponse.setRelationships(deserializerUserFromJson(relationshipResponseData));
        return relationshipResponse;
    }

    private ArrayList<Relationship> deserializerUserFromJson(JsonObject relationshipResponseData){
        ArrayList<Relationship> relationships = new ArrayList<>();
       // for (int i = 0; i < relationshipResponseData.size(); i++) {
            //JsonObject relationshipResponseDataObject = relationshipResponseData.get(i).getAsJsonObject();

            Relationship currentRelationship = new Relationship();
            //currentRelationship.setIncoming_status(relationshipResponseData.get(JsonKeys.RELATIONSHIP_INCOMING_STATUS).getAsString());
            currentRelationship.setOutgoing_status(relationshipResponseData.get(JsonKeys.RELATIONSHIP_OUTGOING_STATUS).getAsString());
            currentRelationship.setTarget_user_is_private(relationshipResponseData.get(JsonKeys.RELATIONSHIP_TARGET_USER_PRIVATE).getAsBoolean());

            relationships.add(currentRelationship);
        //}
        return relationships;
    }
}
