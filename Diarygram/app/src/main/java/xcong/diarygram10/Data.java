package xcong.diarygram10;

import java.io.Serializable;

/**
 * Created by 28907 on 2016/9/26.
 */
public class Data implements Serializable {

    private static final long serialVersionUID = 1L;

    private String time = "";

    private String week;

    private String describe = "";

    private String name = "";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek(){
        return week;
    }

    public void setWeek(String week){
        this.week = week;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }
}
