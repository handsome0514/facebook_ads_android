package com.edu.magika.adapters;

import static com.edu.magika.utils.Utility.showInterstitialAd;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edu.magika.R;
import com.edu.magika.model.Album1;
import com.edu.magika.students.StudentDailyAssignment;
import com.edu.magika.students.StudentDownloads;
import com.edu.magika.students.StudentGmeetLiveClasses;
import com.edu.magika.students.StudentHomeworkNew;
import com.edu.magika.students.StudentLiveClasses;
import com.edu.magika.students.StudentOnlineCourse;
import com.edu.magika.students.StudentOnlineExam;
import com.edu.magika.students.StudentSyllabusTimetable;
import com.edu.magika.utils.Constants;
import com.edu.magika.utils.Utility;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class ElearningModuleAdapter extends RecyclerView.Adapter<ElearningModuleAdapter.MyViewHolder> {
    private FragmentActivity context;
    private List<Album1> albumList;
    ArrayList<String> moduleCodeList=new ArrayList<String>();
    ArrayList<String> moduleStatusList=new ArrayList<String>();
    public Map<String, String> aparams = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    View view;
    public ElearningModuleAdapter(FragmentActivity context, List<Album1> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_module_selection, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView modulename;
        LinearLayout layout;
        ImageView moduleiamge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            modulename = (TextView) itemView.findViewById(R.id.modulename);
            moduleiamge = (ImageView) itemView.findViewById(R.id.moduleiamge);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Utility.setLocale(context, Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
        Album1 album = albumList.get(position);
        holder.modulename.setText(album.getName());
        if(album.getValue().equals("0")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            // notifyItemRemoved(position);
        }else{
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.itemView.setVisibility(View.VISIBLE);
        }
        Picasso.get().load(album.getThumbnail()).fit().centerInside().placeholder(null).into(holder.moduleiamge);
        System.out.println("moduleCodeList=="+moduleCodeList.toString());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitialAd(context);
                if(position==0){
                    Intent examlistintent=new Intent(context, StudentHomeworkNew.class);
                    context.startActivity(examlistintent);
                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                }else if(position==1){
                    Intent examscheduleintent=new Intent(context, StudentDailyAssignment.class);
                    context.startActivity(examscheduleintent);
                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                }else if(position==2){
                    Intent lessonplanintent=new Intent(context, StudentSyllabusTimetable.class);
                    context.startActivity(lessonplanintent);
                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                }else if(position==3){
                    Intent addexpenseintent=new Intent(context, StudentOnlineExam.class);
                    context.startActivity(addexpenseintent);
                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                }else if(position==4){
                    Intent addexpenseintent=new Intent(context, StudentDownloads.class);
                    context.startActivity(addexpenseintent);
                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                }else if(position==5){
                    aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                    aparams.put("addontype","ssoclc");
                    JSONObject ocobj=new JSONObject(aparams);
                    Log.e("CheckAddon params", ocobj.toString());
                    CheckAddon(ocobj.toString(),"ssoclc");
                }else if(position==6){
                    aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                    aparams.put("addontype","sszlc");
                    JSONObject zobj=new JSONObject(aparams);
                    Log.e("CheckAddon params", zobj.toString());
                    CheckAddon(zobj.toString(),"sszlc");
                }else if(position==7){
                    aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                    aparams.put("addontype", "ssglc");
                    JSONObject gobj=new JSONObject(aparams);
                    Log.e("CheckAddon params", gobj.toString());
                    CheckAddon(gobj.toString(),"ssglc");
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


    private void CheckAddon(String bodyParams, final String type) {

        final String requestBody = bodyParams;
        String url = "https://sstrace.qdocs.in/postlic/verifyaddon";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {

                    try {

                        JSONObject object = new JSONObject(result);
                        if(object.getString("status").equals("1")) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            builder.setCancelable(false);
                            builder.setMessage(R.string.verificationMessage);
                            //builder.setMessage(object.getString("msg"));
                            builder.setTitle("");
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            if(type.equals("sszlc")){
                                Intent liveclasses = new Intent(context, StudentLiveClasses.class);
                                context.startActivity(liveclasses);
                                context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                            }else if(type.equals("ssoclc")){
                                Intent online_course = new Intent(context, StudentOnlineCourse.class);
                                context.startActivity(online_course);
                                context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                            }else if(type.equals("ssglc")){
                                Intent gmeetliveclasses = new Intent(context, StudentGmeetLiveClasses.class);
                                context.startActivity(gmeetliveclasses);
                                context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(context.getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(context.getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(context.getApplicationContext(), "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    public  void setMenu(){

    }



}
