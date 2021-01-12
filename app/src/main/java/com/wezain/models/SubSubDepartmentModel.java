package com.wezain.models;

import java.io.Serializable;

public class SubSubDepartmentModel implements Serializable {
    private int id;
    private String title;
    private String icon;
    private String parent;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getParent() {
        return parent;
    }
}
