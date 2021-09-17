/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import javax.swing.*;

import leaf.edit.cmd.Command;
import leaf.edit.ui.BasicTextEditor;
import leaf.edit.ui.EditorListTask;
import leaf.util.Properties;
import leaf.main.Shell;

/**
 * タブを展開するサイズを設定するコマンドの共通の実装です。
 *
 * @author 無線部開発班
 */
public abstract class SetTabSize extends Command {
	private static final Properties properties;

	static {
		properties = Properties.getInstance(SetTabSize.class);
	}

	private final int size;

	SetTabSize(int size) {
		this.size = size;
	}

	@Override
	public JMenuItem createMenuItem(JMenuItem item) {
		item = new JRadioButtonMenuItem();
		item.addActionListener(Shell.getInstance());
		item.setActionCommand(getClass().getSimpleName());
		item.setSelected(size == SetTabSize.getTabSize());
		return item;
	}

	/**
	 * エディタのタブ展開サイズを返します。
	 *
	 * @return 適用されるタブ展開サイズ
	 */
	public static int getTabSize() {
		var num = properties.get("size", String.class, "4");
		try {
			return Integer.parseInt(num);
		} catch (NumberFormatException ex) {
			return 4;
		}
	}

	/**
	 * タブ展開サイズを変更し、エディタ全体に適用します。
	 *
	 * @param size 適用するタブ展開サイズ
	 */
	public static void setTabSize(final int size) {
		new EditorListTask<>(BasicTextEditor.class) {
			@Override
			public boolean process(BasicTextEditor editor) {
				editor.setTabSize(size);
				return false;
			}
		}.start();
		properties.put("size", Integer.toString(size));
	}
}
