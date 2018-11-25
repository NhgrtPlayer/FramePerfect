package com.corp.detective.core;

import com.corp.detective.database.entities.Game;
import com.corp.detective.database.entities.Character;
import com.corp.detective.database.entities.Move;

import java.util.ArrayList;

public interface DataLoadedListener {
    void onDataLoaded(ArrayList<Game> games, ArrayList<Character> characters, ArrayList<Move> Moves);
}
