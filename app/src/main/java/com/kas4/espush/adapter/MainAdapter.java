package com.kas4.espush.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kas4.espush.R;
import com.kas4.espush.entity.AppTypeEntity;
import com.kas4.espush.entity.BaseEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/25.
 */
public class MainAdapter extends BaseAdapter  {

    private DisplayImageOptions options;
    private Context mContext;
    List<BaseEntity> itemList;


    public MainAdapter(Context mContext, List itemList) {
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
                    LayoutInflater.from(mContext).inflate(R.layout.item_list_main, parent, false);
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
            AppTypeEntity o = (AppTypeEntity) itemList.get(position);

            h.tv0.setText(o.getName());

            if (o.getOnline_num() == -1) {
                h.tv1.setText("正在加载..");
            } else {
                h.tv1.setText("当前在线：" + o.getOnline_num());
            }

            ImageLoader.getInstance().displayImage("", h.iv_left, options);



            final int pos=position;
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemListener!=null){
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


        public ItemViewHolder(View v) {
            super(v);
            iv_left = (ImageView) v.findViewById(R.id.iv_left);
            tv0 = (TextView) v.findViewById(R.id.tv0);
            tv1 = (TextView) v.findViewById(R.id.tv1);


        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


}


