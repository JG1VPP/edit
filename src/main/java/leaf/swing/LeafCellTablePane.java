/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import leaf.util.LocalizeManager;

/**
 * {@link LeafCellTable}によるシミュレーションを容易にするパネルです。
 *
 * @author 無線部開発班
 * @since 2012年3月17日
 */
public class LeafCellTablePane extends JComponent {
	private static final long serialVersionUID = 1L;
	private final JButton bstart;
	private final LeafCellTable table;

	private final JFileChooser chooser;
	private final LocalizeManager localize;

	/**
	 * パネルを構築します。
	 */
	public LeafCellTablePane() {
		setLayout(new BorderLayout(5, 5));
		localize = LocalizeManager.get(LeafCellTablePane.class);
		var filterName = localize.translate("filechooser_filter_name");
		FileFilter filter = new FileNameExtensionFilter(filterName, "bin");
		chooser = new JFileChooser();
		chooser.addChoosableFileFilter(filter);
		table = new LeafCellTable();
		add(table, BorderLayout.CENTER);
		var bopen = new JButton(localize.translate("button_open"));
		var bsave = new JButton(localize.translate("button_save"));
		bstart = new JButton(localize.translate("button_start"));
		var breset = new JButton(localize.translate("button_reset"));
		var bsize = bopen.getPreferredSize();
		bopen.setPreferredSize(new Dimension(100, bsize.height));
		bsave.setPreferredSize(new Dimension(100, bsize.height));
		bstart.setPreferredSize(new Dimension(100, bsize.height));
		breset.setPreferredSize(new Dimension(100, bsize.height));
		var console = Box.createHorizontalBox();
		console.add(Box.createHorizontalStrut(5));
		console.add(bopen);
		console.add(Box.createHorizontalStrut(5));
		console.add(bsave);
		console.add(Box.createHorizontalGlue());
		console.add(bstart);
		console.add(Box.createHorizontalStrut(5));
		console.add(breset);
		console.add(Box.createHorizontalStrut(5));
		console.add(Box.createRigidArea(new Dimension(0, bsize.height + 10)));
		add(console, BorderLayout.SOUTH);
		bopen.addActionListener(e -> {
			if (getCellAutomata() != null) openPattern();
		});
		bsave.addActionListener(e -> {
			if (getCellAutomata() != null) savePattern();
		});
		bstart.addActionListener(e -> setAutoUpdateEnabled(!isAutoUpdateEnabled()));
		breset.addActionListener(e -> {
			setAutoUpdateEnabled(false);
			table.init();
		});
	}

	/**
	 * セルテーブルの世代更新周期の設定値をミリ秒単位で返します。
	 *
	 * @return 世代更新周期
	 */
	public int getAutoUpdateInterval() {
		return table.getAutoUpdateInterval();
	}

	/**
	 * セルテーブルの世代更新周期をミリ秒単位で設定します。
	 *
	 * @param ms 世代更新周期
	 *
	 * @throws IllegalArgumentException 正数でない周期を指定した場合
	 */
	public void setAutoUpdateInterval(int ms) throws IllegalArgumentException {
		table.setAutoUpdateInterval(ms);
	}

	/**
	 * このパネルが内蔵するセルテーブルのセルオートマータを返します。
	 *
	 * @return セルオートマータ
	 */
	public Automata getCellAutomata() {
		return table.getCellAutomata();
	}

	/**
	 * このパネルが内蔵するセルテーブルにセルオートマータを設定します。
	 *
	 * @param automata オートマータ
	 */
	public void setCellAutomata(Automata automata) {
		table.setCellAutomata(automata);
	}

	/**
	 * このパネルが内蔵するセルテーブルを返します。
	 *
	 * @return セルテーブル
	 */
	public LeafCellTable getInternalCellTable() {
		return table;
	}

	/**
	 * セルテーブルの自動的な世代更新動作が稼働中であるか返します。
	 *
	 * @return 稼働中である場合は真 停止中である場合は偽
	 */
	public boolean isAutoUpdateEnabled() {
		return table.isAutoUpdateEnabled();
	}

	/**
	 * セルテーブルの自動的な世代更新を開始または停止します。
	 *
	 * @param b 開始する場合は真 停止する場合は偽
	 */
	public synchronized void setAutoUpdateEnabled(boolean b) {
		table.setAutoUpdateEnabled(b);
		if (b) {
			bstart.setText(localize.translate("button_pause"));
		} else {
			bstart.setText(localize.translate("button_start"));
		}
	}

	/**
	 * ファイル選択画面を表示して、パターンをファイルから読み込みます。
	 */
	private void openPattern() {
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			var file = chooser.getSelectedFile();
			try {
				var pattern = Pattern.read(new FileInputStream(file));
				pattern.setPattern(getCellAutomata());
			} catch (IOException ex) {
				var msg = localize.translate("failed_read_pattern");
				JOptionPane.showMessageDialog(this, msg);
			} catch (IllegalArgumentException ex) {
				var msg = localize.translate("illegal_size_pattern");
				JOptionPane.showMessageDialog(this, msg);
			}
			table.repaint();
		}
	}

	/**
	 * ファイル選択画面を表示して、パターンをファイルに保存します。
	 */
	private void savePattern() {
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			var file = chooser.getSelectedFile();
			try {
				var pattern = Pattern.getPattern(getCellAutomata());
				pattern.write(new FileOutputStream(file));
			} catch (IOException ex) {
				var msg = localize.translate("failed_save_pattern");
				JOptionPane.showMessageDialog(this, msg);
			}
		}
	}

}
