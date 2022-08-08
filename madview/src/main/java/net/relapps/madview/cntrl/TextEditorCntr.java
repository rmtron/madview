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

import java.util.ArrayList;
import java.util.List;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import net.relapps.fx.Loader;
import relapps.exif.misc.IListener;

/**
 * Text editor controller.
 *
 * @author RMT
 */
public class TextEditorCntr {

    public String getText() {
        return textArea.getText();
    }

    public boolean isModified() {
        return _changed;
    }

    public void setModified(boolean flag) {
        _changed = flag;
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public void setUpdateListener(IListener listener) {
        _updateListener = listener;
    }

    @FXML
    void btnBoldPressed() {
        editSelection("**", "**");
    }

    @FXML
    void btnBulletsPressed() {
        editSelectionMLAdd("* ");
    }

    @FXML
    void btnCodeBlockPressed() {
        editSelection("```\n", "```");
    }

    @FXML
    void btnHeader1Pressed() {
        editSelectionAdd("# ");
    }

    @FXML
    void btnHeader2Pressed() {
        editSelectionAdd("## ");
    }

    @FXML
    void btnHeader3Pressed() {
        editSelectionAdd("### ");
    }

    @FXML
    void btnItalicPressed() {
        editSelection("*", "*");
    }

    @FXML
    void initialize() {
        styleToolButton(btnBold, "bold.png", "Bold");
        styleToolButton(btnItalic, "italic.png", "Italic");
        styleToolButton(btnBullets, "bullets.png", "Bullets");
        styleToolButton(btnHeader1, "header-1.png", "Header 1");
        styleToolButton(btnHeader2, "header-2.png", "Header 2");
        styleToolButton(btnHeader3, "header-3.png", "Header 3");
        styleToolButton(btnCodeBlock, "codeblock.png",
                "Preformatted clode block");
        textArea.setFont(Font.font("monospace"));
    }

    @FXML
    void keyPressed(KeyEvent ev) {
    }

    @FXML
    void keyReleased(KeyEvent ev) {
    }

    @FXML
    void textChanged(Event ev) {
        if (_initialized) {
            _changed = true;
            if (_updateListener != null) {
                _updateListener.notifyPerformed();
            }
        }
        _initialized = true;
    }

    private void editSelection(String pre, String post) {
        String selected = textArea.getSelectedText();
        textArea.replaceSelection(pre + selected + post);
    }

    private void editSelectionAdd(String pre) {
        int end = getSelectionEnd();
        String text = textArea.getText();
        int pos = 0;
        for (int n = end; n > 0; --n) {
            char c = text.charAt(n - 1);
            if (c == '\n') {
                pos = n;
                break;
            }
        }
        int len = text.length();
        int prelen = pre.length();
        boolean insert = pos + prelen > len;
        if (!insert) {
            insert = !text.substring(pos, pos + prelen).equals(pre);
        }
        if (insert) {
            textArea.insertText(pos, pre);
        }
    }

    private void editSelectionMLAdd(String pre) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        if (end == start) {
            editSelectionAdd(pre);
            return;
        }
        String text = textArea.getText();
        List<Integer> pos = new ArrayList<>();
        for (int n = end - 1; n >= start; --n) {
            if (n > 0) {
                char c = text.charAt(n - 1);
                if (c == '\n') {
                    pos.add(n);
                }
            } else {
                pos.add(n);
            }
        }
        if (pos.isEmpty()) {
            pos.add(0);
        }
        int len = text.length();
        int prelen = pre.length();
        for (int p : pos) {
            boolean insert = p + prelen > len;
            if (!insert) {
                insert = !text.substring(p, p + prelen).equals(pre);
            }
            if (insert) {
                textArea.insertText(p, pre);
            }
        }
    }

    private int getSelectionEnd() {
        int anchor = textArea.getAnchor();
        int caret = textArea.getCaretPosition();
        if (anchor > caret) {
            return anchor;
        } else {
            return caret;
        }
    }

    private int getSelectionStart() {
        int anchor = textArea.getAnchor();
        int caret = textArea.getCaretPosition();
        if (anchor > caret) {
            return caret;
        } else {
            return anchor;
        }
    }

    private void styleToolButton(Button btn, String icon, String toolTip) {
        final String STANDARD_BUTTON_STYLE
                = "-fx-background-color: transparent;";
        final String HOVERED_BUTTON_STYLE = "-fx-background-color: #ddd;";
        double size = 16.0;

        var img = Loader.loadImage(icon);
        img.setPreserveRatio(true);
        img.setFitWidth(size);
        img.setSmooth(true);
        btn.setGraphic(img);
        btn.setPrefSize(size, size);
        btn.setTooltip(new Tooltip(toolTip));
        btn.setStyle(STANDARD_BUTTON_STYLE);
        btn.setOnMouseEntered((MouseEvent mouseEvent) -> {
            btn.setStyle(HOVERED_BUTTON_STYLE);
        });
        btn.setOnMouseExited((MouseEvent mouseEvent) -> {
            btn.setStyle(STANDARD_BUTTON_STYLE);
        });
    }
    private boolean _changed = false;
    private boolean _initialized = false;
    private IListener _updateListener = null;
    @FXML
    private Button btnBold;
    @FXML
    private Button btnBullets;
    @FXML
    private Button btnCodeBlock;
    @FXML
    private Button btnHeader1;
    @FXML
    private Button btnHeader2;
    @FXML
    private Button btnHeader3;
    @FXML
    private Button btnItalic;
    @FXML
    private TextArea textArea;
}
