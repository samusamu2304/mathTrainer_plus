package com.boala.mathtrainer;

import com.google.firebase.firestore.IgnoreExtraProperties;
@IgnoreExtraProperties
public class UsersPoints {
    private String name;
    private double points;
    private boolean ranked;

    public UsersPoints() {
    }

    public UsersPoints(String name, double points, boolean ranked) {
        this.name = name;
        this.points = points;
        this.ranked = ranked;
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

    public boolean isRanked() {
        return ranked;
    }

    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }
}
