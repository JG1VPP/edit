/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import leaf.edit.cmd.Command;
import leaf.edit.ui.TextEditorUtils;
import leaf.util.NewsReader;

/**
 * ニュースフィードを取得して一覧を出力するコマンドです。
 *
 * @author 無線部開発班
 */
public final class NewsFeed extends Command {
	@Override
	public void process(Object... args) throws IOException {
		var url = getFrame().getStatusBar().getNewsBarURL();
		if (url != null) {
			var feed = new NewsReader(url).read();
			if (feed.getCategories().length == 0) {
				var sw = new StringWriter();
				var pw = new PrintWriter(sw);
				pw.println(feed.getTitle());
				pw.println();
				for (var item : feed.getItems()) {
					pw.println(item.getTitle());
				}
				var editor = TextEditorUtils.addTab(feed.getTitle());
				editor.read(new StringReader(sw.toString()));
			} else for (var category : feed.getCategories()) {
				var sw = new StringWriter();
				var pw = new PrintWriter(sw);
				pw.print(feed.getTitle());
				pw.print("  [");
				pw.print(category);
				pw.println("]");
				pw.println();
				for (var item : feed.getItems(category)) {
					pw.println(item.getTitle());
				}
				var editor = TextEditorUtils.addTab(category);
				editor.read(new StringReader(sw.toString()));
			}
		}
	}
}
