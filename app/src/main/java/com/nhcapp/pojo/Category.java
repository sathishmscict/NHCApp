package com.nhcapp.pojo;

/**
 * Created by Satish Gadde on 18-06-2016.
 */
public class Category {


    int categoryid;
  String categoryname,imageurl,parentid,totalproducts,totalsubcategories;

    /*public Category(int categoryid, String categoryname, String imageurl) {
        this.categoryid = categoryid;
        this.categoryname = categoryname;
        this.imageurl = imageurl;
    }*/

    public Category(int categoryid, String categoryname, String imageurl, String parentid,String pc,String totalsubCategories) {
        this.categoryid = categoryid;
        this.categoryname = categoryname;
        this.imageurl = imageurl;

        this.parentid = parentid;
        this.totalproducts = pc;

        this.totalsubcategories = totalsubCategories;
    }

    public String getTotalsubcategories() {
        return totalsubcategories;
    }

    public void setTotalsubcategories(String totalsubcategories) {
        this.totalsubcategories = totalsubcategories;
    }

    public String getTotalproducts() {
        return totalproducts;
    }

    public void setTotalproducts(String totalproducts) {
        this.totalproducts = totalproducts;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }


    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
	
}
