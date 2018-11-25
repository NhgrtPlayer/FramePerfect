package com.corp.detective.frameperfect.loaders;

import com.corp.detective.core.DataLoadedListener;
import com.corp.detective.core.DataLoader;
import com.corp.detective.database.entities.Game;
import com.corp.detective.database.entities.Character;
import com.corp.detective.database.entities.Move;
import com.corp.detective.webservice.AirWebServiceCaller;
import com.corp.detective.webservice.AirWebServiceHandler;

import java.util.ArrayList;
import java.util.List;

public class WsDataLoader extends DataLoader {
    private boolean gamesArrived = false;
    private boolean charactersArrived = false;
    private boolean movesArrived = false;

    @Override
    public void loadData(DataLoadedListener dataLoadedListener) {
        super.loadData(dataLoadedListener);

        AirWebServiceCaller gamesWs = new AirWebServiceCaller(gamesHandler);
        AirWebServiceCaller charactersWs = new AirWebServiceCaller(charactersHandler);
        AirWebServiceCaller movesWs = new AirWebServiceCaller(movesHandler);

        gamesWs.getAll("getAll", Game.class);
        charactersWs.getAll("getAll", Character.class);
        movesWs.getAll("getAll", Character.class);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    AirWebServiceHandler gamesHandler = new AirWebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok, long timestamp) {
            if(ok){
                List<Game> games = (List<Game>) result;
                for(Game game : games){
                    game.save();
                }
                gamesArrived = true;
                checkDataArrival();
            }
        }
    };

    AirWebServiceHandler charactersHandler = new AirWebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok, long timestamp) {
            if(ok){
                List<Character> characters = (List<Character>) result;
                for(Character character : characters){
                    character.save();
                }
                charactersArrived = true;
                checkDataArrival();
            }
        }
    };

    AirWebServiceHandler movesHandler = new AirWebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok, long timestamp) {
            if(ok){
                List<Move> moves = (List<Move>) result;
                for(Move move : moves){
                    move.save();
                }
                movesArrived = true;
                checkDataArrival();
            }
        }
    };

    private void checkDataArrival(){
        if(gamesArrived && charactersArrived){
            mDataLoadedListener.onDataLoaded((ArrayList<Game>) Game.getAll(), (ArrayList<Character>) Character.getAll(), (ArrayList<Move>) Move.getAll());
        }
    }
}
