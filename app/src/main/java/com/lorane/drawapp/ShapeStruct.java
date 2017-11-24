package com.lorane.drawapp;

import android.graphics.Bitmap;

/**
 * Created by Lorane on 23/11/2017.
 */

public class ShapeStruct {

    private Bitmap shape;
    private float touchX;
    private float touchY;

    public ShapeStruct(Bitmap newShape, float newX, float newY){
        shape = newShape;
        touchX = newX;
        touchY = newY;
    }

    public Bitmap GetShape(){
        return shape;
    }

    public float GetX(){
        return touchX;
    }

    public float GetY(){
        return touchY;
    }
}
