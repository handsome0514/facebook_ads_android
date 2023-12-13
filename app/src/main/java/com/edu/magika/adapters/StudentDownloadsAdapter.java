package com.edu.magika.adapters;

import static android.widget.Toast.makeText;

import static com.edu.magika.utils.Utility.showNativeAd;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.edu.magika.OpenPdf;
import com.edu.magika.R;
import com.edu.magika.students.StudentVideoTutorialPlay;
import com.edu.magika.utils.Constants;
import com.edu.magika.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentDownloadsAdapter extends BaseAdapter {
    long downloadID;
    private FragmentActivity context;
    private ArrayList<String> idList;
    private ArrayList<String> nameList;
    private ArrayList<String> sharedateList;
    private ArrayList<String> valid_uptoList;
    private ArrayList<String> sharebyList;
    private ArrayList<String> descriptionList;
    private ArrayList<String> uploaddateList;
    private ArrayList<String> uploadcontentsArray;
    private ArrayList<String> checkdate;
    private ArrayList<String> created_bylist;
    String urlStr,videoUrl,title,thumbpath;
    RecyclerView recyclerView;
    StudentContentUploadAdapter adapter;
    public String defaultDateFormat,superadmin_restriction;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    public StudentDownloadsAdapter(FragmentActivity activity, ArrayList<String> idList, ArrayList<String> nameList,
                                   ArrayList<String> sharedateList, ArrayList<String> sharebyList, ArrayList<String> valid_uptoList, ArrayList<String> descriptionList, ArrayList<String> uploaddateList, ArrayList<String> uploadcontentsArray, ArrayList<String> checkdate, ArrayList<String> created_bylist) {

        this.context = activity;
        this.idList = idList;
        this.nameList = nameList;
        this.sharedateList = sharedateList;
        this.sharebyList = sharebyList;
        this.valid_uptoList = valid_uptoList;
        this.descriptionList = descriptionList;
        this.uploaddateList = uploaddateList;
        this.uploadcontentsArray = uploadcontentsArray;
        this.checkdate = checkdate;
        this.created_bylist = created_bylist;

    }
    @Override
    public int getCount() {
        return nameList.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        superadmin_restriction = Utility.getSharedPreferences(context.getApplicationContext(), Constants.superadmin_restriction);

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.adapter_fragment_studentdownload, viewGroup, false);


        LinearLayout nameHeader = view.findViewById(R.id.headLayout);
        LinearLayout attachment_layout = view.findViewById(R.id.attachment_layout);
        TableLayout attachmentTable = view.findViewById(R.id.studentAdapter_Table);
        TextView sharedate = (TextView) view.findViewById(R.id.sharedate);
        TextView valid_upto = (TextView) view.findViewById(R.id.valid_upto);
        TextView shareby = (TextView) view.findViewById(R.id.shareby);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView uploaddate = (TextView) view.findViewById(R.id.uploaddate);
        TextView titlename = (TextView) view.findViewById(R.id.title);
        sharedate.setTag(position);

        if (position == 0)
        {
            showNativeAd(context);
        }

        sharedate.setText(sharedateList.get(position));
        description.setText(descriptionList.get(position));
        uploaddate.setText(uploaddateList.get(position));
        titlename.setText(nameList.get(position));

        if(superadmin_restriction.equals("enabled")){
            shareby.setText(sharebyList.get(position));
        }else{
            if(created_bylist.get(position).equals("1")){
                shareby.setText("");
            }else{
                shareby.setText(sharebyList.get(position));
            }
        }

        try {
            if (new SimpleDateFormat("yyyy-MM-dd").parse(checkdate.get(position)).before(new Date())) {
                valid_upto.setText(valid_uptoList.get(position)+" Expires");
                attachment_layout.setVisibility(View.GONE);
            }else{
                valid_upto.setText(valid_uptoList.get(position));
                attachment_layout.setVisibility(View.VISIBLE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        nameHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

        Log.e("DATA==",uploadcontentsArray.get(position));

        try {
            final JSONArray dataArray = new JSONArray(uploadcontentsArray.get(position));
            Log.e("DDDATA==", String.valueOf(dataArray.length()));
            if(String.valueOf(dataArray.length()).equals("0")){
                attachmentTable.setVisibility(View.GONE);
            }else{
                attachmentTable.setVisibility(View.VISIBLE);

                for(int i=0; i<dataArray.length(); i++) {
                    final TableRow tr = (TableRow) context.getLayoutInflater().inflate(R.layout.adapter_content_upload_detail, null);

                    TextView filename;

                    filename = (TextView) tr.findViewById(R.id.filename);
                    LinearLayout downloadBtn = (LinearLayout) tr.findViewById(R.id.downloadBtn);
                    filename.setText(dataArray.getJSONObject(i).getString("real_name"));

                    if(dataArray.getJSONObject(i).getString("file_type").equals("video")){
                        videoUrl=dataArray.getJSONObject(i).getString("vid_url");

                        title=dataArray.getJSONObject(i).getString("real_name");
                        thumbpath=Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl)+dataArray.getJSONObject(i).getString("dir_path")+dataArray.getJSONObject(i).getString("img_name");
                        downloadBtn.setVisibility(View.GONE);
                        filename.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(context.getApplicationContext(), StudentVideoTutorialPlay.class);
                                intent.putExtra("title", title);
                                intent.putExtra("titlepath", thumbpath);
                                intent.putExtra("videolink",videoUrl);
                                context.startActivity(intent);
                            }
                        });
                    }else{
                        downloadBtn.setVisibility(View.VISIBLE);
                        urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                        urlStr += dataArray.getJSONObject(i).getString("dir_path")+dataArray.getJSONObject(i).getString("img_name");
                        downloadBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                System.out.println("urlStr="+urlStr);
                                downloadID = Utility.beginDownload(context, urlStr, urlStr);
                                Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                                intent.putExtra("imageUrl",urlStr);
                                context.startActivity(intent);

                            }
                        });
                    }

                    attachmentTable.addView(tr);
                    context.registerForContextMenu(tr);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  view.setTag(viewHolder);
        return view;
    }

    public BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.notification_logo)
                                .setContentTitle(context.getApplicationContext().getString(R.string.app_name))
                                .setContentText("All Download completed");


                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());

            }
        }
    };
}