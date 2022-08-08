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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collection of file utilities.
 *
 * @author RMT
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * Copy a directory, create the destination directory if necessary.
     *
     * @param sourceDir the source dir
     * @param targetDir the target dir
     * @throws java.io.IOException Thrown on error.
     */
    public static void copyDirectory(File sourceDir, File targetDir)
            throws IOException {

        //Create targetDir (created even no files in source dir)
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        //Get files from sourceDir and copy all
        for (File sourceFile : sourceDir.listFiles()) {

            //If directory, call recursive
            if (sourceFile.isDirectory()) {
                copyDirectory(sourceFile, new File(targetDir.getAbsolutePath()
                                                           + File.separator
                                                           + sourceFile.
                              getName()));
            } else {
                //Copy file
                copyFile(sourceFile, new File(targetDir.getAbsolutePath()
                                                      + File.separator
                                                      + sourceFile.
                         getName()));
            }
        }
    }

    /**
     * Copy a file, create the destination directory if necessary.
     *
     * @param source The source file.
     * @param destination The destination file.
     * @throws java.io.IOException Thrown on error.
     */
    public static void copyFile(File source, File destination)
            throws IOException {
        // Check if the destination directory exists.
        File parentDir = destination.getParentFile();
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Not able to create directory: "
                                              + parentDir.getCanonicalPath());
            }
        }

        try (InputStream in = new FileInputStream(source)) {
            try (OutputStream out = new FileOutputStream(destination)) {
                copyStream(in, out);
            }
        }
    }

    /**
     * Copy the contents from one stream to another. The streams are not closed
     * after this call.
     *
     * @param in The input stream.
     * @param out The output stream.
     * @return The number of bytes copied.
     * @throws java.io.IOException Thrown on error.
     */
    public static long copyStream(InputStream in, OutputStream out)
            throws IOException {
        return copyStream(in, out, null);
    }

    /**
     * Copy the contents from one stream to another. The streams are not closed
     * after this call.
     *
     * @param in The input stream.
     * @param out The output stream.
     * @param progressListener A progress listener. The maximum value (total
     * number of bytes) must be configured before calling this method.
     * @return The number of bytes copied.
     * @throws java.io.IOException Thrown on error.
     */
    @SuppressWarnings("NestedAssignment")
    public static long copyStream(InputStream in, OutputStream out,
                                  IProgressListener progressListener)
            throws IOException {
        long total = 0;
        byte[] buffer = new byte[1024 * 128];
        int length;

        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
            total += length;
            if (progressListener != null) {
                progressListener.setValue((int) total);
            }
        }
        return total;
    }

    /**
     * Delete a file or a directory with all its contents.
     *
     * @param entry The entry to delete.
     * @return True if the entry was successfully deleted.
     */
    public static boolean deleteRecursive(File entry) {
        if (entry.exists()) {
            if (entry.isDirectory()) {
                File files[] = entry.listFiles();
                for (File f : files) {
                    deleteRecursive(f);
                }
            }
            return entry.delete();
        }
        return false;
    }

    /**
     * Get extension of a file
     *
     * @param file The file
     * @return The extension of the file
     */
    public static String getExtension(File file) {
        String rv = null;

        String strFileName = file.getName();

        if (strFileName.lastIndexOf('.') != -1) {
            rv = strFileName.substring(strFileName.lastIndexOf('.') + 1);
        }

        return rv;
    }

    /**
     * Get get file name without extension.
     *
     * @param file The file.
     * @return The file name without extension.
     */
    public static String getNameWithoutExtension(File file) {
        return getNameWithoutExtension(file.getName());
    }

    /**
     * Get get file name without extension.
     *
     * @param file The file name.
     * @return The file name without extension, if no extension found
     * <i>file</i> is returned.
     */
    public static String getNameWithoutExtension(String file) {
        String rv = file;

        if (file.lastIndexOf('.') != -1) {
            rv = file.substring(0, file.lastIndexOf('.'));
        }

        return rv;
    }

    /**
     * Returns the paths contained in the given path argument.
     *
     * @param path The path.
     * @return A list of the paths starting with the root.
     */
    @SuppressWarnings("NestedAssignment")
    public static List<File> getPathList(File path) {
        List<File> paths = new ArrayList<>();
        File parent = path;
        if (!parent.getName().equals(".")) {
            paths.add(parent);
        }
        while ((parent = parent.getParentFile()) != null) {
            if (!parent.getName().equals(".")) {
                paths.add(parent);
            }
        }
        Collections.reverse(paths);
        return paths;
    }

    /**
     * Find the relative path between two absolute paths, the root and the given
     * entry.
     *
     * @param root The root directory.
     * @param entry The entry.
     * @return The entry path relative to root, or null if the given entries are
     * not absolute paths or on different drives.
     */
    public static String getRelativePath(File root, File entry) {
        StringBuilder relPath = null;
        if (root.isAbsolute() && entry.isAbsolute()) {
            List<File> rootList = getPathList(root);
            List<File> entryList = getPathList(entry);

            // Check if the same root, same drive on Windows, if not mal asunto
            if (rootList.get(0).equals(entryList.get(0))) {
                relPath = new StringBuilder();
                int min;
                if (rootList.size() < entryList.size()) {
                    min = rootList.size();
                } else {
                    min = entryList.size();
                }

                // First eliminate common root
                int index = -1;
                for (int n = 0; n < min; n++) {
                    File rp = rootList.get(n);
                    File ep = entryList.get(n);
                    if (!rp.equals(ep)) {
                        index = n;
                        break;
                    }
                }
                if (index < 0) {
                    index = min;
                }

                for (int n = index; n < rootList.size(); n++) {
                    relPath.append("..");
                    relPath.append(File.separator);
                }

                int size = entryList.size();
                for (int n = index; n < size; n++) {
                    relPath.append(entryList.get(n).getName());
                    if (n < size - 1) {
                        relPath.append(File.separator);
                    }
                }
            }
        }
        return relPath != null ? relPath.toString() : null;
    }

    /**
     * Check if two files are different by reading the contents.
     *
     * @param file1 File 1
     * @param file2 File 2
     * @return True if the files are different (file contents).
     * @throws java.io.IOException Thrown on error.
     */
    public static boolean isDifferent(File file1, File file2)
            throws IOException {
        if (!file1.exists()) {
            throw new FileNotFoundException("File not found: " + file1.
                    getCanonicalPath());
        }

        if (!file2.exists()) {
            throw new FileNotFoundException("File not found: " + file2.
                    getCanonicalPath());
        }

        boolean diff = false;
        long srcLen = file1.length();
        long dstLen = file2.length();
        if (srcLen != dstLen) {
            diff = true;
        }

        if (!diff) {
            FileInputStream f1 = new FileInputStream(file1);
            FileInputStream f2 = new FileInputStream(file2);

            // The files have the same size.
            long size = file1.length();
            for (long n = 0; n < size; n++) {
                int b1 = f1.read();
                int b2 = f2.read();
                if (b1 != b2) {
                    diff = true;
                    break;
                }
            }
        }
        return diff;
    }

    /**
     * Read a file and return the contents as a byte array.
     *
     * @param file The file to read.
     * @return The file contents.
     * @throws java.io.IOException Thrown on error.
     */
    public static byte[] readFile(File file) throws IOException {
        byte[] buffer;
        try (FileInputStream in = new FileInputStream(file)) {
            long lsize = file.length();
            int size = (int) lsize;
            buffer = new byte[size];
            in.read(buffer);
        }
        return buffer;
    }

    /**
     * Write a byte vector to a file.
     *
     * @param file The output file.
     * @param buffer The buffer to write.
     * @throws IOException Thrown on IO error.
     */
    public static void writeFile(File file, byte[] buffer) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(buffer);
        }
    }
}
