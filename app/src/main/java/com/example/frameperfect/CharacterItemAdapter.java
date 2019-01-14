package com.example.frameperfect;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        Log.d("viewCharactrItemAdapter", "ROW CREATED");

        return row;
    }

}
