package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class CategoryDataModel implements Serializable {
    private List<DepartmentModel> data;
    private List<DepartmentModel> sub_departments;
    private int id;
    private String title;
    private String icon;
    public List<DepartmentModel> getData() {
        return data;
    }

    public List<DepartmentModel> getSub_departments() {
        return sub_departments;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }
}
