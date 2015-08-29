package com.kas4.espush;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kas4.espush.app.EspushApplication;

/**
 * Created by kas4 QQ: 1504368178 on 15/6/20.
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected Context mContext = this;
    protected Activity mActivity = this;
    protected EspushApplication mAppContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppContext = (EspushApplication) getApplication();

        int res = getLayoutRes();
        if (res <= 0) {
            return;
        }

        setContentView(res);

        setTitleBar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected abstract void setTitleBar();

    protected abstract int getLayoutRes();


    ProgressDialog mProgressSpinnerDialog;
    protected void showProgressDialog(String title,String message) {
        if(mProgressSpinnerDialog==null){
            mProgressSpinnerDialog = new ProgressDialog(mContext);
        }
        mProgressSpinnerDialog.setTitle(title);
        mProgressSpinnerDialog.setMessage(message);
        mProgressSpinnerDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressSpinnerDialog.show();
    }

    protected void cancelProgressDialog() {
        if(mProgressSpinnerDialog!=null&&mProgressSpinnerDialog.isShowing()){
            mProgressSpinnerDialog.cancel();
        }
    }
    Handler handler=new Handler();

}
