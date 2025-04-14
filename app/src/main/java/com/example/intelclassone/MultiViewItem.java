package com.example.intelclassone;

public class MultiViewItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_AD = 2;

    public int type;
    public String text;

    public MultiViewItem(int type, String text) {
        this.type = type;
        this.text = text;
    }
}

