package yjf.baidumap;

import com.baidu.platform.comapi.map.C;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yin on 2016/10/21.
 */
public class People implements Serializable {
    private String name = null;
    private String number = null;
    private String latitude = null;
    private String longitude = null;
    private String altitude = null;
    private String accuracy = null;
    private String nearest_address = null;

    private String time_update = null;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getNumber(){
        return number;
    }
    public void setNumber(String number){
        this.number=number;
    }
    public String getLatitude(){
        return latitude;
    }
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }
    public String getLongitude(){
        return longitude;
    }
    public void setLongitude(String longitude){ this.longitude = longitude; }
    public String getAltitude(){ return altitude;}
    public void setAltitude(String altitude){ this.altitude = altitude; }
    public String getNear(){
        return nearest_address;
    }
    public void setNear(String nearest_address){
        this.nearest_address=nearest_address;
    }
    public void setTime_update(String update){
        /*SimpleDateFormat   formatter   =   new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        time_update = str;*/
        time_update = update;
    }
    public String getTime_update(){
        return time_update;
    }
    /*public void setTime(){
        SimpleDateFormat   formatter   =   new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        try {
            long result=(formatter.parse(str).getTime()-formatter.parse(time_update).getTime())/1000;//当前时间减去测试时间 这个的除以1000得到秒，相应的60000得到分，3600000得到小时
            last_update = result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
           long result = (formatter.parse(time_update).getTime()-formatter.parse(str).getTime())/1000 + 300;
            next_update = result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    /*public long getLast_update(){
        return last_update;
    }

    public long getNext_update(){

        return next_update;
    }*/

    public People(String name, String number, String lat, String lon, String hig, String near ,String update){
        this.name=name;
        this.number=number;
        latitude=lat;
        longitude=lon;
        altitude=hig;
        nearest_address=near;
        time_update=update;
    }

    public People(){}
}

