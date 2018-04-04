package org.thoughtcrime.securesms;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by daanish on 4/4/2018.
 */

public interface DrawingViewInterface {

  void setUpDrawing();

  void setColor(String newColor);

  void setSizeForBrush(float newSize);

  void setErase(boolean bErase);
}
