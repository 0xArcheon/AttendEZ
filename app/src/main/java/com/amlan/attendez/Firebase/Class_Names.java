package com.amlan.attendez.Firebase;

public class Class_Names {
    String ownerId;
    String crId;
    String name_class;
    String name_subject;
    String position_bg;

    public Class_Names() {

    }

    public Class_Names(String ownerId, String crId, String name_class, String name_subject, String position_bg) {
        this.ownerId = ownerId;
        this.crId = crId;
        this.name_class = name_class;
        this.name_subject = name_subject;
        this.position_bg = position_bg;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerid) {
        this.ownerId = ownerid;
    }

    public String getCrId() {
        return crId;
    }

    public void setCrId(String crId) {
        this.crId = crId;
    }


    public String getName_class() {
        return name_class;
    }

    public void setName_class(String name_class) {
        this.name_class = name_class;
    }

    public String getName_subject() {
        return name_subject;
    }

    public void setName_subject(String name_subject) {
        this.name_subject = name_subject;
    }

    public String getPosition_bg() {
        return position_bg;
    }

    public void setPosition_bg(String position_bg) {
        this.position_bg = position_bg;
    }
}
