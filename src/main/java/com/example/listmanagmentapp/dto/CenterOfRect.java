package com.example.listmanagmentapp.dto;

import org.opencv.core.Point;
import org.opencv.core.Rect;

public record CenterOfRect(
        Rect rect,
        Point center
){}
