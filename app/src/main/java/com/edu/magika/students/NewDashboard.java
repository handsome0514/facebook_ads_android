package com.edu.magika.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import com.edu.magika.AboutSchool;
import com.edu.magika.Login;
import com.edu.magika.R;
import com.edu.magika.SettingActivity;
import com.edu.magika.TakeUrl;
import com.edu.magika.adapters.AcademicModuleAdapter;
import com.edu.magika.adapters.CommunicateModuleAdapter;
import com.edu.magika.adapters.ElearningModuleAdapter;
import com.edu.magika.adapters.LoginChildListAdapter;
import com.edu.magika.adapters.OtherModuleAdapter;
import com.edu.magika.model.Album1;
import com.edu.magika.utils.Constants;
import com.edu.magika.utils.DatabaseHelper;
import com.edu.magika.utils.DrawerArrowDrawable;
import com.edu.magika.utils.Utility;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import static android.widget.Toast.makeText;
import static com.edu.magika.utils.Utility.showInterstitialAd;

public class NewDashboard extends AppCompatActivity {

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    public DrawerArrowDrawable drawerArrowDrawable;
    ImageView drawerIndicator,firstIV,secondIV,thirdIV,fourthIV;
    public DrawerLayout drawer;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA};
    protected FrameLayout mDrawerLayout, actionBar;
    CardView card_view_outer;
    private NavigationView navigationView;
    public boolean flipped;
    LoginChildListAdapter studentListAdapter;
    public float offset;
    ImageView actionBarLogo;
    TextView unread_count,version_name;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> aparams = new Hashtable<String, String>();
    FrameLayout notification_alert;
    private TextView classTV, nameTV, childDetailsTV;
    private ImageView profileImageIV;
    private LinearLayout switchChildBtn;
    private RelativeLayout drawerHead;
    public Map<String, String> logoutparams = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    String device_token;
    ArrayList<String> childIdList = new ArrayList<String>();
    ArrayList<String> childNameList = new ArrayList<String>();
    ArrayList<String> childClassList = new ArrayList<String>();
    ArrayList<String> childImageList = new ArrayList<String>();
    JSONArray modulesJson;
    TextView name,admissionno,classdata;
    ImageView profileImageview;
    ArrayList<String> moduleCodeList = new ArrayList<String>();
    ArrayList<String> moduleStatusList = new ArrayList<String>();
    LinearLayout profilelinear;
    CollapsingToolbarLayout collapsing_toolbar;
    RecyclerView elearning_recyclerView,academic_recyclerView,communicate_recyclerView,other_recyclerView;
    ElearningModuleAdapter elearningModuleAdapter;
    AcademicModuleAdapter academicModuleAdapter;
    CommunicateModuleAdapter communicateModuleAdapter;
    OtherModuleAdapter otherModuleAdapter;
    ArrayList<Album1> communicatealbumList,elearningalbumList,academicalbumList,otheralbumList;
    TextView textview1,textview2,textview3,textview4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        setContentView(R.layout.activity_new_dashboard);

        drawerIndicator = findViewById(R.id.drawer_indicator);
        drawer = findViewById(R.id.drawer_layout);
        actionBar = findViewById(R.id.actionBar);
        unread_count = findViewById(R.id.unread_count);
        actionBarLogo = findViewById(R.id.actionBar_logo);
        notification_alert = findViewById(R.id.notification_alert);
        navigationView = findViewById(R.id.nav_view);
        profilelinear = findViewById(R.id.profilelinear);
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        name = findViewById(R.id.name);
        admissionno = findViewById(R.id.admissionno);
        textview1 = findViewById(R.id.textview1);
        textview1.setText(getApplicationContext().getString(R.string.elearning));
        textview2 = findViewById(R.id.textview2);
        textview2.setText(getApplicationContext().getString(R.string.academic));
        textview3 = findViewById(R.id.textview3);
        textview3.setText(getApplicationContext().getString(R.string.communicate));
        textview4 = findViewById(R.id.textview4);
        textview4.setText(getApplicationContext().getString(R.string.others));
        classdata = findViewById(R.id.classdata);
        profileImageview = findViewById(R.id.studentProfile_profileImageview);
        card_view_outer = findViewById(R.id.card_view_outer);
        Locale current = getResources().getConfiguration().locale;

        params.put("site_url", Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl));
        JSONObject obj=new JSONObject(params);
        Log.e("params", obj.toString());
        System.out.println("params==" +obj.toString());
        getDatasFromApi(obj.toString());
        // makeText(this, Utility.getSharedPreferences(getApplicationContext(),Constants.currentLocale), Toast.LENGTH_SHORT).show();
        elearningalbumList = new ArrayList<>();
        academicalbumList = new ArrayList<>();
        communicatealbumList = new ArrayList<>();
        otheralbumList = new ArrayList<>();

        initializeAds();

        prepareNavList();
        setUpDrawer();
        decorate();
        setUpPermission();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        device_token = task.getResult();
                    }
                });
        DatabaseHelper db = new DatabaseHelper(NewDashboard.this);
        int profile_counts = db.getProfilesCount();
        db.close();
        if(String.valueOf(profile_counts).equals("0")){
            unread_count.setVisibility(View.GONE);
        }else{
            unread_count.setText(String.valueOf(profile_counts));
        }



        if(Utility.getSharedPreferences(getApplicationContext(), "role").equals("parent")) {
            if(Utility.getSharedPreferencesBoolean(getApplicationContext(), "hasMultipleChild")) {
                switchChildBtn.setVisibility(View.VISIBLE);
            } else {
                switchChildBtn.setVisibility(View.GONE);
            }
        } else {
            switchChildBtn.setVisibility(View.GONE);
        }

        switchChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChildList();
            }
        });

        elearning_recyclerView = findViewById(R.id.elearning_recyclerView);
        elearningModuleAdapter = new ElearningModuleAdapter(NewDashboard.this,elearningalbumList);
        GridLayoutManager elearningLayoutManager=new GridLayoutManager(this,4);
        elearning_recyclerView.setLayoutManager(elearningLayoutManager);
        elearning_recyclerView.setAdapter(elearningModuleAdapter);
        // elearning();

        academic_recyclerView = findViewById(R.id.academic_recyclerView);
        academicModuleAdapter = new AcademicModuleAdapter(NewDashboard.this,academicalbumList);
        GridLayoutManager academicLayoutManager=new GridLayoutManager(this,4);
        academic_recyclerView.setLayoutManager(academicLayoutManager);
        academic_recyclerView.setAdapter(academicModuleAdapter);
        //academic();

        communicate_recyclerView = findViewById(R.id.communicate_recyclerView);
        communicateModuleAdapter = new CommunicateModuleAdapter(NewDashboard.this,communicatealbumList);
        GridLayoutManager communicateLayoutManager=new GridLayoutManager(this,4);
        communicate_recyclerView.setLayoutManager(communicateLayoutManager);
        communicate_recyclerView.setAdapter(communicateModuleAdapter);
        //  communicate();

        other_recyclerView = findViewById(R.id.other_recyclerView);
        otherModuleAdapter = new OtherModuleAdapter(NewDashboard.this,otheralbumList);
        GridLayoutManager otherLayoutManager=new GridLayoutManager(this,4);
        other_recyclerView.setLayoutManager(otherLayoutManager);
        other_recyclerView.setAdapter(otherModuleAdapter);
        // other();


        notification_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(NewDashboard.this);
                db.updatestatus("0","1");

                Intent intent=new Intent(NewDashboard.this,NotificationList.class);
                startActivity(intent);
            }
        });


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Title");

        Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.drawerIndicatorColour));

        drawerIndicator.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }
                drawerArrowDrawable.setParameter(offset);
            }
        });

        drawerIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")){
            if (Utility.isConnectingToInternet(getApplicationContext())) {
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                params.put("date_from", getDateOfMonth(new Date(), "first"));
                params.put("date_to", getDateOfMonth(new Date(), "last"));
                params.put("role", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
                params.put("user_id", Utility.getSharedPreferences(getApplicationContext(), Constants.userId));
                JSONObject object=new JSONObject(params);
                Log.e("params ", object.toString());
                getDataFromApi(object.toString());
            } else {
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }

        }else{
            if (Utility.isConnectingToInternet(getApplicationContext())) {
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                params.put("date_from", getDateOfMonth(new Date(), "first"));
                params.put("date_to", getDateOfMonth(new Date(), "last"));
                params.put("role", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
                JSONObject object=new JSONObject(params);
                Log.e("params ", object.toString());
                getDataFromApi(object.toString());
            } else {
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }

            if (Utility.isConnectingToInternet(getApplicationContext())) {
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                JSONObject object=new JSONObject(params);
                Log.e("params ", object.toString());
                getCurrencyDataFromApi(object.toString());
            } else {
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static String getDateOfMonth(Date date, String index){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(index.equals("first")) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        } else {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormatter.format(cal.getTime());
    }
    private void getElearningFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getELearningUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Modules Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Modules Result"+result);

                        modulesJson = object.getJSONArray("module_list");
                        System.out.println("Modules length"+modulesJson.length());

                        int[] covers = new int[]{
                                R.drawable.ic_dashboard_homework,
                                R.drawable.ic_assignment,
                                R.drawable.ic_lessonplan,
                                R.drawable.ic_onlineexam,
                                R.drawable.ic_downloadcenter,
                                R.drawable.ic_onlinecourse,
                                R.drawable.ic_videocam,
                                R.drawable.ic_videocam,
                        };

                        Utility.setSharedPreference(getApplicationContext(), Constants.modulesArray, modulesJson.toString());
                        if (modulesJson.length() != 0) {
                            for(int i = 0; i < modulesJson.length(); i++) {
                                Album1 album1=new Album1();
                                album1.setName(modulesJson.getJSONObject(i).getString("name"));
                                album1.setValue(modulesJson.getJSONObject(i).getString("status"));
                                album1.setThumbnail(covers[i]);
                               /* moduleCodeList.add(modulesJson.getJSONObject(i).getString("short_code"));
                                moduleStatusList.add(modulesJson.getJSONObject(i).getString("status"));*/
                                elearningalbumList.add(album1);
                            }
                            elearningModuleAdapter.notifyDataSetChanged();
                            //setMenu(navigationView.getMenu());
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    private void getAcademicsFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getAcademicsUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Modules Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Modules Result"+result);

                        modulesJson = object.getJSONArray("module_list");
                        System.out.println("Modules length"+modulesJson.length());

                        int[] covers = new int[]{
                                R.drawable.ic_calender_cross,
                                R.drawable.ic_lessonplan,
                                R.drawable.ic_nav_attendance,
                                R.drawable.ic_nav_reportcard,
                                R.drawable.ic_dashboard_homework,
                                R.drawable.ic_nav_timeline,
                                R.drawable.ic_documents_certificate,
                        };
                        Utility.setSharedPreference(getApplicationContext(), Constants.modulesArray, modulesJson.toString());
                        if (modulesJson.length() != 0) {
                            for(int i = 0; i < modulesJson.length(); i++) {
                                Album1 album1=new Album1();
                                album1.setName(modulesJson.getJSONObject(i).getString("name"));
                                album1.setValue(modulesJson.getJSONObject(i).getString("status"));
                                album1.setThumbnail(covers[i]);
                               /* moduleCodeList.add(modulesJson.getJSONObject(i).getString("short_code"));
                                moduleStatusList.add(modulesJson.getJSONObject(i).getString("status"));*/
                                academicalbumList.add(album1);
                            }
                            academicModuleAdapter.notifyDataSetChanged();
                            //setMenu(navigationView.getMenu());
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    private void getCommunicateFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getCommunicateUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Modules Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Modules Result"+result);

                        modulesJson = object.getJSONArray("module_list");
                        System.out.println("Modules length"+modulesJson.length());

                        int[] covers = new int[]{
                                R.drawable.ic_notice,
                                R.drawable.ic_notification
                        };

                        Utility.setSharedPreference(getApplicationContext(), Constants.modulesArray, modulesJson.toString());
                        if (modulesJson.length() != 0) {
                            for(int i = 0; i < modulesJson.length(); i++) {
                                Album1 album1=new Album1();
                                album1.setName(modulesJson.getJSONObject(i).getString("name"));
                                album1.setValue(modulesJson.getJSONObject(i).getString("status"));
                                album1.setThumbnail(covers[i]);
                               /* moduleCodeList.add(modulesJson.getJSONObject(i).getString("short_code"));
                                moduleStatusList.add(modulesJson.getJSONObject(i).getString("status"));*/
                                communicatealbumList.add(album1);
                            }
                            communicateModuleAdapter.notifyDataSetChanged();
                            //setMenu(navigationView.getMenu());
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    private void getOthersFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getOthersUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Modules Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Modules Result"+result);

                        modulesJson = object.getJSONArray("module_list");
                        System.out.println("Modules length"+modulesJson.length());

                        int[] covers = new int[]{
                                R.drawable.ic_nav_fees,
                                R.drawable.ic_leave,
                                R.drawable.ic_visitors,
                                R.drawable.ic_nav_transport,
                                R.drawable.ic_nav_hostel,
                                R.drawable.ic_dashboard_pandingtask,
                                R.drawable.ic_library,
                                R.drawable.ic_teacher

                        };

                        Utility.setSharedPreference(getApplicationContext(), Constants.modulesArray, modulesJson.toString());
                        if (modulesJson.length() != 0) {
                            for(int i = 0; i < modulesJson.length(); i++) {
                                Album1 album1=new Album1();
                                album1.setName(modulesJson.getJSONObject(i).getString("name"));
                                album1.setValue(modulesJson.getJSONObject(i).getString("status"));
                                album1.setThumbnail(covers[i]);
                               /* moduleCodeList.add(modulesJson.getJSONObject(i).getString("short_code"));
                                moduleStatusList.add(modulesJson.getJSONObject(i).getString("status"));*/
                                otheralbumList.add(album1);
                            }
                            otherModuleAdapter.notifyDataSetChanged();
                            //setMenu(navigationView.getMenu());
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    private void setUpDrawer() {
        //HEADER
        View headerLayout = navigationView.getHeaderView(0);

        Menu menu = navigationView.getMenu();
        RelativeLayout tracks = (RelativeLayout) menu.findItem(R.id.nav_log_version).getActionView();
        TextView version_name = (TextView) tracks.findViewById(R.id.version_name);
        version_name.setText(getApplicationContext().getString(R.string.version)+" on "+Utility.getSharedPreferences(getApplicationContext(), Constants.app_ver));

        classTV = headerLayout.findViewById(R.id.drawer_userClass);
        nameTV = headerLayout.findViewById(R.id.drawer_userName);
        profileImageIV = headerLayout.findViewById(R.id.drawer_logo);
        drawerHead = headerLayout.findViewById(R.id.drawer_head);
        switchChildBtn = headerLayout.findViewById(R.id.drawer_switchChildBtn);
        childDetailsTV = headerLayout.findViewById(R.id.drawer_studentDetailsTV);
        //HEADER
        Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.drawerIndicatorColour));

        drawerIndicator.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }
                drawerArrowDrawable.setParameter(offset);
            }
        });
        drawerIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void showChildList() {

        View view = getLayoutInflater().inflate(R.layout.fragment_login_bottom_sheet, null);
        view.setMinimumHeight(500);

        TextView headerTV = view.findViewById(R.id.login_bottomSheet_header);
        ImageView crossBtn = view.findViewById(R.id.login_bottomSheet_crossBtn);
        RecyclerView childListView = view.findViewById(R.id.login_bottomSheet_listview);

        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        headerTV.setText(getString(R.string.childList));

        Log.e("ImageList", childImageList.toString());
        Log.e("Class List", childClassList.toString());
        Log.e("ID List", childIdList.toString());

        studentListAdapter = new LoginChildListAdapter(NewDashboard.this, childIdList, childNameList, childClassList, childImageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        childListView.setLayoutManager(mLayoutManager);
        childListView.setItemAnimator(new DefaultItemAnimator());
        childListView.setAdapter(studentListAdapter);

        final BottomSheetDialog dialog = new BottomSheetDialog(NewDashboard.this);

        dialog.setContentView(view);
        dialog.show();

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("parent_id", Utility.getSharedPreferences(getApplicationContext(), "userId"));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getStudentsListFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
        Log.e("Child Name", childNameList.toString());
    }

    private void setUpPermission() {
        if(ActivityCompat.checkSelfPermission(NewDashboard.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(NewDashboard.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(NewDashboard.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(NewDashboard.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[3])){
                ActivityCompat.requestPermissions(NewDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            } else {
                ActivityCompat.requestPermissions(NewDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.permissionStatus, true);
        }
    }


    /*   private void setMenu(final Menu navMenu) {

           if(moduleCodeList.contains("live_classes")) {
               System.out.println("live CLasses Integration");
               navMenu.findItem(R.id.nav_zoomliveclasses).setVisible(true);
           } else {
               System.out.println("live CLasses not Integration");
               navMenu.findItem(R.id.nav_zoomliveclasses).setVisible(false);
           }

           if(moduleCodeList.contains("gmeet_live_classes")) {
               navMenu.findItem(R.id.nav_livegmeetclass).setVisible(true);
           } else {
               navMenu.findItem(R.id.nav_livegmeetclass).setVisible(false);
           }

           if(moduleCodeList.contains("online_course")) {
               navMenu.findItem(R.id.nav_online_course).setVisible(true);
           } else {
               navMenu.findItem(R.id.nav_online_course).setVisible(false);
           }

           for (int i = 0; i<moduleCodeList.size(); i++) {
               if (moduleCodeList.get(i).equals("fees") && moduleStatusList.get(i).equals("0")) {
                   System.out.println("fees Hide");
                   navMenu.findItem(R.id.nav_fees).setVisible(false);

               } if (moduleCodeList.get(i).equals("student_attendance") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_attendance).setVisible(false);
               } if (moduleCodeList.get(i).equals("examinations") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_examination).setVisible(false);

               } if (moduleCodeList.get(i).equals("download_center") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_downloadCenter).setVisible(false);
               } if (moduleCodeList.get(i).equals("library") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_library).setVisible(false);
               } if (moduleCodeList.get(i).equals("transport_routes") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_transportRoute).setVisible(false);
               } if (moduleCodeList.get(i).equals("hostel_rooms") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_hostel).setVisible(false);
               } if (moduleCodeList.get(i).equals("homework") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_homework).setVisible(false);

               } if (moduleCodeList.get(i).equals("communicate") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_noticeBoard).setVisible(false);

               }if (moduleCodeList.get(i).equals("online_examination") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_onlineexam).setVisible(false);
               }if (moduleCodeList.get(i).equals("class_timetable") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_timetable).setVisible(false);
               }if (moduleCodeList.get(i).equals("attendance") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_attendance).setVisible(false);
               }if (moduleCodeList.get(i).equals("teachers_rating") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_teachers).setVisible(false);
               }if (moduleCodeList.get(i).equals("notice_board") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_noticeBoard).setVisible(false);
               }if (moduleCodeList.get(i).equals("lesson_plan") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_syllabus).setVisible(false);
               }if (moduleCodeList.get(i).equals("syllabus_status") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_syllabus_status).setVisible(false);
               }if (moduleCodeList.get(i).equals("apply_leave") && moduleStatusList.get(i).equals("0")) {
                   navMenu.findItem(R.id.nav_applyleave).setVisible(false);
               } if(moduleCodeList.get(i).equals("live_classes") && moduleStatusList.get(i).equals("0")) {
                   System.out.println("live CLasses Hide");
                   navMenu.findItem(R.id.nav_zoomliveclasses).setVisible(false);
               }if(moduleCodeList.get(i).equals("gmeet_live_classes") && moduleStatusList.get(i).equals("0")) {
                   System.out.println("google_meeting Hide");
                   navMenu.findItem(R.id.nav_livegmeetclass).setVisible(false);
               }if(moduleCodeList.get(i).equals("online_course") && moduleStatusList.get(i).equals("0")) {
                   System.out.println("google_meeting Hide");
                   navMenu.findItem(R.id.nav_online_course).setVisible(false);
               }
           }
       }*/
    private void decorate() {

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        String appLogo = Utility.getSharedPreferences(this, Constants.appLogo)+"?"+new Random().nextInt(11);

        Picasso.get().load(Utility.getSharedPreferences(this, "userImage")).placeholder(R.drawable.placeholder_user).into(profileImageIV);
        Picasso.get().load(Utility.getSharedPreferences(this, "userImage")).placeholder(R.drawable.placeholder_user).into(profileImageview);
        Picasso.get().load(appLogo).fit().centerInside().placeholder(null).into(actionBarLogo);

        nameTV.setText(Utility.getSharedPreferences(this, Constants.userName));
        admissionno.setText("Admission No. "+Utility.getSharedPreferences(this, Constants.admission_no));
        classdata.setText(Utility.getSharedPreferences(this, Constants.classSection));
        name.setText(Utility.getSharedPreferences(this, Constants.userName));
        classTV.setText(Utility.getSharedPreferences(this, Constants.classSection));
        childDetailsTV.setText("Child - " + Utility.getSharedPreferences(getApplicationContext(), "studentName")
                + "\n" + Utility.getSharedPreferences(this, Constants.classSection));

        if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")) {
            classTV.setVisibility(View.GONE);
            childDetailsTV.setVisibility(View.VISIBLE);
        } else {
            classTV.setVisibility(View.VISIBLE);
            childDetailsTV.setVisibility(View.GONE);
        }
        System.out.println("Colour=="+Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour));
        actionBar.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        profilelinear.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        collapsing_toolbar.setContentScrimColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));

        drawerHead.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        }
    }

    //RUNTIME PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults
        );
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){

            if(ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[3])){

                AlertDialog.Builder builder = new AlertDialog.Builder(NewDashboard.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs to access to your storage, camera and call permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(NewDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {

                if(Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(NewDashboard.this);
                    builder.setTitle("Allow Notifications");
                    builder.setMessage("For smooth functioning of app, please provide Auto-Start permission and allow notification access.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {

                }

            }
        }
    }

    private void getStudentsListFromApi (String bodyParams) {

        childIdList.clear(); childNameList.clear(); childClassList.clear(); childImageList.clear();

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.parent_getStudentList;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        JSONArray dataArray = object.getJSONArray("childs");
                        if (dataArray.length() != 0) {

                            for(int i = 0; i < dataArray.length(); i++) {
                                childIdList.add(dataArray.getJSONObject(i).getString("id"));
                                childNameList.add(dataArray.getJSONObject(i).getString("firstname") + " " +  dataArray.getJSONObject(i).getString("lastname") );
                                childClassList.add(dataArray.getJSONObject(i).getString("class") + "-" +  dataArray.getJSONObject(i).getString("section"));
                                childImageList.add(dataArray.getJSONObject(i).getString("image"));
                            }
                            studentListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getDatasFromApi(String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = "https://sstrace.qdocs.in/postlic/verifyappjsonv2";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                System.out.println("result=="+result);
                if (result != null) {
                    pd.dismiss();
                    try {

                        JSONObject object = new JSONObject(result);

                        if(object.getString("status").equals("1")) {
                            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.isLoggegIn, false);

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NewDashboard.this);
                            builder.setCancelable(false);
                            //builder.setMessage(R.string.verificationMessage);
                            builder.setMessage(object.getString("msg"));
                            builder.setTitle("");
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    if (Utility.isConnectingToInternet(getApplicationContext())) {
                                        logoutparams.put("deviceToken", device_token);
                                        JSONObject obj=new JSONObject(logoutparams);
                                        Log.e("params ", obj.toString());
                                        System.out.println("Logout Details=="+obj.toString());
                                        loginOutApi(obj.toString());
                                    } else {
                                        makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                           android.app.AlertDialog alert = builder.create();
                            alert.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);


    }


    private void prepareNavList() {
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("user", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getElearningFromApi(obj.toString());
            getCommunicateFromApi(obj.toString());
            getAcademicsFromApi(obj.toString());
            getOthersFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        showInterstitialAd(NewDashboard.this);
                        Intent dashboard = new Intent(NewDashboard.this, NewDashboard.class);
                        startActivity(dashboard);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_profile:
                        showInterstitialAd(NewDashboard.this);
                        Intent profile = new Intent(NewDashboard.this, StudentProfileDetailsNew.class);
                        startActivity(profile);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_about:
                        showInterstitialAd(NewDashboard.this);
                        Intent about = new Intent(NewDashboard.this, AboutSchool.class);
                        startActivity(about);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_setting:
                        showInterstitialAd(NewDashboard.this);
                        Intent setting = new Intent(NewDashboard.this, SettingActivity.class);
                        startActivity(setting);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_logout:
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NewDashboard.this);
                        builder.setCancelable(false);
                        builder.setMessage(R.string.logoutMsg);
                        builder.setTitle("");
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                if (Utility.isConnectingToInternet(getApplicationContext())) {
                                    logoutparams.put("deviceToken", device_token);
                                    JSONObject obj=new JSONObject(logoutparams);
                                    Log.e("params ", obj.toString());
                                    System.out.println("Logout Details=="+obj.toString());
                                    loginOutApi(obj.toString());
                                } else {
                                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }
                return false;
            }
        });

    }



    public void initializeAds()
    {
        Context context = this;

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

    }

    private void loginOutApi (String bodyParams) {
        DatabaseHelper dataBaseHelpers = new DatabaseHelper(NewDashboard.this);
        dataBaseHelpers.deleteAll() ;

        final ProgressDialog pd = new ProgressDialog(NewDashboard.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(NewDashboard.this, "apiUrl")+ Constants.logoutUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = object.getString("status");
                        if (success.equals("1")) {
                            Utility.setSharedPreferenceBoolean(getApplicationContext(), "isLoggegIn", false);
                            Intent logout = new Intent(NewDashboard.this, Login.class);
                            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            logout.putExtra("EXIT", true);
                            startActivity(logout);
                            finish();
                        } else {
                            Intent intent=new Intent(NewDashboard.this, TakeUrl.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(NewDashboard.this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                // Toast.makeText(StudentDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(NewDashboard.this,TakeUrl.class);
                startActivity(intent);
                finish();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(NewDashboard.this, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(NewDashboard.this, "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getDataFromApi (String bodyParams) {

        Log.e("RESULT PARAMS", bodyParams);


        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.getDashboardUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        //TODO success
                        String success = "1"; //object.getString("success");
                        if (success.equals("1")) {

                            Utility.setSharedPreference(getApplicationContext(), Constants.classId, object.getString("class_id"));
                            Utility.setSharedPreference(getApplicationContext(), Constants.sectionId, object.getString("section_id"));

                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }

    private void getCurrencyDataFromApi (String bodyParams) {
        Log.e("RESULT PARAMS", bodyParams);
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.getStudentCurrencyUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        //TODO success
                        JSONArray data = object.getJSONArray("result");
                        System.out.println("Currency data=="+data.toString());
                        Utility.setSharedPreference(getApplicationContext(), Constants.currency_price, data.getJSONObject(0).getString("base_price"));
                        Utility.setSharedPreference(getApplicationContext(), Constants.currency_short_name, data.getJSONObject(0).getString("name"));
                        Utility.setSharedPreference(getApplicationContext(), Constants.currency, data.getJSONObject(0).getString("symbol"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }

}
