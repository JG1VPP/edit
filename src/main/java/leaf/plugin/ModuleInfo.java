/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.plugin;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * モジュールの管理情報を表現するクラスです。
 *
 * @author 無線部開発班
 * @since 2012/06/16
 */
public final class ModuleInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	static {
		ModuleMap.setTransient("module");
	}

	private String location, name;
	private transient Module module;

	/**
	 * 空の管理情報を構築します。
	 */
	public ModuleInfo() {
		location = null;
		name = null;
	}

	/**
	 * モジュールの位置を指定して管理情報を構築します。
	 *
	 * @param url  モジュールの位置
	 * @param name モジュールの名前
	 */
	public ModuleInfo(URL url, String name) {
		location = url.toString();
		this.name = name;
	}

	/**
	 * モジュールの位置を指定して管理情報を構築します。
	 *
	 * @param location モジュールの位置
	 * @param name     モジュールの名前
	 */
	public ModuleInfo(String location, String name) {
		this.location = location;
		this.name = name;
	}

	/**
	 * モジュールの本体が保存されている場所を返します。
	 *
	 * @return モジュールの保存場所
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * モジュールの本体が保存されている場所を指定します。
	 *
	 * @param url モジュールの保存場所
	 */
	public void setLocation(String url) {
		this.location = url;
	}

	/**
	 * モジュールの本体が保存されている場所をURLで指定します。
	 *
	 * @param url モジュールの保存場所
	 */
	public void setLocation(URL url) {
		setLocation(url != null ? url.toExternalForm() : null);
	}

	/**
	 * モジュールの本体が保存されている場所をURLで返します。
	 *
	 * @return モジュールの保存場所
	 */
	public URL getLocationURL() {
		try {
			return new URL(location);
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/**
	 * モジュールのインスタンスを返します。
	 *
	 * @return モジュール
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * モジュールのインスタンスを設定します。
	 *
	 * @param module モジュール
	 */
	public void setModule(Module module) {
		this.module = module;
	}

	/**
	 * モジュールの名前を返します。
	 *
	 * @return 名前
	 */
	public String getName() {
		return name;
	}

	/**
	 * モジュールの名前を設定します。
	 *
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * モジュールの文字列による表現を返します。
	 *
	 * @return モジュールの名前
	 */
	@Override
	public String toString() {
		return name;
	}

}
