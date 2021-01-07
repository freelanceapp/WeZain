package com.wezain.models;

import java.io.Serializable;

public class DepartmentModel implements Serializable {
    private int id;
    private String title;
    private String icon;
    private String basic_department_id;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getBasic_department_id() {
        return basic_department_id;
    }
}
