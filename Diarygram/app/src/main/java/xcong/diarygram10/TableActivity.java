package xcong.diarygram10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by 28907 on 2016/10/10.
 */
public class TableActivity extends AppCompatActivity {
    static List<Object> Diary = new ArrayList<>();
    private ListView diaryview;
    private xcong.diarygram10.CustomFontTextView monthtxt;
    private xcong.diarygram10.CustomFontTextView yeartxt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monthtxt = (xcong.diarygram10.CustomFontTextView)findViewById(R.id.month);
        yeartxt = (xcong.diarygram10.CustomFontTextView)findViewById(R.id.year);
        Intent intent = this.getIntent();
        String Date;
        DataString s = new DataString();
        final String year = intent.getStringExtra("year");
        final String month = intent.getStringExtra("month");
        monthtxt.setText(s.getMonth(month));
        yeartxt.setText(year);
        String day = "";
        Calendar c = Calendar.getInstance();
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        for (int i = 1; i <= mDay; i++) {
            day = String.valueOf(i);
            Date = year + month + day;
            Diary.add(getObject(Date));
        }
        ACAdapter acAdapter = new ACAdapter(TableActivity.this, Diary);
        diaryview=(ListView)findViewById(R.id.diaryview);
        diaryview.setAdapter(acAdapter);

        TextView addsign=(TextView)findViewById(R.id.addsign);
        addsign.setOnClickListener(listener);

