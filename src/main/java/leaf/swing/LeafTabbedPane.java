/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 閉じるボタンとドラッグアンドドロップ機能を持ったタブ領域です。
 *
 * @author 無線部開発班
 * @since 2010年3月12日
 */
public class LeafTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	/*DnD用フィールド*/
	private static final int LINE_WIDTH = 5;
	private final TabTitleGhost glasspane;
	private final Rectangle cursorRect = new Rectangle();
	private final DragSourceListener dsl;
	private final Transferable trans;
	private final DragGestureListener dgl;
	private int index = -1;

	/**
	 * 空のタブ領域を生成します。
	 */
	public LeafTabbedPane() {
		this(TOP, SCROLL_TAB_LAYOUT);
	}

	/**
	 * タブの表示位置を指定して空のタブ領域を生成します。
	 *
	 * @param tabPlacement タブの表示位置
	 */
	public LeafTabbedPane(int tabPlacement) {
		this(tabPlacement, SCROLL_TAB_LAYOUT);
	}

	/**
	 * タブの表示位置とレイアウトポリシーを指定して空のタブ領域を生成します。
	 *
	 * @param tabPlacement    タブの表示位置
	 * @param tabLayoutPolicy レイアウトポリシー
	 */
	public LeafTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		addChangeListener(new TabChangeHandler());
		addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				getSelectedComponent().requestFocusInWindow();
			}
		});
		glasspane = new TabTitleGhost();
		dsl = new TabDragSourceHandler();
		trans = new TabTransferable();
		dgl = new TabDragGestureListener();
		DropTargetListener dtl = new TabDropTargetHandler();
		new DropTarget(glasspane, DnDConstants.ACTION_COPY_OR_MOVE, dtl, true);
	}

	/**
	 * タイトルとアイコン、ツールチップを指定してタブを追加します。
	 *
	 * @param title タイトル
	 * @param icon  アイコン
	 * @param comp  追加するコンポーネント
	 * @param tip   ツールチップ
	 */
	@Override
	public void addTab(String title, Icon icon, Component comp, String tip) {
		addTab(title, icon, comp, null, getTabCount());
		setToolTipTextAt(getTabCount() - 1, tip);
	}

	/**
	 * タイトルとアイコンを指定してタブを追加します。
	 *
	 * @param title タイトル
	 * @param icon  アイコン
	 * @param comp  追加するコンポーネント
	 */
	@Override
	public void addTab(String title, Icon icon, Component comp) {
		addTab(title, icon, comp, null, getTabCount());
	}

	/**
	 * タイトルを指定してタブを追加します。
	 *
	 * @param title タイトル
	 * @param comp  追加するコンポーネント
	 */
	@Override
	public void addTab(String title, Component comp) {
		addTab(title, null, comp, null, getTabCount());
	}

	/**
	 * コンポーネントの名前をタイトルに指定してタブを追加します。
	 *
	 * @param comp 追加するコンポーネント
	 *
	 * @return 追加したコンポーネント
	 */
	@Override
	public Component add(Component comp) {
		addTab(comp.getName(), null, comp, null, getTabCount());
		return comp;
	}

	/**
	 * タイトルを指定してタブを追加します。
	 *
	 * @param title タイトル
	 * @param comp  追加するコンポーネント
	 *
	 * @return 追加したコンポーネント
	 */
	@Override
	public Component add(String title, Component comp) {
		addTab(title, null, comp, null, getTabCount());
		return comp;
	}

	/**
	 * コンポーネントの名前をタイトルに指定して、指定された位置にタブを追加します。
	 *
	 * @param comp  追加するコンポーネント
	 * @param index 追加する位置
	 *
	 * @return 追加したコンポーネント
	 */
	@Override
	public Component add(Component comp, int index) {
		addTab(comp.getName(), null, comp, null, index);
		return comp;
	}

	/**
	 * タブに表示するオブジェクトを指定してタブを追加します。
	 *
	 * @param comp        追加するコンポーネント
	 * @param constraints タブで表示されるオブジェクト
	 */
	@Override
	public void add(Component comp, Object constraints) {
		if (constraints instanceof String) {
			addTab((String) constraints, null, comp, null, getTabCount());
		} else if (constraints instanceof Icon) {
			addTab(null, (Icon) constraints, comp, null, getTabCount());
		} else {
			addTab(comp.getName(), null, comp, null, getTabCount());
		}
	}

	/**
	 * タブに表示するオブジェクトを指定して、指定した位置にタブを追加します。
	 *
	 * @param comp        追加するコンポーネント
	 * @param constraints タブで表示されるオブジェクト
	 */
	@Override
	public void add(Component comp, Object constraints, int index) {
		if (constraints instanceof String) {
			addTab((String) constraints, null, comp, null, index);
		} else if (constraints instanceof Icon) {
			addTab(null, (Icon) constraints, comp, null, index);
		} else {
			addTab(comp.getName(), null, comp, null, index);
		}
	}

	/**
	 * 指定した位置のタブのタイトルを変更します。
	 *
	 * @param index 変更するタブの位置
	 * @param title 新しいタイトル
	 */
	public void setTitleAt(int index, String title) {
		super.setTitleAt(index, title);
		((TabTitleLabel) getTabComponentAt(index)).setTitle(title);
	}

	/**
	 * 指定した位置のタブのアイコンを変更します。
	 *
	 * @param index 変更するタブの位置
	 * @param icon  新しいアイコン
	 */
	public void setIconAt(int index, Icon icon) {
		super.setIconAt(index, icon);
		((TabTitleLabel) getTabComponentAt(index)).setIcon(icon);
	}

	/**
	 * 指定した位置のツールチップを変更します。
	 *
	 * @param index 変更するタブの位置
	 * @param tip   新しいツールチップ
	 */
	public void setToolTipTextAt(int index, String tip) {
		super.setToolTipTextAt(index, tip);
		((TabTitleLabel) getTabComponentAt(index)).setToolTipText(tip);
	}

	/**
	 * タイトルとアイコンを指定して、指定した位置にタブを追加します。
	 *
	 * @param title タイトル
	 * @param icon  アイコン
	 * @param comp  追加するコンポーネント
	 * @param index 追加する位置
	 */
	public void addTab(String title, Icon icon, Component comp, int index) {
		addTab(title, icon, comp, null, index);
	}

	/**
	 * タイトルとアイコンを指定して、指定した位置にタブを追加します。
	 *
	 * @param title タイトル
	 * @param icon  アイコン
	 * @param comp  追加するコンポーネント
	 * @param tip   ツールチップ
	 * @param index 追加する位置
	 */
	public void addTab(String title, Icon icon, Component comp, String tip, int index) {
		var tab = new TabTitleLabel(title, icon, tip);
		super.add(comp, title, index);
		if (index >= 0 && index < getTabCount()) {
			super.setIconAt(index, icon);
			super.setTabComponentAt(index, tab);
			super.setToolTipTextAt(index, tip);
			setSelectedIndex(index);
			comp.requestFocusInWindow();
			if (getTabCount() == 1) tab.setCloseButtonVisible(true);
		}
	}

	/**
	 * {@link TabCloseListener}を追加します。
	 *
	 * @param listener 登録するTabListener
	 */
	public void addTabListener(TabCloseListener listener) {
		listenerList.add(TabCloseListener.class, listener);
	}

	/**
	 * {@link TabCloseListener}のリストを返します。
	 *
	 * @return リスナーのリスト
	 */
	private TabCloseListener[] getTabListenerList() {
		return listenerList.getListeners(TabCloseListener.class);
	}

	private final int getTargetIndex(Point point) {
		var ptab = SwingUtilities.convertPoint(glasspane, point, LeafTabbedPane.this);
		final var tabPlace = getTabPlacement();
		for (var i = 0; i < getTabCount(); i++) {
			var r = getBoundsAt(i);
			if (tabPlace == TOP || tabPlace == BOTTOM) {
				r.setRect(r.x - r.width / 2, r.y, r.width, r.height);
			} else {
				r.setRect(r.x, r.y - r.height / 2, r.width, r.height);
			}
			if (r.contains(ptab)) return i;
		}
		var r = getBoundsAt(getTabCount() - 1);
		if (tabPlace == TOP || tabPlace == BOTTOM) {
			r.setRect(r.x + r.width / 2, r.y, r.width, r.height);
		} else {
			r.setRect(r.x, r.y + r.height / 2, r.width, r.height);
		}
		return (r.contains(ptab)) ? getTabCount() : -1;
	}

	/**
	 * {@link TabCloseListener}を削除します。
	 *
	 * @param listener 削除するTabListener
	 */
	public void removeTabListener(TabCloseListener listener) {
		listenerList.remove(TabCloseListener.class, listener);
	}

	/**
	 * 指定した位置のタイトルとツールチップを変更します。
	 *
	 * @param index 変更するタブの位置
	 * @param title 新しいタイトル
	 * @param tip   新しいツールチップ
	 */
	public void setTitleAt(int index, String title, String tip) {
		setTitleAt(index, title);
		setToolTipTextAt(index, tip);
	}

	/**
	 * タブのタイトルが昇順に並ぶようにタブをソートします。
	 */
	public void sortInAscending() {
		var index = getSelectedIndex();
		var tabs = new ArrayList<TabSortInfo>();
		for (var i = 0; i < getTabCount(); i++) {
			tabs.add(new TabSortInfo(i));
		}
		Collections.sort(tabs);
		removeAll();
		for (var tab : tabs) {
			addTab(tab.title, tab.icon, tab.comp, tab.tooltip);
		}
		setSelectedIndex(index);
	}

	private final class TabChangeHandler implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			var selected = getSelectedIndex();
			for (var i = 0; i < getTabCount(); i++) {
				var tab = (TabTitleLabel) getTabComponentAt(i);
				if (tab != null) tab.setCloseButtonVisible(i == selected);
			}
		}
	}

	private final class TabTitleLabel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final Icon icon_close;
		private final JLabel label_title;
		private final JButton button_close;

		public TabTitleLabel(String title, Icon icon, String tooltip) {
			super(new BorderLayout());
			setOpaque(false);
			label_title = new JLabel(title, icon, JLabel.LEFT);
			label_title.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
			label_title.setToolTipText(tooltip);
			label_title.addMouseListener(new ClickHandler());
			icon_close = new CloseIcon();
			var size = new Dimension();
			size.width = icon_close.getIconWidth();
			size.height = icon_close.getIconHeight();
			button_close = new JButton(new CloseAction());
			button_close.setPreferredSize(size);
			button_close.setBorderPainted(false);
			button_close.setContentAreaFilled(false);
			button_close.setFocusable(false);
			button_close.setFocusPainted(false);
			button_close.setToolTipText("close");
			add(label_title, BorderLayout.CENTER);
			add(button_close, BorderLayout.EAST);
			setBorder(BorderFactory.createEmptyBorder(2, 1, 1, 1));
			setCloseButtonVisible(false);
			var ds = DragSource.getDefaultDragSource();
			ds.createDefaultDragGestureRecognizer(label_title, DnDConstants.ACTION_COPY_OR_MOVE, dgl);
		}

		public void close() {
			var index = indexOfTabComponent(TabTitleLabel.this);
			var cmp = LeafTabbedPane.this.getComponentAt(index);
			var e = new TabCloseEvent(this, cmp);
			for (var l : getTabListenerList()) {
				if (!l.tabClosing(e)) return;
			}
			index = indexOfTabComponent(TabTitleLabel.this);
			if (index >= 0) LeafTabbedPane.this.remove(index);
		}

		public void setCloseButtonVisible(boolean visible) {
			button_close.setVisible(visible);
		}

		public void setIcon(Icon icon) {
			label_title.setIcon(icon);
			fireStateChanged();
		}

		public void setTitle(String title) {
			label_title.setText(title);
			fireStateChanged();
		}

		public void setToolTipText(String tip) {
			label_title.setToolTipText(tip);
		}

		private class ClickHandler extends MouseAdapter {
			@Override
			public void mousePressed(MouseEvent e) {
				setSelectedIndex(indexOfTabComponent(TabTitleLabel.this));
			}
		}

		private class CloseAction extends AbstractAction {
			private static final long serialVersionUID = 1L;

			public CloseAction() {
				super(null, icon_close);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				TabTitleLabel.this.close();
			}
		}
	}

	private final class TabDropTargetHandler extends DropTargetAdapter {
		private Point old = new Point();

		@Override
		public void dragEnter(DropTargetDragEvent e) {
			if (!isDragAcceptable(e)) e.rejectDrag();
			else e.acceptDrag(e.getDropAction());
		}

		@Override
		public void dragOver(final DropTargetDragEvent e) {
			var point = e.getLocation();
			switch (getTabPlacement()) {
				case TOP:
				case BOTTOM:
					initVerticalLine(getTargetIndex(point));
					break;
				case LEFT:
				case RIGHT:
					initHorizontalLine(getTargetIndex(point));
					break;
			}
			glasspane.setPoint(point);
			if (!old.equals(point)) glasspane.repaint();
			old = point;
		}

		@Override
		public void drop(DropTargetDropEvent e) {
			if (isDropAcceptable(e)) {
				moveTab(index, getTargetIndex(e.getLocation()));
				e.dropComplete(true);
			} else e.dropComplete(false);
			repaint();
		}

		private void initHorizontalLine(int i) {
			if (i < 0 || index == i || i - index == 1) {
				cursorRect.setRect(0, 0, 0, 0);
			} else if (i == 0) {
				var r = SwingUtilities.convertRectangle(LeafTabbedPane.this, getBoundsAt(0), glasspane);
				cursorRect.setLocation(r.x, r.y - LINE_WIDTH / 2);
				cursorRect.setSize(r.width, LINE_WIDTH);
			} else {
				var r = SwingUtilities.convertRectangle(LeafTabbedPane.this, getBoundsAt(i - 1), glasspane);
				cursorRect.setLocation(r.x, r.y + r.width - LINE_WIDTH / 2);
				cursorRect.setSize(r.width, LINE_WIDTH);
			}
		}

		private void initVerticalLine(int i) {
			if (i < 0 || index == i || i - index == 1) {
				cursorRect.setRect(0, 0, 0, 0);
			} else if (i == 0) {
				var r = SwingUtilities.convertRectangle(LeafTabbedPane.this, getBoundsAt(0), glasspane);
				cursorRect.setLocation(r.x - LINE_WIDTH / 2, r.y);
				cursorRect.setSize(LINE_WIDTH, r.height);
			} else {
				var r = SwingUtilities.convertRectangle(LeafTabbedPane.this, getBoundsAt(i - 1), glasspane);
				cursorRect.setLocation(r.x + r.width - LINE_WIDTH / 2, r.y);
				cursorRect.setSize(LINE_WIDTH, r.height);
			}
		}

		private boolean isDragAcceptable(DropTargetDragEvent e) {
			var t = e.getTransferable();
			var f = e.getCurrentDataFlavors();
			return t.isDataFlavorSupported(f[0]) && index >= 0;
		}

		private boolean isDropAcceptable(DropTargetDropEvent e) {
			var t = e.getTransferable();
			var f = t.getTransferDataFlavors();
			return t.isDataFlavorSupported(f[0]) && index >= 0;
		}

		private void moveTab(int prev, int next) {
			if (next < 0 || prev == next) return;
			var cmp = getComponentAt(prev);
			var tab = getTabComponentAt(prev);
			var title = getTitleAt(prev);
			var icon = getIconAt(prev);
			var tip = getToolTipTextAt(prev);
			var flg = isEnabledAt(prev);
			var target = prev > next ? next : next - 1;
			remove(prev);
			addTab(title, icon, cmp, tip, target);
			setEnabledAt(target, flg);
			setTabComponentAt(target, tab);
			if (flg) setSelectedIndex(target);
		}
	}

	private final class TabDragSourceHandler extends DragSourceAdapter {
		@Override
		public void dragEnter(DragSourceDragEvent e) {
			e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
		}

		@Override
		public void dragOver(DragSourceDragEvent e) {
			var point = e.getLocation();
			SwingUtilities.convertPointFromScreen(point, glasspane);
			var c = e.getDragSourceContext();
			var idx = getTargetIndex(point);
			if (getTabAreaBounds().contains(point) && idx >= 0 && idx != index && idx != index + 1) {
				c.setCursor(DragSource.DefaultMoveDrop);
				glasspane.setCursor(DragSource.DefaultMoveDrop);
			} else {
				c.setCursor(DragSource.DefaultMoveNoDrop);
				glasspane.setCursor(DragSource.DefaultMoveNoDrop);
			}
		}

		@Override
		public void dragExit(DragSourceEvent e) {
			e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
			cursorRect.setRect(0, 0, 0, 0);
			glasspane.setPoint(new Point(-1000, -1000));
			glasspane.repaint();
		}

		@Override
		public void dragDropEnd(DragSourceDropEvent e) {
			cursorRect.setRect(0, 0, 0, 0);
			index = -1;
			glasspane.setVisible(false);
			glasspane.setImage(null);
		}

		private Rectangle getTabAreaBounds() {
			var ret = getBounds();
			var cmp = getSelectedComponent();
			var index = 0;
			while (cmp == null && index < getTabCount()) {
				cmp = getComponentAt(index++);
			}
			var cmprect = cmp == null ? new Rectangle() : cmp.getBounds();
			switch (getTabPlacement()) {
				case TOP:
					ret.height -= cmprect.height;
					break;
				case BOTTOM:
					ret.y += cmprect.y + cmprect.height;
					ret.height -= cmprect.height;
					break;
				case LEFT:
					ret.width -= cmprect.width;
					break;
				case RIGHT:
					ret.x += cmprect.x + cmprect.width;
					ret.width -= cmprect.width;
			}
			ret.grow(2, 2);
			return ret;
		}
	}

	private final class TabTransferable implements Transferable {
		final String NAME = LeafTabbedPane.class.getName();
		private final DataFlavor flavor;

		public TabTransferable() {
			flavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, NAME);
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[]{flavor};
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.getHumanPresentableName().equals(NAME);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) {
			return LeafTabbedPane.this.getTabComponentAt(index);
		}
	}

	private final class TabDragGestureListener implements DragGestureListener {
		@Override
		public void dragGestureRecognized(DragGestureEvent e) {
			if (getTabCount() <= 1) return;
			Component comp = e.getComponent().getParent();
			index = indexOfTabComponent(comp);
			if (!isEnabledAt(index)) return;
			initGlassPane(getTabComponentAt(index), e.getDragOrigin());
			try {
				e.startDrag(DragSource.DefaultMoveDrop, trans, dsl);
			} catch (InvalidDnDOperationException ex) {
				ex.printStackTrace();
			}
		}

		private void initGlassPane(Component cmp, Point point) {
			getRootPane().setGlassPane(glasspane);
			var image = new BufferedImage(cmp.getWidth(), cmp.getHeight(), BufferedImage.TYPE_INT_ARGB);
			var g = image.getGraphics();
			cmp.paint(g);
			glasspane.setImage(image);
			var glasspt = SwingUtilities.convertPoint(cmp, point, glasspane);
			glasspane.setPoint(glasspt);
			glasspane.setVisible(true);
		}
	}

	private final class TabTitleGhost extends JPanel {
		private static final long serialVersionUID = 1L;
		private final AlphaComposite comp;
		private Point location;
		private BufferedImage image;

		public TabTitleGhost() {
			setOpaque(false);
			location = new Point();
			comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
		}

		@Override
		public void paintComponent(Graphics g) {
			var g2 = (Graphics2D) g;
			g2.setComposite(comp);
			if (image != null) {
				var lx = location.getX();
				var ly = location.getY();
				var iw = image.getWidth(this);
				var ih = image.getHeight(this);
				var x = (int) (lx - iw / 2d);
				var y = (int) (ly - ih / 2d);
				g2.drawImage(image, x, y, null);
			}
			if (index >= 0) {
				g2.setPaint(Color.BLUE);
				g2.fill(cursorRect);
			}
		}

		public void setImage(BufferedImage image) {
			this.image = image;
		}

		public void setPoint(Point point) {
			this.location = point;
		}
	}

	private class TabSortInfo implements Comparable<TabSortInfo> {
		public final String title, tooltip;
		public final Component comp;
		public final Icon icon;

		public TabSortInfo(int index) {
			this.title = getTitleAt(index);
			this.tooltip = getToolTipTextAt(index);
			this.icon = getIconAt(index);
			this.comp = getComponentAt(index);
		}

		@Override
		public int compareTo(TabSortInfo to) {
			return title.compareTo(to.title);
		}

		@Override
		public int hashCode() {
			return title.hashCode() + comp.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TabSortInfo) {
				return (compareTo((TabSortInfo) obj) == 0);
			} else {
				return false;
			}
		}
	}

}
