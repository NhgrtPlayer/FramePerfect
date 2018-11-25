package com.corp.detective.frameperfect.adapters;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import com.corp.detective.database.entities.Game;
import com.corp.detective.database.entities.Character;

import java.util.List;

public class ExpandableGameItem extends Game implements Parent<Character> {

    private List<Character> characters;

    public ExpandableGameItem(Game game){
        super(game.getId(), game.getName(), game.getDescription(), game.getImgUrl());
        this.characters = game.getCharacterList();
    }

    @Override
    public List<Character> getChildList() {
        return characters;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}

