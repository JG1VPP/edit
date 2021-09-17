/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static javax.xml.XMLConstants.DEFAULT_NS_PREFIX;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * 小型軽量なRSS/Atomフィードリーダーの実装です。
 *
 * @author 無線部開発班
 * @since 2011年5月2日
 */
public class NewsReader {
	private static final Map<String, FeedParser> mimes;

	static {
		mimes = new HashMap<>();
		addFeedParser(new AtomParser());
		addFeedParser(new RssParser());
	}

	private final URL url;
	private FeedParser parser;

	/**
	 * URLを指定してフィードリーダーを生成します。
	 *
	 * @param url 読み込み先のURL
	 */
	public NewsReader(URL url) {
		this.url = url;
	}

	/**
	 * 指定したMIME type でフィードパーサーを検索します。
	 *
	 * @param mime フィード形式を指定するMIME type
	 *
	 * @return 対応するフィードパーサー
	 *
	 * @throws IOException            自動識別に失敗した場合
	 * @throws UnknownFormatException 未対応の形式の場合
	 */
	private FeedParser getFeedParser(String mime) throws IOException, UnknownFormatException {
		var parser = mimes.get(mime);
		if (parser != null) return parser;
		parser = mimes.get(new Detector().detect());
		if (parser != null) return parser;
		throw new UnknownFormatException(mime);
	}

	/**
	 * フィードの読み込み先となるURLを返します。
	 *
	 * @return フィードを読み込むURL
	 */
	public URL getURL() {
		return url;
	}

	/**
	 * 指定したMIME type でフィードを取得して返します。
	 *
	 * @param mime フィード形式を指定するMIME type
	 *
	 * @return フィード
	 *
	 * @throws IOException            読み込みに失敗した場合
	 * @throws UnknownFormatException 未対応の形式の場合
	 */
	public NewsFeed read(final String mime) throws IOException, UnknownFormatException {
		return getFeedParser(mime).parse(url);
	}

	/**
	 * フィードを取得して返します。
	 *
	 * @return フィード
	 *
	 * @throws IOException            読み込みに失敗した場合
	 * @throws UnknownFormatException 未対応の形式の場合
	 */
	public NewsFeed read() throws IOException, UnknownFormatException {
		if (parser != null) return parser.parse(url);
		var conn = url.openConnection();
		var mime = conn.getContentType();
		if (mime != null) {
			var range = mime.indexOf(';');
			if (range >= 0) mime = mime.substring(0, range);
		}
		parser = getFeedParser(mime);
		return parser.parse(conn.getInputStream());
	}

	/**
	 * フィードパーサーを登録します。
	 *
	 * @param parser 追加するパーサー
	 */
	private static void addFeedParser(FeedParser parser) {
		for (var mime : parser.getMimeTypes()) {
			mimes.put(mime, parser);
		}
	}

	/**
	 * 標準化されていないMIMEtypeに対する最後の砦です。
	 */
	private class Detector extends DefaultHandler {
		private String mime;

		/**
		 * フィードを解析してMIMEタイプを返します。
		 *
		 * @return 書式を指定する登録済みMIMEタイプ
		 *
		 * @throws IOException 読み込みに失敗した場合
		 */
		public String detect() throws IOException {
			SAXParserFactory fact;
			try (var stream = url.openStream()) {
				fact = SAXParserFactory.newInstance();
				fact.newSAXParser().parse(stream, this);
				return mime;
			} catch (ParserConfigurationException ex) {
				throw new IOException(ex);
			} catch (SAXException ex) {
				return mime;
			}
		}

		@Override
		public void startElement(String uri, String local, String qname, Attributes attr) throws SAXException {
			if (qname.equalsIgnoreCase("feed")) {
				mime = "application/atom+xml";
			} else if (qname.equalsIgnoreCase("rss")) {
				mime = "application/rss+xml";
			}
			throw new SAXException(); // jump to catch
		}
	}

