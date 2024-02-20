import java.lang.reflect.Array;

public class Factory < T > {
    Class < T > t;

    public Class < T > type() { // Return the type associated with this factory
        return t;
    }

    // Constructor - creates a factory for creating objects and
    // arrays of type t(=T)
    // t0
    public Factory(Class < T > t0) {
        t = t0;
    }

    // Allocate a new array of objects of type T.
    // n the size of the array to allocate
    // @return the array allocated
    @SuppressWarnings({"unchecked"})
    protected T[] newArray(int n) {
        return (T[]) Array.newInstance(t, n);
    }

    public T newInstance() {
        T x;
        try {
            x = t.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            x = null;
        }
        return x;
    }
}