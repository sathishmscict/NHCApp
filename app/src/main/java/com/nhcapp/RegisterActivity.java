package com.nhcapp;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SignUpEvent;
import com.nhcapp.app.MyApplication;
import com.nhcapp.database.dbhandler;
import com.nhcapp.helper.AllKeys;
import com.nhcapp.helper.Utility;
import com.nhcapp.session.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;


public class RegisterActivity extends AppCompatActivity {

    private Context context = this;

    private EditText edtEmail;
    private TextInputLayout edtEmailWrapper;
    private TextInputLayout edtmobileWrapper;
    private EditText edtMobile;
    private TextInputLayout edtpasswordWrapper;
    private EditText edtPassword;
    private RadioGroup rdGrpGender;
    private Button btnSignup;
    private String TAG = RegisterActivity.class.getSimpleName();
    private String GENDER_ID = "1";
    private SpotsDialog progress_dialog;
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();


    private static final int PROFILE_PIC_SIZE = 120;

    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;

    private String facebook = "";

    public static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 100;


    private String PROVIDER_USERID = "0";
    private TextView txtError;
    private Button btnSignin;
    private TextInputLayout edtAddressWrapper;
    private EditText edtAddress;
    private TextInputLayout edtFirstnameWrapper;
    private EditText edtFirstname;
    private TextInputLayout edtLastnameWrapper;
    private EditText edtLastname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // String pass = BCrypt.hashpw("ze@123", BCrypt.gensalt());


        sessionmanager = new SessionManager(context);
        userDetails = sessionmanager.getSessionDetails();


        setTitle(getString(R.string.app_name) + " Registration");


        progress_dialog = new SpotsDialog(context);
        progress_dialog.setCancelable(false);

        edtFirstnameWrapper = (TextInputLayout) findViewById(R.id.edtFirstnameWrapper);
        edtFirstname = (EditText) findViewById(R.id.edtFirstname);

        edtLastnameWrapper = (TextInputLayout) findViewById(R.id.edtLastnameWrapper);
        edtLastname = (EditText) findViewById(R.id.edtLastname);


        edtEmailWrapper = (TextInputLayout) findViewById(R.id.edtEmailWrapper);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        edtmobileWrapper = (TextInputLayout) findViewById(R.id.edtmobileWrapper);
        edtMobile = (EditText) findViewById(R.id.edtMobile);

        edtpasswordWrapper = (TextInputLayout) findViewById(R.id.edtpasswordWrapper);
        edtPassword = (EditText) findViewById(R.id.edtpassword);

        edtAddressWrapper = (TextInputLayout) findViewById(R.id.edtAddressWrapper);
        edtAddress = (EditText) findViewById(R.id.edtAddress);


        rdGrpGender = (RadioGroup) findViewById(R.id.rdGrpGender);

        btnSignup = (Button) findViewById(R.id.btnSignup);

        btnSignin = (Button) findViewById(R.id.btnSignin);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        txtError = (TextView) findViewById(R.id.txterror);
        txtError.setVisibility(View.GONE);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean IsError = false;
                if (edtFirstname.getText().toString().equals("")) {
                    IsError = true;
                    edtFirstnameWrapper.setErrorEnabled(true);
                    edtFirstnameWrapper.setError("Enter Firstname");
                } else {
                    edtFirstnameWrapper.setErrorEnabled(false);

                }

                if (edtLastname.getText().toString().equals("")) {
                    IsError = true;
                    edtLastnameWrapper.setErrorEnabled(true);
                    edtLastnameWrapper.setError("Enter Lastname");
                } else {
                    edtLastnameWrapper.setErrorEnabled(false);

                }


                if (edtEmail.getText().toString().equals("")) {
                    IsError = true;
                    edtEmailWrapper.setErrorEnabled(true);
                    edtEmailWrapper.setError("Enter Email");
                } else {
                    if (AllKeys.checkEmail(edtEmail.getText().toString())) {
                        edtEmailWrapper.setErrorEnabled(false);
                    } else {
                        IsError = true;
                        edtEmailWrapper.setErrorEnabled(true);
                        edtEmailWrapper.setError("Invalid Email");

                    }


                }

                if (edtMobile.getText().toString().equals("")) {
                    IsError = true;
                    edtmobileWrapper.setErrorEnabled(true);
                    edtmobileWrapper.setError("Enter Mobile");
                } else {

                    if (edtMobile.getText().toString().length() != 10) {
                        edtmobileWrapper.setErrorEnabled(true);
                        edtmobileWrapper.setError("Invalid mobileno");
                        IsError = true;
                    } else {
                        edtmobileWrapper.setErrorEnabled(false);

                    }


                }

                if (edtPassword.getText().toString().equals("")) {
                    IsError = true;
                    edtpasswordWrapper.setErrorEnabled(true);
                    edtpasswordWrapper.setError("Enter Password");
                } else {
                    edtpasswordWrapper.setErrorEnabled(false);

                }
                if (rdGrpGender.getCheckedRadioButtonId() == R.id.rdMale) {
                    GENDER_ID = "Male";
                } else if (rdGrpGender.getCheckedRadioButtonId() == R.id.rdFemale) {
                    GENDER_ID = "Female";
                } else {
                    Toast.makeText(context, "select gender", Toast.LENGTH_SHORT).show();
                    IsError = true;
                }

