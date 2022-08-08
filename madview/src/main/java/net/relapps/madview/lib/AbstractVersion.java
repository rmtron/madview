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

/**
 * Interface handling program version information. Use negative micro version to
 * indicate that it is not used.
 *
 * @author RMT
 */
public abstract class AbstractVersion {

    /**
     * Constructor.
     *
     * @param name The application name.
     * @param description Program short description.
     * @param copyright The copyright string.
     * @param version Version array containing major, minor and micro version
     * numbers.
     * @param versionDate Version date text string.
     * @param homepage Home page URL.
     */
    protected AbstractVersion(
            String name,
            String description,
            String copyright,
            Integer[] version,
            String versionDate,
            String homepage) {
        this(name,
                description,
                copyright,
                version,
                versionDate,
                homepage,
                "");
    }

    /**
     * Constructor.
     *
     * @param name The application name.
     * @param description Program short description.
     * @param copyright The copyright string.
     * @param version Version array containing major, minor and micro version
     * numbers.
     * @param versionDate Version date text string.
     * @param homepage Home page URL.
     * @param versionSuffix An optional version suffix string (beta etc.)
     */
    @SuppressWarnings("LeakingThisInConstructor")
    protected AbstractVersion(
            String name,
            String description,
            String copyright,
            Integer[] version,
            String versionDate,
            String homepage,
            String versionSuffix) {
        this(name,
                description,
                copyright,
                version,
                versionDate,
                homepage,
                versionSuffix, "");
    }

    /**
     * Constructor.
     *
     * @param name The application name.
     * @param description Program short description.
     * @param copyright The copyright string.
     * @param version Version array containing major, minor and micro version
     * numbers.
     * @param versionDate Version date text string.
     * @param homepage Home page URL.
     * @param versionSuffix An optional version suffix string (beta etc.)
     * @param email The publisher e-mail address.
     */
    protected AbstractVersion(
            String name,
            String description,
            String copyright,
            Integer[] version,
            String versionDate,
            String homepage,
            String versionSuffix,
            String email) {
        _name = name;
        _description = description;
        _copyright = copyright;
        if (version.length > 0) {
            _versionMajor = version[0];
            if (version.length > 1) {
                _versionMinor = version[1];
            }
            if (version.length > 2) {
                _versionMicro = version[2];
            }
        }
        _date = versionDate;
        _homepage = homepage;
        _versionSuffix = versionSuffix;
        _email = email;
    }

    /**
     * Return the copyright.
     *
     * @return The copyright text.
     */
    public String getCopyright() {
        return _copyright;
    }

    /**
     * Return the version date.
     *
     * @return The version date string.
     */
    public String getDate() {
        return _date;
    }

    /**
     * returns the program description text.
     *
     * @return The description.
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Returns the e-mail address.
     *
     * @return E-mail address.
     */
    public String getEMail() {
        return _email;
    }

    /**
     * Returns the home page.
     *
     * @return The home page URL as a string.
     */
    public String getHomePage() {
        return _homepage;
    }

    /**
     * Returns the major version number.
     *
     * @return The major version number.
     */
    public int getMajor() {
        return _versionMajor < 0 ? 0 : _versionMajor;
    }

    /**
     * Returns the micro version number.
     *
     * @return The micro version number.
     */
    public int getMicro() {
        return _versionMicro < 0 ? 0 : _versionMicro;
    }

    /**
     * Returns the minor version number.
     *
     * @return The minor version number.
     */
    public int getMinor() {
        return _versionMinor < 0 ? 0 : _versionMinor;
    }

    /**
     * Returns the program name.
     *
     * @return The program name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the program version without the program name.
     *
     * @return The program version.
     */
    public String getVersion() {
        StringBuilder ret = new StringBuilder();
        ret.append("v");
        if (_versionMajor >= 0) {
            ret.append(_versionMajor);
        }
        if (_versionMinor >= 0) {
            if (ret.length() != 1) {
                ret.append(".");
            }
            ret.append(_versionMinor);
        }
        if (_versionMicro >= 0) {
            if (ret.length() != 1) {
                ret.append(".");
            }
            ret.append(_versionMicro);
        }
        if (_versionSuffix != null && !_versionSuffix.isEmpty()) {
            ret.append(" ");
            ret.append(_versionSuffix);
        }
        return ret.toString();
    }

    public int getVersionNum() {
        int ret = getMajor() * 1000000;
        ret += getMinor() * 1000;
        ret += getMicro();
        return ret;
    }

    /**
     * Returns the version string including the program name.
     *
     * @return The version string.
     */
    public String getVersionString() {
        return getName() + " " + getVersion();
    }

    /**
     * Returns the version string as HTML.
     *
     * @return The version text as HTML.
     */
    public String getVersionStringHTML() {
        return "<html>" + "<strong>" + getName() + "</strong> " + getVersion()
                + " (" + getDate() + "), " + getCopyright() + "</html>";
    }

    @Override
    public String toString() {
        return getVersion();
    }

    private String _copyright;
    private String _date;
    private String _description;
    private String _email;
    private String _homepage;
    private String _name;
    private int _versionMajor = -1;
    private int _versionMicro = -1;
    private int _versionMinor = -1;
    private String _versionSuffix = "";
}
