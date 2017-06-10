package com.example.anujsharma.yoblunt_exoplayer2.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.anujsharma.yoblunt_exoplayer2.R;
import com.example.anujsharma.yoblunt_exoplayer2.adapters.DisplayVideosAdapter;
import com.example.anujsharma.yoblunt_exoplayer2.dataStructures.DataUrls;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMainRecylerView;
    private DisplayVideosAdapter displayVideosAdapter;
    private DataUrls dataUrls;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        rvMainRecylerView = (RecyclerView) findViewById(R.id.rvMainRecyclerView);
        dataUrls = new DataUrls(this, 5);
        layoutManager = new LinearLayoutManager(this);
        displayVideosAdapter = new DisplayVideosAdapter(this, dataUrls.getDataUrls());
        rvMainRecylerView.setLayoutManager(layoutManager);
        rvMainRecylerView.setAdapter(displayVideosAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
