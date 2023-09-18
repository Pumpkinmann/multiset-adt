import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// Define the MultiSet interface
interface MultiSet<T> {
    boolean add(T item);
    void remove(T item);
    boolean contains(T item);
    boolean isEmpty();
    int count(T item);
    int size();
}

// Define the TreeNode class
class TreeNode<T> {
    T item;
    List<TreeNode<T>> children;

    TreeNode(T item) {
        this.item = item;
        this.children = new ArrayList<>();
    }
}

// Define the TreeMultiSet class
class TreeMultiSet<T> implements MultiSet<T> {
    private TreeNode<T> root;

    TreeMultiSet() {
        this.root = null;
    }

    @Override
    public boolean add(T item) {
        if (root == null) {
            root = new TreeNode<>(item);
        } else {
            insert(root, item);
        }
        return true; // always returns true!
    }

    private void insert(TreeNode<T> node, T item) {
        if (item.equals(node.item)) {
            // Item already exists, add it as a child
            node.children.add(new TreeNode<>(item));
        } else {
            for (TreeNode<T> child : node.children) {
                insert(child, item);
            }
        }
    }

    @Override
    public void remove(T item) {
        if (root != null) {
            remove(root, item);
        }
    }

    private void remove(TreeNode<T> node, T item) {
        Iterator<TreeNode<T>> iterator = node.children.iterator();
        while (iterator.hasNext()) {
            TreeNode<T> child = iterator.next();
            if (item.equals(child.item)) {
                iterator.remove();
            } else {
                remove(child, item);
            }
        }
    }

    @Override
    public boolean contains(T item) {
        return contains(root, item);
    }

    private boolean contains(TreeNode<T> node, T item) {
        if (node == null) {
            return false;
        }
        if (item.equals(node.item)) {
            return true;
        }
        for (TreeNode<T> child : node.children) {
            if (contains(child, item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public int count(T item) {
        return count(root, item);
    }

    private int count(TreeNode<T> node, T item) {
        if (node == null) {
            return 0;
        }
        int count = 0;
        if (item.equals(node.item)) {
            count++;
        }
        for (TreeNode<T> child : node.children) {
            count += count(child, item);
        }
        return count;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(TreeNode<T> node) {
        if (node == null) {
            return 0;
        }
        int count = 1; // Count the root node
        for (TreeNode<T> child : node.children) {
            count += size(child);
        }
        return count;
    }
}

// Define the ArrayListMultiSet class
class ArrayListMultiSet<T> implements MultiSet<T> {
    private List<T> list;

    ArrayListMultiSet() {
        this.list = new ArrayList<>();
    }

    @Override
    public boolean add(T item) {
        list.add(item);
        return true;
    }

    @Override
    public void remove(T item) {
        list.remove(item);
    }

    @Override
    public boolean contains(T item) {
        return list.contains(item);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int count(T item) {
        int count = 0;
        for (T element : list) {
            if (item.equals(element)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int size() {
        return list.size();
    }
}

// Define the LinkedListMultiSet class
class LinkedListMultiSet<T> implements MultiSet<T> {
    private Node<T> front;
    private int size;

    LinkedListMultiSet() {
        this.front = null;
        this.size = 0;
    }

    @Override
    public boolean add(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = front;
        front = newNode;
        size++;
        return true;
    }

    @Override
    public void remove(T item) {
        Node<T> current = front;
        Node<T> previous = null;
        while (current != null) {
            if (item.equals(current.item)) {
                size--;
                if (previous != null) {
                    previous.next = current.next;
                } else {
                    front = current.next;
                }
                return;
            }
            previous = current;
            current = current.next;
        }
    }

    @Override
    public boolean contains(T item) {
        Node<T> current = front;
        while (current != null) {
            if (item.equals(current.item)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int count(T item) {
        int count = 0;
        Node<T> current = front;
        while (current != null) {
            if (item.equals(current.item)) {
                count++;
            }
            current = current.next;
        }
        return count;
    }

    @Override
    public int size() {
        return size;
    }

    private static class Node<T> {
        T item;
        Node<T> next;

        Node(T item) {
            this.item = item;
            this.next = null;
        }
    }
}

//public class Main {
//    public static void main(String[] args) {
//        List<MultiSet<Integer>> multiSets = Arrays.asList(new TreeMultiSet<>(), new ArrayListMultiSet<>(), new LinkedListMultiSet<>());
//
//        for (MultiSet<Integer> multiSet : multiSets) {
//            for (int n : new int[]{500, 1000, 2000, 4000}) {
//                profileMultiSet(multiSet, n);
//            }
//        }
//    }
//
//    public static void profileMultiSet(MultiSet<Integer> multiSet, int n) {
//        List<Integer> itemsAdded = new ArrayList<>();
//        for (int i = 0; i < n; i++) {
//            int x = ThreadLocalRandom.current().nextInt(0, 101);
//            multiSet.add(x);
//            itemsAdded.add(x);
//        }
//
//        assert multiSet.size() == n;
//
//        long start = System.nanoTime();
//
//        for (int x : itemsAdded) {
//            multiSet.remove(x);
//        }
//
//        long end = System.nanoTime();
//
//        assert multiSet.isEmpty();
//
//        System.out.printf("%5d %s %.6f%n", n, multiSet.getClass().getSimpleName(), (end - start) / 1e9);
//    }
//}
