package com.abhiandroid.adventureindia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhiandroid.adventureindia.Fragments.ContactUs;
import com.abhiandroid.adventureindia.Fragments.Favorite;
import com.abhiandroid.adventureindia.Fragments.Home;
import com.abhiandroid.adventureindia.Fragments.About;

/**
 * Created by AbhiAndroid
 */
public class DrawerViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView icon;

    public DrawerViewHolder(final Context context, View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        icon = (ImageView) itemView.findViewById(R.id.titleIcon);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDrawerAdapter.selected_item = getPosition();
                switch (getPosition()) {
                    case 0:
                        ((MainActivity) context).loadFragment(new Home(), false);
                        break;
                    case 1:
                        ((MainActivity) context).loadFragment(new Favorite(), false);
                        break;
                    case 2:
//                        ((MainActivity) context).loadFragment(new ContactUs(), false);

                        break;
                    case 3:
                        // perform click on Rate Item
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }
                        break;

                    case 4:
                        ((MainActivity) context).loadFragment(new About(), false);
                        break;
                    case 5:
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Abhi+Android")));
                        break;
                }
                if (getAdapterPosition()!=3&&getAdapterPosition()!=5) {
                    MainActivity.title.setText(MainActivity.menuTitles.get(getPosition()));
                    MainActivity.customDrawerAdapter.notifyDataSetChanged();
                    MainActivity.drawerLayout.closeDrawers();
                }

            }
        });
    }

}
