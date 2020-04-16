package com.boala.mathtrainer;

import com.google.firebase.firestore.IgnoreExtraProperties;
@IgnoreExtraProperties
public class UsersPoints {
    private String name;
    private double points;

    public UsersPoints() {
    }

    public UsersPoints(String name, double points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public double getPoints() {
        return points;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
