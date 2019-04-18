package com.abhiandroid.adventureindia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.abhiandroid.adventureindia.Fragments.Home;

import butterknife.OnClick;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }



    public void Register(View view){
        finish();
        startActivity(new Intent(Login.this, Registration.class));
    }
}

