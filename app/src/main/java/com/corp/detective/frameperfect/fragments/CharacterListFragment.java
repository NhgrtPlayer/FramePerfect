package com.corp.detective.frameperfect.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corp.detective.core.DataLoadedListener;
import com.corp.detective.core.DataLoader;
import com.corp.detective.database.entities.Character;
import com.corp.detective.database.entities.Game;
import com.corp.detective.database.entities.Move;
import com.corp.detective.frameperfect.R;
import com.corp.detective.frameperfect.adapters.ExpandableGameItem;
import com.corp.detective.frameperfect.adapters.GameRecyclerAdapter;
import com.corp.detective.frameperfect.loaders.DbDataLoader;
import com.corp.detective.frameperfect.loaders.WsDataLoader;

import java.util.ArrayList;
import java.util.List;

public class CharacterListFragment extends Fragment implements DataLoadedListener {
    GameRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
    }

    public void loadData(){
        DataLoader dataLoader;
        if(Game.getAll().isEmpty()){
            System.out.println("Loading web data");
            dataLoader = new WsDataLoader();
        } else {
            System.out.println("Loading local data");
            dataLoader = new DbDataLoader();
        }
        dataLoader.loadData(this);
    }

    @Override
    public void onDataLoaded(ArrayList<Game> games, ArrayList<Character> characters, ArrayList<Move> Moves) {
        List<ExpandableGameItem> gameItems = new ArrayList<>();

        if(games != null){

            for(Game game : games)
                gameItems.add(new ExpandableGameItem(game));

            RecyclerView mRecycler = (RecyclerView) getActivity().findViewById(R.id.main_recycler);
            if(mRecycler != null){
                mAdapter = new GameRecyclerAdapter(getActivity(), gameItems);
                mRecycler.setAdapter(mAdapter);
                mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }

    }
}