# TransparentPlayerMod1.16.5
プレイヤーの透明度を指定できるMOD
forge 36.2.39

## コマンド
### データを直接変更
アルファ値を取得
```
/transparent data get <Players>
```
アルファ値の設定
```
/transparent data set <Players> <value>
```
アルファ値のリセット(255にする)
```
/transparent data reset <Players>
```
アルファ値の加算
```
/transparent data add <Players> <value>
```
アルファ値の減算
```
/transparent data subtract <Players> <value>
```
### スコアボード関連
スコアボード<objectives>の値を使って変更
```
/transparent scoreboard <Players> set_from <objectives> 
```
スコアボード<objectives>の値を使ってスコアを設定
```
/transparent scoreboard <Players> set_score <objectives> 
```
## プレイヤーNBT
int:0-255

- 0:透明
- 255:不透明

ForgeCaps."transparentplayermod:alpha"を追加(読み取り専用)
```
/data get entity @s ForgeCaps."transparentplayermod:alpha"
```
セレクターの指定
```
/execute as @a[nbt={ForgeCaps:{"transparentplayermod:alpha":255}}] run transparent get @s
```
## 注意
- セレクター指定は可能
- クライアントとサーバー両方に導入必須
