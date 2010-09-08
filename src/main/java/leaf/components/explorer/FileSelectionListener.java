/**************************************************************************************
月白プロジェクト Java 拡張ライブラリ 開発コードネーム「Leaf」
始動：2010年6月8日
バージョン：Edition 1.0
開発言語：Pure Java SE 6
開発者：東大アマチュア無線クラブ2010年度新入生 川勝孝也
***************************************************************************************
「Leaf」は「月白エディタ」1.2以降及び「Jazlog(ZLOG3.0)」用に開発されたライブラリです
**************************************************************************************/
package leaf.components.explorer;

import java.io.File;
/**
*エクスプローラツリーでのファイル選択イベントを受信するリスナーです。
*/
public interface FileSelectionListener{
	
	/**
	*ファイル選択時に呼び出されます。
	*@param file 選択されたファイル
	*/
	public void fileSelected(File file);
}