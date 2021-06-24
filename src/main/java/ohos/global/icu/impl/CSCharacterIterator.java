package ohos.global.icu.impl;

import java.text.CharacterIterator;

public class CSCharacterIterator implements CharacterIterator {
    private int index;
    private CharSequence seq;

    public int getBeginIndex() {
        return 0;
    }

    public CSCharacterIterator(CharSequence charSequence) {
        if (charSequence != null) {
            this.seq = charSequence;
            this.index = 0;
            return;
        }
        throw new NullPointerException();
    }

    public char first() {
        this.index = 0;
        return current();
    }

    public char last() {
        this.index = this.seq.length();
        return previous();
    }

    public char current() {
        if (this.index == this.seq.length()) {
            return 65535;
        }
        return this.seq.charAt(this.index);
    }

    public char next() {
        if (this.index < this.seq.length()) {
            this.index++;
        }
        return current();
    }

    public char previous() {
        int i = this.index;
        if (i == 0) {
            return 65535;
        }
        this.index = i - 1;
        return current();
    }

    public char setIndex(int i) {
        if (i < 0 || i > this.seq.length()) {
            throw new IllegalArgumentException();
        }
        this.index = i;
        return current();
    }

    public int getEndIndex() {
        return this.seq.length();
    }

    public int getIndex() {
        return this.index;
    }

    @Override // java.lang.Object
    public Object clone() {
        CSCharacterIterator cSCharacterIterator = new CSCharacterIterator(this.seq);
        cSCharacterIterator.setIndex(this.index);
        return cSCharacterIterator;
    }
}
