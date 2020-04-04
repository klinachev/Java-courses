package queue;

public class ArrayQueueADT {
    private /*static*/ int start = 0, size = 0;
    private /*static*/ Object[] elements = new Object[5];

    // pre: element ≠ null ^ queue != null
    // post: n = n' + 1 ^ a[n] = element
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue);
        queue.elements[(queue.start + queue.size++) % queue.elements.length] = element;
    }

    // pre: element ≠ null ^ queue != null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i + 1] ∧ a[1] = element
    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapacity(queue);
        if (queue.start == 0) {
            queue.start = queue.elements.length;
        }
        queue.size++;
        queue.elements[--queue.start] = element;
    }

    // pre: capacity > 0 ^ queue != null
    // post: capacity = 2 * capacity'
    private static void ensureCapacity(ArrayQueueADT queue) {
        if (queue.elements.length <= queue.size) {
            Object[] cop = new Object[queue.elements.length * 2];
            System.arraycopy(queue.elements, queue.start, cop, 0, queue.elements.length - queue.start);
            System.arraycopy(queue.elements, 0, cop, queue.elements.length - queue.start, queue.start);
            queue.start = 0;
            queue.elements = cop;
        }
    }

    // pre: n > 0 ^ queue != null
    // post: ℝ = a[n'] ∧ n = n' − 1
    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0;

        Object a = queue.elements[(queue.start + --queue.size) % queue.elements.length];
        queue.elements[(queue.start + queue.size) % queue.elements.length] = null;
        return a;
    }

    // pre: n > 0 ^ queue != null
    // post: ℝ = a[1] ∧ n = n' − 1 ∧ ∀i=1..n : a[i] = a[i + 1]'
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        Object a = queue.elements[queue.start];
        queue.elements[queue.start++] = null;
        if (queue.start == queue.elements.length) {
            queue.start = 0;
        }
        queue.size--;
        return a;
    }

    // pre: n > 0 ^ queue != null
    // peek: ℝ = a[n]
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elements[(queue.start + queue.size - 1) % queue.elements.length];
    }

    // pre: n > 0 ^ queue != null
    // peek: ℝ = a[1]
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elements[queue.start];
    }

    // pre: queue != null
    // post: ℝ = n
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // pre: queue != null
    // post: ℝ = n > 0
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // pre: queue != null
    // post: n = 0
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[5];
        queue.start = 0;
        queue.size = 0;
    }
}
