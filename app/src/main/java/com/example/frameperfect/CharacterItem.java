package com.example.frameperfect;

import com.microsoft.windowsazure.mobileservices.table.DateTimeOffset;

public class CharacterItem {

    @com.google.gson.annotations.SerializedName("id")
    private String mId;
    public String getId() {
        return mId;
    }
    public final void setId(String id) {
        mId = id;
    }

    @com.google.gson.annotations.SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }
    public final void setName(String name) {
        mName = name;
    }

    @com.google.gson.annotations.SerializedName("gameId")
    private String mGameId;
    public String getGameId() {
        return mGameId;
    }
    public final void setGameId(String gameId) {
        mGameId = gameId;
    }

    @com.google.gson.annotations.SerializedName("imgUrl")
    private String mImgUrl;
    public String getImgUrl() {
        return mImgUrl;
    }
    public final void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public CharacterItem() {

    }

    public CharacterItem(String id, String name) {
        this.setId(id);
        this.setName(name);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CharacterItem && ((CharacterItem) o).mId == mId;
    }
}
