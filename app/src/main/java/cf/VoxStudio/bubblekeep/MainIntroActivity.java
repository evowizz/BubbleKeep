/*
The intro activity
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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

        addSlide(new SimpleSlide.Builder()
                .title("Welcome")
                .description("This intro will guide you through this app")
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Take notes everywhere")
                .description("Doesn't matter if you are in Browser or on homescreen, you can take notes quickly")
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Just a click")
                .description("Click on your Keep Bubble to open note taking window")
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addSlide(new SimpleSlide.Builder()
                    .title("Just one more thing")
                    .description("Due to changes in Android Marshmallow, we need to set the overlay permission")
                    .image(R.drawable.ic_perm_overlay)
                    .background(R.color.colorPrimary)
                    .backgroundDark(R.color.colorPrimaryDark)
                    .buttonCtaLabel("Grant")
                    .buttonCtaClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:cf.VoxStudio.bubblekeep")));
                        }
                    })
                    .build());

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setNavigationPolicy(new NavigationPolicy() {
                @Override
                public boolean canGoForward(int position) {
                    switch (position) {
                        case 4:
                            return Settings.canDrawOverlays(MainIntroActivity.this);
                    }
                    return true;
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
