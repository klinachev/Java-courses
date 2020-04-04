package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    // Post: new Object of same class
    private Queue sameClass() throws IllegalAccessException, InstantiationException {
        return this.getClass().newInstance();
    }
    // pre: ∀i=1..n --> (predicate(a[i]) == true || predicate(a[i]) == false)
	// post: mas: type(mas) == type(a), 
	//		∀i: predicate(a[i]) == true --> ∃j: mas[j] == a[i],
	//		∀i1 < i2: predicate(a[i1]) == true ^ predicate(a[i2]) == true --> ∃j1 < j2: mas[j1] == a[i1], mas[j2] == a[i2]
    public Queue filter(Predicate<Object> pred) throws IllegalAccessException, InstantiationException {
        // :NOTE: where is parameterization?
        // :NOTE: use of deprecated methods?
        Queue a = sameClass();
        for (int i = size; i > 0; i--) {
            Object b = this.dequeue();
            if (pred.test(b)) {
                a.enqueue(b);
            }
            this.enqueue(b);
        }
        return a;
    }
    
    // pre: ∀i=1..n --> ∃function(a[i])
	// post: mas: type(mas) == type(a), 
	//		∀i=1..n --> mas[i] == function(a[i])
    public Queue map(Function<Object, Object> func) throws IllegalAccessException, InstantiationException {
        // :NOTE: copy-paste code for new instance of queue
        Queue a = sameClass();
        for (int i = size; i > 0; i--) {
            Object b = this.dequeue();
            a.enqueue(func.apply(b));
            this.enqueue(b);
        }
        return a;
    }

    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i] ∧ a[n] = element
    public void enqueue(Object element) {
        assert element != null;
        pushImpl(element);
        size++;
    }

    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i] ∧ a[n] = element
    protected abstract void pushImpl(Object element);

    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i + 1] ∧ a[1] = element
    public Object element() {
        assert size > 0;

        return elementImpl();
    }

    // pre: element ≠ null
    // post: n = n' + 1 ∧ ∀i=1..n' : a[i]' = a[i + 1] ∧ a[1] = element
    protected abstract Object elementImpl();

    // pre: n > 0
    // post: ℝ = a[1] ∧ n = n' − 1 ∧ ∀i=1..n : a[i]' = a[i - 1]
    public Object dequeue() {
        assert size > 0;

        Object result = elementImpl();
        size--;
        dequeueImpl();
        return result;
    }

    // pre: n > 0
    // post: n = n' − 1 ∧ ∀i=1..n : a[i]' = a[i - 1]
    protected abstract void dequeueImpl();

    // post: n = 0
    public void clear() {
        while (size > 0) {
            dequeue();
        }
    }

    // post: R: R = n
    public int size() {
        return size;
    }

    // post: true <-> n == 0
    public boolean isEmpty() {
        return size == 0;
    }
}

