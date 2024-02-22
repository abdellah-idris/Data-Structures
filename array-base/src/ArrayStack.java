import java.util.AbstractList;


public class ArrayStack < T > extends AbstractList < T > {
    Factory < T > f; // keeps track of the class of objects we store
    T[] a; // The array used to store elements
    int n; // The number of elements stored

    protected void resize() { // Resize the internal array
        T[] b = f.newArray(Math.max(n * 2, 1));
        for (int i = 0; i < n; i++) {
            b[i] = a[i];
        }
        a = b;
    }

    void resizeOptimized() {
        T[] b = f.newArray(Math.max(2 * n, 1));
        System.arraycopy(a, 0, b, 0, n);
        a = b;
    }

    // Constructor t0 the type of objects that are stored in this list
    public ArrayStack(Class < T > t) {
        f = new Factory < T > (t);
        a = f.newArray(1);
        n = 0;
    }

    // Retrieves the element at the specified index i in the array a
    public T get(int i) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        return a[i];
    }

    // Returns the current size of the array
    public int size() {
        return n;
    }

    // Inserts the element x at the specified index i in the array a
    public void push(T x) {
        if (n + 1 > a.length) resize();
        //  Optimisation : System.arraycopy(a, i, a, i + 1, n - i);
        a[n] = x;
        n++;
    }

    // Pops the top element from the stack
    public T pop() {
        T x = a[n-1]; // Decreasing the count and then removing from the top
        a[--n]=null;
        if (a.length >= 3 * n) resize();
        return x;
    }

    private boolean empty(){
        return n == 0;
    }

    private T peek(){
        return a[n-1];
    }

    private boolean search(T x){
        for (T t : a) {
            if (t == x) return true;
        }
        return false;
    }

    /*
    * We can observe that resizeOptimized() performed significantly better in terms of time efficiency compared to resize().
    * resizeOptimized() took approximately 50% less time to execute compared to resize().*/

    // Remove last element from the array
    public static void main(String args[]) {

        System.out.println("Stack Operations");




        ArrayStack < Integer > stack = new ArrayStack < > (Integer.class);
        System.out.println("empty State: " + stack.empty());
        // Adds the elements to the stack
        stack.push(1);
        stack.push(2);
        stack.push(3);

        System.out.println("Stack number of elements: " + stack.size());
        System.out.println("empty State: " + stack.empty());
        System.out.println("Peek: " + stack.peek());

        System.out.println("element 3: " + stack.search(3));

        int removedElement = stack.pop();
        System.out.println("Removed element: " + removedElement);


        System.out.println("element 3: " + stack.search(3));


        System.out.println("Stack elements:");
        for (int i = 0; i < stack.size(); i++) {
            System.out.println(stack.get(i));
        }


        System.out.println("Stack Optimisation");

        // Creating an ArrayStack of Integers
        ArrayStack<Integer> stackOptimisation = new ArrayStack<>(Integer.class);

        // Adding a large number of elements to the stack
        int numElements = 1_000_000; // Add 1 million elements
        for (int i = 0; i < numElements; i++) {
            stackOptimisation.push(i);
        }

        // Measuring time taken by resize()
        long startTimeResize = System.nanoTime();
        stackOptimisation.resize();
        long durationResize = (System.nanoTime() - startTimeResize);

        // Measuring time taken by resizeOptimized()
        long startTimeResizeOptimized = System.nanoTime();
        stackOptimisation.resizeOptimized();
        long durationResizeOptimized = (System.nanoTime() - startTimeResizeOptimized);

        // Printing the durations
        System.out.println("Time taken by resize(): " + durationResize + " nanoseconds");
        System.out.println("Time taken by resizeOptimized(): " + durationResizeOptimized + " nanoseconds");
    }

}