public class RbTree implements Tree{
    private RbTreeNode root = null;
    private enum Side {RIGHT, LEFT};

    public int keyComparisons;
    public int repins;
    public int recolors;

    public RbTreeNode getRoot(){
        return root;
    }

    @Override
    public void print() {
        //TODO write more readable print

        if (root == null) {
            return;
        }
        printSubtree(root, "");
    }

    private void printSubtree(RbTreeNode node, String prefix){
        if (node == null) {
            return;
        }
        
        System.out.println(prefix + "╲");
        if (node.getColor() == RbTreeNode.Color.RED) {
            System.out.println(prefix + " " + "\u001B[31m" + node.getKey() + "\u001B[0m");
        }
        else{
            System.out.println(prefix + " " + node.getKey());
        }

        if (node.right == null) {
            if (node.getLeft() == null) {
                return;
            }
            else{
                printSubtree(node.getLeft(), prefix + "  ");
            }
        }
        else{
            if (node.left == null) {
                System.out.println(prefix + " │");
                printSubtree(node.getRight(), prefix + "  ");
            }
            else{
                printSubtree(node.getLeft(), prefix + " │");
                printSubtree(node.getRight(), prefix + "  ");
            }
        }
    }

    @Override
    public void insert(int key) {
        RbTreeNode newnNode = new RbTreeNode(key);
        RbTreeNode parent = null;
        RbTreeNode current = root;

        while (current != null) {
            parent = current;
            if (newnNode.getKey() < current.getKey()) {
                current = current.getLeft();
            }
            else { 
                current = current.getRight();
            }
        }
        newnNode.setParent(parent);
        repins++;
        
        if (parent == null) {
            root = newnNode;
        }
        else if (parent.getKey() > newnNode.getKey()) {
            parent.setLeft(newnNode);
        }
        else{
            parent.setRight(newnNode);
        }
        repins++;

        if (newnNode.getParent() == null) {
            newnNode.setColor(RbTreeNode.Color.BLACK);
            recolors++;
            return;
        }
        else if (newnNode.getParent().getParent() == null) {
            return;
        }
        fixup(newnNode);
    }

    private void bstInsert(RbTreeNode newNode){
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
        repins++;
        if (parent.getKey() > newNode.getKey()) {
            parent.setLeft(newNode);
        }
        else{
            parent.setRight(newNode);
        }
        repins++;

    }

    @Override
    public void delete(int key) {
        RbTreeNode node = find(key);

        if (node == null) {
            return;
        }

        RbTreeNode movedNode;
        RbTreeNode.Color deletedColor; 

        //at most one child
        if (node.getLeft() == null || node.getRight() == null) {
            movedNode = deleteOneChild(node);
            deletedColor = node.getColor();
        }
        else{
            RbTreeNode successor = successor(node);
            node.setKey(successor.getKey());
            movedNode = deleteOneChild(successor);
            deletedColor = successor.getColor();
        }

        if (deletedColor == RbTreeNode.Color.BLACK) {
            fixupDelete(movedNode);

            if (movedNode.getKey() == -1) {
                replaceChild(movedNode.getParent(), movedNode, null);
            }
            //remove fake node??
        }

    }

    private void replaceChild(RbTreeNode parent, RbTreeNode oldChild, RbTreeNode newChild){
        if (parent == null) {
            root = newChild;
        }
        else if (parent.getLeft() == oldChild) {
            parent.setLeft(newChild);
        }
        else if (parent.getRight() == oldChild) {
            parent.setRight(newChild);
        }
        repins++;

        if (newChild != null) {
            newChild.setParent(parent);
            repins++;
        }
    }

    private RbTreeNode deleteOneChild(RbTreeNode node){
        if (node.getLeft() != null) {
            replaceChild(node.getParent(), node, node.getLeft());
            return node.getLeft();
        }
        else if (node.getRight() != null) {
            replaceChild(node.getParent(), node, node.getRight());
            return node.getRight();
        }
        else{
            if (node.getColor() == RbTreeNode.Color.BLACK) {
                RbTreeNode fakeNode = new RbTreeNode(-1, RbTreeNode.Color.BLACK);
                replaceChild(node.getParent(), node, fakeNode);
                return fakeNode;
            }
            else{
                replaceChild(node.getParent(), node, null);
                return null;
            }
        }
    }

