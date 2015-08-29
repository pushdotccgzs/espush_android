package com.kas4.espush.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kas4.espush.R;
import com.kas4.espush.entity.BaseEntity;
import com.kas4.espush.entity.DeviceEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/25.
 */
public class DeviceListAdapter extends BaseAdapter {


    private DisplayImageOptions options;
    private Context mContext;
    List<BaseEntity> itemList;


    public DeviceListAdapter(Context mContext, List itemList) {
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
                    LayoutInflater.from(mContext).inflate(R.layout.item_list_device, parent, false);
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
            DeviceEntity o = (DeviceEntity) itemList.get(position);


            ImageLoader.getInstance().displayImage("", h.iv_left, options);

            h.tv0.setText(o.getName());


            if(position==itemList.size()-1){
                h.view_line_bottom.setVisibility(View.GONE);
            }


            final int pos=position;
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemListener != null) {
                        onItemListener.onClick(pos);
                    }
                }
            });
            h.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (onItemListener != null) {
                        return onItemListener.onLongClick(pos);
                    }

                    return false;
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
        View view_line_bottom;


        public ItemViewHolder(View v) {
            super(v);
            iv_left = (ImageView) v.findViewById(R.id.iv_left);
            tv0 = (TextView) v.findViewById(R.id.tv0);
            view_line_bottom=(View)v.findViewById(R.id.view_line_bottom);

        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}


