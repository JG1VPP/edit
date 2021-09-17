/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.main;

import java.lang.reflect.InvocationTargetException;

import leaf.swing.MainFrame;

/**
 * フレームワークがアプリケーションを起動する際に必ず使用されます。
 *
 * @author 無線部開発班
 * @since 2013/01/01
 */
public final class Launcher<A extends Application> {
	private final MainFrame frame;
	private final Shell shell;
	private final Class<? extends Application> appclass;

	/**
	 * アプリケーションのクラスを指定してランチャーを構築します。
	 *
	 * @param appclass 起動するクラス
	 */
	public Launcher(Class<A> appclass) {
		this.appclass = appclass;
		this.frame = MainFrame.getInstance();
		this.shell = Shell.getInstance();
	}

	/**
	 * アプリケーションを起動します。起動に失敗するとnullを返します。
	 *
	 * @return 起動済みアプリケーション
	 */
	public final A launch() {
		try {
			var method = appclass.getMethod("newInstance", MainFrame.class);
			@SuppressWarnings("unchecked") var app = (A) method.invoke(null, frame);
			app.installCommands(shell);
			frame.setJMenuBar(app.createMenuBar());
			frame.setJToolBar(app.createToolBar());
			app.installFinished();
			return app;
		} catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | SecurityException | ClassCastException | NoSuchMethodException ex) {
		}
		return null;
	}

}