        View changesign=(View)findViewById(R.id.changesign);
        changesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TableActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        diaryview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataString c = new DataString();
                String day = String.valueOf(position+1);
                Calendar cal = Calendar.getInstance();
                String mMonth = c.getMonth(month);
                String strdate=year+"-"+month+"-"+day;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
                java.util.Date date = null;
                try {
                    date = format.parse(strdate);// 将字符串转换为日期
                } catch (ParseException e) {
                    System.out.println("输入的日期格式不合理！");
                }
                String week = getWeek(date);
                if(week.equals("星期一"))
                    week = "Monday";
                else if(week.equals("星期二"))
                    week = "Tuesday";
                else if(week.equals("星期三"))
                    week = "Wednesday";
                else if(week.equals("星期四"))
                    week = "Thursday";
                else if(week.equals("星期五"))
                    week = "Friday";
                else if(week.equals("星期六"))
                    week = "Saturday";
                else if(week.equals("星期日"))
                    week = "Sunday";
                Bundle bundle = new Bundle();
                bundle.putString("date",week + " / " + mMonth + " " + day + " / " + year);
                bundle.putString("week",week);
                bundle.putString("day",day);
                bundle.putString("name",year+month+day);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(TableActivity.this,WriteActivity.class);
                startActivity(intent);
            }
        });

    }

    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            DataString date = new DataString();
            Bundle bundle = new Bundle();
            bundle.putString("date",date.StringData());
            Calendar cal = Calendar.getInstance();
            String year,month,day,week;
            year=String.valueOf(cal.get(Calendar.YEAR));
            week=String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
            month=String.valueOf(cal.get(Calendar.MONTH) + 1);// 获取当前月份
            day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            if ("1".equals(week)) {
                week = "Sunday";
            } else if ("2".equals(week)) {
                week = "Monday";
            } else if ("3".equals(week)) {
                week = "Tuesday";
            } else if ("4".equals(week)) {
                week = "Wednesday";
            } else if ("5".equals(week)) {
                week = "Thursday";
            } else if ("6".equals(week)) {
                week = "Friday";
            } else if ("7".equals(week)) {
                week = "Saturday";
            }
            bundle.putString("week",week);
            bundle.putString("day",day);
            bundle.putString("name",year+month+day);
            Intent intent=new Intent();//
            intent.putExtras(bundle);
            intent.setClass(TableActivity.this, WriteActivity.class);//
            startActivity(intent);//
        }
    };

    public class DataString {
        private String mYear;
        private String mMonth;
        private String mDay;
        private String mWay;
        public String StringData() {
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            if ("1".equals(mWay)) {
                mWay = "Sunday";
            } else if ("2".equals(mWay)) {
                mWay = "Monday";
            } else if ("3".equals(mWay)) {
                mWay = "Tuesday";
            } else if ("4".equals(mWay)) {
                mWay = "Wednesday";
            } else if ("5".equals(mWay)) {
                mWay = "Thursday";
            } else if ("6".equals(mWay)) {
                mWay = "Friday";
            } else if ("7".equals(mWay)) {
                mWay = "Saturday";
            }
            if ("0".equals(mMonth)) {
                mMonth = "January";
            } else if ("1".equals(mMonth)) {
                mYear = "February";
            } else if ("2".equals(mMonth)) {
                mMonth = "March";
            } else if ("3".equals(mMonth)) {
                mMonth = "April";
            } else if ("4".equals(mMonth)) {
                mMonth = "May";
            } else if ("5".equals(mMonth)) {
                mMonth = "June";
            } else if ("6".equals(mMonth)) {
                mMonth = "July";
            } else if ("7".equals(mMonth)) {
                mMonth = "August";
            } else if ("8".equals(mMonth)) {
                mMonth = "September";
            } else if ("9".equals(mMonth)) {
                mMonth = "October";
            } else if ("10".equals(mMonth)) {
                mMonth = "November";
            } else if ("11".equals(mMonth)) {
                mMonth = "December";
            }


            return mWay + " / " + mMonth + " " + mDay + " / " + mYear;
        }
        public String getWeek(String mWay){
            if ("1".equals(mWay)) {
                mWay = "Sunday";
            } else if ("2".equals(mWay)) {
                mWay = "Monday";
            } else if ("3".equals(mWay)) {
                mWay = "Tuesday";
            } else if ("4".equals(mWay)) {
                mWay = "Wednesday";
            } else if ("5".equals(mWay)) {
                mWay = "Thursday";
            } else if ("6".equals(mWay)) {
                mWay = "Friday";
            } else if ("7".equals(mWay)) {
                mWay = "Saturday";
            }
            return mWay;
        }
        public String getMonth(String mMonth){
            if ("0".equals(mMonth)) {
                mMonth = "January";
            } else if ("1".equals(mMonth)) {
                mYear = "February";
            } else if ("2".equals(mMonth)) {
                mMonth = "March";
            } else if ("3".equals(mMonth)) {
                mMonth = "April";
            } else if ("4".equals(mMonth)) {
                mMonth = "May";
            } else if ("5".equals(mMonth)) {
                mMonth = "June";
            } else if ("6".equals(mMonth)) {
                mMonth = "July";
            } else if ("7".equals(mMonth)) {
                mMonth = "August";
            } else if ("8".equals(mMonth)) {
                mMonth = "September";
            } else if ("9".equals(mMonth)) {
                mMonth = "October";
            } else if ("10".equals(mMonth)) {
                mMonth = "November";
            } else if ("11".equals(mMonth)) {
                mMonth = "December";
            }
            return mMonth;
        }
    }//当前日期的读取

    public static String getWeek(Date date) {
        // String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        // Calendar cal = Calendar.getInstance();
        // cal.setTime(date);
        // int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        // if(week_index<0){
        // week_index = 0;
        // }
        // return weeks[week_index];
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(date);
        return week;
    }
    private Object getObject(String name) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = this.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }
    public void onResume(){
        super.onResume();
        List<Object> Diary = new ArrayList<>();
        String Date;
        String year="2016";
        String month="10";
        String day="";
        Calendar c= Calendar.getInstance();
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        for(int i=1;i<=mDay;i++){
            day=String.valueOf(i);
            Date=year+month+day;
            Diary.add(getObject(Date));
        }
        ACAdapter acAdapter = new ACAdapter(TableActivity.this,Diary);
        diaryview = (ListView) findViewById(R.id.diaryview);
        diaryview.setAdapter(acAdapter);
    }
    public void onStop(){
        super.onStop();
        super.finish();
    }
}
