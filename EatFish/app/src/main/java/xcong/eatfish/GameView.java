package xcong.eatfish;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 聪 on 2016/11/10.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable,android.view.View.OnTouchListener{
    protected GameActivity gameActivity;
    private Bitmap myfish;
    private List<Bitmap> fishl = new ArrayList();
    private List<Bitmap> fishr = new ArrayList();
    private Bitmap background ,buffbmp;
    private ArrayList<GameImage> gameImages = new ArrayList();
    private Bitmap dcache;//二级缓存
    int display_w,display_h;
    int[] level={30,80,150,240,500};
    private int buff_time = 0;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.setOnTouchListener(this);//鼠标点击事件注册
        this.gameActivity=(GameActivity)context;
    }

    //初始化照片
    public void init(){
        //首先加载所有的图片
        fishl.add(BitmapFactory.decodeResource(getResources(),R.drawable.l1));
        fishl.add(BitmapFactory.decodeResource(getResources(),R.drawable.l2));
        fishl.add(BitmapFactory.decodeResource(getResources(),R.drawable.l3));
        fishl.add(BitmapFactory.decodeResource(getResources(),R.drawable.l4));
        fishl.add(BitmapFactory.decodeResource(getResources(),R.drawable.l5));
        fishr.add(BitmapFactory.decodeResource(getResources(),R.drawable.r1));
        fishr.add(BitmapFactory.decodeResource(getResources(),R.drawable.r2));
        fishr.add(BitmapFactory.decodeResource(getResources(),R.drawable.r3));
        fishr.add(BitmapFactory.decodeResource(getResources(),R.drawable.r4));
        fishr.add(BitmapFactory.decodeResource(getResources(),R.drawable.r5));
        background=BitmapFactory.decodeResource(getResources(),R.drawable.game);
        buffbmp = BitmapFactory.decodeResource(getResources(),R.drawable.buffbmp);
        myfish = BitmapFactory.decodeResource(getResources(),R.drawable.l1);
        //二级缓存照片的创建
        dcache = Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);

        gameImages.add(new background(background));//先加入背景照片
        gameImages.add(new Myfish(myfish));
        gameImages.add(new fishl(fishl.get(0),1));
        gameImages.add(new fishr(fishr.get(0),1));
    }

    private interface GameImage{
        public Bitmap getBitmap();
        public int getX();
        public int getY();
    }

    private class fishl implements GameImage{
        private Bitmap fish1=null;
        private int x,y;//随机出现坐标
        private int width,height;
        private int i;//是哪一级别的鱼
        public fishl(Bitmap fish1,int i){
            this.fish1=fish1;
            x=-fish1.getWidth();
            Random ran = new Random();
            y=ran.nextInt(display_h-fish1.getHeight());
            width = fish1.getWidth();
            height = fish1.getHeight();
            this.i=i;
        }
        public Bitmap getBitmap(){
            x+=10*i;
            if(x>display_w){
                gameImages.remove(this);//出了屏幕就删除当前对象
            }
            return fish1;
        }
        public void beEat(ArrayList<GameImage> gameImages2){
            for(GameImage image:(List<GameImage>)gameImages2.clone()){//用克隆的来循环，因为在删除中会影响循环
                if(image instanceof Myfish){
                        if (image.getX() + ((Myfish) image).getWidth() >= x && image.getY() + ((Myfish) image).getHeight() >= y && image.getX() <= x + width
                                && image.getY() <= y + height) {
                            if (i - 1 <= ((Myfish) image).getLevel()) {
                                Log.i("APP.TAG", "击中");
                                gameImages.remove(this);
                                score += 5 * i;
                                if (level[((Myfish) image).getLevel()] <= score) {
                                    ((Myfish) image).setLevel();
                                    if (xx == 0)
                                        ((Myfish) image).Change(fishl.get(((Myfish) image).getLevel()));
                                    else
                                        ((Myfish) image).Change(fishr.get(((Myfish) image).getLevel()));
                                }
                            }
                            else {
                                gameImages.remove(image);
                                state=false;
                            }
                        }
                }
            }
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
    }

    private class fishr implements GameImage{
        private Bitmap fish1=null;
        private int x,y;//随机出现坐标
        private int width,height;
        private int i;//是哪一级别的鱼
        public fishr(Bitmap fish1,int i){
            this.fish1=fish1;
            x=display_w+fish1.getWidth();
            Random ran = new Random();
            y=ran.nextInt(display_h-fish1.getHeight());
            width = fish1.getWidth();
            height = fish1.getHeight();
            this.i=i;
        }
        public Bitmap getBitmap(){
            x-=10*i;
            if(x<-fish1.getWidth()){
                gameImages.remove(this);//出了屏幕就删除当前对象
            }
            return fish1;
        }
        public void beEat(ArrayList<GameImage> gameImages2){
            for(GameImage image:(List<GameImage>)gameImages2.clone()){//用克隆的来循环，因为在删除中会影响循环
                if(image instanceof Myfish){
                    if(image.getX()+((Myfish) image).getWidth()>=x && image.getY()+ ((Myfish) image).getHeight() >=y && image.getX()<= x+width
                            && image.getY()<=y+height) {
                        if (i - 1 <= ((Myfish) image).getLevel()) {
                            gameImages.remove(this);
                            Log.i("APP.TAG", "击中");
                            score += 5 * i;
                            if (level[((Myfish) image).getLevel()] <= score) {
                                ((Myfish) image).setLevel();
                                if (xx == 0)
                                    ((Myfish) image).Change(fishl.get(((Myfish) image).getLevel()));
                                else
                                    ((Myfish) image).Change(fishr.get(((Myfish) image).getLevel()));
                            }
                        }
                        else {
                            gameImages.remove(image);
                            state=false;
                        }
                    }
                }
            }
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
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

    private class Myfish implements GameImage{
        private Bitmap bitmap=null;
        private int x,y;
        private int width;
        private int height;
        private int level;
        private Bitmap newBitmap=null;
        private Myfish(Bitmap bitmap){
            this.bitmap=bitmap;
            x=(display_w-bitmap.getWidth())/2;
            y=0;
            //得到自己鱼的高和宽
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            level=0;
        }
        private int index=0;
        private int num=0;
        public Bitmap getBitmap(){
            return bitmap;
        }
        public void Change(Bitmap bitmap){
            this.bitmap=bitmap;
        }
        public int getY(){
            return y;
        }
        public int getX(){
            return x;
        }
        public void setX(int x){
            this.x=x;
        }
        public void setY(int y){
            this.y=y;
        }
        public int getWidth(){
            return width;
        }
        public int getHeight(){
            return height;
        }
        public int getLevel(){return level;}
        public void setLevel(){
            level++;
        }
    }

    private class buff implements GameImage{
        private Bitmap bitmap=null;
        private int x,y;
        private int width;
        private int height;
        private buff(Bitmap bitmap){
            this.bitmap=bitmap;
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            Random ran = new Random();
            x=ran.nextInt(display_h-width);
            y=-height;
        }
        public Bitmap getBitmap(){
            y+=10;
            if(y>display_h){
                gameImages.remove(this);//出了屏幕就删除当前对象
            }
            return bitmap;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
        public void beEat(ArrayList<GameImage> gameImages2) {
            for (GameImage image : (List<GameImage>) gameImages2.clone()) {//用克隆的来循环，因为在删除中会影响循环
                if (image instanceof Myfish) {
                    if (image.getX() + ((Myfish) image).getWidth() >= x && image.getY() + ((Myfish) image).getHeight() >= y && image.getX() <= x + width
                            && image.getY() <= y + height) {
                        gameImages.remove(this);
                        score = level[((Myfish) image).getLevel()];
                        if (level[((Myfish) image).getLevel()] <= score) {
                            ((Myfish) image).setLevel();
                            if (xx == 0)
                                ((Myfish) image).Change(fishl.get(((Myfish) image).getLevel()));
                            else
                                ((Myfish) image).Change(fishr.get(((Myfish) image).getLevel()));
                        }
                    }
                }
            }
        }
    }

    private boolean state = false;//什么时候启动线程
    private SurfaceHolder holder;
    private int score=0;
    private boolean stopState= false;


    //绘画方法
    public void run(){
        Paint p = new Paint();
        Paint pt = new Paint();
        Paint ptt = new Paint();
        Random ran = new Random();
        int r = ran.nextInt(13);
        RectF rectBlackBg = new RectF(2, 2, 600, 50);
        RectF rectYellow;
        RectF rectYellowf = new RectF(2, 2, 600, 50);
        RectF rectL1 =  new RectF((int)(600*(30/500.0)), 2  , (int)(600*(30/500.0))+10, 52);
        RectF rectL2 = new RectF((int)(600*(80/500.0)), 2  , (int)(600*(80/500.0))+10, 52);
        RectF rectL3 = new RectF((int)(600*(150/500.0)), 2  , (int)(600*(150/500.0))+10, 52);
        RectF rectL4 = new RectF((int)(600*(240/500.0)), 2  , (int)(600*(240/500.0))+10, 52);
        pt.setColor(Color.BLACK);
        ptt.setColor(Color.YELLOW);
        int fish1_num=0;
        try{
            while(state){
                Canvas newCanvas = new Canvas(dcache);//画二级缓存
                while(stopState){

                }
                //画图形集合
                for(GameImage image:(List<GameImage>)gameImages.clone()){//用克隆的来循环，因为在删除中会影响循环
                    if(image instanceof fishl){
                        ((fishl)image).beEat(gameImages);//把控制的鱼传给游动的鱼
                    }
                    else if(image instanceof fishr){
                        ((fishr)image).beEat(gameImages);//把控制的鱼传给游动的鱼
                    }
                    else if(image instanceof buff){
                        ((buff) image).beEat(gameImages);
                    }
                    newCanvas.drawBitmap(image.getBitmap(),image.getX(),image.getY(),p);
                }

                newCanvas.drawRoundRect(rectBlackBg,10,10,pt);
                if(score<=500){
                    rectYellow = new RectF(2, 2, (int)(600*(score/500.0)), 50);
                    newCanvas.drawRoundRect(rectYellow,10,10,ptt);
                }
                else {
                    newCanvas.drawRoundRect(rectYellowf,10,10,ptt);
                }
                newCanvas.drawRoundRect(rectL1,10,10,ptt);
                newCanvas.drawRoundRect(rectL2,10,10,ptt);
                newCanvas.drawRoundRect(rectL3,10,10,ptt);
                newCanvas.drawRoundRect(rectL4,10,10,ptt);

                if(fish1_num==20){
                    fish1_num=0;
                    if(r==0)
                        gameImages.add(new fishl(fishl.get(0),1));
                    else if(r==1) {
                        gameImages.add(new fishl(fishl.get(0),1));
                        gameImages.add(new fishr(fishr.get(0),1));
                    }
                    else if(r==2){
                        gameImages.add(new fishr(fishr.get(0),1));
                    }
                    else if(r==3)
                        gameImages.add(new fishl(fishl.get(1),2));
                    else if(r==4){
                        gameImages.add(new fishl(fishl.get(1),2));
                        gameImages.add(new fishr(fishr.get(1),2));
                    }
                    else if(r==5)
                        gameImages.add(new fishr(fishr.get(1),2));
                    else if(r==6){
                        gameImages.add(new fishl(fishl.get(2),3));
                    }
                    else if(r==7){
                        gameImages.add(new fishl(fishl.get(2),3));
                        gameImages.add(new fishr(fishr.get(2),3));
                    }
                    else if(r==8){
                        gameImages.add(new fishr(fishr.get(2),3));
                    }
                    else if(r==9){
                        gameImages.add(new fishl(fishl.get(3),4));
                    }
                    else if(r==10){
                        gameImages.add(new fishr(fishr.get(3),4));
                    }
                    else if(r==11){
                        gameImages.add(new fishl(fishl.get(4),5));
                    }
                    else if(r==12){
                        gameImages.add(new fishr(fishr.get(4),5));
                    }
                    r = ran.nextInt(13);
                    if(buff_time == 1000){
                        this.gameImages.add(new buff(buffbmp));
                        buff_time = 0;
                    }
                }
                buff_time++;
                fish1_num++;
                Canvas canvas = holder.lockCanvas();
                canvas.drawBitmap(dcache,0,0,p);
                holder.unlockCanvasAndPost(canvas);
                Thread.sleep(20);
            }
        }catch(Exception e){
        }
        Message message = new Message();
        message.what = 	1;
        message.arg1 = score;
        gameActivity.getHandler().sendMessage(message);

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

    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
        display_w=this.getWidth();
        display_h=this.getHeight();
    }
    Thread thread = null;
    public void surfaceDestroyed(SurfaceHolder holder){
        state = false;
    }

    public int getscore(){
        return score;
    }
    Myfish my;
    int xx=0;

    public boolean onTouch(View v, MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN){//手接近屏幕
            for(GameImage image:gameImages){
                if(image instanceof Myfish){
                    Myfish myfish = (Myfish) image;
                    //找到鱼对象
                    if(myfish.getX()<event.getX()&&myfish.getY()<event.getY()&&
                            myfish.getX()+myfish.getWidth()>event.getX()&&
                            myfish.getY()+myfish.getHeight()>event.getY()){
                        my=myfish;
                    }else{
                        my=null;
                    }
                    break;
                }
            }
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(my!=null){
                if(xx==0) {//x=0表示向右
                    if(event.getX()>my.getX()) {
                        my.setX((int) event.getX());
                        my.setY((int) event.getY());
                    }
                    else{
                        xx=1;
                        my.Change(fishr.get(my.getLevel()));
                        my.setX((int) event.getX());
                        my.setY((int) event.getY());
                    }
                }
                else{//x=1表示正在向左
                    if(event.getX()<my.getX()){
                        my.setX((int) event.getX());
                        my.setY((int) event.getY());
                    }
                    else{
                        xx=0;
                        my.Change(fishl.get(my.getLevel()));
                        my.setX((int) event.getX());
                        my.setY((int) event.getY());
                    }
                }
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            my=null;
        }
        return true;
    }
}
