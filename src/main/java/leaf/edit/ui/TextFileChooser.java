/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import leaf.edit.shell.SetFileExtensions;
import leaf.swing.LeafFileTree;
import leaf.util.LocalizeManager;

/**
 * テキストエディタで用いられるファイル選択コンポーネントです。
 *
 * @author 無線部開発班
 * @since 2012/03/27
 */
public final class TextFileChooser extends JFileChooser {

	private static TextFileChooser instance;

	private final LocalizeManager localize;

	private JComboBox<Charset> comb;

	private TextFileChooser() {
		localize = LocalizeManager.get(getClass());
		initialize();
	}

	private JPanel createCharsetComboBoxPanel() {
		var subpanel = new JPanel(new BorderLayout());
		var label = new JLabel(localize.translate("label_charset"), JLabel.CENTER);
		subpanel.add(label, BorderLayout.WEST);
		comb = new JComboBox<>(TextEditorUtils.getCharsets());
		subpanel.add(comb, BorderLayout.CENTER);
		comb.setEditable(false);
		return subpanel;
	}

	@Override
	protected JDialog createDialog(Component parent) throws HeadlessException {
		var dialog = super.createDialog(parent);
		dialog.pack();
		dialog.setResizable(false);
		return dialog;
	}

	private LeafFileTree createDirectoryTree() {
		var dirtree = new LeafFileTree();
		dirtree.setPreferredSize(new Dimension(200, 20));
		dirtree.addFileSelectionListener(e -> setCurrentDirectory(e.getFile()));
		return dirtree;
	}

	/**
	 * ファイルに対して適切なファイルの種別を選択します。
	 *
	 * @param file ファイル
	 *
	 * @return ファイル種別
	 */
	public FileType getFileType(File file) {
		for (var ff : getChoosableFileFilters()) {
			if (ff.accept(file) && ff instanceof ListFileFilter) {
				return ((ListFileFilter) ff).getFileType();
			}
		}
		return FileType.BINARY;
	}

	/**
	 * 現在選択されている文字セットを取得します。
	 *
	 * @return 文字セット
	 */
	public Charset getSelectedCharset() {
		return (Charset) comb.getSelectedItem();
	}

	/**
	 * ファイル選択コンポーネントを初期化します。
	 */
	public void initialize() {
		var mainpanel = new JPanel(new BorderLayout());
		mainpanel.add(createDirectoryTree(), BorderLayout.CENTER);
		mainpanel.add(createCharsetComboBoxPanel(), BorderLayout.SOUTH);
		setAccessory(mainpanel);
		resetChoosableFileFilters();
		for (FileFilter filter : SetFileExtensions.getFileFilters()) {
			addChoosableFileFilter(filter);
		}
	}

	/**
	 * ファイル選択コンポーネントのインスタンスを返します。
	 *
	 * @return インスタンス
	 */
	public static TextFileChooser getInstance() {
		if (instance == null) instance = new TextFileChooser();
		return instance;
	}
}
