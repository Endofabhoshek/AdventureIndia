package com.abhiandroid.adventureindia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.abhiandroid.adventureindia.MVP.RegisterUserResponse;
import com.abhiandroid.adventureindia.Model.SharedPrefManager;

import com.abhiandroid.adventureindia.Fragments.PlaceDetail;
import com.abhiandroid.adventureindia.Model.User;
import com.abhiandroid.adventureindia.Retrofit.Api;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.abhiandroid.adventureindia.Fragments.Categories;
import com.abhiandroid.adventureindia.Fragments.Home;
import com.abhiandroid.adventureindia.Fragments.AllPlaces;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends FragmentActivity implements LocationListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    EditText editTextUsername, editTextEmail, editTextPassword;
    RadioGroup radioGroupGender;

    public static DrawerLayout drawerLayout;
    public static List<String> menuTitles;
    public static ArrayList<Integer> menuIcons = new ArrayList<>(Arrays.asList(R.drawable.home_icon, R.drawable.star_icon, R.drawable.contact_icon, R.drawable.rate_icon, R.drawable.about_icon, R.drawable.more_icon));
    public static CustomDrawerAdapter customDrawerAdapter;
    private AdView mAdView;
    public static ImageView menu, share, search;
    public static TextView title;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static SearchView searchView;
    public static ArrayList<String> imageIds = new ArrayList<>();
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    boolean isGPSEnabled, isNetworkEnabled;
    LocationManager locationManager;
    Location location;
    public static double latitude, longitude;
    private PrefManager prefManager;
    public static boolean firstTime = true;
    public static String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prefManager = new PrefManager(this);
        getFavoriteData(); // get saved favorite list data
        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            userName = SharedPrefManager.getInstance(this).getUser().getUsername();
        }


        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        menuTitles = Arrays.asList(getResources().getStringArray(R.array.menuArray));
        title = (TextView) findViewById(R.id.title);
        menu = (ImageView) findViewById(R.id.menu);
        share = (ImageView) findViewById(R.id.share);
        search = (ImageView) findViewById(R.id.search);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        searchView = (SearchView) findViewById(R.id.searchView);
        // customized searchView
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(id);
        searchEditText.setTextColor(getResources().getColor(R.color.color_white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.light_white));
        // display Banner Ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("A9C93B8F0F67284AB70DF28784CA0F1C")
                .build();
        mAdView.loadAd(adRequest);
        // load home fragment
        loadFragment(new Home(), false);
        setRecyclerData(); // set drawer items
        // implement onQueryTextListener on searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // filter news list
                String text = s;
                AllPlaces.allPlacesAdapter.filter(text);
                Categories.categoriesAdapter.filter(text);
                return false;
            }
        });
        displayFirebaseRegId(); // display firebase id
        if (SplashScreen.id.length() > 0) {
            Intent intent = new Intent(MainActivity.this, PlaceDetail.class);
            intent.putExtra("pos", 0);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

//        if (SharedPrefManager.getInstance(this).isLoggedIn()){
//            finish();
//            startActivity(new Intent(this, Login.class));
//        }

//        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
//        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
//        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
//        radioGroupGender = (RadioGroup) findViewById(R.id.radioGender);


//        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //if user pressed on button register
//                //here we will register the user to server
//                registerUser();
//            }
//           });
//
//        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //if user pressed on login
//                //we will open the login screen
//                finish();
//                startActivity(new Intent(MainActivity.this, Login.class));
//            }
//        });



//        ButterKnife.bind(this);
//        prefManager = new PrefManager(this);
//        getFavoriteData(); // get saved favorite list data
//
//
//        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        menuTitles = Arrays.asList(getResources().getStringArray(R.array.menuArray));
//        title = (TextView) findViewById(R.id.title);
//        menu = (ImageView) findViewById(R.id.menu);
//        share = (ImageView) findViewById(R.id.share);
//        search = (ImageView) findViewById(R.id.search);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        searchView = (SearchView) findViewById(R.id.searchView);
//        // customized searchView
//        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//        EditText searchEditText = (EditText) searchView.findViewById(id);
//        searchEditText.setTextColor(getResources().getColor(R.color.color_white));
//        searchEditText.setHintTextColor(getResources().getColor(R.color.light_white));
//        // display Banner Ads
//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("A9C93B8F0F67284AB70DF28784CA0F1C")
//                .build();
//        mAdView.loadAd(adRequest);
//        // load home fragment
//        loadFragment(new Home(), false);
//        setRecyclerData(); // set drawer items
//        // implement onQueryTextListener on searchView
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                // filter news list
//                String text = s;
//                AllPlaces.allPlacesAdapter.filter(text);
//                Categories.categoriesAdapter.filter(text);
//                return false;
//            }
//        });
//        displayFirebaseRegId(); // display firebase id
//        if (SplashScreen.id.length() > 0) {
//            Intent intent = new Intent(MainActivity.this, PlaceDetail.class);
//            intent.putExtra("pos", 0);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }

    }

    private void getFavoriteData() {
        sharedPreferences = getSharedPreferences("favoriteData", 0);
        editor = sharedPreferences.edit();
        Log.d("favoriteData", sharedPreferences.getString("data", "0"));
        String data = sharedPreferences.getString("data", "0");
        data = data.replace("[", "");
        data = data.replace("]", "");
        String[] array = data.split(", ");
        imageIds = new ArrayList<>(Arrays.asList(array));
        Log.d("arrayList", imageIds.toString().trim());
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("FCM", "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId)) {
        } else
            Log.d("Firebase", "Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void setRecyclerData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        customDrawerAdapter = new CustomDrawerAdapter(MainActivity.this, menuTitles, menuIcons);
        recyclerView.setAdapter(customDrawerAdapter);
    }


    @OnClick({R.id.menuHomeImage, R.id.menu, R.id.share, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuHomeImage:
                drawerLayout.closeDrawers();
                CustomDrawerAdapter.selected_item = 0;
                customDrawerAdapter.notifyDataSetChanged();
                title.setText(menuTitles.get(0));
                loadFragment(new Home(), false);
                break;
            case R.id.menu:
                if (!MainActivity.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    MainActivity.drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.share:
                shareApp();
                break;

        }
    }


    public void shareApp() {

         if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
             Intent myIntent = new Intent(this, WelcomeProfile.class);
             myIntent.putExtra("userName", userName);
             startActivity(myIntent);
         }
         else{
             Intent myIntent = new Intent(this, Login.class);
             myIntent.putExtra("userName", userName);
             startActivity(myIntent);
         }


    }

    @Override
    public void onBackPressed() {
        // double press to exit
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart", "onStartCalled");
        checkConnection();
    }

    public void updateLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            Log.d("GPS", "Enabled");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                return;
            }
            getLocation();

        } else {
            if (firstTime) {
                GPSManager gps = new GPSManager(MainActivity.this);
                gps.start();
                firstTime = false;
            }
        }


    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        // if GPS Enabled get lat/long using GPS Services
        if (isGPSEnabled) {
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                }
            }
        }
        if (location != null) {
            onLocationChanged(location);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                prefManager.setFirstTimeLaunch(false);
                getLocation();
            }
        }
    }


    private void checkConnection() {
        if (isNetworkAvailable())
            updateLocation();

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("LocationCoordinates", latitude + "   " + longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}