	/**
	 * フィードパーサーの基底実装です。
	 *
	 * @author 無線部開発班
	 * @since 2011年7月4日
	 */
	abstract static class FeedParser {
		private final Map<Integer, XMLHandler> handlers;

		/**
		 * パーサーを生成します。
		 */
		public FeedParser() {
			handlers = new HashMap<>();
		}

		/**
		 * このパーサーにイベントハンドラーを登録します。
		 *
		 * @param type    関連付けるイベントの種類
		 * @param handler イベントハンドラー
		 */
		protected final void addHandler(int type, XMLHandler handler) {
			handlers.put(type, handler);
		}

		/**
		 * このパーサーに関連付けられるMIMEtypeのリストを返します。
		 *
		 * @return MIMEtypeのリスト
		 */
		public abstract List<String> getMimeTypes();

		/**
		 * 読み込み先URLを指定してフィードを取得します。
		 *
		 * @param url フィードを読み込むURL
		 *
		 * @return フィード
		 *
		 * @throws IOException 読み込みに失敗した場合
		 */
		public final NewsFeed parse(URL url) throws IOException {
			return parse(url.openStream());
		}

		/**
		 * 読み込み先ストリームを指定してフィードを取得します。
		 *
		 * @param stream フィードを読み込むストリーム
		 *
		 * @return フィード
		 *
		 * @throws IOException 読み込みに失敗した場合
		 */
		public NewsFeed parse(InputStream stream) throws IOException {
			XMLEventReader reader = null;
			XMLInputFactory fact;
			try {
				fact = XMLInputFactory.newInstance();
				stream = new BufferedInputStream(stream);
				reader = fact.createXMLEventReader(stream);
				while (reader.hasNext()) {
					var e = reader.nextEvent();
					final var type = e.getEventType();
					var handler = handlers.get(type);
					if (handler != null) handler.handle(e);
				}
				return null;
			} catch (FactoryConfigurationError | Exception ex) {
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

		/**
		 * 各種タグ要素に対応した処理を実行します。
		 */
		public interface TagHandler {
			void handle(XMLEvent e) throws Exception;

			String tag();
		}

		/**
		 * 各種イベント毎に処理を実行します。
		 */
		public abstract static class XMLHandler {
			private Map<String, TagHandler> handlers = null;

			public final void addHandler(TagHandler handler) {
				if (handlers == null) {
					handlers = new HashMap<>();
				}
				handlers.put(handler.tag().toLowerCase(), handler);
			}

			public final TagHandler getHandler(String tag) {
				if (handlers == null) return null;
				return handlers.get(tag.toLowerCase());
			}

			public abstract void handle(XMLEvent e) throws Exception;

			public final boolean isSupported(String tag) {
				if (handlers == null) return false;
				return handlers.get(tag.toLowerCase()) != null;
			}
		}

	}

	/**
	 * 簡易的なRSSパーサーの実装です。
	 *
	 * @author 無線部開発班
	 * @since 2011年5月2日
	 */
	static final class RssParser extends FeedParser {
		private final DateFormat format;
		private final EndElementHandler endElementHandler;
		private NewsFeed feed;
		private NewsItem item;
		private String value;
		private int nest = 0;

		/**
		 * フィード解析器を生成します。
		 */
		public RssParser() {
			format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
			var startElementHandler = new StartElementHandler();
			endElementHandler = new EndElementHandler();
			addHandler(END_ELEMENT, endElementHandler);
			addHandler(START_ELEMENT, startElementHandler);
			addHandler(CHARACTERS, new CharactersHandler());
		}

		/**
		 * このパーサーに関連付けられるMIME typeのリストを返します。
		 *
		 * @return MIME type のリスト
		 */
		@Override
		public List<String> getMimeTypes() {
			return List.of("application/rss+xml");
		}

		/**
		 * 読み込み先ストリームを指定してフィードを取得します。
		 *
		 * @param stream フィードを読み込むストリーム
		 *
		 * @return フィード
		 *
		 * @throws IOException 読み込みに失敗した場合
		 */
		@Override
		public NewsFeed parse(InputStream stream) throws IOException {
			try {
				super.parse(stream);
				return feed;
			} finally {
				feed = null;
			}
		}

		/**
		 * 文字列を読み込みます。
		 */
		private class CharactersHandler extends XMLHandler {
			private final StringBuilder sb = new StringBuilder();

			@Override
			public void handle(XMLEvent e) {
				if (nest != 0) return;
				if (value == null) sb.setLength(0);
				var data = ((Characters) e).getData();
				final var length = data.length();
				for (var i = 0; i < length; i++) {
					var ch = data.charAt(i);
					if (Character.getType(ch) != Character.LINE_SEPARATOR) {
						sb.append(ch);
					}
				}
				value = sb.toString();
			}
		}

		/**
		 * 要素の開始通知を処理します。
		 */
		private class StartElementHandler extends XMLHandler {
			public StartElementHandler() {
				addHandler(new StartElementHandler.ChannelHandler());
				addHandler(new StartElementHandler.ItemHandler());
			}

			@Override
			public void handle(XMLEvent e) throws Exception {
				value = null;
				var qname = ((StartElement) e).getName();
				var local = qname.getLocalPart();
				var prefix = qname.getPrefix();
				if (prefix.equals(DEFAULT_NS_PREFIX)) {
					var handler = getHandler(local);
					if (handler != null) handler.handle(e);
					else if (!endElementHandler.isSupported(local)) {
						if (feed != null) nest++;
					}
				} else if (feed != null) nest++;
			}

			// <channel>
			private class ChannelHandler implements TagHandler {
				public void handle(XMLEvent e) {
					feed = new NewsFeed();
				}

				public String tag() {
					return "channel";
				}
			}

			// <item>
			private class ItemHandler implements TagHandler {
				public void handle(XMLEvent e) {
					if (feed != null) {
						feed.addItem(item = new NewsItem());
					}
				}

				public String tag() {
					return "item";
				}
			}
		}

		/**
		 * 要素の終了通知を処理します。
		 */
		private class EndElementHandler extends XMLHandler {
			public EndElementHandler() {
				addHandler(new EndElementHandler.ItemHandler());
				addHandler(new EndElementHandler.LinkHandler());
				addHandler(new EndElementHandler.TitleHandler());
				addHandler(new EndElementHandler.PubDateHandler());
				addHandler(new EndElementHandler.GeneratorHandler());
				addHandler(new EndElementHandler.CopyrightHandler());
				addHandler(new EndElementHandler.LanguageHandler());
				addHandler(new EndElementHandler.CategoryHandler());
				addHandler(new EndElementHandler.DescriptionHandler());
			}

			@Override
			public void handle(XMLEvent e) throws Exception {
				if (feed == null) return;
				var qname = ((EndElement) e).getName();
				var local = qname.getLocalPart();
				if (nest != 0) nest--;
				else {
					var handler = getHandler(local);
					if (handler != null) handler.handle(e);
				}
				value = null;
			}

			// </item>
			private class ItemHandler implements TagHandler {
				public void handle(XMLEvent e) {
					item = null;
				}

				public String tag() {
					return "item";
				}
			}

			// </link>
			private class LinkHandler implements TagHandler {
				public void handle(XMLEvent e) throws MalformedURLException {
					var link = new URL(value);
					if (item != null) item.setLink(link);
					else feed.setLink(link);
				}

				public String tag() {
					return "link";
				}
			}

			// </title>
			private class TitleHandler implements TagHandler {
				public void handle(XMLEvent e) {
					if (item != null) item.setTitle(value);
					else feed.setTitle(value);
				}

				public String tag() {
					return "title";
				}
			}

			// </pubdate>
			private class PubDateHandler implements TagHandler {
				public void handle(XMLEvent e) throws ParseException {
					var date = format.parse(value);
					if (item != null) item.setDate(date);
					else feed.setDate(date);
				}

				public String tag() {
					return "pubdate";
				}
			}

			// </generator>
			private class GeneratorHandler implements TagHandler {
				public void handle(XMLEvent e) {
					feed.setGenerator(value);
				}

				public String tag() {
					return "generator";
				}
			}

			// </copyright>
			private class CopyrightHandler implements TagHandler {
				public void handle(XMLEvent e) {
					feed.setCopyright(value);
				}

				public String tag() {
					return "copyright";
				}
			}

			// </language>
			private class LanguageHandler implements TagHandler {
				public void handle(XMLEvent e) {
					feed.setLanguage(new Locale(value));
				}

				public String tag() {
					return "language";
				}
			}

			// </category>
			private class CategoryHandler implements TagHandler {
				public void handle(XMLEvent e) {
					feed.addItem(value, item);
				}

				public String tag() {
					return "category";
				}
			}

			// </description>
			private class DescriptionHandler implements TagHandler {
				public void handle(XMLEvent e) {
					if (item != null) item.setDescription(value);
					else feed.setDescription(value);
				}

				public String tag() {
					return "description";
				}
			}
		}
	}

	/**
	 * 簡易的なAtomパーサーの実装です。
	 *
	 * @author 無線部開発班
	 * @since 2011年7月4日
	 */
	static final class AtomParser extends FeedParser {
		private final DateFormat format1, format2;
		private final EndElementHandler endElementHandler;
		private NewsFeed feed;
		private NewsItem item;
		private String value;
		private int nest = 0;

		/**
		 * フィード解析器を生成します。
		 */
		public AtomParser() {
			format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
			format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
			var startElementHandler = new StartElementHandler();
			endElementHandler = new EndElementHandler();
			addHandler(END_ELEMENT, endElementHandler);
			addHandler(START_ELEMENT, startElementHandler);
			addHandler(CHARACTERS, new CharactersHandler());
		}

		/**
		 * このパーサーに関連付けられるMIME typeのリストを返します。
		 *
		 * @return MIME type のリスト
		 */
		@Override
		public List<String> getMimeTypes() {
			return List.of("application/atom+xml");
		}

		/**
		 * 読み込み先ストリームを指定してフィードを取得します。
		 *
		 * @param stream フィードを読み込むストリーム
		 *
		 * @return フィード
		 *
		 * @throws IOException 読み込みに失敗した場合
		 */
		@Override
		public NewsFeed parse(InputStream stream) throws IOException {
			try {
				super.parse(stream);
				return feed;
			} finally {
				feed = null;
			}
		}

		private class CharactersHandler extends XMLHandler {
			private final StringBuilder sb = new StringBuilder();

			@Override
			public void handle(XMLEvent e) {
				if (nest != 0) return;
				if (value == null) sb.setLength(0);
				var data = ((Characters) e).getData();
				final var length = data.length();
				for (var i = 0; i < length; i++) {
					var ch = data.charAt(i);
					if (Character.getType(ch) != Character.LINE_SEPARATOR) {
						sb.append(ch);
					}
				}
				value = sb.toString();
			}
		}

		private class StartElementHandler extends XMLHandler {
			public StartElementHandler() {
				addHandler(new StartElementHandler.FeedHandler());
				addHandler(new StartElementHandler.EntryHandler());
				addHandler(new StartElementHandler.CategoryHandler());
				addHandler(new StartElementHandler.LinkHandler());
			}

			@Override
			public void handle(XMLEvent e) throws Exception {
				value = null;
				var qname = ((StartElement) e).getName();
				var local = qname.getLocalPart();
				var prefix = qname.getPrefix();
				if (prefix.equals(DEFAULT_NS_PREFIX)) {
					var handler = getHandler(local);
					if (handler != null) handler.handle(e);
					else if (!endElementHandler.isSupported(local)) {
						if (feed != null) nest++;
					}
				} else if (feed != null) nest++;
			}

			// <feed>
			private class FeedHandler implements TagHandler {
				public void handle(XMLEvent e) {
					feed = new NewsFeed();
				}

				public String tag() {
					return "feed";
				}
			}

			// <entry>
			private class EntryHandler implements TagHandler {
				public void handle(XMLEvent e) {
					if (feed != null) {
						feed.addItem(item = new NewsItem());
					}
				}

				public String tag() {
					return "entry";
				}
			}

			// <category>
			private class CategoryHandler implements TagHandler {
				public void handle(XMLEvent e) {
					var qterm = new QName("term");
					var se = (StartElement) e;
					var term = se.getAttributeByName(qterm);
					feed.addItem(term.getValue(), item);
				}

				public String tag() {
					return "category";
				}
			}

			// <link>
			private class LinkHandler implements TagHandler {
				public void handle(XMLEvent e) throws MalformedURLException {
					var se = (StartElement) e;
					var qrel = new QName("rel");
					var qhref = new QName("href");
					var rel = se.getAttributeByName(qrel);
					var href = se.getAttributeByName(qhref);
					if ("alternate".equals(rel.getValue())) {
						var link = new URL(href.getValue());
						if (item != null) item.setLink(link);
						else feed.setLink(link);
					}
				}

				public String tag() {
					return "link";
				}
			}
		}

		private class EndElementHandler extends XMLHandler {
			public EndElementHandler() {
				addHandler(new EndElementHandler.EntryHandler());
				addHandler(new EndElementHandler.TitleHandler());
				addHandler(new EndElementHandler.ContentHandler());
				addHandler(new EndElementHandler.UpdatedHandler());
				addHandler(new EndElementHandler.GeneratorHandler());
				addHandler(new EndElementHandler.RightsHandler());
			}

			@Override
			public void handle(XMLEvent e) throws Exception {
				if (feed == null) return;
				var qname = ((EndElement) e).getName();
				var local = qname.getLocalPart();
				if (nest != 0) nest--;
				else {
					var handler = getHandler(local);
					if (handler != null) handler.handle(e);
				}
				value = null;
			}

			/**
			 * RFC3339形式に従って日時を解析して返します。
			 *
			 * @param text 日時の文字列
			 *
			 * @return 解析された日時
			 *
			 * @throws ParseException 日時の書式が不正の場合
			 */
			private Date parseDate(String text) throws ParseException {
				try {
					return format1.parse(text);
				} catch (ParseException ex) {
					return format2.parse(text);
				}
			}

			// </entry>
			private class EntryHandler implements TagHandler {
				public void handle(XMLEvent e) {
					item = null;
				}

				public String tag() {
					return "entry";
				}
			}

			// </title>
			private class TitleHandler implements TagHandler {
				public void handle(XMLEvent e) {
					if (item != null) item.setTitle(value);
					else feed.setTitle(value);
				}

				public String tag() {
					return "title";
				}
			}

			// </content>
			private class ContentHandler implements TagHandler {
				public void handle(XMLEvent e) {
					if (item != null) item.setDescription(value);
					else feed.setDescription(value);
				}

				public String tag() {
					return "content";
				}
			}

			// </updated>
			private class UpdatedHandler implements TagHandler {
				public void handle(XMLEvent e) throws ParseException {
					var date = parseDate(value);
					if (item != null) item.setDate(date);
					else feed.setDate(date);
				}

				public String tag() {
					return "updated";
				}
			}

			// </generator>
			private class GeneratorHandler implements TagHandler {
				public void handle(XMLEvent e) {
					feed.setGenerator(value);
				}

				public String tag() {
					return "generator";
				}
			}

			// </rights>
			private class RightsHandler implements TagHandler {
				public void handle(XMLEvent e) {
					feed.setCopyright(value);
				}

				public String tag() {
					return "rights";
				}
			}
		}
	}

	/**
	 * サポートされていない形式のフィードを読み込もうとした場合にスローされます。
	 *
	 * @author 無線部開発班
	 * @since 2011年9月22日
	 */
	public static class UnknownFormatException extends IOException {
		private static final long serialVersionUID = 1L;

		/**
		 * 指定された詳細メッセージを持つ例外を生成します。
		 *
		 * @param msg 例外の内容を説明するメッセージ
		 */
		public UnknownFormatException(String msg) {
			super(msg);
		}

	}
}
