package xcong.eatfish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ant.liao.GifView;


/**
 * Created by 聪 on 2016/11/10.
 */
public class LoadActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.loading);
        GifView gifImageView1 = (GifView) findViewById(R.id.gif1);
        try {
            gifImageView1.setGifImage(R.drawable.fish);
            // 设置显示的图片
        } catch (Exception e) {
            e.printStackTrace();
        }
        final long start=System.currentTimeMillis();
        new Thread(new Runnable()
        {
            public void run()
            {
                Intent intent;
                intent=new Intent(LoadActivity.this,MainActivity.class);
                long end=System.currentTimeMillis();
                if(end-start<7000)
                {
                    try
                    {
                        Thread.sleep(7000-(end-start));
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
