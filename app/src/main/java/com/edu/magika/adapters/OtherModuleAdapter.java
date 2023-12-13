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
import com.edu.magika.students.StudentAppyLeave;
import com.edu.magika.students.StudentFees;
import com.edu.magika.students.StudentHostel;
import com.edu.magika.students.StudentLibraryBookIssued;
import com.edu.magika.students.StudentTasks;
import com.edu.magika.students.StudentTeachersList;
import com.edu.magika.students.StudentTransportRoutes;
import com.edu.magika.students.StudentVisitorBook;
import com.edu.magika.utils.Constants;
import com.edu.magika.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OtherModuleAdapter extends RecyclerView.Adapter<OtherModuleAdapter.MyViewHolder> {
    private FragmentActivity context;
    private List<Album1> albumList;

    View view;
    public OtherModuleAdapter(FragmentActivity context, List<Album1> albumList) {
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
        LinearLayout layout,view;
        ImageView moduleiamge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            modulename = (TextView) itemView.findViewById(R.id.modulename);
            moduleiamge = (ImageView) itemView.findViewById(R.id.moduleiamge);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            view = (LinearLayout) itemView.findViewById(R.id.view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Utility.setLocale(context, Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
        Album1 album = albumList.get(position);
         holder.modulename.setText(album.getName());
        if(album.getValue().equals("0")){
            holder.layout.setVisibility(View.GONE);
            holder.view.setVisibility(View.VISIBLE);
           // holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            // notifyItemRemoved(position);
        }else{
           // holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.layout.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.GONE);
        }
         Picasso.get().load(album.getThumbnail()).fit().centerInside().placeholder(null).into(holder.moduleiamge);
         holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInterstitialAd(context);
                    if(position==0){
                        Intent examlistintent=new Intent(context, StudentFees.class);
                        context.startActivity(examlistintent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                    }else if(position==1){
                        Intent examscheduleintent=new Intent(context, StudentAppyLeave.class);
                        context.startActivity(examscheduleintent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                    }else if(position==2){
                        Intent lessonplanintent=new Intent(context, StudentVisitorBook.class);
                        context.startActivity(lessonplanintent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                    }else if(position==3){
                        Intent addexpenseintent=new Intent(context, StudentTransportRoutes.class);
                        context.startActivity(addexpenseintent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                    }else if(position==4){
                        Intent addexpenseintent=new Intent(context, StudentHostel.class);
                        context.startActivity(addexpenseintent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                    }else if(position==5){
                        Intent addexpenseintent=new Intent(context, StudentTasks.class);
                        context.startActivity(addexpenseintent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                    }else if(position==6){
                        Intent addexpenseintent=new Intent(context, StudentLibraryBookIssued.class);
                        context.startActivity(addexpenseintent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                    }else if(position==7){
                        Intent addexpenseintent=new Intent(context, StudentTeachersList.class);
                        context.startActivity(addexpenseintent);
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
