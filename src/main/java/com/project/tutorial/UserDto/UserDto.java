package com.project.tutorial.UserDto;

import java.util.ArrayList;

public class UserDto {
    private String fullname;

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
}
