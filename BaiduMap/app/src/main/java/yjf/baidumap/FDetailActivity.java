package yjf.baidumap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by 聪 on 2016/10/24.
 */
public class FDetailActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detail);
        final People a = (People)getIntent().getSerializableExtra(FriendsActivity.SER_KEY);

        TextView txt_friend_name = (TextView)findViewById(R.id.txt_friend_name);
        txt_friend_name.setText(a.getName());

        TextView txt_friend_number = (TextView)findViewById(R.id.txt_friend_number);
        txt_friend_number.setText(a.getNumber());

        TextView txt_friend_long_lang = (TextView)findViewById(R.id.txt_friend_long_lang);
        txt_friend_long_lang.setText(a.getLatitude()+"/"+a.getLongitude());

        final TextView txt_friend_altitude = (TextView)findViewById(R.id.txt_friend_altitude);
        txt_friend_altitude.setText(a.getAltitude());

        TextView txt_friend_nearest_city = (TextView)findViewById(R.id.txt_friend_nearest_city);
        txt_friend_nearest_city.setText(a.getNear());

        Button btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dlg = new AlertDialog.Builder(FDetailActivity.this).create();
                dlg.show();
                final Window window = dlg.getWindow();
                window.setContentView(R.layout.dialog_delete);
                TextView txt_friend_number = (TextView)window.findViewById(R.id.txt_friend_number);
                txt_friend_number.setText(a.getNumber());
                Button btn_dialog_ok = (Button)window.findViewById(R.id.btn_dialog_ok);
                btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FriendsActivity.util.delete(a.getName());
                        finish();
                    }
                });
            }
        });

    }
}
