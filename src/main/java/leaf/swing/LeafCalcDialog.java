/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import javax.swing.*;

import leaf.util.MathUtils;

import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.math.RoundingMode.HALF_EVEN;

/**
 * 32桁四則演算・メモリー機能を持つ汎用の電卓ダイアログです。
 * 内部的に10進数で計算するため基数変換の誤差は発生しません。
 *
 * @author 無線部開発班
 * @since 2010年5月8日
 */
public final class LeafCalcDialog extends LeafDialog {
	private static final int SCALE = 32;
	private static final int BUTTON_WIDTH = 80;
	private static final boolean DISPLAY_IS_INPUT = true;
	private static final boolean DISPLAY_IS_RESULT = false;
	private static final int OP_NON = 0;
	private static final int OP_ADD = 1;
	private static final int OP_SUB = 2;
	private static final int OP_MUL = 3;
	private static final int OP_DIV = 4;
	private final MathUtils leafMath;
	private final Font font_button, font_big, font_small;
	private BigDecimal memo, dnm, nmr;
	private boolean display;
	private String input;
	private int op;
	private JLabel label;
	private InputListener listener;

	/**
	 * 親ダイアログを指定して電卓ダイアログを生成します。
	 *
	 * @param owner 親ダイアログ
	 */
	public LeafCalcDialog(Dialog owner) {
		super(owner, false);
		setResizable(false);
		leafMath = new MathUtils(SCALE);
		font_button = new Font(Font.MONOSPACED, Font.PLAIN, 18);
		font_small = new Font(Font.SERIF, Font.BOLD, 24);
		font_big = new Font(Font.SERIF, Font.BOLD, 32);
		setLayout(new BorderLayout());
		createContents();
		initialize();
	}

	/**
	 * 親フレームを指定して電卓ダイアログを生成します。
	 *
	 * @param owner 親フレーム
	 */
	public LeafCalcDialog(Frame owner) {
		super(owner, false);
		setResizable(false);
		leafMath = new MathUtils(SCALE);
		font_button = new Font(Font.MONOSPACED, Font.PLAIN, 18);
		font_small = new Font(Font.SERIF, Font.BOLD, 24);
		font_big = new Font(Font.SERIF, Font.BOLD, 32);
		setLayout(new BorderLayout());
		createContents();
		initialize();
	}

	private void addToMemory() {
		memo = memo.add(new BigDecimal(label.getText()));
	}

	private void backspace() {
		if (display == DISPLAY_IS_INPUT && input.length() > 1) {
			if (input.charAt(input.length() - 1) == '.') input = input.substring(0, input.length() - 2);
			else input = input.substring(0, input.length() - 1);
			label.setText(input.isEmpty() ? "0" : input);
		} else {
			input = "";
			label.setText("0");
		}
		display = DISPLAY_IS_INPUT;
	}

	private void calculate(int op) {
		if (!input.isEmpty()) {
			var decimal = new BigDecimal(input);
			switch (this.op) {
				case OP_ADD:
					nmr = nmr.add(dnm.multiply(decimal));
					break;
				case OP_SUB:
					nmr = nmr.subtract(dnm.multiply(decimal));
					break;
				case OP_MUL:
					nmr = nmr.multiply(decimal);
					break;
				case OP_DIV:
					dnm = dnm.multiply(decimal);
					break;
				default:
					nmr = decimal;
					dnm = BigDecimal.ONE;
			}
		} else {
			switch (this.op) {
				case OP_SUB:
					nmr = nmr.negate();
					break;
				case OP_MUL:
					nmr = nmr.pow(2);
					dnm = dnm.pow(2);
					break;
				case OP_DIV:
					var tmp = nmr;
					nmr = dnm;
					dnm = tmp;
			}
		}
		if ((this.op = op) == OP_NON) display = DISPLAY_IS_RESULT;
		input = "";
		display();
	}

	private void clear() {
		nmr = BigDecimal.ZERO;
		dnm = BigDecimal.ONE;
		label.setText(nmr.toString());
		display = DISPLAY_IS_INPUT;
		op = OP_NON;
		input = "";
	}

	private void clearAll() {
		clear();
		clearMemory();
	}

	private void clearInput() {
		if (display == DISPLAY_IS_INPUT) {
			input = "";
			label.setText("0");
		}
	}

	private void clearMemory() {
		memo = BigDecimal.ZERO;
	}

	private void createContents() {
		getContentPane().removeAll();
		label = new CalcLabel();
		label.setOpaque(true);
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		add(label, BorderLayout.NORTH);
		var panel = new JPanel(new GridLayout(5, 5));
		add(panel, BorderLayout.CENTER);
		listener = new InputListener();
		addKeyListener(listener);
		panel.add(new InputButton("CM"));
		panel.add(new InputButton("RM"));
		panel.add(new InputButton("M-"));
		panel.add(new InputButton("M+"));
		panel.add(new InputButton("√"));
		panel.add(new InputButton("7"));
		panel.add(new InputButton("8"));
		panel.add(new InputButton("9"));
		panel.add(new InputButton("/"));
		panel.add(new InputButton("AC"));
		panel.add(new InputButton("4"));
		panel.add(new InputButton("5"));
		panel.add(new InputButton("6"));
		panel.add(new InputButton("*"));
		panel.add(new InputButton("C"));
		panel.add(new InputButton("1"));
		panel.add(new InputButton("2"));
		panel.add(new InputButton("3"));
		panel.add(new InputButton("-"));
		panel.add(new InputButton("CE"));
		panel.add(new InputButton("0"));
		panel.add(new InputButton("."));
		panel.add(new InputButton("+/-"));
		panel.add(new InputButton("+"));
		panel.add(new InputButton("="));
		setPreferredContentSize();
	}

