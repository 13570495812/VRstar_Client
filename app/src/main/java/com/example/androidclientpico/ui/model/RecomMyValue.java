package com.example.androidclientpico.ui.model;

public class RecomMyValue {

    public String name;
    public String PackageName;
    public String image;
    @Override
    public String toString() {
        return "RecommandBodyValue{" +
                "id='" + name + '\'' +
                ", lbl='" + PackageName + '\'' +
                ", code='" + image + '\'' +
                '}';
    }
}
