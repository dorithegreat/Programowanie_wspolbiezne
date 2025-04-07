public class RbTreeNode extends TreeNode {
    public enum Color {RED, BLACK};
    private Color color;

    public RbTreeNode(int key){
        super(key);
        color = Color.RED;
    }

    public RbTreeNode(int key, Color color){
        super(key);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public RbTreeNode getParent(){
        return (RbTreeNode)parent;
    }

    public RbTreeNode getLeft(){
        return (RbTreeNode)left;
    }

    public RbTreeNode getRight(){
        return (RbTreeNode)right;
    }
}
