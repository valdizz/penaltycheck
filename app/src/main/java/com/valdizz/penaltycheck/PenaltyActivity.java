package com.valdizz.penaltycheck;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.valdizz.penaltycheck.db.DataHelper;

public class PenaltyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalty);

        setTitle(R.string.title_activity_penalty);
        addFragment(getIntent().getLongExtra(DataHelper.AUTOID_PARAM, -1));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check penalties of this auto
                ((PenaltyFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_penalty)).checkPenalty();
            }
        });
    }

    private void addFragment(long auto_id){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_penalty, PenaltyFragment.newInstance(auto_id));
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_check).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_help:

                return true;
            case R.id.action_rate:
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.valdizz.penaltycheck")));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
