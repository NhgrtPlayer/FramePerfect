package com.example.frameperfect;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MoveDetailsActivity extends Activity {
    private MoveItem mMove;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_details);

        mMove = (MoveItem)getIntent().getSerializableExtra("moveItem");

        if (mMove == null) {

            Log.d("MoveDetailsActivity", "Really ? mMove Null");
            return;
        }

        TextView moveNameTextView = (TextView) findViewById(R.id.move_details_name);
        moveNameTextView.setText(mMove.getName());

        TextView moveDamageTextView = (TextView) findViewById(R.id.move_damage);
        moveDamageTextView.setText(mMove.getDamage());
        TextView moveStartUpTextView = (TextView) findViewById(R.id.move_startup);
        moveStartUpTextView.setText(mMove.getStartUp());
        TextView moveActiveTextView = (TextView) findViewById(R.id.move_active);
        moveActiveTextView.setText(mMove.getActive());
        TextView moveRecoveryTextView = (TextView) findViewById(R.id.move_recovery);
        moveRecoveryTextView.setText(mMove.getRecovery());
        TextView moveFrameAdvTextView = (TextView) findViewById(R.id.move_frameadvantage);
        moveFrameAdvTextView.setText(mMove.getFrameAdvantage());
        TextView moveGuardTextView = (TextView) findViewById(R.id.move_guard);
        moveGuardTextView.setText(mMove.getGuard());
        Log.d("MoveDetailsActivity", "END OF ONCREATE, NO ERRORS ???");
    }
}
