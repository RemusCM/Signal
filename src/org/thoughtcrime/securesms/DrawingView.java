package org.thoughtcrime.securesms;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("NewApi")
public class DrawingView extends View {

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

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDrawing();
    }

    /**
     * This function returns the current canvas
     * being worked upon by the user.
     * @return drawCanvas the current canvas being
     * worked upon by the user
     */
    public Canvas getCanvas()
    {
        return drawCanvas;
    }

    /**
     * This method initializes the attributes of the
     * CustomViewForDrawing class.
     */
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

    @Override
    protected void onSizeChanged(int w, int h, int wprev, int hprev){
        super.onSizeChanged(w, h, wprev, hprev);
        canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    /**
     * This method is called when a stroke is drawn on the canvas
     * as a part of the painting.
     */
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        for (CustomPath p : paths){
            drawPaint.setStrokeWidth(p.getBrushThickness());
            drawPaint.setColor(p.getColor());
            canvas.drawPath(p, drawPaint);
        }
        if(!drawPath.isEmpty()) {
            drawPaint.setStrokeWidth(drawPath.getBrushThickness());
            drawPaint.setColor(drawPath.getColor());
            canvas.drawPath(drawPath, drawPaint);
        }
    }

    /**
     * This method acts as an event listener when a touch
     * event is detected on the device.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawPath.setColor(paintColor);
                drawPath.setBrushThickness(brushSize);
                if(smoothStrokes & !erase) {
                    startX = touchX;
                    startY = touchY;
                }
                drawPath.reset();
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                if(smoothStrokes && !erase) {

                    endX = touchX;
                    endY = touchY;
                    drawPath.reset();
                    drawPath.moveTo(startX, startY);
                    drawPath.lineTo(endX, endY);
                }

                paths.add(drawPath);
                drawPath = new CustomPath(paintColor, brushSize);
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    /**
     * This function is called when the user desires a color change.
     * This functions sets the paintColor object of CustomViewForDrawing.
     * @param newColor
     */
    public void setColor(String newColor){
        System.out.println("setcolor");
        //invalidate();
        System.out.println(newColor + " !!!!!!!!!!!!!!!!!!!!!!!!!!");
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    /**
     * This function returns the current color that has been
     * selected.
     * @return current color
     */
    public int getColor()
    {
        return paintColor;
    }

    /**This method is called when either the brush or the eraser
     * sizes are to be changed. This method sets the brush/eraser
     * sizes to the new values depending on user selection.
     */
    public void setSizeForBrush(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    /**
     * This functions sets the previous brush
     * size upon brush size change.
     * @param prevSize
     */
    public void setPrevBrushSize(float prevSize){
        prevBrushSize = prevSize;
    }

    /**
     * This function is called when the brush size
     * is changed by the user. This function returns
     * previous brush size used prior to brush size
     * change
     * @return
     */
    public float getPrevBrushSize(){
        return prevBrushSize;
    }

    /**
     * This function sets the paint color
     * to white when eraser functionality is
     * selected by the user.
     * @param bErase
     */
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
}

class CustomPath extends Path{
    private int color;
    private float brushThickness;

    public float getBrushThickness() {
        return brushThickness;
    }

    public void setBrushThickness(float brushThickness) {
        this.brushThickness = brushThickness;
    }

    public CustomPath(int color, float brushThickness) {
        super();
        this.color = color;
        this.brushThickness = brushThickness;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
