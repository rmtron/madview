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
import java.util.List;
import javafx.stage.FileChooser;

/**
 * Represents a file type.
 *
 * @author RMT
 */
public enum FileType {
    MARKDOWN("Markdown document", "*.md", ".md"),
    HTML("HTML document", "*.html", ".html");

    private FileType(String description, String filter, String ext) {
        _filter = filter;
        _description = description;
        _ext = ext;
    }

    public String getDescription() {
        return _description;
    }

    public FileChooser.ExtensionFilter getFilter() {
        return new FileChooser.ExtensionFilter(_description,
                _filter);
    }

    public boolean isType(File file) {
        String absPath = file.getAbsolutePath();
        return absPath.endsWith(_ext);
    }

    public String getExtension() {
        return _ext;
    }

    public boolean isFilter(List<String> extList) {
        String filter = extList.get(0);
        return filter.equals(_filter);
    }
    private final String _description;
    private final String _ext;
    private final String _filter;
}
