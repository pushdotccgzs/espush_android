package com.kas4.espush;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kas4.espush.adapter.DeviceListAdapter;
import com.kas4.espush.adapter.OnItemListener;
import com.kas4.espush.app.Constant;
import com.kas4.espush.entity.AppTypeEntity;
import com.kas4.espush.entity.AppsResponse;
import com.kas4.espush.entity.BaseEntity;
import com.kas4.espush.entity.DeviceEntity;
import com.kas4.espush.entity.ResultResponse;
import com.kas4.espush.manager.NetAPI;
import com.kas4.espush.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class DeviceListActivity extends BaseActivity {

    private Toolbar toolbar;
    private AppTypeEntity mAppTypeEnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initIntent();
        initList();

        if (mAppTypeEnt != null)
            setTitle(mAppTypeEnt.getName());

    }

    AppsResponse mEnt;

    private void initIntent() {
        Intent i = getIntent();
        if (i != null) {
            mEnt = (AppsResponse) i.getSerializableExtra(Constant.KEY_ENTITY);
            mAppTypeEnt = (AppTypeEntity) i.getSerializableExtra(Constant.KEY_APPTYPE_ENTITY);
            if (mEnt != null) {
                mDataList.clear();
                mDataList.addAll(mEnt.getOnline());

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadFirstPage();
    }

    final int MODE_FIRST_LOAD = 1;
    final int MODE_HAND_REFRESH = 1 << 1;


    int mMode = MODE_FIRST_LOAD;
    List<BaseEntity> mDataList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refresh_layout;
    private DeviceListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private void initList() {

        mAdapter = new DeviceListAdapter(mContext, mDataList);
        mAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onClick(int pos) {
                Intent i = new Intent(mContext, IOListActivity.class);

                i.putExtra(Constant.KEY_ENTITY, mDataList.get(pos));

                i.putExtra(Constant.KEY_APPTYPE_ENTITY, mAppTypeEnt);
                startActivity(i);
            }

            @Override
            public boolean onLongClick(int pos) {
                showLongDialog(pos);
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
        refresh_layout.setEnabled(false );
//        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                mMode = MODE_HAND_REFRESH;
//                loadFirstPage();
//            }
//        });
    }

    int mPage = -1;

    void loadFirstPage() {

        requestData(0);
    }


    void showLongDialog(final int pos) {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("开发者选项")
                .setItems(R.array.array_device_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                showTextInputDialog(pos, Constant.TYPE_MSG_FORMAT_AT);
                                break;
                            case 1:
                                showTextInputDialog(pos, Constant.TYPE_MSG_FORMAT_LUA);
                                break;
                            case 2:
                                requestRefresh(pos);

                                break;
                        }

                    }
                })
                .create();
        dialog.show();
    }

    private void requestRefresh(final int pos) {

        DeviceEntity e = (DeviceEntity) mDataList.get(pos);

        showProgressDialog(null, "Loading..");

        NetAPI.refreshDevice(e.getDevid(), mAppTypeEnt.getAppid(), mAppTypeEnt.getAppkey(), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                ResultResponse res = (ResultResponse) response;
                if (res != null) {
                    ToastUtil.showShortToast(mContext, res.getStatus());

                    if (res.getStatus().equals(Constant.ONLINE_DEVICE)) {

                    } else {
                        mDataList.remove(pos);
                        mAdapter.notifyItemRemoved(pos);
                    }

                }
                cancelProgressDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast(mContext, "error");

                cancelProgressDialog();
            }
        });


    }

    void requestPushMsg(int pos, String type, String msg) {

        DeviceEntity e = (DeviceEntity) mDataList.get(pos);

        NetAPI.pushMessage(e.getDevid(), type, msg, mAppTypeEnt.getAppid(), mAppTypeEnt.getAppkey(), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                ResultResponse res = (ResultResponse) response;

                ToastUtil.showShortToast(getApplicationContext(), "" + res.getMsg());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast(mContext, "error");
            }
        });


    }


    private void showTextInputDialog(final int pos, final String type) {

        String title = "发送运程" + type + "指令";

        LayoutInflater factory = LayoutInflater.from(mContext);
        final View textEntryView = factory.inflate(R.layout.dialog_text_input, null);

        final EditText et = (EditText) textEntryView.findViewById(R.id.et_message);

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setView(textEntryView)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String msg = et.getEditableText().toString();
                        requestPushMsg(pos, type, msg);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.cancel();
                    }
                })
                .create();
        dialog.show();
    }


    private void requestData(int page) {


        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        refresh_layout.setRefreshing(false);

                    }
                });


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


    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_device;
    }


}
