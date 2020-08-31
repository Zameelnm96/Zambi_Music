package com.example.zambimusic.service.singletonvalues;

public class IsShuffled {
    private boolean booleanValue;
    private static IsShuffled instance;

    private IsShuffled(){ booleanValue = false  ;}
    public  static IsShuffled getInstance(){
        if (instance == null){
            synchronized (Position.class){
                if(instance == null){
                    instance = new IsShuffled();
                }
            }
        }

        return instance;
    }
    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }
}
