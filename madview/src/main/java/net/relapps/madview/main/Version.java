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

import net.relapps.madview.lib.AbstractVersion;

/**
 * Handles version information.
 *
 * @author RMT
 */
public class Version extends AbstractVersion {

    public Version() {
        super(NAME, DESCRIPTION, COPYRIGHT, VERSION,
                DATE, HOMEPAGE, VERSION_SUFFIX, EMAIL);
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String... args) {
        Version ver = new Version();
        System.out.println(ver.getVersionString() + " (" + ver.getDate() + ")");
        System.out.println(ver.getDescription());
        System.out.println(ver.getCopyright());
        System.out.println("Home page: " + ver.getHomePage());
        System.out.println("E-mail: " + ver.getEMail());
    }

    /**
     * Returns the URL with the version information.
     *
     * @return The URL.
     */
    public String getVersionURL() {
        return "https://relapps.net/madview/current.json";
    }
    private static final String COPYRIGHT = "© 2022 RELapps.net";
    private static final String DATE = "15/AUG/2022";
    private static final String DESCRIPTION = "Markdown viewer and editor";
    private static final String EMAIL = "info@relapps.net";
    private static final String HOMEPAGE
            = "https://relapps.net/web/madview.html";
    private static final String NAME = "madview";
    private static final Integer[] VERSION = {0, 4};
    private static final String VERSION_SUFFIX = "";
}
