package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.adapter.Filter_Object;
import com.gandaedukasi.gandateacher.adapter.FlowLayout;
import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MapelPengajarActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayList<Filter_Object> mArrFilter;
    private ScrollView mScrollViewFilter;
    private Filter_Adapter mFilter_Adapter ;
    private FlowLayout mFlowLayoutFilter ;
    JsonArray data = new JsonArray();
    ProgressDialog pDialog;
    Session session;
    String pendidikan_pengajar;
    private String zona_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pendidikan_pengajar = getIntent().getStringExtra("pendidikan_pengajar");
        zona_id = getIntent().getStringExtra("zona_id");
        session = new Session(MapelPengajarActivity.this);
        setContentView(R.layout.activity_mapel_pengajar);

        Log.d("pendidikan_pengajar",">"+pendidikan_pengajar);

        mArrFilter = new ArrayList<>();

        pDialog = new ProgressDialog(MapelPengajarActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        String url = new RequestServer().getServer_url()+"mapel";
        Log.d("Pendidikan Url",">"+url);

        JsonObject jsonReq = new JsonObject();
        jsonReq.addProperty("tingkat_pendidikan", pendidikan_pengajar);

        if(isNetworkAvailable()){
            pDialog.show();
            Ion.with(MapelPengajarActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").toString();
                                if (status.equals("1")){
                                    data.addAll(result.getAsJsonArray("data"));
                                    int lengthOfstrArr = data.size();

                                    for (int i = 0; i < lengthOfstrArr; i++) {
                                        JsonObject objData = data.get(i).getAsJsonObject();
                                        Filter_Object filter_object = new Filter_Object();
                                        filter_object.mId = objData.get("id").getAsString();
                                        filter_object.mName = objData.get("nama").getAsString();
                                        filter_object.mIsSelected = false;
                                        mArrFilter.add(filter_object);
                                    }

                                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                                    getSupportActionBar().setTitle(getString(R.string.app_name));

                                    mListView = (ListView) findViewById(R.id.listViewFilter);
                                    mScrollViewFilter = (ScrollView)findViewById(R.id.scrollViewFilter);
                                    mFlowLayoutFilter = (FlowLayout)findViewById(R.id.flowLayout);

                                    mFilter_Adapter = new Filter_Adapter(mArrFilter);
                                    mListView.setAdapter(mFilter_Adapter);
                                }
                            } catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void addFilterTag() {
        final ArrayList<Filter_Object> arrFilterSelected = new ArrayList<>();

        mFlowLayoutFilter.removeAllViews();

        int length = mArrFilter.size();
        boolean isSelected = false;
        for (int i = 0; i < length; i++) {
            Filter_Object fil = mArrFilter.get(i);
            if (fil.mIsSelected) {
                isSelected = true;
                arrFilterSelected.add(fil);
            }
        }
        if (isSelected) {
            mScrollViewFilter.setVisibility(View.VISIBLE);
        } else {
            mScrollViewFilter.setVisibility(View.GONE);
        }
        int size = arrFilterSelected.size();
        LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < size; i++) {
            View view = layoutInflater.inflate(R.layout.filter_tag_edit, null);

            TextView tv = (TextView) view.findViewById(R.id.tvTag);
            LinearLayout linClose = (LinearLayout) view.findViewById(R.id.linClose);
            final Filter_Object filter_object = arrFilterSelected.get(i);
            linClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showToast(filter_object.name);


                    int innerSize = mArrFilter.size();
                    for (int j = 0; j < innerSize; j++) {
                        Filter_Object mFilter_Object = mArrFilter.get(j);
                        if (mFilter_Object.mName.equalsIgnoreCase(filter_object.mName)) {
                            mFilter_Object.mIsSelected = false;

                        }
                    }
                    addFilterTag();
                    mFilter_Adapter.updateListView(mArrFilter);
                }
            });


            tv.setText(filter_object.mName);
            int color = getResources().getColor(R.color.themecolor);

            View newView = view;
            newView.setBackgroundColor(color);

            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 10;
            params.topMargin = 5;
            params.leftMargin = 10;
            params.bottomMargin = 5;

            newView.setLayoutParams(params);

            mFlowLayoutFilter.addView(newView);
        }
    }

    public class Filter_Adapter extends BaseAdapter {
        ArrayList<Filter_Object> arrMenu;

        public Filter_Adapter(ArrayList<Filter_Object> arrOptions) {
            this.arrMenu = arrOptions;
        }

        public void updateListView(ArrayList<Filter_Object> mArray) {
            this.arrMenu = mArray;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.arrMenu.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.filter_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mTtvName = (TextView) convertView.findViewById(R.id.tvName);
                viewHolder.mTvSelected = (TextView) convertView.findViewById(R.id.tvSelected);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Filter_Object mService_Object = arrMenu.get(position);
            viewHolder.mTtvName.setText(mService_Object.mName);

            if (mService_Object.mIsSelected) {
                viewHolder.mTvSelected.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mTvSelected.setVisibility(View.INVISIBLE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mService_Object.mIsSelected = !mService_Object.mIsSelected;
                    mScrollViewFilter.setVisibility(View.VISIBLE);

                    addFilterTag();
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView mTtvName, mTvSelected;

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            ArrayList<Filter_Object> arrMenu = mFilter_Adapter.arrMenu;
            int sizeData = arrMenu.size();
            int countSelected = 0;
            String selected = "";
            for (int i=0; i<sizeData; i++){
                if(arrMenu.get(i).mIsSelected){
                    selected = selected+arrMenu.get(i).mId+";";
                    countSelected++;
                }
            }
            if(countSelected>0){
                pDialog = new ProgressDialog(MapelPengajarActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);

                String url = new RequestServer().getServer_url()+"complatingProfile";
                Log.d("Test Url",">"+url);

                JsonObject jsonReq = new JsonObject();
                jsonReq.addProperty("tingkat_pendidikan", pendidikan_pengajar);
                jsonReq.addProperty("mapel", selected);
                jsonReq.addProperty("zona_id", zona_id);
                jsonReq.addProperty("user_id", session.getUserId());
                Log.d("Request",">"+jsonReq);

                if(isNetworkAvailable()){
                    pDialog.show();
                    Ion.with(MapelPengajarActivity.this)
                            .load(url)
                            //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                            .setJsonObjectBody(jsonReq)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    try{
                                        String status = result.get("status").toString();
                                        if (status.equals("1")){
                                            //TODO buat string value untuk welcome
                                            Toast.makeText(getApplicationContext(), "Selamat datang di Edukezy teacher!", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(MapelPengajarActivity.this, MainActivity.class);
                                            ComponentName cn = i.getComponent();
                                            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                                            startActivity(mainIntent);
                                        }else {
                                            //TODO jika status 0
                                        }
                                    }catch (Exception ex){
                                        Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                    }
                                    pDialog.dismiss();
                                }
                            });
                }else{
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.id_pilih_tingkat_pendidikan_empty), Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
