package com.corp.detective.frameperfect.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.corp.detective.core.CurrentActivity;
import com.corp.detective.database.entities.Game;
import com.corp.detective.database.entities.Character;
import com.corp.detective.frameperfect.R;
import com.corp.detective.frameperfect.fragments.CharacterDetailsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CharacterViewHolder extends ChildViewHolder {
    @BindView(R.id.character_name)
    TextView mCharacterName;
    @BindView(R.id.character_desc)
    TextView mCharacterDesc;

    Character mCharacter;
    View mCharacterItemView;
    GameRecyclerAdapter mAdapter;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public CharacterViewHolder(@NonNull View itemView, GameRecyclerAdapter adapter) {
        super(itemView);
        mCharacterItemView = itemView;
        mAdapter = adapter;
        ButterKnife.bind(this, itemView);
    }

    public void bind(Character character){
        mCharacter = character;
        mCharacterName.setText(character.getName());
        mCharacterDesc.setText(character.getDescription());
    }

    @OnClick
    public void characterSelected(){
        Bundle args = new Bundle();
        args.putInt("id", mCharacter.getId());

        CharacterDetailsFragment mCharacterDetailsFragment = new CharacterDetailsFragment();
        mCharacterDetailsFragment.setArguments(args);
        FragmentManager mFragmentManager = CurrentActivity.getActivity().getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, mCharacterDetailsFragment);
        mFragmentTransaction.addToBackStack("CharacterDetails");
        mFragmentTransaction.commit();
    }

    @OnLongClick
    public boolean characterLongSelected(){

        // AlertDialog import android.app.AlertDialog
        final AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext()).create();
        final int parentPosition = getParentAdapterPosition();

        alertDialog.setTitle(itemView.getContext().getString(R.string.removal_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, itemView.getContext().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Game parentGame = Game.getGameById(mCharacter.getGameId());
                // delete in database
                mCharacter.delete();
                // delete in list items
                mAdapter.getParentList().get(getParentAdapterPosition()).getChildList().remove(getChildAdapterPosition());
                // redraw list and remove this item
                mAdapter.notifyChildRemoved(getParentAdapterPosition(), getChildAdapterPosition());
                mAdapter.notifyDataSetChanged();

                if(mAdapter.getParentList().get(parentPosition).getChildList().size() == 0){
                    mAdapter.notifyParentRemoved(parentPosition);
                    parentGame.delete();
                    mAdapter.getParentList().remove(parentPosition);
                    mAdapter.notifyDataSetChanged();
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, itemView.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

        return true;
    }
}
