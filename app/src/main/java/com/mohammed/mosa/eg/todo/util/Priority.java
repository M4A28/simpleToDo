package com.mohammed.mosa.eg.todo.util;

import android.graphics.drawable.ColorDrawable;

public class Priority {
    ColorDrawable colorDrawable;
    String text;

    public Priority(ColorDrawable colorDrawable, String text) {
        this.colorDrawable = colorDrawable;
        this.text = text;
    }

    public ColorDrawable getColorDrawable() {
        return colorDrawable;
    }

    public void setColorDrawable(ColorDrawable colorDrawable) {
        this.colorDrawable = colorDrawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