    public void fixupDelete(RbTreeNode node){
        if (node == root) {
            node.setColor(RbTreeNode.Color.RED);
            recolors++;
            return;
        }

        RbTreeNode sibling = getSibling(node);
        if (sibling.getColor() == RbTreeNode.Color.RED) {
            sibling.setColor(RbTreeNode.Color.BLACK);
            node.getParent().setColor(RbTreeNode.Color.RED);
            recolors += 2;

            if (node == node.getParent().getLeft()) {
                rotateLeft(node.getParent());
            }
            else{
                rotateRight(node.getParent());
            }
            sibling = getSibling(node);
        }

        if (isBlack(sibling.getLeft()) && isBlack(sibling.getRight())){
            sibling.setColor(RbTreeNode.Color.RED);
            recolors++;
            
            if (node.getParent().getColor() == RbTreeNode.Color.RED) {
                node.getParent().setColor(RbTreeNode.Color.BLACK);
                recolors++;
            }
            else{
                fixupDelete(node.getParent());
            }
        }
        else{
            boolean nodeIsLeft = node == node.getParent().getLeft();

            if (nodeIsLeft && isBlack(sibling.getRight())) {
                sibling.getLeft().setColor(RbTreeNode.Color.BLACK);
                sibling.setColor(RbTreeNode.Color.RED);
                recolors += 2;

                rotateRight(sibling);
                sibling = node.getParent().getRight();
            }
            else if (!nodeIsLeft && isBlack(sibling.getLeft())) {
                sibling.getRight().setColor(RbTreeNode.Color.BLACK);
                sibling.setColor(RbTreeNode.Color.RED);
                recolors += 2;

                rotateLeft(sibling);
                sibling = node.getParent().getLeft();
            }

            sibling.setColor(node.getParent().getColor());
            node.getParent().setColor(RbTreeNode.Color.BLACK);
            recolors += 2;

            if (nodeIsLeft) {
                sibling.getRight().setColor(RbTreeNode.Color.BLACK);
                rotateLeft(node.getParent());
            }
            else{
                sibling.getLeft().setColor(RbTreeNode.Color.BLACK);
                rotateRight(node.getParent());
            }
            recolors++;
        }

    }

    public RbTreeNode successor(RbTreeNode node){
        if (node.getRight() != null) {
            return minimum(node.getRight());
        }

        RbTreeNode parent = node.getParent();
        while (parent != null && node == parent.getRight()) {
            node = parent;
            parent = parent.getParent();
        } 
        return parent;
    }

    public RbTreeNode minimum(RbTreeNode node){
        RbTreeNode current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    private void fixup(RbTreeNode node){
        while (node != root && node.getParent().getColor() == RbTreeNode.Color.RED) {
            if (node.getParent() == node.getParent().getParent().getLeft()) {
                RbTreeNode uncle = node.getParent().getParent().getRight();

                if (uncle != null && uncle.getColor() == RbTreeNode.Color.RED) {
                    node.getParent().setColor(RbTreeNode.Color.BLACK);
                    uncle.setColor(RbTreeNode.Color.BLACK);
                    node.getParent().getParent().setColor(RbTreeNode.Color.RED);
                    recolors += 3;

                    node = node.getParent().getParent();
                }
                else{
                    if (node == node.getParent().getRight()) {
                        node = node.getParent();
                        rotateLeft(node);
                    }
                    node.getParent().setColor(RbTreeNode.Color.BLACK);
                    node.getParent().getParent().setColor(RbTreeNode.Color.RED);
                    recolors += 2;
                    rotateRight(node.getParent().getParent());
                }
            }
            else{
                RbTreeNode uncle = node.getParent().getParent().getLeft();
                if (uncle != null && uncle.getColor() == RbTreeNode.Color.RED) {
                    node.getParent().setColor(RbTreeNode.Color.BLACK);
                    uncle.setColor(RbTreeNode.Color.BLACK);
                    node.getParent().getParent().setColor(RbTreeNode.Color.RED);
                    recolors += 3;

                    node = node.getParent().getParent();
                }
                else{
                    if (node == node.getParent().getLeft()) {
                        node = node.getParent();
                        rotateRight(node);
                    }
                    node.getParent().setColor(RbTreeNode.Color.BLACK);
                    node.getParent().getParent().setColor(RbTreeNode.Color.RED);
                    recolors += 2;

                    rotateLeft(node.getParent().getParent());
                }
            }
        }
        root.setColor(RbTreeNode.Color.BLACK);
        recolors++;
    }

    public void rotateLeft(RbTreeNode x){
        RbTreeNode y = x.getRight();

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

    private void rotateRight(RbTreeNode x){
        RbTreeNode y = x.getLeft();
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

    private RbTreeNode getSibling(RbTreeNode node){
        RbTreeNode parent = node.getParent();
        if (node == parent.getLeft()) {
            return parent.getRight();
        }
        else {
            return parent.getLeft();
        }
    }

    private boolean isBlack(RbTreeNode node){
        return node == null || node.getColor() == RbTreeNode.Color.BLACK;
    }

    @Override
    public int height() {
        return heightHelp(root);
    }

    private int heightHelp(RbTreeNode node){
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
    public RbTreeNode find(int key) {
        if (root == null) {
            return null;
        }

        TreeNode node = root;
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
        return (RbTreeNode)node;
    }


}
