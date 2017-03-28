package com.aquiletour.aquiletour.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import com.aquiletour.aquiletour.R;

/**
 * Class extended from activities that use a toolbar
 */
abstract public class ActivityWithToolbar extends AppCompatActivity {
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutId());
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.setSupportActionBar(this.toolbar);
    }

    /**
     * Retrieves the layout to use for this controller
     * @return int The layout identifier as defined in R.layout
     */
    abstract protected int getLayoutId();
}