                if (IsError == false) {

                    // Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();


                   /* if (Utility.checkPermission_ReadSMS(context) == true) {

                        edtEmailWrapper.setEnabled(false);
                        edtmobileWrapper.setEnabled(false);


                    }*/

                    sendUserDetailsToServer();


                }


            }
        });




        /*Drawable drawable = edtUsername.getBackground(); // get current EditText drawable
        drawable.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP); // change the drawable color

        if(Build.VERSION.SDK_INT > 16) {
            edtUsername.setBackground(drawable); // set the new drawable to EditText

        }else{
            edtUsername.setBackgroundDrawable(drawable); // use setBackgroundDrawable because setBackground required API 16

        }
*/


    }
    //onCreate Completed

    private void sendUserDetailsToServer() {


        showDialog();


        txtError.setVisibility(View.GONE);

        String url_signupuser = AllKeys.WEBSITE + "InsertUserData";





        Log.d(TAG, "URL SignupUser  :" + url_signupuser);
        StringRequest str_request_signup = new StringRequest(Request.Method.POST, url_signupuser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Log.d(TAG, "Singup Response : " + response);
                if (response.toLowerCase().contains("mobile no already exist")) {

                        edtmobileWrapper.setEnabled(true);
                        edtmobileWrapper.setError("Mobile no already exist");



                } else if (response.toLowerCase().contains("email id already exist")) {

                        edtEmailWrapper.setEnabled(true);
                        edtEmailWrapper.setError("Email id already exist");


                } else if (response.contains("fname"))
                {
                    try {
                        //response = dbhandler.convertToJsonFormat(response);
                        JSONObject obj = new JSONObject(response);
                        JSONArray arr = obj.getJSONArray(AllKeys.ARRAY_LOGINDATA);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject c = arr.getJSONObject(i);


                            String userid = c.getString(AllKeys.TAG_USER_ID);
                            String firstname = c.getString(AllKeys.TAG_FIRST_NAME);
                            String lastname = c.getString(AllKeys.TAG_LASTNAME);
                            String userAddresss = c.getString(AllKeys.TAG_ADDRESS);
                            String userMobile = c.getString(AllKeys.TAG_MOBILENO);
                            String verificationStatus = c.getString(AllKeys.TAG_VERIFICATION_STATUS);
                            String user_Avatar = null;
                            try {
                                user_Avatar = c.getString(AllKeys.TAG_USER_AVATAR);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String user_Email = c.getString(AllKeys.TAG_USER_EMAIL);

                            String user_Gender = c.getString(AllKeys.TAG_USER_GENDER);

                            //(String str_userid, String str_firstname, String str_lastname, String str_avatar, String str_email,  String str_mobile, String str_verified_mobile,String address) {
                            sessionmanager.setUserDetails(userid, firstname, lastname, user_Avatar, user_Email, userMobile, verificationStatus, userAddresss,user_Gender);


                            Answers.getInstance().logSignUp(new SignUpEvent()
                                    .putMethod("Signup User")
                                    .putCustomAttribute("UserName : ", firstname + lastname)
                                    .putCustomAttribute("UserMobile : ", userMobile)
                                    .putCustomAttribute("UserId : ", userid)
                                    .putCustomAttribute("UserEmail : ", user_Email)

                                    .putSuccess(true));


                        }


                        Intent ii = new Intent(context, VerificationActivity.class);
                        startActivity(ii);
                        finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(context, "Try again...", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                } else {
                    sendUserDetailsToServer();
                }

                //  sendUserDetailsToServer();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<String, String>();



                //params.put("username", dbhandler.convertEncodedString(edtUsername.getText().toString()));

                    params.put("email", dbhandler.convertEncodedString(edtEmail.getText().toString()));
                    params.put("password", dbhandler.convertEncodedString(edtPassword.getText().toString()));
                    params.put("phone", edtMobile.getText().toString());
                    params.put("firstname", dbhandler.convertEncodedString(edtFirstname.getText().toString()));
                    params.put("lastname", dbhandler.convertEncodedString(edtLastname.getText().toString()));
                params.put("address", dbhandler.convertEncodedString(edtAddress.getText().toString()));

                // params.put("password", dbhandler.convertEncodedString(BCrypt.hashpw(edtPassword.getText().toString(), BCrypt.gensalt())));
                //BCrypt.hashpw("ze@123", BCrypt.gensalt());

                params.put("gender", GENDER_ID);

                params.put("device_type", "android");


                Log.d(TAG, "Params :" + params.toString());

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(str_request_signup);


    }


    public void CallLoginIntent(View view) {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void showDialog() {
        if (!progress_dialog.isShowing()) {
            progress_dialog.show();

        }
    }

    private void hideDialog() {
        if (progress_dialog.isShowing()) {
            progress_dialog.dismiss();


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {

            Intent i = new Intent(context, LoginActivity.class);
            startActivity(i);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = null;
        try {
            i = new Intent(context, LoginActivity.class);

            startActivity(i);
            finish();
        } catch (Exception e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Error in Converting Class : " + e.getMessage());
            e.printStackTrace();
        }


    }



}
