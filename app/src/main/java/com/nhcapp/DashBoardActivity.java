package com.nhcapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.nhcapp.app.MyApplication;
import com.nhcapp.database.dbhandler;
import com.nhcapp.fragments.FragmentHome;
import com.nhcapp.fragments.FragmentOrders;
import com.nhcapp.fragments.FragmentProfile;
import com.nhcapp.helper.AllKeys;
import com.nhcapp.helper.CustomRequest;
import com.nhcapp.helper.Utils;
import com.nhcapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private TextView txtname;
    private TextView txtemail;
    private ImageView imgProfilePic;
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails= new HashMap<String, String>();
    private Menu menu;
    private Context context =this;
    private String TAG = DashBoardActivity.class.getSimpleName();
    private SpotsDialog pDialog;

    private ArrayList<String> list_cartItemsId = new ArrayList<String>();
    private MenuItem cart;

    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


     /*   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemS000electedListener(this);*/


        sessionmanager = new SessionManager(getApplicationContext());
        userDetails = sessionmanager.getSessionDetails();


       // sessionmanager.checkLogin();

        pDialog =  new SpotsDialog(context);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //app:headerLayout="@layout/nav_header_menu"
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_dash_board);

        try {


            txtname = (TextView) headerLayout.findViewById(R.id.txtname);
            txtemail = (TextView) headerLayout.findViewById(R.id.txtemail);
            imgProfilePic = (ImageView) headerLayout.findViewById(R.id.imgProfilePic);

            SetUserProfilePictireFromBase64EnodedString();


            imgProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Fragment fragment = new FragmentProfile();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();


                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);*/


                    getMenuInflater().inflate(R.menu.activity_dash_board_drawer, menu);
                    MenuItem mProfileFrag = menu.findItem(R.id.nav_profile);

                    onNavigationItemSelected(mProfileFrag);


                    /*MenuItem mDefaultFrag = (MenuItem) navigationView.findViewById(R.id.nav_profile);
                    onNavigationItemSelected(mDefaultFrag);*/


                }
            });


            txtemail.setText("" + userDetails.get(SessionManager.KEY_USER_EMAIL));
            txtname.setText("" + userDetails.get(SessionManager.KEY_USER_FIRSTNAME));
        } catch (Exception e) {
            e.printStackTrace();
        }



        Intent ii = getIntent();
        String ProfileUpdateData = "" + ii.getStringExtra("PROFILEUPDATE");
        if (ProfileUpdateData.equals("TRUE"))
        {

            Fragment fragment = new FragmentProfile();
            //getSupportActionBar().setTitle(getString(R.string.app_name));


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle("Profile");
            setTitle("Profile");


                /*MenuItem mDefaultFrag = (MenuItem) findViewById(R.id.nav_profile);
                onNavigationItemSelected(mDefaultFrag);*/


        } else {

            Fragment fragment = new FragmentHome();
            //getSupportActionBar().setTitle(getString(R.string.app_name));


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

                getSupportActionBar().setTitle(getString(R.string.app_name));

        }


        if(userDetails.get(SessionManager.KEY_USERID).equals("0"))
        {

            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();

        }
        else if (userDetails.get(SessionManager.KEY_VERSTATUS).equals("0") && !userDetails.get(SessionManager.KEY_USERID).equals("0")) {
            Intent intent = new Intent(context, VerificationActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            getAllCartItemDetailsFromServer();
            UpdateFcmTokenDetailsToServer();
            setAddToCartBadget();
        }




    }


    public void showDialog() {

        pDialog.setCancelable(false);

        try {
            if (!pDialog.isShowing()) {

                pDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void hideDialog() {
        try {
            if (pDialog.isShowing()) {
                pDialog.dismiss();

            }
           /* if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Update FCM token onm service
     */

    private void UpdateFcmTokenDetailsToServer()
    {
        showDialog();


        //String url = AllKeys.WEBSITE + "updateSellerFCM?device_type=android&sellerid=" + userDetails.get(SessionManager.KEY_SELLER_ID) + "&fcm=" + fcm_tokenid + "";
        String url = AllKeys.WEBSITE + "InsertFCMToken";

        Log.d(TAG, "URL  InsertFCMToken : " + url);


        CustomRequest request = new CustomRequest(Request.Method.POST, url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response InsertFCMToken : " + response.toString());
                hideDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                } else {
                    UpdateFcmTokenDetailsToServer();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();

                String fcm_tokenid = "";
                try {
                    MyFirebaseInstanceIDService mid = new MyFirebaseInstanceIDService();
                    fcm_tokenid = mid.onTokenRefreshNew(context);

                } catch (Exception e) {
                    fcm_tokenid = "";
                    e.printStackTrace();
                }


                params.put("type", "fcmtoken");
                params.put("userid", userDetails.get(SessionManager.KEY_USERID));
                params.put("fcmtoken", fcm_tokenid);

                Log.d(TAG, "Update FCM Params :" + params.toString());


                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);


    }
    //Complete Update FCM on Server
    private void getAllCartItemDetailsFromServer()
    {


        showDialog();

        String url_addtowishlist = AllKeys.WEBSITE + "ViewCartData?type=cartdata&userid=" + userDetails.get(SessionManager.KEY_USERID) + "";
        Log.d(TAG, "URL ViewCartData : " + url_addtowishlist);
        final JsonObjectRequest str_manageCart = new JsonObjectRequest(Request.Method.GET, url_addtowishlist, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response str_manageCart : " + response);

                list_cartItemsId.clear();
                try
                {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {
                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_CARTDATA);

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);

                                list_cartItemsId.add(c.getString(AllKeys.TAG_CART_PRODUCTID));


                            }
                            if (list_cartItemsId.size() == 0) {


                                sessionmanager.setCartItemsIdDetails("0");
                            } else {
                                String data = list_cartItemsId.toString();

                                data = data.substring(1, data.length() - 1);
                                sessionmanager.setCartItemsIdDetails(data);
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                    setAddToCartBadget();
                }


                setAddToCartBadget();
                hideDialog();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {
                    getAllCartItemDetailsFromServer();
                }

                setAddToCartBadget();

                Log.d(TAG, "Error in ManageWishList : " + error.getMessage());

            }
        });
        MyApplication.getInstance().addToRequestQueue(str_manageCart);


    }

    private void setAddToCartBadget() {

        //    String query = "select * from " + db.TABLE_ORDER;


        try {
            LayerDrawable icon = (LayerDrawable) cart.getIcon();


            Utils.setBadgeCount(DashBoardActivity.this, icon, list_cartItemsId.size());
            // startCountAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void SetUserProfilePictireFromBase64EnodedString() {
        try {
            userDetails = sessionmanager.getSessionDetails();
            String myBase64Image = userDetails.get(SessionManager.KEY_ENODEDED_STRING);
            if (!myBase64Image.equals("")) {


                Bitmap myBitmapAgain = dbhandler.decodeBase64(myBase64Image);

                imgProfilePic.setImageBitmap(myBitmapAgain);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Decode Img Exception : ", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;


        getMenuInflater().inflate(R.menu.dash_board, menu);

        cart = (MenuItem) menu.findItem(R.id.menu_addtocart);

        if (userDetails.get(SessionManager.KEY_VERSTATUS).equals("1") && !userDetails.get(SessionManager.KEY_USERID).equals("0"))
        {
            getAllCartItemDetailsFromServer();
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_addtocart) {


            if(list_cartItemsId.size() != 0)
            {

                Intent intent = new Intent(context, CheckoutActivity.class);
                startActivity(intent);
                finish();

            }
            else
            {
                Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else if (item.getItemId() == R.id.menu_search) {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("ActivityName", TAG);
            startActivity(intent);
            finish();

            //Toast.makeText(context, "Search Activity", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    public void setupFragment(Fragment fragment, String title)
    {
        setTitle(title);

        if (fragment != null) {


            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();


        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragment = new FragmentHome();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }


    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SetUserProfilePictireFromBase64EnodedString();

        if (id == R.id.nav_home) {

           Fragment f= new FragmentHome();

            setupFragment(f,getString(R.string.app_name));


        } else if (id == R.id.nav_orderhistory) {


            Fragment f= new FragmentOrders();

            setupFragment(f,"Order History");



        } else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey install the app here - https://play.google.com/store/apps/details?id="+ context.getPackageName() +"");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);


        }
        else if (id == R.id.nav_profile) {
            Fragment f= new FragmentProfile();

            setupFragment(f,"Profile");

        }
        else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_contactus) {

            Intent intent = new Intent(context, ContactUsActivity.class);
            startActivity(intent);
            finish();


        } else if (id == R.id.nav_logout) {


            sessionmanager.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
