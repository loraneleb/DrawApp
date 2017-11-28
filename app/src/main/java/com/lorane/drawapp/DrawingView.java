package com.lorane.drawapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

    private boolean isBrush;
    private Bitmap shapeBitmap;

    private Bitmap background;

    private ArrayList<Object> draws;
    private ArrayList<Object> undoneDraws;

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
        background = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        draws = new ArrayList<>();
        undoneDraws = new ArrayList<>();
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

        /*canvas.drawBitmap(background, 0, 0, null);

        for(int i = 0; i < draws.size(); i++){
            Object o = draws.get(i);
            if(o.getClass() == PathStruct.class){
                PathStruct pathStruct = (PathStruct) o;
                canvas.drawPath(pathStruct.GetPath(), pathStruct.GetPaint());
            }
            else if(o.getClass() == ShapeStruct.class){
                ShapeStruct shapeStruct = (ShapeStruct) o;
                Bitmap shape = shapeStruct.GetShape();
                canvas.drawBitmap(shape, shapeStruct.GetX()-shape.getWidth()/2, shapeStruct.GetY()-shape.getHeight()/2, null);
            }
        }*/

        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                undoneDraws.clear();
                if(isBrush) {
                    drawPath.moveTo(touchX, touchY);
                }
                else {
                    drawCanvas.drawBitmap(shapeBitmap, touchX - shapeBitmap.getWidth() / 2, touchY - shapeBitmap.getHeight() / 2, null);
                    draws.add(new ShapeStruct(shapeBitmap, touchX, touchY));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(isBrush)
                    drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                if(isBrush) {
                    drawCanvas.drawPath(drawPath, drawPaint);
                    Path newPath = new Path(drawPath);
                    Paint newPaint = new Paint(drawPaint);
                    draws.add(new PathStruct(newPath, newPaint));
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
        isBrush = true;
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

    public void eraseDraw(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        draws.clear();
        undoneDraws.clear();
        drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        drawCanvas.drawBitmap(background, 0, 0, null);
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
            case R.id.moon_shape:
                shapeBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.moon70);
                break;
            case R.id.cloud_shape:
                shapeBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
                break;
            case R.id.dog_shape:
                shapeBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.dog70);
                break;
            case R.id.cat_shape:
                shapeBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.cat70);
                break;
            case R.id.fish_shape:
                shapeBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.fish70);
                break;
                case R.id.bubbles_shape:
                shapeBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.bubbles70);
                break;
            case R.id.shell_shape:
                shapeBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.shell70);
                break;

            default:
                shapeBitmap = null;
                break;
        }
    }

    public void onUndo(){
        if (draws.size() > 0){
            undoneDraws.add(draws.remove(draws.size()-1));

            drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

            drawCanvas.drawBitmap(background, 0, 0, null);

            for(int i = 0; i < draws.size(); i++){
                Object o = draws.get(i);
                if(o.getClass() == PathStruct.class){
                    PathStruct pathStruct = (PathStruct) o;
                    drawCanvas.drawPath(pathStruct.GetPath(), pathStruct.GetPaint());
                }
                else if(o.getClass() == ShapeStruct.class){
                    ShapeStruct shapeStruct = (ShapeStruct) o;
                    Bitmap shape = shapeStruct.GetShape();
                    drawCanvas.drawBitmap(shape, shapeStruct.GetX()-shape.getWidth()/2, shapeStruct.GetY()-shape.getHeight()/2, null);
                }
            }

            drawCanvas.drawPath(drawPath, drawPaint);
            invalidate();
        }
    }

    public void onRedo(){
        if (undoneDraws.size() > 0){
            draws.add(undoneDraws.remove(undoneDraws.size()-1));

            drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

            drawCanvas.drawBitmap(background, 0, 0, null);

            for(int i = 0; i < draws.size(); i++){
                Object o = draws.get(i);
                if(o.getClass() == PathStruct.class){
                    PathStruct pathStruct = (PathStruct) o;
                    drawCanvas.drawPath(pathStruct.GetPath(), pathStruct.GetPaint());
                }
                else if(o.getClass() == ShapeStruct.class){
                    ShapeStruct shapeStruct = (ShapeStruct) o;
                    Bitmap shape = shapeStruct.GetShape();
                    drawCanvas.drawBitmap(shape, shapeStruct.GetX()-shape.getWidth()/2, shapeStruct.GetY()-shape.getHeight()/2, null);
                }
            }

            drawCanvas.drawPath(drawPath, drawPaint);
            invalidate();
        }
    }

    public void onNew(int id){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        switch(id){
            case R.id.space:
                background = BitmapFactory.decodeResource(getResources(), R.drawable.espace);
                break;
            case R.id.white:
                background = BitmapFactory.decodeResource(getResources(), R.drawable.white);
                break;
            case R.id.beach:
                background = BitmapFactory.decodeResource(getResources(), R.drawable.beach);
                break;
            case R.id.plain:
                background = BitmapFactory.decodeResource(getResources(), R.drawable.plain);
                break;
            case R.id.sea:
                background = BitmapFactory.decodeResource(getResources(), R.drawable.sea);
                break;
            default:
                break;
        }
        drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        drawCanvas.drawBitmap(background, 0, 0, null);
        draws.clear();
        undoneDraws.clear();
        invalidate();
    }
}
