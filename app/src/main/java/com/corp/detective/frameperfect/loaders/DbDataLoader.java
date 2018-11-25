package com.corp.detective.frameperfect.loaders;

import com.corp.detective.core.DataLoadedListener;
import com.corp.detective.core.DataLoader;
import com.corp.detective.database.entities.Character;
import com.corp.detective.database.entities.Game;
import com.corp.detective.database.entities.Move;

import java.util.ArrayList;

public class DbDataLoader extends DataLoader {
    @Override
    public void loadData(DataLoadedListener dataLoadedListener) {
        super.loadData(dataLoadedListener);
        try{
            games = (ArrayList<Game>) Game.getAll();
            characters = (ArrayList<Character>) Character.getAll();
            moves = (ArrayList<Move>) Move.getAll();

            mDataLoadedListener.onDataLoaded(games, characters, moves);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
