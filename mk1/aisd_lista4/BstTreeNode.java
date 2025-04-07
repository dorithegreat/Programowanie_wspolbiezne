public class BstTreeNode extends TreeNode {

    public BstTreeNode(int key){
        super(key);
    }

    @Override
    public BstTreeNode getLeft() {
        return (BstTreeNode)left;
    }
    @Override
    public BstTreeNode getRight() {
        return (BstTreeNode)right;
    }
    @Override
    public BstTreeNode getParent() {
        return (BstTreeNode)parent;
    }
}
