package com.wahhao.mfg.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.wahhao.mfg.BuildConfig;
import com.wahhao.mfg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataModel {

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

	public static String secrate_key = "9ae2d1fb9e58fcf7a2dd7fecacdb24d6be9580f21afa9d62f9f00c723a827048";

	public static String getPlace() {


		return "https://maps.googleapis.com/maps/api/place/autocomplete/";
	}

	public static Drawable setDrawableSelector(Context context, int normal, int selected) {
		Drawable state_normal = ContextCompat.getDrawable(context, normal);

		Drawable state_pressed = ContextCompat.getDrawable(context, selected);


		Bitmap state_normal_bitmap = ((BitmapDrawable) state_normal).getBitmap();


		Bitmap disabledBitmap = Bitmap.createBitmap(
				state_normal.getIntrinsicWidth(),
				state_normal.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(disabledBitmap);

		Paint paint = new Paint();
		paint.setColor(context.getResources().getColor(R.color.theme_color));
		paint.setAlpha(126);
		canvas.drawBitmap(state_normal_bitmap, 0, 0, paint);
		BitmapDrawable state_normal_drawable = new BitmapDrawable(context.getResources(), disabledBitmap);
		StateListDrawable drawable = new StateListDrawable();

		drawable.addState(new int[]{android.R.attr.state_selected},
				state_pressed);

		drawable.addState(new int[]{android.R.attr.state_enabled},
				state_normal_drawable);

		return drawable;
	}



	public static Drawable setDrawableSelectorNone(Context context, int normal, int selected) {
		Drawable state_normal = ContextCompat.getDrawable(context, normal);

		Drawable state_pressed = ContextCompat.getDrawable(context, selected);


		Bitmap state_normal_bitmap = ((BitmapDrawable) state_normal).getBitmap();


		Bitmap disabledBitmap = Bitmap.createBitmap(
				state_normal.getIntrinsicWidth(),
				state_normal.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(disabledBitmap);

		Paint paint = new Paint();
		paint.setColor(context.getResources().getColor(R.color.bottom_bg));
		paint.setAlpha(126);
		canvas.drawBitmap(state_normal_bitmap, 0, 0, paint);
		BitmapDrawable state_normal_drawable = new BitmapDrawable(context.getResources(), disabledBitmap);
		StateListDrawable drawable = new StateListDrawable();

		drawable.addState(new int[]{android.R.attr.state_selected},
				state_pressed);

		drawable.addState(new int[]{android.R.attr.state_enabled},
				state_normal_drawable);

		return drawable;
	}

	public static void setTextWatcherTo(EditText... editTexts) {

		for (final EditText editText : editTexts)
			editText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

					String result = s.toString().replaceAll(" ", "");
					if (!s.toString().equals(result)) {
						editText.setText(result);
						editText.setSelection(result.length());

					}
				}
			});
	}

	public static int timeout = 20000;
	private static ProgressDialog pd_st;

	/**
	 * Custom Loading.
	 *
	 * @param c
	 *            Application Context.
	 * @param msg
	 *            Message.
	 */


	/**
	 * Close custom loading.
	 */
	public static void loading_box_stop() {
		if (pd_st != null)
			if (pd_st.isShowing()) {
				pd_st.dismiss();
			}
	}

	// ***************
	public static long last_press = 0;
	public static int button_press_delay = 500; // delay in milliseconds
	public static String urlLoad = "";
	static String Result_status = "";
	public static int device_density;

	public static Boolean isTapable() {
		if (last_press + button_press_delay < System.currentTimeMillis()) {
			last_press = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}

	private static boolean net;

	public static String customerid = "";
	public static boolean locationEnabled = false;
	private static AlertDialog dialogNoInternet;
	public static double LATITUDE;
	public static double LONGITUDE;
	public static Fragment fragmentMain;
	public static int notificationCount = 0;
	public static double selectedLattitude;
	public static double selectedLongitude;
	public static long animSpeed = 1000;
	public static long menuOpenTime = 500;


	// coverting date format "yyyy-MM-dd HH:mm:ss" --->
	// "EEE, dd MMM yyyy hh:mm a"  2016-01-14 04:35:59
	@SuppressLint("SimpleDateFormat")
	public static String timeZoneConverterReverse(String timeAndDate) {

		Log.e("===", "==" + timeAndDate);
		// Date will return local time in Javayyyy-MM-dd'T'HH:mm:ssz
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		try {
			Date myDate = sdf.parse(timeAndDate);
			DateFormat converter = new SimpleDateFormat("EEE, dd MMM yyyy");
			// converter.setTimeZone(TimeZone.getTimeZone("GMT"));

			//System.out.println("local time : " + myDate);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Log.e("time in GMT : ===", converter.format(myDate));

			return converter.format(myDate);

		} catch (ParseException e) {

			e.printStackTrace();

		}
		Log.e("===", "==" + timeAndDate);
		return timeAndDate;

	}

	public static boolean Internetcheck(Activity act) {
		ConnectivityManager connectivity = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static void getAlertDialogWithbothMessage(Activity act, String title, String msg) {

		new AlertDialog.Builder(act).setTitle(title).setMessage(msg).setCancelable(false)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						// dialogNoInternet.cancel();
					}
				}).show();

	}

	public static void getAlertDialogWithbothMessageFinish(final Activity act, String title, String msg) {

		new AlertDialog.Builder(act).setTitle(title).setMessage(msg).setCancelable(false)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						act.finish();
						// dialogFinish.cancel();
					}
				}).show();

	}

	public static void getAlertDialogWithMessage(Activity act, String msg) {
		new AlertDialog.Builder(act).setTitle("OOPS!").setMessage(msg).setCancelable(false)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).show();
	}


	public static void hideKeyBoard(EditText ed, Activity c) {
		InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);

	}


	public static void ChangeStatusBarColor(Activity context) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

			Window window = context.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(context.getResources().getColor(R.color.colorAccent));
		}
	}



	public static void hideKeyBoard(Activity mContext) {
		try {
			InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (mContext.getCurrentFocus() != null)
				inputManager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
		}
	}


	static public boolean checkPermission(Activity ctx, String permission) {

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {


			int result = ContextCompat.checkSelfPermission(ctx, permission);
//        Manifest.permission.ACCESS_FINE_LOCATION
			if (result == PackageManager.PERMISSION_GRANTED) {

				return true;

			} else {

				return false;

			}
		} else {
			return true;
		}
	}

	static public void requestPermission(Activity context, String permission, int requestCode, String message) {

		if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {

			Toast.makeText(context, message, Toast.LENGTH_LONG).show();

		} else {

			ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
		}
	}

	static public void requestPermissionMultiple(Activity context, String[] permission, int requestCode, String message) {

		if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission[0]) && ActivityCompat.shouldShowRequestPermissionRationale(context, permission[1])) {

			Intent intent = new Intent();
			intent.setAction(
					Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts("package",
					BuildConfig.APPLICATION_ID, null);
			intent.setData(uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);

		} else {

			ActivityCompat.requestPermissions(context, permission, requestCode);
		}
	}
