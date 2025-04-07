package com.aisd;

public class TwoWayList {
    private TwoWayListElement start = null;
    private int length = 0;

    public TwoWayListElement getStart(){
        return start;
    }

    public void setStart(TwoWayListElement newStart){
        start = newStart;
    }

    public int getLength(){
        return length;
    }

    public void incrementLength(){
        length++;
    }
}
