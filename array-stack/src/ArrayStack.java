import java.util.AbstractList;
import java.util.Collection;
import java.lang.reflect.Array;

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
        for (int j = n; j > i; j--)
            a[j] = a[j - 1];
        a[i] = x;
        n++;
    }
    // Removes and returns the element at the specified index i in the array a
    public T remove(int i) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        T x = a[i];
        for (int j = i; j < n - 1; j++)
            a[j] = a[j + 1];
        n--;
        if (a.length >= 3 * n) resize();
        return x;
    }

    public static void main(String args[]) {

        ArrayStack < Integer > stack = new ArrayStack < > (Integer.class);
        // Adds the elements to the stack
        stack.add(0, 1);
        stack.add(1, 2);
        stack.add(2, 3);

        System.out.println("Stack size: " + stack.size());

        int removedElement = stack.remove(1);
        System.out.println("Removed element: " + removedElement);

        System.out.println("Stack elements:");
        for (int i = 0; i < stack.size(); i++) {
            System.out.println(stack.get(i));
        }
    }
}