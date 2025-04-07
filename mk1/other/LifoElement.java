package com.aisd;

public class LifoElement {
    private LifoElement nextElement = null;
    private Object value;

    public LifoElement(Object element){
        value = element;
    }

    public void setNext(LifoElement next){
        nextElement = next;
    }

    public Object getValue(){
        return value;
    }

    public LifoElement getNext(){
        return nextElement;
    }

    public Object takeOut(){
        Object result = nextElement.getValue();
        nextElement = nextElement.getNext();
        return result;
    }
}
