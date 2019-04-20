package com.abhiandroid.adventureindia;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.abhiandroid.adventureindia.Retrofit.MySingleton;

import com.abhiandroid.adventureindia.MVP.RegisterUserResponse;
import com.abhiandroid.adventureindia.Model.SharedPrefManager;
import com.abhiandroid.adventureindia.Model.User;
import com.abhiandroid.adventureindia.Retrofit.Api;
import com.abhiandroid.adventureindia.Retrofit2.ServiceImplementation;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Map;


public class Registration extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword;
    RadioGroup radioGroupGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
           });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private ProgressBar progressBar;

//    public void RegisterUser(View view){
//        registerUser();
//    }

    public void registerUser(){
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        radioGroupGender = (RadioGroup) findViewById(R.id.radioGroupGender);

        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        final String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE); // yaha par visible kia...

        String url = "http://192.168.1.103/app_dashboard/JSON/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(response);
                String success =  json.get("Success").getAsString();
                if (success.equals("true")){
                    Toast.makeText(getApplicationContext(), json.get("Message").getAsString(), Toast.LENGTH_SHORT).show();
                    User user = new User(username,email,gender, 1);
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
                else if(success.equals("false")){
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
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);
                return  params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void AddUser(String username,String email,String password,String gender){


    }

    public void LoginPage(View view){

    }
}
