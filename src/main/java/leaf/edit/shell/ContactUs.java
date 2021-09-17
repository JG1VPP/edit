/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;

import leaf.edit.cmd.Command;
import leaf.swing.MainFrame;
/**
 * 開発者にメール送信するコマンドです。
 */

/**
 * 開発者にメールを送信するコマンドです。
 *
 * @author 無線部開発班
 */
public final class ContactUs extends Command {
	private static Desktop desktop = null;

	@Override
	public void process(Object... args) {
		try {
			mail(new URI("mailto:nextzlog@gmail.com"));
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		}
	}

	public static void mail(URI uri) {
		if (desktop == null && Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
		}
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.mail(uri);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Mail", JOptionPane.INFORMATION_MESSAGE);
			}
		} else
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "Mail not supported by system", "Mail", JOptionPane.INFORMATION_MESSAGE);
	}
}
