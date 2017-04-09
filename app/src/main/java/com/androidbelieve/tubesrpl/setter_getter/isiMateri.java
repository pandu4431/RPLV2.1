package com.androidbelieve.tubesrpl.setter_getter;

/**
 * Created by pandu on 25/03/17.
 */

public class isiMateri {
    private String title;
    private String description;
    private String idmateri;

    public isiMateri(String title, String description, String idmateri) {
        this.description = description;
        this.title = title;
        this.idmateri=idmateri;
    }

    public String getIdmateri() {
        return idmateri;
    }

    public void setIdmateri(String idmateri) {
        this.idmateri = idmateri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
