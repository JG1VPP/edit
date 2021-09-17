/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.awt.*;
import java.util.Arrays;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import leaf.edit.cmd.EditorCommand;
import leaf.shell.LocaleEvent;
import leaf.shell.LocaleListener;
import leaf.swing.LeafDialog;
import leaf.util.Find;
import leaf.util.LocalizeManager;

/**
 * エディタで表示中のプログラムを実行するコマンドを処理します。
 *
 * @author 無線部開発班
 * @since 2011/11/12
 */
public final class Eval extends EditorCommand implements LocaleListener {
	private final LocalizeManager localize;
	private final ScriptEngineManager manager;
	private final String[] languages;
	private ScriptEngine engine;
	private EvalDialog dialog = null;

	public Eval() {
		manager = new ScriptEngineManager();
		var facts = manager.getEngineFactories();
		languages = new String[facts.size()];
		for (var index = 0; index < facts.size(); index++) {
			var fact = facts.get(index);
			languages[index] = fact.getLanguageName();
		}
		Arrays.sort(languages);
		localize = LocalizeManager.get(getClass());
	}

	@Override
	public void localeChanged(LocaleEvent e) {
		if (dialog != null) dialog.initialize();
	}

	@Override
	public void process(Object... args) {
		if (dialog == null) dialog = new EvalDialog();
		var suffix = Find.getSuffix(getEditor().getFile());
		if (suffix != null) {
			var engine = manager.getEngineByExtension(suffix);
			if (engine != null) {
				var language = engine.getFactory().getLanguageName();
				dialog.combo.setSelectedItem(language);
			}
		}
		dialog.setVisible(true);
	}

	final class EvalDialog extends LeafDialog {
		private final JLabel labelInfo1 = new JLabel();
		private final JLabel labelInfo2 = new JLabel();
		public JComboBox<String> combo;

		public EvalDialog() {
			super(getFrame(), false);
			setTitle(localize.translate("title"));
			setLayout(new BorderLayout());
			initialize();
			pack();
			setResizable(false);
		}

		private void createPanel1Content(JPanel panel1) {
			combo = new JComboBox<>();
			panel1.add(combo, BorderLayout.CENTER);
			combo.addItemListener(e -> {
				var name = (String) combo.getSelectedItem();
				engine = manager.getEngineByName(name);
				if (engine != null) updateInfo();
			});
			for (var name : languages) combo.addItem(name);
		}

		private void createPanel2Content(JPanel panel2) {
			panel2.add(labelInfo1, BorderLayout.NORTH);
			panel2.add(labelInfo2, BorderLayout.SOUTH);
		}

		private void createPanel3Content(JPanel panel3) {
			panel3.add(Box.createHorizontalGlue());
			var button1 = new JButton();
			button1.setText(localize.translate("button_eval"));
			panel3.add(button1);
			button1.addActionListener(e -> {
				var text = getEditor().getText();
				new ScriptWorker(engine, text).execute();
			});
			panel3.add(Box.createHorizontalStrut(5));
			var button2 = new JButton();
			button2.setText(localize.translate("button_close"));
			panel3.add(button2);
			button2.addActionListener(e -> dispose());
			panel3.add(Box.createHorizontalStrut(5));
		}

		@Override
		public void initialize() {
			getContentPane().removeAll();
			var center = new JPanel(new BorderLayout(10, 10));
			add(center, BorderLayout.CENTER);
			var panel1 = new JPanel(new BorderLayout());
			panel1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), localize.translate("panel_select_language")));
			center.add(panel1, BorderLayout.NORTH);
			var panel2 = new JPanel(new BorderLayout());
			panel2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), localize.translate("panel_information")));
			center.add(panel2, BorderLayout.CENTER);
			var panel3 = new JPanel(null);
			panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
			center.add(panel3, BorderLayout.SOUTH);
			var bottom = new JPanel(null);
			bottom.setPreferredSize(new Dimension(360, 10));
			add(bottom, BorderLayout.SOUTH);
			createPanel1Content(panel1);
			createPanel2Content(panel2);
			createPanel3Content(panel3);
		}

		private void updateInfo() {
			var factory = engine.getFactory();
			labelInfo1.setText(String.format("%s %s (%s %s)", factory.getLanguageName(), factory.getLanguageVersion(), factory.getEngineName(), factory.getEngineVersion()));
			labelInfo2.setText(localize.translate("label_example", factory.getProgram(factory.getOutputStatement("Hello, World!"))));
		}
	}

	private static class ScriptWorker extends SwingWorker<String, String> {
		private final ScriptEngine engine;
		private final String script;

		public ScriptWorker(ScriptEngine engine, String script) {
			this.engine = engine;
			this.script = script;
		}

		@Override
		public String doInBackground() {
			try {
				System.out.print(engine.getFactory().getEngineName());
				System.out.println('>');
				var result = engine.eval(script);
				System.out.println("=> " + result);
			} catch (ScriptException ex) {
				System.out.println(ex.getMessage());
			}
			return "Done";
		}
	}
}
