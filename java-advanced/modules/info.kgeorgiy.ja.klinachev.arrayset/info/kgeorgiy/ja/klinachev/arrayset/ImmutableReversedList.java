package info.kgeorgiy.ja.klinachev.arrayset;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

public class ImmutableReversedList<E> extends AbstractList<E> implements List<E>, RandomAccess {
    private final List<E> data;

    public ImmutableReversedList(List<E> data) {
        this.data = data;
    }

    public List<E> reverseList() {
        return data;
    }

    @Override
    public int size() {
        return data.size();
    }

    private int reverseIndex(int index) {
        return size() - index - 1;
    }

    @Override
    public E get(int index) {
        return data.get(reverseIndex(index));
    }

}
