package yjf.baidumap;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yin on 2016/10/21.
 */
public class EnemiesActivity extends Activity{
    ArrayList<People> person;
    private ListView lvw_enemies_list;

    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    ABAdapter myAdapter;
    static DBcHelper util;

    private String flag; // 标识是更新还是添加

    public final static String SER_KEY = "yjf.baidumap.ser";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enemies_list);

        util = new DBcHelper(EnemiesActivity.this);
        person = util.getAllData();

        myAdapter = new ABAdapter(EnemiesActivity.this,person);
        lvw_enemies_list = (ListView) findViewById(R.id.lvw_enemies_list);
        lvw_enemies_list.setAdapter(myAdapter);

        //发短信
        Button btn_enemies_list_radar = (Button)findViewById(R.id.btn_enemies_list_radar);
        btn_enemies_list_radar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SmsManager manager = SmsManager.getDefault();
                for(int i=0;i<person.size();i++) {
                    manager.sendTextMessage(person.get(i).getNumber(), null, "where are you?", null, null);
                }
                Toast.makeText(getApplicationContext(), "发送完毕", Toast.LENGTH_SHORT).show();
            }
        });

        mSMSBroadcastReceiver=new SMSBroadcastReceiver();
        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void OnReceived(String message,String number) {
                String[] ary = message.split("/");
                int i=0;
                while(person.get(i)!=null) {
                    if(person.get(i).getNumber()==number) {
                        if (Integer.parseInt(ary[0]) <= 90 && Integer.parseInt(ary[0]) >= -90)
                            person.get(0).setLatitude(ary[0]);
                        if (Integer.parseInt(ary[1]) <= 180 && Integer.parseInt(ary[1]) >= -180)
                            person.get(0).setLongitude(ary[1]);
                        util.update(person.get(0));
                    }
                    i++;
                }
            }
        });

        //添加按钮事件
        Button btn_enemies_list_add = (Button)findViewById(R.id.btn_enemies_list_add);
        //建立事件
        btn_enemies_list_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog dlg = new AlertDialog.Builder(EnemiesActivity.this).create();
                dlg.show();
                final Window window = dlg.getWindow();
                window.setContentView(R.layout.dialog_add_enemy);

                dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                Button btn_dialog_ok = (Button)window.findViewById(R.id.btn_dialog_ok);
                btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //保存输入的信息
                        People people = new People();
                        EditText txt_enemy_name = (EditText) window.findViewById(R.id.txt_enemy_name);

                        String name=txt_enemy_name.getText().toString();
                        people.setName(name);
                        EditText txt_enemy_number = (EditText)window.findViewById(R.id.txt_enemy_number);
                        String number=txt_enemy_number.getText().toString();
                        people.setNumber(number);
                        person.add(people);
                        //添加进数据库
                        util.insert(people);
                        dlg.cancel();
                        ABAdapter abAdapter = new ABAdapter(EnemiesActivity.this,person);
                        lvw_enemies_list = (ListView) findViewById(R.id.lvw_enemies_list);
                        lvw_enemies_list.setAdapter(abAdapter);
                    }
                });
            }
        });
        lvw_enemies_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = person.get(position).getName();
                String number = person.get(position).getNumber();
                String lat = person.get(position).getLatitude();
                String lon = person.get(position).getLongitude();
                String hig = person.get(position).getAltitude();
                String near = person.get(position).getNear();
                String update = person.get(position).getTime_update();
                flag = "1";

                People a = new People();
                a.setName(name);
                a.setNumber(number);
                a.setLatitude(lat);
                a.setLongitude(lon);
                a.setAltitude(hig);
                a.setNear(near);
                a.setTime_update(update);

                Intent intent = new Intent(EnemiesActivity.this,FDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(SER_KEY,a);
                intent.putExtras(bundle);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });


    }

    public void onResume(){
        super.onResume();
        person = util.getAllData();
        ABAdapter myAdapter = new ABAdapter(EnemiesActivity.this,person);
        lvw_enemies_list = (ListView) findViewById(R.id.lvw_enemies_list);
        lvw_enemies_list.setAdapter(myAdapter);
    }



}

