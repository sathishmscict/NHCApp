package com.nhcapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;
import com.google.android.youtube.player.YouTubeIntents;
import com.mzelzoghbi.zgallery.ZGallery;
import com.mzelzoghbi.zgallery.entities.ZColor;
import com.nhcapp.adapter.ProductImagesRecyclerViewAdapter;
import com.nhcapp.animation.ShakeAnimation;
import com.nhcapp.app.MyApplication;
import com.nhcapp.database.dbhandler;
import com.nhcapp.helper.AllKeys;
import com.nhcapp.helper.NetConnectivity;
import com.nhcapp.helper.Utils;
import com.nhcapp.pojo.Category;
import com.nhcapp.pojo.ProductData;
import com.nhcapp.session.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class SingleItemActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView btnAddToWishList;
    private Button btnAddToCart;
    // private SubsamplingScaleImageView imgItem;
    private NetworkImageView imgItem;


    //private ImageView imgItem;


    private String TAG = SingleItemActivity.class.getSimpleName();


    private SpotsDialog pDialog;
    private Context context = this;
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private dbhandler db;

    private TextView txtProductName;
    private TextView txtProductPrice, txtProductMRP, txtOffer;
    private TextView txtDescr;
    private TextView txtDescrInfo;
    private RecyclerView recyclerview_images;

    public ArrayList<ProductData> list_ProductImages = new ArrayList<ProductData>();
    public ArrayList<String> list_Images = new ArrayList<String>();


    private String PRODUCT_NAME = "";
    private String PRODUCT_PRICE = "";
    private String QUANTITY = "1", UNIT_PRICE = "", PROMO_VALUE = "", IS_MLM = "0";
    private MenuItem cart;
    private CoordinatorLayout coordinateLayout;

    private ArrayList<String> list_cartItemsId = new ArrayList<String>();
    //private TextView txtAvailability;
    //private Button btnCheckPincode, btnCheckPincodeChange;
    // private TextInputLayout edtPincodeWrapper;
    // private EditText edtPincode;
    // private String CITY_NAME;

    private boolean Is_Pincode_Available = false;
    // private ViewPager viewPager;
    //private TabLayout tabLayout;
    private NestedScrollView mScrollView;
    // private LinearLayout llBeforePincode;
    //private LinearLayout llAfterPincode;
    private RecyclerView recyclerview_size;
    private boolean IS_PRODUCT_SIZE = false;

    private CardView crdSize;

    private ArrayList<String> list_productSize = new ArrayList<String>();
    private boolean IS_SIZE_SELECTED = false;
    private String SELECTED_SIZE = "";
    private Button btnBuyNow;
    private LinearLayout ll_mainUI;
    private LinearLayout ll_no_internet;
    private Button btnNoNetwork;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SQLiteDatabase sd;
    private LinearLayout llTechnicalData;
    private TextView tvTechnicalData;
    private TextView tvTechnicalDataDescr;
    private WebView wvTechnicalDataDescr;
    private boolean DESCR_FLAG = false, TECHNICAL_FLAG = false, BROCHURES_FLAG = false, WALLCHARTS_FLAG = false;
    private LinearLayout llBrochures;
    private TextView tvBrochures;
    private TextView tvBrochuresPDF;
    private LinearLayout llWallchart;
    private TextView tvWallchart;
    private TextView tvWallchartPDF;
    private String WALLCHART_PDF_URL = "", BROCHURE_PDF_URL = "";
    private ProductImagesRecyclerViewAdapter_New adapter_ProductImages;
    private String YOUTUBE_VIDEO_THUMBNAIL = "";
    private LinearLayout llPicturesAndClips;
    private TextView tvPicturesAndClips;
    private NetworkImageView imgVideosThumbnail;
    private boolean YOUTUBE_VIDEO_FLAG = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        setTitle("");
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);

        sessionmanager = new SessionManager(context);
        userDetails = sessionmanager.getSessionDetails();

        //Default Set Ad Multiple Products
        sessionmanager.setCheckoutType("multiple", userDetails.get(SessionManager.KEY_PRODUCT_ID));

        db = new dbhandler(context);
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();


        ll_mainUI = (LinearLayout) findViewById(R.id.llmainUI);
        ll_no_internet = (LinearLayout) findViewById(R.id.ll_no_internet);
        btnNoNetwork = (Button) findViewById(R.id.btnNoNetwork);

        btnNoNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadDataFromServer();
            }
        });



        /*imgItem = (ImageView) findViewById(R.id.imgItem);*/
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtProductMRP = (TextView) findViewById(R.id.txtProductMRP);
        txtProductPrice = (TextView) findViewById(R.id.txtProductPrice);
        txtOffer = (TextView) findViewById(R.id.txtOffer);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
        btnAddToWishList = (TextView) findViewById(R.id.txtWishLiast);


        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        crdSize = (CardView) findViewById(R.id.crdSize);


        recyclerview_images = (RecyclerView) findViewById(R.id.recyclerview_images);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerview_images.setLayoutManager(layoutManager2);
        recyclerview_images.setItemAnimator(new DefaultItemAnimator());


        txtDescr = (TextView) findViewById(R.id.txtDescr);
        txtDescrInfo = (TextView) findViewById(R.id.txtDescrInfo);
        txtDescrInfo.setVisibility(View.GONE);


        txtDescr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DESCR_FLAG == false) {
                    DESCR_FLAG = true;
                    txtDescr.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down, 0);
                    txtDescrInfo.setVisibility(View.VISIBLE);


                } else {
                    DESCR_FLAG = false;
                    txtDescr.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_move_to_next, 0);
                    txtDescrInfo.setVisibility(View.GONE);

                }


            }
        });

        mScrollView = (NestedScrollView) findViewById(R.id.srollview1);


        btnBuyNow = (Button) findViewById(R.id.btnBuyNow);


        llTechnicalData = (LinearLayout) findViewById(R.id.llTechnicalData);
        tvTechnicalData = (TextView) findViewById(R.id.tvTechnicalData);
        tvTechnicalDataDescr = (TextView) findViewById(R.id.tvTechnicalDataDescr);
        wvTechnicalDataDescr = (WebView) findViewById(R.id.wvTechnicalDataDescr);
        wvTechnicalDataDescr.setVisibility(View.GONE);

        wvTechnicalDataDescr.getSettings().setJavaScriptEnabled(true);
        wvTechnicalDataDescr.getSettings().setLoadWithOverviewMode(true);
        // wvTechnicalDataDescr.getSettings().setUseWideViewPort(true);
        wvTechnicalDataDescr.getSettings().setBuiltInZoomControls(true);
        wvTechnicalDataDescr.getSettings().setDisplayZoomControls(false);


        llTechnicalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TECHNICAL_FLAG == false) {
                    TECHNICAL_FLAG = true;
                    tvTechnicalData.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down, 0);
                    wvTechnicalDataDescr.setVisibility(View.VISIBLE);


                } else {
                    TECHNICAL_FLAG = false;
                    tvTechnicalData.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_move_to_next, 0);
                    wvTechnicalDataDescr.setVisibility(View.GONE);

                }


            }
        });


        llBrochures = (LinearLayout) findViewById(R.id.llBrochures);
        tvBrochures = (TextView) findViewById(R.id.tvBrochures);
        tvBrochuresPDF = (TextView) findViewById(R.id.tvBrochuresPDF);
        tvBrochuresPDF.setVisibility(View.GONE);

        llBrochures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BROCHURES_FLAG == false) {
                    BROCHURES_FLAG = true;
                    tvBrochures.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down, 0);
                    tvBrochuresPDF.setVisibility(View.VISIBLE);


                } else {
                    BROCHURES_FLAG = false;
                    tvBrochures.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_move_to_next, 0);
                    tvBrochuresPDF.setVisibility(View.GONE);

                }


            }
        });

        tvBrochuresPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("URL", "http://docs.google.com/gview?embedded=true&url=" + BROCHURE_PDF_URL);
                //intent.putExtra("URL", "http://www.homebethe.com/");
                intent.putExtra("TITLE", "Brouchure");
                intent.putExtra("ACTIVITY", "SingleItemActivity");


                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);


            }
        });


        llPicturesAndClips = (LinearLayout) findViewById(R.id.llPicturesAndClips);
        tvPicturesAndClips = (TextView) findViewById(R.id.tvPicturesAndClips);
        imgVideosThumbnail = (NetworkImageView) findViewById(R.id.imgVideosThumbnail);
        imgVideosThumbnail.setVisibility(View.GONE);
        imgVideosThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String version = YouTubeIntents.getInstalledYouTubeVersionName(context);
                if (version != null) {
                    String text = String.format(getString(R.string.youtube_currently_installed), version);
                    //youTubeVersionText.setText(text);
                    YouTubeIntents.canResolvePlayVideoIntent(context);

                    Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(context, YOUTUBE_VIDEO_THUMBNAIL, true, false);
                    startActivity(intent);

                } else {
//                    youTubeVersionText.setText(getString(R.string.youtube_not_installed));
                    //Toast.makeText(context, "Youtube not installed", Toast.LENGTH_SHORT).show();
                    Snackbar.make(coordinateLayout,"Youtube not installed",Snackbar.LENGTH_LONG).show();
                }
            }
        });


        llPicturesAndClips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (YOUTUBE_VIDEO_FLAG == false) {
                    YOUTUBE_VIDEO_FLAG = true;
                    tvPicturesAndClips.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down, 0);
                    imgVideosThumbnail.setVisibility(View.VISIBLE);


                } else {
                    YOUTUBE_VIDEO_FLAG = false;
                    tvPicturesAndClips.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_move_to_next, 0);
                    imgVideosThumbnail.setVisibility(View.GONE);

                }


            }
        });


        llWallchart = (LinearLayout) findViewById(R.id.llWallchart);
        tvWallchart = (TextView) findViewById(R.id.tvWallchart);
        tvWallchartPDF = (TextView) findViewById(R.id.tvWallchartPDF);
        tvWallchartPDF.setVisibility(View.GONE);

        llWallchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (WALLCHARTS_FLAG == false) {
                    WALLCHARTS_FLAG = true;
                    tvWallchart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down, 0);
                    tvWallchartPDF.setVisibility(View.VISIBLE);


                } else {
                    WALLCHARTS_FLAG = false;
                    tvWallchart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_move_to_next, 0);
                    tvWallchartPDF.setVisibility(View.GONE);

                }


            }
        });

        tvWallchartPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("URL", "http://docs.google.com/gview?embedded=true&url=" + WALLCHART_PDF_URL);
                //intent.putExtra("URL", "http://www.homebethe.com/");
                intent.putExtra("TITLE", "Wallchart");
                intent.putExtra("ACTIVITY", "SingleItemActivity");


                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);


            }
        });


        //Get Cart Item details from sesion
        try {
            if (!userDetails.get(SessionManager.KEY_CARTITEMS_ID).equals("")) {
                list_cartItemsId = new ArrayList<>(Arrays.asList(userDetails.get(SessionManager.KEY_CARTITEMS_ID).split(",")));

            }
            if (list_cartItemsId.indexOf(userDetails.get(SessionManager.KEY_PRODUCT_ID)) != -1) {
                btnAddToCart.setText("go to cart");
            } else {
                btnAddToCart.setText("add to cart");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        imgItem = (NetworkImageView) findViewById(R.id.imgItem);


        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //StartDispalyImages();

                if (list_Images.size() > 0) {
                    galleryActivity();
                } else {
                   // Toast.makeText(context, "No images found", Toast.LENGTH_SHORT).show();
                }


            }
        });


        btnAddToCart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (btnAddToCart.getText().toString().toLowerCase().equals("go to cart")) {

                            sessionmanager.setActivityName(TAG);

                                Intent ii = new Intent(context, CheckoutActivity.class);
                                startCountAnimation();
                                sessionmanager.setActivityName(userDetails.get(SessionManager.KEY_ACTIVITY_NAME));
                                sessionmanager.setCheckoutType("multiple", userDetails.get(SessionManager.KEY_PRODUCT_ID));
                                startActivity(ii);
                                finish();

                            Toast.makeText(context, "Checkout Activity", Toast.LENGTH_SHORT).show();

                        } else {

                            if (userDetails.get(SessionManager.KEY_USERID).equals("0")) {
                                sessionmanager.setNewUserSession("SingleItemActivity");
                                Intent intent = new Intent(context, LoginActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                manageAddToCartDetailsToServer(userDetails.get(SessionManager.KEY_PRODUCT_ID), QUANTITY, "insertcart");
                            }

                        }


                        setAddToCartBadget();


                    }
                });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (userDetails.get(SessionManager.KEY_USERID).equals("0")) {
                    sessionmanager.setNewUserSession("SingleItemActivity");
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    sessionmanager.setCheckoutType("single", userDetails.get(SessionManager.KEY_PRODUCT_ID));

                    manageAddToCartDetailsToServer(userDetails.get(SessionManager.KEY_PRODUCT_ID), QUANTITY, "add");
                }


            }
        });


        btnAddToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userDetails.get(SessionManager.KEY_USERID).equals("0")) {
                    sessionmanager.setNewUserSession("SingleItemActivity");
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {


                    userDetails = sessionmanager.getSessionDetails();

                    if (userDetails.get(SessionManager.KEY_WISHLIST_STATUS).equals("-1")) {

                        sendWishListDetailsToServer(db, sd, userDetails.get(SessionManager.KEY_PRODUCT_ID), PRODUCT_PRICE, "add");

                    } else {

                        sendWishListDetailsToServer(db, sd, userDetails.get(SessionManager.KEY_PRODUCT_ID), PRODUCT_PRICE, "remove");
                    }


                    btnAddToWishList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.clockwise_refresh));
                }


            }
        });


        recyclerview_size = (RecyclerView) findViewById(R.id.recyclerview_size);
        RecyclerView.LayoutManager layoutManager_size = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerview_size.setLayoutManager(layoutManager_size);
        recyclerview_size.setItemAnimator(new DefaultItemAnimator());


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);


        //  swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);


        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {


                                        if (NetConnectivity.isOnline(context)) {
                                            //new AsyncData1().execute();
                                          /*  swipeRefreshLayout.setRefreshing(true);
                                            if (swipeRefreshLayout.isRefreshing()) {
                                                swipeRefreshLayout.setRefreshing(false);
                                            }*/
                                            LoadDataFromServer();
                                            //  new SendAllDetialToServer().execute();
                                            Log.d("oncreate", "from run method");


                                        } else {
                                            // swipeRefreshLayout.setRefreshing(false);
                                            //checkInternet();
                                            Toast.makeText(context, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
        );


        hideNoInternetUI();


    }
    //onCreate Completed

    public class ProductImagesRecyclerViewAdapter_New extends RecyclerView.Adapter<ProductImagesRecyclerViewAdapter_New.MyViewHolder> {

        ImageLoader mImageLoader;
        private final Context _context;
        // private final ArrayList<ProductData> list_NewProcuts;
        private final LayoutInflater inflater;
        private final SessionManager sessionmanager;


        private HashMap<String, String> userDetails = new HashMap<String, String>();
        private String TAG = ProductImagesRecyclerViewAdapter.class.getSimpleName();


        // Start with first item selected
        //  private int focusedItem = 0;
        private int selectedItem = 0;

        public ProductImagesRecyclerViewAdapter_New(Context context) {
            this._context = context;
            // this.list_NewProcuts = listNewProduct;
            inflater = LayoutInflater.from(context);

            sessionmanager = new SessionManager(context);
            userDetails = sessionmanager.getSessionDetails();


        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            // private final RatingBar ratingBar;
            //  private final TextView txtPrice, txtName, txtDelete,txtProductMRP;
            private final NetworkImageView imgItem;
            //  private final CardView crdProduct;
            // private final LinearLayout myBackground;

            public MyViewHolder(View itemView) {
                super(itemView);
                //  crdProduct = (CardView) itemView.findViewById(R.id.crdProduct);
                imgItem = (NetworkImageView) itemView.findViewById(R.id.imgSingleItem);

            }

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View v = inflater.inflate(R.layout.row_single_product_images, parent, false);

            MyViewHolder viewFolder = new MyViewHolder(v);
            return viewFolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final ProductData pd = list_ProductImages.get(position);


            try {
                //Glide.with(_context).load(pd.getImage_url()).into(holder.imgItem);

                // Instantiate the RequestQueue.
                mImageLoader = MyApplication.getInstance()
                        .getImageLoader();
                //Image URL - This can point to any image file supported by Android
                //final String url = "http://goo.gl/0rkaBz";


                if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("1")) {
                    mImageLoader.get(pd.getImage_url(), ImageLoader.getImageListener(holder.imgItem,
                            R.drawable.nhc500, R.drawable
                                    .nhc500));


                } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("2")) {

                    mImageLoader.get(pd.getImage_url(), ImageLoader.getImageListener(holder.imgItem,
                            R.drawable.sa500, R.drawable
                                    .sa500));


                } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("3")) {
                    mImageLoader.get(pd.getImage_url(), ImageLoader.getImageListener(holder.imgItem,
                            R.drawable.se500, R.drawable
                                    .se500));


                }

                holder.imgItem.setImageUrl(pd.getImage_url(), mImageLoader);


            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.imgItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//
                    //    StartDispalyImages();


                    try {


                        URL myFileUrl = new URL(pd.getImage_url());
                        HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        //imgItem.setImage(ImageSource.bitmap(bm));
                        //.placeholder(R.mipmap.ic_launcher)
                        Glide.with(context).load(pd.getImage_url()).error(R.mipmap.ic_launcher).into(imgItem);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }


        @Override
        public int getItemCount() {
            return list_ProductImages.size();
        }
    }


    private void showInternetUI() {

        ll_mainUI.setVisibility(View.GONE);
        ll_no_internet.setVisibility(View.VISIBLE);
    }

    private void hideNoInternetUI() {

        ll_mainUI.setVisibility(View.VISIBLE);
        ll_no_internet.setVisibility(View.GONE);

    }


    private void LoadDataFromServer() {
        getSingleItemDisplayDetailsFromServer();

        getAllCartItemDetailsFromServer(false);
    }

    private void shakeInit(LinearLayout ll) {


        ShakeAnimation.create().with(ll)
                .setDuration(2000)
                .setRepeatMode(ShakeAnimation.RESTART)
                .setRepeatCount(1)
                .start();

        /*.setRepeatCount(ShakeAnimation.INFINITE)*/
    }
