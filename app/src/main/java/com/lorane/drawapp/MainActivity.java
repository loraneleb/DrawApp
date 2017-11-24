package com.lorane.drawapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.Dialog;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, saveBtn, shapeBtn, newBtn, undoBtn, redoBtn;
    private float smallBrush, mediumBrush, largeBrush;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = findViewById(R.id.drawing);
        LinearLayout paintLayout = findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        drawView.setBrushSize(mediumBrush);

        eraseBtn = findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        shapeBtn = findViewById(R.id.shape_btn);
        shapeBtn.setOnClickListener(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                EraseDraw();
            }
        });

        undoBtn = findViewById(R.id.undo_btn);
        undoBtn.setOnClickListener(this);

        redoBtn = findViewById(R.id.redo_btn);
        redoBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public void paintClicked(View view){
        //use chosen color
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public void onClick(View view){
        //respond to clicks
        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    drawView.shapeOff();
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    drawView.shapeOff();
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    drawView.shapeOff();
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    drawView.shapeOff();
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    drawView.shapeOff();
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    drawView.shapeOff();
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            //new draw
            final Dialog newDialog = new Dialog(this);
            newDialog.setTitle("Choose Shape:");
            newDialog.setContentView(R.layout.new_chooser);

            ImageButton whiteBtn = newDialog.findViewById(R.id.white);
            whiteBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.onNew(R.id.white);
                    newDialog.dismiss();
                }
            });

            ImageButton spaceBtn = newDialog.findViewById(R.id.space);
            spaceBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.onNew(R.id.space);
                    newDialog.dismiss();
                }
            });

            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            final Dialog saveDialog = new Dialog(this);
            saveDialog.setTitle("Save drawing:");
            saveDialog.setContentView(R.layout.save_draw);

            ImageButton yesBtn = saveDialog.findViewById(R.id.yesBtn);
            yesBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                    saveDialog.dismiss();
                }
            });

            ImageButton noBtn = saveDialog.findViewById(R.id.noBtn);
            noBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    saveDialog.dismiss();
                }
            });

            saveDialog.show();
        }
        else if(view.getId()==R.id.shape_btn){
            //draw shape
            final Dialog shapeDialog = new Dialog(this);
            shapeDialog.setTitle("Choose Shape:");
            shapeDialog.setContentView(R.layout.shape_chooser);

            ImageButton starBtn = shapeDialog.findViewById(R.id.star_shape);
            starBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.shapeOn();
                    drawView.setShape(R.id.star_shape);
                    shapeDialog.dismiss();
                }
            });

            ImageButton spaceShipBtn = shapeDialog.findViewById(R.id.space_ship_shape);
            spaceShipBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.shapeOn();
                    drawView.setShape(R.id.space_ship_shape);
                    shapeDialog.dismiss();
                }
            });

            shapeDialog.show();
        }
        else if(view.getId()==R.id.undo_btn){
            drawView.onUndo();
        }
        else if(view.getId()==R.id.redo_btn){
            drawView.onRedo();
        }
    }

    public void EraseDraw(){
        final Dialog eraseDialog = new Dialog(this);
        eraseDialog.setTitle("New drawing:");
        eraseDialog.setContentView(R.layout.erase_draw);

        ImageButton yesBtn = eraseDialog.findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.eraseDraw();
                drawView.shapeOff();
                eraseDialog.dismiss();
            }
        });

        ImageButton noBtn = eraseDialog.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                eraseDialog.dismiss();
            }
        });

        eraseDialog.show();
    }
}
