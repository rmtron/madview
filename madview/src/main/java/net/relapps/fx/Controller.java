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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
     *
     * @param clazz
     */
    @SuppressWarnings("CallToPrintStackTrace")
    protected void createDialog(Class<?> clazz) {
        try {
            createDialogP(clazz);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the stage.
     *
     * @return The stage.
     */
    protected Stage getStage() {
        return _stage;
    }

    private void createDialogP(Class<?> clazz) throws IOException {
        String className = clazz.getName();
        StringBuilder fxml = new StringBuilder();
        fxml.append('/');
        fxml.append(className.replace('.', '/'));
        fxml.append(".fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(fxml.toString()));

        fxmlLoader.setController(this);
        Parent root = (Parent) fxmlLoader.load();

        // Scene scene = new Scene(root, 300, 250);
        Scene scene = new Scene(root);

        _stage = new Stage();

        if (_modal && _parent != null) {
            _stage.initModality(Modality.WINDOW_MODAL);
        }
        if (_parent != null) {
            _stage.initOwner(_parent.getStage());
            setLocationToParent();
        }

        _stage.setOnCloseRequest((WindowEvent e) -> {
            e.consume();
            closeRequest();
        });
        _stage.setScene(scene);
    }

    private void setLocationToParent() {
        // Stage parentStage = parent.getStage();
        Stage childStage = getStage();

        PositionChildAction bar = new PositionChildAction();
        childStage.setOnShown(bar);
    }
    private final boolean _modal;
    private final Controller _parent;
    private Stage _stage;

    private class PositionChildAction implements EventHandler<WindowEvent> {

        PositionChildAction() {
        }

        @Override
        public void handle(WindowEvent event) {
            Stage childStage = getStage();

            Double cw = childStage.getWidth();
            Double ch = childStage.getHeight();

            Stage parentStage = _parent.getStage();
            Double pw = parentStage.getWidth();
            Double ph = parentStage.getHeight();
            Double px = parentStage.getX();
            Double py = parentStage.getY();

            double pxc = (pw / 2.0) + px;
            double pyc = (ph / 2.0) + py;

            double x = pxc - (cw / 2.0);
            double y = pyc - (ch / 2.0);
            childStage.setX(x);
            childStage.setY(y);
        }
    }
}
