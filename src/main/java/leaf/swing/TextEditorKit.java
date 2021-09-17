/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import javax.swing.text.*;

/**
 * 水平タブや改行を可視化するエディタキットです。
 *
 * @author 無線部開発班
 * @since 2012年12月28日
 */
public class TextEditorKit extends StyledEditorKit {
	private static final long serialVersionUID = 1L;

	/**
	 * エディタキットを構築します。
	 */
	public TextEditorKit() {
		super();
	}

	/**
	 * ビューを作成するファクトリを返します。
	 *
	 * @return ビューのファクトリ
	 */
	@Override
	public ViewFactory getViewFactory() {
		return new LeafViewFactory();
	}

	private static final class LeafViewFactory implements ViewFactory {
		@Override
		public View create(Element elem) {
			var kind = elem.getName();
			if (kind != null) {
				switch (kind) {
					case AbstractDocument.ContentElementName:
						return new TabCharacterView(elem);
					case AbstractDocument.ParagraphElementName:
						return new LineSeparatorView(elem);
					case AbstractDocument.SectionElementName:
						return new BoxView(elem, View.Y_AXIS);
					case StyleConstants.ComponentElementName:
						return new ComponentView(elem);
					case StyleConstants.IconElementName:
						return new IconView(elem);
				}
			}
			return new TabCharacterView(elem);
		}
	}

}
