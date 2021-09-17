/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.File;
import java.net.MalformedURLException;
import javax.swing.*;

import leaf.edit.cmd.Command;
import leaf.swing.PhotoGlass;

/**
 * 閲覧する画像をガラスレイヤー上に表示するコマンドです。
 *
 * @author 無線部開発班
 */
final class Picture extends Command {
	@Override
	public void process(Object... args) throws Exception {
		var chooser = getFileChooser();
		chooser.setSelectedFile(new File("image.png"));
		chooser.setCurrentDirectory(null);
		if (chooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
			view(chooser.getSelectedFile());
		}
	}

	/**
	 * 指定されたファイルから画像を読み込んで表示します。
	 *
	 * @param file
	 *
	 * @throws MalformedURLException ファイルのURLへの変換に失敗した場合
	 */
	protected static void view(File file) throws MalformedURLException {
		var frame = getFrame();
		frame.getGlassPane().setVisible(false);
		frame.setGlassPane(new PhotoGlass(file.toURI().toURL()));
		frame.getGlassPane().setVisible(true);
	}

}
