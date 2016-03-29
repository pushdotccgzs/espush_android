package cn.espush.app;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import cn.espush.app.R;


public class AboutActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView wv = (WebView) findViewById(R.id.wv);
        wv.loadUrl("file:///android_asset/about.html");
    }

    @Override
    protected void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("关于epush.cn");

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about;
    }


}
