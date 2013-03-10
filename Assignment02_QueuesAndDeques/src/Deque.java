import java.util.Iterator;
import java.util.NoSuchElementException;

// Throw a java.lang.NullPointerException if the client attempts to add a null item; 

// throw a java.util.NoSuchElementException if the client attempts to remove an 
//      item from an empty deque; 

// throw a java.lang.UnsupportedOperationException if the client calls the 
//      remove() method in the iterator; 

// throw a java.util.NoSuchElementException if the client calls the next() method 
//      in the iterator and there are no more items to return.

// Your deque implementation should support each deque operation in constant 
// worst-case time  and use space proportional to the number of items currently 
// in the deque. Additionally, your iterator implementation should support the 
// operations next()  and hasNext() (plus construction) in constant worst-case 
// time and  use a constant amount of extra space per iterator.

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item item;
        private Node next;
        private Node prev;

        public Node(Item itemIn) {
            item = itemIn;
            next = null;
            prev = null;
        }
    }

    private Node mHead;
    private Node mTail;
    private int mSize;

    // construct an empty deque
    public Deque() {
        mHead = null;
        mTail = null;
        mSize = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return 0 == mSize;
    }

    // return the number of items on the deque
    public int size() {
        return mSize;
    }

    // insert the item at the front
    public void addFirst(Item item) {
        if (null == item)
            throw new NullPointerException();
        Node newHead = new Node(item);
        if (0 >= mSize) {
            mHead = newHead;
            mTail = newHead;
        } else {
            newHead.next = mHead;
            mHead.prev = newHead;
            mHead = newHead;
        }
        mSize++;
    }

    // insert the item at the end
    public void addLast(Item item) {
        if (null == item)
            throw new NullPointerException();
        Node newTail = new Node(item);
        if (0 >= mSize) {
            mTail = newTail;
            mHead = newTail;
        } else {
            mTail.next = newTail;
            newTail.prev = mTail;
            mTail = newTail;
        }
        mSize++;
    }

    // delete and return the item at the front
    public Item removeFirst() {
        Item ret = null;
        if (0 >= mSize) {
            throw new NoSuchElementException();
        } else if (1 == mSize) {
            ret = mHead.item;
            mHead = null;
            mTail = null;
            mSize = 0;
        } else {
            ret = mHead.item;
            mHead = mHead.next;
            mHead.prev = null;
            mSize--;
        }
        return ret;
    }

    // delete and return the item at the end
    public Item removeLast() {
        Item ret = null;
        if (0 >= mSize) {
            throw new NoSuchElementException();
        } else if (1 == mSize) {
            ret = mTail.item;
            mHead = null;
            mTail = null;
            mSize = 0;
        } else {
            ret = mTail.item;
            mTail = mTail.prev;
            mTail.next = null;
            mSize--;
        }
        return ret;
    }

    // return an iterator over items in order from front to end
    @Override
    public Iterator<Item> iterator() {
        return new Itr(mHead);
    }

    private class Itr implements Iterator<Item> {
        private Node next;

        Itr(Node node) {
            next = node;
        }

        @Override
        public boolean hasNext() {
            return null != next;
        }

        @Override
        public Item next() {
            if (null == next)
                throw new NoSuchElementException();
            Item ret = next.item;
            next = next.next;
            return ret;
        }

        @Override
        public void remove() {
            // NOT implemented
            throw new UnsupportedOperationException();
        }
    }
}