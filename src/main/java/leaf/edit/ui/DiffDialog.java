/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import leaf.swing.LeafDialog;
import leaf.util.Diff.EditList;

/**
 * テキストファイル間の差分抽出機能を提供するダイアログです。
 *
 * @author 無線部開発班
 * @since 2012/09/08
 */
public final class DiffDialog extends LeafDialog {
	private static final long serialVersionUID = 1L;
	private final DefaultComboBoxModel<String> model_old;
	private final DefaultComboBoxModel<String> model_new;
	private JButton button_diff, button_close;
	private JComboBox<String> combo_old;
	private JComboBox<String> combo_new;
	private JLabel label_old, label_new;
	private JCheckBox ch_case;
	private DiffWorker worker;
	private boolean isFinished = CANCEL_OPTION;
	private EditList result = new EditList();

	/**
	 * 親フレームを指定してモーダルダイアログを生成します。
	 *
	 * @param owner 親フレーム
	 */
	public DiffDialog(Frame owner) {
		super(owner, true);
		setContentSize(new Dimension(485, 85));
		setResizable(false);
		setLayout(null);
		model_old = new DefaultComboBoxModel<>();
		model_new = new DefaultComboBoxModel<>();
		initialize();
		addWindowListener(new DialogCloseListener());
		var tl = new TabListener();
		tl.stateChanged(null);
		TextEditorUtils.getTabbedPane().addChangeListener(tl);
	}

	/**
	 * 親ダイアログを指定してモーダルダイアログを生成します。
	 *
	 * @param owner 親ダイアログ
	 */
	public DiffDialog(Dialog owner) {
		super(owner, true);
		setContentSize(new Dimension(485, 85));
		setResizable(false);
		setLayout(null);
		model_old = new DefaultComboBoxModel<>();
		model_new = new DefaultComboBoxModel<>();
		initialize();
		addWindowListener(new DialogCloseListener());
		var tl = new TabListener();
		tl.stateChanged(null);
		TextEditorUtils.getTabbedPane().addChangeListener(tl);
	}

	/**
	 * 抽出した差分を表す編集リストを返します。
	 *
	 * @return 差分のリスト
	 */
	public EditList getResult() {
		return result;
	}

	/**
	 * ダイアログの表示と配置を初期化します。
	 */
	@Override
	public void initialize() {
		setTitle(translate("title"));
		getContentPane().removeAll();
		label_old = new JLabel(translate("label_old"));
		combo_old = new JComboBox<>(model_old);
		combo_old.setEditable(false);
		add(label_old);
		add(combo_old);
		label_new = new JLabel(translate("label_new"));
		combo_new = new JComboBox<>(model_new);
		combo_new.setEditable(false);
		add(label_new);
		add(combo_new);
		ch_case = new JCheckBox(translate("case_sensitive"), true);
		ch_case.setMnemonic(KeyEvent.VK_S);
		add(ch_case);
		button_diff = new JButton(translate("button_diff"));
		button_diff.setMnemonic(KeyEvent.VK_D);
		button_close = new JButton(translate("button_close"));
		button_close.setMnemonic(KeyEvent.VK_C);
		add(button_diff);
		add(button_close);
		layoutComponents();
		button_diff.addActionListener(e -> startDiff());
		button_close.addActionListener(e -> {
			if (worker != null) {
				worker.cancel(false);
				isFinished = CANCEL_OPTION;
			}
			dispose();
		});
	}

	private void layoutComponents() {
		var label_y = 10;
		var pref = button_diff.getPreferredSize().height;
		label_old.setBounds(5, label_y, 60, pref);
		combo_old.setBounds(65, label_y, 430, pref);
		label_y += pref + 5;
		label_new.setBounds(5, label_y, 60, pref);
		combo_new.setBounds(65, label_y, 430, pref);
		label_y += pref + 5;
		var ch_y = label_y + pref + 10;
		ch_case.setBounds(10, ch_y, 260, pref);
		ch_y += pref + 10;
		var button_y = label_y + pref + 10;
		button_diff.setBounds(275, button_y, 100, pref);
		button_close.setBounds(395, button_y, 100, pref);
		button_y += pref + 10;
		setContentSize(new Dimension(500, Math.max(button_y, ch_y)));
	}

	/**
	 * ダイアログを表示します。
	 *
	 * @return 抽出完了した場合true
	 */
	public boolean showDialog() {
		setVisible(true);
		return isFinished;
	}

	private void startDiff() {
		var tabpane = TextEditorUtils.getTabbedPane();
		var i_old = combo_old.getSelectedIndex();
		var i_new = combo_new.getSelectedIndex();
		var ed_old = (TextEditor) tabpane.getComponentAt(i_old);
		var ed_new = (TextEditor) tabpane.getComponentAt(i_new);
		worker = new DiffWorker(ed_old.getText(), ed_new.getText());
		worker.execute();
		button_diff.setEnabled(false);
	}

	private class DialogCloseListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			if (worker != null) worker.cancel(false);
			isFinished = CANCEL_OPTION;
		}
	}

	private class TabListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			var tabpane = TextEditorUtils.getTabbedPane();
			model_old.removeAllElements();
			model_new.removeAllElements();
			final var tc = tabpane.getTabCount();
			var temp = "%" + (tc / 10 + 1) + "d - %s";
			for (var i = 0; i < tc; i++) {
				var title = tabpane.getTitleAt(i);
				var item = String.format(temp, i + 1, title);
				model_old.addElement(item);
				model_new.addElement(item);
			}
			var selected = tabpane.getSelectedIndex();
			combo_old.setSelectedIndex(selected);
			combo_new.setSelectedIndex(selected);
		}
	}

	private class DiffWorker extends SwingWorker<String, String> {
		private final boolean isCaseSensitive;
		private final String oldtext, newtext;

		public DiffWorker(String oldtext, String newtext) {
			this.oldtext = oldtext;
			this.newtext = newtext;
			isCaseSensitive = ch_case.isSelected();
		}

		@Override
		protected String doInBackground() {
			result = new Diff(isCaseSensitive).compare(oldtext, newtext);
			return "Done";
		}

		@Override
		public void done() {
			button_diff.setEnabled(true);
			isFinished = OK_OPTION;
			dispose();
		}
	}

	private static class Diff extends leaf.util.Diff {
		private final boolean isCaseSensitive;

		public Diff(boolean isCaseSensitive) {
			this.isCaseSensitive = isCaseSensitive;
		}

		@Override
		protected boolean equals(Object oobj, Object nobj) {
			if (isCaseSensitive) return oobj.equals(nobj);
			return ((String) oobj).equalsIgnoreCase((String) nobj);
		}
	}
}
