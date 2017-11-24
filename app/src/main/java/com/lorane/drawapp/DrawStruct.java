package com.lorane.drawapp;

/**
 * Created by Lorane on 23/11/2017.
 */

import android.graphics.Paint;
import android.graphics.Path;

public class DrawStruct {

    private Path path;
    private Paint paint;

    public DrawStruct(Path newPath, Paint newPaint){
        path = newPath;
        paint = newPaint;
    }

    public Path GetPath(){
        return path;
    }

    public Paint GetPaint(){
        return paint;
    }
}
