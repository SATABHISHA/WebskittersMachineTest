package org.example.demo.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.example.demo.Adapter.HomeExpandableListAdapter;
import org.example.demo.Model.KeyData;
import org.example.demo.Model.ObjectTitle;
import org.example.demo.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    ArrayList<ObjectTitle> objectTitleArrayList = new ArrayList<>();
    ArrayList<KeyData> keyDataArrayList = new ArrayList<>();
    HashMap<ObjectTitle, ArrayList<KeyData>> listDataChild = new HashMap<ObjectTitle, ArrayList<KeyData>>();
    ExpandableListView explistviewData;
    TextView tv_data_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        explistviewData = (ExpandableListView) findViewById(R.id.lvExp);
        tv_data_status = findViewById(R.id.tv_data_status);

        loadDataDetails();
    }


    public void loadDataDetails() {

        String url = "http://planningpro.dedicateddevelopers.us/api/role/planning/0";
        Log.d("listurl-=>", url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(HomeActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getResponseData(response);

                        loading.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error.printStackTrace();
            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + "739|c0DxqQa9nURCa2OSMMgxAdyEMdLre0JCscuXpTYz");
                return headerMap;
            }
        };
        stringRequest.setRetryPolicy(new

            DefaultRetryPolicy(10000,
                               DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        }

        public void getResponseData(String response){

           try {
               JSONObject jsonObject = new JSONObject(response);
               Log.d("response-=>",response);

               if(!objectTitleArrayList.isEmpty()){
                   objectTitleArrayList.clear();
               } if(!keyDataArrayList.isEmpty()){
                   keyDataArrayList.clear();
               }
               if(!listDataChild.isEmpty()){
                   listDataChild.clear();
               }
               JSONArray jsonArray = jsonObject.getJSONArray("data");
               JSONObject jsonObject1 = jsonArray.getJSONObject(0);
               JSONObject jsonObject2 = jsonObject1.getJSONObject("planning");
               JSONArray jsonArray1 = jsonObject2.getJSONArray("objective");



               ObjectTitle objectTitle;
               for(int i=0; i<jsonArray1.length(); i++){
                   keyDataArrayList = new ArrayList<>();
                   objectTitle =  new ObjectTitle();
                   JSONObject jsonObject3 = jsonArray1.getJSONObject(i);

                   objectTitle.setContent_obj(jsonObject3.getString("content_obj"));
                   objectTitleArrayList.add(objectTitle);

                   JSONArray jsonArray2 = jsonObject3.getJSONArray("key_result");

//                   keyDataArrayList = new ArrayList<>();
                   KeyData keyData;
                   for(int j=0; j<jsonArray2.length(); j++){
                       keyData = new KeyData();
                       JSONObject jsonObject4 = jsonArray2.getJSONObject(j);
                       keyData.setKey_result(jsonObject4.getString("key_result"));

                       keyDataArrayList.add(keyData);

                   }

                   listDataChild.put(objectTitleArrayList.get(i), keyDataArrayList);
               }

               if(!objectTitleArrayList.isEmpty()) {
                   explistviewData.setVisibility(View.VISIBLE);
                   tv_data_status.setVisibility(View.GONE);
                   HomeExpandableListAdapter expandableListAdapter;
                   expandableListAdapter = new HomeExpandableListAdapter(HomeActivity.this, objectTitleArrayList, listDataChild);
                   explistviewData.setAdapter(expandableListAdapter);
                   //-----code to open child list starts----------
                   for (int k = 0; k < explistviewData.getExpandableListAdapter().getGroupCount(); k++) {
                       //Expand group
                       explistviewData.expandGroup(k);

                   }
                   //-----code to open child list ends----------

                   //----------code to open/close child element starts---------
                   explistviewData.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                       @Override
                       public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                           if (expandableListView.isGroupExpanded(i)) {
                               expandableListView.collapseGroup(i);
                           } else {
                               boolean animateExpansion = false;
                               expandableListView.expandGroup(i, animateExpansion);
                           }
                           //telling the listView we have handled the group click, and don't want the default actions.
                           return true;
                       }
                   });
                   //----------code to open/close child element ends---------
               }else{
                   explistviewData.setVisibility(View.GONE);
                   tv_data_status.setVisibility(View.VISIBLE);
               }

           }catch (JSONException e){
               e.printStackTrace();
           }


        }

    }
