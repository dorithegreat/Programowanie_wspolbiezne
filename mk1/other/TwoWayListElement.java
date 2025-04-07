package com.aisd;

public class TwoWayListElement {
    private TwoWayListElement nextElement = null;
    private TwoWayListElement previousElement = null;
    private int value;

    public TwoWayListElement(int value){
        this.value = value;
    }

    public void setNext(TwoWayListElement next){
        nextElement = next;
    }

    public TwoWayListElement getNext(){
        return nextElement;
    }

    public TwoWayListElement getPrev(){
        return previousElement;
    }

    public void setPrev(TwoWayListElement prev){
        previousElement = prev;
    }

    public int getValue(){
        return value;
    }
}
