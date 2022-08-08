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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import net.relapps.fx.StdDialogs;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

/**
 * Listen to click on hyperlinks in the web viewer.
 *
 * @author RMT
 */
public class HyperLinkRedirectListener implements ChangeListener<Worker.State>,
        EventListener {

    public HyperLinkRedirectListener(WebView webView) {
        this.webView = webView;
    }

    @Override
    public void changed(ObservableValue<? extends Worker.State> observable,
            Worker.State oldValue, Worker.State newValue) {
        if (Worker.State.SUCCEEDED.equals(newValue)) {
            Document document = webView.getEngine().getDocument();
            NodeList anchors = document.getElementsByTagName(ANCHOR_TAG);
            for (int i = 0; i < anchors.getLength(); i++) {
                Node node = anchors.item(i);
                EventTarget eventTarget = (EventTarget) node;
                eventTarget.addEventListener(CLICK_EVENT, this, false);
            }
        }
    }

    @Override
    public void handleEvent(Event event) {
        // TODO cleanup this method.
        if (event.getCurrentTarget() instanceof HTMLAnchorElement anchorElement) {
            event.preventDefault();
            String href = anchorElement.getHref();

            if (Desktop.isDesktopSupported()) {
                try {
                    openLinkInSystemBrowser(href);
                } catch (UnsupportedOperationException ex) {
                    try {
                        var proc = Runtime.getRuntime().exec("xdg-open " + href);
                        proc.waitFor();
                    } catch (IOException | InterruptedException ex2) {
                    }

                }
            } else {
                StdDialogs.showError(webView, "Error", "",
                        "Not possible to open URL");
                // xdg-open https://relapps.net/
                try {
                    var proc = Runtime.getRuntime().exec("xdg-open " + href);
                    proc.waitFor();
                } catch (IOException | InterruptedException ex) {
                }
            }
        }
    }

    private void openLinkInSystemBrowser(String url) {
        try {
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException e) {
            StdDialogs.showException(webView, e, "Error opening URL");
        }
    }
    private static final String ANCHOR_TAG = "a";
    private static final String CLICK_EVENT = "click";
    private final WebView webView;
}