	private void display() {
		if (dnm.compareTo(BigDecimal.ZERO) != 0) {
			var val = nmr.divide(dnm, SCALE, HALF_EVEN);
			var text = val.toPlainString();
			if (text.indexOf('.') > 0) {
				var i = text.length() - 1;
				while (text.charAt(--i) == '0') ;
				if (text.charAt(i) == '.') i--;
				label.setText(text.substring(0, i + 1));
			} else label.setText(text);
		} else {
			label.setText("E");
			input = "";
		}
	}

	private void displayMemory() {
		display = DISPLAY_IS_RESULT;
		input = memo.toPlainString();
		label.setText(input);
	}

	private void e() {
		label.setText(input = "2.7182818284590452353602874713527");
		display = DISPLAY_IS_RESULT;
	}

	/**
	 * 電卓画面の表示を初期化します。
	 */
	@Override
	public void initialize() {
		clearAll();
		setTitle(translate("title"));
	}

	private void input(char ch) {
		if (display == DISPLAY_IS_RESULT) input = "";
		if (input.length() < SCALE && (ch != '.' || input.indexOf('.') < 0)) {
			if (input.isEmpty() && ch == '.') input = "0";
			input = input.concat(Character.toString(ch));
			display = DISPLAY_IS_INPUT;
			label.setText(input);
		}
	}

	private void negate() {
		if (display == DISPLAY_IS_INPUT) {
			if (input.isEmpty()) return;
			var dec = new BigDecimal(input);
			input = dec.negate().toPlainString();
			label.setText(input);
		} else {
			nmr = nmr.negate();
			display();
		}
	}

	private void pi() {
		label.setText(input = "3.1415926535897932384626433832795");
		display = DISPLAY_IS_RESULT;
	}

	private void setPreferredContentSize() {
		var dummy = new JButton("DUMMY");
		var h = dummy.getPreferredSize().height;
		label.setPreferredSize(new Dimension(getWidth(), 2 * h));
		setContentSize(new Dimension(5 * BUTTON_WIDTH, 7 * h));
	}

	/**
	 * 電卓画面を表示します。
	 */
	public void showDialog() {
		super.setVisible(true);
	}

	private void sqrt() {
		var input = new BigDecimal(label.getText());
		this.input = leafMath.sqrt(input).toPlainString();
		label.setText(this.input);
		display = DISPLAY_IS_INPUT;
	}

	private void subtractFromMemory() {
		memo = memo.subtract(new BigDecimal(label.getText()));
	}

	private class CalcLabel extends JLabel {
		public CalcLabel() {
			super("", JLabel.RIGHT);
		}

		public void setText(String text) {
			if (text != null) {
				setFont(text.length() > 24 ? font_small : font_big);
			}
			super.setText(text);
		}
	}

	private class InputButton extends JButton {
		public InputButton(String name) {
			super(name);
			setFont(font_button);
			setFocusable(false);
			setActionCommand(name);
			addActionListener(listener);
		}
	}

	private class InputListener extends KeyAdapter implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			var cmd = e.getActionCommand();
			if (cmd.matches("[0-9.]")) input(cmd.charAt(0));
			else if (cmd.equals("+")) calculate(OP_ADD);
			else if (cmd.equals("-")) calculate(OP_SUB);
			else if (cmd.equals("*")) calculate(OP_MUL);
			else if (cmd.equals("/")) calculate(OP_DIV);
			else if (cmd.equals("=")) calculate(OP_NON);
			else if (cmd.equals("AC")) clearAll();
			else if (cmd.equals("C")) clear();
			else if (cmd.equals("CE")) clearInput();
			else if (cmd.equals("+/-")) negate();
			else if (cmd.equals("CM")) clearMemory();
			else if (cmd.equals("RM")) displayMemory();
			else if (cmd.equals("M+")) addToMemory();
			else if (cmd.equals("M-")) subtractFromMemory();
			else sqrt();
		}

		@Override
		public void keyPressed(KeyEvent e) {
			var ch = e.getKeyChar();
			var code = e.getKeyCode();
			if (ch >= '0' && ch <= '9' || ch == '.') input(ch);
			else if (ch == '+') calculate(OP_ADD);
			else if (ch == '-') calculate(OP_SUB);
			else if (ch == '*') calculate(OP_MUL);
			else if (ch == '/') calculate(OP_DIV);
			else if (code == VK_BACK_SPACE) backspace();
			else if (code == VK_DELETE) clear();
			else if (code == VK_ENTER) calculate(OP_NON);
			else if (code == VK_ESCAPE) dispose();
			else if (ch == 'p') pi();
			else if (ch == 'e') e();
		}
	}

}
