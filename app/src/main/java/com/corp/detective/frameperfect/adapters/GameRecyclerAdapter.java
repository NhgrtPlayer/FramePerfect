package com.corp.detective.frameperfect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.corp.detective.database.entities.Character;
import com.corp.detective.frameperfect.R;

import java.util.List;

public class GameRecyclerAdapter extends ExpandableRecyclerAdapter<ExpandableGameItem, Character, GameViewHolder, CharacterViewHolder> {
    private LayoutInflater mInflater;

    /**
     * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
     * <p>
     * Any changes to {@link #mParentList} should be made on the original instance, and notified via
     * {@link #notifyParentInserted(int)}
     * {@link #notifyParentRemoved(int)}
     * {@link #notifyParentChanged(int)}
     * {@link #notifyParentRangeInserted(int, int)}
     * {@link #notifyChildInserted(int, int)}
     * {@link #notifyChildRemoved(int, int)}
     * {@link #notifyChildChanged(int, int)}
     * methods and not the notify methods of RecyclerView.Adapter.
     *
     * @param parentList List of all parents to be displayed in the RecyclerView that this
     *                   adapter is linked to
     */
    public GameRecyclerAdapter(Context context, @NonNull List<ExpandableGameItem> parentList) {
        super(parentList);
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public GameViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View gameView = mInflater.inflate(R.layout.game_list_item, parentViewGroup, false);
        return new GameViewHolder(gameView);
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View characterView = mInflater.inflate(R.layout.character_list_item, childViewGroup, false);
        return new CharacterViewHolder(characterView, this);
    }

    @Override
    public void onBindParentViewHolder(@NonNull GameViewHolder parentViewHolder, int parentPosition, @NonNull ExpandableGameItem parent) {
        parentViewHolder.bind((ExpandableGameItem) parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull CharacterViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull Character child) {
        childViewHolder.bind(child);
    }
}
