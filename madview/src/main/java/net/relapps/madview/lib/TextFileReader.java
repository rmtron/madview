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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Utility for reading a text file. The default encoding is UTF8.
 *
 * @author RMT
 */
public class TextFileReader implements Closeable {

    /**
     * Create a new instance and open file for reading.
     *
     * @param file The file to open.
     * @throws FileNotFoundException Thrown on file open error.
     * @throws UnsupportedEncodingException Thrown on error in encoding.
     */
    public TextFileReader(File file)
            throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream fileInpStream = new FileInputStream(file);
        InputStreamReader inpStreamReader
                = new InputStreamReader(fileInpStream,
                        "UTF8");
        _stream = new BufferedReader(inpStreamReader);
    }

    /**
     * Read the contents from an InputStream (UTF-8) and return the contents as
     * a string.
     *
     * @param inStream The input stream.
     * @return The text read.
     * @throws FileNotFoundException Thrown if file not found.
     * @throws UnsupportedEncodingException Thrown on unsupported encoding.
     * @throws IOException Thrown on read error.
     */
    @SuppressWarnings("NestedAssignment")
    public static String read(InputStream inStream)
            throws UnsupportedEncodingException, IOException {
        StringBuilder text = new StringBuilder();
        InputStreamReader inpStreamReader
                = new InputStreamReader(inStream, "UTF8");
        try (BufferedReader in = new BufferedReader(inpStreamReader)) {
            String str;
            while ((str = in.readLine()) != null) {
                text.append(str);
                text.append('\n');
            }
        }
        return text.toString();
    }

    /**
     * Read the contents of a file (UTF-8) and return it as a string.
     *
     * @param file The file to read.
     * @return The text read.
     * @throws FileNotFoundException Thrown if file not found.
     * @throws UnsupportedEncodingException Thrown on unsupported encoding.
     * @throws IOException Thrown on read error.
     */
    @SuppressWarnings("NestedAssignment")
    public static String readFile(File file) throws FileNotFoundException,
            UnsupportedEncodingException, IOException {
        try (FileInputStream fileInpStream = new FileInputStream(file)) {
            return read(fileInpStream);
        }
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
     * Read a line from the file. Do not include line terminating char(s).
     *
     * @return The string read or null on end of file.
     * @throws IOException Thrown on IO error.
     */
    public String readLine() throws IOException {
        if (_stream == null) {
            throw new Error("File not open.");
        }
        return _stream.readLine();
    }
    private BufferedReader _stream = null;
}
