package com.jduenas.petagram.notifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by jduenas on 18/01/2017.
 */

public class NotificacionIDTokenService extends FirebaseInstanceIdService {

    private static final String TAG = "NotificacionIDTokenServ";

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: " + token);
    }
}