//onCreate Completed


    @Override
    public void onRefresh() {
        if (NetConnectivity.isOnline(context)) {
            //new AsyncData1().execute();
            //  swipeRefreshLayout.setColorSchemeColors(R.color.color1, R.color.color2, R.color.color3);
            //      swipeRefreshLayout.setRefreshing(true);
            //swipeRefreshLayout.setColorSchemeColors(R.color.color1, R.color.color2, R.color.color3);


            Log.d("swipe from ", "onrefresh method");

            LoadDataFromServer();


        } else {
            Toast.makeText(context, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }
    }


    public void galleryActivity() {

        ArrayList<String> newImages = new ArrayList<>();

        newImages.clear();


        for (int i = 0; i < list_Images.size(); i++) {
            newImages.add(list_Images.get(i).toString());


        }

        ZGallery.with(this, newImages)
                .setToolbarTitleColor(ZColor.WHITE)
                .setGalleryBackgroundColor(ZColor.WHITE)
                .setToolbarColorResId(R.color.colorPrimary)
                .setTitle(PRODUCT_NAME)
                .show();
    }


    private void manageAddToCartDetailsToServer(final String ProductId, final String Quantity, final String Type) {
        showDialog();

        String url = AllKeys.WEBSITE + "manageCart/" + Type + "/" + userDetails.get(SessionManager.KEY_USERID) + "/" + ProductId + "/" + Quantity + "/" + db.convertEncodedString(db.getDateTime()) + "/" + db.convertEncodedString(db.getDateTime()) + "/" + SELECTED_SIZE + "";
        url = AllKeys.WEBSITE + "UsercartData?type=" + Type + "&userid=" + userDetails.get(SessionManager.KEY_USERID) + "&productid=" + userDetails.get(SessionManager.KEY_PRODUCT_ID) + "&qty=" + Quantity + "&date=" + dbhandler.convertEncodedString(dbhandler.getDateTime()) + "&status=1&companyid=" + userDetails.get(SessionManager.KEY_COMPANY_ID) + "";
        Log.d(TAG, "URL UsercartData : " + url);
        JsonObjectRequest str_manageCart = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response str_manageCart : " + response);


                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                    if (error_status == false) {
                        if (record_status == true) {
                            userDetails = sessionmanager.getSessionDetails();

                            if (userDetails.get(SessionManager.KEY_CHECKOUT_TYPE).equals("single")) {
                       /* Intent ii = new Intent(context, CheckoutActivity.class);

                        startCountAnimation();
                        sessionmanager.setActivityName(userDetails.get(SessionManager.KEY_ACTIVITY_NAME));

                        startActivity(ii);
                        finish();*/
                                Toast.makeText(context, "Buy Now", Toast.LENGTH_SHORT).show();

                            } else {
                                //Toast.makeText(context, "Added to your cart successfully", Toast.LENGTH_SHORT).show();
                                Snackbar.make(coordinateLayout, "Added to your cart successfully", Snackbar.LENGTH_SHORT).show();
                                btnAddToCart.setText("Go to cart");
                                list_cartItemsId.add(userDetails.get(SessionManager.KEY_PRODUCT_ID));


                                if (list_cartItemsId.size() == 0) {


                                    sessionmanager.setCartItemsIdDetails("");
                                } else {

                                    String data = list_cartItemsId.toString();

                                    data = data.substring(1, data.length() - 1);
                                    sessionmanager.setCartItemsIdDetails(data);
                                    setAddToCartBadget();
                                }

                            }
                        }
                    } else {
                        Toast.makeText(context, "Sorry,try again...", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    Toast.makeText(context, "Try again...", Toast.LENGTH_SHORT).show();
                    hideDialog();
                } else {
                    //manageAddToCartDetailsToServer(ProductId, Quantity, Type);
                    hideDialog();
                    Toast.makeText(context, "Try again...", Toast.LENGTH_SHORT).show();


                }


                Log.d(TAG, "Error in ManageWishList : " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToRequestQueue(str_manageCart);

    }


    /**
     * Send wishlist details to server
     *
     * @param db
     * @param sd
     * @param ProductId
     * @param ProductPrice
     * @param type
     */
    private void sendWishListDetailsToServer(final dbhandler db, final SQLiteDatabase sd, final String ProductId, final String ProductPrice, final String type) {

        showDialog();

        String url_addtowishlist = AllKeys.WEBSITE + "manageWishlist/" + type + "/" + userDetails.get(SessionManager.KEY_USERID) + "/" + ProductId + "/" + db.convertEncodedString(db.getDateTime()) + "/" + db.convertEncodedString(db.getDateTime()) + "/" + ProductPrice + "";
        Log.d(TAG, "URL AddToWishList : " + url_addtowishlist);
        StringRequest str_addToWishList = new StringRequest(Request.Method.GET, url_addtowishlist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response ManageWishList : " + response);

                if (response.equals("0")) {
                    Toast.makeText(context, "Sorry,try again...", Toast.LENGTH_SHORT).show();
                } else {


                    if (type.equals("add")) {
                        sessionmanager.setProductDetails(userDetails.get(SessionManager.KEY_PRODUCT_ID), "1", userDetails.get(SessionManager.KEY_PRODUCT_DESCR), userDetails.get(SessionManager.KEY_PRODUCT_IMAGE_URL), userDetails.get(SessionManager.KEY_PRODUCT_RATING));
                        Toast.makeText(context, "Added to your wishlist", Toast.LENGTH_SHORT).show();
                        // btnAddToWishList.setText("view wishlist");
                        btnAddToWishList.setBackgroundResource(R.drawable.ic_wishlist_seleceted2);
                    } else {
                        sessionmanager.setProductDetails(userDetails.get(SessionManager.KEY_PRODUCT_ID), "-1", userDetails.get(SessionManager.KEY_PRODUCT_DESCR), userDetails.get(SessionManager.KEY_PRODUCT_IMAGE_URL), userDetails.get(SessionManager.KEY_PRODUCT_RATING));
                        btnAddToWishList.setBackgroundResource(R.drawable.ic_wishlist_default);

                        Toast.makeText(context, "Removed from your wishlist", Toast.LENGTH_SHORT).show();
                        //  btnAddToWishList.setBackgroundResource(R.drawable.ic_wishlist_default);
                    }

                }

                db.close();
                sd.close();

                hideDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                } else {
                    sendWishListDetailsToServer(db, sd, ProductId, ProductPrice, type);

                }
                Log.d(TAG, "Error in ManageWishList : " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToRequestQueue(str_addToWishList);

    }


    public void showDialog() {

        try {
            if (!pDialog.isShowing()) {

                pDialog.show();
            }
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
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


    private void getSingleItemDisplayDetailsFromServer() {
        showDialog();
        hideNoInternetUI();


        String url_getproduct = AllKeys.WEBSITE + "GetJSONForProductData?type=ProductData&id=" + userDetails.get(SessionManager.KEY_PRODUCT_ID) + "";
        //url_getproduct = AllKeys.WEBSITE + "GetJSONForProductData?type=ProductData&id=" + 1670 + "";
        Log.d(TAG, "URL GetJSONForProductData : " + url_getproduct);
        // url_getproduct = "http://19designs.org/yelona/index.php/welcome/getProductDetailsByProductId/93";
        JsonObjectRequest str_singleitem = new JsonObjectRequest(Request.Method.GET, url_getproduct, null, new Response.Listener<JSONObject>() {
            @Override

            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response :" + response.toString());


                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                    if (error_status == false) {
                        if (record_status == true) {


                            try {
                                list_ProductImages.clear();
                                list_Images.clear();


                                JSONArray arr = response.getJSONArray(AllKeys.ARRAY_PRODUCTDATA);

                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject c = arr.getJSONObject(i);


                                    String image_url = c.getString(AllKeys.TAG_IMAGE);


                                    //   Glide.with(context).load(image_url).error(R.drawable.splash_screen_500).into(image_url);

                                    try {

                                        // Instantiate the RequestQueue.
                                        ImageLoader mImageLoader = MyApplication.getInstance()
                                                .getImageLoader();
                                        //Image URL - This can point to any image file supported by Android
                                        //final String url = "http://goo.gl/0rkaBz";


                                        if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("1")) {
                                            mImageLoader.get(image_url, ImageLoader.getImageListener(imgItem,
                                                    R.drawable.nhc500, R.drawable
                                                            .nhc500));


                                        } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("2")) {

                                            mImageLoader.get(image_url, ImageLoader.getImageListener(imgItem,
                                                    R.drawable.sa500, R.drawable
                                                            .sa500));


                                        } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("3")) {
                                            mImageLoader.get(image_url, ImageLoader.getImageListener(imgItem,
                                                    R.drawable.se500, R.drawable
                                                            .se500));


                                        }

                                        imgItem.setImageUrl(image_url, mImageLoader);

                                       /* if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("1")) {
                                            Glide.with(context).load(image_url).placeholder(R.drawable.nhc500).error(R.drawable.nhc500).crossFade(R.anim.fadein, 2000).into(imgItem);
                                        } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("2")) {
                                            Glide.with(context).load(image_url).placeholder(R.drawable.sa500).error(R.drawable.sa500).crossFade(R.anim.fadein, 2000).into(imgItem);
                                        } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("3")) {
                                            Glide.with(context).load(image_url).placeholder(R.drawable.se500).error(R.drawable.se500).crossFade(R.anim.fadein, 2000).into(imgItem);
                                        }*/

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    ProductData pd = new ProductData(c.getString(AllKeys.TAG_IMAGE), true);
                                    list_ProductImages.add(pd);
                                    list_Images.add(c.getString(AllKeys.TAG_IMAGE));

                                    //sessionmanager.setCategoryTypeAndIdDetails(,"","");


                                    YOUTUBE_VIDEO_THUMBNAIL = c.getString(AllKeys.TAG_YOUTUBE_VIDEO);

                                    if (YOUTUBE_VIDEO_THUMBNAIL.equals("")) {

                                        llPicturesAndClips.setVisibility(View.GONE);
                                    } else {

                                        llPicturesAndClips.setVisibility(View.VISIBLE);


                                        // Instantiate the RequestQueue.
                                        ImageLoader mImageLoader = MyApplication.getInstance()
                                                .getImageLoader();
                                        //Image URL - This can point to any image file supported by Android
                                        //final String url = "http://goo.gl/0rkaBz";

                                        YOUTUBE_VIDEO_THUMBNAIL = YOUTUBE_VIDEO_THUMBNAIL.substring(YOUTUBE_VIDEO_THUMBNAIL.indexOf("=")+1,YOUTUBE_VIDEO_THUMBNAIL.length());

                                        String video_thumbnail_url = "http://img.youtube.com/vi/" + YOUTUBE_VIDEO_THUMBNAIL + "/0.jpg";









                                        if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("1")) {
                                            mImageLoader.get(video_thumbnail_url, ImageLoader.getImageListener(imgVideosThumbnail,
                                                    R.drawable.nhc500, R.drawable
                                                            .nhc500));


                                        } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("2")) {

                                            mImageLoader.get(video_thumbnail_url, ImageLoader.getImageListener(imgVideosThumbnail,
                                                    R.drawable.sa500, R.drawable
                                                            .sa500));


                                        } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("3")) {
                                            mImageLoader.get(video_thumbnail_url, ImageLoader.getImageListener(imgVideosThumbnail,
                                                    R.drawable.se500, R.drawable
                                                            .se500));


                                        }

                                        imgVideosThumbnail.setImageUrl(video_thumbnail_url, mImageLoader);

                                    }


                                    PRODUCT_NAME = c.getString(AllKeys.TAG_PRODUCT_NAME);
                                    setTitle(PRODUCT_NAME);
                                    txtProductName.setText(PRODUCT_NAME);
                                    txtProductPrice.setText("\u20b9 " + c.getString(AllKeys.TAG_OFFER_PRICE));
                                    PRODUCT_PRICE = c.getString(AllKeys.TAG_OFFER_PRICE);
                                    txtProductMRP.setText("\u20b9 " + c.getString(AllKeys.TAG_REAL_PRICE));

                                    UNIT_PRICE = c.getString(AllKeys.TAG_OFFER_PRICE);

                                    WALLCHART_PDF_URL = c.getString(AllKeys.TAG_WALLCHART);
                                    tvWallchartPDF.setText(PRODUCT_NAME + " Wallchart");

                                    if (WALLCHART_PDF_URL.equals("")) {

                                        llWallchart.setVisibility(View.GONE);
                                    } else {
                                        llWallchart.setVisibility(View.VISIBLE);

                                    }

                                    BROCHURE_PDF_URL = c.getString(AllKeys.TAG_BROUCHURE);
                                    tvBrochuresPDF.setText(PRODUCT_NAME + " Brouchure");

                                    if (BROCHURE_PDF_URL.equals("")) {

                                        llBrochures.setVisibility(View.GONE);
                                    } else {
                                        llBrochures.setVisibility(View.VISIBLE);

                                    }


                                    //Check Inventory if not in inventory then disable addToCart
                            /*if (c.getString(AllKeys.TAG_INVENTORY).equals("0"))
                            {
                                btnBuyNow.setEnabled(false);
                                btnAddToCart.setText("Out Of Stock");
                                btnAddToCart.setEnabled(false);
                                txtAvailability.setText("Out Of Stock");
                            } else {
                                btnBuyNow.setEnabled(true);
                                txtAvailability.setText("In Stock");
                                txtAvailability.setTextColor(Color.parseColor("#4CAF50"));
                            }*/


                                    Double offer = Double.parseDouble(c.getString(AllKeys.TAG_OFFER_PRICE)) * 100 / Double.parseDouble(c.getString(AllKeys.TAG_REAL_PRICE));
                                    int percentage = 100 - offer.intValue();
                                    if (percentage == 0 || percentage < 0) {
                                        txtOffer.setVisibility(View.GONE);
                                    } else {
                                        //  holder.txtOffer.setVisibility(View.GONE);
                                        txtOffer.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fadein));

                                        if (percentage <= 9) {
                                            txtOffer.setText(" " + percentage + " % off");
                                        } else {
                                            txtOffer.setText("" + percentage + " % off");
                                        }
                                    }


                                    String descr = c.getString(AllKeys.TAG_LONGDESCR);


                                    sessionmanager.setProductDetails(userDetails.get(SessionManager.KEY_PRODUCT_ID), userDetails.get(SessionManager.KEY_WISHLIST_STATUS), descr, image_url, "0");


                                    try {
                                        Answers.getInstance().logPurchase(new PurchaseEvent());

                                        Answers.getInstance().logPurchase(new PurchaseEvent()
                                                .putItemPrice(BigDecimal.valueOf(Double.parseDouble(UNIT_PRICE)))
                                                .putCurrency(Currency.getInstance("INR"))
                                                .putItemName(PRODUCT_NAME)


                                                .putCustomAttribute("ProductID", userDetails.get(SessionManager.KEY_PRODUCT_ID))
                                                .putCustomAttribute("UserID", userDetails.get(SessionManager.KEY_USERID))

                                                .putSuccess(true));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    descr = descr.replace(";", "\n\n");

                                    txtDescrInfo.setText("\n\n" + Html.fromHtml(descr));


                                    String more_images = c.getString(AllKeys.TAG_IMAGE);
                                    if (c.getString(AllKeys.TAG_TECHNICAL_DATA).equals("")) {

                                        llTechnicalData.setVisibility(View.GONE);
                                    } else {
                                        llTechnicalData.setVisibility(View.VISIBLE);
                                       /* String str_decode = c.getString(AllKeys.TAG_TECHNICAL_DATA);

                                        try {
                                            str_decode = URLDecoder.decode(str_decode,"utf-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }*/
                                        //tvTechnicalDataDescr.setText(Html.fromHtml(c.getString(AllKeys.TAG_TECHNICAL_DATA)));
                                        final String mimeType = "text/html";
                                        final String encoding = "UTF-8";


                                        String html = c.getString(AllKeys.TAG_TECHNICAL_DATA);


                                        wvTechnicalDataDescr.loadDataWithBaseURL("", html, mimeType, encoding, "");


                                        tvTechnicalDataDescr.setVisibility(View.GONE);


                                    }


                                    if (IS_PRODUCT_SIZE) {


                                        crdSize.setVisibility(View.VISIBLE);

                                        ProductSizeDisplayAdapter adapter_productsize = new ProductSizeDisplayAdapter(context);
                                        recyclerview_size.setAdapter(adapter_productsize);
                                    } else {
                                        crdSize.setVisibility(View.GONE);
                                    }


                                }


                                JSONArray arr_images = response.getJSONArray("Images");
                                for (int k = 0; k < arr_images.length(); k++) {
                                    JSONObject c = arr_images.getJSONObject(k);
                                    ProductData pd = new ProductData(c.getString(AllKeys.TAG_ORDER_PRODUCT_IMAGE), true);

                                    list_ProductImages.add(pd);
                                    list_Images.add(c.getString(AllKeys.TAG_ORDER_PRODUCT_IMAGE));

                                }


                                recyclerview_images.setVisibility(View.VISIBLE);
                                adapter_ProductImages = new ProductImagesRecyclerViewAdapter_New(context);
                                recyclerview_images.setAdapter(adapter_ProductImages);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                hideDialog();
                            }
                            setAddToCartBadget();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideDialog();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                    showInternetUI();
                } else {
                    getSingleItemDisplayDetailsFromServer();
                }


                Log.d(TAG, "Error in singleItem : " + error.getMessage());

            }
        });
        MyApplication.getInstance().addToRequestQueue(str_singleitem);


    }


    private void getAllCartItemDetailsFromServer(final boolean isCheckout) {

        hideNoInternetUI();
        showDialog();

        String url_addtowishlist = AllKeys.WEBSITE + "ViewCartData?type=cartdata&userid=" + userDetails.get(SessionManager.KEY_USERID) + "";
        Log.d(TAG, "URL ViewCartData : " + url_addtowishlist);
        JsonObjectRequest str_manageCart = new JsonObjectRequest(Request.Method.GET, url_addtowishlist, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response str_manageCart : " + response);

                list_cartItemsId.clear();
                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false)
                    {
                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_CARTDATA);

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);

                                list_cartItemsId.add(c.getString(AllKeys.TAG_CART_PRODUCTID));


                            }
                            setAddToCartBadget();
                        }

                    }
                } catch (JSONException e) {
                    setAddToCartBadget();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideNoInternetUI();
                    showInternetUI();
                } else {
                    getAllCartItemDetailsFromServer(isCheckout);
                }

                setAddToCartBadget();

                Log.d(TAG, "Error in ManageWishList : " + error.getMessage());

            }
        });
        MyApplication.getInstance().addToRequestQueue(str_manageCart);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(R.menu.item_display, menu);
            cart = (MenuItem) menu.findItem(R.id.menu_addtocart);

            MenuItem c = (MenuItem) menu.findItem(R.id.menu_displayview);
            c.setVisible(false);


            LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();


