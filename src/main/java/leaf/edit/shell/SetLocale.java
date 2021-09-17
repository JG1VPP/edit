/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import javax.swing.*;

import leaf.edit.app.TsEditApp;
import leaf.edit.cmd.Command;
import leaf.edit.ui.MenuBar;
import leaf.edit.ui.PopupMenu;
import leaf.edit.ui.ToolBar;
import leaf.edit.ui.TextFileChooser;
import leaf.util.Properties;
import leaf.util.LocalizeManager;
import leaf.main.Shell;

/**
 * ダイアログを表示してロケールの設定を変更します。
 *
 * @author 無線部開発班
 */
public final class SetLocale extends Command {
	private static final Properties properties;

	static {
		properties = Properties.getInstance(SetLocale.class);
	}

	private final LocalizeManager localize;

	public SetLocale() { // after MainFrame initialized
		localize = LocalizeManager.get(Open.class);
	}

	private LocaleElem[] getAvailableLocales() {
		var list = new ArrayList<LocaleElem>();
		for (var l : Locale.getAvailableLocales()) {
			if (localize.available(l)) {
				list.add(new LocaleElem(l));
			}
		}
		return list.toArray(new LocaleElem[0]);
	}

	@Override
	public void process(Object... args) throws Exception {
		var locale = LocalizeManager.getLocale();
		var elems = getAvailableLocales();
		Arrays.sort(elems);
		var elem = (LocaleElem) JOptionPane.showInputDialog(TsEditApp.getMainFrame(), "Select locale...", "Locale", JOptionPane.QUESTION_MESSAGE, null, elems, new LocaleElem(locale));
		if (elem != null) updateLocale(elem.locale);
	}

	/**
	 * フレームワーク全体のロケールを更新します。
	 *
	 * @param locale 設定するロケール
	 *
	 * @throws Exception メニューバーの初期化で不正があった場合
	 */
	private void updateLocale(Locale locale) throws Exception {
		LocalizeManager.setLocale(locale);
		JComponent.setDefaultLocale(locale);
		Locale.setDefault(locale);
		properties.put("language", locale.getLanguage());
		properties.put("country", locale.getCountry());
		properties.put("variant", locale.getVariant());
		MenuBar.getInstance().initialize();
		ToolBar.getInstance().initialize();
		PopupMenu.getInstance().initialize();
		TsEditApp.getMainFrame().setLocale(locale);
		var chooser = TextFileChooser.getInstance();
		chooser.setLocale(locale);
		chooser.initialize();
		chooser.updateUI();
		Shell.getInstance().setLocale(locale);
	}

	/**
	 * フレームワーク全体のロケールを返します。
	 *
	 * @return ロケール
	 */
	public static Locale getLocale() {
		var language = properties.get("language", String.class, null);
		var country = properties.get("country", String.class, null);
		var variant = properties.get("variant", String.class, null);
		if (variant != null) return new Locale(language, country, variant);
		if (country != null) return new Locale(language, country);
		if (language != null) return new Locale(language);
		return Locale.ENGLISH;
	}

	private static class LocaleElem implements Comparable<LocaleElem> {
		public final String name;
		public final Locale locale;

		public LocaleElem(Locale locale) {
			this.name = locale.getDisplayName(Locale.ENGLISH);
			this.locale = locale;
		}

		@Override
		public int compareTo(LocaleElem another) {
			return name.compareTo(another.name);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof LocaleElem) {
				return ((LocaleElem) obj).locale.equals(locale);
			}
			return false;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
