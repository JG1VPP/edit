/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileFilter;

import leaf.util.Find;

/**
 * 拡張子リストを指定することによりファイルをフィルタリングします。
 *
 * @author 無線部開発班
 * @since 2012/04/25
 */
public class ListFileFilter extends FileFilter implements Cloneable {

	private String description;
	private List<String> fileNameExtensionList;
	private FileType fileType;

	/**
	 * 空のフィルタを構築します。
	 */
	public ListFileFilter() {
		description = null;
		fileNameExtensionList = null;
		fileType = FileType.TEXT;
	}

	/**
	 * 説明を指定してフィルタを構築します。
	 *
	 * @param description フィルタの説明
	 */
	public ListFileFilter(String description) {
		this.description = description;
		fileNameExtensionList = new ArrayList<>();
		fileType = FileType.TEXT;
	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) return true;
		return fileNameExtensionList.contains(Find.getSuffix(file));
	}

	/**
	 * このフィルタの説明を返します。
	 *
	 * @return フィルタの説明
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * このフィルタの説明を設定します。
	 *
	 * @param description フィルタの説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * このフィルタに拡張子を追加します。
	 *
	 * @param extension 拡張子
	 */
	public void addFileNameExtension(String extension) {
		fileNameExtensionList.add(extension);
	}

	/**
	 * このフィルタのディープコピーを返します。
	 *
	 * @return コピーされたフィルタ
	 */
	@Override
	public ListFileFilter clone() {
		var clone = new ListFileFilter();
		clone.description = this.description;
		clone.fileNameExtensionList = this.fileNameExtensionList;
		clone.fileType = this.fileType;
		return clone;
	}

	@Override
	public String toString() {
		return getDescription();
	}

	/**
	 * このフィルタの拡張子リストを返します。
	 *
	 * @return 拡張子のリスト
	 */
	public List<String> getFileNameExtensionList() {
		return fileNameExtensionList;
	}

	/**
	 * このフィルタの拡張子リストを設定します。
	 *
	 * @param list 拡張子のリスト
	 */
	public void setFileNameExtensionList(List<String> list) {
		this.fileNameExtensionList = list;
	}

	/**
	 * このフィルタのファイル種別を返します。
	 *
	 * @return ファイルの種別
	 */
	public FileType getFileType() {
		return fileType;
	}

	/**
	 * このフィルタのファイル種別を設定します。
	 *
	 * @param fileType ファイルの種別
	 */
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	/**
	 * このフィルタから拡張子を削除します。
	 *
	 * @param extension 拡張子
	 */
	public void removeFileNameExtension(String extension) {
		fileNameExtensionList.remove(extension);
	}
}
