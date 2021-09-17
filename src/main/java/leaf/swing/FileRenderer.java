/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * {@link LeafFileTree}のファイル表示に用いるレンダラーです。
 *
 * @author 無線部開発班
 * @since 2013/02/17
 */
final class FileRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	private final TreeCellRenderer renderer;
	private final FileSystemView view;

	/**
	 * LeafFileTreeとセルレンダラーを指定してレンダラーを構築します。
	 *
	 * @param tree ファイルツリー
	 * @param r    セルレンダラー
	 */
	public FileRenderer(LeafFileTree tree, TreeCellRenderer r) {
		this.renderer = r;
		this.view = tree.getFileSystemView();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
		var comp = (JLabel) renderer.getTreeCellRendererComponent(tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
		if (isSelected) {
			comp.setOpaque(false);
			comp.setForeground(getTextSelectionColor());
		} else {
			comp.setOpaque(true);
			comp.setForeground(getTextNonSelectionColor());
			comp.setBackground(getBackgroundNonSelectionColor());
		}
		if (value instanceof DefaultMutableTreeNode) {
			var node = (DefaultMutableTreeNode) value;
			var obj = node.getUserObject();
			if (obj instanceof File) {
				var file = (File) obj;
				comp.setIcon(view.getSystemIcon(file));
				comp.setText(view.getSystemDisplayName(file));
				comp.setToolTipText(file.getPath());
			}
		}
		return comp;
	}

}
