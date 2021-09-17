/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import javax.swing.*;

/**
 * ブラウザの起動に関するユーティリティです。
 *
 * @author 無線部開発班
 * @since 2012/04/30
 */
public final class BrowseUtils {

	private static final LocalizeManager localize;
	private static Desktop desktop;

	static {
		localize = LocalizeManager.get(BrowseUtils.class);
	}

	/**
	 * 指定されたURIを閲覧するソフトウェアを起動します。
	 *
	 * @param uri 閲覧するURI
	 */
	public static void browse(URI uri) {
		if (desktop == null && Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
		}
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), localize.translate("browse"), JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, localize.translate("browse_not_supported"), localize.translate("browse"), JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
