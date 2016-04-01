package cn.espush.app.adapter;

import android.view.MotionEvent;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/27.
 */
public interface OnItemListener {

    void onClick(int pos);

    boolean onLongClick(int pos);

    void onCheckedChanged(int pos, boolean b);

    boolean onCheckedTouch(int pos, MotionEvent motionEvent);
}
