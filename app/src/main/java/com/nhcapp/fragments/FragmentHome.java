package com.nhcapp.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.nhcapp.CategoryAcitivity;
import com.nhcapp.DashBoardActivity;
import com.nhcapp.R;
import com.nhcapp.session.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import dmax.dialog.SpotsDialog;

import static android.content.Context.ALARM_SERVICE;


public class FragmentHome extends android.support.v4.app.Fragment {




    private Context context = getActivity();


    private SessionManager sessionmanager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();



    private String TAG = FragmentHome.class.getSimpleName();
    private SpotsDialog pDialog;

    private ImageButton imgMenu;
    private LinearLayout ll_shah_agency;
    private LinearLayout ll_nh_corporation;
    private LinearLayout ll_shah_enterprise;
    private TextView tvnhcorporation;


    public FragmentHome() {
        // Required empty public constructor
    }

	/*
     * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); setHasOptionsMenu(true);
	 * 
	 * 
	 * }
	 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container,
                false);

        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        pDialog = new SpotsDialog(getActivity());
        pDialog.setCancelable(false);


        sessionmanager = new SessionManager(getActivity());
        userDetails = sessionmanager.getSessionDetails();



        ll_shah_agency=(LinearLayout)rootView.findViewById(R.id.li1);
        ll_nh_corporation=(LinearLayout)rootView.findViewById(R.id.li2);
        ll_shah_enterprise=(LinearLayout)rootView.findViewById(R.id.li3);


        ll_shah_agency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sessionmanager.setCompnayTypeAndIdDetails("2","Shah Agency");

                Intent intent = new Intent(getActivity(), CategoryAcitivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });


        ll_nh_corporation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sessionmanager.setCompnayTypeAndIdDetails("1","NH Corporation");
                Intent intent = new Intent(getActivity(), CategoryAcitivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        ll_shah_enterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                sessionmanager.setCompnayTypeAndIdDetails("3","Shah Enterprise");
                Intent intent = new Intent(getActivity(), CategoryAcitivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
    //onCreateView Completed


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // pDialog.dismiss();
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
    }




}