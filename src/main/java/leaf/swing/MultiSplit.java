/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import javax.swing.*;

/**
 * コンテナの多重分割を実装します。
 * <p>
 * {@link #createComponent()}を実装することで多重分割機能を提供します。
 * <p>
 * フォーカスを監視して多重分割を実装するため、
 * フォーカスを取得できないコンポーネントを配置することは推奨されません。
 *
 * @author 無線部開発班
 * @since 2012/04/30
 */
public abstract class MultiSplit extends JComponent {
	/**
	 * 縦方向の分割です。
	 */
	public static final int VERTICAL_SPLIT = JSplitPane.VERTICAL_SPLIT;
	/**
	 * 横方向の分割です。
	 */
	public static final int HORIZONTAL_SPLIT = JSplitPane.HORIZONTAL_SPLIT;
	private static final long serialVersionUID = 1L;
	private final LinkedList<Component> compList;
	private final boolean mode;
	private Component focused;

	/**
	 * 自動再描画処理が無効な分割コンテナを生成します。
	 */
	public MultiSplit() {
		this(false);
	}

	/**
	 * 自動再描画処理の有無を指定して分割コンテナを生成します。
	 *
	 * @param mode ディ倍打のドラッグ中の再描画処理の有無
	 */
	public MultiSplit(boolean mode) {
		setLayout(new BorderLayout());
		compList = new LinkedList<>();
		add(focused = getNewComponent(), BorderLayout.CENTER);
		this.mode = mode;
	}

	/**
	 * 画面分割時に新たに配置するコンポーネントを指定します。
	 *
	 * @return 配置するコンポーネント
	 */
	protected abstract Component createComponent();

	/**
	 * 画面分割前の最初に配置されるコンポーネントを指定します。
	 *
	 * @return 配置するコンポーネント
	 */
	protected abstract Component createFirstComponent();

	/**
	 * 画面分割により追加されたコンポーネントの一覧を返します。
	 *
	 * @return コンポーネントの配列
	 */
	public Component[] getAddedComponents() {
		return compList.toArray(new Component[0]);
	}

	/**
	 * createComponentメソッドをラップします。
	 *
	 * @return 追加するコンポーネント
	 */
	private Component getNewComponent() {
		final var comp = (compList.size() == 0) ? createFirstComponent() : createComponent();
		FocusListener listener = new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				focused = comp;
			}
		};
		var list = new LinkedList<Component>();
		list.add(comp);
		while (list.size() > 0) {
			var child = list.get(0);
			list.remove(child);
			if (child.isFocusable()) {
				child.addFocusListener(listener);
			}
			if (child instanceof Container) {
				var comps = ((Container) child).getComponents();
				Collections.addAll(list, comps);
			}
		}
		compList.add(comp);
		return comp;
	}

	/**
	 * 分割対象のコンポーネントを含む親分割コンテナを返します。
	 */
	private JSplitPane getParentSplitPane() {
		Component comp = focused.getParent();
		if (comp instanceof JSplitPane) return (JSplitPane) comp;
		else return null;
	}

	/**
	 * コンテナの分割を1段階解除します。
	 *
	 * @return 不要になったコンポーネント
	 */
	public Component[] merge() {
		var split = getParentSplitPane();
		if (split == null) return new Component[0];
		Component maintain = focused, remove;
		if (focused == split.getLeftComponent()) {
			remove = split.getRightComponent();
		} else {
			remove = split.getLeftComponent();
		}
		var cont = split.getParent();
		if (cont == null) return new Component[0];
		if (cont instanceof JSplitPane) {
			var parent = (JSplitPane) cont;
			var divider = parent.getDividerLocation();
			if (split == parent.getLeftComponent()) {
				parent.remove(split);
				parent.setLeftComponent(maintain);
			} else {
				parent.remove(split);
				parent.setRightComponent(maintain);
			}
			parent.setDividerLocation(divider);
		} else {
			setVisible(false);
			remove(split);
			add(maintain, BorderLayout.CENTER);
			setVisible(true);
		}
		return searchComponents(remove);
	}

	/**
	 * 削除する分割コンテナの子コンポーネントを探索します。
	 *
	 * @param remove 削除するコンポーネント
	 *
	 * @return コンポーネントの配列
	 */
	private Component[] searchComponents(Component remove) {
		var list = new LinkedList<Component>();
		list.add(remove);
		for (var i = 0; i < list.size(); i++) {
			var child = list.get(i);
			if (child instanceof JSplitPane) {
				var split = (JSplitPane) child;
				list.add(split.getLeftComponent());
				list.add(split.getRightComponent());
				list.remove(split);
				i--;
			} else compList.remove(child);
		}
		return list.toArray(new Component[0]);
	}

	/**
	 * コンテナを1段階分割します。
	 *
	 * @param orient 分割方向
	 */
	public void split(int orient) {
		if (!(focused.getParent() instanceof JSplitPane)) {
			splitFirst(orient);
			validate();
		} else splitNext(orient);
	}

	/**
	 * 未分割の画面分割コンテナを分割します。
	 *
	 * @param orient 分割方向
	 */
	private void splitFirst(int orient) {
		var comp = getNewComponent();
		var split = new JSplitPane(orient, mode);
		split.setOneTouchExpandable(true);
		split.setLeftComponent(focused);
		split.setRightComponent(comp);
		remove(focused);
		add(split, BorderLayout.CENTER);
		split.setDividerLocation((((orient == VERTICAL_SPLIT) ? getHeight() : getWidth()) - split.getDividerSize()) / 2);
	}

	/**
	 * 既に分割されている画面分割コンテナを分割します。
	 *
	 * @param orient 分割方向
	 */
	private void splitNext(int orient) {
		var comp = getNewComponent();
		var parent = getParentSplitPane();
		var divider = Objects.requireNonNull(parent).getDividerLocation();
		var child = new JSplitPane(orient, mode);
		child.setOneTouchExpandable(true);
		if (parent.getLeftComponent() == focused) {
			parent.remove(focused);
			parent.setLeftComponent(child);
		} else {
			parent.remove(focused);
			parent.setRightComponent(child);
		}
		parent.setDividerLocation(divider);
		child.setLeftComponent(focused);
		child.setRightComponent(comp);
		child.setDividerLocation((((orient == VERTICAL_SPLIT) ? focused.getHeight() : focused.getWidth()) - child.getDividerSize()) / 2);
	}

}
