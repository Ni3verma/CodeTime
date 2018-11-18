package com.Import.codetime.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favourite")
public class FavouriteEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int contestId;
    private String name;
    private String resName;

    @Ignore
    public FavouriteEntry(int contestId, String name, String resName) {
        this.contestId = contestId;
        this.name = name;
        this.resName = resName;
    }

    public FavouriteEntry(int id, int contestId, String name, String resName) {

        this.id = id;
        this.contestId = contestId;
        this.name = name;
        this.resName = resName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }
}
