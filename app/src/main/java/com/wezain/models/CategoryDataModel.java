package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class CategoryDataModel implements Serializable {
    private List<DepartmentModel> data;

    public List<DepartmentModel> getData() {
        return data;
    }
}
