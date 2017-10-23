package com.valdizz.penaltycheck;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.valdizz.penaltycheck.db.DataHelper;


public class AutoEditActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoedit);

        long auto_id = getIntent().getLongExtra(DataHelper.AUTOID_PARAM, -1);
        setTitle(auto_id != -1 ? R.string.title_activity_editauto : R.string.title_activity_addauto);
        addFragment(auto_id);

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
    }

    private void addFragment(long auto_id){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_autoedit, AutoEditFragment.newInstance(auto_id));
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_autoedit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                AutoEditFragment autoEditFragment = (AutoEditFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_autoedit);
                autoEditFragment.saveAuto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
