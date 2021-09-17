/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.cmd;

import java.util.ArrayList;
import java.util.List;

import leaf.edit.app.TsEditApp;
import leaf.edit.ui.TextFileChooser;
import leaf.swing.MainFrame;

/**
 * 現在表示されているエディタがテキストエディタであることを前提とします。
 *
 * @author 無線部開発班
 */
public abstract class Command extends leaf.main.Command {
	private static final List<Command> commandList;

	static {
		commandList = new ArrayList<>();
	}

	/**
	 * コマンドを構築します。
	 */
	public Command() {
		commandList.add(this);
	}

	/**
	 * エディタで用いられるファイル選択コンポーネントを返します。
	 *
	 * @return ファイル選択コンポーネント
	 */
	public static final TextFileChooser getFileChooser() {
		return TextFileChooser.getInstance();
	}

	/**
	 * このコマンドを処理する時のメインウィンドウを返します。
	 *
	 * @return メインウィンドウ
	 */
	public static final MainFrame getFrame() {
		return TsEditApp.getMainFrame();
	}

	/**
	 * このクラスを継承した全てのコマンドの有効・無効を切り替えます。
	 *
	 * @param enabled 有効にする場合true
	 */
	public static synchronized void setAllEnabled(Class<? extends Command> type, boolean enabled) {
		for (var cmd : commandList) {
			if (type.isAssignableFrom(cmd.getClass())) {
				cmd.setEnabled(enabled);
			}
		}
	}
}
