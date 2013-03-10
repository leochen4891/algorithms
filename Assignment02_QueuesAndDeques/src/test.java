import java.util.Iterator;


public class test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        
        Integer i;
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addFirst(5);
        
        i = deque.removeLast();
        i = deque.removeLast();
        i = deque.removeLast();
        i = deque.removeLast();
        i = deque.removeLast();
        
    }

}
