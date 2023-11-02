package com.chrono.speedtimeapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Time implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    private long id;
    @SerializedName("idTrack")
    private long idTrack;
    @SerializedName("idUser")
    private long idUser;
    @SerializedName("time")
    private long time;
    @SerializedName("p1time")
    private long p1time;
    @SerializedName("p2time")
    private long p2time;
    @SerializedName("kmh")
    private String kmh;
    @SerializedName("date")
    private String date;

    public Time() {
        super();
    }

    public Time(Long id, Long idUser, Long idTrack, Long time, Long p1time, Long p2time, String kmh, String date) {
        super();
        this.id = id;
        this.idUser = idUser;
        this.idTrack = idTrack;
        this.time = time;
        this.p1time = p1time;
        this.p2time = p2time;
        this.kmh = kmh;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdTrack() {
        return idTrack;
    }

    public void setIdTrack(Long idTrack) {
        this.idTrack = idTrack;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getP1time() {
        return p1time;
    }

    public void setP1time(Long p1time) {
        this.p1time = p1time;
    }

    public Long getP2time() {
        return p2time;
    }

    public void setP2time(Long p2time) {
        this.p2time = p2time;
    }

    public String getKmh() {
        return kmh;
    }

    public void setKmh(String kmh) {
        this.kmh = kmh;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
