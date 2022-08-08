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
package net.relapps.madview.md;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.superscript.SuperscriptExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.KeepType;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import net.relapps.fx.Loader;
import net.relapps.madview.main.Main;
import net.relapps.madview.main.Version;
import relapps.lib.fileutils.FileUtils;
import relapps.lib.misc.ISO8601;

/**
 * Handles markdown.
 *
 * @author RMT
 */
public class MarkdownMgr {

    private MarkdownMgr() {
    }

    public static MarkdownMgr getInstance() {
        if (_instance == null) {
            _instance = new MarkdownMgr();
        }
        return _instance;
    }

    public String markdownToHtml(String markdown) {
        return gitHub(markdown);
    }

    public String markdownToHtmlDocument(String markdown) throws IOException {
        Version vers = new Version();
        String app = vers.getVersionString();
        String ts = ISO8601.now();
        StringBuilder ret = new StringBuilder();
        String mdHtml = gitHub(markdown);
        String prismjs = Loader.loadText("prism.js");
        String prismcss = Loader.loadText("prism.css");
        
        ret.append("<!DOCTYPE html>\n");
        ret.append("<html>\n");
        ret.append("<head>\n");
        ret.append("<meta http-equiv=\"content-type\" "
                + "content=\"text/html; charset=utf-8\"/>\n");
        ret.append("<title></title>\n");
        ret.append("<meta name=\"generator\" content=\"");
        ret.append(app);
        ret.append("\"/>\n");
        ret.append("<meta name=\"created\" content=\"");
        ret.append(ts);
        ret.append("\"/>\n");
        ret.append("<style>\n");
        ret.append(prismcss);
        ret.append(getCSS());
        ret.append("</style>\n");
        ret.append("<script>\n");
        ret.append(prismjs);
        ret.append("</script>\n");
        ret.append("</head>\n");
        ret.append("<body>\n");
        ret.append(mdHtml);
        ret.append("</body>\n");
        ret.append("</html>\n");
        return ret.toString();
    }

    private String getCSS() throws IOException {
        String folder = "/net/relapps/madview/md/";
        URL url = Main.class.getResource(folder + "styles.css");
        URLConnection connection = url.openConnection();
        ByteArrayOutputStream result;
        // Read text from stream.
        try ( InputStream inputStream = connection.getInputStream()) {
            result = new ByteArrayOutputStream();
            FileUtils.copyStream(inputStream, result);
        }
        return result.toString("UTF-8");
    }

    private String gitHub(String md) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        options.set(Parser.EXTENSIONS, Arrays.asList(
                // SubscriptExtension.create(),
                SuperscriptExtension.create(),
                AutolinkExtension.create(),
                // AnchorLinkExtension.create(),
                EmojiExtension.create(),
                StrikethroughExtension.create(),
                TablesExtension.create(),
                TaskListExtension.create(),
                FootnoteExtension.create()
        ));
        // uncomment and define location of emoji images from https://github.com/arvida/emoji-cheat-sheet.com
        // options.set(EmojiExtension.ROOT_IMAGE_PATH, "");
        // Uncomment if GFM anchor links are desired in headings
        // options.set(AnchorLinkExtension.ANCHORLINKS_SET_ID, false);
        // options.set(AnchorLinkExtension.ANCHORLINKS_ANCHOR_CLASS, "anchor");
        // options.set(AnchorLinkExtension.ANCHORLINKS_SET_NAME, true);
        // options.set(AnchorLinkExtension.ANCHORLINKS_TEXT_PREFIX, "<span class=\"octicon octicon-link\"></span>");
        // References compatibility
        options.set(Parser.REFERENCES_KEEP, KeepType.LAST);

        // Set GFM table parsing options
        options.set(TablesExtension.COLUMN_SPANS, false)
                .set(TablesExtension.MIN_HEADER_ROWS, 1)
                .set(TablesExtension.MAX_HEADER_ROWS, 1)
                .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
                .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
                .set(TablesExtension.WITH_CAPTION, false)
                .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true);

        // Setup List Options for GitHub profile which is kramdown for documents
        options.setFrom(ParserEmulationProfile.GITHUB_DOC);

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(md);
        return renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
    }
    private static MarkdownMgr _instance;
}
