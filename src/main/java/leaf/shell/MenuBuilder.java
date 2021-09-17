/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.shell;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import leaf.swing.LeafIcons;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * ビルド文書を解析するビルダーの実装です。
 *
 * @author 無線部開発班
 * @since 2011年12月11日
 */
public final class MenuBuilder {
	private final Shell shell;

	/**
	 * シェルを指定してビルダを構築します。
	 *
	 * @param shell 関連付けられるシェル
	 */
	public MenuBuilder(Shell shell) {
		this.shell = shell;
	}

	/**
	 * ビルド対象とビルド文書を読み込むストリームを指定して文書を解析します。
	 *
	 * @param menubar ビルド対象のメニューバー
	 * @param stream  ビルド文書を読み込むストリーム
	 *
	 * @throws IOException          読み込みに失敗した場合
	 * @throws UnknownNameException 規約違反時
	 */
	public void build(JMenuBar menubar, InputStream stream) throws IOException, UnknownNameException {
		new Builder(shell, menubar).build(stream);
	}

	/**
	 * ビルド対象とビルド文書を読み込むストリームを指定して文書を解析します。
	 *
	 * @param popup  ビルド対象のポップアップメニュー
	 * @param stream ビルド文書を読み込むストリーム
	 *
	 * @throws IOException          読み込みに失敗した場合
	 * @throws UnknownNameException 規約違反時
	 */
	public void build(JPopupMenu popup, InputStream stream) throws IOException, UnknownNameException {
		new Builder(shell, popup).build(stream);
	}

	/**
	 * ビルド対象とビルド文書を読み込むストリームを指定して文書を解析します。
	 *
	 * @param toolbar ビルド対象のツールバー
	 * @param stream  ビルド文書を読み込むストリーム
	 *
	 * @throws IOException          読み込みに失敗した場合
	 * @throws UnknownNameException 規約違反時
	 */
	public void build(JToolBar toolbar, InputStream stream) throws IOException, UnknownNameException {
		new Builder(shell, toolbar).build(stream);
	}

	/**
	 * JMenuの記述の開始イベントを処理します。
	 *
	 * @author 無線部開発班
	 * @since 2011年12月11日
	 */
	static final class MenuHandler extends ItemHandler {
		/**
		 * シェルを指定してハンドラーを構築します。
		 *
		 * @param shell 関連付けられるシェル
		 */
		public MenuHandler(Shell shell) {
			super(shell);
		}

		/**
		 * このハンドラーがデフォルトで生成するコンポーネントを指定します。
		 *
		 * @return JMenuのインスタンスを返す
		 */
		@Override
		protected AbstractButton createDefaultButton() {
			return new JMenu();
		}

		/**
		 * このハンドラーが処理する要素の名前を返します。
		 *
		 * @return menu
		 */
		@Override
		public QName name() {
			return new QName("menu");
		}
	}

	/**
	 * セパレータを追加する記述に対するマーカーとなります。
	 * 従ってこのハンドラーでは実質的な処理は行われません。
	 *
	 * @author 無線部開発班
	 * @since 2011年12月11日
	 */
	static final class SeparatorHandler extends ElementHandler {
		/**
		 * ハンドラーを構築します。
		 */
		public SeparatorHandler() {
		}

		/**
		 * スタックにプッシュするためのダミーを返します。
		 *
		 * @param e 受け取るイベント
		 *
		 * @return nullでないダミーコンポーネント
		 */
		@Override
		public JComponent handle(StartElement e) {
			return new JSeparator(); // dummy (not null)
		}

		/**
		 * このハンドラーが処理する要素の名前を返します。
		 *
		 * @return item
		 */
		@Override
		public QName name() {
			return new QName("separator");
		}
	}

	/**
	 * JMenuItemの記述の開始イベントを処理します。
	 *
	 * @author 無線部開発班
	 * @since 2011年12月11日
	 */
	static class ItemHandler extends AbstractButtonHandler {
		private final Shell shell;

		/**
		 * シェルを指定してハンドラーを構築します。
		 *
		 * @param shell 関連付けられるシェル
		 */
		public ItemHandler(Shell shell) {
			this.shell = shell;
			addHandler(new CommandHandler());
		}

		/**
		 * このハンドラーがデフォルトで生成するコンポーネントを指定します。
		 *
		 * @return JMenuItemのインスタンスを返す
		 */
		@Override
		protected AbstractButton createDefaultButton() {
			return new JMenuItem();
		}

