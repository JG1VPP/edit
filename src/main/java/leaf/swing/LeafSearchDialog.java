/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

/**
 * {@link JTextComponent}の文字列検索用のダイアログです。
 *
 * @author 無線部開発班
 * @since 2010年5月22日
 */
public final class LeafSearchDialog extends LeafDialog {
	/**
	 * 検索する方向を指定します。
	 *
	 * @author 無線部開発班
	 * @since 2021/09/21
	 */
	public enum Ward {
		/**
		 * 前方を検索する動作を指定します
		 */
		SEARCH_UPWARD,
		/**
		 * 後方を検索する動作を指定します
		 */
		SEARCH_DOWNWARD,
		/**
		 * 先頭を検索する動作を指定します
		 */
		SEARCH_FIRST,
		/**
		 * 末尾を検索する動作を指定します
		 */
		SEARCH_LAST
	}
	private static final int HISTORY_MAX = 30;
	private JButton button_close;
	private JButton button_first;
	private JButton button_next;
	private JCheckBox ch_case;
	private JCheckBox ch_dotall;
	private JCheckBox ch_regex;
	private JComboBox<String> combo_pattern;
	private JLabel label_pattern;
	private JPanel panel_updown;
	private JRadioButton radio_down;
	private JRadioButton radio_up;
	private Matcher matcher;
	private JTextComponent textComp;

	/**
	 * このダイアログの所有者を指定してモーダレスダイアログを構築します。
	 *
	 * @param owner このダイアログの親となるFrame
	 */
	public LeafSearchDialog(Frame owner) {
		super(owner, false);
		setResizable(false);
		getContentPane().setLayout(null);
		initialize();
		addComponentListener(new DialogShownListener());
	}


	/**
	 * このダイアログの所有者を指定してモーダレスダイアログを構築します。
	 *
	 * @param owner このダイアログの親となるDialog
	 */
	public LeafSearchDialog(Dialog owner) {
		super(owner, false);
		setResizable(false);
		getContentPane().setLayout(null);
		initialize();
		addComponentListener(new DialogShownListener());
	}

	private void addItem(JComboBox<String> combo, String text) {
		DefaultComboBoxModel<String> model;
		if (text != null && !text.isEmpty()) {
			model = (DefaultComboBoxModel<String>) combo.getModel();
			model.removeElement(text);
			model.insertElementAt(text, 0);
			if (model.getSize() > HISTORY_MAX) {
				model.removeElementAt(HISTORY_MAX);
			}
			combo.setSelectedIndex(0);
		}
	}

	private String getText(JComboBox<String> combo) {
		return (String) combo.getEditor().getItem();
	}

	/**
	 * このダイアログに関連付けられたJTextComponentを返します。
	 *
	 * @return 検索対象
	 */
	public JTextComponent getTextComponent() {
		return textComp;
	}

	/**
	 * このダイアログに関連付けるJTextComponentを指定します。
	 *
	 * @param comp 検索対象
	 */
	public void setTextComponent(JTextComponent comp) {
		final var old = this.textComp;
		this.textComp = comp;
		firePropertyChange("textComponent", old, comp);
	}

