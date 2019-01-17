package com.example.frameperfect;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

class CharacterItemAdapter extends ArrayAdapter<CharacterItem> {

    Context mContext;
    int mLayoutResourceId;

    public CharacterItemAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final CharacterItem currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final TextView characterNameTextView = (TextView) row.findViewById(R.id.character_name);
        characterNameTextView.setText(currentItem.getName());

        ImageView image = (ImageView) row.findViewById(R.id.character_image);

        Log.d("viewCharactrItemAdapter", "CHARACTER NAME : " + currentItem.getName());
        Log.d("viewCharactrItemAdapter", "IMAGE URL : " + currentItem.getImgUrl());
        Log.d("viewCharactrItemAdapter", "IMAGE EMPTY ? : " + TextUtils.isEmpty(currentItem.getImgUrl()));
        Log.d("viewCharactrItemAdapter", "IMAGE EMPTY ? : " + currentItem.getImgUrl().matches(".*[a-zA-Z].*"));
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

        Log.d("viewCharactrItemAdapter", "ROW CREATED");

        return row;
    }

}
