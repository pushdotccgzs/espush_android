package cn.espush.app;

import android.content.Intent;
import android.os.Bundle;



public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    toMain();
                }
                toMain();
            }
        }).start();

    }

    private void toMain() {
        Intent i = new Intent(mContext, MainActivity.class);
        startActivity(i);
        finish();


    }

    @Override
    protected void setTitleBar() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

}
