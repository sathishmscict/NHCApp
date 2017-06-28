package com.nhcapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nhcapp.app.MyApplication;
import com.nhcapp.helper.AllKeys;
import com.nhcapp.session.SessionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Satish Gadde on 02-09-2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        Context context = this;


    }

    public String onTokenRefreshNew(Context context) {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token new : " + refreshedToken);


        return refreshedToken;
        //sendRegistrationToServer(refreshedToken, context);

    }



}
