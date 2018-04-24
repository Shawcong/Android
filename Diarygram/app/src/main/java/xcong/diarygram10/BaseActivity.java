package xcong.diarygram10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ant.liao.GifView;

/**
 * Created by 28907 on 2016/9/27.
 */
public class BaseActivity extends Activity {
    private Button button;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        GifView gifImageView1 = (GifView) findViewById(R.id.gif1);
        try {
            gifImageView1.setGifImage(R.drawable.my);
            // 设置显示的图片
        } catch (Exception e) {
            e.printStackTrace();
        }

        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
