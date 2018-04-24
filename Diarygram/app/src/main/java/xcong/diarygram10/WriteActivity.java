package xcong.diarygram10;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 28907 on 2016/9/24.
 */

public class WriteActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        Intent intent = getIntent();
        final EditText edit = (EditText) findViewById(R.id.edit);
        xcong.diarygram10.CustomFontTextView write_date = (xcong.diarygram10.CustomFontTextView) findViewById(R.id.write_date);
        Bundle bundle = getIntent().getExtras();
        String date = bundle.getString("date");
        String name = bundle.getString("name");
        write_date.setText(date);//日期显示
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        Rect r = new Rect();
        //获取当前界面可视部分
        //警告：对于刚跳到一个新的界面就要弹出软键盘的情况上述代码可能由于界面为加载完全而无法弹出软键盘。此时应该适当的延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(edit, 0);
                           }

                       },
                500);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        edit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
            }
        });

        //读取是否有当天信息
        Data a=(Data)getObject(name);
        if(a!=null){//说明有数据
            edit.setText(a.getDescribe());
        }
        else {
        }
    }

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

    //获取年月日
    public class DataString {
        private String mYear;
        private String mMonth;
        private String mDay;
        private String mWay;

        public String NumberData(){
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            return mYear+mMonth+mDay;
        }
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


            return mWay + " / " + mMonth + " " + mDay + " / " + mYear;
        }
        public String getWeek(){
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
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
            return mWay;
        }
        public String getMonth(){
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
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
        public String getYaer(){
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
            return mYear;
        }
        public String getDay(){
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            return mDay;
        }
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

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        EditText edit = (EditText) findViewById(R.id.edit);
        String context=edit.getText().toString();
        Data sod = new Data();
        Bundle bundle = getIntent().getExtras();
        sod.setWeek(bundle.getString("week"));
        sod.setName(bundle.getString("name"));
        sod.setTime(bundle.getString("day"));
        sod.setDescribe(context);
        saveObject(sod.getName(),sod);
    }
}
