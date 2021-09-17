/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.util.ArrayList;
import java.util.List;

import leaf.edit.cmd.Command;
import leaf.edit.ui.FileFilterDialog;
import leaf.edit.ui.ListFileFilter;
import leaf.swing.LeafDialog;
import leaf.util.Properties;

/**
 * テキストエディタで用いる拡張子を設定するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SetFileExtensions extends Command {
	private static List<ListFileFilter> filters;

	private static Properties properties;

	@SuppressWarnings("unchecked")
	public SetFileExtensions() {
		properties = Properties.getInstance(getClass());
		filters = new ArrayList<>();
		filters = properties.get("list", List.class, filters);
	}

	@Override
	public void process(Object... args) {
		List<ListFileFilter> list = new ArrayList<>();
		for (var filter : filters) {
			list.add(filter.clone());
		}
		var dialog = new FileFilterDialog(getFrame(), list);
		if (dialog.showDialog() == LeafDialog.OK_OPTION) {
			properties.put("list", filters = list);
			getFileChooser().initialize();
		}
	}

	/**
	 * {@link TextFileChooser}で用いるフィルタのリストを返します。
	 *
	 * @return フィルタのリスト
	 */
	public static List<ListFileFilter> getFileFilters() {
		return filters;
	}

}
