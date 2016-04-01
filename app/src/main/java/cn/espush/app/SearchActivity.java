package cn.espush.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import cn.espush.app.app.Constant;
import cn.espush.app.entity.AppTypeEntity;
import cn.espush.app.entity.BaseEntity;
import cn.espush.app.manager.AppTypeManager;
import cn.espush.app.search.LocalSearchManager;
import cn.espush.app.search.udp.MSG_CFG;
import cn.espush.app.search.wifi.WifiClientManager;
import cn.espush.app.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivity {

    private Toolbar toolbar;
    private Handler handler = new Handler();
    private EditText et_ssid;
    private EditText et_pwd;
    private Spinner spinner;
    private TextView et_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initSpinner();
        requestAppTypeList();

        ToastUtil.showShortToast(getApplicationContext(), "请先连接设备AP");
    }

    private void initView() {
        et_ssid = (EditText) findViewById(R.id.et_ssid);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_select = (TextView) findViewById(R.id.et_select);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();


        unregisterReceiver();

    }

    List<BaseEntity> mDataList = new ArrayList<>();
    List<CharSequence> mStrList = new ArrayList<>();

    void requestAppTypeList() {

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                List<AppTypeEntity> list = AppTypeManager.getAppTypeList(mContext);
                if (list != null) {
                    mDataList.addAll(list);
                    for (AppTypeEntity o : list) {
                        String str = o.getName();
                        mStrList.add(str);
                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();

                    }
                });


                return null;
            }
        };
        task.execute();

    }

    ArrayAdapter<CharSequence> mAdapter;


    private void initSpinner() {
        mStrList.add("请选择");

        spinner = (Spinner) findViewById(R.id.spinner);
        mAdapter = new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_spinner_item, mStrList);

//                ArrayAdapter.createFromResource(
//                this, R.array.array_search_spinner, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mAdapter);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {

                        } else {
                            int pos = i - 1;
                            AppTypeEntity o = (AppTypeEntity) mDataList.get(pos);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    et_select.setError(null);
                    et_select.clearFocus();
                }
                return false;
            }
        });

    }

    @Override
    protected void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("设备组网");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }

    EditText errorView = null;

    public void onClickSearch(View view) {
        if (errorView != null)
            errorView.setError(null);


        String ssid = et_ssid.getEditableText().toString();
        String pwd = et_pwd.getEditableText().toString();


        boolean flag = true;
        View v = null;
        String error = null;

        int pos = spinner.getSelectedItemPosition();
        if (pos == 0) {
            v = et_select;
            error = "类别未选择";

            flag = false;
        }

        if (TextUtils.isEmpty(pwd)) {
            v = et_pwd;
            error = "不能为空";

            flag = false;
        }
        if (TextUtils.isEmpty(ssid)) {
            v = et_ssid;
            error = "不能为空";

            flag = false;
        }

        if (!flag) {
            errorView = (EditText) v;
            errorView.requestFocus();
            errorView.setError(error);

        } else {
            AppTypeEntity o = (AppTypeEntity) mDataList.get(pos - 1);
            doSearch(ssid, pwd, o);
        }

    }

    // TODO
    void doSearch(final String ssid, final String pwd, AppTypeEntity o) {

        MSG_CFG m = new MSG_CFG();
        int id = 0;
        try {
            id = Integer.parseInt(o.getAppid());
        } catch (NumberFormatException e) {

        }
        m.setAppid(id);
        m.setAppkey(o.getAppkey());
        m.setSsid(ssid);
        m.setPassword(pwd);

        LocalSearchManager.getInstance(mContext).setMsg(m);

        AsyncTask task = new AsyncTask() {


            @Override
            protected Object doInBackground(Object[] objects) {

                LocalSearchManager.sendBroadcast(mContext, Constant.TYPE_MSG_SEARCH_CONNECTING);
//                boolean b = WifiClientManager.getInstance(mContext).connect(ssid, pwd);
                boolean b = WifiClientManager.getInstance(mContext).search(ssid, pwd);

                return b;
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                LocalSearchManager.sendBroadcast(mContext, Constant.TYPE_MSG_SEARCH_FAIL);
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Boolean b = (Boolean) o;
                if (b) {

                } else {
                    LocalSearchManager.sendBroadcast(mContext, Constant.TYPE_MSG_SEARCH_FAIL);
                }
            }
        };
        task.execute();
    }


    MsgReceiver mMsgReceiver = new MsgReceiver();


    void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_MSG_SEARCH);
        registerReceiver(mMsgReceiver, filter);

    }

    void unregisterReceiver() {

        unregisterReceiver(mMsgReceiver);

    }


    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent i) {
            if (i.getAction().equals(Constant.ACTION_MSG_SEARCH)) {

                final int type = i.getIntExtra("type", 0);

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        switch (type) {
                            case Constant.TYPE_MSG_SEARCH_FAIL: {
                                cancelProgressDialog();
                                ToastUtil.showShortToast(getApplicationContext(), "Fail");
                            }
                            break;
                            case Constant.TYPE_MSG_SEARCH_CONNECTING: {
                                showProgressDialog(null, "Connecting..");
                            }
                            break;
                            case Constant.TYPE_MSG_SEARCH_BEEP: {
                                showProgressDialog(null, "Say Hello..");
                            }
                            break;
                            case Constant.TYPE_MSG_SEARCH_SEND_DATA: {
                                showProgressDialog(null, "Send data..");
                            }
                            break;
                            case Constant.TYPE_MSG_SEARCH_SEND_OK: {

                                cancelProgressDialog();
                                ToastUtil.showShortToast(getApplicationContext(), "Success!");
                                finish();

                            }
                            break;

                        }
                    }
                });

            }
        }

    }
}
