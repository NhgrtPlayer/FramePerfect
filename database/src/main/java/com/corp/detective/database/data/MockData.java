package com.corp.detective.database.data;

import com.corp.detective.database.entities.Character;
import com.corp.detective.database.entities.Game;

public class MockData {
    public static void writeAll(){
        Game dbfGame = new Game();
        dbfGame.setName("DragonBall Fighter Z");
        dbfGame.save(); // <-- available from BaseModel super class

        Character vegito = new Character();
        vegito.setName("Vegito");
        vegito.setDescription("Fusion between goku and vegeta");
        vegito.setGame(dbfGame);
        vegito.save();

        Character broly = new Character();
        broly.setName("Broly");
        broly.setDescription("First saiyan legendaire");
        broly.setGame(dbfGame);
        broly.save();
    }
}
