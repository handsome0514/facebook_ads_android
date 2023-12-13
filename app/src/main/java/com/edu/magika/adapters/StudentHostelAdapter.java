package com.edu.magika.adapters;

import android.app.ProgressDialog;
import android.graphics.Color;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.edu.magika.R;
import com.edu.magika.students.StudentHostel;
import com.edu.magika.utils.Constants;
import com.edu.magika.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;
import static com.edu.magika.utils.Utility.showNativeAd;

public class StudentHostelAdapter extends RecyclerView.Adapter<StudentHostelAdapter.MyViewHolder> {

    private StudentHostel context;
    private ArrayList<String> hostelIdList;
    private ArrayList<String> hostelNameList;
    private ArrayList<String> room_typeList;
    private ArrayList<String> room_noList;
    private ArrayList<String> no_of_bedList;
    private ArrayList<String> cost_per_bedList;
    private ArrayList<String> assignList;

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();

    StudentHostelDetailAdapter adapter;

    public StudentHostelAdapter(StudentHostel studentHostel, ArrayList<String> hostelIdList, ArrayList<String> hostelNameList,
                                ArrayList<String> room_typeList, ArrayList<String> room_noList, ArrayList<String> no_of_bedList,
                                ArrayList<String> cost_per_bedList, ArrayList<String> assignList) {
        this.context = studentHostel;
        this.hostelIdList = hostelIdList;
        this.hostelNameList = hostelNameList;
        this.room_typeList = room_typeList;
        this.room_noList = room_noList;
        this.no_of_bedList = no_of_bedList;
        this.cost_per_bedList = cost_per_bedList;
        this.assignList = assignList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView hostelNameTV,roomtypeTV,roomnoTV,noofbedTV,costperbedTV,statusTV;
        public RelativeLayout containerView;
        LinearLayout headLayout;

        public MyViewHolder(View view) {
            super(view);
            hostelNameTV = (TextView) view.findViewById(R.id.adapter_hostelnameTv);
            roomtypeTV = view.findViewById(R.id.adapter_roomtypeTV);
            roomnoTV= view.findViewById(R.id.adapter_roomnoTV);
            noofbedTV = view.findViewById(R.id.adapter_noofbedTV);
            costperbedTV = view.findViewById(R.id.adapter_costperbedTV);
            statusTV = view.findViewById(R.id.Adapter_statusTV);
            headLayout = view.findViewById(R.id.headLayout);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_hostel, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (position == 0)
        {
            showNativeAd(context);
        }

        final String currency = Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency);
        holder.hostelNameTV.setText(hostelNameList.get(position));
        holder.roomtypeTV.setText(room_typeList.get(position));
        holder.roomnoTV.setText(room_noList.get(position));
        holder.noofbedTV.setText(no_of_bedList.get(position));
        holder.costperbedTV.setText(currency +cost_per_bedList.get(position));
        if(assignList.get(position).equals("1")){
            holder.statusTV.setVisibility(View.VISIBLE);
        }else{
            holder.statusTV.setVisibility(View.GONE);
        }
        holder.headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));


    }

    @Override
    public int getItemCount() {
        return hostelIdList.size();
    }


}
