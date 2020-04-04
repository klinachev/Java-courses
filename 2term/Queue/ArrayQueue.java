package queue;


public class ArrayQueue extends AbstractQueue {
    private int start = 0;
    private Object[] elements = new Object[5];


    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i] ∧ a[n] = element
    protected void pushImpl(Object element) {
        ensureCapacity();
        elements[(start + size) % elements.length] = element;
    }

    // pre: capacity > 0
    // post: capacity = 2 * capacity'
    private void ensureCapacity() {
        if (elements.length <= size) {
            Object[] cop = new Object[elements.length * 2];
            System.arraycopy(elements, start, cop, 0, elements.length - start);
            System.arraycopy(elements, 0, cop, elements.length - start, start);
            start = 0;
            elements = cop;
        }
    }

    // pre: n > 0
    // post: n = n' − 1 ∧ ∀i=1..n : a[i] = a[i + 1]'
    public void dequeueImpl() {
        elements[start++] = null;
        if (start == elements.length) {
            start = 0;
        }
    }

    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i + 1] ∧ a[1] = element
    public Object elementImpl() {
        return elements[start];
    }

    // post: n = 0
    public void clear() {
        elements = new Object[5];
        start = 0;
        size = 0;
    }

    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i + 1] ∧ a[1] = element
    public void push(Object element) {
        assert element != null;

        ensureCapacity();
        if (start == 0) {
            start = elements.length;
        }
        size++;
        elements[--start] = element;
    }

    // pre: n > 0
    // peek: ℝ = a[n]
    public Object peek() {
        assert size > 0;

        return elements[(start + size - 1) % elements.length];
    }

    // pre: n > 0
    // post: ℝ = a[n'] ∧ n = n' − 1
    public Object remove() {
        assert size > 0;

        Object a = elements[(start + --size) % elements.length];
        elements[(start + size) % elements.length] = null;
        return a;
    }
}
