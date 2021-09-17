/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.app;

import leaf.util.Properties;
import leaf.main.Application;
import leaf.main.Launcher;
import leaf.plugin.ModuleManager;
import leaf.edit.os.OS;
import leaf.swing.MainFrame;

/**
 * アプリケーションを動作させる前に必ずこのクラスで起動してください。
 *
 * @author 無線部開発班
 * @since 2013/01/03
 */
public final class Framework {

	/**
	 * 全てのモジュールを終了してからフレームワークとJava実行環境を終了します。
	 *
	 * @param status 終了ステータスコード
	 */
	public static void shutdown(int status) {
		var frame = MainFrame.getInstance();
		if (!ModuleManager.getInstance().shutdownAllModules()) return;
		new WindowBounds(frame, WindowBounds.MAIN_FRAME).saveBounds();
		frame.getStatusBar().saveNewsBarURL();
		OS.exit(System.getProperty("os.name"));
		Properties.save();
		System.exit(status);
	}

	/**
	 * 起動前のアプリケーションのクラスを指定してフレームワークを起動します。
	 *
	 * @param appclass 起動するアプリケーションのクラス
	 *
	 * @throws Exception 起動に失敗した場合
	 */
	public static void startup(Class<? extends Application> appclass) {
		OS.startup(System.getProperty("os.name"));
		var frame = MainFrame.getInstance();
		new WindowBounds(frame, WindowBounds.MAIN_FRAME).applyBounds();
		var launcher = new Launcher(appclass);
		launcher.launch();
		frame.setVisible(true);
		ModuleManager.getInstance().loadAllModules();
	}

}
