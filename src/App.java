import java.util.Random;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static final Scanner STDIN = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    private static final String INPUT_YES[] = { "Y", "y" };
    private static final String INPUT_NO[] = { "N", "n" };

    private static final String[] TRUMP_NUMBER = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
    private static final int ACE_INDEX = 0;
    private static final int TEN_INDEX = 9;
    private static final int JACK_INDEX = 10;
    private static final int QUEEN_INDEX = 11;
    private static final int KING_INDEX = 12;

    private static final List<Integer> PLAYER_CARDS = new ArrayList<>();
    private static final List<Integer> DEALER_CARDS = new ArrayList<>();

    private static final int BUST_NUMBER = 21;
    private static final int DEALER_DRAW_CARD_LIMIT = 17;

    private static final int ONE_POINT = 1;
    private static final int TEN_POINT = 10;
    private static final int ELEVEN_POINT = 11;
    private static final int MAX_A_AS_ELEVEN = 10;

    private static final int GIVE_CARD_TIMES_IN_FIRST = 2;

    private static int COIN = 100;
    private static final int ENTRY_COIN = 10;
    private static final int PRIZE_COIN = 20;
    private static final int BLACKJACK_COIN = 30;
    private static final int DRAW_COIN = 10;
    private static final int END_GAME_COIN_LIMIT = 0;

    private static enum Result {
        PLAYER_WIN, PLAYER_BLACKJACK, PLAYER_LOSE, PLAYER_BUST, DEALER_BURST, DRAW
    }

    private static enum Type {
        PLAYER, DEALER
    }

    public static void main(String[] args) {
        startGame();
        endGame();
    }

    public static void startGame() {
        while (hasCoins()) {
            startRound();
            endRound();
        }
    }

    public static void endGame() {
        showWithNewLine(Messages.NOT_ENOUGH_COIN);
        showWithNewLine(Messages.END_GAME);
        STDIN.close();
    }

    private static void startRound() {
        bettingCoins();
        giveInitialCard();
        startTurn(Type.PLAYER);
        startTurn(Type.DEALER);
    }

    private static void endRound() {
        Result result = getGameResult();
        showResult(result);
        showBettingResult(result);
        clearCard();
    }

    private static boolean hasCoins() {
        return END_GAME_COIN_LIMIT < COIN;
    }

    private static void bettingCoins() {
        showFormattedMessage(Messages.COIN_INFO, COIN);
        showFormattedMessage(Messages.BETTING_COIN, ENTRY_COIN);
        showNewLine();
        takeCoins(ENTRY_COIN);
    }

    private static void giveInitialCard() {
        for (int i = 0; i < GIVE_CARD_TIMES_IN_FIRST; i++) {
            giveCard(Type.PLAYER);
            giveCard(Type.DEALER);
        }
        showNewLine();
        showTotalScore(Type.DEALER, getScore(Type.DEALER));
        showTotalScore(Type.PLAYER, getScore(Type.PLAYER));
    }

    private static void startTurn(Type type) {
        int totalScore = getScore(type);
        while (shouldDrawCard(type, totalScore)) {
            giveCard(type);
            totalScore = getScore(type);
            showTotalScore(type, totalScore);
            showNewLine();
        }
    }

    private static void showResult(Result result) {
        switch (result) {
        case PLAYER_BUST:
            showWithNewLine(Messages.PLAYER_BUST);
        case PLAYER_LOSE:
            showWithNewLine(Messages.LOSE);
            break;
        case PLAYER_BLACKJACK:
            showWithNewLine(Messages.BLACKJACK);
            showWithNewLine(Messages.WIN);
            break;
        case DEALER_BURST:
            showWithNewLine(Messages.DEALER_BUST);
        case PLAYER_WIN:
            showWithNewLine(Messages.WIN);
            break;
        case DRAW:
            showWithNewLine(Messages.DRAW);
            break;
        }
    }

    private static void showBettingResult(Result result) {
        switch (result) {
        case PLAYER_BUST:
        case PLAYER_LOSE:
            showWithNewLine(Messages.TAKE_COIN);
            break;
        case PLAYER_BLACKJACK:
            showFormattedMessage(Messages.GIVE_COIN, BLACKJACK_COIN);
            break;
        case DEALER_BURST:
        case PLAYER_WIN:
            showFormattedMessage(Messages.GIVE_COIN, PRIZE_COIN);
            break;
        case DRAW:
            showFormattedMessage(Messages.GIVE_COIN, DRAW_COIN);
            break;
        }
    }

    private static void giveCoins(int coin) {
        COIN += coin;
    }

    private static void takeCoins(int coin) {
        COIN -= coin;
    }

    private static void giveCard(Type type) {
        int trumpNumber = getTrumpNumberByRandom();
        switch (type) {
        case PLAYER:
            addCard(type, trumpNumber);
            showFormattedMessage(Messages.PLAYER_GIVE_CARD, TRUMP_NUMBER[trumpNumber]);
            break;
        case DEALER:
            addCard(type, trumpNumber);
            showFormattedMessage(Messages.DEALER_GIVE_CARD, TRUMP_NUMBER[trumpNumber]);
            break;
        }
    }

    private static void addCard(Type type, int trumpNumber) {
        switch (type) {
        case PLAYER:
            PLAYER_CARDS.add(trumpNumber);
            break;
        case DEALER:
            DEALER_CARDS.add(trumpNumber);
        }
    }

    private static void clearCard() {
        PLAYER_CARDS.clear();
        DEALER_CARDS.clear();
    }

    private static boolean shouldDrawCard(Type type, int totalScore) {
        switch (type) {
        case PLAYER:
            if (!isBust(totalScore)) {
                return getPlayerInput();
            }
            return false;
        case DEALER:
            return totalScore <= DEALER_DRAW_CARD_LIMIT;
        default:
            return false;
        }
    }

    private static void showTotalScore(Type type, int totalScore) {
        switch (type) {
        case PLAYER:
            showFormattedMessage(Messages.PLAYER_TOTAL_TRUMP_NUMBER, totalScore);
            break;
        case DEALER:
            showFormattedMessage(Messages.DEALER_TOTAL_TRUMP_NUMBER, totalScore);
            break;
        }
    }

    private static int getScore(Type type) {
        int totalScore = 0;
        switch (type) {
        case PLAYER:
            for (int trumpNumber : PLAYER_CARDS) {
                totalScore += convertToScore(trumpNumber, totalScore);
            }
            break;
        case DEALER:
            for (int trumpNumber : DEALER_CARDS) {
                totalScore += convertToScore(trumpNumber, totalScore);
            }
            break;
        }
        return totalScore;
    }

    private static int getTrumpNumberByRandom() {
        return RANDOM.nextInt(TRUMP_NUMBER.length);
    }

    private static int convertToScore(int trumpNumber, int totalScore) {
        switch (trumpNumber) {
        case ACE_INDEX:
            if (totalScore <= MAX_A_AS_ELEVEN) {
                return ELEVEN_POINT;
            }
            return ONE_POINT;
        default:
            if (isFaceCard(trumpNumber)) {
                return TEN_POINT;
            }
            return trumpNumber + 1;
        }
    }

    private static boolean isFaceCard(int trumpNumber) {
        return trumpNumber == JACK_INDEX || trumpNumber == QUEEN_INDEX || trumpNumber == KING_INDEX;
    }

    private static boolean getPlayerInput() {
        showWithoutNewLine(Messages.WAITING_INPUT);
        String input = STDIN.next();
        showNewLine();
        if (!isCorrectString(input)) {
            showWithNewLine(Messages.ENTER_Y_OR_N_WARN);
            showNewLine();
            return getPlayerInput();
        }
        return isYes(input);
    }

    private static boolean isCorrectString(String str) {
        for (String inputYes : INPUT_YES) {
            if (str.equals(inputYes)) {
                return true;
            }
        }
        for (String inputNo : INPUT_NO) {
            if (str.equals(inputNo)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isYes(String str) {
        for (String inputYes : INPUT_YES) {
            if (str.equals(inputYes)) {
                return true;
            }
        }
        return false;
    }

    private static Result getGameResult() {
        int playerScore = getScore(Type.PLAYER);
        int dealerScore = getScore(Type.DEALER);

        if (isBust(playerScore)) {
            return Result.PLAYER_BUST;
        }
        if (isBlackjack(Type.PLAYER)) {
            giveCoins(BLACKJACK_COIN);
            return Result.PLAYER_BLACKJACK;
        }
        if (isBust(dealerScore)) {
            giveCoins(PRIZE_COIN);
            return Result.DEALER_BURST;
        }
        if (isPlayerWin(playerScore, dealerScore)) {
            giveCoins(PRIZE_COIN);
            return Result.PLAYER_WIN;
        }
        if (isDraw(playerScore, dealerScore)) {
            giveCoins(DRAW_COIN);
            return Result.DRAW;
        }
        return Result.PLAYER_LOSE;
    }

    private static boolean isBust(int totalScore) {
        return BUST_NUMBER < totalScore;
    }

    private static boolean isPlayerWin(int playerScore, int dealerScore) {
        return dealerScore < playerScore;
    }

    private static boolean isDraw(int playerScore, int dealerScore) {
        if (isBlackjack(Type.DEALER)) {
            return false;
        }
        return playerScore == dealerScore;
    }

    private static boolean isBlackjack(Type type) {
        switch (type) {
        case PLAYER:
            return PLAYER_CARDS.contains(ACE_INDEX) && (PLAYER_CARDS.contains(TEN_INDEX) || hasFaceCard(type));
        case DEALER:
            return DEALER_CARDS.contains(ACE_INDEX) && (DEALER_CARDS.contains(TEN_INDEX) || hasFaceCard(type));
        default:
            return false;
        }
    }

    private static boolean hasFaceCard(Type type) {
        switch (type) {
        case PLAYER:
            return PLAYER_CARDS.contains(JACK_INDEX) || PLAYER_CARDS.contains(QUEEN_INDEX)
                    || PLAYER_CARDS.contains(KING_INDEX);
        case DEALER:
            return DEALER_CARDS.contains(JACK_INDEX) || DEALER_CARDS.contains(QUEEN_INDEX)
                    || DEALER_CARDS.contains(KING_INDEX);
        default:
            return false;
        }
    }

    private static void showWithNewLine(String message) {
        System.out.println(message);
    }

    private static void showWithoutNewLine(String message) {
        System.out.printf(message);
    }

    private static void showNewLine() {
        System.out.println();
    }

    private static void showFormattedMessage(String message, int a) {
        System.out.format(message, a);
    }

    private static void showFormattedMessage(String message, String a) {
        System.out.format(message, a);
    }
}
