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

import java.util.Timer;
import java.util.TimerTask;
import net.relapps.madview.lib.IListener;

/**
 * Handle updates in a specified interval.
 *
 * @author RMT
 */
public class Updater {

    /**
     * Create a new instance of Updater.
     *
     * @param actionListener The action listener.
     * @param interval The interval in milliseconds.
     */
    public Updater(IListener actionListener, int interval) {
        _timer = new Timer("Updater", true);
        _listener = actionListener;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (_updated) {
                    _listener.notifyPerformed();
                    _updated = false;
                }
            }
        };
        _timer.scheduleAtFixedRate(task, interval, interval);
    }

    /**
     * Dispose the object.
     */
    public void dispose() {
        _timer.cancel();
        _timer = null;
        _listener = null;
    }

    /**
     * Call to launch an update, the update is not executed immediately.
     */
    public void update() {
        if (_timer == null) {
            throw new RuntimeException("Updated disposed.");
        }
        _updated = true;
    }

    private IListener _listener;
    private Timer _timer;
    private boolean _updated;
}
