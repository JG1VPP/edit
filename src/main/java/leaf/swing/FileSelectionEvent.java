/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.io.File;
import java.util.EventObject;

/**
 * {@link LeafFileTree}のファイル選択イベントです。
 *
 * @author 無線部開発班
 * @since 2010年9月18日
 */
public class FileSelectionEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private final File file;

	/**
	 * イベントの発生源とファイルを指定してイベントを構築します。
	 *
	 * @param source イベントの発生源
	 * @param file   イベントに関連付けるファイル
	 */
	public FileSelectionEvent(Object source, File file) {
		super(source);
		this.file = file;
	}

	/**
	 * 関連付けられたファイルを返します。
	 *
	 * @return ファイル
	 */
	public File getFile() {
		return file;
	}

}
