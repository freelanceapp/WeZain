package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class CategoryDataModel implements Serializable {
    private List<MainDepartmentModel> data;
    public List<MainDepartmentModel> getData() {
        return data;
    }
}
