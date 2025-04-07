public class GraphEdge {
    private GraphNode[] nodes = new GraphNode[2];
    private double weight;

    public double getWeight(){
        return weight;
    }

    public GraphNode getFirstNode(){
        return nodes[0];
    }
    public GraphNode getSecondNode(){
        return nodes[1];
    }

    public GraphEdge(GraphNode u, GraphNode v, double weight){
        nodes[0] = u;
        nodes[1] = v;
        this.weight = weight;
    }

    public GraphNode getOtherNode(GraphNode node){
        if (node == nodes[0]) {
            return nodes[1];
        }

        if (node == nodes[1]) {
            return nodes[0];
        }

        return null;
    }

    public boolean contains(GraphNode node){
        if (nodes[0] == node || nodes[1] == node) {
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "[" + nodes[0].getName() + " ," + nodes[1].getName() + ", " + weight + "]";
    }
}
