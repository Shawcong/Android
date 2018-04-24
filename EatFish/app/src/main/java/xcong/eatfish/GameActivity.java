package xcong.eatfish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by 聪 on 2016/11/10.
 */
public class GameActivity extends Activity implements DialogInterface.OnClickListener{
    GameView view=null;
    private EndView endView;
    private GameView gameView;

    EditText inputServer;
    AlertDialog.Builder alert;
    String name;
    People person = new People();
    int score;

    ListView list;
    static ArrayList<People> people ;//排行榜
    static DBbHelper util;
    ABAdapter myAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                toGameView();
            } else if(msg.what == 1){
                toPass(msg.arg1);
            }
            else if(msg.what == 2){
                toRankView();
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Bundle bundle = this.getIntent().getExtras();
        util = new DBbHelper(GameActivity.this);
        people = util.getAllData();
        int key = bundle.getInt("key");
        if(key==0) {
            toGameView();
        }
        else {
            toRankView();
        }
    }
    protected void onRestart() {
        super.onRestart();
    }
    protected void onStop(){
        super.onStop();
    }

    //getter和setter方法
    public Handler getHandler() {
        return handler;
    }
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    //显示游戏的主界面
    public void toGameView(){
        if(gameView == null){
            gameView = new GameView(this);
        }
        setContentView(gameView);
        endView = null;
    }

    public void toPass(int score){
        alert = new AlertDialog.Builder(this);
        inputServer = new EditText(this);
        alert.setTitle("请输入你的名字");
        alert.setView(inputServer);
        alert.setNeutralButton("确认",this);
        alert.create().show();
        this.score=score;
    }

    //显示游戏结束的界面
    public void toEndView(){
        if(endView == null){
            endView = new EndView(this,name,score);
        }
        setContentView(endView);
        gameView = null;
    }
    public void toRankView(){
        setContentView(R.layout.layout_end);
        myAdapter = new ABAdapter(GameActivity.this,people);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(myAdapter);
        endView = null;
        gameView = null;
    }

    public void onClick(DialogInterface dialog, int which){
        name = inputServer.getText().toString();
        person = new People(name,String.valueOf(score));
        int i=0;
        while(i<people.size()) {
            if(people.get(i).getName().equals(name)) {
                break;
            }
            i++;
        }
        if(i == people.size()) {
            people.add(person);
            util.insert(person);
        }
        else {
            if(score>Integer.parseInt(people.get(i).getScore()))
            people.get(i).setScore(String.valueOf(score));
            util.update(people.get(i));
        }
        toEndView();
    }
}
