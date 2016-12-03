package cf.VoxStudio.bubblekeep;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefsActivityFragment extends PreferenceFragment {


        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
}
