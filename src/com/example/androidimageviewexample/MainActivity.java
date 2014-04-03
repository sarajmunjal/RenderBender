package com.example.androidimageviewexample;

import java.util.Calendar;
import android.support.v8.renderscript.*;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	Button button1,button2;
	ImageView image;
	private RenderScript mRS;
    private Allocation mInAllocation;
    private Allocation[] mOutAllocations;
    private ScriptC_saturation mScript;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		addButtonListener();
		
	}
	
	public void addButtonListener() {

		image = (ImageView) findViewById(R.id.image);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				//Bitmap colorPic = BitmapFactory.decodeFile("/mnt/sdcard/Pictures/logo2.jpg");
				Bitmap colorPic = BitmapFactory.decodeResource(getResources(),R.drawable.logo2);
				image.setImageBitmap(colorPic);
			}
		});
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				//Bitmap colorPic = BitmapFactory.decodeFile("/mnt/sdcard/Pictures/logo2.jpg");
				Bitmap colorPic = BitmapFactory.decodeResource(getResources(),R.drawable.logo2);
				Bitmap bwPic= Bitmap.createBitmap(colorPic.getWidth(),colorPic.getHeight(),colorPic.getConfig());
				//Create the context and I/O allocations
		        
		        final Allocation input = Allocation.createFromBitmap(rs, colorPic,Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);
		        final Allocation output = Allocation.createTyped(rs, input.getType());
				int i,j,pixel;
				
				int A,R,G,B;
				final  double redQ = 0.299, greenQ=0.587 , blueQ = 0.114;
				Calendar t1=Calendar.getInstance();
				for(i=0;i<colorPic.getWidth();i++)
				{
					for(j=0;j<colorPic.getHeight();j++)
					{
						pixel = colorPic.getPixel(i, j);
						A=Color.alpha(pixel);
						R=Color.red(pixel);
						G=Color.green(pixel);
						B=Color.blue(pixel);
						R = 255-R;
						G=255-G;
						B = 255-B;
						bwPic.setPixel(i,j,Color.argb(A, R, G, B));
					}
				}
				Calendar t2=Calendar.getInstance();
				Long lund = t2.getTimeInMillis()-t1.getTimeInMillis();
				Log.e("kaam",""+ lund );
				image.setImageBitmap(bwPic);
			}
		});
	}
	
	private void createScript() {
        //Initialize RS
        mRS = RenderScript.create(this);
 
        //Allocate buffers
        mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn);
        mOutAllocations = new Allocation[NUM_BITMAPS];
        for (int i = 0; i < NUM_BITMAPS; ++i) {
            mOutAllocations[i] = Allocation.createFromBitmap(mRS, mBitmapsOut[i]);
        }
 
        //Load script
        mScript = new ScriptC_saturation(mRS);
    }
}