package com.example.zambimusic.service.singletonvalues;

import com.example.zambimusic.enums.REPEAT;

public class IsRepeated {
    private REPEAT repeat;
    private static IsRepeated instance;

    private IsRepeated(){ repeat = REPEAT.NO_REPEAT ;}
    public  static IsRepeated getInstance(){
        if (instance == null){
            synchronized (Position.class){
                if(instance == null){
                    instance = new IsRepeated();
                }
            }
        }

        return instance;
    }
    public void setRepeat(REPEAT repeat) {
        this.repeat = repeat;
    }

    public REPEAT getRepeat() {
        return repeat;
    }


}


