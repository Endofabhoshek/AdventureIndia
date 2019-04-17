package com.abhiandroid.adventureindia.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.abhiandroid.adventureindia.MainActivity;
import com.abhiandroid.adventureindia.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class About extends Fragment {

    View view;
    @Bind(R.id.emailId)
    TextView emailId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        MainActivity.share.setVisibility(View.INVISIBLE);
        MainActivity.searchView.setVisibility(View.INVISIBLE);
        return view;
    }

    @OnClick({R.id.emailLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emailLayout:
                // perform click on Email ID
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                final PackageManager pm = getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                String className = null;
                for (final ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                        className = info.activityInfo.name;

                        if(className != null && !className.isEmpty()){
                            break;
                        }
                    }
                }
                emailIntent.setData(Uri.parse("mailto:"+emailId.getText().toString().trim()));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback Of City Guide App");
                emailIntent.setClassName("com.google.android.gm", className);
                try {
                    startActivity(emailIntent);
                } catch(ActivityNotFoundException ex) {
                    // handle error
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.share.setVisibility(View.VISIBLE);
        MainActivity.searchView.setVisibility(View.VISIBLE);
    }
}
