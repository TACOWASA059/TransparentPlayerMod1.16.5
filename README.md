# TransparentPlayerMod1.16.5
プレイヤーの透明度を指定できるMOD
forge 36.2.39

## プレイヤーNBT
ForgeCaps."transparentplayermod:alpha"を追加(読み取り専用)
int:0-255
```
/data get entity @s ForgeCaps."transparentplayermod:alpha"
```
## コマンド
アルファ値を取得
```
/transparent get <Player>
```
アルファ値の設定
```
/transparent set <Player> <value>
```
アルファ値のリセット
```
/transparent reset <Player> <value>
```
アルファ値の加算
```
/transparent add <Player> <value>
```
アルファ値の減算
```
/transparent subtract <Player> <value>
```

## 注意
- セレクター指定は可能
- シングルの場合は/transparentコマンドはset以外機能しない
