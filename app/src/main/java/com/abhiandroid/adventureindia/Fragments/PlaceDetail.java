package com.abhiandroid.adventureindia.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiandroid.adventureindia.Login;
import com.abhiandroid.adventureindia.MVP.Place;
import com.abhiandroid.adventureindia.MainActivity;
import com.abhiandroid.adventureindia.MapViewActivity;
import com.abhiandroid.adventureindia.Model.SharedPrefManager;
import com.abhiandroid.adventureindia.Model.User;
import com.abhiandroid.adventureindia.OptionalImageFullView;
import com.abhiandroid.adventureindia.R;
import com.abhiandroid.adventureindia.Retrofit.MySingleton;
import com.abhiandroid.adventureindia.WelcomeProfile;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceDetail extends AppCompatActivity {

    @Bind({R.id.bannerImage, R.id.image, R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.shadowIcon})
    List<ImageView> imageViews;
    private ProgressBar progressBar;
    int pos;
    public static int addTime = 1;
    InterstitialAd mInterstitialAd;
    private AdView mAdView;
    public static List<Place> newsListResponsesData;
    @Bind(R.id.optionalImagesCardView)
    CardView optionalImagesCardView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Bind(R.id.favorite)
    FloatingActionButton favorite;
    @Bind({R.id.menu, R.id.back, R.id.share})
    List<ImageView> imageViews1;
    @Bind(R.id.searchView)
    SearchView searchView;
    @Bind(R.id.title)
    TextView title;
    @Bind({R.id.facility, R.id.address, R.id.phoneNo, R.id.website, R.id.description, R.id.placeName, R.id.distance})
    List<TextView> textViews;
    String distance;
    ArrayList<String> imageList = new ArrayList<>();
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("rateUs", 0);
        editor = sharedPreferences.edit();
        imageViews1.get(0).setVisibility(View.INVISIBLE);
        imageViews1.get(2).setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
        imageViews1.get(1).setVisibility(View.VISIBLE);

        // display Ads and rate dialog
        if (addTime % 4 == 0)
            showAd();
        else if (addTime % 7 == 0) {
            addTime = addTime + 1;
            if (sharedPreferences.getString("rate", "No").equalsIgnoreCase("No")) {
                showRateDialog();
            }
        } else
            addTime = addTime + 1;

        // display banner Ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("A9C93B8F0F67284AB70DF28784CA0F1C")
                .build();
        mAdView.loadAd(adRequest);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);

        if (MainActivity.latitude == 0.0) {
            textViews.get(6).setVisibility(View.GONE);
        } else {
            try {
                float distanceInMeters;
                Location loc1 = new Location("");
                loc1.setLatitude(MainActivity.latitude);
                loc1.setLongitude(MainActivity.longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(Double.parseDouble(newsListResponsesData.get(pos).getLatitude()));
                loc2.setLongitude(Double.parseDouble(newsListResponsesData.get(pos).getLongitude()));

                distanceInMeters = loc1.distanceTo(loc2);
                Log.d("distance", distanceInMeters + "");
                distanceInMeters = distanceInMeters / 1000;
                textViews.get(6).setText(new DecimalFormat("##.#").format(distanceInMeters) + " km away");
            } catch (Exception e) {
                textViews.get(6).setText("Not Found");

            }
        }

        if (MainActivity.imageIds.contains(newsListResponsesData.get(pos).getPlaceId().trim())) {
            favorite.setImageResource(R.drawable.favorited_icon);
        } else
            favorite.setImageResource(R.drawable.unfavorite_icon);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.imageIds.contains(newsListResponsesData.get(pos).getPlaceId().trim())) {
                    Log.d("remove", "YES");
                    favorite.setImageResource(R.drawable.unfavorite_icon);
                    MainActivity.imageIds.remove(newsListResponsesData.get(pos).getPlaceId().trim());
                    Toast.makeText(PlaceDetail.this, "Removed From Your Favorite", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("remove", "NO");
                    MainActivity.imageIds.add(newsListResponsesData.get(pos).getPlaceId().trim());
                    favorite.setImageResource(R.drawable.favorited_icon);
                    Toast.makeText(PlaceDetail.this, "Added To Your Favorite", Toast.LENGTH_SHORT).show();
                }
                Log.d("updatedList", MainActivity.imageIds.toString());
                MainActivity.editor.putString("data", MainActivity.imageIds.toString().trim());
                MainActivity.editor.commit();
            }
        });
        imageViews1.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });
        imageViews1.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("");
        setData();
        integrateMap();

        findViewById(R.id.btnBookNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                BookTrek();
            }
        });
    }

    private void integrateMap() {
        mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.framelayout, mapFragment).commit();
        Bundle args = new Bundle();
        args.putString("longitude", newsListResponsesData.get(pos).getLatitude());
        args.putString("latitude", newsListResponsesData.get(pos).getLongitude());
        mapFragment.setArguments(args);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng citylocation = new LatLng(Double.parseDouble(newsListResponsesData.get(pos).getLatitude()), Double.parseDouble(newsListResponsesData.get(pos).getLongitude()));

                googleMap.addMarker(new MarkerOptions().position(citylocation).title("Marker Title").snippet("Marker Description"));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(citylocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }

    @OnClick({R.id.address, R.id.image, R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.viewMap, R.id.navigate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.address:
            case R.id.navigate:
                String geoUri = "http://maps.google.com/maps?q=loc:" + newsListResponsesData.get(pos).getLatitude() + "," + newsListResponsesData.get(pos).getLongitude() + " (" + textViews.get(1).getText().toString() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
                break;
            case R.id.image:
                showFullSize(0);
                break;
            case R.id.image1:
                showFullSize(1);
                break;
            case R.id.image2:
                showFullSize(2);
                break;
            case R.id.image3:
                showFullSize(3);
                break;
            case R.id.image4:
                showFullSize(4);
                break;
            case R.id.viewMap:
                startActivity(new Intent(PlaceDetail.this, MapViewActivity.class).putExtra("lat", newsListResponsesData.get(pos).getLatitude()).putExtra("long", newsListResponsesData.get(pos).getLongitude()));
                break;

        }
    }

    private void showFullSize(int i) {
        OptionalImageFullView.imagesList = imageList;
        OptionalImageFullView.currentPos = i;
        Intent intent = new Intent(PlaceDetail.this, OptionalImageFullView.class);
        startActivity(intent);
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
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void setData() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
                imageViews.get(6).setVisibility(View.GONE);
            }
        });
        builder.build().
                load(newsListResponsesData.get(pos)
                        .getImage())
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.error_image)
                .into(imageViews.get(0));
        textViews.get(0).setText(newsListResponsesData.get(pos).getFacility());
        textViews.get(1).setText(newsListResponsesData.get(pos).getAddress());
        textViews.get(2).setText(newsListResponsesData.get(pos).getPhone());
        textViews.get(3).setText(newsListResponsesData.get(pos).getWebsite());
        textViews.get(4).setText(newsListResponsesData.get(pos).getDescription());
        textViews.get(5).setText(newsListResponsesData.get(pos).getTitle());
        if ((newsListResponsesData.get(pos).getImage1().equalsIgnoreCase("null") || newsListResponsesData.get(pos).getImage1().equalsIgnoreCase("")) &&
                (newsListResponsesData.get(pos).getImage2().equalsIgnoreCase("null") || newsListResponsesData.get(pos).getImage2().equalsIgnoreCase("")) &&
                (newsListResponsesData.get(pos).getImage3().equalsIgnoreCase("null") || newsListResponsesData.get(pos).getImage3().equalsIgnoreCase("")) &&
                (newsListResponsesData.get(pos).getImage4().equalsIgnoreCase("null") || newsListResponsesData.get(pos).getImage4().equalsIgnoreCase("")) &&
                (newsListResponsesData.get(pos).getImage5().equalsIgnoreCase("null") || newsListResponsesData.get(pos).getImage5().equalsIgnoreCase(""))) {
            optionalImagesCardView.setVisibility(View.GONE);
        } else {
            optionalImagesCardView.setVisibility(View.VISIBLE);

            setOptionalImages(imageViews.get(1), newsListResponsesData.get(pos).getImage1());
            setOptionalImages(imageViews.get(2), newsListResponsesData.get(pos).getImage2());
            setOptionalImages(imageViews.get(3), newsListResponsesData.get(pos).getImage3());
            setOptionalImages(imageViews.get(4), newsListResponsesData.get(pos).getImage4());
            setOptionalImages(imageViews.get(5), newsListResponsesData.get(pos).getImage5());
        }

    }

    private void setOptionalImages(ImageView imageView, String s) {
        if (s==null||s.equalsIgnoreCase("null") ||s.equalsIgnoreCase("")|| newsListResponsesData.get(pos).getImage2()==null||newsListResponsesData.get(pos).getImage2().equalsIgnoreCase("null")||newsListResponsesData.get(pos).getImage2().equalsIgnoreCase("")) {
            imageView.setVisibility(View.GONE);

        } else {
            imageList.add(s);
            Picasso.with(PlaceDetail.this)
                    .load(s)
                    .placeholder(R.drawable.defaultimage)
                    .error(R.drawable.defaulterrorimage)
                    .into(imageView);
        }
    }

    public void shareApp() {
        // share app with friends
        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            Intent myIntent = new Intent(this, WelcomeProfile.class);
            startActivity(myIntent);
        }
        else{
            Intent myIntent = new Intent(this, Login.class);

            startActivity(myIntent);
        }


    }

    private void showRateDialog() {
        // Rate your app on Play Store
        new AlertDialog.Builder(PlaceDetail.this)
                .setTitle("Rate Us On Play Store")
                .setMessage("If you like this App. Please rate us on playstore.")
                .setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        editor.putString("rate", "Yes");
                        editor.commit();

                    }
                })
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        editor.putString("rate", "No");
                        editor.commit();

                    }
                }).show();
    }

    private void showAd() {
        addTime = addTime + 1;
        mInterstitialAd = new InterstitialAd(PlaceDetail.this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("A9C93B8F0F67284AB70DF28784CA0F1C")
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void BookTrek() {


        String url = "http://192.168.1.103/app_dashboard/JSON/addbooking.php";

        if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getUsername() == null){
            Toast.makeText(getApplicationContext(), "Please login and try!", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(response);
                String success =  json.get("Success").getAsString();
                if (success.equals("true")){
                    Toast.makeText(getApplicationContext(), json.get("Message").getAsString(), Toast.LENGTH_SHORT).show();
                }
                else if(success.equals("false")){
                    Toast.makeText(getApplicationContext(),  json.get("Message").getAsString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "ErrorResponse",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", SharedPrefManager.getInstance(getApplicationContext()).getUser().getUsername());
                params.put("treklocation", newsListResponsesData.get(pos).getTitle());
                params.put("userid", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId().toString());
                return  params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
