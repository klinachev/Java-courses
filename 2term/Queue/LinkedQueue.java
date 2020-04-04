package queue;


public class LinkedQueue extends AbstractQueue {
    private Node head = new Node(null), end = head;


    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i] ∧ a[n] = element
    protected void pushImpl(Object element) {
        head.value = element;
        head.next = new Node(null);
        head = head.next;
    }

    // pre: n > 0
    // post: n = n' − 1 ∧ ∀i=1..n : a[i]' = a[i - 1]
    protected void dequeueImpl() {
        end.value = null;
        end = end.next;
    }

    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i + 1] ∧ a[1] = element
    protected Object elementImpl() {
        return end.value;
    }

    // post: n = 0
    public void clearImpl() {
        head = new Node(null);
        end = head;
    }

    private class Node {
        private Object value = null;
        private Node next;

        public Node(Object value) {
            this.value = value;
        }
    }
}

