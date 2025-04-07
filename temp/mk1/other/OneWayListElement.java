package com.aisd;

public class OneWayListElement {
    private OneWayListElement nextElement = null;
    private int value;

    public OneWayListElement(int value){
        this.value = value;
    }

    public void setNext(OneWayListElement next){
        nextElement = next;
    }

    public OneWayListElement getNext(){
        return nextElement;
    }

    public int getValue(){
        return value;
    }
}
