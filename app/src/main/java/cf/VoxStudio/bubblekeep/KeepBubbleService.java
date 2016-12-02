package cf.VoxStudio.bubblekeep;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class KeepBubbleService extends Service {

    WindowManager wm;
    LinearLayout ll;

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
                LinearLayout.LayoutParams.MATCH_PARENT, 400);
        ll.setBackgroundColor(Color.argb(0, 0, 0, 0));
        ll.setLayoutParams(layoutParameteres);


        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                100, 100, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.TOP | Gravity.LEFT;
        parameters.x = 0;
        parameters.y = 100;
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
                wm.removeViewImmediate(ll);
                stopService(new Intent(KeepBubbleService.this, KeepBubbleService.class));
                KeepBubbleService.this.stopSelf();
                return true;
            }
        });



        ViewGroup.LayoutParams btnParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));

                        wm.updateViewLayout(ll, updatedParameters);

                    default:
                        break;
                }

                return false;
            }
        });

    }
}

