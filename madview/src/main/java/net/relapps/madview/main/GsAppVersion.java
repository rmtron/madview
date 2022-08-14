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
package net.relapps.madview.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * Version information stored on relapps.net.
 *
 * @author RMT
 */
public class GsAppVersion {

    /**
     * Download an URL.
     *
     * @param url The URL.
     * @return The content as a string.
     * @throws IOException Thrown on error.
     */
    public static GsAppVersion getCurrentVersion(String url) throws IOException {
        StringBuilder out = new StringBuilder();
        try ( BufferedInputStream is = new BufferedInputStream(new URL(url).
                openStream())) {
            try ( Reader in = new InputStreamReader(is, "UTF-8")) {
                char[] buffer = new char[1024];
                for (;;) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0) {
                        break;
                    }
                    out.append(buffer, 0, rsz);
                }
            }
        }
        if (!out.isEmpty()) {
            Gson gson = new Gson();
            return gson.fromJson(out.toString(), GsAppVersion.class);
        } else {
            return null;
        }
    }

    /**
     * Check if a new version is available.
     *
     * @return The new version information or null if no new version.
     */
    public static GsAppVersion isNewVersionAvailable() {
        try {
            Version version = new Version();
            var url = version.getVersionURL();
            var appVersion = getCurrentVersion(url);
            if (appVersion != null) {
                boolean newVersion = false;
                if (appVersion.getMajor() > version.getMajor()) {
                    newVersion = true;
                } else if (appVersion.getMajor() == version.getMajor()) {
                    if (appVersion.getMinor() > version.getMinor()) {
                        newVersion = true;
                    } else if (appVersion.getMinor() == version.getMinor()) {
                        newVersion = appVersion.getMicro() > version.getMicro();
                    }
                }
                if (newVersion) {
                    return appVersion;
                }
            }
        } catch (IOException ex) {
            // Silently skip if problems with reading version file.
        }
        return null;
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String args[]) throws IOException {
        Version version = new Version();
        var url = version.getVersionURL();
        var ver = getCurrentVersion(url);

        System.out.println("From server " + url);
        System.out.println(ver);

        System.out.println("\nCurrent application version");
        var current = new Version();
        var gsver = new GsAppVersion();
        System.out.println(current.getVersionString());
        gsver.setDate(current.getDate());
        gsver.setMajor(current.getMajor());
        gsver.setMinor(current.getMinor());
        gsver.setMicro(current.getMicro());
        gsver.setVersionString(current.getVersion());
        gsver.setDownloadURL(current.getHomePage());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(gsver);
        System.out.println(json);

        var newVersion = isNewVersionAvailable();
        if (newVersion != null) {
            System.out.println("New version found:");
            System.out.println(newVersion);
        }
    }

    public String getDate() {
        return date;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public int getMajor() {
        return major;
    }

    public int getMicro() {
        return micro;
    }

    public int getMinor() {
        return minor;
    }

    public String getVersionString() {
        return versionString;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMicro(int micro) {
        this.micro = micro;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("Date: ");
        ret.append(getDate());
        ret.append('\n');
        ret.append("URL: ");
        ret.append(getDownloadURL());
        ret.append('\n');
        ret.append("Major: ");
        ret.append(getMajor());
        ret.append('\n');
        ret.append("Minor: ");
        ret.append(getMinor());
        ret.append('\n');
        ret.append("Micro: ");
        ret.append(getMicro());
        ret.append('\n');
        ret.append("Version: ");
        ret.append(getVersionString());
        return ret.toString();
    }
    private String date;
    private String downloadURL;
    private int major;
    private int micro;
    private int minor;
    private String versionString;
}
