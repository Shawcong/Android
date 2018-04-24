package xcong.diarygram10;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 28907 on 2016/9/26.
 */
public class ACAdapter extends BaseAdapter {
    //itemA类的type标志
    private static final int TYPE_A = 0;
    //itemB类的type标志
    private static final int TYPE_B = 1;

    private Context context;
    //整合数据
    private List<Object> data = new ArrayList<>();

    public ACAdapter(Context context,List<Object> as) {
        this.context=context;
        //把数据装载同一个list里面
        //这里把所有数据都转为object类型是为了装载同一个list里面好进行排序
        data.addAll(as);
        //按时间排序来填充数据
    }
    /**
     * 获得itemView的type
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int result = 0;
        if (data.get(position)==null) {
            result = TYPE_B;
        } else {
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
        return 2;
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
        //根据position获得View的type
        int type = getItemViewType(position);
        if (convertView == null) {
            //实例化
            holder1 = new ViewHolder1();
            holder2 = new ViewHolder2();
            //根据不同的type 来inflate不同的item layout
            //然后设置不同的tag
            //这里的tag设置是用的资源ID作为Key
            switch (type) {
                case TYPE_A:
                    convertView = View.inflate(context, R.layout.activity_table, null);
                    holder1.text = (TextView) convertView.findViewById(R.id.text);
                    convertView.setTag(R.id.tag_first, holder1);
                    break;
                case TYPE_B:
                    convertView = View.inflate(context, R.layout.activity_empty, null);
                    convertView.setTag(R.id.tag_second, holder2);
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
            }
        }

        Object o = data.get(position);
        //根据不同的type设置数据
        switch (type) {
            case TYPE_A:
                Data a = (Data) o;
                holder1.text.append(Html.fromHtml("<b>"+a.getTime()+"</b>"));
                holder1.text.append("  ");
                holder1.text.append(Html.fromHtml("<b>"+a.getWeek()+"</b>"));
                holder1.text.append("  /  ");
                holder1.text.append(a.getDescribe());
                break;

            case TYPE_B:
                Data b = (Data) o;
                break;
        }

        return convertView;
    }

    /**
     * item A 的Viewholder
     */
    private static class ViewHolder1 {
        TextView text;
    }

    /**
     * item B 的Viewholder
     */
    private static class ViewHolder2 {
    }



}
