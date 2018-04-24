package xcong.eatfish;


import java.io.Serializable;

/**
 * Created by Yin on 2016/10/21.
 */
public class People implements Serializable {
    private String name = null;
    private String score = null;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getScore(){
        return score;
    }
    public void setScore(String score){
        this.score=score;
    }

    public People(String name,String score){
        this.name=name;
        this.score=score;
    }
    public People(){}
}

