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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import net.relapps.fx.Loader;
import net.relapps.fx.StdDialogs;
import net.relapps.madview.lib.TextFileReader;
import net.relapps.madview.lib.TextFileWriter;
import net.relapps.madview.main.GsAppVersion;
import net.relapps.madview.main.Updater;
import net.relapps.madview.main.Version;
import net.relapps.madview.md.MarkdownMgr;

/**
 *
 * @author RMT
 */
public class MainController { // implements EventHandler<Event> {

    public MainController() {
    }

    @FXML
    public void closeDocument() throws IOException {
        boolean close;
        if (isModified()) {
            close = StdDialogs.showConfirmation(vbox, "Quit",
                    "Document modified",
                    "Really close the document?");

        } else {
            close = true;
        }
        if (close) {
            closeEditor();
            _mdText = "";
            setMarkdown(_mdText);
            _file = null;
            btnOpenEditor.setDisable(false);
            btnCloseEditor.setDisable(true);
            setTitle();
        }
    }

    @FXML
    public void openAbout() {
        try {
            Dialog<String> dialog = new Dialog<>();
            DialogPane pane = dialog.getDialogPane();
            // DialogPane pane = new DialogPane();
            Loader<Parent> loader = new Loader<>("aboutdlg");
            Parent parent = loader.getNode();
            pane.setContent(parent);
            dialog.initOwner(StdDialogs.getWindow(vbox));
            dialog.showAndWait();
            dialog.close();
        } catch (IOException ex) {
            StdDialogs.showException(vbox, ex, "Error opening dialog.");
        }
    }

