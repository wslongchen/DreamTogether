package com.example.mrpan.dreamtogether.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by mrpan on 16/3/26.
 */
public class TimeLineAdapter extends BaseAdapter {

    private List<Dream> datas;

    private Context context;

    private String year;

    public TimeLineAdapter(List<Dream> datas,Context context) {
        this.datas=datas;
        this.context=context;
    }

    @Override
    public int getCount() {
        return datas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.user_dreamlist_item2, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Dream dream = datas.get(position);
        viewHolder.dream_content.setText(dream.getPost_content());
        String time = dream.getPost_date();
        Date date = DateUtils.StrToDate(time);

        viewHolder.timeHour.setText(date.getHours() + ":" + date.getMinutes());
        viewHolder.timeMonth_day.setText(DateUtils.getShortSpellMonth(date) + date.getDay());
        String y = (new SimpleDateFormat("yyyy")).format(date);
        if (position == 0) {
            year = y;
            viewHolder.timeYear.setVisibility(View.VISIBLE);
            viewHolder.timeYear.setText(year + "");
        } else {
            if (Integer.parseInt(year) - Integer.parseInt(y) == 1) {
                year = y;
                viewHolder.timeYear.setVisibility(View.VISIBLE);
                viewHolder.timeYear.setText(year + "");
            } else {
                viewHolder.timeYear.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder{
        public TextView timeYear;
        public TextView timeMonth_day;
        public TextView timeHour;
        public TextView dream_content;
        public ImageView time_point;
        public TextView time_line;

        public ViewHolder(View v){
            dream_content= (TextView) v.findViewById(R.id.dream_recent_content);
            //time_line=(TextView)v.findViewById(R.id.line);
            //time_point=(ImageView)v.findViewById(R.id.recent_dream_head);
            timeMonth_day=(TextView)v.findViewById(R.id.dream_recent_date_month_day);
            timeHour=(TextView)v.findViewById(R.id.dream_recent_date_hour);
            timeYear=(TextView)v.findViewById(R.id.dream_recent_date_year);
        }
    }
}
