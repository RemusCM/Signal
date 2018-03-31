package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DrawingActivity extends Activity implements OnClickListener {
    private DrawingView drawView;
    private Button drawBtn;
    private Button eraseBtn, saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_activity);

        drawView = (DrawingView) findViewById(R.id.drawing);

        drawBtn = (Button) findViewById(R.id.pen_button);
        drawBtn.setOnClickListener(this);

        drawView.setSizeForBrush(10);

        eraseBtn = (Button)findViewById(R.id.eraser_button);
        eraseBtn.setOnClickListener(this);

        saveBtn = (Button)findViewById(R.id.save_button);
        saveBtn.setOnClickListener(this);

    }

    /**
     * This method is called when the user selects "yes" from the save
     * dialog box. This method saves the current image on which the user
     * is working to the device gallery.
     */
    public void saveDrawing()
    {

        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                makeToast("Saved drawing");
                //save drawing
                Bitmap bitmap;
                //View v1 = findViewById(R.id.drawing);
                View v1 = drawView;
                v1.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", ".png");
                v1.setDrawingCacheEnabled(false);
            }
        });

        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    /**
     * This method is used to return the current status of the drawView
     * @return drawView the current status of the drawView
     */
    public DrawingView getDrawView()
    {
        return drawView;
    }

    /**
     * This method is called to display a toast message on device
     * screen.
     * @param message
     */
    public void makeToast(String message) {
        // with jam obviously
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * This function defines all the event listeners for the
     * icons in the main layout of the application.
     */
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.pen_button)
        {
            drawView.setColor("black");
            drawView.setSizeForBrush(10);
            drawView.setPrevBrushSize(10);
        }
        else if(v.getId()==R.id.eraser_button){
            drawView.setErase(true);
            drawView.setSizeForBrush(30);
        }
        else if(v.getId()==R.id.save_button)
        {
            saveDrawing();
        }
    }
}
