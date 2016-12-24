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

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.List;

import cf.VoxStudio.bubblekeep.Services.KeepBubbleService;

public class MyPreferencesActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }


    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        SharedPreferences prefsFragment;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            prefsFragment = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
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


            if (key.matches("bubblechanger") && isServiceRunning(KeepBubbleService.class, getActivity())) {
                getActivity().stopService(new Intent(getActivity(), KeepBubbleService.class));
                getActivity().startService(new Intent(getActivity(), KeepBubbleService.class));

            } else if (key.matches("launcher-icon-changer")) {
                setIcon(getIcon(), getActivity());
                killLauncher();
                Toast.makeText(getActivity(), "Launcher restart might be required for the launcher icon to change", Toast.LENGTH_LONG).show();
            } else if (key.matches("navbarcolor")){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (sharedPreferences.getBoolean("navbarcolor", true)) {
                        getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
                        getActivity().recreate();
                    } else if (!sharedPreferences.getBoolean("navbarcolor", true)) {
                        getActivity().getWindow().setNavigationBarColor(Color.parseColor("#000000"));
                        getActivity().recreate();
                    }
                }

            }


        }


        public void setIcon(int icon, Context ctx) {
            PackageManager pm = getActivity().getPackageManager();
            // Enable/disable activity-aliases

            if (icon == R.mipmap.ic_launcher) {
                pm.setComponentEnabledSetting(
                        new ComponentName(ctx, "cf.VoxStudio.bubblekeep.MainActivity-Bubble1"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                pm.setComponentEnabledSetting(
                        new ComponentName(ctx, "cf.VoxStudio.bubblekeep.MainActivity-Bubble2"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                pm.setComponentEnabledSetting(
                        new ComponentName(ctx, "cf.VoxStudio.bubblekeep.MainActivity-Bubble3"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            } else if (icon == R.mipmap.ic_bubble2) {
                pm.setComponentEnabledSetting(
                        new ComponentName(ctx, "cf.VoxStudio.bubblekeep.MainActivity-Bubble2"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                pm.setComponentEnabledSetting(
                        new ComponentName(ctx, "cf.VoxStudio.bubblekeep.MainActivity-Bubble1"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                pm.setComponentEnabledSetting(
                        new ComponentName(ctx, "cf.VoxStudio.bubblekeep.MainActivity-Bubble3"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            } else if (icon == R.mipmap.ic_bubble3) {
                pm.setComponentEnabledSetting(
                        new ComponentName(ctx, "cf.VoxStudio.bubblekeep.MainActivity-Bubble3"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                pm.setComponentEnabledSetting(
                        new ComponentName(ctx, "cf.VoxStudio.bubblekeep.MainActivity-Bubble1"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                pm.setComponentEnabledSetting(
                        new ComponentName(ctx, "cf.VoxStudio.bubblekeep.MainActivity-Bubble2"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            }

        }


        public int getIcon() {
            int image;
            if (prefsFragment.getString("launcher-icon-changer", "").matches("1")) {
                image = R.mipmap.ic_launcher;
            } else if (prefsFragment.getString("launcher-icon-changer", "").matches("2")) {
                image = R.mipmap.ic_bubble2;
            } else if (prefsFragment.getString("launcher-icon-changer", "").matches("3")) {
                image = R.mipmap.ic_bubble3;
            } else {
                image = R.mipmap.ic_bubble1;
            }
            return image;
        }

        private boolean isServiceRunning(Class<?> serviceClass, Context c) {
            ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

        private void killLauncher() {
            PackageManager pm = getActivity().getPackageManager();
            ActivityManager am = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);

            // Find launcher and kill it
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            List<ResolveInfo> resolves = pm.queryIntentActivities(i, 0);
            for (ResolveInfo res : resolves) {
                if (res.activityInfo != null) {
                    am.killBackgroundProcesses(res.activityInfo.packageName);
                }
            }
        }


    }


}