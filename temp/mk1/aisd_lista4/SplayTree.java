public class SplayTree implements Tree {

    private SplayTreeNode root = null;
    private enum Side {RIGHT, LEFT};

    public int keyComparisons;
    public int repins;

    @Override
    public void print() {
        if (root == null) {
            return;
        }
        printSubtree(root, "");
    }

    private void printSubtree(SplayTreeNode node, String prefix){
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
                printSubtree(node.getLeft(), prefix + "  ");
            }
        }
        else{
            if (node.getLeft() == null) {
                System.out.println(prefix + " │");
                printSubtree(node.getRight(), prefix + "  ");
            }
            else{
                printSubtree(node.getLeft(), prefix + " │");
                printSubtree(node.getRight(), prefix + "  ");
            }
        }   
    }

    private void splay(SplayTreeNode node){
        if (node == null) {
            return;
        }
        while (node != root) {
            SplayTreeNode p = node.getParent();
            SplayTreeNode g = node.getParent().getParent();
            if (p == root) {
                //zig
                if (node == p.getLeft()) {
                    rotateRight(p);
                }
                else{
                    rotateLeft(p);
                }
            }
            else if (p == g.getLeft()) {
                if (node == p.getLeft()) {
                    //zig zig
                    rotateRight(g);
                    rotateRight(p);
                }
                else{
                    //zig zag
                    rotateLeft(p);
                    rotateRight(g);
                }
            }
            else{
                if (node == p.getRight()) {
                    //zig zig
                    rotateLeft(g);
                    rotateLeft(p);
                }
                else{
                    //zig zag
                    rotateRight(p);
                    rotateLeft(g);
                }
            }
        }
    }

    public void rotateLeft(SplayTreeNode x){
        SplayTreeNode y = x.getRight();

        x.setRight(y.getLeft());
        repins++;
        if (y.getLeft() != null) {
            y.getLeft().setParent(x);
            repins++;
        }
        y.setParent(x.getParent());
        repins++;
        if (x.getParent() == null) {
            root = y;
        }
        else if (x == x.getParent().getLeft()) {
            x.getParent().setLeft(y);
        }
        else{
            x.getParent().setRight(y);
        }
        repins++;
        y.setLeft(x);
        x.setParent(y);
        repins += 2;
    }

    private void rotateRight(SplayTreeNode x){
        SplayTreeNode y = x.getLeft();
        x.setLeft(y.getRight());
        repins++;
        if (y.getRight() != null) {
            y.getRight().setParent(x);
            repins++;
        }
        y.setParent(x.getParent());
        repins++;
        if (x.getParent() == null) {
            root = y;
        }
        else if (x == x.getParent().getRight()) {
            x.getParent().setRight(y);
        }
        else{
            x.getParent().setLeft(y);
        }
        repins++;
        y.setRight(x);
        x.setParent(y);
        repins += 2;
    }

    @Override
    public void insert(int key) {
        SplayTreeNode newNode = new SplayTreeNode(key);
        bstInsert(newNode);
        splay(newNode);
    }

    private void bstInsert(SplayTreeNode newNode){
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
        }
        newNode.setParent(parent);
        if (parent.getKey() > newNode.getKey()) {
            parent.setLeft(newNode);
        }
        else{
            parent.setRight(newNode);
        }

    }

    @Override
    public void delete(int key) {
        SplayTreeNode node = find(key);
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
            SplayTreeNode next = successor(node);
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

    private void repin(SplayTreeNode u, SplayTreeNode v){
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

    public SplayTreeNode successor(SplayTreeNode node){
        if (node.getRight() != null) {
            return minimum(node.getRight());
        }

        SplayTreeNode parent = node.getParent();
        while (parent != null && node == parent.getRight()) {
            node = parent;
            parent = parent.getParent();
        } 
        return parent;
    }

    public SplayTreeNode minimum(SplayTreeNode node){
        SplayTreeNode current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    @Override
    public int height() {
        return heightHelp(root);
    }

    private int heightHelp(SplayTreeNode node){
        if (node == null) {
            return 0;
        }

        int leftHeight = heightHelp(node.getLeft());
        int rightHeight = heightHelp(node.getRight());
        return Math.max(leftHeight + 1, rightHeight + 1);
    }

    @Override
    public boolean checkIfExists(int key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkIfExists'");
    }

    @Override
    public SplayTreeNode find(int key) {
        if (root == null) {
            return null;
        }

        SplayTreeNode node = root;
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
        splay(node);
        return node;
    }
    
}
