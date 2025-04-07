public class GraphNode {

    private int name;

    public int getName(){
        return name;
    }


    public GraphNode(int n){
        name = n;
    }

    public String toString(){
        return "" + name;
    }
}