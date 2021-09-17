/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import leaf.swing.LeafColorDialog;
import leaf.swing.LeafDialog;
import leaf.swing.KeywordSet;
import leaf.swing.SyntaxManager;

/**
 * テキストエディタに適用されるキーワード強調機能の設定を行うダイアログです。
 *
 * @author 無線部開発班
 * @since 2010/09/15
 */
public final class SyntaxDialog extends LeafDialog {

	private final LeafColorDialog dialog;
	private final int BLOCK_COMMENT_START = 0;
	private final int BLOCK_COMMENT_END = 1;
	private final int LINE_COMMENT_START = 2;
	private final SyntaxManager manager;
	private boolean isChanged = CANCEL_OPTION;
	private DefaultComboBoxModel<KeywordSet> setmodel;
	private List<KeywordSet> sets;
	private KeywordSet set = null;
	private List<String> keywords = null;
	private JPanel panel_keywords, panel_comment;
	private JLabel label_set;
	private JComboBox<KeywordSet> combo_set;
	private JButton button_add_set;
	private JButton button_del_set;
	private JButton button_ext_set;
	private JButton button_add_keyword;
	private JButton button_edit_keyword;
	private JButton button_del_keyword;
	private JButton button_comment_start;
	private JButton button_comment_end;
	private JButton button_comment_line;
	private JButton button_colors;
	private JButton button_ok;
	private JButton button_cancel;
	private JScrollPane scroll_keywords;
	private JList<String> list_keywords;
	private Map<String, Color> colors;

