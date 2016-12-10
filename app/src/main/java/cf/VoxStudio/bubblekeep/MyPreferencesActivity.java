/*
The preferences fragment
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
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class MyPreferencesActivity extends PreferenceActivity  {

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        SharedPreferences sharedPref = getSharedPreferences("MainPrefs", Context.MODE_PRIVATE);




        editor = sharedPref.edit();
        editor.apply();


    }


    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }

        @Override
        public void onResume() {
            super.onResume();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            ListPreference lp = (ListPreference) findPreference("bubblechanger");

            if (key.matches("bubblechanger")) {
                Drawable image = getResources().getDrawable(R.mipmap.ic_bubble1); //setting default fot android studio not not to give me na error

                if (lp.getValue().matches("1")){
                    image = getResources().getDrawable(R.mipmap.ic_bubble1);
                } else if (lp.getValue().matches("2")){
                    image = getResources().getDrawable(R.mipmap.ic_bubble2);
                } else if (lp.getValue().matches("3")){
                    image = getResources().getDrawable(R.mipmap.ic_bubble3);
                }
                KeepBubbleService.wm.removeViewImmediate(KeepBubbleService.ll);
                KeepBubbleService.openButton.setImageDrawable(image);
                KeepBubbleService.wm.addView(KeepBubbleService.ll, KeepBubbleService.parameters);

            }
        }
    }





}