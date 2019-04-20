package com.abhiandroid.adventureindia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiandroid.adventureindia.Model.SharedPrefManager;

public class WelcomeProfile extends AppCompatActivity {

    TextView loggeduser ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_profile);

        loggeduser = (TextView) findViewById(R.id.textLoggedUser);
        loggeduser.setText("Welcome " +  SharedPrefManager.getInstance(getApplicationContext()).getUser().getUsername());

    }

    public void logOutUser(View view) {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
        Toast.makeText(getApplicationContext(), "You have been logged out!", Toast.LENGTH_SHORT).show();
    }
}
