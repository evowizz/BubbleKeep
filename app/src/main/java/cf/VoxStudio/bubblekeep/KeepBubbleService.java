/*
The bubble service
    Copyright (C) 2016  VoxStudio

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package cf.VoxStudio.bubblekeep;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class KeepBubbleService extends Service {

    //keep stuff
    public final static String Keep = "com.google.android.keep";
    public final static String Activity = Keep + ".activities.ShareReceiverActivity";

    // variables
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams Params;
    private int windowHeight;
    private int windowWidth;

    // UI
    private ImageView exitImage;
    private ImageView openImage;

    //preferences
    SharedPreferences sharedPref;
    SharedPreferences prefsFragment;
    SharedPreferences.Editor editor;

    // methods
    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(Service.WINDOW_SERVICE);

        prefsFragment = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref = getSharedPreferences("MainPrefs", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showHud();
        return START_STICKY;
    }

    private void showHud() {
        if (exitImage != null) {
            mWindowManager.removeView(exitImage);
            exitImage = null;
        }

        Params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;

        Params.gravity = Gravity.TOP | Gravity.END;

        openImage = new ImageView(this);
        openImage.setImageDrawable(getImage());
        addOpenListener();

        Params.x = 0;
        Params.y = 100;

        mWindowManager.addView(openImage, Params);
        openImage.requestLayout();
        addOnTouchListener();
        addOpenListener();
    }

    private void addOnTouchListener() {
        openImage.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        initialX = Params.x;
                        initialY = Params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        // add recycle bin when moving crumpled paper
                        addExitView();

                        return false;
                    case MotionEvent.ACTION_UP:

                        int centerOfScreenByX = windowWidth / 2;

                        // remove crumpled paper when the it is in the recycle bin's area
                        if ((Params.y > windowHeight - exitImage.getHeight() - openImage.getHeight()) &&
                                ((Params.x > centerOfScreenByX - exitImage.getWidth() - openImage.getWidth() / 2) && (Params.x < centerOfScreenByX + exitImage.getWidth() / 2))) {
                                stopSelf();
                        }

                        // always remove recycle bin ImageView when paper is dropped
                        mWindowManager.removeView(exitImage);
                        exitImage = null;

                        return false;
                    case MotionEvent.ACTION_MOVE:
                        // move paper ImageView
                        Params.x = initialX + (int) (initialTouchX - event.getRawX());
                        Params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(openImage, Params);
                        return false;
                }
                return true;
            }
        });
    }

    private void addExitView() {
        // add recycle bin ImageView centered on the bottom of the screen
        WindowManager.LayoutParams mRecycleBinParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        mRecycleBinParams.gravity = Gravity.BOTTOM | Gravity.CENTER;

        exitImage = new ImageView(this);
        exitImage.setImageResource(R.mipmap.ic_bubble2);

        mRecycleBinParams.x = 0;
        mRecycleBinParams.y = 25;

        mWindowManager.addView(exitImage, mRecycleBinParams);

        YoYo.with(Techniques.ZoomIn)
                .duration(200)
                .playOn(exitImage);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // remove views on destroy!
        if (openImage != null) {
            mWindowManager.removeView(openImage);
            openImage = null;
        }

        if (exitImage != null) {
            mWindowManager.removeView(exitImage);
            exitImage = null;
        }
    }

    public Drawable getImage(){
        Drawable image = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_bubble1); //setting default fot android studio not not to give me na error

        if (prefsFragment.getString("bubblechanger","").matches("1")){
            image = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_bubble1);
        } else if (prefsFragment.getString("bubblechanger","").matches("2")){
            image = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_bubble2);
        } else if (prefsFragment.getString("bubblechanger","").matches("3")){
            image = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_bubble3);
        }
        return image;
    }

    public void addOpenListener(){
        openImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent = new Intent();
                    intent.setClassName(Keep, Activity);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (ActivityNotFoundException e) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(KeepBubbleService.this, R.style.AppTheme_MaterialDialogTheme);

                    dialogBuilder.setTitle("Google Keep not found");
                    dialogBuilder.setMessage("Do you want to install it now?");
                    dialogBuilder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    exit();
                                }
                            }
                    );

                    dialogBuilder.setPositiveButton("Install it!",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    try {
                                        Intent playstore = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.keep"));
                                        playstore.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(playstore);
                                    } catch (ActivityNotFoundException e) {
                                        Intent playstore = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.keep"));
                                        playstore.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(playstore);
                                    }

                                }
                            });

                    final AlertDialog dialog = dialogBuilder.create();
                    final Window dialogWindow = dialog.getWindow();
                    assert dialogWindow != null;
                    final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();

                    // Set fixed width (280dp) and WRAP_CONTENT height
                    final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialogWindowAttributes);
                    lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics());
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialogWindow.setAttributes(lp);

                    // Set to TYPE_SYSTEM_ALERT so that the Service can display it
                    dialogWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.show();
                }

                YoYo.with(Techniques.Pulse)
                        .duration(700)
                        .playOn(openImage);
            }
        });
    }

    public void exit() {
        YoYo.with(Techniques.ZoomOut)
                .duration(700)
                .playOn(openImage);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, 700);

        editor.putBoolean("isOn", false);
        editor.apply();
    }
}


