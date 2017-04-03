package com.androidbelieve.tubesrpl.setter_getter;

/**
 * Created by pandu on 25/03/17.
 */

public class isiMateri {
    private String title;
    private String description;

    public isiMateri(String description, String title) {
        this.description = description;
        this.title = title;
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
