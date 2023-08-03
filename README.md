# Interscheckin

交差点でのチェックインを補助するために作成された Swarm 利用者のための Android アプリケーション

## 機能

* 公式 Swarm アプリにある検索機能を省き、とにかく現在地周辺の Venue のみを表示することに徹しています
* 「運転モード」を有効化すると、「交差」が含まれる Venue を優先的に表示します
* 名前に「交差点」が含まれる Venue は背景が緑になって目立ちます
* アプリ別の言語設定がサポートされています（Android の App language 機能が使えます）
* 表示される Venue を長押しすることで、手軽にチェックインすることができます

## スクリーンショット

| ja  | en  | チェックイン |
| --- | --- | ---        |
| <img width="200" src="README.md.assets/screenshot_main_ja.png"> | <img width="200" src="README.md.assets/screenshot_main_en.png"> | <img width="200" src="README.md.assets/screenshot_main_checkin_created.png"> |

## インストール方法
https://github.com/private-yusuke/interscheckin/actions/runs/ から最新の workflow run のページを開き、Artifacts の一覧から APK ファイルが格納された zip ファイルをダウンロードしてください。

zip ファイルを解凍して得られた APK は `adb install` 等で端末に追加できるかもしれません（未確認。署名がないせいで追加できない可能性があります）。

うまくいかない場合は、手元に Android Studio をインストールし、自前でビルドとインストールをしてください。

## 使い方

初回起動時には、位置情報取得の許可（†1）と、Foursquare の API キー、OAuth token といった認証情報の設定（†2）をする必要があります。

認証情報は、自分で生成する必要があります。以下に参考となる資料へのリンクを掲載します。

* API キー
    * https://location.foursquare.com/developer/reference/places-api-get-started
* OAuth token
    * https://location.foursquare.com/developer/reference/authentication-v2

これらの認証情報を生成できたら、初回起動時に表示される以下の画面にそれぞれ入力します。

| 位置情報取得の許可をする画面（†1） | 認証情報を入力する画面（†2） |
| --- | --- |
| <img width="200" src="README.md.assets/screenshot_location_request.png"> | <img width="200" src="README.md.assets/screenshot_set_credentials_first.png"> |

### より便利な利用法

Interscheckin は Foursquare が提供している v3 API を利用していますが、これは公式アプリが表示する Venue の一覧とは異なるリストを返すものです。
そのため、公式アプリには表示される Venue が Interscheckin では表示されない現象がよく発生します。

この問題を軽減するために、履歴一覧画面から履歴をいくつか表示させてみることができます。 
表示された Venue が Interscheckin で今後チェックインする際に選択できるようになります。

注意点：履歴に基いた Venue の追加表示は、利用者自身が履歴を表示しなければ追加されません。
定期的に履歴一覧画面を開き、直近で初めてチェックインした Venue をこまめにアプリに追加することをおすすめします。

こちらも参照: https://github.com/private-yusuke/interscheckin/pull/51
