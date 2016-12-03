package cf.VoxStudio.bubblekeep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPref = getSharedPreferences("IntroPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("hasSeenIntro", true);
        editor.apply();

        /**
         * Standard slide (like Google's intros)
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addSlide(new SimpleSlide.Builder()
                    .title("We need to set permissions")
                    .description("Duo to changes in android marshmallow we need the overlay permmision")
                    .image(R.drawable.ic_perm_overlay)
                    .background(R.color.colorPrimary)
                    .backgroundDark(R.color.colorPrimaryDark)
                    .buttonCtaLabel("Grant")
                    .buttonCtaClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            startActivity(i);
                        }
                    })
                    .build());

        }else {
            addSlide(new SimpleSlide.Builder()
                    .title("You are all set")
                    .description("Please continue")
                    .image(R.drawable.ic_check)
                    .background(R.color.colorPrimary)
                    .backgroundDark(R.color.colorPrimaryDark)
                    .build());

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setNavigationPolicy(new NavigationPolicy() {
                @Override
                public boolean canGoForward(int position) {
                    return Settings.canDrawOverlays(MainIntroActivity.this);
                }

                @Override
                public boolean canGoBackward(int i) {
                    return true;
                }
            });
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK) || super.onKeyDown(keyCode, event);

    }
}
