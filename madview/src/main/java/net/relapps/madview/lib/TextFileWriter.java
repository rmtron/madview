/*
 * Copyright(c) 2022 RELapps.net
 * https://relapps.net
 *
 * This source code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * https://github.com/rmtron/madview/blob/main/LICENSE
 */
package net.relapps.madview.lib;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Utility for writing a text file. The default encoding is UTF8.
 *
 * @author RMT
 */
public class TextFileWriter implements Closeable, Appendable {

    /**
     * Create a new instance and open file for writing.
     *
     * @param file The file to open.
     * @throws FileNotFoundException Thrown on file open error.
     * @throws UnsupportedEncodingException Thrown on error in encoding.
     */
    public TextFileWriter(File file)
            throws UnsupportedEncodingException, FileNotFoundException {
        FileOutputStream fileOutStream = new FileOutputStream(file);
        _stream = new OutputStreamWriter(fileOutStream, "UTF8");
    }

    /**
     * Create a new instance and open file for writing.
     *
     * @param file The file to open.
     * @throws FileNotFoundException Thrown on file open error.
     * @throws UnsupportedEncodingException Thrown on error in encoding.
     */
    public TextFileWriter(String file)
            throws UnsupportedEncodingException, FileNotFoundException {
        this(new File(file));
    }

    /**
     * Write a string to a file.
     *
     * @param file The output file.
     * @param text The text to write.
     * @throws FileNotFoundException Thrown if path not found.
     * @throws UnsupportedEncodingException Thrown on unsupported encoding.
     * @throws IOException Thrown on write error.
     *
     */
    public static void writeFile(File file, String text)
            throws FileNotFoundException, UnsupportedEncodingException,
            IOException {
        try (FileOutputStream fileOutStream = new FileOutputStream(file)) {
            try (OutputStreamWriter outStreamWriter = new OutputStreamWriter(
                    fileOutStream, "UTF8")) {
                outStreamWriter.write(text);
            }
        }
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
        _stream.write(csq.toString());
        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws
            IOException {
        CharSequence text = csq.subSequence(start, end);
        append(text);
        return this;
    }

    @Override
    public Appendable append(char c) throws IOException {
        _stream.append(c);
        return this;
    }

    /**
     * Close the file.
     *
     * @throws IOException Thrown on error.
     */
    @Override
    public void close() throws IOException {
        if (_stream != null) {
            _stream.close();
            _stream = null;
        }
    }

    /**
     * Write a text line to the stream.
     *
     * @param text The text to write, without line terminating char(s).
     * @throws IOException Thrown on IO error.
     */
    public void writeLine(String text) throws IOException {
        _stream.write(text);
        _stream.write("\n");
    }
    private OutputStreamWriter _stream = null;
}