		/**
		 * このハンドラーが処理する要素の名前を返します。
		 *
		 * @return menu
		 */
		@Override
		public QName name() {
			return new QName("item");
		}

		// "command"属性、つまり対応するコマンドを指定する属性です。
		private class CommandHandler extends AttributeHandler {
			@Override
			public JComponent handle(Attribute attr) throws UnknownNameException, ClassCastException {
				var cmd = shell.getCommand(attr.getValue());
				if (cmd instanceof MenuItemProvider) {
					var mip = (MenuItemProvider) cmd;
					return item = mip.createMenuItem((JMenuItem) item);
				} else throw new UnknownNameException(attr.getValue());
			}

			@Override
			public QName name() {
				return command;
			}
		}

	}

	/**
	 * 各種イベント処理クラスの基底となるクラスです。
	 *
	 * @author 無線部開発班
	 * @since 2011年12月11日
	 */
	abstract static class Handler<E extends XMLEvent> {
		/**
		 * イベントオブジェクトを受け取って処理を実行します。
		 *
		 * @param e 受け取るイベント
		 *
		 * @return 処理の結果生成されたコンポーネント
		 *
		 * @throws Exception この処理が発生しうる例外
		 */
		public abstract JComponent handle(E e) throws Exception;

		/**
		 * このハンドラが担当するイベントの名前を返します。
		 *
		 * @return イベント名
		 */
		public abstract QName name();
	}

	/**
	 * ビルド文書解析時に各種要素毎の処理を実行します。
	 *
	 * @author 無線部開発班
	 * @since 2011年12月11日
	 */
	abstract static class ElementHandler extends Handler<StartElement> {
		private Map<QName, AttributeHandler> handlers = null;

		/**
		 * このハンドラーに各属性に対応する隷下ハンドラーを追加します。
		 *
		 * @param handler 追加するハンドラー
		 */
		protected final void addHandler(AttributeHandler handler) {
			if (handlers == null) {
				handlers = new HashMap<>();
			}
			handlers.put(handler.name(), handler);
		}

		/**
		 * 指定されたコンポーネントにセパレータを追加します。
		 *
		 * @param comp コンポーネント
		 */
		public void addSeparator(JComponent comp) {
			if (comp instanceof JToolBar) {
				((JToolBar) comp).addSeparator();
			} else if (comp instanceof JMenu) {
				((JMenu) comp).addSeparator();
			} else if (comp instanceof JPopupMenu) {
				((JPopupMenu) comp).addSeparator();
			} else comp.add(new JSeparator()); // uncommon
		}

		/**
		 * 指定した属性名に対応する隷下属性ハンドラーを返します。
		 *
		 * @param name 解析する属性
		 *
		 * @return 対応する属性ハンドラー
		 */
		protected final AttributeHandler getHandler(QName name) {
			return (handlers == null) ? null : handlers.get(name);
		}
	}

	/**
	 * JButtonの記述の開始イベントを処理します。
	 *
	 * @author 無線部開発班
	 * @since 2011年12月11日
	 */
	static final class ButtonHandler extends AbstractButtonHandler {
		private final Shell shell;

		/**
		 * シェルを指定してハンドラーを構築します。
		 *
		 * @param shell 関連付けられるシェル
		 */
		public ButtonHandler(Shell shell) {
			this.shell = shell;
			addHandler(new CommandHandler());
		}

		/**
		 * このハンドラーがデフォルトで生成するコンポーネントを指定します。
		 *
		 * @return JButtonのインスタンスを返す
		 */
		@Override
		protected AbstractButton createDefaultButton() {
			var button = new JButton();
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setFocusable(false);
			button.setRequestFocusEnabled(false);
			return button;
		}

		/**
		 * このハンドラーが処理する要素の名前を返します。
		 *
		 * @return menu
		 */
		@Override
		public QName name() {
			return new QName("button");
		}

		// "command"属性、つまり対応するコマンドを指定する属性です。
		private class CommandHandler extends AttributeHandler {
			@Override
			public JComponent handle(Attribute attr) throws UnknownNameException, ClassCastException {
				var cmd = shell.getCommand(attr.getValue());
				if (cmd instanceof ButtonProvider) {
					var bp = (ButtonProvider) cmd;
					return item = bp.createButton((JButton) item);
				} else throw new UnknownNameException(attr.getValue());
			}

			@Override
			public QName name() {
				return command;
			}
		}
	}

	/**
	 * ビルド文書解析時に各種属性毎の処理を実行します。
	 *
	 * @author 無線部開発班
	 * @since 2011年12月11日
	 */
	abstract static class AttributeHandler extends Handler<Attribute> {
	}

