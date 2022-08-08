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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import net.relapps.madview.lib.FileUtils;
import net.relapps.madview.lib.TextFileReader;
import net.relapps.madview.main.Main;

/**
 * Resource and FXML GUI loader.
 *
 * @author RMT
 * @param <T>
 */
public class Loader<T extends Object> {

    public Loader(String fxml) throws IOException {
        String folder = "/net/relapps/madview/";
        URL url = Main.class.getResource(folder + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        _node = fxmlLoader.load();
        _controller = fxmlLoader.getController();
    }

    public static ImageView loadImage(String name) {
        String folder = "/net/relapps/madview/";
        URL url = Main.class.getResource(folder + name);
        ImageView image = new ImageView(url.toString());
        return image;
    }

    public static String loadText(String name) throws IOException {
        String folder = "/net/relapps/madview/";
        URL url = Main.class.getResource(folder + name);
        String ret;
        try ( var inp = url.openStream()) {
            ret = TextFileReader.read(inp);
        }
        return ret;
    }

    public static void setStyle(Node node, String name) throws IOException {
        String folder = "/net/relapps/madview/";
        URL url = Main.class.getResource(folder + name);
        URLConnection connection = url.openConnection();
        ByteArrayOutputStream result;
        // Read text from stream.
        try ( InputStream inputStream = connection.getInputStream()) {
            result = new ByteArrayOutputStream();
            FileUtils.copyStream(inputStream, result);
        }
        String style = result.toString("UTF-8");
        node.setStyle(style);
    }

    public Object getController() {
        return _controller;
    }

    public T getNode() {
        return _node;
    }

    private final Object _controller;
    private final T _node;
}
