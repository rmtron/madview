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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.relapps.fx.WebViewHelper;

/**
 * FXML Controller class
 *
 * @author RMT
 */
public class HtmlViewerController implements Initializable {

    /**
     * Returns the HTML text.
     *
     * @return The HTML text.
     */
    public String getHTML() {
        return _html;
    }
    //extends Controller {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (_html != null) {
            setHTML(_html);
        }
        _webViewHelper = new WebViewHelper(htmlViewer);
        WebEngine webEngine = htmlViewer.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener(
                new HyperLinkRedirectListener(htmlViewer));
    }

    /**
     * Assign the HTML to show.
     *
     * @param html The HTML.
     */
    public void setHTML(String html) {
        if (htmlViewer != null) {
            int hScroll = _webViewHelper.getHScrollValue();
            int vScroll = _webViewHelper.getVScrollValue();
            WebEngine engine = htmlViewer.getEngine();
            engine.loadContent(html);
            _html = html;
            // Scroll to the same position after assigning new contents.
            Platform.runLater(() -> {
                _webViewHelper.scrollTo(hScroll, vScroll);
            });
        } else {
            this._html = html;
        }
    }
    private String _html = null;
    private WebViewHelper _webViewHelper = null;
    @FXML
    private WebView htmlViewer;
}
