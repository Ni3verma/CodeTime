
package com.Import.codetime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("objects")
    @Expose
    private List<Contest> contests = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Contest> getContests() {
        return contests;
    }

    public void setContests(List<Contest> objects) {
        this.contests = objects;
    }

}
