/** Author: Samantha Murray Tuesta
 * Date: 11/2/2020
 * Purpose: Create an AVL tree and helper methods to cut down the runtime of finding * unique words within a txt file.
*/

public class AVL {

  public Node root;

  private int size;

  public int getSize() {
    return size;
  }

  /** find w in the tree. return the node containing w or
  * null if not found */
  public Node search(String w) {
    return search(root, w);
  }
  private Node search(Node n, String w) {
    if (n == null) {
      return null;
    }
    if (w.equals(n.word)) {
      return n;
    } else if (w.compareTo(n.word) < 0) {
      return search(n.left, w);
    } else {
      return search(n.right, w);
    }
  }

  /** insert w into the tree as a standard BST, ignoring balance */
  public void bstInsert(String w) {
    if (root == null) {
      root = new Node(w);
      size = 1;
      return;
    }
    bstInsert(root, w);
  }

  /* insert w into the tree rooted at n, ignoring balance
   * pre: n is not null */
  private void bstInsert(Node n, String w) {
    if (w.equals(n.word)){
      return;
    }
    else if (w.compareTo(n.word) < 0){
      if (n.left == null){
        n.left = new Node(w, n);
        size++;
      }
      bstInsert(n.left, w);
    }
    else if (w.compareTo(n.word) > 0){
      if (n.right == null){
        n.right = new Node(w, n);
        size++;
      }
      bstInsert(n.right, w);
    }
  }

  /** accessor method for height returns: height if n is not null, and 0 if null*/
  private int height(Node n){
    if (n == null){
      return -1;
    }
    return n.height;
  }

  /** compare the two heights of the children of n to determine the height of n 
   *  precondition: n cannot be null */
  private void updateHeight(Node n){
    if (n == null){
      return;
    }
    n.height = Math.max(height(n.left), height(n.right)) + 1;
  }

  /** insert w into the tree, maintaining AVL balance
  *  precondition: the tree is AVL balanced */
  public void avlInsert(String w) {
    if (root == null) {
      root = new Node(w);
      size = 1;
      return;
    }
    avlInsert(root, w);
  }

  /* insert w into the tree, maintaining AVL balance
   *  precondition: the tree is AVL balanced and n is not null */
  private void avlInsert(Node n, String w) {
    if (w.equals(n.word)){
      return;
    }
    else if (w.compareTo(n.word) < 0){
      if (n.left == null){
        n.left = new Node(w, n);
        updateHeight(n.left);
        size++;
      }
      avlInsert(n.left, w);
    }
    else if (w.compareTo(n.word) > 0){
      if (n.right == null){
        n.right = new Node(w, n);
        updateHeight(n.left);
        size++;
      }
      avlInsert(n.right, w);
    }
    updateHeight(n);
    rebalance(n);
  }

  /** do a left rotation: rotate on the edge from x to its right child.
  *  precondition: x has a non-null right child */
  public void leftRotate(Node x) {
    Node y = x.right;
    x.right = y.left;
    if (y.left != null){
      y.left.parent = x;
    }
    y.parent = x.parent;
    if (x.parent == null){
      root = y;
    }
    else if (x == x.parent.left){
      x.parent.left = y;
    }
    else{
      x.parent.right = y;
    }
    y.left = x;
    x.parent = y;
    //updates the tree from the bottom up
    updateHeight(x.left);
    updateHeight(x.right);
    updateHeight(x);
    updateHeight(y.left);
    updateHeight(y.right);
    updateHeight(y);
    updateHeight(root);
  }

  /** do a right rotation: rotate on the edge from x to its left child.
  *  precondition: y has a non-null left child */
  public void rightRotate(Node y) {
    Node x = y.left;
    y.left = x.right;
    if (x.right != null){
      x.right.parent = y;
    }
    x.parent = y.parent;
    if (y.parent == null){
      root = x;
    }
    else if (y == y.parent.right){
      y.parent.right = x;
    }
    else{
      y.parent.left = x;
    }
    x.right = y;
    y.parent = x;
    //updates the tree from the bottom up
    updateHeight(y.left);
    updateHeight(y.right);
    updateHeight(y);
    updateHeight(x.left);
    updateHeight(x.right);
    updateHeight(x);
    updateHeight(root);
  }

  /** rebalance a node N after a potentially AVL-violating insertion.
  *  precondition: none of n's descendants violates the AVL property */
  public void rebalance(Node n) {
    if (bal(n) < -1){
      if (bal(n.left) < 0){
        rightRotate(n);
      }
      else{
        leftRotate(n.left);
        rightRotate(n);
      }
    }
    else if (bal(n) > 1){
      if (bal(n.right) <= 0){
        rightRotate(n.right);
        leftRotate(n);
      }
      else{
        leftRotate(n);
      }
    }
  }

  /** checks the balance of an Node n
   *  precondition: n is not null */
  private int bal(Node n){
    return height(n.right) - height(n.left);
  }

  /** print a sideways representation of the tree - root at left,
  * right is up, left is down. */
  public void printTree() {
    printSubtree(root, 0);
  }
  private void printSubtree(Node n, int level) {
    if (n == null) {
      return;
    }
    printSubtree(n.right, level + 1);
    for (int i = 0; i < level; i++) {
      System.out.print("        ");
    }
    System.out.println(n);
    printSubtree(n.left, level + 1);
  }

  /** inner class representing a node in the tree. */
  public class Node {
    public String word;
    public Node parent;
    public Node left;
    public Node right;
    public int height;

    public String toString() {
      return word + "(" + height + ")";
    }

    /** constructor: gives default values to all fields */
    public Node() { }

    /** constructor: sets only word */
    public Node(String w) {
      word = w;
    }

    /** constructor: sets word and parent fields */
    public Node(String w, Node p) {
      word = w;
      parent = p;
    }

    /** constructor: sets all fields */
    public Node(String w, Node p, Node l, Node r) {
      word = w;
      parent = p;
      left = l;
      right = r;
    }
  }
}
