/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.security.SecureRandom;

import leaf.edit.cmd.EditorCommand;

/**
 * パスワードを生成して挿入するコマンドです。
 *
 * @author 無線部開発班
 * @since 2012/12/03
 */
public final class InsertPassword extends EditorCommand {
	@Override
	public void process(Object... args) {
		var random = new SecureRandom();
		var password = new char[20];
		for (var i = 0; i < password.length; i++) {
			switch (random.nextInt(7)) {
				case 0:
				case 1:
				case 2:
					password[i] = (char) ('a' + random.nextInt(26));
					break;
				case 3:
				case 4:
				case 5:
					password[i] = (char) ('A' + random.nextInt(26));
					break;
				case 6:
					password[i] = (char) ('0' + random.nextInt(10));
			}
		}
		getEditor().replaceSelection(new String(password));
	}

}
