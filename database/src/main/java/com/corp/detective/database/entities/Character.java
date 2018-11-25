package com.corp.detective.database.entities;

import com.corp.detective.database.MainDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(database = MainDatabase.class)
public class Character extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column int id;
    @Column String name;
    @Column String description;
    @Column int gameId;
    @Column String imgUrl;
    List<Move> MoveList;

    @Column
    @ForeignKey(tableClass = Game.class)
    Game game;

    public Character() {

    }

    public Character(int id, String name, String description, String imgUrl, int gameId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.gameId = gameId;
    }
    public static List<Character> getAll(){
        return SQLite.select().from(Character.class).queryList();
    }

    public List<Move> getMoveList(){
        if(MoveList == null || MoveList.isEmpty()){
            MoveList = new Select().from(Move.class)
                    .where(Move_Table.characterId.eq(id))
                    .queryList();
        }
        return MoveList;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) { this.gameId = gameId; }

    public Game getGame() { return game; }

    public void setGame(Game game) { this.game = game; }

    public static Character getCharacterById(int id){
        return SQLite.select().from(Character.class).where(Character_Table.id.eq(id)).querySingle();
    }
}
