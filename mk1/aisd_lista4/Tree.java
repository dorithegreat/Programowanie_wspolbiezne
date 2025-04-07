public interface Tree {
    
    void print();
    void insert(int key);
    void delete(int key);
    int height();

    boolean checkIfExists(int key);
    TreeNode find(int key);
}