	/**
	 * {@link AbstractButton}の記述の開始イベントを処理します。
	 *
	 * @author 無線部開発班
	 * @since 2012年3月28日
	 */
	abstract static class AbstractButtonHandler extends ElementHandler {
		protected final QName command = new QName("command");
		private final Map<String, ButtonGroup> groups;
		protected AbstractButton item;

		/**
		 * ハンドラーを構築します。
		 */
		public AbstractButtonHandler() {
			addHandler(new GroupHandler());
			addHandler(new TextHandler());
			addHandler(new IconHandler());
			addHandler(new LeafIconHandler());
			addHandler(new AccelHandler());
			addHandler(new MnemonicHandler());
			addHandler(new ToolTipHandler());
			groups = new HashMap<>();
			// 継承クラスは上に加えて「command」属性を処理する必要
		}

		/**
		 * このハンドラーがデフォルトで生成するコンポーネントを指定します。
		 *
		 * @return JMenuItemのインスタンスを返す
		 */
		protected abstract AbstractButton createDefaultButton();

		/**
		 * イベントオブジェクトを受け取って処理を実行します。
		 *
		 * @param e 受け取るイベント
		 *
		 * @return 処理の結果生成されたコンポーネント
		 *
		 * @throws Exception この処理が発生しうる例外
		 */
		@Override
		public JComponent handle(StartElement e) throws Exception {
			item = createDefaultButton();
			handle(e, command);
			final var iterator = e.getAttributes();
			while (iterator.hasNext()) {
				var name = iterator.next().getName();
				if (!name.equals(command)) handle(e, name);
			}
			JComponent ntem = item;
			item = null;
			return ntem;
		}

		/**
		 * このハンドラーが処理する要素の名前を返します。
		 *
		 * @return item
		 */
		@Override
		public QName name() {
			return new QName("item");
		}

		/**
		 * 指定した名前の属性に対して処理を実行します。
		 *
		 * @param e    イベント
		 * @param name 属性名
		 *
		 * @throws Exception この処理が発生しうる例外
		 */
		private void handle(StartElement e, QName name) throws Exception {
			var attr = e.getAttributeByName(name);
			if (attr != null) {
				var handler = getHandler(attr.getName());
				if (handler != null) handler.handle(attr);
				else throw new UnknownNameException(attr.getName());
			}
		}

		// "group"属性、つまりアイテムの排他的選択動作を指定する属性です。
		private class GroupHandler extends AttributeHandler {
			@Override
			public JComponent handle(Attribute attr) {
				var group = groups.get(attr.getValue());
				if (group == null) {
					groups.put(attr.getValue(), group = new ButtonGroup());
				}
				group.add(item);
				return item;
			}

			@Override
			public QName name() {
				return new QName("group");
			}
		}

		// "text"属性、つまりアイテムの表示テキストを指定する属性です。
		private class TextHandler extends AttributeHandler {
			@Override
			public JComponent handle(Attribute attr) {
				item.setText(attr.getValue());
				return item;
			}

			@Override
			public QName name() {
				return new QName("text");
			}
		}

		// "icon"属性、つまりアイテムの表示アイコンを指定する属性です。
		private class IconHandler extends AttributeHandler {
			@Override
			public JComponent handle(Attribute attr) {
				item.setIcon(new ImageIcon(attr.getValue()));
				return item;
			}

			@Override
			public QName name() {
				return new QName("icon");
			}
		}

		// "leaficon"属性、つまりアイテムの表示アイコンを指定する属性です。
		private class LeafIconHandler extends AttributeHandler {
			@Override
			public JComponent handle(Attribute attr) {
				item.setIcon(LeafIcons.getIcon(attr.getValue()));
				return item;
			}

			@Override
			public QName name() {
				return new QName("leaficon");
			}
		}

		// "accel"属性、つまりアイテムのキーアクセラレータを指定する属性です。
		private class AccelHandler extends AttributeHandler {
			@Override
			public JComponent handle(Attribute attr) throws ClassCastException {
				var as = KeyStroke.getKeyStroke(attr.getValue());
				((JMenuItem) item).setAccelerator(as);
				return item;
			}

			@Override
			public QName name() {
				return new QName("accel");
			}
		}

		// "mnemonic"属性、つまりアイテムのニーモニックキーを指定する属性です。
		private class MnemonicHandler extends AttributeHandler {
			@Override
			public JComponent handle(Attribute attr) {
				var mnemo = attr.getValue();
				var ms = KeyStroke.getKeyStroke(mnemo);
				item.setMnemonic(ms.getKeyCode());
				return item;
			}