//int orderid=db.getMaxOrderId();


            setAddToCartBadget();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            Intent i = new Intent(context, ItemDisplayActivity.class);
            startActivity(i);
            finish();


            /*if(userDetails.get(SessionManager.KEY_CATEGORY_TYPE).equals("category"))
            {



                Intent i = new Intent(context , CategoryAcitivity.class);
                startActivity(i);
                finish();

            }
            else if(userDetails.get(SessionManager.KEY_CATEGORY_TYPE).equals("subcategory"))
            {


                Intent i = new Intent(context , SubCategoryActivity.class);
                startActivity(i);
                finish();

            }
*/

        } else if (item.getItemId() == R.id.menu_whishlist) {
            /*Intent intent = new Intent(context, WishListActivity.class);
            //intent.putExtra("ActivityName", TAG);
            sessionmanager.setActivityName(TAG);
            startActivity(intent);
            finish();*/
            Toast.makeText(context, "clicked on Wishlist", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.menu_addtocart) {


            if (list_cartItemsId.size() != 0) {

                sessionmanager.setActivityName(TAG);
                Intent intent = new Intent(context, CheckoutActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show();
            }

            // getAllCartItemDetailsFromServer(true);
            //Toast.makeText(context, "Clicked on addtocart", Toast.LENGTH_SHORT).show();


        } else if (item.getItemId() == R.id.menu_search) {


            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("ActivityName", TAG);
            startActivity(intent);
            finish();

           // Toast.makeText(context, "Search Activity", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        Intent i = new Intent(context, ItemDisplayActivity.class);
        startActivity(i);
        finish();

/*
        if(userDetails.get(SessionManager.KEY_CATEGORY_TYPE).equals("category"))
        {



            Intent i = new Intent(context , CategoryAcitivity.class);
            startActivity(i);
            finish();

        }
        else if(userDetails.get(SessionManager.KEY_CATEGORY_TYPE).equals("subcategory"))
        {


            Intent i = new Intent(context , SubCategoryActivity.class);
            startActivity(i);
            finish();

        }

*/

    }


    private void startCountAnimation() {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, 600);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {

                //cart.setText("" + (int) animation.getAnimatedValue());
                //  setAddToCartBadget();
            }
        });
        animator.start();
    }

    private void setAddToCartBadget() {

        //    String query = "select * from " + db.TABLE_ORDER;


        try {
            LayerDrawable icon = (LayerDrawable) cart.getIcon();


            Utils.setBadgeCount(SingleItemActivity.this, icon, list_cartItemsId.size());
            // startCountAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //Custom View Pageer


    //Complete Custom View Pager


    /**
     * Display Product Size UI
     */
    public class ProductSizeDisplayAdapter extends RecyclerView.Adapter<ProductSizeDisplayAdapter.MyViewHolder> {


        private final Context _context;


        public ProductSizeDisplayAdapter(Context context) {
            this._context = context;


        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private final TextView txtSize;


            public MyViewHolder(View itemView) {
                super(itemView);
                txtSize = (TextView) itemView.findViewById(R.id.size);


            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(_context).inflate(R.layout.row_single_product_size, parent, false);


            MyViewHolder myviewHolder = new MyViewHolder(view);
            return myviewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


            if (position == 0) {


                if (IS_SIZE_SELECTED == false) {

                    holder.txtSize.setBackgroundResource(R.drawable.sizeround1);
                    holder.txtSize.setTextColor(Color.parseColor("#585858"));
                    holder.txtSize.setBackgroundResource(R.drawable.sizeround2);
                    holder.txtSize.setTextColor(Color.parseColor("#ffffff"));
                    SELECTED_SIZE = list_productSize.get(position);


                    //holder.txtSize
                }
            }

            if (SELECTED_SIZE.equals(list_productSize.get(position))) {

                holder.txtSize.setBackgroundResource(R.drawable.sizeround1);
                holder.txtSize.setTextColor(Color.parseColor("#585858"));
                holder.txtSize.setBackgroundResource(R.drawable.sizeround2);
                holder.txtSize.setTextColor(Color.parseColor("#ffffff"));


            } else {
                holder.txtSize.setBackgroundResource(R.drawable.sizeround1);
                holder.txtSize.setTextColor(Color.parseColor("#585858"));

                //holder.txtSize.setBackgroundResource(R.drawable.sizeround2);
                //holder.txtSize.setTextColor(Color.parseColor("#ffffff"));


            }


            holder.txtSize.setText(list_productSize.get(position));

            holder.txtSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    IS_SIZE_SELECTED = true;


                    SELECTED_SIZE = list_productSize.get(position);
                    holder.txtSize.setBackgroundResource(R.drawable.sizeround1);
                    holder.txtSize.setTextColor(Color.parseColor("#585858"));
                    holder.txtSize.setBackgroundResource(R.drawable.sizeround2);
                    holder.txtSize.setTextColor(Color.parseColor("#ffffff"));

                    notifyItemChanged(position);
                    notifyDataSetChanged();

                }
            });


        }

        @Override
        public int getItemCount() {
            return list_productSize.size();
        }
    }



/*Complete DIsplay Product Size UI*/
}
