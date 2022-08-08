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

/**
 *
 * @author RMT
 */
public class SceneUserData {
    public SceneUserData(String fileName) {
        _fileName = fileName;
    }

    public File getFile() {
        return new File(_fileName);
    }

    public boolean hasFile() {
        return _fileName != null && !_fileName.isBlank();
    }
    private final String _fileName;
}
