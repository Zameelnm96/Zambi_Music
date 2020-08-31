package com.example.zambimusic.service.singletonvalues;

public class Position {
    private int intValue;
    private static Position instance;

    private Position(){ intValue = -1;}
    public  static Position getInstance(){
        if (instance == null){
            synchronized (Position.class){
                if(instance == null){
                    instance = new Position();
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
