package com.uek335.done;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.uek335.done.model.Task;

import java.util.ArrayList;
import java.util.List;

public class LstViewAdapter extends ArrayAdapter<String> {
    int groupid;
    Task[] taskList;
    ArrayList<String> desc;
    Context context;
    public LstViewAdapter(Context context, int vg, int id, Task[] taskList, String[] taskTitel){
        super(context, vg, id, taskTitel);
        this.context=context;
        groupid=vg;
        this.taskList = taskList;
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
        holder.textView.setText(taskList[position].getTitle());
        holder.textView.setTextColor(Color.BLACK);
        holder.textView.setId(taskList[position].getId());
        holder.button.setId(taskList[position].getId());
        switch(taskList[position].getCategory()){
            case 0:
                rowView.setBackgroundColor(Color.parseColor("#77dd77"));
                break;
            case 1:
                rowView.setBackgroundColor(Color.parseColor("#ff8b3d"));
                break;
            case 2:
                rowView.setBackgroundColor(Color.parseColor("#59bfff"));
        }

        return rowView;
    }
}
