package com.abhiandroid.adventureindia.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.abhiandroid.adventureindia.MVP.Place;
import com.abhiandroid.adventureindia.R;


/**
 * Created by ubantu on 3/3/17.
 */
public class AllPlaceViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView placeName, distance;
    CardView cardView;

    public AllPlaceViewHolder(final Context context, View itemView, List<Place> newsListResponse) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.placeImage);
        placeName = (TextView) itemView.findViewById(R.id.placeName);
        distance = (TextView) itemView.findViewById(R.id.distance);
        cardView = (CardView) itemView.findViewById(R.id.cardView);

    }
}
