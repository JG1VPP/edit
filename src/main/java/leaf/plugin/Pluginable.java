/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.plugin;

import java.awt.*;
import javax.swing.*;

/**
 * プラグイン可能なモジュールが実装するインターフェースです。
 *
 * @author 無線部開発班
 * @since 2012/12/28
 */
public interface Pluginable extends Removable {
	/**
	 * このプラグインの設定管理画面を構築します。
	 *
	 * @param owner 管理画面の所有者
	 *
	 * @return 生成された管理画面
	 */
	JDialog createConfigurationDialog(Window owner);

	/**
	 * プラグイン固有の名前を返します。
	 *
	 * @return プラグインの名前
	 */
	String getName();

	/**
	 * このプラグインが設定管理画面を提供するか返します。
	 *
	 * @return 設定管理画面を持つ場合true 持たない場合false
	 */
	boolean hasConfigurationDialog();

}
