package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.thoughtcrime.securesms.giph.ui.GiphyActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

@SuppressLint("NewApi")
public class DrawingActivity extends Activity implements OnClickListener {

  private static final String TAG = DrawingActivity.class.getSimpleName();

  private DrawingView drawView;
  private SecureRandom secureRandom = new SecureRandom();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.drawing_activity);

    drawView = findViewById(R.id.drawing);
    drawView.setSizeForBrush(10);

    Button drawBtn = findViewById(R.id.pen_button);
    drawBtn.setOnClickListener(this);

    Button eraseBtn = findViewById(R.id.eraser_button);
    eraseBtn.setOnClickListener(this);

    Button saveBtn = findViewById(R.id.save_button);
    saveBtn.setOnClickListener(this);

  }

  /**
   * This method is called when the user selects "yes" from the save
   * dialog box. This method saves the current image on which the user
   * is working to the device gallery.
   */
  public void saveDrawing() {
    AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
    saveDialog.setTitle("Save drawing");
    saveDialog.setMessage("Save drawing to device Gallery?");
    saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @SuppressLint("StaticFieldLeak")
      public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(getBaseContext(), "Saving drawing...", Toast.LENGTH_SHORT).show();

        View content = drawView;
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String imageName = randomString(4).toLowerCase();
        File file = new File(path + "/" + imageName + ".png");
        FileOutputStream osStream;
        try {
          file.createNewFile();
          osStream = new FileOutputStream(file);
          bitmap.compress(Bitmap.CompressFormat.PNG, 100, osStream);
          osStream.flush();
          osStream.close();
          Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        
        new AsyncTask<Void, Void, Uri>() {
          @Override
          protected Uri doInBackground(Void... params) {
            return Uri.fromFile(file);
          }
          protected void onPostExecute(@Nullable Uri uri) {
            if (uri == null) {
              Toast.makeText(DrawingActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            } else {
              setResult(RESULT_OK, new Intent().setData(uri));
              finish();
            }
          }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

      }
    });

    saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });
    saveDialog.show();
  }

  /**
   * This method is used to return the current status of the drawView
   *
   * @return drawView the current status of the drawView
   */
  public DrawingView getDrawView() {
    return drawView;
  }

  /**
   * This function defines all the event listeners for the
   * icons in the main layout of the application.
   */
  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.pen_button) {
      drawView.setColor("black");
      drawView.setSizeForBrush(10);
      drawView.setPrevBrushSize(10);
      Toast.makeText(this, "Pen selected.", Toast.LENGTH_SHORT).show();
    } else if (v.getId() == R.id.eraser_button) {
      drawView.setErase(true);
      drawView.setSizeForBrush(30);
      Toast.makeText(this, "Eraser selected.", Toast.LENGTH_SHORT).show();
    } else if (v.getId() == R.id.save_button) {
      saveDrawing();
    }
  }

  public void onClickCancel(View view) {
    DrawingActivity.super.onBackPressed();
  }

  public String randomString(int len) {
    StringBuilder sb = new StringBuilder(len);
    String alphaNumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    for (int i = 0; i < len; i++)
      sb.append(alphaNumeric.charAt(secureRandom.nextInt(alphaNumeric.length())));
    return sb.toString();
  }
}
