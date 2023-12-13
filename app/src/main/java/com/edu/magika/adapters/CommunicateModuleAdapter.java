package com.edu.magika.adapters;

import static com.edu.magika.utils.Utility.showInterstitialAd;

import android.content.Intent;
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
import com.edu.magika.R;
import com.edu.magika.model.Album1;
import com.edu.magika.students.NotificationList;
import com.edu.magika.students.StudentNoticeBoard;
import com.edu.magika.utils.Constants;
import com.edu.magika.utils.Utility;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class CommunicateModuleAdapter extends RecyclerView.Adapter<CommunicateModuleAdapter.MyViewHolder> {
    private FragmentActivity context;
    private List<Album1> albumList;

    View view;
    public CommunicateModuleAdapter(FragmentActivity context, ArrayList<Album1> albumList) {
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
        Album1 album1 = albumList.get(position);
         holder.modulename.setText(album1.getName());

         if(album1.getValue().equals("0")){
             holder.itemView.setVisibility(View.GONE);
             holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            // notifyItemRemoved(position);
         }else{
             holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
             holder.itemView.setVisibility(View.VISIBLE);
         }
       Picasso.get().load(album1.getThumbnail()).fit().centerInside().placeholder(null).into(holder.moduleiamge);
         holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInterstitialAd(context);
                    if(position==0){
                        Intent examlistintent=new Intent(context, StudentNoticeBoard.class);
                        context.startActivity(examlistintent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                    }else if(position==1){
                        Intent examscheduleintent=new Intent(context, NotificationList.class);
                        context.startActivity(examscheduleintent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
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


}
