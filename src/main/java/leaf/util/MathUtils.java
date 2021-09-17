/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_EVEN;

/**
 * 指数関数、対数関数、三角関数など数値計算を高精度で実行します。
 *
 * @author 無線部開発班
 * @since 2012年2月2日
 */
public final class MathUtils {
	private final Exponential exponential;
	private final Logarithm logarithm;
	private final SquareRoot sqrt;
	private final Cosine cosine;
	private final int scale;

	/**
	 * 計算精度を指定してオブジェクトを構築します。
	 *
	 * @param scale 計算精度
	 */
	public MathUtils(int scale) {
		this.scale = scale;
		exponential = new Exponential(scale);
		logarithm = new Logarithm(scale);
		sqrt = new SquareRoot(scale);
		cosine = new Cosine(scale);
	}

	/**
	 * 余弦関数を計算します。
	 *
	 * @param rad 孤度法での角度
	 *
	 * @return 余弦値
	 */
	public BigDecimal cos(BigDecimal rad) {
		return cosine.value(rad.remainder(Constants.PI_2MUL));
	}

	/**
	 * 指数関数を計算します。
	 *
	 * @param exp 指数
	 *
	 * @return 指数の計算値
	 */
	public BigDecimal exp(BigDecimal exp) {
		return exponential.value(exp);
	}

	/**
	 * ネイピア数を底とする自然対数を計算します。
	 *
	 * @param val 0よりも大きい数
	 *
	 * @return 自然対数値
	 *
	 * @throws ArithmeticException valが0以下の場合
	 */
	public BigDecimal log(BigDecimal val) {
		return logarithm.log(val);
	}

	/**
	 * 累乗値を計算します。
	 *
	 * @param base ベース
	 * @param exp  指数
	 *
	 * @return 累乗の計算値
	 */
	public BigDecimal pow(BigDecimal base, BigDecimal exp) {
		return exp(exp.multiply(log(base)));
	}

	/**
	 * 正弦関数を計算します。
	 *
	 * @param rad 孤度法での角度
	 *
	 * @return 正弦値
	 */
	public BigDecimal sin(BigDecimal rad) {
		return cosine.value(rad.remainder(Constants.PI_2MUL).subtract(Constants.PI_2DIV));
	}

	/**
	 * 平方根を計算します。
	 *
	 * @param base 平方根を求める数
	 *
	 * @return 平方根
	 */
	public BigDecimal sqrt(BigDecimal base) {
		return sqrt.value(base);
	}

	/**
	 * 正接関数を計算します。
	 *
	 * @param rad 孤度法での角度
	 *
	 * @return 正接値
	 */
	public BigDecimal tan(BigDecimal rad) {
		rad = rad.remainder(Constants.PI_2MUL);
		var sin = cosine.value(rad.subtract(Constants.PI_2DIV));
		var cos = cosine.value(rad);
		return sin.divide(cos, scale, HALF_EVEN);
	}

	/**
	 * 数値計算で用いられる各定数を定義します。
	 *
	 * @author 無線部開発班
	 * @since 2012年2月2日
	 */
	static final class Constants {
		public static final BigDecimal E = new BigDecimal("2.71828182845904523536028747135266249775724709369995957");
		public static final BigDecimal PI = new BigDecimal("3.14159265358979323846264338327950288419716939937510582");
		public static final BigDecimal PI_2MUL = new BigDecimal("6.28318530717958647692528676655900576839433879875021164");
		public static final BigDecimal PI_2DIV = new BigDecimal("1.57079632679489661923132169163975144209858469968755291");
		public static final BigDecimal LN_2 = new BigDecimal("0.69314718055994530941723212145817656807550013436026");
		public static final BigDecimal TWO = new BigDecimal(2);
	}

	/**
	 * 余弦関数の計算を行います。
	 *
	 * @author 無線部開発班
	 * @since 2012年2月2日
	 */
	static final class Cosine extends Taylor {
		public Cosine(int scale) {
			super(scale);
		}

		@Override
		public BigDecimal value(BigDecimal arg) {
			BigDecimal cos = BigDecimal.ONE, old = cos;
			for (var i = 1; true; i++, old = cos) {
				var add = arg.pow(i << 1).divide(factorial(i << 1), scale + 4, HALF_EVEN);
				cos = (i % 2 == 0) ? cos.add(add) : cos.subtract(add);
				if (old.compareTo(cos) == 0) return cos.setScale(scale, HALF_EVEN);
			}
		}
	}

