package com.example.frameperfect;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MoveItemAdapter extends ArrayAdapter<MoveItem> {

    Context mContext;
    int mLayoutResourceId;

    public MoveItemAdapter(Context context, int layoutResourceId) {
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

        final MoveItem currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);

        final TextView moveNameTextView = (TextView) row.findViewById(R.id.move_name);
        final TextView moveDamageTextView = (TextView) row.findViewById(R.id.move_damage);
        final TextView moveStartUpTextView = (TextView) row.findViewById(R.id.move_startup);
        final TextView moveActiveTextView = (TextView) row.findViewById(R.id.move_active);
        final TextView moveRecoveryTextView = (TextView) row.findViewById(R.id.move_recovery);
        final TextView moveFrameAdvTextView = (TextView) row.findViewById(R.id.move_frameadvantage);
        final TextView moveGuardTextView = (TextView) row.findViewById(R.id.move_guard);
        moveNameTextView.setText(currentItem.getCommand());
        moveDamageTextView.setText(currentItem.getDamage());
        moveStartUpTextView.setText(currentItem.getStartUp());
        moveActiveTextView.setText(currentItem.getActive());
        moveRecoveryTextView.setText(currentItem.getRecovery());
        moveFrameAdvTextView.setText(currentItem.getFrameAdvantage());
        moveGuardTextView.setText(currentItem.getGuard());

        Log.d("getView MoveItemAdapter", "TABLE ROW CREATED");

        return row;
    }

}
