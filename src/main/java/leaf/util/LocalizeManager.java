/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * LeafAPIの全クラスの多言語化を支援する専用マネージャです。
 *
 * @author 無線部開発班
 * @since 2011年6月18日
 */
public final class LocalizeManager {
	private static final WeakHashMap<Class<?>, LocalizeManager> map;
	private static Locale locale = Locale.ENGLISH;

	static {
		map = new WeakHashMap<>();
	}

	private final Class<?> clazz;
	private final Logger logger;
	private HashMap<String, String> table;
	private Locale mylocale;

	/**
	 * クラスを指定してマネージャのインスタンスを生成します。
	 *
	 * @param clazz 契約するクラス
	 */
	private LocalizeManager(Class<?> clazz) {
		final var name = getClass().getName() + " for " + clazz.getName();
		var cons = new ConsoleHandler();
		cons.setFormatter(new LogFormatter());
		logger = Logger.getLogger(name);
		logger.setUseParentHandlers(false);
		logger.addHandler(cons);
		this.clazz = clazz;
	}

	/**
	 * 指定したロケールがサポートされているか調べます。
	 *
	 * @param locale 調べるロケール
	 *
	 * @return 対応ファイルが用意されている場合true
	 */
	public boolean available(Locale locale) {
		return clazz.getClassLoader().getResource(getPath(locale)) != null;
	}

	/**
	 * 指定されたロケールでのリソースディレクトリへのパスを返します。
	 *
	 * @param locale ロケール
	 *
	 * @return 言語セットの存在するディレクトリへのパス
	 */
	private String getPath(Locale locale) {
		var pack = clazz.getPackage().getName();
		final var path = new StringJoiner("/");
		path.add("localize");
		path.add(locale.getISO3Language());
		path.add(pack.replace(".", "/"));
		path.add(clazz.getSimpleName() + ".xml");
		return path.toString();
	}

	/**
	 * 指定されたリソースの読み込みストリームを返します。
	 *
	 * @param name リソース名
	 *
	 * @return リソースを読み込むストリーム
	 */
	private InputStream getResource(String name) {
		this.mylocale = locale;
		return clazz.getClassLoader().getResourceAsStream(getPath(locale));
	}

	/**
	 * 必要とされる時点で言語セットを読み込みます。
	 *
	 * @param locale 読み込む言語に対応するロケール
	 *
	 * @throws SAXException
	 * @throws IOException        読み込みに失敗した場合
	 * @throws ClassCastException 読み込み先がJarでない場合
	 */
	private void load(Locale locale) throws Exception {
		this.mylocale = locale;
		var name = clazz.getSimpleName() + ".xml";
		table = new DataParser().parse(getResource(name));
	}

	/**
	 * エラーログ付で言語セットを読み込みます。
	 *
	 * @param locale 読み込む言語に対応するロケール
	 *
	 * @return 読み込みに成功した場合true
	 */
	private boolean loadAndLog(Locale locale) {
		try {
			load(locale);
			logger.fine("loaded successfully.");
			return true;
		} catch (Exception ex) {
			logger.warning("failed to load:" + ex);
		}
		return false;
	}

	/**
	 * 指定したキーに対応するローカライズされた文字列を返します。
	 *
	 * @param key 検索用のキー
	 *
	 * @return ロケールに対応した文字列
	 */
	public String translate(String key) {
		if (!mylocale.equals(locale)) {
			if (!loadAndLog(locale)) loadAndLog(Locale.ENGLISH);
		}
		var localized = table.get(key);
		if (localized != null) return localized;
		logger.warning("key not found:" + key);
		return "";
	}

	/**
	 * 指定したキーに対応するフォーマット済み文字列を返します。
	 *
	 * @param key  検索用のキー
	 * @param args 書式指示子により参照される引数
	 *
	 * @return ロケールに対応したフォーマット済み文字列
	 */
	public String translate(String key, Object... args) {
		try {
			return String.format(translate(key), args);
		} catch (IllegalFormatException ex) {
			logger.warning("illegal format : " + key);
			return "";
		}
	}

	/**
	 * 指定されたクラスに対応するマネージャを返します。
	 *
	 * @param clazz 国際化するクラス
	 *
	 * @return マネージャのインスタンス
	 */
	public static LocalizeManager get(Class<?> clazz) {
		var instance = map.get(clazz);
		if (instance == null) {
			instance = new LocalizeManager(clazz);
			instance.loadAndLog(LocalizeManager.locale);
			map.put(clazz, instance);
		}
		return instance;
	}

	/**
	 * マネージャが使用する言語を返します。
	 *
	 * @return 設定されている言語ロケール
	 */
	public static Locale getLocale() {
		return LocalizeManager.locale;
	}

	/**
	 * マネージャが使用する言語を指定します。
	 *
	 * @param locale 設定する言語ロケール
	 */
	public static void setLocale(Locale locale) {
		LocalizeManager.locale = locale;
	}

	/**
	 * マネージャのエラーログの書式を指定します。
	 */
	private final class LogFormatter extends Formatter {
		private final Date date = new Date();
		private final DateFormat format;

		public LogFormatter() {
			format = DateFormat.getDateTimeInstance();
		}

		@Override
		public synchronized String format(LogRecord record) {
			var sb = new StringBuilder();
			date.setTime(record.getMillis());
			sb.append(format.format(date));
			sb.append(' ');
			sb.append(record.getLevel().getLocalizedName());
			sb.append("\nat LocalizeManager for ");
			sb.append(clazz.getCanonicalName());
			sb.append(' ');
			sb.append(mylocale.getDisplayLanguage(Locale.ENGLISH));
			sb.append('\n');
			sb.append(super.formatMessage(record));
			return sb.append('\n').toString();
		}
	}

	/**
	 * XML形式の言語セットをストリームから読み込みます。
	 */
	private static final class DataParser extends DefaultHandler {
		private HashMap<String, String> table;

		/**
		 * XMLを解析して対応するマップを返します。
		 *
		 * @param stream ソースとなるストリーム
		 *
		 * @return 読み込まれた言語セット
		 */
		public HashMap<String, String> parse(InputStream stream) throws Exception {
			this.table = new HashMap<>();
			SAXParserFactory fact;
			try {
				fact = SAXParserFactory.newInstance();
				if (fact != null) {
					var parser = fact.newSAXParser();
					parser.parse(stream, this);
				}
				return this.table;
			} finally {
				if (stream != null) stream.close();
			}
		}

		@Override
		public void startElement(String uri, String local, String name, Attributes attr) {
			if (name != null && name.equals("entry")) {
				var key = attr.getValue("key");
				var value = attr.getValue("value");
				this.table.put(key, value);
			}
		}
	}

}
