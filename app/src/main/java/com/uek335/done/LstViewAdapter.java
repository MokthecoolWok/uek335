package com.uek335.done;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class LstViewAdapter extends ArrayAdapter<String> {
    int groupid;
    String[] item_list;
    ArrayList<String> desc;
    Context context;
    public LstViewAdapter(Context context, int vg, int id, String[] item_list){
        super(context, vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;
    }
    static class ViewHolder{
        public TextView textView;
        public Button button;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = convertView;
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textView= (TextView) rowView.findViewById(R.id.txt);
            viewHolder.button=(Button) rowView.findViewById(R.id.bt);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.textView.setText(item_list[position]);
        holder.button.setText(item_list[position]);
        return rowView;
    }
}
