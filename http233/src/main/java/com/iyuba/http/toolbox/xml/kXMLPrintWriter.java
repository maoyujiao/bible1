package com.iyuba.http.toolbox.xml;

import java.io.IOException;
import java.io.Writer;

public class kXMLPrintWriter extends Writer {
    private final Writer out;

    /**
     */

    public kXMLPrintWriter(Writer out) {
        super(out);
        this.out = out;
    }

    @Override
    public void close() {
        try {
            out.close();
        } catch (IOException e) {
        }
    }

    @Override
    public void flush() {
        try {
            out.flush();
        } catch (IOException e) {
        }
    }

    @Override
    public void write(int c) {
        try {
            out.write(c);
        } catch (IOException e) {
        }
    }

    @Override
    public void write(char buf[], int off, int len) {
        try {
            out.write(buf, off, len);
        } catch (IOException e) {
        }
    }

    @Override
    public void write(String s) {
        try {
            out.write(s, 0, s.length());
        } catch (IOException e) {
        }
    }

    /**
     */

    public void print(char ch) {
        write(String.valueOf(ch));
    }

    /**
     */

    private void newLine() {
        write('\n');
    }

    /**
     */

    public void print(String str) {
        if (str == null) {
            str = "null";
        }
        write(str);
    }

    /**
     */

    public void println(char ch) {
        print(ch);
        newLine();
    }

    /**
     */

    public void println(String str) {
        print(str);
        newLine();
    }
}
