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
package net.relapps.fx;

import javafx.scene.web.WebView;

/**
 * WebView utilities.
 *
 * @author RMT
 */
public class WebViewHelper {

    public WebViewHelper(WebView webView) {
        _webView = webView;
    }

    /**
     * Returns the maximum horizontal scroll value. This is equivalent to
     * {@link javafx.scene.control.ScrollBar#getMax()}.
     *
     * @return horizontal scroll max
     */
    public int getHScrollMax() {
        return (Integer) _webView.getEngine().executeScript(
                "document.body.scrollHeight");
    }

    /**
     * Returns the horizontal scroll value, i.e.thumb position. This is
     * equivalent to {@link javafx.scene.control.ScrollBar#getValue()}.
     *
     * @return horizontal scroll value
     */
    public int getHScrollValue() {
        return (Integer) _webView.getEngine().executeScript(
                "document.body.scrollLeft");
    }

    /**
     * Returns the maximum vertical scroll value.This is equivalent to
     * {@link javafx.scene.control.ScrollBar#getMax()}.
     *
     * @return vertical scroll max
     */
    public int getVScrollMax() {
        return (Integer) _webView.getEngine().executeScript(
                "document.body.scrollWidth");
    }

    /**
     * Returns the vertical scroll value, i.e. thumb position. This is
     * equivalent to {@link javafx.scene.control.ScrollBar#getValue().
     *
     * @return vertical scroll value
     */
    public int getVScrollValue() {
        return (Integer) _webView.getEngine().executeScript(
                "document.body.scrollTop");
    }

    /**
     * Scrolls to the specified position.
     *
     * @param hScroll horizontal scroll value
     * @param vScroll vertical scroll value
     */
    public void scrollTo(int hScroll, int vScroll) {
        _webView.getEngine().
                executeScript("window.scrollTo("
                        + hScroll + ", " + vScroll + ")");
    }

    private final WebView _webView;
}
