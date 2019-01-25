package com.example.frameperfect;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

class GameItemAdapter extends ArrayAdapter<GameItem> {

    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public GameItemAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final GameItem currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final TextView gameNameTextView = (TextView) row.findViewById(R.id.game_name);
        gameNameTextView.setText(currentItem.getName());

        ImageView image = (ImageView) row.findViewById(R.id.game_image);

        if (currentItem.getImgUrl().matches(".*[a-zA-Z].*")) {
            Picasso.with(this.getContext())
                    .load(currentItem.getImgUrl())
                    .centerCrop()
                    .fit()
                    .into(image);
        }
        else {
            Picasso.with(this.getContext())
                    .load(android.R.drawable.ic_menu_gallery)
                    .centerCrop()
                    .fit()
                    .into(image);
        }

        Log.d("getView GameItemAdapter", "ROW CREATED");

        return row;
    }

}
