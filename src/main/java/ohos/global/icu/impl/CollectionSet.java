package ohos.global.icu.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class CollectionSet<E> implements Set<E> {
    private final Collection<E> data;

    public CollectionSet(Collection<E> collection) {
        this.data = collection;
    }

    public int size() {
        return this.data.size();
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public boolean contains(Object obj) {
        return this.data.contains(obj);
    }

    @Override // java.util.Collection, java.util.Set, java.lang.Iterable
    public Iterator<E> iterator() {
        return this.data.iterator();
    }

    public Object[] toArray() {
        return this.data.toArray();
    }

    @Override // java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.data.toArray(tArr);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean add(E e) {
        return this.data.add(e);
    }

    public boolean remove(Object obj) {
        return this.data.remove(obj);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> collection) {
        return this.data.containsAll(collection);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> collection) {
        return this.data.addAll(collection);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean retainAll(Collection<?> collection) {
        return this.data.retainAll(collection);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean removeAll(Collection<?> collection) {
        return this.data.removeAll(collection);
    }

    public void clear() {
        this.data.clear();
    }
}
