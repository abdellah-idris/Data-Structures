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

    // Replaces the element at index i in the array a with the given element x and returns the original element
    public T set(int i, T x) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        T y = a[i];
        a[i] = x;
        return y;
    }

    // Inserts the element x at the specified index i in the array a
    public void add(int i, T x) {
        if (i < 0 || i > n) throw new IndexOutOfBoundsException();
        if (n + 1 > a.length) resize();
        //  Optimisation : System.arraycopy(a, i, a, i + 1, n - i);
        for (int j = n; j > i; j--)
            a[j] = a[j - 1];
        a[i] = x;
        n++;
    }

    // Removes and returns the element at the specified index i in the array a
    public T remove(int i) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        T x = a[i];
        // Optimisation : System.arraycopy(a, i + 1, a, i, n - i - 1);
        for (int j = i; j < n - 1; j++)
            a[j] = a[j + 1];
        n--;
        if (a.length >= 3 * n) resize();
        return x;
    }

    // Pops the top element from the stack
    public T remove() {
        T x = a[--n]; // Decreasing the count and then removing from the top
        if (a.length >= 3 * n) resize();
        return x;
    }

    /*
    * We can observe that resizeOptimized() performed significantly better in terms of time efficiency compared to resize().
    * resizeOptimized() took approximately 50% less time to execute compared to resize().*/

    // Remove last element from the array
    public static void main(String args[]) {
        // Creating an ArrayStack of Integers
        ArrayStack<Integer> stack = new ArrayStack<>(Integer.class);

        // Adding a large number of elements to the stack
        int numElements = 1000000; // Add 1 million elements
        for (int i = 0; i < numElements; i++) {
            stack.add(i);
        }

        // Measuring time taken by resize()
        long startTimeResize = System.nanoTime();
        stack.resize();
        long endTimeResize = System.nanoTime();
        long durationResize = (endTimeResize - startTimeResize);

        // Measuring time taken by resizeOptimized()
        long startTimeResizeOptimized = System.nanoTime();
        stack.resizeOptimized();
        long endTimeResizeOptimized = System.nanoTime();
        long durationResizeOptimized = (endTimeResizeOptimized - startTimeResizeOptimized);

        // Printing the durations
        System.out.println("Time taken by resize(): " + durationResize + " nanoseconds");
        System.out.println("Time taken by resizeOptimized(): " + durationResizeOptimized + " nanoseconds");
    }

}