	/**
	 * 親フレームとLeafSyntaxManagerを指定して設定画面を生成します。
	 *
	 * @param owner   親フレーム
	 * @param manager 設定保存先のLeafSyntaxManager
	 */
	public SyntaxDialog(Frame owner, SyntaxManager manager) {
		super(owner, null, true);
		setResizable(false);
		setLayout(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isChanged = CANCEL_OPTION;
			}
		});
		colors = SyntaxManager.getColorMap();
		dialog = new LeafColorDialog(this, colors);
		this.manager = manager;
		initialize();
	}

	/**
	 * 親ダイアログとSyntaxManagerを指定して設定画面を生成します。
	 *
	 * @param owner   親ダイアログ
	 * @param manager 設定保存先のSyntaxManager
	 */
	public SyntaxDialog(Dialog owner, SyntaxManager manager) {
		super(owner, null, true);
		setResizable(false);
		setLayout(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isChanged = CANCEL_OPTION;
			}
		});
		colors = SyntaxManager.getColorMap();
		dialog = new LeafColorDialog(this, colors);
		this.manager = manager;
		initialize();
	}

	private void addKeyword() {
		var word = JOptionPane.showInputDialog(this, translate("addKeyword_message"), getTitle(), JOptionPane.PLAIN_MESSAGE);
		if (word != null) addKeyword(word);
	}

	private void addKeyword(String word) {
		if (keywords.contains(word)) {
			showMessage(translate("addKeyword_duplicate"));
		} else {
			keywords.add(word);
			Collections.sort(keywords);
			var arr = keywords.toArray(new String[0]);
			list_keywords.setListData(arr);
		}
		list_keywords.setSelectedValue(word, true);
		list_keywords.repaint();
	}

	private void addSet() {
		var name = (String) JOptionPane.showInputDialog(this, translate("addSet_message"), getTitle(), JOptionPane.PLAIN_MESSAGE, null, null, "");
		if (name == null) return;
		for (var set : sets) {
			if (set.getName().equals(name)) {
				showMessage(translate("addSet_duplicate"));
				return;
			}
		}
		set = new KeywordSet(name);
		sets.add(set);
		setmodel.addElement(set);
		combo_set.setSelectedItem(set);
		update();
	}

	private void editKeyword() {
		var word = list_keywords.getSelectedValue();
		keywords.remove(word);
		word = (String) JOptionPane.showInputDialog(this, translate("editKeyword_message"), getTitle(), JOptionPane.PLAIN_MESSAGE, null, null, word);
		if (word != null) addKeyword(word);
	}

	@Override
	public void initialize() {
		if (manager.getKeywordSetCount() > 0) {
			sets = new ArrayList<>(manager.getKeywordSets());
			var arr = sets.toArray(new KeywordSet[0]);
			setmodel = new DefaultComboBoxModel<>(arr);
		} else {
			sets = new ArrayList<>();
			setmodel = new DefaultComboBoxModel<>();
		}
		getContentPane().removeAll();
		setTitle(translate("title"));
		label_set = new JLabel(translate("label_set"), JLabel.RIGHT);
		combo_set = new JComboBox<>(setmodel);
		set = (KeywordSet) combo_set.getSelectedItem();
		add(label_set);
		add(combo_set);
		button_add_set = new JButton(translate("button_add_set"));
		button_del_set = new JButton(translate("button_del_set"));
		button_ext_set = new JButton(translate("button_ext_set"));
		add(button_add_set);
		add(button_del_set);
		add(button_ext_set);
		panel_keywords = new JPanel(null);
		Border etch = new EtchedBorder(EtchedBorder.LOWERED);
		panel_keywords.setBorder(new TitledBorder(etch, translate("panel_keywords")));
		add(panel_keywords);
		list_keywords = new JList<>();
		list_keywords.setLayoutOrientation(JList.VERTICAL_WRAP);
		list_keywords.setVisibleRowCount(0);
		scroll_keywords = new JScrollPane(list_keywords);
		panel_keywords.add(scroll_keywords);
		button_add_keyword = new JButton(translate("button_add_keyword"));
		button_edit_keyword = new JButton(translate("button_edit_keyword"));
		button_del_keyword = new JButton(translate("button_del_keyword"));
		panel_keywords.add(button_add_keyword);
		panel_keywords.add(button_edit_keyword);
		panel_keywords.add(button_del_keyword);
		panel_comment = new JPanel(null);
		etch = new EtchedBorder(EtchedBorder.LOWERED);
		panel_comment.setBorder(new TitledBorder(etch, translate("panel_comment")));
		add(panel_comment);
		button_comment_start = new JButton(translate("button_comment_start"));
		button_comment_end = new JButton(translate("button_comment_end"));
		button_comment_line = new JButton(translate("button_comment_line"));
		panel_comment.add(button_comment_start);
		panel_comment.add(button_comment_end);
		panel_comment.add(button_comment_line);
		button_colors = new JButton(translate("button_colors"));
		button_ok = new JButton(translate("button_ok"));
		button_cancel = new JButton(translate("button_cancel"));
		add(button_colors);
		add(button_ok);
		add(button_cancel);
		setListeners();
		layoutComponents();
		update();
	}

	private void layoutComponents() {
		var pref_button = button_add_set.getPreferredSize().height;
		var y_comp = 5;
		var b = panel_keywords.getBorder().getBorderInsets(panel_keywords);
		label_set.setBounds(0, y_comp, 50, pref_button);
		combo_set.setBounds(55, y_comp, 120, pref_button);
		button_add_set.setBounds(180, y_comp, 80, pref_button);
		button_del_set.setBounds(265, y_comp, 80, pref_button);
		button_ext_set.setBounds(350, y_comp, 95, pref_button);
		y_comp += 5 + pref_button;
		scroll_keywords.setBounds(5, b.top + 5, 450 - b.left - b.right, 150);
		list_keywords.setFixedCellWidth(scroll_keywords.getWidth() / 4);
		button_add_keyword.setBounds(120, b.top + 160, 100, pref_button);
		button_edit_keyword.setBounds(230, b.top + 160, 100, pref_button);
		button_del_keyword.setBounds(340, b.top + 160, 100, pref_button);
		var h_kwd = b.top + 170 + pref_button + b.bottom;
		panel_keywords.setBounds(0, y_comp, 450, h_kwd);
		y_comp += 5 + panel_keywords.getHeight();
		button_comment_start.setBounds(120, b.top + 5, 100, pref_button);
		button_comment_end.setBounds(230, b.top + 5, 100, pref_button);
		button_comment_line.setBounds(340, b.top + 5, 100, pref_button);
		var h_com = b.top + 10 + pref_button + b.bottom;
		panel_comment.setBounds(0, y_comp, 450, h_com);
		y_comp += panel_comment.getHeight() + 5;
		button_colors.setBounds(10, y_comp, 100, pref_button);
		button_ok.setBounds(230, y_comp, 100, pref_button);
		button_cancel.setBounds(340, y_comp, 100, pref_button);
		setContentSize(new Dimension(450, y_comp + pref_button + 10));
	}

	private void removeKeyword() {
		keywords.remove(list_keywords.getSelectedValue());
		var index = list_keywords.getSelectedIndex();
		list_keywords.setListData(keywords.toArray(new String[0]));
		list_keywords.setSelectedIndex(Math.min(index, keywords.size() - 1));
		list_keywords.ensureIndexIsVisible(index);
		button_del_keyword.setEnabled(keywords.size() > 0);
		button_edit_keyword.setEnabled(button_del_keyword.isEnabled());
	}

	private void removeSet() {
		var ok = JOptionPane.showConfirmDialog(this, translate("removeSet_confirm"), getTitle(), JOptionPane.YES_NO_OPTION);
		if (ok == JOptionPane.YES_OPTION) {
			sets.remove(set);
			setmodel.removeElement(set);
			update();
		}
	}

	private void setCommentSign(String message, int type) {
		String sign;
		switch (type) {
			case BLOCK_COMMENT_START:
				sign = set.getCommentBlockStart();
				break;
			case BLOCK_COMMENT_END:
				sign = set.getCommentBlockEnd();
				break;
			default:
				sign = set.getCommentLineStart();
				break;
		}
		sign = (String) JOptionPane.showInputDialog(this, "", message, JOptionPane.PLAIN_MESSAGE, null, null, sign);
		if (sign == null) return;
		switch (type) {
			case BLOCK_COMMENT_START:
				set.setCommentBlockStart(sign);
				break;
			case BLOCK_COMMENT_END:
				set.setCommentBlockEnd(sign);
				break;
			default:
				set.setCommentLineStart(sign);
				break;
		}
	}

	private void setExtension() {
		var sb = new StringBuilder();
		for (Object obj : set.getExtensions()) sb.append(obj).append(";");
		var exts = (String) JOptionPane.showInputDialog(this, translate("setExtension_message"), getTitle(), JOptionPane.PLAIN_MESSAGE, null, null, sb);
		if (exts != null) {
			var list = new ArrayList<String>();
			for (var split : exts.split(";")) {
				if (!split.isEmpty()) list.add(split);
			}
			set.setExtensions(list);
		}
	}

	private void setListeners() {
		combo_set.addItemListener(e -> {
			set = (KeywordSet) combo_set.getSelectedItem();
			update();
		});
		button_add_set.addActionListener(e -> addSet());
		button_del_set.addActionListener(e -> removeSet());
		button_ext_set.addActionListener(e -> setExtension());
		list_keywords.addListSelectionListener(e -> {
			button_edit_keyword.setEnabled(true);
			button_del_keyword.setEnabled(true);
		});
		button_add_keyword.addActionListener(e -> addKeyword());
		button_edit_keyword.addActionListener(e -> editKeyword());
		button_del_keyword.addActionListener(e -> removeKeyword());
		button_comment_start.addActionListener(e -> setCommentSign(translate("block_comment_start"), BLOCK_COMMENT_START));
		button_comment_end.addActionListener(e -> setCommentSign(translate("block_comment_end"), BLOCK_COMMENT_END));
		button_comment_line.addActionListener(e -> setCommentSign(translate("line_comment_start"), LINE_COMMENT_START));
		button_colors.addActionListener(e -> {
			if (dialog.showDialog() == OK_OPTION) {
				colors = dialog.getResult();
			}
		});
		button_ok.addActionListener(e -> {
			isChanged = OK_OPTION;
			dispose();
		});
		button_cancel.addActionListener(e -> {
			isChanged = CANCEL_OPTION;
			dispose();
		});
	}

	/**
	 * ダイアログを表示します。OKボタンで閉じられた場合設定をマネージャに適用します。
	 *
	 * @return OKボタンで閉じられた場合true
	 */
	public boolean showDialog() {
		setVisible(true);
		if (isChanged) {
			manager.setKeywordSets(sets);
			SyntaxManager.setColorMap(colors);
		}
		return isChanged;
	}

	private void update() {
		if (set != null) keywords = set.getKeywords();
		else keywords = new ArrayList<>();
		list_keywords.setListData(keywords.toArray(new String[0]));
		button_del_set.setEnabled(!sets.isEmpty());
		button_ext_set.setEnabled(button_del_set.isEnabled());
		var enable = (set != null);
		button_add_keyword.setEnabled(enable);
		button_edit_keyword.setEnabled(false);
		button_del_keyword.setEnabled(false);
		button_comment_start.setEnabled(enable);
		button_comment_end.setEnabled(enable);
		button_comment_line.setEnabled(enable);
	}
}
