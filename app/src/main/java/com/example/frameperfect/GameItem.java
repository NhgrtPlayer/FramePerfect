package com.example.frameperfect;

import com.microsoft.windowsazure.mobileservices.table.DateTimeOffset;

public class GameItem {

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;
    public String getId() {
        return mId;
    }
    public final void setId(String id) {
        mId = id;
    }

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }
    public final void setName(String name) {
        mName = name;
    }

    /**
     * GameItem constructor
     */
    public GameItem() {

    }

    public GameItem(String id, String name) {
        this.setId(id);
        this.setName(name);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GameItem && ((GameItem) o).mId == mId;
    }
}
