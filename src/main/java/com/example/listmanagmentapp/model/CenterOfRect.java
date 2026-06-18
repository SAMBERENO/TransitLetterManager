package com.example.listmanagmentapp.model;

import org.opencv.core.Point;
import org.opencv.core.Rect;

public class CenterOfRect {

    private final Rect rect;

    public CenterOfRect(Rect rect) {
        this.rect = rect;
    }

    public Point center() {
        int centerX = rect.x + (rect.width / 2);
        int centerY = rect.y + (rect.height / 2);
        return new Point(centerX, centerY);
    }

}
