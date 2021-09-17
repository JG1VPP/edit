/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.awt.*;
import javax.swing.*;

import leaf.edit.app.TsEditApp;
import leaf.edit.cmd.Command;

/**
 * システムのルックアンドフィールを変更します。
 *
 * @author 無線部開発班
 * @since 2012/05/26
 */
public final class SetLnF extends Command {

	@Override
	public JMenuItem createMenuItem(JMenuItem item) {
		var menu = new JMenu();
		final var group = new ButtonGroup();
		var lnfName = UIManager.getLookAndFeel().getClass().getName();
		for (var info : UIManager.getInstalledLookAndFeels()) {
			var lnfItem = new JRadioButtonMenuItem();
			lnfItem.setSelected(info.getClassName().equals(lnfName));
			lnfItem.setHideActionText(true);
			lnfItem.setText(info.getName());
			final var lnfClassName = info.getClassName();
			lnfItem.addActionListener(e -> setLookAndFeel(lnfClassName));
			group.add(lnfItem);
			menu.add(lnfItem);
		}
		return menu;
	}

	@Override
	public void process(Object... args) {
		setLookAndFeel(UIManager.getLookAndFeel().getClass().getName());
	}

	private void setLookAndFeel(String lnfClassName) {
		try {
			UIManager.setLookAndFeel(lnfClassName);
		} catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException ex) {
		}
		for (var window : Frame.getWindows()) {
			SwingUtilities.updateComponentTreeUI(window);
		}
		TsEditApp.getMainFrame().initialize();
	}

}
