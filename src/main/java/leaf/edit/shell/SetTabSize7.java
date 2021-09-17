/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

public final class SetTabSize7 extends SetTabSize {
	public SetTabSize7() {
		super(7);
	}

	@Override
	public void process(Object... args) {
		setTabSize(7);
	}
}
