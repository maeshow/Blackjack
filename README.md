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

### インターフェース

#### CUI

実行例

``` console
あなたに「8」が配られました。
ディーラーに「A」が配られました。
あなたに「2」が配られました。
ディーラーに「5」が配られました。
ディーラーの合計は 15 です。
現在の合計は 10 です。
もう１枚カードを引きますか？(Y/N)：Y
あなたに「9」が配られました。
現在の合計は 19 です。
もう１枚カードを引きますか？(Y/N)：N
ディーラーに「J」が配られました。
ディーラーの合計は 15 です。
ディーラーに「3」が配られました。
ディーラーの合計は 18 です。
あなたの勝ちです
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
        - giveInitialCard()
        - startTurn(Type type)
        - showResult()
        - giveCard(Type type)
        - addCard(Type type, int trumpNumber)
        - clearCard()
        - shouldDrawCard(Type type, int totalScore)
        - showTotalScore(Type type, int totalScore)
        - getScore(Type type)
        - getTrumpNumberByRandom()
        - convertToScore(int trumpNumber, int totalScore)
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