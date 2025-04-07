import java.util.Comparator;

public class EdgeComparator implements Comparator<GraphEdge> {
    @Override
    public int compare(GraphEdge a, GraphEdge b){
        if (a.getWeight() < b.getWeight()) {
            return -1;
        }
        else if (a.getWeight() > b.getWeight()) {
            return 1;
        }
        else{
            return 0;
        }
    }
}
