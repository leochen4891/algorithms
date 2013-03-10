public class Subset {
    public static void main(String[] args) {
        int size;
        if (args.length > 0) {
            size = Integer.parseInt(args[0]);
        } else {
            System.out.print("need a number\n");
            return;
        }

        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }

        int cnt = 0;
        for (String s:rq) {
            if (cnt >= size) break;
            System.out.print(s+"\n");
            cnt++;
        }
    }
}
