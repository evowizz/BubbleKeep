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
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class KeepBubbleService extends Service {

    WindowManager wm;
    LinearLayout ll;
    boolean isMoving;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParameteres = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.setBackgroundColor(Color.argb(0, 0, 0, 0));
        ll.setLayoutParams(layoutParameteres);


        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.TOP | Gravity.START;
        parameters.x = 0;
        parameters.y = 0;
        final ImageButton openButton = new ImageButton(this);
        openButton.setImageResource(R.mipmap.ic_keep);
        openButton.setBackgroundColor(Color.TRANSPARENT);

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YoYo.with(Techniques.Pulse)
                        .duration(700)
                        .playOn(openButton);
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.google.android.keep", "com.google.android.keep.activities.ShareReceiverActivity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        openButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(KeepBubbleService.this, R.style.AppTheme_MaterialDialogTheme);

                dialogBuilder.setTitle("Exit?");
                dialogBuilder.setMessage("Do you want to exit?");
                dialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

                dialogBuilder.setPositiveButton("Sure",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                YoYo.with(Techniques.ZoomOut)
                                        .duration(700)
                                        .playOn(openButton);
                                Handler h = new Handler();
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        wm.removeViewImmediate(ll);
                                        stopService(new Intent(KeepBubbleService.this, KeepBubbleService.class));
                                        KeepBubbleService.this.stopSelf();
                                    }
                                }, 700);
                            }
                        });

                final AlertDialog dialog = dialogBuilder.create();
                final Window dialogWindow = dialog.getWindow();
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
                return true;

            }
        });


        ViewGroup.LayoutParams btnParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        openButton.setLayoutParams(btnParameters);
        ll.addView(openButton);
        wm.addView(ll, parameters);

        openButton.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = parameters;
            double x;
            double y;
            double pressedX;
            double pressedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isMoving = true;
                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        isMoving = true;
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));

                        wm.updateViewLayout(ll, updatedParameters);

                    default:
                        isMoving = false;
                        break;
                }

                return false;
            }
        });

    }
}

