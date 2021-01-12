package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class MainDepartmentModel implements Serializable {
   private int id;
   private String title;
   private String icon;
   private List<SubDepartmentModel> sub_departments;
   private boolean isSelected = false;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public List<SubDepartmentModel> getSub_departments() {
        return sub_departments;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
