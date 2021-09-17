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
import javax.swing.tree.DefaultTreeModel;

/**
 * ディレクトリ構造をツリー表示するコンポーネントです。
 *
 * @author 無線部開発班
 * @since 2011年7月9日
 */
public class LeafFileTree extends JComponent {
	private static final long serialVersionUID = 1L;
	private final FileSystemView view;
	private final DefaultMutableTreeNode root;

	/**
	 * ファイルツリーを構築します。
	 */
	public LeafFileTree() {
		setLayout(new BorderLayout());
		view = FileSystemView.getFileSystemView();
		root = new DefaultMutableTreeNode();
		var model = new DefaultTreeModel(root);
		var tree = new JTree(model);
		var handler = new DirectoryHandler(this, view, model);
		var renderer = tree.getCellRenderer();
		renderer = new FileRenderer(this, renderer);
		tree.setRootVisible(false);
		tree.setCellRenderer(renderer);
		tree.addTreeSelectionListener(handler);
		add(tree, BorderLayout.CENTER);
		update();
	}

	/**
	 * FileSelectionListenerを追加します。
	 *
	 * @param l 追加するリスナー
	 */
	public void addFileSelectionListener(FileSelectionListener l) {
		listenerList.add(FileSelectionListener.class, l);
	}

	private void addRootDirectory(File rootdir) {
		var node = new DefaultMutableTreeNode(rootdir);
		root.add(node);
		for (var file : view.getFiles(rootdir, true)) {
			if (file.isDirectory()) {
				node.add(new DefaultMutableTreeNode(file));
			}
		}
	}

	/**
	 * 指定されたファイルが選択されたことをリスナーに通知します。
	 *
	 * @param file 選択されたファイル
	 */
	protected final void fireFileSelectionEvent(File file) {
		var e = new FileSelectionEvent(this, file);
		for (var l : listenerList.getListeners(FileSelectionListener.class)) {
			l.fileSelected(e);
		}
	}

	/**
	 * ファイル一覧を取得するために使用するFileSystemViewを返します。
	 *
	 * @return FileSystemView
	 */
	public FileSystemView getFileSystemView() {
		return view;
	}

	/**
	 * FileSelectionListenerを削除します。
	 *
	 * @param l 削除するリスナー
	 */
	public void removeFileSelecitonListener(FileSelectionListener l) {
		listenerList.remove(FileSelectionListener.class, l);
	}

	/**
	 * ディレクトリの表示を最新のものに更新します。
	 */
	public void update() {
		root.removeAllChildren();
		for (var rootdir : view.getRoots()) {
			addRootDirectory(rootdir);
		}
	}

}
