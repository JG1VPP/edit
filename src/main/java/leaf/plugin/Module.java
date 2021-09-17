/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.plugin;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import javax.swing.*;

/**
 * フレームワーク上で動作するモジュールの基底クラスです。
 *
 * @author 無線部開発班
 * @since 2012/06/16
 */
public abstract class Module implements Pluginable {
	/**
	 * このメソッドはデフォルトでnullを返します。
	 *
	 * @param p 所有者となるウィンドウ
	 *
	 * @return 設定ダイアログ
	 */
	@Override
	public JDialog createConfigurationDialog(Window p) {
		return null;
	}

	/**
	 * このメソッドはデフォルトでfalseを返します。
	 *
	 * @return 設定ダイアログを提供する場合true
	 */
	@Override
	public boolean hasConfigurationDialog() {
		return false;
	}

	/**
	 * 指定されたモジュールの名前を取得して返します。
	 *
	 * @param module モジュール
	 *
	 * @return モジュールの名前
	 */
	public static final String getName(Module module) {
		Class<?> type = module.getClass();
		if (!Modifier.isAbstract(type.getModifiers())) {
			try {
				var method = type.getMethod("getName");
				return (String) method.invoke(module);
			} catch (SecurityException | InvocationTargetException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException ex) {
			}
		}
		return null;
	}

	/**
	 * {@link ModuleLoader}がインスタンス化に使用します。
	 *
	 * @return モジュールのインスタンス
	 */
	public static Module newInstance() {
		StackTraceElement ste;
		try {
			throw new IllegalAccessException();
		} catch (IllegalAccessException ex) {
			ste = ex.getStackTrace()[0];
			var method = ste.getMethodName();
			System.err.print("static method '");
			System.err.print(method);
			System.err.println("' not implemented");
		}
		return null;
	}

}
