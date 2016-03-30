package cn.espush.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.espressif.iot.esptouch.demo_activity.EsptouchDemoActivity;

import cn.espush.app.adapter.MainAdapter;
import cn.espush.app.adapter.OnItemListener;
import cn.espush.app.app.Constant;
import cn.espush.app.entity.AppTypeEntity;
import cn.espush.app.entity.AppsResponse;
import cn.espush.app.entity.BaseEntity;
import cn.espush.app.manager.AppTypeManager;
import cn.espush.app.manager.NetAPI;
import cn.espush.app.qrcode.QRCodeActivity;
import cn.espush.app.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {


    public void onClickToSearch(View v) {
        Intent i = new Intent(mContext, EsptouchDemoActivity.class);
        startActivity(i);

    }

    public void onClickToQR(View v) {
        Intent i = new Intent(mContext, QRCodeActivity.class);
        startActivityForResult(i, Constant.REQUEST_QRCODE);

    }

    List<AppsResponse> mAppsList = new ArrayList<>();

    public void requestAddAppType(final AppTypeEntity e) {

        if (AppTypeManager.isExist(mContext, e))
            return;

        NetAPI.getApps(e.getAppid(), e.getAppkey(), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                AppsResponse res = (AppsResponse) response;

                // model
                mAppsList.add(res);
                e.setName(res.getName());
                if (res.getOnline() != null)
                    e.setOnline_num(res.getOnline().size());

                // save
                AppTypeManager.addAppType(mContext, e);
                // show
                mDataList.add(e);
                mAdapter.notifyItemInserted(mDataList.size() - 1);

                // for test
                ToastUtil.showShortToast(mContext, "" + res.getName());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast(mContext, "error");
            }
        });
    }

    public void requestAliveAppType(final AppTypeEntity e, final int pos) {
        NetAPI.getApps(e.getAppid(), e.getAppkey(), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                AppsResponse res = (AppsResponse) response;

                // model
                if (mAppsList.get(pos) != null) {
                    mAppsList.set(pos, res);
                }else{
                    mAppsList.add(res);
                }

                e.setName(res.getName());
                e.setOnline_num(res.getOnline().size());
                // show
                mAdapter.notifyItemChanged(pos);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                e.setOnline_num(0);
                mAdapter.notifyItemChanged(pos);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.REQUEST_QRCODE) {
                if (data != null) {
                    Bundle b = data.getBundleExtra("data");
                    String result = b.getString("result");
                    if (result != null) {
                        String[] arr = result.split(Constant.SPLIT_QRCODE_STRING);

                        if (arr != null && arr.length == 2) {
                            AppTypeEntity e = new AppTypeEntity();
                            e.setAppid(arr[0]);
                            e.setAppkey(arr[1]);

                            requestAddAppType(e);
                        }
                    }

                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFirstPage();
    }

    final int MODE_FIRST_LOAD = 1;
    final int MODE_HAND_REFRESH = 1 << 1;


    int mMode = MODE_FIRST_LOAD;
    List<BaseEntity> mDataList = new ArrayList<>();

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refresh_layout;
    private MainAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private void initList() {

        mAdapter = new MainAdapter(mContext, mDataList);
        mAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onClick(int pos) {
                Intent i = new Intent(mContext, DeviceListActivity.class);

                i.putExtra(Constant.KEY_ENTITY, mAppsList.get(pos));

                i.putExtra(Constant.KEY_APPTYPE_ENTITY, mDataList.get(pos));
                startActivity(i);
            }

            @Override
            public boolean onLongClick(int pos) {

                return false;
            }

            @Override
            public void onCheckedChanged(int pos, boolean b) {

            }

            @Override
            public boolean onCheckedTouch(int pos, MotionEvent motionEvent) {
                return false;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        refresh_layout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_blue_light, android.R.color.holo_orange_light);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mMode = MODE_HAND_REFRESH;
                loadFirstPage();
            }
        });
    }

    int mPage = -1;

    void loadFirstPage() {

        requestData(0);
    }


    private void requestData(final int page) {


        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                List<AppTypeEntity> list = AppTypeManager.getAppTypeList(mContext);
                if (list != null) {
                    if (page == 0)
                        mDataList.clear();

                    mDataList.addAll(list);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        refresh_layout.setRefreshing(false);

                    }
                });

                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        mAppsList.add(new AppsResponse());
                        AppTypeEntity e = list.get(i);
                        e.setOnline_num(-1);
                        requestAliveAppType(e, i);
                    }
                }


                return null;
            }
        };
        task.execute();

    }


    @Override
    protected void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);


        setTitle("ESPUSH");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
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
        if (id == R.id.action_about) {
            toAbout();
            return true;
        }
        if (id == R.id.action_help) {
            toHelp();
            return true;
        }
        if (id == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toHelp() {
        Intent i = new Intent(mContext, HelpActivity.class);
        startActivity(i);
    }

    private void toAbout() {
        Intent i = new Intent(mContext, AboutActivity.class);
        startActivity(i);
    }
}
