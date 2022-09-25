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
package net.relapps.madview.cntrl;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.scene.Node;
import net.relapps.fx.StdDialogs;

/**
 *
 * @author RMT
 */
public class OSUtilities {

    private OSUtilities() {
    }

    /**
     * Open a link/URL in the default system browser.
     *
     * @param parent The parent node, used for showing error message.
     * @param url The URL to open.
     */
    public static void openURLInSystemBrowser(Node parent, String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                openInSystemBrowser(parent, url);
            } catch (UnsupportedOperationException ex) {
                try {
                    var proc = Runtime.getRuntime().exec("xdg-open " + url);
                    proc.waitFor();
                } catch (IOException | InterruptedException ex2) {
                }

            }
        } else {
            StdDialogs.showError(parent, "Error",
                    "", "Not possible to open URL");
            try {
                var proc = Runtime.getRuntime().exec("xdg-open " + url);
                proc.waitFor();
            } catch (IOException | InterruptedException ex) {
            }
        }
    }

    /**
     * Open a link/URL in the default system browser.
     *
     * @param parent The parent node, used for showing error message.
     * @param url The URL to open.
     */
    private static void openInSystemBrowser(Node parent, String url) {
        try {
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException ex) {
            StdDialogs.showException(parent, ex, "Error opening URL");
        }
    }
}
