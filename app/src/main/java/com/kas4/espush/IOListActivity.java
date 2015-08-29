package com.kas4.espush;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kas4.espush.adapter.IOListAdapter;
import com.kas4.espush.adapter.OnItemListener;
import com.kas4.espush.app.Constant;
import com.kas4.espush.entity.AppTypeEntity;
import com.kas4.espush.entity.BaseEntity;
import com.kas4.espush.entity.DeviceEntity;
import com.kas4.espush.entity.IOEntity;
import com.kas4.espush.entity.ResultResponse;
import com.kas4.espush.manager.NetAPI;
import com.kas4.espush.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class IOListActivity extends BaseActivity {

    private Toolbar toolbar;
    private AppTypeEntity mAppTypeEnt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initIntent();
        initList();

        if(mAppTypeEnt!=null&&mEnt!=null){

            setTitle(mAppTypeEnt.getName()+"-"+mEnt.getName());
        }

    }

    DeviceEntity mEnt;

    private void initIntent() {
        Intent i = getIntent();
        if (i != null) {
            mEnt = (DeviceEntity) i.getSerializableExtra(Constant.KEY_ENTITY);
            mAppTypeEnt = (AppTypeEntity) i.getSerializableExtra(Constant.KEY_APPTYPE_ENTITY);
            if (mEnt != null) {


            }
        }
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

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refresh_layout;
    private IOListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    void showSnack(final IOEntity e,final int undoEdge, final int pos){
        Snackbar.make(mRecyclerView, "切换已完成", Snackbar.LENGTH_SHORT).setAction("Undo?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSetIO(e,undoEdge,pos, true);
            }
        }).show();
    }

    void requestSetIO(final IOEntity e, final int newedge, final int pos, final boolean isUndo){
        NetAPI.setGpioEdge(mEnt.getDevid(),""+e.getPin(),""+newedge,mAppTypeEnt.getAppid(), mAppTypeEnt.getAppkey(), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                ResultResponse res = (ResultResponse) response;
                byte[] b = res.getResult().getBytes();
                if (b != null&b.length>0) {
                    int result=b[0];
                    if(result==0){//这里0表示成功
                        e.setEdge(newedge);
                        mAdapter.notifyItemChanged(pos);

                        if(!isUndo)
                        {
                            int undoEdge=e.getEdge()==Constant.ON_IO_EDGE?Constant.OFF_IO_EDGE:Constant.ON_IO_EDGE;
                            showSnack(e, undoEdge, pos);
                        }
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast(mContext, "error");
            }
        });
    }
    private void initList() {

        mAdapter = new IOListAdapter(mContext, mDataList);
        mAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onClick(int pos) {

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
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){

                    IOEntity e= (IOEntity) mDataList.get(pos);
                    int newEdge=e.getEdge()==Constant.ON_IO_EDGE?Constant.OFF_IO_EDGE:Constant.ON_IO_EDGE;

                    requestSetIO(e,newEdge,pos,false);
                    return true;

                }
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

    // 0, 1, 2, 3, 4, 5, 6, 7,  8,  9,  10, 11  index
    // 0，1，2，3，4，5， 9，10， 12，13，14，15   gpio pin
    // 0，        4，5， 9，10， 12，13，14，15    show
    static SparseIntArray siaPin=new SparseIntArray();
    static SparseBooleanArray sbaNotShow=new SparseBooleanArray();
    static{
        sbaNotShow.put(1,true);
        sbaNotShow.put(2,true);
        sbaNotShow.put(3,true);

        int i=0;
        siaPin.put(i++,0);
        siaPin.put(i++,1);
        siaPin.put(i++,2);

        siaPin.put(i++,3);
        siaPin.put(i++,4);
        siaPin.put(i++,5);

        siaPin.put(i++,9);
        siaPin.put(i++,10);
        siaPin.put(i++,12);

        siaPin.put(i++,13);
        siaPin.put(i++,14);
        siaPin.put(i++,15);



    }
    private void requestData(final int page) {

        NetAPI.getIOStatus(mEnt.getDevid(), mAppTypeEnt.getAppid(), mAppTypeEnt.getAppkey(), new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                ResultResponse res = (ResultResponse) response;


                byte[] b = res.getResult().getBytes();

                    if (b != null) {
                    if (page == 0)
                        mDataList.clear();

                    for (int i = 0; i < b.length; i++) {

                        if(sbaNotShow.get(i)==true){
                            continue;
                        }

                        IOEntity e = new IOEntity();
                        e.setName("GPIO" +siaPin.get(i));
                        e.setEdge(b[i]);
                        e.setPin(siaPin.get(i));
                        mDataList.add(e);

                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        refresh_layout.setRefreshing(false);

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast(mContext, "error");
            }
        });


    }



    @Override
    protected void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_io;
    }


}
