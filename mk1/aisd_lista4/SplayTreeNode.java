public class SplayTreeNode extends TreeNode {
    public SplayTreeNode(int key){
        super(key);
    }

    @Override
    public SplayTreeNode getLeft() {
        return (SplayTreeNode)left;
    }
    @Override
    public SplayTreeNode getRight() {
        return (SplayTreeNode)right;
    }
    @Override
    public SplayTreeNode getParent() {
        return (SplayTreeNode)parent;
    }
}
