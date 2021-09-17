/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.os;

import javax.swing.*;

import leaf.util.Properties;

final class MacOSX extends OS {
	@Override
	protected void exit() {
		var prop = Properties.getInstance(getClass());
		prop.put("lnf", UIManager.getLookAndFeel().getClass().getCanonicalName());
	}

	@Override
	protected void startup() throws Exception {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "App");
		var prop = Properties.getInstance(getClass());
		var lnf = prop.get("lnf", OS.getNimbusLookAndFeelClassName());
		UIManager.setLookAndFeel(lnf);
	}
}
