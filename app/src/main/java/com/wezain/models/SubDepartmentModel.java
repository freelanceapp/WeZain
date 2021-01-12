package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class SubDepartmentModel implements Serializable {
    private int id;
    private String title;
    private String icon;
    private String basic_department_id;
    private List<SubSubDepartmentModel> sub_sub_departments;

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

    public List<SubSubDepartmentModel> getSub_sub_departments() {
        return sub_sub_departments;
    }
}
