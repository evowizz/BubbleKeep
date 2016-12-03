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
import android.os.Process;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
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
        SharedPreferences sharedPref = getSharedPreferences("IntroPref", Context.MODE_PRIVATE);
        mainSwitch = (Switch) findViewById(R.id.main_switch);
        switchText = (TextView) findViewById(R.id.switchText);

        if (sharedPref.getBoolean("hasSeenIntro", false) || sharedPref.getBoolean("isRunning", false)){
            startService(new Intent(MainActivity.this, KeepBubbleService.class));

        } else {
            Intent intent = new Intent(this, MainIntroActivity.class);
            startActivity(intent);
        }

        if(sharedPref.getBoolean("isRunning", false)){
            mainSwitch.setChecked(true);
            switchText.setText(textOn);
        }
        else {
            mainSwitch.setChecked(false);
            switchText.setText(textOff);
        }

        mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchText.setText(textOn);
                    startService(new Intent(MainActivity.this, KeepBubbleService.class));
                } else {
                    switchText.setText(textOff);
                    YoYo.with(Techniques.ZoomOut)
                            .duration(700)
                            .playOn(KeepBubbleService.openButton);
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            KeepBubbleService.wm.removeViewImmediate(KeepBubbleService.ll);
                            try{
                                stopService(new Intent(MainActivity.this, KeepBubbleService.class));
                            }catch (IllegalArgumentException e) {
                                startService(new Intent(MainActivity.this, KeepBubbleService.class));
                                stopService(new Intent(MainActivity.this, KeepBubbleService.class));
                            }
                        }
                    }, 700);
                }
            }
        });

    }

    public void switchSwitch(View view){
        if(mainSwitch.isChecked()){
            mainSwitch.setChecked(false);
            switchText.setText(textOff);
            try{
                stopService(new Intent(MainActivity.this, KeepBubbleService.class));
            }catch (IllegalArgumentException e){
                startService(new Intent(MainActivity.this, KeepBubbleService.class));
                stopService(new Intent(MainActivity.this, KeepBubbleService.class));
            }
        }else{
            mainSwitch.setChecked(true);
            switchText.setText(textOn);
            startService(new Intent(MainActivity.this, KeepBubbleService.class));
        }
    }

}