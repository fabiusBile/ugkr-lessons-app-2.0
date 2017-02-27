package com.ugkr.lessons.LinksLists;

/**
 * Created by fabiusbile on 12.02.17.
 */

public class NameCodePair {
    public String name;
    public  String code;
    public boolean favourite;
    public boolean isGroup;
    public NameCodePair(String name, String code, boolean favourite, boolean isGroup) {
        this.name = name;
        this.code = code;
        this.favourite = favourite;
        this.isGroup = isGroup;
    }

    public NameCodePair() {
    }

    public  String toString(){
        return name;
    }
}
