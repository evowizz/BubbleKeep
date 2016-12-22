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

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import cf.VoxStudio.bubblekeep.Services.KeepBubbleService;

public class MainActivity extends AppCompatActivity {
    //Here are the variables
    Switch mainSwitch;
    TextView switchText;
    SharedPreferences.Editor editor;
    String textOn = "On";
    String textOff = "Off";
    SharedPreferences prefsFragment;

    CompoundButton.OnCheckedChangeListener mainSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                handleSwitchOn();
            } else {
                handleSwitchOff();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
        Toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(Toolbar);

        //Adding shared preferences
        SharedPreferences sharedPref = getSharedPreferences("MainPrefs", Context.MODE_PRIVATE);  //Main ones  -  used by every activity
        SharedPreferences introPref = getSharedPreferences("IntroPref", Context.MODE_PRIVATE);  //Intro preferences  -  used only to check if user has seen intro
        prefsFragment = PreferenceManager.getDefaultSharedPreferences(this); //SharedPreferences used by PrefsFragment
        editor = sharedPref.edit();  //shared preferences editor
        //assigning variables
        mainSwitch = (Switch) findViewById(R.id.main_switch);
        switchText = (TextView) findViewById(R.id.switchText);


        //setting on change listener to main switch
        mainSwitch.setOnCheckedChangeListener(mainSwitchListener);

        //If user has seen intro
        if (introPref.getBoolean("hasSeenIntro", false)) {
            //looks like user has seen intro so we will check if it was on the last time
            if (sharedPref.getBoolean("isOn", true)) {
                //it was on, we will check if it is running
                if (isServiceRunning(KeepBubbleService.class)) {
                    //it is running we will check it, but without doing anything
                    mainSwitch.setOnClickListener(null);
                    mainSwitch.setChecked(true);
                    mainSwitch.setOnCheckedChangeListener(mainSwitchListener);
                } else {
                    //it was only on, but killed or ended, so we will set the switch on and launch the service
                    mainSwitch.setChecked(false);
                    switchText.setText(textOff);
                }
            }
            //if it wasn't on we will just uncheck the switch
            else {
                //unchecking switch
                mainSwitch.setChecked(false);
                switchText.setText(textOff);
            }
        }
        //first checking for android version then if the permission is not granted
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //looks like user has android 6
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                //the permission wasn't granted so we will open intro to ask the user for permission
                Intent intent = new Intent(this, MainIntroActivity.class);
                startActivity(intent);
            }
        }
        //if user haven't seen intro
        else {
            //user haven't seen intro, will we open intro
            Intent intent = new Intent(this, MainIntroActivity.class);
            startActivity(intent);
        }


    }

    public void switchSwitch(View view) {
        if (mainSwitch.isChecked()) {
            mainSwitch.setChecked(false);
        } else {
            mainSwitch.setChecked(true);
        }
    }

    public void handleSwitchOff() {
        editor.putBoolean("isOn", false);
        editor.apply();
        switchText.setText(textOff);
        stopService(new Intent(MainActivity.this, KeepBubbleService.class));
    }

    public void handleSwitchOn() {
        editor.putBoolean("isOn", true);
        editor.apply();
        switchText.setText(textOn);
        startService(new Intent(MainActivity.this, KeepBubbleService.class));
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void openAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }


}

