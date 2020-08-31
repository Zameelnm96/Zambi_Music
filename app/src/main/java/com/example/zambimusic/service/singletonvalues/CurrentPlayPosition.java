package com.example.zambimusic.service.singletonvalues;

public class CurrentPlayPosition {
    private int intValue;
    private static CurrentPlayPosition instance;

    private CurrentPlayPosition(){ intValue = -1;}
    public  static CurrentPlayPosition getInstance(){
        if (instance == null){
            synchronized (Position.class){
                if(instance == null){
                    instance = new CurrentPlayPosition();
                }
            }
        }

        return instance;
    }
    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
