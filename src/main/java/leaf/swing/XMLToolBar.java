/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.io.IOException;
import javax.swing.*;

import leaf.main.Shell;
import leaf.shell.MenuBuilder;
import leaf.shell.UnknownNameException;
import leaf.util.LocalizeManager;

/**
 * 構造をXMLファイルに記述させるメニューバーです。
 *
 * @author 無線部開発班
 * @since 2013/01/02
 */
public class XMLToolBar extends JToolBar {
	private final String dir;

	/**
	 * XMLを保管するディレクトリを指定してメニューバーを構築します。
	 *
	 * @param dir XMLファイルを保管するディレクトリ
	 */
	public XMLToolBar(String dir) {
		this.dir = dir;
	}

	/**
	 * ツールバーをリソースファイルから読み込んで構築します。
	 *
	 * @throws UnknownNameException リソースの記述に問題がある場合
	 */
	public void initialize() throws IOException, UnknownNameException {
		removeAll();
		final var iso3 = LocalizeManager.getLocale().getISO3Language();
		final var stream = getClass().getResourceAsStream(String.format("%s/%s.xml", dir, iso3));
		new MenuBuilder(Shell.getInstance()).build(this, stream);
	}
}
