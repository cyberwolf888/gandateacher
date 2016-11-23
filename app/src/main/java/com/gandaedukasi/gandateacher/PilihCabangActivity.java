package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;

public class PilihCabangActivity extends AppCompatActivity {

    ListView listView;
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> itemList;
    JsonArray mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_cabang);
        listView = (ListView) findViewById(R.id.cabang_listview);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        getCabang();

    }

    private void getCabang(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(PilihCabangActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            String url = new RequestServer().getServer_url()+"cabang";
            Log.d("Login Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", true);
            Log.d("Req Data",">"+jsonReq);

            Ion.with(PilihCabangActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            Log.d("Response",">"+result);
                            try{
                                String status = result.get("status").toString();
                                Log.d("status",">"+status);
                                if (status.equals("1")){
                                    mData = result.getAsJsonArray("data");
                                    ArrayList<HashMap<String, String>> xitemList = new ArrayList<HashMap<String, String>>();
                                    for(int i=0; i<mData.size(); i++){
                                        JsonObject objData = mData.get(i).getAsJsonObject();
                                        HashMap<String, String> dataList = new HashMap<String, String>();
                                        dataList.put("nama",objData.get("nama").getAsString());
                                        dataList.put("alamat",objData.get("alamat").getAsString());
                                        xitemList.add(dataList);
                                    }
                                    itemList = xitemList;
                                    ListAdapter adapter = new SimpleAdapter(
                                            PilihCabangActivity.this,
                                            itemList,
                                            R.layout.list_item_cabang,
                                            new String[]{"nama","alamat"},
                                            new int[]{R.id.nama_cabang,R.id.alamat_cabang}
                                    );
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            JsonObject objSelected = mData.get(position).getAsJsonObject();
                                            Intent i = new Intent(PilihCabangActivity.this, TingkatPendidikanActivity.class);
                                            i.putExtra("zona_id",objSelected.get("id").getAsString());
                                            startActivity(i);
                                            Log.d("Selected :","> " + objSelected);
                                        }
                                    });
                                }
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else {
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_next) {

        }
        return super.onOptionsItemSelected(item);
    }


}
