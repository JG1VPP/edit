/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.text.DateFormat;
import java.util.Calendar;

import leaf.edit.cmd.EditorCommand;
import leaf.util.LocalizeManager;

/**
 * エディタに現在時刻を挿入するコマンドです。
 *
 * @author 無線部開発班
 */
public final class InsertTime extends EditorCommand {
	@Override
	public void process(Object... args) {
		var locale = LocalizeManager.getLocale();
		var format = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
		getEditor().replaceSelection(format.format(Calendar.getInstance().getTime()));
	}
}
