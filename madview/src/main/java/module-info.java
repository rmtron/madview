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
module net.relapps.madview {
    requires jdk.xml.dom;
    requires java.desktop;
    requires java.base;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires flexmark.all;
    requires flexmark;
    requires flexmark.ext.abbreviation;
    requires flexmark.util;
    requires flexmark.util.data;
    requires flexmark.util.ast;
    requires flexmark.util.misc;
    requires flexmark.ext.admonition;
    requires flexmark.ext.anchorlink;
    requires flexmark.ext.aside;
    requires flexmark.ext.attributes;
    requires flexmark.ext.autolink;
    requires flexmark.ext.definition;
    requires flexmark.ext.emoji;
    requires flexmark.ext.enumerated.reference;
    requires flexmark.ext.escaped.character;
    requires flexmark.ext.footnotes;
    requires flexmark.ext.gfm.issues;
    requires flexmark.ext.gfm.strikethrough;
    requires flexmark.ext.gfm.tasklist;
    requires flexmark.ext.gfm.users;
    requires flexmark.ext.gitlab;
    requires flexmark.ext.jekyll.front.matter;
    requires flexmark.ext.jekyll.tag;
    requires flexmark.ext.media.tags;
    requires flexmark.ext.macros;
    requires flexmark.ext.ins;
    requires flexmark.ext.xwiki.macros;
    requires flexmark.ext.superscript;
    requires flexmark.ext.tables;
    requires flexmark.ext.toc;
    requires flexmark.ext.typographic;
    requires flexmark.ext.wikilink;
    requires flexmark.ext.yaml.front.matter;
    requires flexmark.ext.youtube.embedded;
    requires com.google.gson;

    opens net.relapps.madview to javafx.fxml;
    opens net.relapps.madview.cntrl to javafx.fxml;
    opens net.relapps.madview.main to com.google.gson;
    exports net.relapps.madview.main;
    exports net.relapps.madview.cntrl;
}
