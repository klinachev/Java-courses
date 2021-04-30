package info.kgeorgiy.ja.klinachev.arrayset;

import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private final List<E> data;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this.data = Collections.emptyList();
        this.comparator = null;
    }

    public ArraySet(Collection<? extends E> data) {
        this(data, null);
    }

    public ArraySet(Collection<? extends E> data, Comparator<? super E> comparator) {
        TreeSet<E> t = new TreeSet<>(comparator);
        t.addAll(data);
        this.data =  List.copyOf(t);
        this.comparator = comparator;
    }

    public ArraySet(List<E> data, Comparator<? super E> comparator) {
        this.data = data;
        this.comparator = comparator;
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @SuppressWarnings("unchecked")
    private int compare(E o1, E o2) {
        if (comparator != null) {
            return comparator.compare(o1, o2);
        } else {
            return ((Comparable<E>) o1).compareTo(o2);
        }
    }

    private boolean equals(E o1, E o2) {
        if (o1 == null || o2 == null) {
            return false;
        }
        if (comparator != null) {
            return comparator.compare(o1, o2) == 0;
        } else {
            return o1.equals(o2);
        }
    }

    private NavigableSet<E> subSet(int from, boolean fromInclusive, int to, boolean toInclusive) {
        if (!fromInclusive) {
            from++;
        }
        if (toInclusive) {
            to++;
        }
        if (from < 0 || to < from) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        return new ArraySet<>(data.subList(from, to), comparator);
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        return data.get(0);
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        return data.get(size() - 1);
    }

    @Override
    public int size() {
        return data.size();
    }

    private int binarySearch(E value) {
        return Collections.binarySearch(data, value, comparator);
    }

    private int insertionPosition(E value) {
        int index = binarySearch(value);
        if (index < 0) {
            return -index - 1;
        }
        return index;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        E obj = (E) o;
        int index = binarySearch(obj);
        return index >= 0;
    }

    private int findWithShift(E value, int ifFound, int ifNotFound) {
        int index = binarySearch(value);
        return index >= 0 ? index + ifFound : -index - 1 + ifNotFound;
    }

    private boolean checkIndex(int index) {
        return index >= 0 && index < size();
    }

    private E getOrNull(int index) {
        return checkIndex(index) ? data.get(index) : null;
    }

    @Override
    public E lower(E e) {
        int i = findWithShift(e, -1, -1);
        return getOrNull(i);
    }

    @Override
    public E floor(E e) {
        int i = findWithShift(e, 0, -1);
        return getOrNull(i);
    }

    @Override
    public E ceiling(E e) {
        int i = findWithShift(e, 0, 0);
        return getOrNull(i);
    }

    @Override
    public E higher(E e) {
        int i = findWithShift(e, 1, 0);
        return getOrNull(i);
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return data.iterator();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        if (data instanceof ImmutableReversedList) {
            return new ArraySet<>(((ImmutableReversedList<E>) data).reverseList(), Collections.reverseOrder(comparator));
        }
        return new ArraySet<>(new ImmutableReversedList<>(data), Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    private boolean fromInclusiveUpdate(boolean fromInclusive, int from, E fromElement) {
        return fromInclusive || (from < size() && !equals(data.get(from), fromElement));
    }

    private boolean toInclusiveUpdate(boolean toInclusive, int to, E toElement) {
        return toInclusive && to < size() && equals(data.get(to), toElement);
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException("fromElement less than toElement");
        }
        int from = insertionPosition(fromElement);
        int to = insertionPosition(toElement);
        if (from < 0 || to < from || to > size()) {
            throw new IllegalArgumentException("Invalid range: [" + from + ", " + to + "]");
        }
        return subSet(from,
                fromInclusiveUpdate(fromInclusive, from, fromElement),
                to,
                toInclusiveUpdate(toInclusive, to, toElement));
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        int to = insertionPosition(toElement);
        return subSet(0, true, to, toInclusiveUpdate(inclusive, to, toElement));
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        int from = insertionPosition(fromElement);
        return subSet(from, fromInclusiveUpdate(inclusive, from, fromElement), size(), false);
    }

}
