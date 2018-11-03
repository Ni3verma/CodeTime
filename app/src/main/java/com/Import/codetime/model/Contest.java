
package com.Import.codetime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contest {

    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("resource")
    @Expose
    private Resource resource;
    @SerializedName("start")
    @Expose
    private String start;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

}
