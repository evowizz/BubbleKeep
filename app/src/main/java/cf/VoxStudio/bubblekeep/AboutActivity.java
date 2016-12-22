package cf.VoxStudio.bubblekeep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import cf.VoxStudio.bubblekeep.Adapter.ExpandableRecyclerAdapter;


public class AboutActivity extends AppCompatActivity {
    //TODO: We need to finish it

    RecyclerView recycler;
    PeopleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
        Toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(Toolbar);

        recycler = (RecyclerView) findViewById(R.id.main_recycler);

        adapter = new PeopleAdapter(this);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

    }
}


