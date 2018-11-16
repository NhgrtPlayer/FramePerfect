package com.corp.detective.database.entities;

import com.corp.detective.database.MainDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

@Table(database = MainDatabase.class)
public class Skill {
    @PrimaryKey(autoincrement = true)
    @Column
    int id;
    @Column String name;
    @Column String description;
    @Column int characterId;
    @Column String imgUrl;

    @Column
    @ForeignKey(tableClass = Character.class)
    Character character;

    public Skill() {

    }

    public Skill(int id, String name, String description, String imgUrl, int characterId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.characterId = characterId;
    }
    public static List<Skill> getAll(){
        return SQLite.select().from(Skill.class).queryList();
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

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) { this.characterId = characterId; }

    public Character getCharacter() { return character; }

    public void setCharacter(Character character) { this.character = character; }

    public static Skill getSkillById(int id){
        return SQLite.select().from(Skill.class).where(Skill_Table.id.eq(id)).querySingle();
    }
}
