package ohos.data.orm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;

public class Clob implements java.sql.Clob {
    private StringBuffer buffer = new StringBuffer();

    public Clob(String str) {
        this.buffer.append(str);
    }

    @Override // java.sql.Clob
    public long length() {
        return (long) this.buffer.length();
    }

    @Override // java.sql.Clob
    public String getSubString(long j, int i) {
        boolean z = false;
        checkConditionWithThrowException(j > 0 && i >= 0);
        int i2 = ((int) j) - 1;
        int i3 = i + i2;
        checkConditionWithThrowException(((long) i2) < length());
        if (((long) i3) <= length()) {
            z = true;
        }
        checkConditionWithThrowException(z);
        return this.buffer.substring(i2, i3);
    }

    @Override // java.sql.Clob
    public Reader getCharacterStream() {
        return new StringReader(toString());
    }

    @Override // java.sql.Clob
    public InputStream getAsciiStream() {
        return new ByteArrayInputStream(toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override // java.sql.Clob
    public long position(String str, long j) {
        boolean z = false;
        checkConditionWithThrowException(str != null);
        checkConditionWithThrowException(j > 0);
        if (j <= length()) {
            z = true;
        }
        checkConditionWithThrowException(z);
        int indexOf = this.buffer.indexOf(str, ((int) j) - 1);
        if (indexOf == -1) {
            return -1;
        }
        return (long) (indexOf + 1);
    }

    private void checkConditionWithThrowException(boolean z) {
        if (!z) {
            throw new IllegalArgumentException();
        }
    }

    @Override // java.sql.Clob
    public long position(java.sql.Clob clob, long j) {
        checkConditionWithThrowException(clob != null);
        try {
            return position(clob.getSubString(1, (int) clob.length()), j);
        } catch (SQLException unused) {
            throw new IllegalArgumentException();
        }
    }

    @Override // java.sql.Clob
    public int setString(long j, String str) {
        checkConditionWithThrowException(str != null);
        return setString(j, str, 0, str.length());
    }

    @Override // java.sql.Clob
    public int setString(long j, String str, int i, int i2) {
        boolean z = false;
        checkConditionWithThrowException(str != null);
        checkConditionWithThrowException(j > 0 && i >= 0);
        int i3 = i + i2;
        checkConditionWithThrowException(i3 <= str.length());
        int i4 = ((int) j) - 1;
        checkConditionWithThrowException(((long) i4) <= length());
        try {
            String substring = str.substring(i, i3);
            int length = substring.length() + i4;
            if (((long) length) <= length()) {
                z = true;
            }
            checkConditionWithThrowException(z);
            this.buffer.replace(i4, length, substring);
            return i2;
        } catch (StringIndexOutOfBoundsException unused) {
            throw new IllegalArgumentException();
        }
    }

    @Override // java.sql.Clob
    public OutputStream setAsciiStream(long j) {
        boolean z = true;
        checkConditionWithThrowException(j > 0);
        int i = ((int) j) - 1;
        if (((long) i) >= length()) {
            z = false;
        }
        checkConditionWithThrowException(z);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(toString().getBytes(StandardCharsets.UTF_8), 0, i);
        return byteArrayOutputStream;
    }

    @Override // java.sql.Clob
    public Writer setCharacterStream(long j) {
        checkConditionWithThrowException(j > 0);
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        int i = ((int) j) - 1;
        if (((long) i) < length()) {
            charArrayWriter.write(toString(), 0, i);
        }
        return charArrayWriter;
    }

    @Override // java.sql.Clob
    public void truncate(long j) {
        checkConditionWithThrowException(j > 0 && j <= length());
        StringBuffer stringBuffer = this.buffer;
        stringBuffer.delete((int) j, stringBuffer.length());
    }

    @Override // java.sql.Clob
    public void free() {
        this.buffer = new StringBuffer();
    }

    @Override // java.sql.Clob
    public Reader getCharacterStream(long j, long j2) {
        checkConditionWithThrowException(j2 > 0);
        return new StringReader(getSubString(j, (int) j2));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return toString().equals(((Clob) obj).toString());
    }

    public int hashCode() {
        return Objects.hashCode(toString());
    }

    public String toString() {
        return this.buffer.toString();
    }
}
