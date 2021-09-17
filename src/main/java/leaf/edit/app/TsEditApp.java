/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.app;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.*;

import leaf.edit.shell.*;
import leaf.edit.ui.BasicTextEditor;
import leaf.edit.ui.MenuBar;
import leaf.edit.ui.SyntaxHighlight;
import leaf.edit.ui.TextEditorUtils;
import leaf.edit.ui.ToolBar;
import leaf.shell.UnknownNameException;
import leaf.swing.TabCloseEvent;
import leaf.swing.TabCloseListener;
import leaf.util.LocalizeManager;
import leaf.main.Application;
import leaf.main.Shell;
import leaf.swing.MainFrame;

/**
 * フレームワークに適用されるtseditアプリケーションです。
 *
 * @author 無線部開発班
 * @since 2012/03/28
 */
public final class TsEditApp extends Application {
	private static TsEditApp instance;
	private final MainFrame frame;

	private TsEditApp(MainFrame frame) {
		this.frame = frame;
		UIManager.put("FileChooser.readOnly", true);
		var locale = SetLocale.getLocale();
		LocalizeManager.setLocale(locale);
		JComponent.setDefaultLocale(locale);
	}

	@Override
	public JMenuBar createMenuBar() {
		var menubar = MenuBar.getInstance();
		try {
			menubar.initialize();
		} catch (IOException | UnknownNameException ex) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(ex.toString());
		}
		return menubar;
	}

	@Override
	public JToolBar createToolBar() {
		var toolbar = ToolBar.getInstance();
		try {
			toolbar.initialize();
		} catch (IOException | UnknownNameException ex) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(ex.toString());
		}
		return toolbar;
	}

	@Override
	public void installCommands(Shell shell) {
		//file menu
		shell.install(new New());
		shell.install(new Open());
		shell.install(new CloseAndOpen());
		shell.install(new Save());
		shell.install(new SaveAs());
		shell.install(new SaveAll());
		shell.install(new Print());
		shell.install(new ReadIn());
		shell.install(new WriteOut());
		shell.install(new Reopen());
		shell.install(new SaveAndCloseTab());
		shell.install(new CloseTab());
		shell.install(new CloseAllTabs());
		shell.install(new Exit());
		shell.install(History.getInstance());
		//leaf.edit menu
		shell.install(new Undo());
		shell.install(new Redo());
		shell.install(new Cut());
		shell.install(new Copy());
		shell.install(new Paste());
		shell.install(new Delete());
		shell.install(new DeleteLine());
		shell.install(new Backspace());
		shell.install(new SelectLine());
		shell.install(new SelectAll());
		//insert menu
		shell.install(new InsertDate());
		shell.install(new InsertTime());
		shell.install(new InsertOSVersion());
		shell.install(new InsertUserName());
		shell.install(new InsertFileName());
		shell.install(new InsertFilePath());
		shell.install(new InsertTextLength());
		shell.install(new InsertByteLength());
		shell.install(new InsertPassword());
		//move menu
		shell.install(new MoveToBOF());
		shell.install(new MoveToEOF());
		shell.install(new MoveToBOL());
		shell.install(new MoveToEOL());
		shell.install(new MoveToPreviousPage());
		shell.install(new MoveToNextPage());
		//select menu
		shell.install(new SelectToBOF());
		shell.install(new SelectToEOF());
		shell.install(new SelectToBOL());
		shell.install(new SelectToEOL());
		//format menu
		shell.install(new SortInAscending());
		shell.install(new SortInDescending());
		//convert menu
		shell.install(new ConvertToLowerCase());
		shell.install(new ConvertToUpperCase());
		shell.install(new ConvertToEmAlphaNumeric());
		shell.install(new ConvertToEnAlphaNumeric());
		shell.install(new ConvertToEmAlphabet());
		shell.install(new ConvertToEnAlphabet());
		shell.install(new ConvertToEmNumeric());
		shell.install(new ConvertToEnNumeric());
		shell.install(new ConvertToKatakana());
		shell.install(new ConvertToHiragana());
		shell.install(new ConvertToWhitespace());
		shell.install(new ConvertToTab());
		//search menu
		var find = new Find();
		shell.install(find);
		shell.install(new FindNext(find));
		shell.install(new FindPrevious(find));
		shell.install(new Replace());
		shell.install(new Grep());
		shell.install(new Diff());
		shell.install(new Jump());
		//web search menu
		shell.install(new SearchWithGoogle());
		shell.install(new SearchWithYahoo());
		shell.install(new SearchWithYouTube());
		shell.install(new SearchWithWikipedia());
		shell.install(new SearchWithNicovideo());
		//extend menu
		shell.install(new Eval());
		shell.install(new Browse());
		shell.install(new HexDump());
		shell.install(new NewsFeed());
		shell.install(new LeftScroll());
		shell.install(new Life());
		shell.install(new Wireworld());
		shell.install(new WorldClock());
		shell.install(new Calculator());
		shell.install(new Console());
		shell.install(new Resource());
		shell.install(new HideWindows());
		//settings menu
		shell.install(new SetLocale());
		shell.install(new SetLnF());
		shell.install(new SetFont());
		shell.install(new SetSyntaxHighlight());
		shell.install(new SetWallpaper());
		shell.install(new SetCursorImage());
		shell.install(new SetTypingSound());
		shell.install(new SetFileExtensions());
		shell.install(new SetCharacterEncodings());
		shell.install(new SetPlugins());
		//tab size menu
		shell.install(new SetTabSize1());
		shell.install(new SetTabSize2());
		shell.install(new SetTabSize3());
		shell.install(new SetTabSize4());
		shell.install(new SetTabSize5());
		shell.install(new SetTabSize6());
		shell.install(new SetTabSize7());
		shell.install(new SetTabSize8());
		//font size menu
		shell.install(new SetFontSizeExtraBig());
		shell.install(new SetFontSizeBig());
		shell.install(new SetFontSizeNormal());
		shell.install(new SetFontSizeSmall());
		//newline code menu
		shell.install(new SetNewLineCodeCR());
		shell.install(new SetNewLineCodeLF());
		shell.install(new SetNewLineCodeCRLF());
		//window menu
		shell.install(new SplitEditorHorizontal());
		shell.install(new SplitEditorVertical());
		shell.install(new MergeEditor());
		shell.install(new NextTab());
		shell.install(new PreviousTab());
		shell.install(new FirstTab());
		shell.install(new LastTab());
		shell.install(new SearchTab());
		shell.install(new SortTabs());
		shell.install(new AlwaysOnTop());
		shell.install(new MaximizeWindow());
		shell.install(new MinimizeWindow());
		//help menu
		shell.install(new HomePage());
	}

	@Override
	public void installFinished() {
		SyntaxHighlight.load();
		TextEditorUtils.addTab();
		frame.addWindowListener(new WindowCloseHandler());
		var tabpane = TextEditorUtils.getTabbedPane();
		tabpane.addTabListener(new EditorCloseHandler());
		getMainFrame().initialize();
	}

	/**
	 * このアプリケーションが関連付けられているメインウィンドウを返します。
	 *
	 * @return メインウィンドウ
	 *
	 * @throws IllegalStateException このアプリケーションが無効である場合
	 */
	public static MainFrame getMainFrame() throws IllegalStateException {
		if (instance != null) return instance.frame;
		throw new IllegalStateException();
	}

	/**
	 * アプリケーションは必ずこのメソッドで起動されます。
	 *
	 * @param frame アプリケーションが適用されるメイン画面
	 *
	 * @return 起動直後のアプリケーション
	 */
	public static Application newInstance(MainFrame frame) {
		instance = new TsEditApp(frame);
		TextEditorUtils.setTabChangeListener();
		return instance;
	}

	private static class WindowCloseHandler extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			Shell.getInstance().call("Exit");
		}
	}

	private static class EditorCloseHandler implements TabCloseListener {
		@Override
		public boolean tabClosing(TabCloseEvent e) {
			var comp = e.getComponent();
			if (comp instanceof BasicTextEditor) {
				return CloseTab.close((BasicTextEditor) comp);
			}
			return true;
		}
	}

}
