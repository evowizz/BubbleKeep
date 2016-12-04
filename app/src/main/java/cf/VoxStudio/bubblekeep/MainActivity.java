/*
The main activity/screen
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class MainActivity extends Activity {

    Switch mainSwitch;
    TextView switchText;
    String textOn = "On";
    String textOff = "Off";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = getSharedPreferences("MainPrefs", Context.MODE_PRIVATE);
        SharedPreferences introPref = getSharedPreferences("IntroPref", Context.MODE_PRIVATE);
        mainSwitch = (Switch) findViewById(R.id.main_switch);
        switchText = (TextView) findViewById(R.id.switchText);

        if (introPref.getBoolean("hasSeenIntro", false)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(Settings.canDrawOverlays(MainActivity.this)){
                    startService(new Intent(MainActivity.this, KeepBubbleService.class));
                }
            }else {
                startService(new Intent(MainActivity.this, KeepBubbleService.class));
            }
        } else {
            Intent intent = new Intent(this, MainIntroActivity.class);
            startActivity(intent);
        }

        if(sharedPref.getBoolean("isRunning", true)){
            mainSwitch.setChecked(true);
            switchText.setText(textOn);
        }
        else {
            mainSwitch.setChecked(false);
            switchText.setText(textOff);
        }

        mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                handleSwitchOn();
            }else {
                handleSwitchOff();
            }
            }
        });

    }

    public void switchSwitch(View view){
        if(mainSwitch.isChecked()){
            mainSwitch.setChecked(false);
        } else {
            mainSwitch.setChecked(true);
        }
    }

    public void handleSwitchOff() {
            switchText.setText(textOff);
                    KeepBubbleService.wm.removeViewImmediate(KeepBubbleService.ll);
                    stopService(new Intent(MainActivity.this, KeepBubbleService.class));
                }
    public void handleSwitchOn(){

            switchText.setText(textOn);
            startService(new Intent(MainActivity.this, KeepBubbleService.class));
        }


    }
