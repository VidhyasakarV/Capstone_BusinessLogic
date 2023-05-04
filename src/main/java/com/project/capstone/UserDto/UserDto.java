package com.project.capstone.UserDto;

import java.util.ArrayList;

public class UserDto {
    private String fullname;
    private String accVisibility;

    private ArrayList<String> List = new ArrayList<>();

    public ArrayList<String> getList() {
        return List;
    }

    public void setList(ArrayList<String> list) {
        List = list;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAccVisibility() {
        return accVisibility;
    }

    public void setAccVisibility(String accVisibility) {
        this.accVisibility = accVisibility;
    }
}
