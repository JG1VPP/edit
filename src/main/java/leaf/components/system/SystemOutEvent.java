/**************************************************************************************
月白プロジェクト Java 拡張ライブラリ 開発コードネーム「Leaf」
始動：2010年6月8日
バージョン：Edition 1.0
開発言語：Pure Java SE 6
開発者：東大アマチュア無線クラブ2010年度新入生 川勝孝也
***************************************************************************************
「Leaf」は「月白エディタ」1.2以降及び「Jazlog(ZLOG3.0)」用に開発されたライブラリです
**************************************************************************************/
package leaf.components.system;
import java.util.EventObject;
/**
*標準出力があった時{@link LeafSystemOutArea}によって通知されるイベントです。
*@author 東大アマチュア無線クラブ
*@since Leaf 1.0 作成：2010年7月11日
*@see SystemOutListener
*/
public class SystemOutEvent extends EventObject{
	
	private boolean type;
	/**標準出力のイベントを表します。*/
	public static final boolean OUT = true;
	/**エラー出力のイベントを表します。*/
	public static final boolean ERR = false;
	/**
	*発生源を指定してイベントを生成します。
	*@param source イベントの発生源
	*/
	public SystemOutEvent(Object source,boolean type){
		super(source);
		this.type = type;
	}
	/**
	*イベントの種類を返します。<br>
	*イベントは、{@link #OUT}もしくは{@link #ERR}の２種類です。
	*@return イベントの種別
	*/
	public boolean getType(){
		return type;
	}
}