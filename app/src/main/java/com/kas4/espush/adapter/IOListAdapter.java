package com.kas4.espush.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kas4.espush.R;
import com.kas4.espush.app.Constant;
import com.kas4.espush.entity.BaseEntity;
import com.kas4.espush.entity.IOEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/25.
 */
public class IOListAdapter extends BaseAdapter {


    private DisplayImageOptions options;
    private Context mContext;
    List<BaseEntity> itemList;


    public IOListAdapter(Context mContext, List itemList) {
        this.mContext = mContext;
        this.itemList = itemList;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            View view = new View(mContext);
            return new FooterViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            View view =
                    LayoutInflater.from(mContext).inflate(R.layout.item_list_io, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {

            View view = new View(mContext);
            return new FooterViewHolder(view);
        }
        return null;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder h = (ItemViewHolder) holder;
            IOEntity o = (IOEntity) itemList.get(position);


            ImageLoader.getInstance().displayImage("", h.iv_left, options);

            h.tv0.setText(o.getName());


            final int pos=position;
            //

            h.switchbutton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (onItemListener != null) {
                        return onItemListener.onCheckedTouch(pos,motionEvent);
                    }
                    return false;
                }
            });

//            h.switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                    if (onItemListener != null) {
//                        onItemListener.onCheckedChanged(pos,b);
//                    }
//
////                    Snackbar.make(compoundButton, "Status switched!", Snackbar.LENGTH_LONG).setAction("Confirm", new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            Toast.makeText(
////                                    mContext,
////                                    "Toast comes out",
////                                    Toast.LENGTH_SHORT).show();
////                        }
////                    }).show();
//                }
//            });


            if(o.getEdge()== Constant.ON_IO_EDGE)
                h.switchbutton.setChecked(true);
            else
                h.switchbutton.setChecked(false);


            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemListener != null) {
                        onItemListener.onClick(pos);
                    }
                }
            });



        }

    }

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_EMPTY = 2;

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 1) {
            return TYPE_EMPTY;
        } else if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size() + 1;

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_left;
        public ImageView iv_right;
        public TextView tv0;
        public TextView tv1;
        public TextView tv2;
        SwitchCompat switchbutton;


        public ItemViewHolder(View v) {
            super(v);
            iv_left = (ImageView) v.findViewById(R.id.iv_left);
            tv0 = (TextView) v.findViewById(R.id.tv0);
            switchbutton=(SwitchCompat)v.findViewById(R.id.switchbutton);

        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}


