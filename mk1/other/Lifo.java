package com.aisd;

public class Lifo {
    private LifoElement start = null;

    public void putIn(Object o){
        LifoElement newElement = new LifoElement(o);
        newElement.setNext(start);
        start = newElement;
    }

    public Object takeOut() {
        if (start == null) {
            return null;
        }

        Object value = start.getValue();
        start = start.getNext();
        return value;
    }
}
