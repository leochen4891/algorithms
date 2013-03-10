import java.util.Iterator;
import java.util.NoSuchElementException;

// Throw a java.lang.NullPointerException if the client attempts to add a null item;

// throw a java.util.NoSuchElementException if the client attempts to sample or
//      dequeue an item from an empty randomized queue; 

// throw a java.lang.UnsupportedOperationException if the client calls the 
//      remove() method in the iterator; 

// throw a java.util.NoSuchElementException if the client calls the next() 
//      method in the iterator and there are no more items to return.

// Your randomized queue implementation should support each randomized queue 
// operation (besides creating an iterator) in constant amortized time and use 
// space proportional to the number of items currently in the queue. That is,
// any sequence of M randomized queue operations (starting from an empty queue) 
// should take at most cM steps in the worst case, for some constant c.
// Additionally, your iterator implementation should support construction in time 
// linear in the number of items and it should support the operations next() 
// and hasNext() in constant worst-case time; you may use a linear amount of extra 
// memory per iterator. The order of two or more iterators to the same randomized 
// queue should be mutually independent; each iterator must maintain its own 
// random order.
public class RandomizedQueue<Item> implements Iterable<Item> {

    private int INIT_SIZE = 10;
    private Item[] mItems;
    private int mSize;

    // construct an empty randomized queue
    public RandomizedQueue() {
        mItems = (Item[]) new Object[INIT_SIZE];
        mSize = 0;
        StdRandom.setSeed(System.nanoTime()+Thread.currentThread().getId());
    }

    // is the queue empty?
    public boolean isEmpty() {
        return 0 == mSize;
    }

    // return the number of items on the queue
    public int size() {
        return mSize;
    }

    // add the item
    public void enqueue(Item item) {
        if (null == item)
            throw new NullPointerException();
        // resize
        if (mSize >= mItems.length) {
            Item[] newArray = (Item[]) new Object[mItems.length * 2];
            for (int i = 0; i < mSize; i++) {
                newArray[i] = mItems[i];
            }
            mItems = newArray;
        }

        // random location to insert
        int position;
        if (0 == mSize) {
            position = 0;
        } else {
            position = StdRandom.uniform(0, mSize);
        }
        mItems[mSize] = mItems[position];
        mItems[position] = item;
        mSize++;
    }

    // delete and return a random item
    public Item dequeue() {
        if (0 == mSize)
            throw new NoSuchElementException();
        int position = StdRandom.uniform(0, mSize);
        Item ret = mItems[position];
        mItems[position] = mItems[--mSize];

        // resize
        if (mSize < mItems.length / 3) {
            Item[] newArray = (Item[]) new Object[mItems.length / 2];
            for (int i = 0; i < mSize; i++) {
                newArray[i] = mItems[i];
            }
            mItems = newArray;
        }
        return ret;
    }

    // return (but do not delete) a random item
    public Item sample() {
        if (0 == mSize) {
            throw new NoSuchElementException();
        }
        return mItems[StdRandom.uniform(0, mSize)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Itr(mItems, mSize);
    }

    private class Itr implements Iterator<Item> {
        private Item[] mItems;
        private int mSize;

        private int[] mRandSeq;
        private int mCur;

        Itr(Item[] items, int size) {
            mItems = items;
            mSize = size;
            mCur = 0;

            // init random sequence
            mRandSeq = new int[size];
            for (int i = 0; i < size; i++) {
                mRandSeq[i] = i;
            }
            
            StdRandom.setSeed(System.nanoTime()+Thread.currentThread().getId());

            // randomize
            int pos;
            for (int i = 0; i < size; i++) {
                pos = StdRandom.uniform(i + 1);
                swap(pos, i);
            }
        }

        @Override
        public boolean hasNext() {
            return mCur < mSize;
        }

        @Override
        public Item next() {
            if (mCur >= mSize)
                throw new NoSuchElementException();
            Item ret = mItems[mRandSeq[mCur]];
            mCur++;
            return ret;
        }

        @Override
        public void remove() {
            // NOT implemented
            throw new UnsupportedOperationException();
        }

        private void swap(int x, int y) {
            Item temp = mItems[x];
            mItems[x] = mItems[y];
            mItems[y] = temp;
        }
    }
}