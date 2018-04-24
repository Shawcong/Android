package xcong.diarygram10;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 28907 on 2016/9/26.
 */
public class ABAdapter extends BaseAdapter {
    //itemA类的type标志
    private static final int TYPE_A = 0;
    //itemB类的type标志
    private static final int TYPE_B = 1;
    //itemC类的type标志
    private static final int TYPE_C = 2;
    private Context context;
    //整合数据
    private List<Object> data = new ArrayList<>();
    private List<Object> week_ans = new ArrayList<>();
    public ABAdapter(Context context, List<Object> as, List<Object> bs) {
        this.context=context;
        //把数据装载同一个list里面
        //这里把所有数据都转为object类型是为了装载同一个list里面好进行排序
        data.addAll(as);
        //按时间排序来填充数据
        week_ans.addAll(bs);
    }
    /**
     * 获得itemView的type
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int result = 0;
        String a= "1";
        String b= "0";
        if (((String)week_ans.get(position)).equals(b) && data.get(position)==null) {
            result = TYPE_B;
            //Log.i("APP.TAG","0");
        } else if(((String)week_ans.get(position)).equals(a) && data.get(position)==null){
            result = TYPE_C;
            //Log.i("APP.TAG","1");
        }
        else{
            result = TYPE_A;
        }
        return result;
    }

    /**
     * 获得有多少中view type
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建两种不同种类的viewHolder变量
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        //根据position获得View的type
        int type = getItemViewType(position);
        if (convertView == null) {
            //实例化
            holder1 = new ViewHolder1();
            holder2 = new ViewHolder2();
            holder3 = new ViewHolder3();
            //根据不同的type 来inflate不同的item layout
            //然后设置不同的tag
            //这里的tag设置是用的资源ID作为Key
            switch (type) {
                case TYPE_A:
                    convertView = View.inflate(context, R.layout.item, null);
                    holder1.title = (TextView) convertView.findViewById(R.id.ItemTitle);
                    holder1.time = (TextView) convertView.findViewById(R.id.ItemText);
                    holder1.describe = (TextView) convertView.findViewById(R.id.ItemImage);
                    convertView.setTag(R.id.tag_first, holder1);
                    break;
                case TYPE_B:
                    convertView = View.inflate(context, R.layout.item2, null);
                    convertView.setTag(R.id.tag_second, holder2);
                    break;
                case TYPE_C:
                    convertView = View.inflate(context, R.layout.item3, null);
                    convertView.setTag(R.id.tag_third, holder3);
                    break;
            }

        } else {
            //根据不同的type来获得tag
            switch (type) {
                case TYPE_A:
                    holder1 = (ViewHolder1) convertView.getTag(R.id.tag_first);
                    break;
                case TYPE_B:
                    holder2 = (ViewHolder2) convertView.getTag(R.id.tag_second);
                    break;
                case TYPE_C:
                    holder3 = (ViewHolder3) convertView.getTag(R.id.tag_third);
                    break;
            }
        }

        Object o = data.get(position);
        //根据不同的type设置数据
        switch (type) {
            case TYPE_A:
                Data a = (Data) o;
                holder1.describe.setText(a.getDescribe());
                holder1.time.setText(a.getTime());
                holder1.title.setText(a.getWeek());
                break;

            case TYPE_B:
                Data b = (Data) o;
                break;

            case TYPE_C:
                Data c = (Data) o;
                break;
        }

        return convertView;
    }

    /**
     * item A 的Viewholder
     */
    private static class ViewHolder1 {
        TextView time;
        TextView describe;
        TextView title;
    }

    /**
     * item B 的Viewholder
     */
    private static class ViewHolder2 {
    }

    private static class ViewHolder3 {
    }

}
