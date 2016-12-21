package cf.VoxStudio.bubblekeep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class AboutActivity extends AppCompatActivity{
//TODO: We need to finish it
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
        Toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(Toolbar);

    }
}
