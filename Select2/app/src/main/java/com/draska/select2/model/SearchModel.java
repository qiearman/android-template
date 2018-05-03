package com.draska.select2.model;

/**
 * Created by TT17010247 on 2018-05-03.
 */

public class SearchModel {
    String text;
    String value;
    Object tag;

    public SearchModel() {}

    public SearchModel(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public SearchModel(String text, String value, Object tag) {
        this.text = text;
        this.value = value;
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
