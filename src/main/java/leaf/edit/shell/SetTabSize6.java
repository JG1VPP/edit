/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

public final class SetTabSize6 extends SetTabSize {
	public SetTabSize6() {
		super(6);
	}

	@Override
	public void process(Object... args) {
		setTabSize(6);
	}
}