	@Override
	public void initialize() {
		setTitle(translate("title"));
		getContentPane().removeAll();
		label_pattern = new JLabel(translate("label_pattern"));
		combo_pattern = new JComboBox<>();
		combo_pattern.setEditable(true);
		add(label_pattern);
		add(combo_pattern);
		ch_case = new JCheckBox(translate("check_case_sensitive"));
		ch_regex = new JCheckBox(translate("check_regex"));
		ch_dotall = new JCheckBox(translate("check_dotall"));
		ch_regex.setSelected(true);
		ch_case.setMnemonic(KeyEvent.VK_C);
		ch_regex.setMnemonic(KeyEvent.VK_G);
		ch_dotall.setMnemonic(KeyEvent.VK_O);
		add(ch_case);
		add(ch_regex);
		add(ch_dotall);
		panel_updown = new JPanel(new GridLayout(1, 2));
		panel_updown.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), translate("panel_direction")));
		add(panel_updown);
		radio_up = new JRadioButton(translate("radio_direction_up"));
		radio_up.setMnemonic(KeyEvent.VK_U);
		panel_updown.add(radio_up);
		radio_down = new JRadioButton(translate("radio_direction_down"));
		radio_down.setMnemonic(KeyEvent.VK_D);
		radio_down.setSelected(true);
		panel_updown.add(radio_down);
		var group = new ButtonGroup();
		group.add(radio_up);
		group.add(radio_down);
		button_next = new JButton(translate("button_find_next"));
		button_first = new JButton(translate("button_find_first"));
		button_close = new JButton(translate("button_close"));
		add(button_next);
		add(button_first);
		add(button_close);
		button_next.setMnemonic(KeyEvent.VK_N);
		button_first.setMnemonic(KeyEvent.VK_F);
		button_close.setMnemonic(KeyEvent.VK_C);
		layoutComponents();
		combo_pattern.getEditor().addActionListener(e -> search((radio_down.isSelected()) ? Ward.SEARCH_DOWNWARD : Ward.SEARCH_UPWARD));
		ch_regex.addActionListener(e -> ch_dotall.setEnabled(ch_regex.isSelected()));
		button_next.addActionListener(e -> search((radio_down.isSelected()) ? Ward.SEARCH_DOWNWARD : Ward.SEARCH_UPWARD));
		button_first.addActionListener(e -> search((radio_down.isSelected()) ? Ward.SEARCH_FIRST : Ward.SEARCH_LAST));
		button_close.addActionListener(e -> dispose());
	}

	private void layoutComponents() {
		var label_y = 10;
		var pref = button_next.getPreferredSize().height;
		label_pattern.setBounds(5, label_y, 60, pref);
		combo_pattern.setBounds(65, label_y, 300, pref);
		var ch_y = label_y + pref + 10;
		var rb_h = radio_up.getPreferredSize().height;
		var b = panel_updown.getBorder().getBorderInsets(panel_updown);
		panel_updown.setBounds(200, ch_y, 170, 10 + rb_h + b.top + b.bottom);
		ch_y += setBounds(ch_case, 5, ch_y, 190) + 5;
		ch_y += setBounds(ch_regex, 5, ch_y, 190) + 5;
		ch_y += setBounds(ch_dotall, 5, ch_y, 190) + 5;
		var button_y = 10;
		button_y += setBounds(button_next, 380, button_y, 115) + 3;
		button_y += setBounds(button_first, 380, button_y, 115) + 23;
		button_y += setBounds(button_close, 380, button_y, 115) + 10;
		setContentSize(new Dimension(500, Math.max(button_y, ch_y) + 10));
	}

	/**
	 * 検索方向を指定して文字列の検索を開始します。
	 *
	 * @param ward 検索方向
	 *
	 * @return 文字列が見つかった場合その位置 見つからなければ-1
	 */
	public int search(Ward ward) {
		var patternText = getText(combo_pattern);
		if (patternText.isEmpty()) {
			showMessage(translate("search_pattern_empty"));
			return -1;
		} else {
			addItem(combo_pattern, getText(combo_pattern));
			updatePattern(!ch_regex.isSelected());
			textComp.requestFocusInWindow();
			var found = false;
			switch (ward) {
				case SEARCH_UPWARD:
					found = searchUpward(textComp.getSelectionStart());
					break;
				case SEARCH_DOWNWARD:
					found = searchDownward(textComp.getSelectionEnd());
					break;
				case SEARCH_FIRST:
					found = searchDownward(0);
					break;
				case SEARCH_LAST:
					found = searchUpward(-1);
					break;
			}
			return found ? textComp.getSelectionStart() : -1;
		}
	}

	private boolean searchDownward(int position) {
		if (matcher == null) return true;
		if (matcher.find(position)) {
			textComp.select(matcher.start(), matcher.end());
			return true;
		} else {
			showMessage(translate("not_found", getText(combo_pattern)));
			return false;
		}
	}

	private boolean searchUpward(int caretPosition) {
		if (matcher == null) return true;
		var found = false;
		int start = 0, end = 0, mstart, mend;
		while (matcher.find(end)) {
			mstart = matcher.start();
			mend = matcher.end();
			if (mend <= caretPosition && textComp.getSelectionStart() != mstart) {
				start = mstart;
				end = mend;
				found = true;
			} else if (caretPosition < 0) {
				start = mstart;
				end = mend;
				found = true;
			} else break;
		}
		if (found) textComp.select(start, end);
		else showMessage(translate("not_found", getText(combo_pattern)));
		return found;
	}

	private int setBounds(JComponent comp, int x, int y, int width) {
		var bounds = new Rectangle(comp.getPreferredSize());
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		comp.setBounds(bounds);
		return bounds.height;
	}

	/**
	 * 正規表現パターンを更新します。
	 *
	 * @param isLiteralMode 正規表現検索を無効にする場合
	 */
	private void updatePattern(boolean isLiteralMode) {
		var opt = 0;
		if (!ch_case.isSelected()) {
			opt |= Pattern.UNICODE_CASE;
			opt |= Pattern.CASE_INSENSITIVE;
		}
		if (isLiteralMode) opt |= Pattern.LITERAL;
		if (ch_dotall.isSelected()) opt |= Pattern.DOTALL;
		var patternText = getText(combo_pattern);
		try {
			var pattern = Pattern.compile(patternText, opt);
			matcher = pattern.matcher(textComp.getText());
		} catch (PatternSyntaxException ex) {
			showMessage(ex.getDescription());
			matcher = null;
		}
	}

	private class DialogShownListener extends ComponentAdapter {
		public void componentShown(ComponentEvent e) {
			if (textComp != null) {
				addItem(combo_pattern, textComp.getSelectedText());
			}
		}
	}
}
