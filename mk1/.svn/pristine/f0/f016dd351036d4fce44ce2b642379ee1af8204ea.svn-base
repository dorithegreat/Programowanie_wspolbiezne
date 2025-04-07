import java.util.ArrayList;

public class BstTree implements Tree{
    private BstTreeNode root = null;
    private enum Side {RIGHT, LEFT};

    public int keyComparisons;
    public int reads;
    public int repins;
    public ArrayList heights = new ArrayList<Integer>();

    @Override
    public void print(){
        //TODO write more readable print
        if (root == null) {
            return;
        }

        printSubtree(root, "");
    }


    private void printSubtree(TreeNode node, String prefix){
        if (node == null) {
            return;
        }
        
        System.out.println(prefix + "╲");
        System.out.println(prefix + " " + node.getKey());

        if (node.right == null) {
            if (node.left == null) {
                return;
            }
            else{
                printSubtree(node.left, prefix + "  ");
            }
        }
        else{
            if (node.left == null) {
                System.out.println(prefix + " │");
                printSubtree(node.right, prefix + "  ");
            }
            else{
                printSubtree(node.left, prefix + " │");
                printSubtree(node.right, prefix + "  ");
            }
        }
        
    }

    @Override
    public boolean checkIfExists(int key) {
        TreeNode node = root;
        while (node.getKey() != key) {
            if (node.getLeft() == null && node.getRight() == null) {
                return false;
            }
            else if (node.getKey() > key) {
                node = node.getRight();
            }
            else{
                node = node.getLeft();
            }
        }
        return true;
    }

    @Override
    public BstTreeNode find(int key) {
        if (root == null) {
            return null;
        }

        BstTreeNode node = root;
        while (node != null && node.getKey() != key) {
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            }
            else if (node.getKey() > key) {
                node = node.getLeft();
            }
            else{
                node = node.getRight();
            }
            keyComparisons++;
        }
        return node;
    }

    @Override
    public void insert(int key) {
        BstTreeNode newNode = new BstTreeNode(key);
        if (root == null) {
            root = newNode;
            return;
        }

        TreeNode node = root;
        TreeNode parent = null;
        while (node != null) {
            parent = node;
            if (node.getKey() < newNode.getKey()) {
                node = node.getRight();
            }
            else{
                node = node.getLeft();
            }
            keyComparisons++;
        }
        newNode.setParent(parent);
        if (parent.getKey() > newNode.getKey()) {
            parent.setLeft(newNode);
        }
        else{
            parent.setRight(newNode);
        }
        keyComparisons++;
        repins++;
        
    }

    @Override
    public void delete(int key) {
        BstTreeNode node = find(key);
        if (node == null) { // it wasn't even in the tree in the first place
            return;
        }

        
        if (node.getLeft() == null) { // only right child
            repin(node, node.getRight());
            return;
        }

        if (node.getRight() == null) { // only left child
            repin(node, node.getLeft());
        }

        if (node.getLeft() != null && node.getRight() != null) { //both children
            BstTreeNode next = successor(node);
            if (next.getParent() != node) {
                repin(next, next.getRight());
                next.setRight(node.getRight());
                next.getRight().setParent(next);
            }
            repin(node, next);
            next.setLeft(node.left);
            next.getLeft().setParent(next);
        }
    }

    private void repin(BstTreeNode u, BstTreeNode v){
        if (u.getParent() == null) {
            root = v;
        }
        else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v);
        }
        else if (u == u.getParent().getRight()) {
            u.getParent().setRight(v);
        }
        else{
            // this should never happen
        }
        repins++;

        if (v != null) {
            v.setParent(u.getParent());
            repins++;
        }
    }

    public BstTreeNode successor(BstTreeNode node){
        if (node.getRight() != null) {
            return minimum(node.getRight());
        }

        BstTreeNode parent = node.getParent();
        while (parent != null && node == parent.getRight()) {
            node = parent;
            parent = parent.getParent();
        } 
        return parent;
    }

    public BstTreeNode minimum(BstTreeNode node){
        BstTreeNode current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    // public no-argument function for getting the height of the whole tree
    // calls an internal function that can calculate height of any subtree
    // did this for convenience of use
    @Override
    public int height() {
        return heightHelp(root);
    }

    private int heightHelp(BstTreeNode node){
        if (node == null) {
            return 0;
        }

        int leftHeight = heightHelp(node.getLeft());
        int rightHeight = heightHelp(node.getRight());
        return Math.max(leftHeight + 1, rightHeight + 1);
    }
}