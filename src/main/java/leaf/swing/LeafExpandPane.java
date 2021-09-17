/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * {@link LeafTaskPane}に表示される展開・収納可能なコンテナです。
 *
 * @author 無線部開発班
 * @since 2010年7月10日
 */
public abstract class LeafExpandPane extends JComponent {
	/**
	 * コンテナが下方向に展開することを示します。
	 */
	public static final int DROP_DOWN_EXPAND = 0;
	/**
	 * コンテナが上方向に展開することを示します。
	 */
	public static final int DROP_UP_EXPAND = 1;
	static final int TITLE_HEIGHT = 30;
	private static final long serialVersionUID = 1L;
	final JComponent content;
	private final JLabel iconLabel, titleLabel, button;
	private final Icon expandicon, foldicon;
	private final ExpandLayout layout;
	boolean isExpanded = false;
	private Dimension contentSize;

	/**
	 * タイトル文字列を指定して下方向に展開するコンテナを構築します。
	 *
	 * @param title 表示するタイトル
	 */
	public LeafExpandPane(String title) {
		this(title, null, DROP_DOWN_EXPAND);
	}

	/**
	 * タイトル文字列とアイコンを指定して下方向に展開するコンテナを構築します。
	 *
	 * @param title 表示するタイトル
	 * @param icon  表示するアイコン
	 */
	public LeafExpandPane(String title, Icon icon) {
		this(title, icon, DROP_DOWN_EXPAND);
	}

	/**
	 * タイトル文字列とアイコン及び展開方向を指定してコンテナを構築します。
	 *
	 * @param title 表示するタイトル
	 * @param icon  表示するアイコン
	 * @param arrow 展開する方向 {@link #DROP_DOWN_EXPAND} {@link #DROP_UP_EXPAND}
	 */
	public LeafExpandPane(String title, Icon icon, final int arrow) {
		if (arrow == DROP_DOWN_EXPAND) {
			expandicon = LeafIcons.getIcon("DROP_DOWN");
			foldicon = LeafIcons.getIcon("DROP_UP");
		} else if (arrow == DROP_UP_EXPAND) {
			expandicon = LeafIcons.getIcon("DROP_UP");
			foldicon = LeafIcons.getIcon("DROP_DOWN");
		} else throw new IllegalArgumentException();
		setLayout(layout = new ExpandLayout(this));
		var titlePane = new JPanel(new BorderLayout());
		add(titlePane, BorderLayout.NORTH);
		iconLabel = new TitleLabel(icon);
		titleLabel = new TitleLabel(title);
		button = new TitleLabel(expandicon);
		button.setPreferredSize(new Dimension(25, 25));
		titlePane.add(iconLabel, BorderLayout.WEST);
		titlePane.add(titleLabel, BorderLayout.CENTER);
		titlePane.add(button, BorderLayout.EAST);
		titlePane.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		titlePane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setExpanded(!isExpanded());
			}
		});
		this.content = createContent();
		setContentSize(new Dimension(200, 180));
		Border outer = BorderFactory.createMatteBorder(0, 2, 2, 2, Color.WHITE);
		var inner = BorderFactory.createEmptyBorder(2, 0, 2, 0);
		Border inout = BorderFactory.createCompoundBorder(outer, inner);
		content.setBorder(inout);
		final var height = titlePane.getPreferredSize().height;
		setMaximumSize(new Dimension(Short.MAX_VALUE, height));
	}

	/**
	 * このコンテナにExpandListenerを追加します。
	 *
	 * @param listener 追加するリスナー
	 */
	public void addExpandListener(ExpandListener listener) {
		listenerList.add(ExpandListener.class, listener);
	}

	/**
	 * コンテナに配置するコンポーネントを生成します。
	 *
	 * @return コンテナに配置するコンポーネント
	 */
	protected abstract JComponent createContent();

	final void fireExpandListeners() {
		final var e = new ExpandEvent(this);
		for (var l : listenerList.getListeners(ExpandListener.class)) {
			l.stateChanged(e);
		}
	}

	/**
	 * コンテナに配置されるコンポーネントの推奨されるサイズを返します。
	 *
	 * @return レイアウトの評価時に用いられる推奨サイズ
	 */
	public Dimension getContentSize() {
		return contentSize;
	}

	/**
	 * コンテナに配置されるコンポーネントの推奨されるサイズを設定します。
	 *
	 * @param size レイアウトの評価時に用いられる推奨サイズ
	 */
	public void setContentSize(Dimension size) {
		final var old = contentSize;
		content.setPreferredSize(contentSize = size);
		firePropertyChange("contentSize", old, size);
	}

	/**
	 * コンテナに表示されているグラフィックアイコンを返します。
	 *
	 * @return コンテナに表示されているアイコン
	 */
	public Icon getIcon() {
		return iconLabel.getIcon();
	}

	/**
	 * コンテナに表示されるグラフィックアイコンを設定します。
	 *
	 * @param icon コンテナに表示されるアイコン
	 */
	public void setIcon(Icon icon) {
		iconLabel.setIcon(icon);
	}

	/**
	 * コンテナに表示されている1行のテキストを返します。
	 *
	 * @return コンテナに表示されているテキスト
	 */
	public String getText() {
		return titleLabel.getText();
	}

	/**
	 * コンテナに表示される1行のテキストを設定します。
	 *
	 * @param text コンテナに表示されるテキスト
	 */
	public void setText(String text) {
		titleLabel.setText(text);
	}

	/**
	 * コンテナが展開され、内部コンポーネントが可視状態であるか返します。
	 * 展開収納状態が遷移中である場合は、遷移後の状態を返します。
	 *
	 * @return 展開されている場合true 収納されている場合false
	 */
	public boolean isExpanded() {
		return layout.isExpandingOrFolding() != isExpanded;
	}

	/**
	 * コンテナの展開/収納状態の切り替え動作を開始します。
	 *
	 * @param b 展開する場合true 収納する場合false
	 */
	public void setExpanded(boolean b) {
		button.setIcon(b ? foldicon : expandicon);
		if (b) add(content, BorderLayout.CENTER);
		layout.setExpanded(isExpanded = b);
	}

	/**
	 * このコンテナからExpandListenerを削除します。
	 *
	 * @param listener 削除するリスナー
	 */
	public void removeExpandListener(ExpandListener listener) {
		listenerList.remove(ExpandListener.class, listener);
	}

}
