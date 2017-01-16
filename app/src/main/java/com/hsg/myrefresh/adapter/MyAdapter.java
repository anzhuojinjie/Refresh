package com.hsg.myrefresh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hsg.myrefresh.R;
import com.hsg.myrefresh.bean.Student;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by Joe on 2017/1/10.
 */

public class MyAdapter extends BaseAdapter {
    private List<Student> datas;
    private LayoutInflater inflater;
    public MyAdapter(Context context, List<Student> datas) {
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Student> mList){
        this.datas = mList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.lv_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Student student = datas.get(position);
        viewHolder.getTv1().setText(student.getName());
        viewHolder.getTv2().setText(student.getNum());
        viewHolder.getRlItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener){
                    onItemClickListener.onItemClickListener(position);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder{
        private TextView tv1;
        private TextView tv2;
        private RelativeLayout rlItem;
        private View root;

        public ViewHolder(View root) {
            this.root = root;
        }
        public RelativeLayout getRlItem(){
            if (rlItem == null && root != null){
                rlItem = (RelativeLayout) root.findViewById(R.id.item_rl);
            }
            return rlItem;
        }
        public TextView getTv1(){
            if (tv1 == null && root != null){
                tv1 = (TextView) root.findViewById(R.id.item_name);
            }
            return tv1;
        }
        public TextView getTv2(){
            if (tv2 == null && root != null){
                tv2 = (TextView) root.findViewById(R.id.item_num);
            }
            return tv2;
        }
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        public void onItemClickListener(int position);
    }
}
