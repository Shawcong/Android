package xcong.diarygram10;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.Date;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener{
    static List<Object> Diary = new ArrayList<>();
    static List<Object>Week_ans = new ArrayList<>();
    private ListView diaryview;
    private xcong.diarygram10.CustomFontTextView monthtxt;
    private xcong.diarygram10.CustomFontTextView yeartxt;
    private Button button;
    private int click_num=0;
    private View selectsign;
    android.app.AlertDialog.Builder alert;
    int postion_del;//要删除的位置
    /** Called when the activity is first created. */ @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monthtxt = (xcong.diarygram10.CustomFontTextView)findViewById(R.id.month);
        yeartxt = (xcong.diarygram10.CustomFontTextView)findViewById(R.id.year);
        selectsign=(View)findViewById(R.id.selectsign);
        String Date;
        DataString s = new DataString();
        Calendar c= Calendar.getInstance();
        final String year=String.valueOf(c.get(Calendar.YEAR));
        final String month=String.valueOf(c.get(Calendar.MONTH)+1);
        monthtxt.setText(s.getMonth(month));
        yeartxt.setText(year);
        String day="";
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        for(int i=1;i<=mDay;i++){
            day=String.valueOf(i);
            Date=(String)yeartxt.getText()+monthtxt.getText()+day;
            Diary.add(getObject(Date));

            String strdate=year+"-"+month+"-"+day;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
            Date date = null;
            try {
                date = format.parse(strdate);// 将字符串转换为日期
            } catch (ParseException e) {
                System.out.println("输入的日期格式不合理！");
            }
            String week = getWeek(date);//判断是周六还是周日
            String Saturday = "星期六";
            String Sunday = "星期日";
            String a = "1";
            String b = "0";
            if(week.equals("星期六") || week.equals("星期日")) {
                Week_ans.add(a);
            }
            else {
                Week_ans.add(b);
            }
        }
        ABAdapter abAdapter = new ABAdapter(MainActivity.this,Diary,Week_ans);
        diaryview = (ListView) findViewById(R.id.diaryview);
        diaryview.setAdapter(abAdapter);


       // diaryview = (ListView) findViewById(R.id.diaryview);/*定义一个动态数组*/
       // ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();/*在数组中存放数据*/
        //.for(int i=0;i<8;i++)
       // {
        //    HashMap<String, Object> map = new HashMap<String, Object>();
       //     map.put("ItemImage", "日记内容");
       //     map.put("ItemTitle", i);
       //     map.put("ItemText", i);
      //      listItem.add(map);
       // }

      //  SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
       //         R.layout.item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
      //          new String[] {"ItemImage","ItemTitle", "ItemText"},new int[]{R.id.ItemImage,R.id.ItemTitle,R.id.ItemText}
      //  );
        //diaryview.setAdapter(mSimpleAdapter);//为ListView绑定适配器

        //为底部导航栏加号加载新页面
        TextView addsign=(TextView)findViewById(R.id.addsign);
        addsign.setOnClickListener(listener);

        selectsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();//
                intent.setClass(MainActivity.this, BaseActivity.class);//
                startActivity(intent);//
            }
        });

        View changesign=(View)findViewById(R.id.changesign);
        changesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click_num == 0) {
                    int num = 0;
                    if (monthtxt.getText() == "January") {
                        num = 1;
                    } else if (monthtxt.getText() == "February") {
                        num = 1;
                    } else if (monthtxt.getText() == "March") {
                        num = 2;
                    } else if (monthtxt.getText() == "April") {
                        num = 3;
                    } else if (monthtxt.getText() == "May") {
                        num = 4;
                    } else if (monthtxt.getText() == "June") {
                        num = 5;
                    } else if (monthtxt.getText() == "July") {
                        num = 6;
                    } else if (monthtxt.getText() == "August") {
                        num = 7;
                    } else if (monthtxt.getText() == "September") {
                        num = 8;
                    } else if (monthtxt.getText() == "October") {
                        num = 9;
                    } else if (monthtxt.getText() == "November") {
                        num = 10;
                    } else if (monthtxt.getText() == "December") {
                        num = 11;
                    }
                    String day = "";
                    String Date;
                    List<Object> DDD = new ArrayList<>();
                    Calendar cal = Calendar.getInstance();
                    cal.clear();
                    cal.set(Calendar.YEAR, Integer.parseInt((String) yeartxt.getText()));
                    cal.set(Calendar.MONTH, num);//Java月份才0开始算
                    int mDay = cal.getActualMaximum(Calendar.DATE);// 获取当前月份的日期号码
                    for (int i = 1; i <= mDay; i++) {
                        day = String.valueOf(i);
                        Date = (String) yeartxt.getText() + monthtxt.getText() + day;
                        DDD.add(getObject(Date));
                    }
                    ACAdapter abAdapter = new ACAdapter(MainActivity.this, DDD);
                    diaryview = (ListView) findViewById(R.id.diaryview);
                    diaryview.setAdapter(abAdapter);
                    click_num++;
                }
                else{
                    onResume();
                    click_num=0;
                }
            }
        });


        diaryview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataString c = new DataString();
                String day = String.valueOf(position+1);
                Calendar cal = Calendar.getInstance();
                String mMonth = (String)monthtxt.getText();
                String strdate=yeartxt.getText()+"-"+month+"-"+day;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
                Date date = null;
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
                int num = 0;
                if (monthtxt.getText() == "January") {
                    num = 0;
                } else if (monthtxt.getText() == "February") {
                    num = 1;
                } else if (monthtxt.getText() == "March") {
                    num = 2;
                } else if (monthtxt.getText() == "April") {
                    num = 3;
                } else if (monthtxt.getText() == "May") {
                    num = 4;
                } else if (monthtxt.getText() == "June") {
                    num = 5;
                } else if (monthtxt.getText() == "July") {
                    num = 6;
                } else if (monthtxt.getText() == "August") {
                    num = 7;
                } else if (monthtxt.getText() == "September") {
                    num = 8;
                } else if (monthtxt.getText() == "October") {
                    num = 9;
                } else if (monthtxt.getText() == "November") {
                    num = 10;
                } else if (monthtxt.getText() == "December") {
                    num = 11;
                }
                Bundle bundle = new Bundle();
                bundle.putString("date",week + " / " + mMonth + " " + day + " / " + yeartxt.getText());
                bundle.putString("week",week);
                bundle.putString("day",day);
                bundle.putString("name",(String)yeartxt.getText()+monthtxt.getText()+day);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,WriteActivity.class);
                startActivity(intent);
            }
        });

        diaryview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int postion, long arg3) {
                // TODO Auto-generated method stub
                // When clicked, show a toast with the TextView text
                alert = new android.app.AlertDialog.Builder(MainActivity.this);
                alert.setTitle("确定要删除么？");
                alert.setNeutralButton("删除",MainActivity.this);
                alert.setNegativeButton("取消",MainActivity.this);
                alert.create().show();
                postion_del=postion;
                return true;
            }
        });



        monthtxt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.icon);
                builder.setTitle("选择一个月份");
                //    指定下拉列表的显示数据
                final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
                //    设置一个下拉的列表选择项
                builder.setItems(months, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (year == yeartxt.getText() && which + 1 > Integer.parseInt(month)) {
                        }
                        else if (year == yeartxt.getText() && which + 1 == Integer.parseInt(month)) {
                            Toast.makeText(MainActivity.this, "选择的月份为：" + months[which], Toast.LENGTH_SHORT).show();
                            monthtxt.setText(months[which]);
                            String day = "";
                            String Date;
                            List<Object> DDD = new ArrayList<>();
                            List<Object> WWW = new ArrayList<>();

                            Calendar cal = Calendar.getInstance();
                            String mDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码

                            for (int i = 1; i <= Integer.parseInt(mDay); i++) {
                                day = String.valueOf(i);
                                Date = (String) yeartxt.getText() + monthtxt.getText() + day;
                                DDD.add(getObject(Date));

                                String strdate = (String) yeartxt.getText() + "-" + String.valueOf(which + 1) + "-" + day;
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
                                Date date = null;
                                try {
                                    date = format.parse(strdate);// 将字符串转换为日期
                                } catch (ParseException e) {
                                    System.out.println("输入的日期格式不合理！");
                                }
                                String week = getWeek(date);//判断是周六还是周日
                                if(week.equals("星期六") || week.equals("星期日"))
                                    WWW.add("1");
                                else
                                    WWW.add("0");
                            }
                            ABAdapter abAdapter = new ABAdapter(MainActivity.this, DDD, WWW);
                            diaryview = (ListView) findViewById(R.id.diaryview);
                            diaryview.setAdapter(abAdapter);
                        } else {
                            Toast.makeText(MainActivity.this, "选择的月份为：" + months[which], Toast.LENGTH_SHORT).show();
                            monthtxt.setText(months[which]);
                            String day = "";
                            String Date;
                            List<Object> DDD = new ArrayList<>();
                            List<Object> WWW = new ArrayList<>();

                            Calendar cal = Calendar.getInstance();
                            cal.clear();
                            cal.set(Calendar.YEAR, Integer.parseInt((String) yeartxt.getText()));
                            cal.set(Calendar.MONTH, which);//Java月份才0开始算
                            int mDay = cal.getActualMaximum(Calendar.DATE);// 获取当前月份的日期号码

                            for (int i = 1; i <= mDay; i++) {
                                day = String.valueOf(i);
                                Date = (String) yeartxt.getText() + monthtxt.getText() + day;
                                DDD.add(getObject(Date));

                                String strdate = (String) yeartxt.getText() + "-" + String.valueOf(which + 1) + "-" + day;
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
                                Date date = null;
                                try {
                                    date = format.parse(strdate);// 将字符串转换为日期
                                } catch (ParseException e) {
                                    System.out.println("输入的日期格式不合理！");
                                }
                                String week = getWeek(date);//判断是周六还是周日
                                if(week.equals("星期六") || week.equals("星期日"))
                                    WWW.add("1");
                                else
                                    WWW.add("0");
                            }
                            ABAdapter abAdapter = new ABAdapter(MainActivity.this, DDD, WWW);
                            diaryview = (ListView) findViewById(R.id.diaryview);
                            diaryview.setAdapter(abAdapter);
                        }
                    }
                });
                builder.show();
            }
        });

        yeartxt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.icon);
                builder.setTitle("选择一个年份");
                //指定下拉列表的显示数据
                final String[] years = new String[Integer.parseInt(year)-1995+1];
                for( int i = Integer.parseInt(year) , j = 0; j<Integer.parseInt(year)-1995+1; i--,j++){
                    years[j] = String.valueOf(i);
                }
                //    设置一个下拉的列表选择项
                builder.setItems(years, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(MainActivity.this, "选择的年份为：" + years[which], Toast.LENGTH_SHORT).show();
                        yeartxt.setText(years[which]);
                        int num = 0;
                        if(monthtxt.getText() == "January"){
                            num=0;
                        }
                        else if(monthtxt.getText() == "February"){
                            num=1;
                        }
                        else if(monthtxt.getText() == "March"){
                            num=2;
                        }
                        else if(monthtxt.getText() == "April"){
                            num=3;
                        }
                        else if(monthtxt.getText() == "May"){
                            num=4;
                        }
                        else if(monthtxt.getText() == "June"){
                            num=5;
                        }
                        else if(monthtxt.getText() == "July"){
                            num=6;
                        }
                        else if(monthtxt.getText() == "August"){
                            num=7;
                        }
                        else if(monthtxt.getText() == "September"){
                            num=8;
                        }
                        else if(monthtxt.getText() == "October"){
                            num=9;
                        }
                        else if(monthtxt.getText() == "November"){
                            num=10;
                        }
                        else if(monthtxt.getText() == "December"){
                            num=11;
                        }
                        String day="";
                        String Date;
                        List<Object> DDD = new ArrayList<>();
                        List<Object> WWW = new ArrayList<>();
                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        cal.set(Calendar.YEAR,Integer.parseInt((String)yeartxt.getText()));
                        cal.set(Calendar.MONTH, num);//Java月份才0开始算
                        int mDay = cal.getActualMaximum(Calendar.DATE);// 获取当前月份的日期号码
                        for(int i=1;i<=mDay;i++){
                            day=String.valueOf(i);
                            Date=(String)yeartxt.getText()+monthtxt.getText()+day;
                            DDD.add(getObject(Date));

                            String strdate=(String) yeartxt.getText()+"-"+String.valueOf(num+1)+"-"+day;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
                            Date date = null;
                            try {
                                date = format.parse(strdate);// 将字符串转换为日期
                            } catch (ParseException e) {
                                System.out.println("输入的日期格式不合理！");
                            }
                            String week = getWeek(date);//判断是周六还是周日
                            if(week.equals("星期六") || week.equals("星期日"))
                                WWW.add("1");
                            else
                                WWW.add("0");
                        }
                        ABAdapter abAdapter = new ABAdapter(MainActivity.this,DDD,WWW);
                        diaryview = (ListView) findViewById(R.id.diaryview);
                        diaryview.setAdapter(abAdapter);
                    }
                });
                builder.show();
            }
        });

    }

    public void onClick(DialogInterface dialog, int which){
        if(which==-3){
            String year = (String)yeartxt.getText();
            String day = String.valueOf(postion_del+1);
            int num = 0;
            if(monthtxt.getText() == "January"){
                num=0;
            }
            else if(monthtxt.getText() == "February"){
                num=1;
            }
            else if(monthtxt.getText() == "March"){
                num=2;
            }
            else if(monthtxt.getText() == "April"){
                num=3;
            }
            else if(monthtxt.getText() == "May"){
                num=4;
            }
            else if(monthtxt.getText() == "June"){
                num=5;
            }
            else if(monthtxt.getText() == "July"){
                num=6;
            }
            else if(monthtxt.getText() == "August"){
                num=7;
            }
            else if(monthtxt.getText() == "September"){
                num=8;
            }
            else if(monthtxt.getText() == "October"){
                num=9;
            }
            else if(monthtxt.getText() == "November"){
                num=10;
            }
            else if(monthtxt.getText() == "December"){
                num=11;
            }
            delObject(year+monthtxt.getText()+day);
            Log.i("55879462","222254894");
            onResume();
        }
        else{

        }
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
            month=String.valueOf(cal.get(Calendar.MONTH)+1);// 获取当前月份
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
            bundle.putString("name",year+date.getMonth(month)+day);
            Intent intent=new Intent();//
            intent.putExtras(bundle);
            intent.setClass(MainActivity.this, WriteActivity.class);//
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
            mMonth = String.valueOf(c.get(Calendar.MONTH));// 获取当前月份
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
            if ("1".equals(mMonth)) {
                mMonth = "January";
            } else if ("2".equals(mMonth)) {
                mYear = "February";
            } else if ("3".equals(mMonth)) {
                mMonth = "March";
            } else if ("4".equals(mMonth)) {
                mMonth = "April";
            } else if ("5".equals(mMonth)) {
                mMonth = "May";
            } else if ("6".equals(mMonth)) {
                mMonth = "June";
            } else if ("7".equals(mMonth)) {
                mMonth = "July";
            } else if ("8".equals(mMonth)) {
                mMonth = "August";
            } else if ("9".equals(mMonth)) {
                mMonth = "September";
            } else if ("10".equals(mMonth)) {
                mMonth = "October";
            } else if ("11".equals(mMonth)) {
                mMonth = "November";
            } else if ("12".equals(mMonth)) {
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
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
        String week = sdf.format(date);
        return week;
    }

    //读取对象
    private Object getObject(String name){
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
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
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

    private void saveObject(String name,Data sod){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = this.openFileOutput(name, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(sod);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }

    public void delObject(String name){
        Data a = null;
        saveObject(name,a);
    }

    public void onResume(){
        super.onResume();
        List<Object> Diary = new ArrayList<>();
        List<Object> WWW = new ArrayList<>();
        String Date;
        int num = 0;
        if(monthtxt.getText() == "January"){
            num=0;
        }
        else if(monthtxt.getText() == "February"){
            num=1;
        }
        else if(monthtxt.getText() == "March"){
            num=2;
        }
        else if(monthtxt.getText() == "April"){
            num=3;
        }
        else if(monthtxt.getText() == "May"){
            num=4;
        }
        else if(monthtxt.getText() == "June"){
            num=5;
        }
        else if(monthtxt.getText() == "July"){
            num=6;
        }
        else if(monthtxt.getText() == "August"){
            num=7;
        }
        else if(monthtxt.getText() == "September"){
            num=8;
        }
        else if(monthtxt.getText() == "October"){
            num=9;
        }
        else if(monthtxt.getText() == "November"){
            num=10;
        }
        else if(monthtxt.getText() == "December"){
            num=11;
        }

        String day="";
        Calendar cal = Calendar.getInstance();
        String year=String.valueOf(cal.get(Calendar.YEAR));
        String month=String.valueOf(cal.get(Calendar.MONTH)+1);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        if(yeartxt.getText().equals(year) && Integer.parseInt(month) == (num+1)) {

            for (int i = 1; i <= mDay; i++) {
                day = String.valueOf(i);
                Date = (String) yeartxt.getText() + monthtxt.getText() + day;
                Diary.add(getObject(Date));

                String strdate = (String) yeartxt.getText() + "-" + String.valueOf(num + 1) + "-" + day;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
                Date date = null;
                try {
                    date = format.parse(strdate);// 将字符串转换为日期
                } catch (ParseException e) {
                    System.out.println("输入的日期格式不合理！");
                }
                String week = getWeek(date);//判断是周六还是周日
                if(week.equals("星期六") || week.equals("星期日"))
                    WWW.add("1");
                else
                    WWW.add("0");
            }
            if(click_num==0) {
                ABAdapter abAdapter = new ABAdapter(MainActivity.this, Diary, WWW);
                diaryview = (ListView) findViewById(R.id.diaryview);
                diaryview.setAdapter(abAdapter);
            }
            else{
                ACAdapter abAdapter = new ACAdapter(MainActivity.this, Diary);
                diaryview = (ListView) findViewById(R.id.diaryview);
                diaryview.setAdapter(abAdapter);
            }
        }
        else {
            cal.clear();
            cal.set(Calendar.YEAR, Integer.parseInt((String) yeartxt.getText()));
            cal.set(Calendar.MONTH, num);//Java月份才0开始算
            mDay = cal.getActualMaximum(Calendar.DATE);// 获取当前月份的日期号码
            for (int i = 1; i <= mDay; i++) {
                day = String.valueOf(i);
                Date = (String) yeartxt.getText() + monthtxt.getText() + day;
                Diary.add(getObject(Date));

                String strdate = (String) yeartxt.getText() + "-" + String.valueOf(num + 1) + "-" + day;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
                Date date = null;
                try {
                    date = format.parse(strdate);// 将字符串转换为日期
                } catch (ParseException e) {
                    System.out.println("输入的日期格式不合理！");
                }
                String week = getWeek(date);//判断是周六还是周日
                if(week.equals("星期六") || week.equals("星期日"))
                    WWW.add("1");
                else
                    WWW.add("0");
            }
            if(click_num==0) {
                ABAdapter abAdapter = new ABAdapter(MainActivity.this, Diary, WWW);
                diaryview = (ListView) findViewById(R.id.diaryview);
                diaryview.setAdapter(abAdapter);
            }
            else{
                ACAdapter abAdapter = new ACAdapter(MainActivity.this, Diary);
                diaryview = (ListView) findViewById(R.id.diaryview);
                diaryview.setAdapter(abAdapter);
            }
        }
    }

    public void onStop(){
        super.onStop();
    }
}
