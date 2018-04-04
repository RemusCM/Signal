package org.thoughtcrime.securesms;

import android.widget.Button;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by daanish on 4/4/2018.
 */

@RunWith(PowerMockRunner.class)
public class DrawingTestingMocking extends BaseUnitTest{
  private DrawingView drawView;
  private Button drawBtn;
  private Button eraseBtn, saveBtn;

  @Override
  public void setUp(){
    drawView = mock(DrawingView.class);
    drawBtn = mock(Button.class);
    eraseBtn = mock(Button.class);
    saveBtn = mock(Button.class);

  }


  public void setUpDrawingView(){

  }



}
