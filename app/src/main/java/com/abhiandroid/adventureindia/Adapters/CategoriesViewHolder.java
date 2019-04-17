package com.abhiandroid.adventureindia.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiandroid.adventureindia.CategoryPlaceList;
import com.abhiandroid.adventureindia.R;
import com.abhiandroid.adventureindia.SplashScreen;


/**
 * Created by ubantu on 3/3/17.
 */
public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView catName;
    CardView cardView;

    public CategoriesViewHolder(final Context context, View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.categoryImage);
        catName = (TextView) itemView.findViewById(R.id.categoryName);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d("data", SplashScreen.categoryListResponseData.get(getAdapterPosition()).getPlace().size() + "");
                    Intent intent = new Intent(context, CategoryPlaceList.class);
                    intent.putExtra("pos", getAdapterPosition());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "No place added in this category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
