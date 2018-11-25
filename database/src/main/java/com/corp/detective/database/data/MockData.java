package com.corp.detective.database.data;

import android.os.Debug;
import android.util.Log;

import com.corp.detective.database.entities.Character;
import com.corp.detective.database.entities.Game;

public class MockData {
    public static void writeAll(){
        Game dbfGame = new Game();
        dbfGame.setId(1);
        dbfGame.setName("DragonBall Fighter Z");
        dbfGame.save(); // <-- available from BaseModel super class

        Character vegito = new Character();
        vegito.setId(1);
        vegito.setName("Vegito");
        vegito.setDescription("Fusion between goku and vegeta");
        vegito.setGame(dbfGame);
        vegito.save();

        Character broly = new Character();
        broly.setId(2);
        broly.setName("Broly");
        broly.setDescription("First saiyan legendaire");
        broly.setGame(dbfGame);
        broly.save();

        System.out.println("DATA MOCKED");
    }
}
