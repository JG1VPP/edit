/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.main;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import leaf.shell.ButtonProvider;
import leaf.shell.MenuItemProvider;


/**
 * アプリケーションの全てのコマンドの基底実装です。
 *
 * @author 無線部開発班
 * @since 2011年8月31日
 */
public abstract class Command extends leaf.shell.Command implements MenuItemProvider, ButtonProvider {

	private boolean isEnabled = true;
	private List<AbstractButton> buttons;

	/**
	 * このコマンドに対応するボタンを構築します。
	 *
	 * @param button ボタン
	 *
	 * @return ボタン
	 */
	@Override
	public JButton createButton(JButton button) {
		if (buttons == null) {
			buttons = new ArrayList<>();
		}
		buttons.add(button);
		button.setEnabled(isEnabled);
		button.addActionListener(Shell.getInstance());
		button.setActionCommand(getClass().getSimpleName());
		return button;
	}

	/**
	 * このコマンドに対応するメニューアイテムを構築します。
	 *
	 * @param item メニューアイテム
	 *
	 * @return メニューアイテム
	 */
	@Override
	public JMenuItem createMenuItem(JMenuItem item) {
		if (buttons == null) {
			buttons = new ArrayList<>();
		}
		buttons.add(item);
		item.setEnabled(isEnabled);
		item.addActionListener(Shell.getInstance());
		item.setActionCommand(getClass().getSimpleName());
		return item;
	}

	/**
	 * このコマンドが利用可能であるか返します。
	 *
	 * @return 利用可能な場合真 利用不可能な場合偽
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	/**
	 * コマンドの利用可能/不可能を切り替えます。
	 *
	 * @param enabled 利用可能な場合真 利用不可能な場合偽
	 */
	public void setEnabled(boolean enabled) {
		if (buttons != null) {
			for (var button : buttons) {
				button.setEnabled(enabled);
			}
		}
		this.isEnabled = enabled;
	}
}
