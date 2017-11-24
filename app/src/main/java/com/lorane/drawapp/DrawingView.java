package com.lorane.drawapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

import java.util.ArrayList;

/**
 * Created by Lorane on 21/11/2017.
 */

public class DrawingView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private float brushSize, lastBrushSize;

    private boolean erase=false;

    private boolean isBrush;
    private Bitmap shapeBitmap;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        //get drawing area setup for interaction

        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        isBrush = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size

        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isBrush)
                    drawPath.moveTo(touchX, touchY);
                else
                    drawCanvas.drawBitmap(shapeBitmap, touchX-shapeBitmap.getWidth()/2, touchY-shapeBitmap.getHeight()/2, null);
                break;
            case MotionEvent.ACTION_MOVE:
                if(isBrush)
                    drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                if(isBrush) {
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                }
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setColor(String newColor){
        //set color
        invalidate();

        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize){
        //update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
        //set erase true or false
        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    public void eraseDraw(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void shapeOn(){
        isBrush = false;
    }

    public void shapeOff(){
        isBrush = true;
    }

    public void setShape(int id){
        switch (id){
            case R.id.star_shape:
                shapeBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.star70);
                break;
            case R.id.space_ship_shape:
                shapeBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.space_ship70);
                break;
            default:
                shapeBitmap = null;
                break;
        }
    }

    public void onReturn(){
        //TODO
    }

    public void onNew(int id){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        switch(id){
            case R.id.space:
                drawCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.espace), 0, 0, null);
                break;
            case R.id.white:
            default:
                break;
        }
        invalidate();
    }
}
