package com.aisd;

public class FifoElement {
    private FifoElement nexElement = null;
    private Object value;

    public FifoElement(Object element){
        value = element;
    }

    public void setValue(Object element){
        value = element;
    }

    public Object getValue(){
        return value;
    }

    public FifoElement getNext(){
        return nexElement;
    }

    public void printOut(){
        System.out.println("value: " + value.toString());
        if (nexElement != null) {
            nexElement.printOut();   
        }
    }

    public void putIn(Object element){
        if (nexElement == null) {
            nexElement = new FifoElement(element);
        }
        else{
            nexElement.putIn(element);
        }
    }

    public Object takeOut(){
        Object result = nexElement.getValue();
        nexElement = nexElement.getNext();
        return result;
    }
}
