package com.chrono.speedtimeapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Track implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private long id;
    @SerializedName("iniLat")
    private double iniLat;
    @SerializedName("iniLong")
    private double iniLong;
    @SerializedName("p1Lat")
    private double p1Lat;
    @SerializedName("p1Long")
    private double p1Long;
    @SerializedName("p2Lat")
    private double p2Lat;
    @SerializedName("p2Long")
    private double p2Long;
    @SerializedName("name")
    private String name;

    public Track() {
        super();
    }

    public Track(long id, double iniLat, double iniLong, double p1Lat, double p1Long, double p2Lat, double p2Long, String name) {
        this.id = id;
        this.iniLat = iniLat;
        this.iniLong = iniLong;
        this.p1Lat = p1Lat;
        this.p1Long = p1Long;
        this.p2Lat = p2Lat;
        this.p2Long = p2Long;
        this.name = name;
    }

    public Track(Long id, String name, double iniLat, double iniLong, double p1Lat, double p1Long, double p2Lat,
                 double p2Long) {
        super();
        this.id = id;
        this.name = name;
        this.iniLat = iniLat;
        this.iniLong = iniLong;
        this.p1Lat = p1Lat;
        this.p1Long = p1Long;
        this.p2Lat = p2Lat;
        this.p2Long = p2Long;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getIniLat() {
        return iniLat;
    }

    public void setIniLat(double iniLat) {
        this.iniLat = iniLat;
    }

    public double getIniLong() {
        return iniLong;
    }

    public void setIniLong(double iniLong) {
        this.iniLong = iniLong;
    }

    public double getP1Lat() {
        return p1Lat;
    }

    public void setP1Lat(double p1Lat) {
        this.p1Lat = p1Lat;
    }

    public double getP1Long() {
        return p1Long;
    }

    public void setP1Long(double p1Long) {
        this.p1Long = p1Long;
    }

    public double getP2Lat() {
        return p2Lat;
    }

    public void setP2Lat(double p2Lat) {
        this.p2Lat = p2Lat;
    }

    public double getP2Long() {
        return p2Long;
    }

    public void setP2Long(double p2Long) {
        this.p2Long = p2Long;
    }

}
