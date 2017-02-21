package com.jduenas.petagram.notifications;

//import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
//import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.jduenas.petagram.MainActivity;
import com.jduenas.petagram.R;
import com.jduenas.petagram.pojo.Relationship;
import com.jduenas.petagram.restApi.EndpointsApi;
import com.jduenas.petagram.restApi.adapter.RestApiAdapter;
import com.jduenas.petagram.restApi.model.RelationshipResponse;
import com.jduenas.petagram.restApi.model.UsuarioResponse;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jduenas on 18/01/2017.
 */

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";
    private static final int NOTIFICATION_ID = 001;
    String actionRelationship = "";


    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        //super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //consultar la relacion con la persona que dio like a la foto
            RestApiAdapter restApiAdapter = new RestApiAdapter();
            Gson gsonRelationship = restApiAdapter.construyeGsonDeserializadorRelationship();
            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApiInstagram(gsonRelationship);

            Call<RelationshipResponse> ralationshipResponseCall = endpointsApi.getRelationshipToUser(remoteMessage.getData().get("liked_by_userid"));
            Log.i("CALLLL",": "+ralationshipResponseCall.request());

            ralationshipResponseCall.enqueue(new Callback<RelationshipResponse>() {
                @Override
                public void onResponse(Call<RelationshipResponse> call, Response<RelationshipResponse> response) {
                    RelationshipResponse relationshipResponse = response.body();
                    ArrayList<Relationship> relationships = relationshipResponse.getRelationships();
                    Log.i("body","responsebody: "+relationships.get(0).toString());
                    createNotifications(relationships.get(0), remoteMessage);
                }

                @Override
                public void onFailure(Call<RelationshipResponse> call, Throwable t) {
                    Log.i("getRelationshipToUser","FALLO LA CONEXION: "+t.toString());
                }
            });
        }


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


    }
    private void createNotifications(Relationship relationship, RemoteMessage remoteMessage){
        actionRelationship = (relationship.getOutgoing_status().equals("follows"))?"unfollow":"follow";

        Uri Sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Intent intentAccion1 = new Intent();
        intentAccion1.setAction("ver_mi_perfil");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intentAccion1,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentAccion2 = new Intent();
        intentAccion2.setAction(actionRelationship);
        intentAccion2.putExtra("user-id",remoteMessage.getData().get("liked_by_userid"));
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this,1,intentAccion2,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentAccion3 = new Intent();
        intentAccion3.setAction("ver_usuario");
        intentAccion3.putExtra("user-id",remoteMessage.getData().get("liked_by_userid"));
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this,1,intentAccion3,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_full_handup_50,getString(R.string.texto_accion_toque),pendingIntent).build();
        NotificationCompat.Action action2 =
                new NotificationCompat.Action.Builder(R.drawable.ic_full_handup_50,actionRelationship,pendingIntent2).build();
        NotificationCompat.Action action3 =
                new NotificationCompat.Action.Builder(R.drawable.ic_full_handup_50,getString(R.string.texto_accion_ver_usuario),pendingIntent3).build();

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.fondopantalla))
                        .setGravity(Gravity.CENTER_VERTICAL)
                ;

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notifications_48)
                .setContentTitle("Notificacion")
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(Sonido)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .extend(wearableExtender.addAction(action))
                .extend(wearableExtender.addAction(action2))
                .extend(wearableExtender.addAction(action3))
                //.addAction(R.drawable.ic_full_handup_50,getString(R.string.texto_accion_toque),pendingIntent)
                ;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID,notificationCompat.build());

    }
}
