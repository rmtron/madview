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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * Dialog base class
 *
 * @author RMT
 */
public abstract class Controller implements Initializable {

    /**
     *
     * @param parent
     * @param modal
     */
    public Controller(Controller parent, boolean modal) {
        _parent = parent;
        _modal = modal;
    }

    /**
     * Close the stage (windows/dialog).
     */
    public void close() {
        _stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        _stage.setTitle(title);
    }

    /**
     * Show the stage.
     */
    public void show() {
        _stage.show();
    }

    /**
     * Show and wait for the stage.
     */
    public void showAndWait() {
        _stage.showAndWait();
    }

    /**
     * Re-implement to handle window close requests. The default implementation
     * closes the stage.
     */
    protected void closeRequest() {
        _stage.close();
    }

    /**
     * Returns the stage.
     *
     * @return The stage.
     */
    protected Stage getStage() {
        return _stage;
    }

    private final boolean _modal;
    private final Controller _parent;
    private Stage _stage;
}
