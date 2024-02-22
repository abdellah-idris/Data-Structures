import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

// An implementation of the Queue<T> interface using an array (circular array)
/*
* The primary challenge in implementing a queue with an array arises from the fact that arrays have a fixed size.
* Unlike stacks, where elements are added and removed from the same end, queues require elements to be added at one end
* and removed from the other.
*  This means that if we use a regular array, adding or removing elements at one end would require shifting all other
* elements, resulting in a time complexity proportional to the number of elements in the array.*/

/*
* To overcome this challenge, the concept of modular arithmetic is introduced. Modular arithmetic involves performing
* arithmetic operations on integers, but only considering the remainder when dividing by a given modulus. In the context
* of ArrayQueue:
* The array is treated as if it were circular, with indices "wrapping around" when they exceed the array's length.
-   When adding an element, we calculate the index where it should be placed using modular arithmetic: (j + n) % a.length.

-   Similarly, when removing an element, we calculate the index from which it should be removed using modular arithmetic: j % a.length.
This approach allows us to efficiently add and remove elements from the ArrayQueue without needing to shift all elements in the array.
Overall, by leveraging modular arithmetic, we can simulate the behavior of an infinite array while using a finite array, enabling an efficient implementation of a queue data structure.
*/

public class ArrayQueue < T > extends AbstractQueue < T > {

    protected Factory < T > f; // The class of elements stored in this queue
    protected T[] a; // Array used to store elements
    protected int j; // Index of next element to de-queue
    protected int n; // Number of elements in the queue

    // Grow the internal array
    protected void resize() {
        T[] b = f.newArray(Math.max(1, n * 2));
        for (int k = 0; k < n; k++)
            b[k] = a[(j + k) % a.length];
        a = b;
        j = 0;
    }

    protected void resizeOptimized() {
        int newSize = Math.max(1, n * 2); // New size of the array
        T[] newArray = f.newArray(newSize); // Create a new array with the new size
        System.arraycopy(a, j, newArray, 0, Math.min(n, a.length - j)); // Copy elements from j to the end of the array
        System.arraycopy(a, 0, newArray, Math.min(n, a.length - j), Math.max(0, n - (a.length - j))); // Copy remaining elements from the beginning of the array
        a = newArray; // Update the reference to the new array
        j = 0; // Reset the index
    }


    public ArrayQueue(Class < T > t) { // Constructor
        f = new Factory < T > (t);
        a = f.newArray(1);
        j = 0;
        n = 0;
    }

    // Return an iterator for the elements of the queue.
    // This iterator does not support the remove operation
    public Iterator < T > iterator() {
        class QueueIterator implements Iterator < T > {
            int k;

            public QueueIterator() {
                k = 0;
            }

            public boolean hasNext() {
                return (k < n);
            }

            public T next() {
                if (k > n) throw new NoSuchElementException();
                T x = a[(j + k) % a.length];
                k++;
                return x;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
        return new QueueIterator();
    }
    // Returns the size of the array
    public int size() {
        return n;
    }
    // Adds the element x to the collection
    public boolean offer(T x) {
        return add(x);
    }
    // Adds the element x to the collection, resizing the array if necessary
    public boolean add(T x) {
        if (n + 1 > a.length) resize();
        a[(j + n) % a.length] = x;
        n++;
        return true;
    }
    // Retrieves the element at the front of the collection without removing it
    public T peek() {
        T x = null;
        if (n > 0) {
            x = a[j];
        }
        return x;
    }
    // Removes and returns the element at the front of the collection
    public T remove() {
        if (n == 0) throw new NoSuchElementException();
        T x = a[j];
        j = (j + 1) % a.length;
        n--;
        if (a.length >= 3 * n) resize();
        return x;
    }

    // Removes and returns the element at the front of the collection, or returns null if the
    public T poll() {
        return n == 0 ? null : remove();
    }

    public static void main(String args[]) {
        int m = 10000, n = 50;
        Queue < Integer > q = new ArrayQueue < Integer > (Integer.class);
        for (int i = 0; i < m; i++) {
            q.add(i);
            if (q.size() > n) {
                Integer x = q.remove();
                assert(x == i - n);
            }
        }
        System.out.println("After addition the queue contents are:");
        Iterator < Integer > it = q.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }


        System.out.println("\n Compare resize functions");
        int numElements = 1_000_000_0; // Number of elements to add to the queue
        // Create an ArrayQueue with the original resize function
        ArrayQueue<Integer> queue = new ArrayQueue<>(Integer.class);
        for (int i = 0; i < numElements; i++) {
            queue.add(i); // Add elements to the queue
        }

        // Measuring time taken by resize()
        long startTimeResize = System.nanoTime();
        queue.resize();
        long durationResize = (System.nanoTime() - startTimeResize);

        // Measuring time taken by resizeOptimized()
        long startTimeResizeOptimized = System.nanoTime();
        queue.resizeOptimized();
        long durationResizeOptimized = (System.nanoTime() - startTimeResizeOptimized);

        // Printing the durations
        System.out.println("Time taken by resize(): " + durationResize + " nanoseconds");
        System.out.println("Time taken by resizeOptimized(): " + durationResizeOptimized + " nanoseconds");
    }
}