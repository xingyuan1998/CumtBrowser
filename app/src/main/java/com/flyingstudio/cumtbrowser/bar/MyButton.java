package com.flyingstudio.cumtbrowser.bar;

/**
 * Created by MEzzsy on 2018/3/25.
 */

public class MyButton {
    private String text;
    private int imageId;

    public MyButton(String text, int imageId) {
        this.text = text;
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}