//	public static boolean checkPlayServices(Context ctx) {
//		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ctx);
//		if (resultCode != ConnectionResult.SUCCESS) {
//			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//				GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) ctx,
//						PLAY_SERVICES_RESOLUTION_REQUEST).show();
//			} else {
//				Log.i("TAG", "This device is not supported.");
//				((Activity) ctx).finish();
//			}
//			return false;
//		}
//		return true;
//	}


	public static boolean CheckEnableGPS(Context context) {


		LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

			return false;
		} else {
			return true;
		}
	}

	public static boolean checkYoutubeLink(String url) {

		String pattern = "http(?:s?):\\/\\/(?:www\\.)?youtu(?:be\\.com\\/watch\\?v=|\\.be\\/)([\\w\\-\\_]*)(&(amp;)?\u200C\u200B[\\w\\?\u200C\u200B=]*)?";
		try {
			if (url.matches(pattern)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	public static int rgb(String hex) {
		int color = (int) Long.parseLong(hex.replace("#", ""), 16);
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 0) & 0xFF;
		return Color.rgb(r, g, b);
	}

	public static String getSongID(String url) {

//		String pattern2 = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
		String pattern = "http(?:s?):\\/\\/(?:www\\.)?youtu(?:be\\.com\\/watch\\?v=|\\.be\\/)([\\w\\-\\_]*)(&(amp;)?\u200C\u200B[\\w\\?\u200C\u200B=]*)?";

		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(url);

		if (matcher.find()) {
//			Log.e("id", "is" + matcher.group(1));

			return matcher.group(1);

		} else {
			return null;
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String setRetrofitError(String data) {

	    String error="";
		try {
			JSONObject ob1 = new JSONObject(data);

            JSONObject ob2=ob1.getJSONObject("errors");
            if(ob2.length()>0){
                JSONObject object=new JSONObject();
                Iterator<?> iterator = ob2.keys();
                while (iterator.hasNext()) {
                    Object key = iterator.next();

                    if(key.toString().equalsIgnoreCase("message")){
                        error=ob2.getString(key.toString());
                    	return error;
					}else {
						object = ob2.getJSONObject(key.toString());
						error = object.getString("message").toString();
						return error;
					}
                }
            }else{
                error=ob1.getString("message");
            }
            return error;
		} catch (Exception e) {
		    Log.e("Error "," "+e.getMessage());
			return "Server Error or Network fail to connect.";
		}
	}

	public static void loading_box(Context c, boolean cancelable) {
		loading_box_stop();
		pd_st = new ProgressDialog(c, android.R.style.Theme_Translucent_NoTitleBar);
		pd_st.show();
		pd_st.setCancelable(cancelable);
		pd_st.setContentView(R.layout.loading_box);

	}

    public static void showSnackBar(View view,String unit) {
        try {
			InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

			Snackbar snackbar = Snackbar
                    .make(view, unit, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(view.getResources().getColor(R.color.snack_background));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            Log.e(" error ", " " + e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    public static void showSnackBarError(View view,String unit) {
        try {
             Snackbar snackbar = Snackbar
                    .make(view, DataModel.setRetrofitError(unit), Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(view.getResources().getColor(R.color.snack_background));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            Log.e(" error ", " " + e.getMessage());
        }
    }





	public static JSONObject setLanguage(int langType, Context context,String unit) {
		JSONObject json = new JSONObject();
		try {
			String file="";
			if(langType==1){
				file="lang/en/lang.json";
			}
			if(langType==2){
				file="lang/cn/lang.json";
			}
			InputStream is = context.getAssets().open(file);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
		 JSONObject	jsonPre = new JSONObject(new String(buffer, "UTF-8"));
		 json=jsonPre.getJSONObject(unit);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//Log.e("Total", json.toString());
		return json;
	}



}
