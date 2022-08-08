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
package net.relapps.madview.lib;

/**
 * Progress listener interface.
 * @author RMT
 */
public interface IProgressListener {

    /**
     * Set the progress notifier in indeterminate or determinate mode.
     * @param flag True to switch to indeterminate mode.
     */
    public void setIndeterminante(boolean flag);

    /**
     * Enable/disable a cancel option.
     * @param flag True - cancel enabled, false - cancel disabled.
     */
    public void setEnabledCancel(boolean flag);

    /**
     * Check if cancel was requested.
     * @return True if cancel was requested.
     */
    public boolean isCanceled();

    /**
     * Assign the progress notifier minimum value.
     * @param value The minimum value.
     */
    public void setMinimum(int value);

    /**
     * Assign the progress notifier maximum value.
     * @param value The maximum value.
     */
    public void setMaximum(int value);

    /**
     * Assign value to the progress notifier.
     * @param value The new value.
     */
    public void setValue(int value);

    /**
     * Called to notify that the progress notifying has finished.
     */
    public void setFinished();

    /**
     * Assign a message text.
     * @param message The message to show.
     */
    public void setMessage(String message);
}