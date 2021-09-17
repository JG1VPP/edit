/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

/**
 * エディタのリストに対する操作を抽象化します。
 *
 * @author 無線部開発班
 */
public abstract class EditorListTask<E> {

	private final Class<E> type;

	/**
	 * タスクを実行する対象となるエディタのクラスを指定します。
	 *
	 * @param type エディタのクラス
	 */
	public EditorListTask(Class<E> type) {
		this.type = type;
	}

	/**
	 * 指定されたエディタに対してタスクを実行します。
	 *
	 * @param editor 対象のエディタ
	 *
	 * @return タスクを終了する場合true
	 */
	public abstract boolean process(E editor);

	/**
	 * 最後尾のエディタからタスクを開始します。
	 *
	 * @return このタスクが中断された場合true
	 */
	@SuppressWarnings("unchecked")
	public final boolean start() {
		var tab = TextEditorUtils.getTabbedPane();
		final var tabCount = tab.getTabCount();
		// to avoid IndexOutOfBoundsException
		for (var i = tabCount - 1; i >= 0; i--) {
			var comp = tab.getComponentAt(i);
			if (type.isInstance(comp)) {
				if (process((E) comp)) return true;
			}
		}
		return false;
	}
}
