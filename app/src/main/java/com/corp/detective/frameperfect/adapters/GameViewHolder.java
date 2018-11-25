package com.corp.detective.frameperfect.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.corp.detective.database.entities.Game;
import com.corp.detective.frameperfect.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameViewHolder extends ParentViewHolder {

    @BindView(R.id.game_name)
    TextView mGameName;
    @BindView(R.id.game_desc)
    TextView mGameDesc;
    @BindView(R.id.game_image)
    ImageView mGameImage;

    View mGameItemView;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public GameViewHolder(@NonNull View itemView) {
        super(itemView);
        mGameItemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void bind(Game game){
        mGameName.setText(game.getName());
        mGameDesc.setText(game.getDescription());
        Picasso.with(mGameItemView.getContext())
                .load(game.getImgUrl())
                .into(mGameImage);
    }
}

