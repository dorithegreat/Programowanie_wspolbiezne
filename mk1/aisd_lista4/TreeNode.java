public abstract class TreeNode {
    protected int key;
    protected TreeNode left = null;
    protected TreeNode right = null;
    protected TreeNode parent = null;

    // * -------getters--and--setters---------
    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void setKey(int key){
        this.key = key;
    }

    public int getKey(){
        return this.key;
    }

    // * -------constructor----------
    public TreeNode(int key){
        this.key = key;
    }
    
}