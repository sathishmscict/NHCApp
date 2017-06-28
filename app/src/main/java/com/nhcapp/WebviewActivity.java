package com.nhcapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nhcapp.helper.NetConnectivity;
import com.nhcapp.session.SessionManager;

import java.util.HashMap;

public class WebviewActivity extends AppCompatActivity /*implements SwipeRefreshLayout.OnRefreshListener*/ {

    private WebView wv;
    private Context context = this;
    private String title, url;
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private ProgressBar progress;
    private CoordinatorLayout coordinatorLayout;
    /*private SwipeRefreshLayout swipeRefreshLayout;*/
    private String activityname="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            //getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);






       /* swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.red), getResources().getColor(R.color.green), getResources().getColor(R.color.blue));

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(WebviewActivity.this);*/

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
      /*  swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {


                                        if (NetConnectivity.isOnline(context)) {
                                            //new AsyncData1().execute();
                                            swipeRefreshLayout.setRefreshing(true);
                                            if (swipeRefreshLayout.isRefreshing()) {
                                                swipeRefreshLayout.setRefreshing(false);
                                            }

                                            LoadWebVieeFromURL();
                                            //  new SendAllDetialToServer().execute();
                                            Log.d("oncreate", "from run method");


                                        } else {
                                            swipeRefreshLayout.setRefreshing(false);
                                            checkInternet();
                                        }
                                    }
                                }
        );*/


        wv = (WebView) findViewById(R.id.wv);

        progress = (ProgressBar) findViewById(R.id.progressBar);

        progress.setMax(100);
        progress.setVisibility(View.VISIBLE);

        sessionmanager = new SessionManager(context);
        userDetails = sessionmanager.getSessionDetails();



        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!NetConnectivity.isOnline(context)) {

            checkInternet();

        }

        try {


            Intent ii = getIntent();
            title = ii.getStringExtra("TITLE");
            setTitle(title);
            url = ii.getStringExtra("URL");
            //url="http://radiant.dnsitexperts.com/result.aspx?deptid=9&empid=342";
            Log.d("WebviewActivity URL :", url);
            activityname = ii.getStringExtra("ACTIVITY");
            Log.d("Activity Name :", ""+activityname);

        } catch (Exception e) {
            Log.d("Error in ", "WebViewActivity : " + e.getMessage());
        }


        if (NetConnectivity.isOnline(context)) {

            LoadWebVieeFromURL();


        } else {
            Toast.makeText(context, getString(R.string.no_network), Toast.LENGTH_SHORT).show();

        }


    }

    private void LoadWebVieeFromURL() {


        try {
            wv.getSettings().setJavaScriptEnabled(true);
            //  wv.getSettings().setDisplayZoomControls(true);
            wv.getSettings().setAppCacheEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

                if(activityname.toLowerCase().equals("singleventdisplayactivity"))
                {
                    wv.getSettings().setDisplayZoomControls(true);
                    wv.getSettings().setBuiltInZoomControls(true);
                    //wv.getSettings().setSupportZoom(true);
                    wv.getSettings().setLoadsImagesAutomatically(true);
                    wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }


        wv.loadUrl(url);
        wv.setWebViewClient(new MyWebViewClient());
        WebviewActivity.this.progress.setProgress(0);

    }

    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            if (Uri.parse(url).getHost().contains("http") || Uri.parse(url).getHost().contains("http://") || Uri.parse(url).getHost().contains("ims")) {
                //open url contain in webview
                return false;

            }

           /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);*/

            return false;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setVisibility(View.GONE);
            WebviewActivity.this.progress.setProgress(100);
            super.onPageFinished(view, url);
           // swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //
            // progress.setVisibility(View.VISIBLE);
           // swipeRefreshLayout.setRefreshing(true);
            WebviewActivity.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }








   /* public boolean onKeyDownssdadasdsad(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    if(wv.canGoBack())
                    {
                        wv.goBack();

                    }
                    else
                    {
                        finish();
                    }
                    return true;
            }

        }
        return onKeyDown(keyCode, event);
    }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent ii = new Intent(getApplicationContext(), SingleItemActivity.class);



            startActivity(ii);
            finish();
            overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent ii = new Intent(getApplicationContext(), SingleItemActivity.class);
        startActivity(ii);
        finish();
        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

    }

  /*  @Override
    public void onRefresh() {
        if (NetConnectivity.isOnline(context)) {
            //new AsyncData1().execute();
            //  swipeRefreshLayout.setColorSchemeColors(R.color.color1, R.color.color2, R.color.color3);
            swipeRefreshLayout.setRefreshing(true);
            //swipeRefreshLayout.setColorSchemeColors(R.color.color1, R.color.color2, R.color.color3);

            Log.d("swipe from ", "onrefresh method");
            LoadWebVieeFromURL();


        } else {
            swipeRefreshLayout.setRefreshing(false);


            checkInternet();

        }
    }*/

    public void checkInternet() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WebviewActivity.this);
        alertDialogBuilder.setMessage(getString(R.string.no_network3));

        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
