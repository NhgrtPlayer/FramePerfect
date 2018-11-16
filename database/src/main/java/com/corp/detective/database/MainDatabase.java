package com.corp.detective.database;

import android.content.Context;

import com.corp.detective.database.data.MockData;
import com.corp.detective.database.entities.Character;
import com.corp.detective.database.entities.Game;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

@Database(name = MainDatabase.NAME, version = MainDatabase.VERSION)
public class MainDatabase {
    public static final String NAME = "main";
    public static final int VERSION = 1;

    public static void initDatabase(Context context)
    {
        FlowManager.init(new FlowConfig.Builder(context).build());
    }

    public static String[] getCharacters()
    {
        if(SQLite.select().from(Game.class).queryList().isEmpty()){
            MockData.writeAll();
        }

        List<Character> characters =
                SQLite.select().from(Character.class).queryList();

        //convert to array of strings
        String[] listItems = new String[characters.size()];
        for(int i = 0; i < characters.size(); i++){
            listItems[i] = characters.get(i).getName();
        }

        return listItems;
    }

    public static String[] getGames()
    {
        if(SQLite.select().from(Game.class).queryList().isEmpty()){
            MockData.writeAll();
        }

        List<Game> games =
                SQLite.select().from(Game.class).queryList();

        //convert to array of strings
        String[] listItems = new String[games.size()];
        for(int i = 0; i < games.size(); i++){
            listItems[i] = games.get(i).getName();
        }

        return listItems;
    }
}
