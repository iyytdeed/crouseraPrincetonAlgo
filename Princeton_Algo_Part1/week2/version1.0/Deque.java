import java.util.*;
import java.lang.*;

public class Deque<Item> implements Iterable<Item> {
    private Node head;
    private Node tail;
    private int size;

    private class Node {
        Node preNode; 
        Item item;
        Node nextNode;
        public Node(Item item_au){
            item = item_au;
            preNode = null;
            nextNode = null;
        }
    }

    private class ListIterator implements Iterator<Item> { 
        Node cur;
        public ListIterator(){
            cur = head;
        }
        @Override
        public boolean hasNext() {
            return cur != null;
        }
        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("there are no more items!");
            Item item = cur.item;
            cur = cur.nextNode;
            return item;
        }
        public void remove(){
            throw new UnsupportedOperationException("iter.remove()");
        }
    }
    public Deque() {
        size = 0;
        head = null;
        tail = null;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public void addFirst(Item item) {
        if(item == null) throw new IllegalArgumentException("addFirst() wrong item");
        Node newNode = new Node(item);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }
        else {
            newNode.nextNode = head;
            head.preNode = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(Item item) {
        if(item == null) throw new IllegalArgumentException("addLast() wrong item");
        Node newNode = new Node(item);
        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            tail.nextNode = newNode;
            newNode.preNode = tail;
            tail = newNode;
        }
        size++;
    }
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("the deque is empty!");
        Item returnItem = null;
        if (size == 1) {
            returnItem = head.item;
            head = null;
            tail = null;
        }
        else {
            Node oldHead = head;
            returnItem = oldHead.item;
            head = oldHead.nextNode;
            head.preNode = null;
            oldHead.nextNode = null;
            oldHead.item = null;
        }
        size--;
        return returnItem;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("the deque is empty!");
        Item returnItem = null;
        if (size == 1) {
            returnItem = tail.item;
            head = null;
            tail = null;
        } 
        else {
            Node oldTail = tail;
            returnItem = oldTail.item;
            tail = oldTail.preNode;
            tail.nextNode = null;
            oldTail.preNode = null;
            oldTail.item = null;
        }
        size--;
        return returnItem;
    }
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    public static void main(String[] args) {
        // unit testing (optional)
        Deque<String> queue = new Deque<String>();
        System.out.println(queue.size);
        queue.addFirst("a");
        queue.addFirst("b");
        queue.addLast("c");
        queue.addFirst("d");
        queue.addLast("e");
        System.out.println(queue.size);
        Iterator<String> iter = queue.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}