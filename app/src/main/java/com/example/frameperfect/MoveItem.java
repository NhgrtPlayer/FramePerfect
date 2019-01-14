package com.example.frameperfect;

import com.microsoft.windowsazure.mobileservices.table.DateTimeOffset;

public class MoveItem {

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private int mId;
    public int getId() {
        return mId;
    }
    public final void setId(int id) {
        mId = id;
    }

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("name")
    private String mCommand;
    public String getCommand() {
        return mCommand;
    }
    public final void setCommand(String command) {
        mCommand = command;
    }

    @com.google.gson.annotations.SerializedName("characterID")
    private int mCharacterId;
    public int getCharacterId() {
        return mCharacterId;
    }
    public final void setCharacterId(int characterId) {
        mCharacterId = characterId;
    }

    @com.google.gson.annotations.SerializedName("moveType")
    private String mMoveType;
    public String getMoveType() {
        return mMoveType;
    }
    public final void setMoveType(String moveType) {
        mMoveType = moveType;
    }

    @com.google.gson.annotations.SerializedName("damage")
    private String mDamage;
    public String getDamage() {
        return mDamage;
    }
    public final void setDamage(String damage) {
        mDamage = damage;
    }

    @com.google.gson.annotations.SerializedName("startUp")
    private String mStartUp;
    public String getStartUp() {
        return mStartUp;
    }
    public final void setStartUp(String startUp) {
        mStartUp = startUp;
    }

    @com.google.gson.annotations.SerializedName("active")
    private String mActive;
    public String getActive() {
        return mActive;
    }
    public final void setActive(String active) {
        mActive = active;
    }

    @com.google.gson.annotations.SerializedName("recovery")
    private String mRecovery;
    public String getRecovery() {
        return mRecovery;
    }
    public final void setRecovery(String recovery) {
        mRecovery = recovery;
    }

    @com.google.gson.annotations.SerializedName("frameAdvantage")
    private String mFrameAdvantage;
    public String getFrameAdvantage() {
        return mFrameAdvantage;
    }
    public final void setFrameAdvantage(String frameAdvantage) {
        mFrameAdvantage = frameAdvantage;
    }

    @com.google.gson.annotations.SerializedName("guard")
    private String mGuard;
    public String getGuard() {
        return mGuard;
    }
    public final void setGuard(String guard) {
        mGuard = guard;
    }

    /**
     * CharacterItem constructor
     */
    public MoveItem() {

    }

    public MoveItem(int id, String name) {
        this.setId(id);
        this.setCommand(name);
    }

    @Override
    public String toString() {
        return getCommand();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MoveItem && ((MoveItem) o).mId == mId;
    }
}
