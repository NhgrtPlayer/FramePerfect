package com.corp.detective.core;

import com.corp.detective.database.entities.Character;
import com.corp.detective.database.entities.Game;
import com.corp.detective.database.entities.Move;

import java.util.ArrayList;

public class DataLoader {
    public ArrayList<Game> games;
    public ArrayList<Character> characters;
    public ArrayList<Move> moves;

    protected DataLoadedListener mDataLoadedListener;

    public void loadData(DataLoadedListener dataLoadedListener){
        this.mDataLoadedListener = dataLoadedListener;
    }

    public boolean dataLoaded(){
        if(games == null || characters == null){
            return false;
        }
        else{
            return true;
        }
    }
}