	/**
	 * ネイピア数を底とする指数計算を行います。
	 *
	 * @author 無線部開発班
	 * @since 2012年2月2日
	 */
	static final class Exponential extends Taylor {
		public Exponential(int scale) {
			super(scale);
		}

		@Override
		public BigDecimal value(BigDecimal arg) {
			BigDecimal result = BigDecimal.ONE, old = result;
			for (var i = 1; true; i++, old = result) {
				result = result.add(arg.pow(i).divide(factorial(i), scale + 4, HALF_EVEN));
				if (old.compareTo(result) == 0) return result.setScale(scale, HALF_EVEN);
			}
		}
	}

	/**
	 * ネイピア数を底とする対数計算を行います。
	 *
	 * @author 無線部開発班
	 * @since 2012年2月2日
	 */
	static final class Logarithm extends Taylor {
		public Logarithm(int scale) {
			super(scale);
		}

		/**
		 * 自然対数を計算します。
		 *
		 * @param val 対数を計算する値
		 *
		 * @return 対数
		 */
		public BigDecimal log(BigDecimal val) {
			if (val.compareTo(ZERO) <= 0) {
				throw new ArithmeticException("log(" + val + ")");
			}
			if (val.compareTo(BigDecimal.ONE) < 0) {
				return log(ONE.divide(val, scale, HALF_EVEN)).negate();
			}
			var n = 0;
			for (; val.compareTo(BigDecimal.ONE) >= 0; n++) {
				val = val.divide(Constants.TWO, scale, HALF_EVEN);
			}
			return Constants.LN_2.multiply(valueOf(n)).add(value(val));
		}

		@Override
		public BigDecimal value(BigDecimal arg) {
			BigDecimal log = ZERO, old = log;
			arg = arg.subtract(ONE);
			for (var i = 1; true; i++, old = log) {
				var add = arg.pow(i).divide(valueOf(i), scale + 4, HALF_EVEN);
				log = (i % 2 == 1) ? log.add(add) : log.subtract(add);
				if (old.compareTo(log) == 0) return log.setScale(scale, HALF_EVEN);
			}
		}
	}

	/**
	 * 平方根の計算を行うクラスです。
	 *
	 * @author 無線部開発班
	 * @since 2012年9月19日
	 */
	static final class SquareRoot extends Taylor {
		public SquareRoot(int scale) {
			super(scale);
		}

		@Override
		public BigDecimal value(BigDecimal arg) {
			var dval = Math.sqrt(arg.doubleValue());
			var x = valueOf(dval);
			var bd_2 = new BigDecimal(2);
			for (var nowscale = 16; nowscale <= scale; nowscale *= 2) {
				var divscale = Math.min(scale, nowscale * 2);
				x = x.subtract(x.pow(2).subtract(arg).divide(x.multiply(bd_2), divscale, HALF_EVEN));
			}
			return x;
		}
	}

	/**
	 * テイラー級数展開の計算を行う基底クラスです。
	 *
	 * @author 無線部開発班
	 * @since 2012年2月2日
	 */
	abstract static class Taylor {
		public final int scale;

		/**
		 * スケールを指定してこのオブジェクトを構築します。
		 *
		 * @param scale 計算精度
		 */
		public Taylor(int scale) {
			this.scale = scale;
		}

		/**
		 * テイラー級数展開の計算で用いる階乗を返します。
		 *
		 * @param n 階乗の終了値
		 *
		 * @return 階乗値
		 */
		public final BigDecimal factorial(int n) {
			if (n < 0) throw new ArithmeticException();
			if (n < 16) {
				var fact = 1;
				for (var i = 2; i <= n; i++) fact *= i;
				return valueOf(fact);
			}
			var result = ONE;
			for (var i = 2; i <= n; i++) {
				result = result.multiply(valueOf(i));
			}
			return result;
		}

		/**
		 * テイラー展開を求めます。
		 *
		 * @param arg 引数
		 */
		public abstract BigDecimal value(BigDecimal arg);
	}
}
