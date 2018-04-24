package xcong.eatfish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by 聪 on 2016/11/10.
 */
public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.layout_main);
        ImageView im = (ImageView)findViewById(R.id.imageView);
        im.setImageResource(R.drawable.menu);
        Button start = (Button)findViewById(R.id.start);
        Button end = (Button)findViewById(R.id.end);
        Button rank = (Button)findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                Bundle bundle = new Bundle();
                bundle.putInt("key",0);
                intent=new Intent(MainActivity.this,GameActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        end.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        rank.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent;
                Bundle bundle = new Bundle();
                bundle.putInt("key",1);
                intent=new Intent(MainActivity.this,GameActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
