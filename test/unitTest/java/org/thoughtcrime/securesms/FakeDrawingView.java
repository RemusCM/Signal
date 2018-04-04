package org.thoughtcrime.securesms;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;


/**
 * Created by daanish on 4/4/2018.
 */

@RunWith(PowerMockRunner.class)
public class FakeDrawingView implements DrawingViewInterface{

  private CustomPath drawPath;
  private Bitmap canvasBitmap;
  private Paint drawPaint;
  private Paint canvasPaint;
  private int paintColor = 000000;
  private Canvas drawCanvas;

  private float brushSize;
  private float prevBrushSize; // when switching, store previous

  private boolean erase = false;
  public boolean smoothStrokes = false;

  private float startX;
  private float startY;
  private float endX;
  private float endY;

  private ArrayList<CustomPath> paths = new ArrayList<CustomPath>();


  public void setUpDrawing(){
    drawPaint = new Paint();;
    drawPath = new CustomPath(paintColor, brushSize);
    erase = false;
    smoothStrokes = false;
    drawPaint.setColor(paintColor);
    drawPaint.setAntiAlias(true);
    drawPaint.setStyle(Paint.Style.STROKE);
    drawPaint.setStrokeJoin(Paint.Join.ROUND);
    drawPaint.setStrokeCap(Paint.Cap.ROUND);

    canvasPaint = new Paint(Paint.DITHER_FLAG);

    brushSize = 20;
    prevBrushSize = brushSize;
  }

  public void setColor(String newColor){
    System.out.println("setcolor");
    //invalidate();
    System.out.println(newColor + " !!!!!!!!!!!!!!!!!!!!!!!!!!");
    paintColor = Color.parseColor(newColor);
    drawPaint.setColor(paintColor);

  }

  public void setSizeForBrush(float newSize){
    DisplayMetrics displayMetrics = mock(DisplayMetrics.class);
    float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, displayMetrics);
    brushSize = pixelAmount;
    drawPaint.setStrokeWidth(brushSize);
  }

  public void setErase(boolean bErase){
    System.out.println("setErase");
    erase = bErase;
    if(erase)
    {
      paintColor = Color.parseColor("#FFFFFF");
      drawPaint.setColor(paintColor);
    }
    else
      drawPaint.setXfermode(null);
  }

  @Test
  public void testSetupDrawing(){

    setUpDrawing();
    Paint drawPaint = mock(Paint.class);

    verify(drawPaint).setColor(paintColor);
    //verify(drawPaint).setAntiAlias(true);
    //verify(drawPaint).setStyle(Paint.Style.STROKE);
    //verify(drawPaint).setStrokeJoin(Paint.Join.ROUND);
    //verify(drawPaint).setStrokeCap(Paint.Cap.ROUND);
  }

  @Test
  public void testSetColor(){

  }

  @Test
  public void testSetSizeForBrush(){

  }

  @Test
  public void testSetErase(){

  }
}
