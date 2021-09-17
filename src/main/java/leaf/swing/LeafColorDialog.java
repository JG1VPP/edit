/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

/**
 * 配色を設定するために使用するモーダルダイアログです。
 *
 * @author 無線部開発班
 * @since 2011年1月5日
 */
public final class LeafColorDialog extends LeafDialog {
	static final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	private final ArrayList<ColorPanel> panels;
	private Box box;
	private JScrollPane scroll;
	private JButton bok, bcancel;
	private Map<String, Color> map;
	private boolean isChanged = CANCEL_OPTION;

	/**
	 * 親フレームを指定してダイアログを生成します。
	 *
	 * @param owner 親フレーム
	 */
	public LeafColorDialog(Frame owner) {
		this(owner, null);
	}

	/**
	 * 親ダイアログを指定してダイアログを生成します。
	 *
	 * @param owner 親ダイアログ
	 */
	public LeafColorDialog(Dialog owner) {
		this(owner, null);
	}

	/**
	 * 親フレームとマップを指定してダイアログを生成します。
	 *
	 * @param owner 親フレーム
	 * @param map   マップ
	 */
	public LeafColorDialog(Frame owner, Map<String, Color> map) {
		super(owner, true);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isChanged = CANCEL_OPTION;
			}
		});
		panels = new ArrayList<>(5);
		this.map = map;
		setLayout(null);
		initialize();
	}

	/**
	 * 親ダイアログとマップを指定してダイアログを生成します。
	 *
	 * @param owner 親ダイアログ
	 * @param map   マップ
	 */
	public LeafColorDialog(Dialog owner, Map<String, Color> map) {
		super(owner, true);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isChanged = CANCEL_OPTION;
			}
		});
		panels = new ArrayList<>(5);
		this.map = map;
		setLayout(null);
		initialize();
	}

	/**
	 * ユーザーにより設定された配色を返します。
	 *
	 * @return 配色マップ
	 */
	public Map<String, Color> getResult() {
		for (var panel : panels) {
			map.put(panel.key, panel.color);
		}
		return map;
	}

	/**
	 * ダイアログの表示を初期化します。
	 */
	@Override
	public void initialize() {
		getContentPane().removeAll();
		setTitle(translate("title"));
		box = Box.createVerticalBox();
		scroll = new JScrollPane(box);
		add(scroll);
		bok = new JButton(translate("button_ok"));
		bcancel = new JButton(translate("button_cancel"));
		add(bok);
		add(bcancel);
		bok.addActionListener(e -> {
			isChanged = OK_OPTION;
			dispose();
		});
		bcancel.addActionListener(e -> {
			isChanged = CANCEL_OPTION;
			dispose();
		});
		layoutComponents();
		setMap(map);
	}

	private void layoutComponents() {
		scroll.setBounds(5, 5, 230, 120);
		var pref = bok.getPreferredSize().height;
		bok.setBounds(20, 130, 100, pref);
		bcancel.setBounds(130, 130, 100, pref);
		setContentSize(new Dimension(240, 140 + pref));
	}

	private void loadSettings() {
		box.removeAll();
		for (var key : map.keySet()) {
			var pane = new ColorPanel(key, map.get(key));
			panels.add(pane);
			box.add(pane);
		}
	}

	/**
	 * 色の設定を並べたマップを設定します。
	 *
	 * @param map 配色マップ
	 */
	public void setMap(Map<String, Color> map) {
		this.map = map;
		loadSettings();
	}

	/**
	 * ダイアログを表示します。
	 *
	 * @return OKボタンで閉じられた場合true
	 */
	public boolean showDialog() {
		loadSettings();
		setVisible(true);
		return isChanged;
	}

	private class ColorPanel extends JPanel {
		public final String key;
		private final JTextField field;
		private final JLabel button;
		public Color color;

		public ColorPanel(String key, Color col) {
			super(new BorderLayout());
			this.key = key;
			field = new JTextField();
			field.setEditable(false);
			field.setFont(font);
			add(field, BorderLayout.CENTER);
			button = new JLabel();
			add(button, BorderLayout.EAST);
			button.setOpaque(true);
			button.setPreferredSize(new Dimension(32, 0));
			button.addMouseListener(new ButtonListener());
			setColor(col);
		}

		private void setColor(Color col) {
			this.color = col != null ? col : Color.WHITE;
			button.setBackground(color);
			field.setText(String.format("%#x : %s", color.getRGB(), key));
		}

		private class ButtonListener extends MouseAdapter {
			public void mousePressed(MouseEvent e) {
				setColor(JColorChooser.showDialog(LeafColorDialog.this, translate("chooser_title"), color));
			}
		}
	}

}
