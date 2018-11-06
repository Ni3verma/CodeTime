package com.Import.codetime.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "contest")
public class ContestEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String URL;
    private int contestId;
    private String startDate;
    private String endDate;
    private String resourceName;
    private int eventType;  // can be 1 or 2 or 3. constants are defined in DbUtils.java class

    @Ignore
    public ContestEntry(String name, String URL, int contestId, String startDate, String endDate, String resourceName, int eventType) {
        this.name = name;
        this.URL = URL;
        this.contestId = contestId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resourceName = resourceName;
        this.eventType = eventType;
    }

    public ContestEntry(int id, String name, String URL, int contestId, String startDate, String endDate, String resourceName, int eventType) {
        this.id = id;
        this.name = name;
        this.URL = URL;
        this.contestId = contestId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resourceName = resourceName;
        this.eventType = eventType;
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

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}