    @FXML
    public void openDocument() {
        if (mayChangeDocument()) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open document");
                FileChooser.ExtensionFilter ef
                        = FileType.MARKDOWN.getFilter();
                fileChooser.getExtensionFilters().add(ef);
                Window stage = StdDialogs.getWindow(vbox);
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    openFile(file);
                }
            } catch (IOException ex) {
                StdDialogs.showException(vbox, ex, "Error opening document.");
            }
        }
    }

    public void openDocument(File file) throws IOException {
        if (mayChangeDocument()) {
            openFile(file);
        }
    }

    @FXML
    public void save() throws IOException {
        if (_file == null) {
            saveAs();
        } else {
            saveMarkdown(_file.getAbsolutePath());
        }
    }

    @FXML
    public void saveAs() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save document");
        FileChooser.ExtensionFilter mdExt
                = FileType.MARKDOWN.getFilter();
        FileChooser.ExtensionFilter htmlExt
                = FileType.HTML.getFilter();
        fileChooser.getExtensionFilters().add(mdExt);
        fileChooser.getExtensionFilters().add(htmlExt);
        if (_file != null) {
            fileChooser.setInitialDirectory(_file.getParentFile());
            fileChooser.setInitialFileName(_file.getName());
        }
        Window stage = StdDialogs.getWindow(vbox);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            // Find file type and add extension if necessary.
            FileType fileType;
            String filePath = file.getAbsolutePath();
            if (FileType.HTML.isType(file)) {
                fileType = FileType.HTML;
            } else if (FileType.MARKDOWN.isType(file)) {
                fileType = FileType.MARKDOWN;
            } else {
                FileChooser.ExtensionFilter extFilter
                        = fileChooser.getSelectedExtensionFilter();
                List<String> ext = extFilter.getExtensions();
                if (FileType.MARKDOWN.isFilter(ext)) {
                    fileType = FileType.MARKDOWN;
                    filePath += fileType.getExtension();
                } else if (FileType.HTML.isFilter(ext)) {
                    fileType = FileType.HTML;
                    filePath += fileType.getExtension();
                } else {
                    return;
                }
            }
            // Save to file.
            if (fileType == FileType.HTML) {
                saveHTML(filePath);
            } else if (fileType == FileType.MARKDOWN) {
                saveMarkdown(filePath);
            }
        }
    }

    @FXML
    void closeEditor() {
        if (_textArea != null) {
            _updater.dispose();
            _updater = null;
            if (_textAreaController.isModified()) {
                _isModified = true;
            }
            setTitle();
            var split = vbox.getChildren().get(1);
            vbox.getChildren().remove(1);
            if (split instanceof SplitPane splitPane) {
                var item = splitPane.getItems().get(1);
                vbox.getChildren().add(1, item);
            }
            _textArea = null;
            _textAreaController = null;
            btnOpenEditor.setDisable(false);
            btnCloseEditor.setDisable(true);
        }
    }

    @FXML
    void initialize() {
        btnAbout.setText("About " + _version.getName() + "...");
        // Window window = vbox.getScene().getWindow();
        Platform.runLater(() -> {
            Scene scene = vbox.getScene();
            _userData = (SceneUserData) scene.getUserData();
            _stage = (Stage) scene.getWindow();
            setTitle();
            if (_userData.hasFile()) {
                try {
                    openFile(_userData.getFile());
                } catch (IOException ex) {
                    StdDialogs.showException(vbox, ex, "Cannot open file.");
                }
            }
            // Capture window close event.
            _stage.addEventFilter(EventType.ROOT, ev -> {
                if (ev.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
                    ev.consume();
                    quitApplication();
                }
            });
            btnOpenEditor.setDisable(false);
            btnCloseEditor.setDisable(true);
            htmlViewerController.setMainController(this);
            checkForNewVersion();
        });
    }

    @FXML
    void openEditor() {
        if (_textArea == null) {
            btnOpenEditor.setDisable(true);
            btnCloseEditor.setDisable(false);

            try {
                var item = vbox.getChildren().get(1);
                vbox.getChildren().remove(1);
                var loader = new Loader<VBox>("texteditor");
                var textArea = loader.getNode();
                _textArea = textArea;
                // Create split pane.
                var splitPane = new SplitPane(textArea, item);
                splitPane.setOrientation(Orientation.VERTICAL);
                VBox.setVgrow(splitPane, Priority.ALWAYS);
                // Add split pane to the vbox.
                vbox.getChildren().add(1, splitPane);
                if (loader.getController() instanceof TextEditorCntr editController) {
                    _textAreaController = editController;
                    _textAreaController.setText(_mdText);
                    _textAreaController.setUpdateListener(() -> {
                        _updater.update();
                        setTitle();
                    });
                }
                _updater = new Updater(() -> {
                    Platform.runLater(() -> {
                        _mdText = _textAreaController.getText();
                        updateHtml();
                    });
                }, 3000);
            } catch (IOException ex) {
                StdDialogs.showException(vbox, ex, "Error opening editor.");
            }
        }
    }

    @FXML
    void quitApplication() {
        boolean close;
        if (isModified()) {
            close = StdDialogs.showConfirmation(vbox, "Quit",
                    "Document modified",
                    "Really quit application?");

        } else {
            close = true;
        }
        if (close) {
            _stage.close();
        }
    }

    private void checkForNewVersion() {
        Timer timer = new Timer("Check for update", true);
        timer.schedule(new TimerTaskImpl(), 1_000);
    }

    private boolean isModified() {
        if (_textAreaController != null) {
            return _textAreaController.isModified();
        } else {
            return _isModified;
        }
    }

    private boolean mayChangeDocument() {
        boolean ret = true;
        if (isModified()) {
            ret = StdDialogs.showConfirmation(vbox,
                    "Document changed",
                    "Document changed",
                    "Do you really want to discard the changes?");
        }
        return ret;
    }

    private void openFile(File file) throws IOException {
        _mdText = TextFileReader.readFile(file);
        try {
            closeEditor();
            setMarkdown(_mdText);
            _isModified = false;
            _file = file;
            boolean canEdit = file.canWrite();
            btnOpenEditor.setDisable(!canEdit);
            btnCloseEditor.setDisable(!canEdit);
            setTitle();
        } catch (Exception ex) {
            StdDialogs.showException(vbox, ex, "Open document failed");
        }
    }

    private void saveHTML(String filePath) {
        try {
            String html = htmlViewerController.getHTML();
            TextFileWriter.writeFile(new File(filePath), html);
        } catch (IOException ex) {
            StdDialogs.showException(vbox, ex, "Error saving file.");
        }
    }

    private void saveMarkdown(String filePath) {
        try {
            TextFileWriter.writeFile(new File(filePath), _mdText);
            _isModified = false;
            _textAreaController.setModified(false);
            setTitle();
        } catch (IOException ex) {
            StdDialogs.showException(vbox, ex, "Error saving file.");
        }
    }

    private void setMarkdown(String md) {
        try {
            String html = MarkdownMgr.getInstance()
                    .markdownToHtmlDocument(md);
            htmlViewerController.setHTML(html);
        } catch (IOException ex) {
            StdDialogs.showException(vbox, ex, "Error");
        }
    }

    private void setTitle() {
        if (_stage != null) {
            String title;
            if (_file == null) {
                title = _version.getVersionString();
            } else {
                StringBuilder text = new StringBuilder();
                if (isModified()) {
                    text.append("* ");
                }
                text.append(_file.getAbsolutePath());
                title = text.toString();
            }
            _stage.setTitle(title);
        }
    }

    private void updateHtml() {
        setMarkdown(_mdText);
    }
    private File _file = null;
    private boolean _isModified = false;
    private String _mdText;
    private Stage _stage;
    private VBox _textArea = null;
    private TextEditorCntr _textAreaController;
    private Updater _updater;
    private SceneUserData _userData;
    private final Version _version = new Version();
    @FXML
    private MenuItem btnAbout;
    @FXML
    private MenuItem btnCloseEditor;
    @FXML
    private MenuItem btnOpenEditor;
    @FXML
    private HtmlViewerController htmlViewerController;
    @FXML
    private VBox vbox;

    private class TimerTaskImpl extends TimerTask {

        TimerTaskImpl() {
        }

        @Override
        public void run() {
            var appVers = GsAppVersion.isNewVersionAvailable();
            Platform.runLater(() -> {
                if (appVers != null) {

                    StringBuilder msg = new StringBuilder();
                    msg.append("Version ");
                    msg.append(appVers.getVersionString());
                    msg.append(" available for download at: ");
                    msg.append(appVers.getDownloadURL());
                    StdDialogs.showInfo(vbox, "New version available",
                            "New version of madview available", msg.
                                    toString());
                }
            });
        }
    }
}
