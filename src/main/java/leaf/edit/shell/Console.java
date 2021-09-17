/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;

import leaf.edit.cmd.Command;
import leaf.edit.ui.TextEditor;
import leaf.edit.ui.TextEditorUtils;

/**
 * コンソール出力を表示するコマンドです。
 *
 * @author 無線部開発班
 */
public final class Console extends Command {
	private WeakReference<TextEditor> ref = null;

	private WeakReference<TextEditor> createConsoleOutputEditor() {
		TextEditor editor = TextEditorUtils.addTab("Console");
		editor.setEditable(false);
		var cos = new ConsoleOutputStream(editor);
		var ps = new PrintStream(cos, true);
		System.setErr(ps);
		System.setOut(ps);
		return new WeakReference<>(editor);
	}

	@Override
	public void process(Object... args) {
		System.gc(); // clear weak references
		if (ref == null || ref.get() == null) {
			ref = createConsoleOutputEditor();
		} else {
			TextEditorUtils.setSelectedEditor(ref.get());
		}
	}

	private static class ConsoleOutputStream extends OutputStream {
		private final WeakReference<TextEditor> ref;
		private final ByteArrayOutputStream stream;

		public ConsoleOutputStream(TextEditor editor) {
			super();
			this.ref = new WeakReference<>(editor);
			this.stream = new ByteArrayOutputStream();
		}

		@Override
		public synchronized void write(int b) {
			stream.write(b);
		}

		@Override
		public synchronized void flush() {
			var editor = ref.get();
			if (editor != null) editor.append(stream.toString());
			stream.reset();
		}
	}
}
