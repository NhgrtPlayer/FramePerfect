package com.example.frameperfect;

public class AccountItem {

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

    @com.google.gson.annotations.SerializedName("password")
    private String mPassword;
    public String getPassword() {
        return mPassword;
    }
    public final void setPassword(String password) {
        mPassword = password;
    }

    public AccountItem() {

    }

    public AccountItem(String id, String name) {
        this.setId(id);
        this.setName(name);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AccountItem && ((AccountItem) o).mId == mId;
    }
}
