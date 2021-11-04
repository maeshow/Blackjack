## Blackjack

ブラックジャック

## Overview

コンピュータとのブラックジャック勝負

## Detail

- プレイヤーとコンピュータそれぞれに、カードが2枚配られる
- 「A」は「1 または 11」として計算でき、「J ～ K」は「10」として計算する
- カードの合計が「21」に近い方が勝ち
- ※マークの考慮や、同一数字は4枚まで、といった制限は入れない
- プレイヤーは、カードの合計が「21」未満の場合は、もう1枚引くか、引かないかを選ぶことができる
- ただし、「21」を超えた場合は、バーストとなる
- ディーラーは17以下の場合は必ずカードを引く
- 持ち点が10より大きい場合、Aは1として計算する
- ブラックジャック判定（A と 10 / J / Q / K の組み合わせ）を入れる
- コインを賭けれるようにする
    - 手持ち 100 コインでコインが 0 になると終了
    - １回あたり 10 コインを賭ける
    - ブラックジャックで勝ち: 30 コインの払い戻し
    - ブラックジャック以外で勝ち: 20 コインの払い戻し
    - 引き分け: 10 コイン（掛け金）の払い戻し
    - 負け: 払い戻しなし（賭け金没収）

### インターフェース

#### CUI

実行例

``` console
現在の手持ち： 100コイン
掛け金(10コイン)を賭けます。        

あなたに「4」が配られました。       
ディーラーに「9」が配られました。   
あなたに「A」が配られました。       
ディーラーに「3」が配られました。   

ディーラーの合計は 12 です。        
現在の合計は 15 です。
もう一枚カードを引きますか？(Y/N)： Y

あなたに「3」が配られました。
現在の合計は 18 です。       

もう一枚カードを引きますか？(Y/N)： N

ディーラーに「7」が配られました。
ディーラーの合計は 19 です。

あなたの負けです。
掛け金が没収されます。
現在の手持ち： 90コイン
```

## Structure Overview

- src/
    - App
        - String[] TRUMP_NUMBER
        - List<Integer> PLAYER_CARDS
        - List<Integer> DEALER_CARDS
        - enum Result { PLAYER_WIN, PLAYER_LOSE, PLAYER_BURST, DEALER_BURST, DRAW }
        - enum Type { PLAYER, DEALER }
        - main()
        - startGame()
        - endGame()
        - startRound()
        - endRound()
        - hasCoins()
        - bettingCoins()
        - giveInitialCard()
        - startTurn(Type type)
        - showResult(Result result)
        - showBettingResult(Result result)
        - giveCoins(int coin)
        - takeCoins(int coin)
        - giveCard(Type type)
        - addCard(Type type, int trumpNumber)
        - clearCard()
        - shouldDrawCard(Type type, int totalScore)
        - showTotalScore(Type type, int totalScore)
        - getScore(Type type)
        - getTrumpNumberByRandom()
        - convertToScore(int trumpNumber, int totalScore)
        - isFaceCard(int trumpNumber)
        - getPlayerInput()
        - isCorrectString(String str)
        - isYes(String str)
        - getGameResult()
        - isBust(int totalScore)
        - isPlayerWin(int playerScore, int dealerScore)
        - isDraw(int playerScore, int dealerScore)
        - isBlackjack(Type type)
        - hasFaceCard(Type type)
    - Messages