/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;

import leaf.swing.LeafDialog;
import leaf.util.LocalizeManager;

/**
 * タブの検索を行うためのダイアログです。
 *
 * @author 無線部開発班
 */
public final class TabFindDialog extends LeafDialog {
	private static final long serialVersionUID = 1L;
	private static final int HISTORY_MAX = 20;
	private final LocalizeManager localize;
	private final JTabbedPane tabpane;
	private final List<Integer> result;
	private JComboBox<String> searchcomb;
	private JList<String> list;


	/**
	 * 対象となるタブ付きコンポーネントを指定してダイアログを構築します。
	 *
	 * @param owner   このダイアログの所有者
	 * @param tabpane 検索対象
	 *
	 * @wbp.parser.constructor
	 */
	public TabFindDialog(JFrame owner, JTabbedPane tabpane) {
		super(owner, false);
		setContentSize(new Dimension(420, 160));
		setResizable(false);
		localize = LocalizeManager.get(getClass());
		this.tabpane = tabpane;
		result = new LinkedList<>();
		initialize();
		tabpane.addChangeListener(e -> search());
	}

	/**
	 * 対象となるタブ付きコンポーネントを指定してダイアログを構築します。
	 *
	 * @param owner   このダイアログの所有者
	 * @param tabpane 検索対象
	 */
	public TabFindDialog(JDialog owner, JTabbedPane tabpane) {
		super(owner, false);
		setContentSize(new Dimension(420, 160));
		setResizable(false);
		localize = LocalizeManager.get(getClass());
		this.tabpane = tabpane;
		result = new LinkedList<>();
		initialize();
		tabpane.addChangeListener(e -> search());
	}

	private void addItem(JComboBox<String> combo, String item) {
		DefaultComboBoxModel<String> model;
		if (item != null && !item.isEmpty()) {
			model = (DefaultComboBoxModel<String>) combo.getModel();
			model.removeElement(item);
			model.insertElementAt(item, 0);
			if (model.getSize() > HISTORY_MAX) {
				model.removeElementAt(HISTORY_MAX);
			}
			combo.setSelectedIndex(0);
		}
	}

	@Override
	public void initialize() {
		setTitle(localize.translate("title"));
		getContentPane().removeAll();
		var top = new JPanel(null);
		top.setPreferredSize(new Dimension(420, 10));
		getContentPane().add(top, BorderLayout.NORTH);
		var content = new JPanel(new BorderLayout(5, 5));
		getContentPane().add(content, BorderLayout.CENTER);
		var north = new JPanel(new BorderLayout(5, 5));
		content.add(north, BorderLayout.NORTH);
		var searchlabel = new JLabel(localize.translate("label_partial"));
		north.add(searchlabel, BorderLayout.WEST);
		searchcomb = new JComboBox<>();
		searchcomb.setEditable(true);
		north.add(searchcomb, BorderLayout.CENTER);
		list = new JList<>();
		var scroll = new JScrollPane(list);
		content.add(scroll, BorderLayout.CENTER);
		list.addListSelectionListener(e -> {
			var index = list.getSelectedIndex();
			if (index >= 0) tabpane.setSelectedIndex(result.get(index));
		});
		var south = new JPanel(null);
		south.setLayout(new BoxLayout(south, BoxLayout.X_AXIS));
		content.add(south, BorderLayout.SOUTH);
		south.add(Box.createHorizontalGlue());
		var bsearch = new JButton(localize.translate("button_search"));
		bsearch.setMnemonic(KeyEvent.VK_S);
		south.add(bsearch);
		getRootPane().setDefaultButton(bsearch);
		bsearch.addActionListener(e -> search());
		south.add(Box.createHorizontalStrut(5));
		var bclose = new JButton(localize.translate("button_close"));
		bclose.setMnemonic(KeyEvent.VK_C);
		south.add(bclose);
		bclose.addActionListener(e -> dispose());
		south.add(Box.createHorizontalStrut(5));
		var bottom = new JPanel(null);
		bottom.setPreferredSize(new Dimension(420, 10));
		getContentPane().add(bottom, BorderLayout.SOUTH);
		search();
	}

	public void search() {
		result.clear();
		var partial = (String) searchcomb.getEditor().getItem();
		addItem(searchcomb, partial);
		partial = partial.toLowerCase();
		List<String> titles = new ArrayList<>();
		var current = -1;
		for (var i = 0; i < tabpane.getTabCount(); i++) {
			var title = tabpane.getTitleAt(i).toLowerCase();
			if (title.contains(partial)) {
				result.add(i);
				titles.add(tabpane.getTitleAt(i));
				if (tabpane.getSelectedIndex() == i) current = result.size() - 1;
			}
		}
		list.setListData(titles.toArray(new String[0]));
		if (current >= 0) list.setSelectedIndex(current);
	}
}
