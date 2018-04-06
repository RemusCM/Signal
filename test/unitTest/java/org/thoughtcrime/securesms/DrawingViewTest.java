package org.thoughtcrime.securesms;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class DrawingViewTest {

  public class FakeDrawingView {

    FakeDrawingView() {
    }

    Paint drawPaint       = mock(Paint.class);
    CustomPath drawPath   = mock(CustomPath.class);
    Resources resources   = mock(Resources.class);
    TypedValue typedValue = mock(TypedValue.class);
    int paintColor        = Color.BLACK; // default paint color
    float brushSize;
    boolean erase;
    boolean smoothStrokes;
    float prevBrushSize;

    Paint getDrawPaint() {
      return drawPaint;
    }

    void setUpDrawing() {
      erase         = false;
      smoothStrokes = false;
      brushSize     = 20;
      prevBrushSize = brushSize;

      drawPaint.setColor(paintColor);
      drawPaint.setAntiAlias(true);
      drawPaint.setStyle(Paint.Style.STROKE);
      drawPaint.setStrokeJoin(Paint.Join.ROUND);
      drawPaint.setStrokeCap(Paint.Cap.ROUND);

      when(getDrawPaint().getColor()).thenReturn(paintColor);
      when(getDrawPaint().getStyle()).thenReturn(Paint.Style.STROKE);
      when(getDrawPaint().getStrokeJoin()).thenReturn(Paint.Join.ROUND);
      when(getDrawPaint().getStrokeCap()).thenReturn(Paint.Cap.ROUND);
    }

    void setColor(String newColor) {
      paintColor = Color.parseColor(newColor);
      drawPaint.setColor(paintColor);
      when(drawPaint.getColor()).thenReturn(paintColor);
    }

    void setSizeForBrush(float newSize) {
      brushSize = newSize;
      drawPaint.setStrokeWidth(brushSize);
      when(drawPaint.getStrokeWidth()).thenReturn(brushSize);
    }

    void setErase(boolean bErase) {
      // when user clicks eraser button
      // asserts that the color for erasing is always white
      // otherwise set it to null
      erase = bErase;
      if (erase) {
        paintColor = Color.WHITE;
        drawPaint.setColor(paintColor);
        when(drawPaint.getColor()).thenReturn(paintColor);
      } else {
        when(drawPaint.getXfermode()).thenReturn(null);
        drawPaint.setXfermode(null);
      }
    }
  }


  @Test
  public void testSetUpDrawing() {
    FakeDrawingView fdv = new FakeDrawingView();
    fdv.setUpDrawing();
    Paint paint = fdv.getDrawPaint();

    System.out.println("\n- Testing setUpDrawing : paint.getColor() -");
    System.out.println("    Expected color: " + Color.BLACK);
    System.out.println("    Actual: " + paint.getColor());
    assertEquals(Color.BLACK, paint.getColor());

    System.out.println("\n- Testing setUpDrawing : paint.getStyle() -");
    System.out.println("    Expected style: " + Paint.Style.STROKE);
    System.out.println("    Actual: " + paint.getStyle());
    assertEquals(Paint.Style.STROKE, paint.getStyle());

    System.out.println("\n- Testing setUpDrawing : paint.getStrokeJoin() -");
    System.out.println("    Expected stroke join: " + Paint.Join.ROUND);
    System.out.println("    Actual: " + paint.getStrokeJoin());
    assertEquals(Paint.Join.ROUND, paint.getStrokeJoin());

    System.out.println("\n- Testing setUpDrawing : paint.getStrokeCap() -");
    System.out.println("    Expected color: " + Paint.Cap.ROUND);
    System.out.println("    Actual: " + paint.getStrokeCap());
    assertEquals(Paint.Cap.ROUND, paint.getStrokeCap());
  }

  @Test
  public void testSetColor() {
    FakeDrawingView fakeDrawingView = new FakeDrawingView();
    // set the new color for painting
    fakeDrawingView.setColor("cyan");
    Paint paint = fakeDrawingView.getDrawPaint();
    assertEquals(Color.parseColor("cyan"), paint.getColor());
  }

  @Test
  public void testSetSizeForBrush() {
    FakeDrawingView fakeDrawingView = new FakeDrawingView();
    // set the new size for brush
    fakeDrawingView.setSizeForBrush(30.0f);
    Paint paint = fakeDrawingView.getDrawPaint();
    System.out.println("\n- Testing setSizeForBrush : paint.getStrokeWidth() -");
    System.out.println("    Expected stroke width: 30.0");
    System.out.println("    Actual: " + paint.getStrokeWidth());
    assertEquals(30.0f, paint.getStrokeWidth());
  }

  @Test
  public void testSetErase() {
    FakeDrawingView fakeDrawingView = new FakeDrawingView();
    fakeDrawingView.setErase(true);
    Paint paint = fakeDrawingView.getDrawPaint();
    // if eraser button is selected/clicked
    System.out.println("\n- Testing setErase : paint.getColor() -");
    System.out.println("    Expected color for erasing : " + Color.WHITE);
    System.out.println("    Actual: " + paint.getColor());
    assertEquals(Color.WHITE, paint.getColor());

    // if eraser button is NOT selected/clicked
    fakeDrawingView.setErase(false);
    System.out.println("\n- Testing setErase : paint.getXfermode() -");
    System.out.println("    Expected null if eraser is not selected");
    System.out.println("    Actual: " + paint.getXfermode());
    assertEquals(null, paint.getXfermode());
  }
}
