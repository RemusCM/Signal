package org.thoughtcrime.securesms;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.Button;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by daanish on 4/4/2018.
 */

@RunWith(PowerMockRunner.class)
public class DrawingCreator{
  private DrawingView drawView;
  private Button drawBtn;
  private Button eraseBtn, saveBtn;

  private CustomPath drawPath;
  private Bitmap canvasBitmap;
  private Paint drawPaint;
  private Paint canvasPaint;
  private int paintColor = 000000;
  private Canvas drawCanvas;

  private float brushSize;
  private float prevBrushSize;

  private boolean erase = false;
  public boolean smoothStrokes = false;

  private float startX;
  private float startY;
  private float endX;
  private float endY;

  public void setUp() {
    drawView = mock(DrawingView.class);
    drawBtn = mock(Button.class);
    eraseBtn = mock(Button.class);
    saveBtn = mock(Button.class);

  }

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

}
