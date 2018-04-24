package yjf.baidumap;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    MapView mMapView = null;
    ArrayList<People> person=null;
    ArrayList<People> person2=null;
    DBbHelper util;
    DBcHelper util2;

    // 普通折线，点击时改变宽度
    Polyline mPolyline;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BaiduMap mBaiduMap;

    boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        ImageView image = (ImageView)findViewById(R.id.imageview_sweep);
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.tip);
        image.startAnimation(animation);

        util = new DBbHelper(MainActivity.this);
        util2 = new DBcHelper(MainActivity.this);
        person = util.getAllData();
        person2 = util2.getAllData();

        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);

        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);//反编译获得具体位置，只有网络定位才可以
        mLocClient.setLocOption(option);
        mLocClient.start();
        OverlayOptions option1 ;


        BitmapDescriptor bitfmap =  BitmapDescriptorFactory.fromResource(R.drawable.friend_marker);
        if(person!=null) {
            for (int i = 0; i < person.size(); i++) {
                if (person.get(i).getLatitude() != null && person.get(i).getLongitude() != null) {
                    //定义Maker坐标点
                    //构建MarkerOption，用于在地图上添加Marker
                    option1 = new MarkerOptions()
                            .position(new LatLng(Integer.parseInt(person.get(i).getLatitude()), Integer.parseInt(person.get(i).getLongitude())))
                            .icon(bitfmap);
                    //在地图上添加Marker，并显示
                    mMapView.getMap().addOverlay(option1);
                }
            }
        }

        BitmapDescriptor bitemap =  BitmapDescriptorFactory.fromResource(R.drawable.enemy_marker);
        if(person2!=null) {
            for (int i = 0; i < person2.size(); i++) {
                if (person2.get(i).getLatitude() != null && person2.get(i).getLongitude() != null) {
                    //定义Maker坐标点
                    //构建MarkerOption，用于在地图上添加Marker
                    option1 = new MarkerOptions()
                            .position(new LatLng(Integer.parseInt(person2.get(i).getLatitude()), Integer.parseInt(person2.get(i).getLongitude())))
                            .icon(bitfmap);
                    //在地图上添加Marker，并显示
                    mMapView.getMap().addOverlay(option1);
                }
            }
        }



        //点击朋友按钮进入朋友列表
        Button btn_friends = (Button) findViewById(R.id.btn_friends);
        //建立事件
        btn_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });

        //点击敌人按钮进入敌人列表
        Button btn_enemies = (Button) findViewById(R.id.btn_enemies);
        //建立事件
        btn_enemies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EnemiesActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        double locallat,locallon;
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            locallat=location.getLatitude();
            locallon=location.getLongitude();
            if (person != null) {
                for (int i = 0; i < person.size(); i++) {
                    if (person.get(i).getLatitude() != null && person.get(i).getLongitude() != null) {
                        LatLng p1 = new LatLng(Integer.parseInt(person.get(i).getLatitude()), Integer.parseInt(person.get(i).getLongitude()));
                        LatLng p2 = new LatLng(locallat,locallon);
                        List<LatLng> points = new ArrayList<LatLng>();
                        points.add(p1);
                        points.add(p2);
                        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                                .color(0xFF2BD54D).points(points);
                        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                    }
                }
            }
            if (person2 != null) {
                for (int i = 0; i < person2.size(); i++) {
                    if (person2.get(i).getLatitude() != null && person2.get(i).getLongitude() != null) {
                        LatLng p1 = new LatLng(Integer.parseInt(person2.get(i).getLatitude()), Integer.parseInt(person2.get(i).getLongitude()));
                        LatLng p2 = new LatLng(locallat,locallon);
                        List<LatLng> points = new ArrayList<LatLng>();
                        points.add(p1);
                        points.add(p2);
                        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                                .color(0xFFAA0000).points(points);
                        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                    }
                }
            }
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
        public double relat(){
            return locallat;
        }
        public double relon(){
            return locallon;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
