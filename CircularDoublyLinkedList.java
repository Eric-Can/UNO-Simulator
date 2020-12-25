/*
 *Circular Doubly Linked List
 *
 * Data structure that contains the properties of a circular and a doubly linked list
 *
 */
public class CircularDoublyLinkedList<E> {
    private static class Node<E> { //nested node class
        private E element;
        private Node<E> prev;
        private Node<E> next;
        public Node(E e, Node<E> p, Node<E> n){//constructor
            element = e;
            next = n;
            prev = p;
        }
        public E getElement() {return element; }//getters & setters
        public Node<E> getPrev() { return prev; }
        public Node<E> getNext() { return next; }
        public void setPrev(Node<E> p) {prev = p;}
        public void setNext(Node<E> n) { next = n;}
    }
    private boolean forward = true; //controlling which way the loop moves. true = forward, false = backwards
    private Node<E> tail = null;
    private int size = 0;
    public CircularDoublyLinkedList() {} //constructor
    public int size() {return size;}
    public boolean isEmpty() { return size == 0; }//checks if empty
    public E first() {//returns the first element
        if(isEmpty()) return null;
        if(size() == 1) return tail.getElement();//if size is 1, then the tail is the first element
        //if(!forward) return tail.getElement();//once reversed, tail is now the first element
        return tail.getNext().getElement();
    }
    public E last () {//returns the last element
        if(isEmpty()) return null;
        if(size() == 1) return tail.getElement(); //if size is 1, then the tail is the last element
        return tail.getElement();
    }
    public void reverse() { //reverses the direction of the linked list
        forward = !forward; //flips the current direction of the list
    }

    public void rotate() {//rotates the tail of the linked list
        if(size() > 1 && tail != null && forward) tail = tail.getNext(); //only rotate if there is more than 1 element
        else if (size() > 1 && tail != null) tail = tail.getPrev();//if list is reversed, changed which direction to rotate the tail
    }
    public void addFirst(E e){ //adds a node to the front of the linked list
        if(this.size() == 0) { //if the size is 0, the new node will be the first one and will point to itself
            tail = new Node<>(e,null,null);
            tail.setNext(tail);
            tail.setPrev(tail);
            size++;
        }
        else if(this.size() == 1) {//when the new node is added, the 2 nodes will point to each other
            addBetween(e,tail,tail);
        }
        else {//if more than 2 elements, then you can place a node between 2 nodes
            addBetween(e, tail, tail.getNext());
        }
    }
    public void addLast(E e){ //adds a node to the front of the linked list
        this.addFirst(e);
        tail = tail.getNext();
    }
    public E removeFirst(){ //removes a node from the front of the linked list
        if(isEmpty()) return null;
        return remove(tail.getNext());
    }

    public E removeLast() { //removes a node from the end of the linked list
        if(isEmpty()) return null;
        tail = tail.getPrev();//prev node is now tail
        return remove(tail);
    }
    private void addBetween(E e, Node<E> predecessor, Node<E> successor){ //add a node between 2 other nodes
        Node<E> newest = new Node<>(e,predecessor, successor);
        if(predecessor != null) { //if predecessor is null, you can't set anything
            predecessor.setNext(newest);
        }
        if(successor != null) { //if successor is null, you can't set anything
            successor.setPrev(newest);
        }
        size++;
    }
    private E remove(Node<E> node) { //removes a node from the linked list
        Node<E> predecessor = node.getPrev();
        Node<E> successor = node.getNext();
        predecessor.setNext(successor); //change where the neighbouring nodes are points to remove the reference of the given node
        successor.setPrev(predecessor);
        size--;
        return node.getElement();
    }
}
