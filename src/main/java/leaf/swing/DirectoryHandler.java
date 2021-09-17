/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.io.File;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * ディレクトリがクリックされた時に展開するハンドラーです。
 *
 * @author 無線部開発班
 * @since 2013/02/17
 */
final class DirectoryHandler implements TreeSelectionListener {
	private final DefaultTreeModel model;
	private final FileSystemView view;
	private final LeafFileTree tree;

	/**
	 * LeafFileTreeとTreeModelを指定してハンドラーを構築します。
	 *
	 * @param tree  ファイルツリー
	 * @param model ツリーのモデル
	 */
	public DirectoryHandler(LeafFileTree tree, FileSystemView view, TreeModel model) {
		this.tree = tree;
		this.view = tree.getFileSystemView();
		this.model = (DefaultTreeModel) model;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		var c = e.getPath().getLastPathComponent();
		var node = (DefaultMutableTreeNode) c;
		final var file = (File) node.getUserObject();
		if (node.isLeaf() && file.isDirectory()) {
			new ExpandWorker(node, file).execute();
		}
		tree.fireFileSelectionEvent(file);
	}

	private class ExpandWorker extends SwingWorker<String, File> {
		private final DefaultMutableTreeNode node;
		private final File parent;

		public ExpandWorker(DefaultMutableTreeNode node, File dir) {
			this.node = node;
			this.parent = dir;
		}

		@Override
		public String doInBackground() {
			var children = view.getFiles(parent, true);
			for (var child : children) {
				if (child.isDirectory() && !child.isHidden()) {
					publish(child);
				}
			}
			return "Done";
		}

		@Override
		protected void process(java.util.List<File> chunks) {
			for (var file : chunks) {
				node.add(new DefaultMutableTreeNode(file));
			}
			model.nodeStructureChanged(node);
		}
	}

}
