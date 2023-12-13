package com.edu.magika;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.edu.magika.students.NewDashboard;
import com.edu.magika.students.StudentFees;
import com.edu.magika.utils.Constants;
import com.edu.magika.utils.MyApp;
import com.edu.magika.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.widget.Toast.makeText;

import static androidx.constraintlayout.widget.Constraints.TAG;

import static com.edu.magika.utils.Utility.showInterstitialAd;

import androidx.annotation.NonNull;

public class TakeUrl extends Activity {
    Context context = this;
    EditText urlET;
    LinearLayout submitBtn;
    Locale myLocale;
    String langCode = "";

    public Map<String, String>  headers = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.take_url_activity);

        initializeAds();

        urlET = findViewById(R.id.et_domain_takeUrl);
        urlET.setText("https://"); // Pre-fill URL
        submitBtn = findViewById(R.id.btn_submit_takeUrl);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showInterstitialAd(TakeUrl.this);

                String appDomain = urlET.getText().toString();
                if(appDomain.isEmpty()){
                    makeText(TakeUrl.this, "Please Enter URL", Toast.LENGTH_SHORT).show();
                }else{
                    if(Utility.isConnectingToInternet(TakeUrl.this)){
                        getDataFromApi(appDomain);
                    }else{
                        makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                    }

                    Utility.setSharedPreference(getApplicationContext(), Constants.appDomain, appDomain);
                }

            }
        });
    }

    public void initializeAds()
    {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

                AdLoader adLoader = new AdLoader.Builder(context, getString(R.string.nativead))
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
//                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(0xFFFF6666)).build();
                                NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                                TemplateView template = findViewById(R.id.my_template);
                                template.setVisibility(View.VISIBLE);
                                template.setStyles(styles);
                                template.setNativeAd(nativeAd);
                            }

                        })
                        .build();

                adLoader.loadAd(new AdRequest.Builder().build());

                loadBannerAds();
            }
        });
    }

    public void loadBannerAds(){

        AdView adView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }
        });

        AdSettings.addTestDevice("28294d2f-bad8-446c-af8f-d8382498275f");
        Resources res = getResources();
        com.facebook.ads.AdView fb_adView = new com.facebook.ads.AdView(this,res.getString(R.string.fb_banner), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.addView(fb_adView);
        fb_adView.loadAd();

    }

    public void setLocale(String localeName) {

        if(localeName.isEmpty() || localeName.equals("null")) {
            localeName = "en";
            Log.e("localName status", "empty");
        }
        Locale myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Log.e("Status", "Locale updated!");
        Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.isLocaleSet, true);
        Utility.setSharedPreference(getApplicationContext(), Constants.currentLocale, localeName);
    }

     private void getDataFromApi (String domain) {
         final ProgressDialog pd = new ProgressDialog(this);
         pd.setMessage("Loading");
         pd.setCancelable(false);
         pd.show();

         if(!domain.endsWith("/")) {
             domain += "/";
         }

         final String url = domain+"app";
         Log.e("Verification Url", url);

         StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String result) {
                 Log.e("Result", result);
                 if (result != null) {
                     pd.dismiss();
                     try {
                         JSONObject object = new JSONObject(result);

                         String success = "200"; //object.getString("status");
                         if (success.equals("200")) {
                             Utility.setSharedPreferenceBoolean(getApplicationContext(), "isUrlTaken", true);
                             Utility.setSharedPreference(MyApp.getContext(), Constants.apiUrl, object.getString("url"));
                             Utility.setSharedPreference(MyApp.getContext(), Constants.imagesUrl, object.getString("site_url"));
                             String app_ver= object.getString("app_ver");
                             Utility.setSharedPreference(getApplicationContext(), Constants.app_ver, app_ver);
                             String appLogo = object.getString("site_url") + "uploads/school_content/logo/app_logo/" + object.getString("app_logo");
                             Utility.setSharedPreference(MyApp.getContext(), Constants.appLogo, appLogo );

                             String secColour = object.getString("app_secondary_color_code");
                             String primaryColour = object.getString("app_primary_color_code");

                            /* String secColour = "#E7F1EE";
                             String primaryColour = "#424242";*/

                             if(secColour.length() == 7 && primaryColour.length() == 7) {
                                 Utility.setSharedPreference(getApplicationContext(), Constants.secondaryColour, secColour);
                                 Utility.setSharedPreference(getApplicationContext(), Constants.primaryColour, primaryColour);
                             }else {
                                 Utility.setSharedPreference(getApplicationContext(), Constants.secondaryColour, Constants.defaultSecondaryColour);
                                 Utility.setSharedPreference(getApplicationContext(), Constants.primaryColour, Constants.defaultPrimaryColour);
                             }
                             Log.e("apiUrl Utility", Utility.getSharedPreferences(getApplicationContext(), "apiUrl"));
                             langCode = object.getString("lang_code");
                             Utility.setSharedPreference(getApplicationContext(), Constants.langCode,langCode);

                             if(!langCode.isEmpty()) {
                                 setLocale(langCode);
                             }

                             ismaintenancemode();



                         } else {
                             Toast.makeText(getApplicationContext(), "Invalid Domain.", Toast.LENGTH_SHORT).show();
                         }
                     } catch (JSONException e) {
                             langCode = "";
                         e.printStackTrace();
                     }
                 } else {
                     pd.dismiss();
                     Toast.makeText(getApplicationContext(), "Invalid Domain.", Toast.LENGTH_SHORT).show();
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 pd.dismiss();
                 System.out.println("not responding");
                 try {
                     int  statusCode = error.networkResponse.statusCode;
                     NetworkResponse response = error.networkResponse;
                     Log.e("Volley Error",""+statusCode+" "+response.data.toString());
                     if(error instanceof ClientError) {
                         Toast.makeText(getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_SHORT).show();
                     } else {
                         Toast.makeText(getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_SHORT).show();
                     }
                 } catch (NullPointerException npe) {
                     Toast.makeText(getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_SHORT).show();
             }
             }
         });
         RequestQueue requestQueue = Volley.newRequestQueue(TakeUrl.this); //Creating a Request Queue
         requestQueue.add(stringRequest);//Adding request to the queue
     }


    private void ismaintenancemode() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getMaintenanceModeStatusUrl;
        System.out.println("url=="+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String maintenance_mode = object.getString("maintenance_mode");
                    System.out.println("maintenance_mode="+maintenance_mode.toString());
                    if(maintenance_mode.equals("0")){
                        Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", false);
                        pd.dismiss();
                        Intent asd = new Intent(getApplicationContext(), Login.class);
                        startActivity(asd);
                        finish();
                    } else{
                        Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", true);
                        pd.dismiss();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TakeUrl.this);
                        builder.setCancelable(false);
                        builder.setMessage(R.string.maintainMessage);
                        builder.setTitle("");
                        android.app.AlertDialog alert = builder.create();
                        alert.show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(TakeUrl.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                Log.e("Headers", headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(TakeUrl.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue


    }
}
