/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.charset.Charset;
import javax.swing.*;

import leaf.swing.LeafDialog;

/**
 * ストリーム入出力で用いられる文字セットを選択するダイアログです。
 *
 * @author 無線部開発班
 * @since 2012/03/31
 */
public final class CharsetDialog extends LeafDialog {
	private static final long serialVersionUID = 1L;
	private DefaultListModel<Charset> model_selected;
	private DefaultListModel<Charset> model_available;

	private JList<Charset> list_selected, list_available;
	private JButton button_add;
	private JButton button_delete;

	private boolean isApproved = CANCEL_OPTION;

	/**
	 * 所有者となる{@link JFrame}を指定してダイアログを構築します。
	 *
	 * @param owner 所有するJFrame
	 */
	public CharsetDialog(Frame owner) {
		super(owner, true);
		setContentSize(new Dimension(420, 270));
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isApproved = CANCEL_OPTION;
			}
		});
		setLayout(null);
		initialize();
	}

	/**
	 * 所有者となる{@link JDialog}を指定してダイアログを構築します。
	 *
	 * @param owner 所有するJDialog
	 */
	public CharsetDialog(Dialog owner) {
		super(owner, true);
		setContentSize(new Dimension(420, 270));
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isApproved = CANCEL_OPTION;
			}
		});
		setLayout(null);
		initialize();
	}

	private void addSelectedCharset() {
		var index = list_available.getSelectedIndex();
		model_selected.add(model_selected.getSize(), model_available.get(index));
		list_selected.setSelectedIndex(model_selected.getSize() - 1);
		if (index < model_available.getSize() - 1) {
			list_available.setSelectedIndex(index + 1);
		} else {
			list_available.clearSelection();
			button_add.setEnabled(false);
		}
	}

	private DefaultListModel<Charset> createModelOfAvailableCharset() {
		var model = new DefaultListModel<Charset>();
		var sets = TextEditorUtils.getAvailableCharsets();
		for (var i = 0; i < sets.length; i++) model.add(i, sets[i]);
		return model;
	}

	/**
	 * 選択されている文字セットの一覧を返します。
	 *
	 * @return 文字セットの配列
	 */
	public Charset[] getSelectedCharsets() {
		var length = model_selected.getSize();
		var chsets = new Charset[length];
		for (var i = 0; i < length; i++) {
			chsets[i] = model_selected.get(i);
		}
		return chsets;
	}

	/**
	 * 選択されている文字セットの一覧を設定します。
	 *
	 * @param chsets 文字セットの配列
	 */
	public void setSelectedCharsets(Charset[] chsets) {
		model_selected.clear();
		for (var chset : chsets) {
			model_selected.addElement(chset);
		}
	}

	@Override
	public void initialize() {
		setTitle(translate("title"));
		getContentPane().removeAll();
		var label_selected = new JLabel(translate("label_selected_codes"));
		label_selected.setBounds(5, 10, 160, 20);
		add(label_selected);
		model_selected = new DefaultListModel<>();
		list_selected = new JList<>(model_selected);
		list_selected.addListSelectionListener(e -> button_delete.setEnabled(model_selected.getSize() > 0));
		var scroll = new JScrollPane(list_selected);
		scroll.setBounds(5, 30, 160, 200);
		add(scroll);
		var label_available = new JLabel(translate("label_available_codes"));
		label_available.setBounds(255, 10, 160, 20);
		add(label_available);
		model_available = createModelOfAvailableCharset();
		list_available = new JList<>(model_available);
		list_available.addListSelectionListener(e -> {
			if (!model_available.isEmpty()) {
				var selected = list_available.getSelectedValue();
				button_add.setEnabled(!model_selected.contains(selected));
			} else button_add.setEnabled(false);
		});
		scroll = new JScrollPane(list_available);
		scroll.setBounds(255, 30, 160, 200);
		add(scroll);
		button_add = new JButton(translate("button_add"));
		button_delete = new JButton(translate("button_delete"));
		final var bheight = button_add.getPreferredSize().height;
		button_add.setBounds(170, 120, 80, bheight);
		button_add.setEnabled(false);
		add(button_add);
		button_add.addActionListener(e -> addSelectedCharset());
		button_delete.setBounds(170, 160, 80, bheight);
		button_delete.setEnabled(false);
		add(button_delete);
		button_delete.addActionListener(e -> removeCharset());
		var button_ok = new JButton(translate("button_ok"));
		button_ok.setBounds(180, 240, 100, bheight);
		add(button_ok);
		button_ok.addActionListener(e -> {
			if (model_selected.getSize() > 0) {
				isApproved = OK_OPTION;
				dispose();
			} else {
				showMessage(translate("button_ok_action_error"));
			}
		});
		var button_cancel = new JButton(translate("button_cancel"));
		button_cancel.setBounds(300, 240, 100, bheight);
		add(button_cancel);
		button_cancel.addActionListener(e -> {
			isApproved = CANCEL_OPTION;
			dispose();
		});
	}

	private void removeCharset() {
		var index = list_selected.getSelectedIndex();
		model_selected.remove(index);
		if (index >= model_selected.getSize()) {
			list_selected.setSelectedIndex(index - 1);
		} else {
			list_selected.setSelectedIndex(index);
		}
		var selected = list_available.getSelectedValue();
		if (!model_selected.contains(selected)) {
			button_add.setEnabled(selected != null);
		} else button_add.setEnabled(false);
	}

	/**
	 * モーダルダイアログを表示します。
	 *
	 * @return 内容が変更された場合true
	 */
	public boolean showDialog() {
		setVisible(true);
		return isApproved;
	}
}
