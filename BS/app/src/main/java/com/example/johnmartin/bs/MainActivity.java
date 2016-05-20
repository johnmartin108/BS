package com.example.johnmartin.bs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SettingsFragment s_frag;
    private WelcomeFragment w_frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        s_frag = new SettingsFragment();
        w_frag = new WelcomeFragment();

        getFragmentManager().beginTransaction().replace(R.id.content, w_frag).commit();
    }

    public void onSettingsClicked(View v) {
        getFragmentManager().beginTransaction().replace(R.id.content, s_frag).commit();
    }

    public void onBackClicked(View v) {
        getFragmentManager().beginTransaction().replace(R.id.content, w_frag).commit();
    }
}
