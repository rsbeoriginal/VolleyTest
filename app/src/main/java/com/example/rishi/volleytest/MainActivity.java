package com.example.rishi.volleytest;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.rishi.volleytest.VolleySingleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String json_tag="json_tag";
    String json_url="http://jsonplaceholder.typicode.com/users/";
    String post_url="http://aspajax.azurewebsites.net/json.php";
    TextView txt_response;
    EditText txt_request,txt_name,txt_age;
    Button bt_request,bt_post,bt_get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_response= (TextView) findViewById(R.id.txt_response);
        txt_request= (EditText) findViewById(R.id.txt_request);
        bt_request= (Button) findViewById(R.id.bt_request);
        txt_name= (EditText) findViewById(R.id.txt_name);
        txt_age= (EditText) findViewById(R.id.txt_age);
        bt_post= (Button) findViewById(R.id.bt_post);
        bt_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=txt_request.getText().toString();
            if (id.isEmpty()){
                json_request();
            }else {
                json_request(id);
            }
            }
        });

        bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name=txt_name.getText().toString(),
                        age=txt_age.getText().toString();
                JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.POST, post_url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(MainActivity.this, ""+response.toString(), Toast.LENGTH_LONG).show();
                        try{
                            String method=response.getJSONObject(0).getString("method");
                            String name=response.getJSONObject(0).getString("name");
                            String age=response.getJSONObject(0).getString("age");
                            txt_response.setText("method: "+method+"\nName:"+name+"\nAge: "+age);
                        }catch (Exception e){
                            txt_response.setText(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Err:"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String, String>();
                        params.put("name",name);
                        params.put("age",age);
                        return params;
                    }
                };


                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, post_url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                        try{
                            String method=response.getString("method");
                            String name=response.getString("name");
                            String age=response.getString("age");
                            txt_response.setText("method: "+method+"\nName:"+name+"\nAge: "+age);
                        }catch (Exception e){
                            txt_response.setText(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Err: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String, String>();
                        params.put("name","Rishi");
                        params.put("age","18");
                        return params;
                    }
                };

                StringRequest stringRequest=new StringRequest(Request.Method.POST, post_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    txt_response.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Err: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String, String>();
                        params.put("name","Rishi");
                        params.put("age","12");
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(jsonObjectRequest);
            }
        });

    }

    private void json_request(String id) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading details . . .");
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, json_url+id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            String id=response.get("id").toString();
                            String name=response.get("name").toString();
                            String email=response.getString("email");
                            txt_response.setText("id: "+id+"\nName: "+name+"\nEmail-id: "+email);
                        }catch (Exception e){
                            String err=e.getMessage();
                            txt_response.setText(err);
                        }finally {
                            progressDialog.hide();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    txt_response.setText(error.getMessage());
                }catch (Exception e){
                    txt_response.setText(e.getMessage());
                }
                progressDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void json_request(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading details . . .");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, json_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int n=response.length();
                        txt_response.setText("");
                        for (int i=0;i<n;i++) {
                            try{
                                String id=response.getJSONObject(i).get("id").toString();
                                String name=response.getJSONObject(i).get("name").toString();
                                String email=response.getJSONObject(i).getString("email");
                                txt_response.setText(txt_response.getText()+"\nid: "+id+"\nName: "+name+"\nEmail-id: "+email);
                            }catch (Exception e){
                                txt_response.setText(e.getMessage());
                            }finally {
                                progressDialog.hide();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txt_response.setText(error.getMessage());
                progressDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }


}
