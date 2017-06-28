package com.nhcapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nhcapp.R;
import com.nhcapp.app.MyApplication;
import com.nhcapp.pojo.Category;
import com.nhcapp.session.SessionManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class CategoryAdapterRecyclerView extends RecyclerView.Adapter<CategoryAdapterRecyclerView.MyViewHolder> {

    private final Context _context;
    private final SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<Category> list_categoty;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivCategory;
        public TextView tvcategory;

        public MyViewHolder(View view) {
            super(view);


            tvcategory = (TextView) view.findViewById(R.id.tvcategory);

            ivCategory = (ImageView) view.findViewById(R.id.ivCategory);


        }

    }


    public CategoryAdapterRecyclerView(Context context, List<Category> catList) {
        this.list_categoty = catList;
        this._context = context;

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category category = list_categoty.get(position);

        holder.tvcategory.setText(category.getCategoryname());


        try {

            if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("1")) {

                Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.nhc500).error(R.drawable.nhc500).crossFade(R.anim.fadein, 2000).into(holder.ivCategory);
            } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("2")) {
                Glide.with(_context).load(list_categoty.get(position).getImageurl()).error(R.drawable.sa500).crossFade(R.anim.fadein, 2000).into(holder.ivCategory);
            } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("3")) {
                Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.se500).error(R.drawable.se500).crossFade(R.anim.fadein, 2000).into(holder.ivCategory);
            }

       /*     // Instantiate the RequestQueue.
            mImageLoader = MyApplication.getInstance()
                    .getImageLoader();
            //Image URL - This can point to any image file supported by Android
            //final String url = "http://goo.gl/0rkaBz";



            if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("1")) {
                mImageLoader.get(list_categoty.get(position).getImageurl(), ImageLoader.getImageListener(holder.ivCategory,
                        R.drawable.nhc500, R.drawable
                                .nhc500));




            } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("2")) {

                mImageLoader.get(list_categoty.get(position).getImageurl(), ImageLoader.getImageListener(holder.ivCategory,
                        R.drawable.sa500, R.drawable
                                .sa500));



            } else if (userDetails.get(SessionManager.KEY_COMPANY_ID).equals("3")) {
                mImageLoader.get(list_categoty.get(position).getImageurl(), ImageLoader.getImageListener(holder.ivCategory,
                        R.drawable.se500, R.drawable
                                .se500));




            }

            holder.ivCategory.setImageUrl(list_categoty.get(position).getImageurl(), mImageLoader);*/



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list_categoty.size();
    }
}