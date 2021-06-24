package ohos.global.icu.impl;

import java.text.CharacterIterator;
import ohos.global.icu.text.UCharacterIterator;

public class UCharacterIteratorWrapper implements CharacterIterator {
    private UCharacterIterator iterator;

    public int getBeginIndex() {
        return 0;
    }

    public UCharacterIteratorWrapper(UCharacterIterator uCharacterIterator) {
        this.iterator = uCharacterIterator;
    }

    public char first() {
        this.iterator.setToStart();
        return (char) this.iterator.current();
    }

    public char last() {
        this.iterator.setToLimit();
        return (char) this.iterator.previous();
    }

    public char current() {
        return (char) this.iterator.current();
    }

    public char next() {
        this.iterator.next();
        return (char) this.iterator.current();
    }

    public char previous() {
        return (char) this.iterator.previous();
    }

    public char setIndex(int i) {
        this.iterator.setIndex(i);
        return (char) this.iterator.current();
    }

    public int getEndIndex() {
        return this.iterator.getLength();
    }

    public int getIndex() {
        return this.iterator.getIndex();
    }

    @Override // java.lang.Object
    public Object clone() {
        try {
            UCharacterIteratorWrapper uCharacterIteratorWrapper = (UCharacterIteratorWrapper) super.clone();
            uCharacterIteratorWrapper.iterator = (UCharacterIterator) this.iterator.clone();
            return uCharacterIteratorWrapper;
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }
}
