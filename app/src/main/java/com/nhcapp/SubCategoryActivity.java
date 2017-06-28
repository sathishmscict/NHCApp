package com.nhcapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nhcapp.adapter.CategoryAdapterRecyclerView;
import com.nhcapp.app.MyApplication;
import com.nhcapp.database.dbhandler;
import com.nhcapp.helper.AllKeys;
import com.nhcapp.pojo.Category;
import com.nhcapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class SubCategoryActivity extends AppCompatActivity {

    private Context context = this;
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private SpotsDialog spotsDialog;
    private TextView tvnodata;
    private ArrayList<Category> list_subcateogry = new ArrayList<Category>();
    private RecyclerView rv_subcategory;
    private String TAG = SubCategoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(back_button);
        } catch (Exception e) {
            e.printStackTrace();
        }


        sessionmanager = new SessionManager(getApplicationContext());
        userDetails = sessionmanager.getSessionDetails();


        setTitle(userDetails.get(SessionManager.KEY_CATEGORY_NAME));
        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);


        tvnodata = (TextView) findViewById(R.id.tvnodata);
        rv_subcategory = (RecyclerView) findViewById(R.id.rv_subcategory);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv_subcategory.setLayoutManager(layoutManager);
        rv_subcategory.setItemAnimator(new DefaultItemAnimator());


        rv_subcategory.addOnItemTouchListener(new dbhandler.RecyclerTouchListener(getApplicationContext(), rv_subcategory, new dbhandler.ClickListener() {
            @Override
            public void onClick(View view, int position) {




                sessionmanager.setSubCategoryTypeAndIdDetails(String.valueOf(list_subcateogry.get(position).getCategoryid()), list_subcateogry.get(position).getCategoryname(),"subcategory");


                if(!list_subcateogry.get(position).getTotalproducts().equals("0"))
                {

                    Intent intent = new Intent(context, ItemDisplayActivity.class);
                    /*intent.putExtra(AllKeys.ACTIVITYNAME , "SubCategory");

                    intent.putExtra(AllKeys.CATEGORYID ,String.valueOf(getIntent().getStringExtra(AllKeys.CATEGORYID)));
*/
                    //intent.putExtra("CATEGORYID", String.valueOf(list_subcateogry.get(position).getCategoryid()));
                 //   intent.putExtra("CATEGORYNAME", String.valueOf(list_subcateogry.get(position).getCategoryname()));
                    startActivity(intent);
                    finish();

                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubCategoryActivity.this);
                    alertDialogBuilder.setTitle("Products Information");
                    alertDialogBuilder.setMessage("Sorry, no products are avalable in "+list_subcateogry.get(position).getCategoryname());
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {

                                    dialog.cancel();
                                    dialog.dismiss();
                                    //txtStatus.setVisibility(View.GONE);


                                }
                            });


                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();;



                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        getCategoryMasterDetailsFromServer();


    }

    private void getCategoryMasterDetailsFromServer() {
        showDialog();
        String url = AllKeys.WEBSITE + "GetJSONForCategoryData?type=Category&branch=" + userDetails.get(SessionManager.KEY_COMPANY_ID) + "";
        Log.d(TAG, "URL GetJSONForCategoryData : " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response GetJSONForCategoryData : " + response);
                try {

                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    list_subcateogry.clear();
                    if (error_status == false) {
                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_CATEGORY);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);


                                Integer category_id = c.getInt(AllKeys.TAG_CATEGORYID);
                                String category_name = c.getString(AllKeys.TAG_CATEGORY_NAME);
                                String category_image = c.getString(AllKeys.TAG_CATEGORY_IMAGE);

                                String parentid = c.getString(AllKeys.TAG_PARENTID);
                                String totalProducts =c.getString(AllKeys.TAG_TOTAL_RECORDS);

                                String totalSubcategpries =c.getString(AllKeys.TAG_TOTAL_SUB_CATEGORIES);





                                Category car = new Category(category_id, category_name, category_image, parentid,totalProducts,totalSubcategpries);


                                Log.d(TAG, userDetails.get(sessionmanager.KEY_CATEGORY_ID)+ " = "+parentid );
                                try {
                                    if(userDetails.get(sessionmanager.KEY_CATEGORY_ID).equals(parentid))
                                    {
                                        list_subcateogry.add(car);
                                    }

                                    /*if (getIntent().getIntExtra(AllKeys.CATEGORYID,0) == Integer.parseInt(parentid)) {


                                    }
*/                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }


                            tvnodata.setVisibility(View.GONE);
                            rv_subcategory.setVisibility(View.VISIBLE);


                            CategoryAdapterRecyclerView adapter = new CategoryAdapterRecyclerView(context, list_subcateogry);
                            rv_subcategory.setAdapter(adapter);


                            hideDialog();


                        } else {

                            tvnodata.setVisibility(View.VISIBLE);
                            rv_subcategory.setVisibility(View.GONE);
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "" + str_error, Toast.LENGTH_SHORT).show();
                        //   Snackbar.make(coordinatorLayout, str_error, Snackbar.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    hideDialog();

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.d(TAG, "GetJSONForCategoryData Error  : " + error.getMessage());
                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {
                    getCategoryMasterDetailsFromServer();
                }
            }
        }
        );
        MyApplication.getInstance().addToRequestQueue(request);

    }


    public void showDialog() {

        try {
            if (!spotsDialog.isShowing()) {

                spotsDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void hideDialog() {
        try {
            if (spotsDialog.isShowing()) {
                spotsDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(context, CategoryAcitivity.class);
            startActivity(intent);
            finish();


        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, CategoryAcitivity.class);
        startActivity(intent);
        finish();

    }


}
