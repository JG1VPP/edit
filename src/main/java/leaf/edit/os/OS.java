/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.os;

import javax.swing.*;

/**
 * 起動時にOS毎のネイティブ設定を適用するための基底クラスです。
 *
 * @since 2011年12月12日
 */
public abstract class OS {

	/**
	 * 終了時動作を実行します。
	 */
	protected abstract void exit();

	/**
	 * 初期設定動作を実行します。
	 *
	 * @throws Exception 何らかの例外
	 */
	protected abstract void startup() throws Exception;

	/**
	 * 指定されたOS向けの実装を呼び出して終了時動作を実行します。
	 *
	 * @param name OS名
	 */
	public static void exit(String name) {
		final var pack = OS.class.getPackage().getName();
		name = pack.concat(".").concat(name.replaceAll(" ", ""));
		try {
			((OS) Class.forName(name).newInstance()).exit();
		} catch (Exception e) {
		}
	}

	/**
	 * Nimbusルックアンドフィールのクラス名を返します。
	 *
	 * @return Nimbusがインストールされていない場合null
	 */
	public static String getNimbusLookAndFeelClassName() {
		for (var info : UIManager.getInstalledLookAndFeels()) {
			if (info.getName().equals("Nimbus")) return info.getClassName();
		}
		return null;
	}

	/**
	 * 指定されたOS向けの実装を呼び出して初期設定動作を実行します。
	 *
	 * @param name OS名
	 */
	public static void startup(String name) {
		final var pack = OS.class.getPackage().getName();
		name = pack.concat(".").concat(name.replaceAll(" ", ""));
		try {
			((OS) Class.forName(name).newInstance()).startup();
		} catch (Exception e) {
		}
	}

}
