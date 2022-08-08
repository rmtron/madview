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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 * Standard dialogs.
 *
 * @author RMT
 */
public class StdDialogs {

    private StdDialogs() {
    }

    /**
     * Returns the window for a node.
     *
     * @param node The node.
     * @return The window or null if not available.
     */
    public static Window getWindow(Node node) {
        Window ret = null;
        if (node != null) {
            Scene scene = node.getScene();
            if (scene != null) {
                Window window = scene.getWindow();
                if (window != null) {
                    ret = window;
                }
            }
        }
        return ret;
    }

    /**
     * Show a confirmation dialog.
     *
     * @param parent The dialog parent.
     * @param title Title.
     * @param header Header text.
     * @param message Message string.
     * @return True if OK is pressed.
     */
    public static boolean showConfirmation(Node parent, String title,
            String header, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        var answ = showDialog(alert, parent, title, header, message);
        if (answ.isPresent()) {
            return answ.get().getButtonData() == ButtonBar.ButtonData.YES
                    || answ.get().getButtonData()
                    == ButtonBar.ButtonData.OK_DONE;
        }
        return false;
    }

    /**
     * Show an error dialog.
     *
     * @param parent Parent node.
     * @param title Dialog title.
     * @param header Header text or empty.
     * @param message The message text.
     */
    public static void showError(Node parent, String title, String header,
            String message) {
        Alert alert = new Alert(AlertType.ERROR);
        showDialog(alert, parent, title, header, message);
    }

    /**
     * Show an exception.
     *
     * @param parent Parent node.
     * @param ex The exception to display.
     * @param msg An additional (optional) message.
     */
    public static void showException(Node parent, Throwable ex, String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        String callstack = getCallstack(ex);
        DialogPane pane = alert.getDialogPane();
        VBox expContent = new VBox();
        TextArea textArea = new TextArea(callstack);
        textArea.setEditable(false);
        expContent.getChildren().add(textArea);
        pane.setExpandableContent(expContent);
        String title = ex.getClass().getName();
        String header;
        String message;
        header = msg;
        if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
            message = ex.getMessage();
        } else {
            message = title;
        }
        showDialog(alert, parent, title, header, message);
    }

    /**
     * Show an info dialog.
     *
     * @param parent Parent node.
     * @param title Dialog title.
     * @param header Header text or empty.
     * @param message The message text.
     */
    public static void showInfo(Node parent, String title, String header,
            String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        showDialog(alert, parent, title, header, message);
    }

    /**
     * Show a warning dialog.
     *
     * @param parent Parent node.
     * @param title Dialog title.
     * @param header Header text or empty.
     * @param message The message text.
     */
    public static void showWarning(Node parent, String title, String header,
            String message) {
        Alert alert = new Alert(AlertType.WARNING);
        showDialog(alert, parent, title, header, message);
    }

    /**
     * Returns exception information as a string.
     *
     * @param ex The exception.
     * @return The exception information as a string.
     */
    private static String getCallstack(Throwable ex) {
        String ret;
        try ( ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PrintStream printStream = new PrintStream(out);
            ex.printStackTrace(printStream);
            ret = out.toString();
        } catch (Exception ex2) {
            ret = "N/A";
        }
        return ret;
    }

    private static void setParent(Alert alert, Node parent) {
        Window window = getWindow(parent);
        if (window != null) {
            alert.initOwner(window);
        }
    }

    private static Optional<ButtonType> showDialog(Alert alert, Node parent,
            String title,
            String header, String message) {
        setParent(alert, parent);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
