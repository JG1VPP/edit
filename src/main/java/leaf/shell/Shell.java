/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.shell;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * アプリケーションが装備するコマンドシステムの中核となるクラスです。
 *
 * @author 無線部開発班
 * @since 2011年12月11日
 */
public class Shell {
	private final HashMap<String, Command> table;
	private final Logger logger;
	private final Locale locale = Locale.getDefault();

	/**
	 * シェルを構築します。
	 */
	public Shell() {
		table = new HashMap<>();
		var cons = new ConsoleHandler();
		cons.setFormatter(new ShellLogFormatter());
		logger = Logger.getLogger(getClass().getName());
		logger.setUseParentHandlers(false);
		logger.addHandler(cons);
	}

	/**
	 * コマンド名と引数を指定してコマンドを実行します。
	 *
	 * @param name コマンド名
	 * @param args コマンドに渡す引数
	 */
	public void call(String name, Object... args) {
		var cmd = table.get(name);
		if (cmd != null) try {
			cmd.process(args);
		} catch (Exception ex) {
			var sw = new StringWriter();
			var pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.warning(sw.toString());
		}
		else logger.warning("Not found : " + name);
	}

	/**
	 * シェルに登録されている全てのコマンドを削除します。
	 */
	public void clear() {
		table.clear();
	}

	/**
	 * 指定されたコマンド名に対応するコマンドを返します。
	 *
	 * @param name コマンド名
	 *
	 * @return 対応するコマンド 存在しない場合null
	 */
	public Command getCommand(String name) {
		return table.get(name);
	}

	/**
	 * シェルに適用されているロケールを返します。
	 *
	 * @return 適用されているロケール
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * シェルに適用されるロケールを設定します。
	 *
	 * @param locale 適用するロケール
	 *
	 * @throws NullPointerException ロケールがnullの場合
	 */
	public void setLocale(Locale locale) throws NullPointerException {
		if (!locale.equals(this.locale)) {
			final var e = new LocaleEvent(this, locale);
			for (var cmd : table.values()) {
				if (cmd instanceof LocaleListener) {
					((LocaleListener) cmd).localeChanged(e);
				}
			}
		}
	}

	/**
	 * 新しいコマンドをシェルにインストールします。
	 *
	 * @param cmd 追加するコマンド
	 */
	public void install(Command cmd) {
		table.put(cmd.getName(), cmd);
	}

	/**
	 * コマンドをシェルからアンインストールします。
	 *
	 * @param cmd 削除するコマンド
	 */
	public void uninstall(Command cmd) {
		table.remove(cmd.getName());
	}

	/**
	 * シェルで発生した全ての例外の出力書式を定義します。
	 *
	 * @since 2011年12月11日
	 */
	private final class ShellLogFormatter extends Formatter {
		final DateFormat format = DateFormat.getDateTimeInstance();
		private final Date date = new Date();

		@Override
		public String format(LogRecord record) {
			var sb = new StringBuilder();
			date.setTime(record.getMillis());
			sb.append(format.format(date)).append(' ');
			sb.append(record.getLevel().getLocalizedName());
			sb.append(" at ");
			sb.append(Shell.this.getClass().getSimpleName());
			sb.append('\n');
			sb.append(super.formatMessage(record));
			return sb.append('\n').toString();
		}
	}
}
