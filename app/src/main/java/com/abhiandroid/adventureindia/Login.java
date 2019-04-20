package com.abhiandroid.adventureindia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.abhiandroid.adventureindia.Fragments.Home;
import com.abhiandroid.adventureindia.Model.SharedPrefManager;
import com.abhiandroid.adventureindia.Model.User;
import com.abhiandroid.adventureindia.Retrofit.MySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

public class Login extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                loginUser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void loginUser(){
        editTextUsername = (EditText) findViewById(R.id.editLoginTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editLoginTextPassword);

        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE); // yaha par visible kia...

        String url = "http://192.168.1.103/app_dashboard/JSON/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(response);
                String success =  json.get("Success").getAsString();
                if (success.equals("true")){
                    Toast.makeText(getApplicationContext(), json.get("Message").getAsString(), Toast.LENGTH_SHORT).show();
                    User user = new User(json.get("username").getAsString(),json.get("email").getAsString(),json.get("gender").getAsString(), json.get("id").getAsInt());
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else if(success.equals("false") ){
                    Toast.makeText(getApplicationContext(),  json.get("Message").getAsString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "ErrorResponse",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return  params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void Register(View view){
        finish();
        startActivity(new Intent(Login.this, Registration.class));
    }
}

