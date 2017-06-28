package com.nhcapp.helper;



import java.util.regex.Pattern;

public class AllKeys {
	public static  final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

	public static final String URL_PINCODE_CHECK = "http://s.evahan.in/Traking/check_service";


	public static final String URL_PINCODE_TRACKING = "http://s.evahan.in/Traking/Check_service/tracking?awbno=";




	public static final String URL_CITYNAME_BY_PINCODE_CHECK = "https://www.whizapi.com/api/v2/util/ui/in/indian-city-by-postal-code?project-app-key=p75dteec5dvdymhkis18c5lk";

	public static String TAG_PAYMENT_HASH_GENERATION="http://19designs.org/yelona/get_hash.php";

	public static final String TAG_MOBILE_SLIDER_1="Mobile Slider One";
	public static final String TAG_MOBILE_SLIDER_2="Mobile Slider Two";


    public static final boolean checkEmail(String email) {
		System.out.println("Email Validation:==>" + email);
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}


	public static final String WEBSITE = "http://shahagenciesindia.com/WebService.asmx/";
	//public static final String WEBSITE = "http://arham.dnsitexperts.com/yelona/index.php/welcome/";//http://demo1.dnsitexperts.com/
	//public static final String WEBSITE = "http://19designs.org/yelona/index.php/welcome/";//http://demo1.dnsitexperts.com/


	public static final String RESOURSES="https://www.shahagenciesindia.com/";
	public static final Integer MY_SOCKET_TIMEOUT_MS=30000;





	//Common Keys
	public static final String TAG_MESSAGE = "MESSAGE";
	public static final String TAG_ERROR_ORIGINAL = "ORIGINAL_ERROR";
	public static final String TAG_ERROR_STATUS = "ERROR_STATUS";
	public static final String TAG_IS_RECORDS = "RECORDS";

	//GetJSONForCategoryData Related Keys
	public static final String ARRAY_CATEGORY="categorydata";
	//public static final String TAG_CATEGORYID="id";
	public static final String TAG_CATEGORY_NAME="category";
	public static final String TAG_CATEGORY_IMAGE="img";
	//public static final String TAG_PARENTID="p_id";

	public static final String TAG_TOTAL_SUB_CATEGORIES="TotalSubCategory";



	//LoginData Related keys
	public static final String ARRAY_LOGINDATA="logindata";
	public static final String TAG_USER_ID="id";
	public static final String TAG_FIRST_NAME="fname";
	public static final String TAG_LASTNAME="lname";
	public static final String TAG_MOBILENO="phoneno";
	public static final String TAG_ADDRESS="address";
	public static final String TAG_VERIFICATION_STATUS="VerificationStatus";
	public static final String TAG_USER_AVATAR = "Profile_Pic";
	public static final String TAG_USER_EMAIL = "Email";

	public static final String TAG_USER_GENDER = "Gender";




	public static final String TAG_CART_PRODUCTID="productid";
    //Productdata Related keys
    public static final String ARRAY_PRODUCTDATA="Productdata";
    public static final String TAG_PRODUCT_ID="id";
    public static final String TAG_PRODUCT_NAME="name";
    public static final String TAG_PRODUCT_LINE="productline";
    public static final String TAG_REAL_PRICE="realprice";
    public static final String TAG_OFFER_PRICE="offerprice";
    public static final String TAG_OFFER_PERCENTAGE="offerper";
    public static final String TAG_STOCK="stock";
    public static final String TAG_UNIT="unit";
    public static final String TAG_LONGDESCR="longdesc";
    public static final String TAG_IMAGE="img";
	public static final String TAG_PRODUCT_FILE="productfile";
	public static final String TAG_TECHNICAL_DATA="TechnicalData";
	public static final String TAG_YOUTUBE_VIDEO="Youtube";
	public static final String TAG_BROUCHURE="Brochure";
	public static final String TAG_WALLCHART="WallChart";


    public static final String TAG_CATEOGRYNAME="categoryname";

    public static final  String TAG_TOTAL_RECORDS="TotalRecords";

	public static final String ACTIVITYNAME = "ActivityName";

	public static final String CATEGORYID = "CategoryId";//For Intent

	public static final String TAG_CATEGORYID = "categoryid";//For Service
	public static final String TAG_PARENTID = "parentid";//For Service
	 public static final String TAG_COMPANYID = "companyid";//For Service







	//ViewCartData Related Keys
	public static final String ARRAY_CARTDATA = "Cartdata";

	/*public static final String TAG="id";
	public static final String TAG="name";
	public static final String TAG="productline";
	public static final String TAG="realprice";
	public static final String TAG="offerprice";
	public static final String TAG="offerper";
	public static final String TAG="stock";
	public static final String TAG="unit";
	public static final String TAG="longdesc";
	public static final String TAG="img";
	public static final String TAG="productfile";*/

	public static final String TAG_PRODUCT_QUANTITY="product_qty";
	public static final String TAG_DATE="date";


	public static final String ARRAY_ORDERHISTORY = "History";



	//public static final String TAG_COMPANYID ="CompanyId";
	public static final String TAG_ORDER_TRACKING_ID = "tracking_id";
	public static final String TAG_ORDER_PRODUCT_IMAGE = "Image";
	public static final String TAG_ORDER_VARIANT_ID = "size_variant";

	public static final String TAG_ORDER_SINGLE_ORDERID = "OrderId";
	public static final String TAG_ORDER_UNIT_PRICE = "Price";
	public static final String TAG_ORDER_QUANTITY = "Qty";
	public static final String TAG_ORDER_CREATED_AT = "date";
	public static final String TAG_ORDER_DELIVERY_DATE = "date";

	public static final String TAG_ORDER_STATUS = "status";
	public static final String TAG_ORDER_DISPATCH_DATE = "date";
	public static final String TAG_ORDER_COMPLETE_DATE = "date";

	//Contact Us Related Keys
	public static final String ARRAY_CONTACTUS_DATA="ContactData";
	public static final String TAG_CONTACT_ADDRESS="Address";
	public static final String TAG_CONTACT_LONGTITUDE="Longitude";
	public static final String TAG_CONTACT_LATTITUDE="Latitude";
	public static final String TAG_CONTACT_EMAIL="Email";
	public static final String TAG_CONTACT_PHONE="Phone";





























}
