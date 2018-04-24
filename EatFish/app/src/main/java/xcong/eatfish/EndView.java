package xcong.eatfish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 聪 on 2016/11/16.
 */
public class EndView extends SurfaceView implements SurfaceHolder.Callback,Runnable,android.view.View.OnTouchListener{
    protected GameActivity gameActivity;
    private List<Bitmap> dust = new ArrayList();//尘埃
    private Bitmap background;//背景
    private Bitmap dcache;//二级缓存
    int display_w,display_h;
    private ArrayList<GameImage> gameImages = new ArrayList();
    private int score1;
    private String startGame = "重新挑战";	// 按钮的文字
    private String exitGame = "返回菜单";
    private Bitmap button;					// 按钮图片
    private Bitmap button2;					// 按钮图片
    private float button_x;
    private float button_y;
    private float button_y2;
    private float strwid;
    private float strhei;
    private boolean isBtChange;				// 按钮图片改变的标记
    private boolean isBtChange2;
    private People person ;
    private String name;
    Rect rect = new Rect();

    public EndView(Context context,String name,int score1){
        super(context);
        getHolder().addCallback(this);
        this.setOnTouchListener(this);//鼠标点击事件注册
        this.score1 = score1;
        this.gameActivity=(GameActivity)context;
        this.name = name;
    }

    public void init(){
        dust.add(BitmapFactory.decodeResource(getResources(),R.drawable.dust6));
        dust.add(BitmapFactory.decodeResource(getResources(),R.drawable.dust5));
        dust.add(BitmapFactory.decodeResource(getResources(),R.drawable.dust4));
        dust.add(BitmapFactory.decodeResource(getResources(),R.drawable.dust3));
        dust.add(BitmapFactory.decodeResource(getResources(),R.drawable.dust2));
        dust.add(BitmapFactory.decodeResource(getResources(),R.drawable.dust1));
        dust.add(BitmapFactory.decodeResource(getResources(),R.drawable.dust0));
        background=BitmapFactory.decodeResource(getResources(),R.drawable.game);
        button = BitmapFactory.decodeResource(getResources(), R.drawable.button);
        button2 = BitmapFactory.decodeResource(getResources(),R.drawable.button2);
        //二级缓存照片的创建
        dcache = Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);
        button_x = display_w / 2 - button.getWidth() / 2;
        button_y = display_h / 2 + button.getHeight();
        button_y2 = button_y + button.getHeight() + 40;
        gameImages.add(new background(background));//先加入背景照片
        gameImages.add(new dust(dust));
    }

    private interface GameImage{
        public Bitmap getBitmap();
        public int getX();
        public int getY();
    }

    private class background implements GameImage{
        private Bitmap image;
        private Bitmap newBitmap=null;
        private background(Bitmap image){
            this.image=image;
            newBitmap=Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);
        }
        public Bitmap getBitmap(){
            Paint p=new Paint();
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(image,new Rect(0,0,image.getWidth(),image.getHeight()),
                    new Rect(0,0,display_w,display_h),p);
            return newBitmap;
        }
        public int getX(){
            return 0;
        }
        public int getY(){
            return 0;
        }
    }

    private class dust implements GameImage{
        private List<Bitmap> my = new ArrayList<Bitmap>();
        int x,y;
        private dust(List<Bitmap> my){
            this.my = my;
        }
        private int index = 0;
        private int num = 0;

        public Bitmap getBitmap(){
            Bitmap bitmap = my.get(index);
            if(num == 0) {
                index++;
                if(index==my.size()-1) {
                    num = 1;
                }
            }
            else{
                index--;
                if(index==0){
                    gameImages.remove(this);
                }
            }
            return bitmap;
        }
        public int getX(){
            Random ran = new Random();
            return ran.nextInt(display_w-dust.get(index).getWidth());
        }
        public int getY(){
            Random ran = new Random();
            return ran.nextInt(display_h-dust.get(index).getHeight());
        }
    }

    private boolean state = false;//什么时候启动线程
    private SurfaceHolder holder;

    public void run(){
        Paint p =new Paint();
        Paint p1 =new Paint();
        p1.setTextSize(80);
        p1.getTextBounds(startGame, 0, startGame.length(), rect);//把字符串长宽给rect
        strwid = rect.width();
        strhei = rect.height();
        int num=0;
        try{
            while(state){
                Canvas newCanvas = new Canvas(dcache);

                //画图形集合
                for(GameImage image:(List<GameImage>)gameImages.clone()){//用克隆的来循环，因为在删除中会影响循环
                    newCanvas.drawBitmap(image.getBitmap(),image.getX(),image.getY(),p);
                }

                if(num==20){
                    num=0;
                    gameImages.add(new dust(dust));
                    gameImages.add(new dust(dust));
                    gameImages.add(new dust(dust));
                    gameImages.add(new dust(dust));
                    gameImages.add(new dust(dust));
                    gameImages.add(new dust(dust));
                    gameImages.add(new dust(dust));
                    gameImages.add(new dust(dust));
                    gameImages.add(new dust(dust));
                }
                num++;

                newCanvas.drawText("分数:      "+score1,button_x, display_h/2,p1);
                newCanvas.drawBitmap(button2, button_x, button_y2, p);
                newCanvas.drawBitmap(button, button_x, button_y, p);
                newCanvas.drawText(startGame, display_w/2 - strwid/2, button_y + button.getHeight()/2 + strhei/2, p1);
                newCanvas.drawText(exitGame, display_w/2 - strwid/2, button_y2 + button.getHeight()/2 + strhei/2, p1);
                Canvas canvas = holder.lockCanvas();
                canvas.drawBitmap(dcache,0,0,p);
                holder.unlockCanvasAndPost(canvas);
                Thread.sleep(50);
            }
        }catch (Exception e){}
    }

    public void surfaceCreated(SurfaceHolder holder){
        display_w=this.getWidth();
        display_h=this.getHeight();
        this.holder=holder;
        init();
        state = true;
        Thread thread = new Thread(this);
        thread.start();

    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        display_w=this.getWidth();
        display_h=this.getHeight();
    }
    Thread thread = null;
    public void surfaceDestroyed(SurfaceHolder holder){
        state = false;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            Log.i("APP.TAG","1");
            //判断第一个按钮是否被按下
            if(x > button_x && x < button_x + button.getWidth()
                    && y > button_y && y < button_y + button.getHeight())
            {
                Message message = new Message();
                message.what = 	0;
                person = new People(name,String.valueOf(score1));
                GameActivity.people.add(person);
                gameActivity.getHandler().sendMessage(message);
            }
            //判断第二个按钮是否被按下
            else if(x > button_x && x < button_x + button2.getWidth()
                    && y > button_y2 && y < button_y2 + button2.getHeight())
            {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            float x = event.getX();
            float y = event.getY();
            if(x > button_x && x < button_x + button.getWidth()
                    && y > button_y && y < button_y + button.getHeight())
            {
                Log.i("APP.TAG","1");
            }
            else{
            }
            if(x > button_x && x < button_x + button.getWidth()
                    && y > button_y2 && y < button_y2 + button.getHeight())
            {
            }
            else{
            }
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            isBtChange = false;
            isBtChange2 = false;
            return true;
        }
        return true;
    }
}
