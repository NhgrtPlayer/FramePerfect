package com.corp.detective.database.entities;

import com.corp.detective.database.MainDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(database = MainDatabase.class)
public class Game extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column int id;
    @Column String name;
    @Column String description;
    @Column String imgUrl;
    List<Character> characterList;

    public Game() {

    }

    public Game(int id, String name, String description, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
    }
    public static List<Game> getAll(){
        return SQLite.select().from(Game.class).queryList();
    }

    public List<Character> getCharacterList(){
        if(characterList == null || characterList.isEmpty()){
            characterList = new Select().from(Character.class)
                    .where(Character_Table.gameId.eq(id))
                    .queryList();
        }
        return characterList;
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

    public static Game getGameById(int id){
        return SQLite.select().from(Game.class).where(Game_Table.id.eq(id)).querySingle();
    }
}