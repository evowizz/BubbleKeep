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
        editor.putBoolean("hasSeenIntro", false);
        editor.apply();

        /**
         * Standard slide (like Google's intros)
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addSlide(new SimpleSlide.Builder()
                    .title("We need to set permissions")
                    .description("Due to changes in Android Marshmallow, we need the overlay permission")
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

        } else {
            addSlide(new SimpleSlide.Builder()
                    .title("You are all set!")
                    .description("Please continue...")
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
