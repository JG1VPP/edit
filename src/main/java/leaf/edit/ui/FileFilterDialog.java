/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;

import leaf.swing.LeafDialog;
import leaf.util.LocalizeManager;

/**
 * ユーザー登録拡張子を設定するダイアログです。
 *
 * @author 無線部開発班
 * @since 2012/04/25
 */
public final class FileFilterDialog extends LeafDialog {
	private static final long serialVersionUID = 1L;
	private final List<ListFileFilter> filterList;
	private final LocalizeManager localize;
	private JLabel label_filter, label_filetype;
	private JList<String> list_ext;
	private JComboBox<ListFileFilter> combo_filter;
	private JComboBox<FileType> combo_filetype;
	private JButton btn_filter_add;
	private JButton btn_filter_del;
	private JButton btn_ext_add;
	private JButton btn_ext_del;
	private JButton btn_ok, btn_cancel;
	private JScrollPane scroll_ext;
	private DefaultListModel<String> model_ext;
	private DefaultComboBoxModel<ListFileFilter> model_filter;
	private DefaultComboBoxModel<FileType> model_filetype;
	private ListFileFilter filter;
	private boolean isApproved = CANCEL_OPTION;

	/**
	 * 所有者となる{@link JFrame}と、フィルタのリストを指定してダイアログを構築します。
	 *
	 * @param owner      所有者
	 * @param filterList フィルタのリスト
	 */
	public FileFilterDialog(JFrame owner, List<ListFileFilter> filterList) {
		super(owner, true);
		localize = LocalizeManager.get(getClass());
		this.filterList = filterList;
		setTitle(localize.translate("title"));
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isApproved = CANCEL_OPTION;
			}
		});
		getContentPane().setLayout(null);
		initialize();
		if (!filterList.isEmpty()) filter = filterList.get(0);
		for (var filter : filterList) {
			model_filter.addElement(filter);
		}
		updateFileFilter();
	}

	private void addFileFilter() {
		var item = JOptionPane.showInputDialog(FileFilterDialog.this, localize.translate("add_filter_msg"), getTitle(), JOptionPane.PLAIN_MESSAGE);
		if (item == null) return;
		for (var filter : filterList) {
			if (filter.getDescription().equals(item)) {
				model_filter.setSelectedItem(filter);
				JOptionPane.showMessageDialog(this, translate("duplicate_filter_msg"), getTitle(), JOptionPane.PLAIN_MESSAGE);
				return;
			}
		}
		filter = new ListFileFilter(item);
		filterList.add(filter);
		model_filter.addElement(filter);
		model_filter.setSelectedItem(filter);
		updateFileFilter();
	}

	private void addFileNameExtension() {
		var item = JOptionPane.showInputDialog(FileFilterDialog.this, localize.translate("add_extension_msg"), getTitle(), JOptionPane.PLAIN_MESSAGE);
		if (item == null) return;
		if (!model_ext.contains(item)) {
			model_ext.addElement(item.toString());
			filter.addFileNameExtension(item);
		}
		list_ext.setSelectedValue(item, true);
	}

	@Override
	public void initialize() {
		getContentPane().removeAll();
		label_filter = new JLabel(localize.translate("label_filter"));
		model_filter = new DefaultComboBoxModel<>();
		combo_filter = new JComboBox<>(model_filter);
		add(label_filter);
		add(combo_filter);
		btn_filter_add = new JButton(translate("button_filter_add"));
		btn_filter_del = new JButton(translate("button_filter_delete"));
		add(btn_filter_add);
		add(btn_filter_del);
		model_ext = new DefaultListModel<>();
		list_ext = new JList<>(model_ext);
		list_ext.setLayoutOrientation(JList.VERTICAL_WRAP);
		list_ext.setFixedCellWidth(100);
		list_ext.setVisibleRowCount(0);
		scroll_ext = new JScrollPane(list_ext);
		add(scroll_ext);
		btn_ext_add = new JButton(localize.translate("button_ext_add"));
		btn_ext_del = new JButton(localize.translate("button_ext_delete"));
		add(btn_ext_add);
		add(btn_ext_del);
		label_filetype = new JLabel(localize.translate("label_filetype"));
		model_filetype = new DefaultComboBoxModel<>();
		combo_filetype = new JComboBox<>(model_filetype);
		add(label_filetype);
		add(combo_filetype);
		btn_ok = new JButton(localize.translate("button_ok"));
		btn_cancel = new JButton(localize.translate("button_cancel"));
		add(btn_ok);
		add(btn_cancel);
		layoutComponents();
		setListeners();
	}

	private void layoutComponents() {
		var h_button = btn_ok.getPreferredSize().height;
		var y_comp = 10;
		label_filter.setBounds(5, y_comp, 80, h_button);
		combo_filter.setBounds(90, y_comp, 140, h_button);
		btn_filter_add.setBounds(240, y_comp, 100, h_button);
		btn_filter_del.setBounds(350, y_comp, 100, h_button);
		y_comp += h_button + 10;
		scroll_ext.setBounds(10, y_comp, 330, 100);
		y_comp += scroll_ext.getHeight() - h_button;
		btn_ext_del.setBounds(350, y_comp, 100, h_button);
		y_comp -= h_button + 10;
		btn_ext_add.setBounds(350, y_comp, 100, h_button);
		y_comp = btn_ext_del.getY() + h_button + 10;
		label_filetype.setBounds(5, y_comp, 80, h_button);
		combo_filetype.setBounds(90, y_comp, 140, h_button);
		btn_ok.setBounds(240, y_comp, 100, h_button);
		btn_cancel.setBounds(350, y_comp, 100, h_button);
		setContentSize(new Dimension(460, y_comp + h_button + 20));
	}

	private void removeFileFilter() {
		var ok = JOptionPane.showConfirmDialog(this, translate("delete_filter_confirm"), getTitle(), JOptionPane.YES_NO_OPTION);
		if (ok == JOptionPane.YES_OPTION) {
			filterList.remove(filter);
			model_filter.removeElement(filter);
			updateFileFilter();
		}
	}

	private void removeFileNameExtension() {
		final var index = list_ext.getSelectedIndex();
		var item = list_ext.getSelectedValue();
		model_ext.removeElementAt(index);
		list_ext.setSelectedIndex(index);
		filter.removeFileNameExtension(item);
		btn_ext_del.setEnabled(!model_ext.isEmpty());
	}

	private void setListeners() {
		combo_filter.addItemListener(e -> {
			filter = (ListFileFilter) combo_filter.getSelectedItem();
			updateFileFilter();
		});
		btn_filter_add.addActionListener(e -> addFileFilter());
		btn_filter_del.addActionListener(e -> removeFileFilter());
		list_ext.addListSelectionListener(e -> btn_ext_del.setEnabled(true));
		btn_ext_add.addActionListener(e -> addFileNameExtension());
		btn_ext_del.addActionListener(e -> removeFileNameExtension());
		combo_filetype.addItemListener(e -> {
			var t = model_filetype.getSelectedItem();
			if (t != null) filter.setFileType((FileType) t);
		});
		btn_ok.addActionListener(e -> {
			isApproved = OK_OPTION;
			dispose();
		});
		btn_cancel.addActionListener(e -> {
			isApproved = CANCEL_OPTION;
			dispose();
		});
	}

	/**
	 * ダイアログを表示します。
	 *
	 * @return ダイアログがOKボタンで閉じられた場合true
	 */
	public boolean showDialog() {
		setVisible(true);
		return isApproved;
	}

	private void updateFileFilter() {
		model_ext.clear();
		model_filetype.removeAllElements();
		if (filter != null) {
			for (var ext : filter.getFileNameExtensionList()) {
				model_ext.addElement(ext);
			}
			var fileType = filter.getFileType();
			model_filetype.addElement(FileType.TEXT);
			model_filetype.addElement(FileType.IMAGE);
			model_filetype.addElement(FileType.BINARY);
			model_filetype.setSelectedItem(fileType);
		}
		var enable = filter != null;
		btn_filter_del.setEnabled(enable);
		btn_ext_add.setEnabled(enable);
		btn_ext_del.setEnabled(!model_ext.isEmpty());
	}
}
