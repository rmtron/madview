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

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.relapps.madview.main.Version;

/**
 * About dialog controller.
 *
 * @author RMT
 */
public class AboutController {

    @FXML
    public void closeDialog() {
        _stage.close();
    }

    @FXML
    void initialize() {
        final String header = "About " + _version.getName();
        labelHeader.setText(header);
        labelApplication.setText(_version.getName());
        labelCopyright.setText(_version.getCopyright()
                + ", " + _version.getHomePage());
        labelVersion.setText(_version.getVersionString());
        Platform.runLater(() -> {
            Scene scene = labelHeader.getScene();
            _stage = (Stage) scene.getWindow();
            _stage.setTitle(header);
            _stage.addEventFilter(EventType.ROOT, ev -> {
                if (ev.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
                    closeDialog();
                }
            });
        });
    }
    private Stage _stage;
    private final Version _version = new Version();
    @FXML
    private Label labelApplication;
    @FXML
    private Label labelCopyright;
    @FXML
    private Label labelHeader;
    @FXML
    private Label labelVersion;
}
