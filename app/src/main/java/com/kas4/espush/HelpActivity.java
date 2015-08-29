package com.kas4.espush;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;


public class HelpActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView wv = (WebView) findViewById(R.id.wv);
        wv.loadUrl("file:///android_asset/help.html");
    }

    @Override
    protected void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("帮助");

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about;
    }


}
