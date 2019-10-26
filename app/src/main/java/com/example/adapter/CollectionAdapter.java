package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bean.SysCollection;
import com.example.studenteducation.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CollectionAdapter extends ArrayAdapter<SysCollection> {
    private int resourceId;     //item的ID
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public CollectionAdapter(Context context, int resource, List<SysCollection> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SysCollection collection = getItem(position);
//        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.collectionName = (TextView) view.findViewById(R.id.collection_name);
            viewHolder.collectionState = (TextView) view.findViewById(R.id.collection_state);
            view.setTag(viewHolder);    //将viewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();    //重新获取viewHolder
        }
        viewHolder.collectionName.setText(collection.getCollectionName());
        String time = sdf.format(new Date());
        if(time.compareTo(collection.getCollectionEndTime()) > 0) {
            viewHolder.collectionState.setText("已结束");
            viewHolder.collectionState.setBackground(getContext().getResources().getDrawable(R.drawable.collection_red_state));

        } else if(time.compareTo(collection.getCollectionEndTime())<=0 && time.compareTo(collection.getCollectionStartTime())>=0) {
            viewHolder.collectionState.setText("进行中");
            viewHolder.collectionState.setBackground(getContext().getResources().getDrawable(R.drawable.collection_light_state));

        } else {
            viewHolder.collectionState.setText("未开始");
            viewHolder.collectionState.setBackground(getContext().getResources().getDrawable(R.drawable.collection_sky_state));
        }

        return view;
    }

    class ViewHolder {
        TextView collectionName;
        TextView collectionState;
    }

}
