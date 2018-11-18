package com.Import.codetime.widget;

public class WidgetModel {
    private String contestName;
    private String resName;
    private int contestId;

    public WidgetModel(String contestName, String resName, int contestId) {
        this.contestName = contestName;
        this.resName = resName;
        this.contestId = contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }
}