			@Override
			public QName name() {
				return new QName("mnemonic");
			}
		}

		// "tooltip"属性、つまりアイテムのツールチップ文字列を指定する属性です。
		private class ToolTipHandler extends AttributeHandler {
			@Override
			public JComponent handle(Attribute attr) {
				item.setToolTipText(attr.getValue());
				return item;
			}

			@Override
			public QName name() {
				return new QName("tooltip");
			}
		}

	}

	/**
	 * ビルド文書を解析するビルダーの実装です。
	 *
	 * @author 無線部開発班
	 * @since 2011年12月11日
	 */
	static final class Builder {
		private final Deque<JComponent> stack;
		private final Shell shell;
		private final EventHandler endElementHandler;
		private final EventHandler startElementHandler;

		/**
		 * シェルとビルド対象を指定してビルダを構築します。
		 *
		 * @param shell  関連付けられるシェル
		 * @param target ビルド対象のコンテナ
		 */
		public Builder(Shell shell, JComponent target) {
			this.shell = shell; // first!
			stack = new ArrayDeque<>();
			endElementHandler = new EndElementHandler();
			startElementHandler = new StartElementHandler();
			stack.push(target);
		}

		/**
		 * ビルド文書を読み込むストリームを指定して文書を解析します。
		 *
		 * @param stream ビルド文書を読み込むストリーム
		 *
		 * @throws IOException          読み込みに失敗した場合
		 * @throws UnknownNameException 規約違反時
		 */
		public void build(InputStream stream) throws IOException, UnknownNameException {
			XMLEventReader reader = null;
			XMLInputFactory fact;
			try {
				fact = XMLInputFactory.newInstance();
				stream = new BufferedInputStream(stream);
				reader = fact.createXMLEventReader(stream);
				while (reader.hasNext()) {
					var e = reader.nextEvent();
					if (e.getEventType() == START_ELEMENT) {
						startElementHandler.handle(e);
					} else if (e.getEventType() == END_ELEMENT) {
						endElementHandler.handle(e);
					}
				}
			} catch (UnknownNameException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new IOException(ex);
			} finally {
				try {
					if (stream != null) stream.close();
					if (reader != null) reader.close();
				} catch (XMLStreamException ex) {
					throw new IOException(ex);
				}
			}
		}

		private class EndElementHandler extends EventHandler {
			@Override
			public void handle(XMLEvent e) {
				stack.pop();
			}
		}

		private class StartElementHandler extends EventHandler {
			public StartElementHandler() {
				addHandler(new ButtonHandler(shell));
				addHandler(new ItemHandler(shell));
				addHandler(new MenuHandler(shell));
				addHandler(new SeparatorHandler());
			}

			@Override
			public void handle(XMLEvent e) throws Exception {
				var qname = ((StartElement) e).getName();
				var handler = getHandler(qname);
				if (handler != null) {
					var item = handler.handle(e.asStartElement());
					if (handler instanceof SeparatorHandler) {
						handler.addSeparator(stack.peek());
					} else stack.peek().add(item);
					assert item != null;
					stack.push(item);
				} else if (!qname.toString().equals("build")) {
					throw new UnknownNameException(qname);
				}
			}
		}

		/**
		 * ビルド文書解析時の各種イベント毎の処理を最初に実行します。
		 *
		 * @author 無線部開発班
		 * @since 2011年12月11日
		 */
		private abstract static class EventHandler {
			private Map<QName, ElementHandler> handlers = null;

			/**
			 * このハンドラーに各要素に対応する隷下ハンドラーを追加します。
			 *
			 * @param handler 追加するハンドラー
			 */
			protected final void addHandler(ElementHandler handler) {
				if (handlers == null) {
					handlers = new HashMap<>();
				}
				handlers.put(handler.name(), handler);
			}

			/**
			 * 指定した要素名に対応する隷下属性ハンドラーを返します。
			 *
			 * @param name 解析する属性
			 *
			 * @return 対応する要素ハンドラー
			 */
			protected final ElementHandler getHandler(QName name) {
				return (handlers == null) ? null : handlers.get(name);
			}

			/**
			 * イベントオブジェクトを受け取って処理を実行します。
			 *
			 * @param e 受け取るイベント
			 *
			 * @throws Exception この処理が発生しうる例外
			 */
			public abstract void handle(XMLEvent e) throws Exception;
		}

	}
}
