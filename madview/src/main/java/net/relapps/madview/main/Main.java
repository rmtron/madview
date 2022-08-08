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
package net.relapps.madview.main;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.relapps.fx.Loader;
import net.relapps.madview.cntrl.SceneUserData;

/**
 * JavaFX App
 */
public class Main extends Application {

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                _fileName = args[0];
            }
            launch();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Loader<Parent> loader = new Loader<>("main");
        scene = new Scene(loader.getNode(), 740, 880);
        SceneUserData userData = new SceneUserData(_fileName);
        scene.setUserData(userData);
        String icon = "/net/relapps/madview/madview.png";
        URL url = Main.class.getResource(icon);
        stage.getIcons().add(new Image(url.toString()));
        stage.setScene(scene);
        stage.show();
    }
    private static String _fileName = null;
    private static Scene scene;
}
