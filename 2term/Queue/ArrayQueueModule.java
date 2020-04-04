package queue;

public class ArrayQueueModule {
    private static int start = 0, size = 0;
    private static Object[] elements = new Object[5];

    // :NOTE: queue is not cycled
    
    // pre: element ≠ null
    // post: n = n' + 1 ^ a[n] = element
    public static void enqueue(Object element) {
        assert element != null;

        ensureCapacity();
        elements[(start + size++) % elements.length] = element;
    }

    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i + 1] ∧ a[1] = element
    public static void push(Object element) {
        assert element != null;

        ensureCapacity();
        if (start == 0) {
            start = elements.length;
        }
        size++;
        elements[--start] = element;
    }

    // pre: capacity > 0
    // post: capacity = 2 * capacity'
    private static void ensureCapacity() {
        if (elements.length <= size) {
            Object[] cop = new Object[elements.length * 2];
            System.arraycopy(elements, start, cop, 0, elements.length - start);
            System.arraycopy(elements, 0, cop, elements.length - start, start);
            start = 0;
            elements = cop;
        }
    }

    // pre: n > 0
    // post: ℝ = a[n'] ∧ n = n' − 1
    public static Object remove() {
        assert size > 0;

        Object a = elements[(start + --size) % elements.length];
        elements[(start + size) % elements.length] = null;
        return a;
    }

    // pre: n > 0
    // post: ℝ = a[1] ∧ n = n' − 1 ∧ ∀i=1..n : a[i] = a[i + 1]'
    public static Object dequeue() {
        assert size > 0;

        Object a = elements[start];
        elements[start++] = null;
        if (start == elements.length) {
            start = 0;
        }
        size--;
        return a;
    }

    // pre: n > 0
    // peek: ℝ = a[n]
    public static Object peek() {
        assert size > 0;

        return elements[(start + size - 1) % elements.length];
    }

    // pre: n > 0
    // peek: ℝ = a[1]
    public static Object element() {
        assert size > 0;

        return elements[start];
    }

    // post: ℝ = n
    public static int size() {
        return size;
    }

    // post: ℝ = n > 0
    public static boolean isEmpty() {
        return size == 0;
    }

    // post: n = 0
    public static void clear() {
        elements = new Object[5];
        start = 0;
        size = 0;
    }
}
