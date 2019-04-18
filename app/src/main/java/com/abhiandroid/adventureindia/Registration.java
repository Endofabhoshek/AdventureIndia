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

import com.abhiandroid.adventureindia.MVP.RegisterUserResponse;
import com.abhiandroid.adventureindia.Model.SharedPrefManager;
import com.abhiandroid.adventureindia.Model.User;
import com.abhiandroid.adventureindia.Retrofit.Api;
import com.abhiandroid.adventureindia.Retrofit2.ServiceImplementation;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


public class Registration extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword;
    RadioGroup radioGroupGender;
    public String response_active ="";
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
        progressBar.setVisibility(View.VISIBLE);

        String url = "http://192.168.1.105/app_dashboard/JSON/register.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, url, null, new com.android.volley.Response<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        textView.setText("Response: " + response.toString());
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });



//        Call<RegisterUserResponse> registerResponse = ServiceImplementation.getClient().sendRegistrationDetails(username, email, password, gender);
//
//        try {
//            retrofit2.Response<RegisterUserResponse> response = registerResponse.execute();
//            if (response.body().getSuccess() == "false"){
//                progressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(getApplicationContext(),response_active.replace("<false>",""),Toast.LENGTH_SHORT).show();
//            }
//            else  if(response.body().getSuccess() == "true"){
//                progressBar.setVisibility(View.INVISIBLE);
//                User user = new User("a","aa","mal",1);
//                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                Toast.makeText(getApplicationContext(),response_active.replace("<true>",""),Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), Login.class));
//            }
//        }
//        catch (Exception ex){
//            progressBar.setVisibility(View.INVISIBLE);
//            Toast.makeText(getApplicationContext(),"Invalid Exception",Toast.LENGTH_SHORT).show();
//        }

//        Api.getClient().sendRegistrationDetails(username, email, password, gender, new Callback<RegisterUserResponse>() {
//            @Override
//            public void success(RegisterUserResponse registerUserResponse, Response response) {
//                if (registerUserResponse.getSuccess() == "false"){
//                    progressBar.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getApplicationContext(),response_active.replace("<false>",""),Toast.LENGTH_SHORT).show();
//                }
//                else if (registerUserResponse.getSuccess() == "true"){
//
//                    progressBar.setVisibility(View.INVISIBLE);
//                    User user = new User("a","aa","mal",1);
//                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                    Toast.makeText(getApplicationContext(),response_active.replace("<true>",""),Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), Login.class));
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                progressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(getApplicationContext(),response_active.replace("<true>",""),Toast.LENGTH_SHORT).show();
//            }
//        });

//        class RegisterUser extends AsyncTask<Void, Void, String> {
//
//            public String response_active ="";
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                //creating request handler object
//
//                Api.getClient().sendRegistrationDetails(username, email, password, gender, new Callback<RegisterUserResponse>() {
//                    @Override
//                    public void success(RegisterUserResponse registerUserResponse, Response response) {
//                        if (registerUserResponse.getSuccess() == "false"){
//                            response_active = registerUserResponse.getSuccess()+"<false>";
//                        }
//                        else if (registerUserResponse.getSuccess() == "true"){
//
//                            response_active = registerUserResponse.getSuccess()+"<true>";
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        response_active = "Invalid Exception";
//                    }
//                });
//
//                return response_active;
//            }
//
//            @Override
//            protected void onPreExecute(){
//                super.onPreExecute();
//                //displaying the progress bar while user registers on the server
//                progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            protected void onPostExecute(String s){
//                User user = new User("a","aa","mal",1);
//                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                finish();
//                if (response_active.contains("<true>")){
//                    Toast.makeText(getApplicationContext(),response_active.replace("<true>",""),Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), Login.class));
//                }
//                else if(response_active.contains("<false>")){
//                    Toast.makeText(getApplicationContext(),response_active.replace("<false>",""),Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//        }
//        RegisterUser ru = new RegisterUser();
//        ru.execute();
    }

    public void AddUser(String username,String email,String password,String gender){


    }

    public void LoginPage(View view){

    }
}
