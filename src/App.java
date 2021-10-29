import java.util.Random;
import java.util.Scanner;

public class App {
    private static final Scanner STDIN = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    private static final String INPUT_YES = "Y";
    private static final String INPUT_NO = "N";

    private static final String[] TRUMP_NUMBER = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
    private static final int ACE_INDEX = 0;
    private static final int JACK_INDEX = 10;
    private static final int QUEEN_INDEX = 11;
    private static final int KING_INDEX = 12;

    private static final int[] TOTAL_SCORE = new int[2];
    private static final int PLAYER_INDEX = 0;
    private static final int DEALER_INDEX = 1;

    private static final int BUST_NUMBER = 21;
    private static final int DEALER_DRAW_CARD_LIMIT = 17;

    private static final int ONE_POINT = 1;
    private static final int TEN_POINT = 10;
    private static final int ELEVEN_POINT = 11;
    private static final int MAX_A_AS_ELEVEN = 10;

    private static enum Result {
        PLAYER_WIN, PLAYER_LOSE, PLAYER_BUST, DEALER_BURST, DRAW
    }

    private static enum Type {
        PLAYER, DEALER
    }

    public static void main(String[] args) {
        startGame();
        STDIN.close();
    }

    public static void startGame() {
        giveInitialCard();
        startTurn(Type.PLAYER);
        startTurn(Type.DEALER);
        showResult();
    }

    private static void giveInitialCard() {
        giveCard(Type.PLAYER);
        giveCard(Type.DEALER);
        giveCard(Type.PLAYER);
        giveCard(Type.DEALER);
        showNewLine();
        showTotalScore(Type.DEALER);
        showTotalScore(Type.PLAYER);
    }

    private static void startTurn(Type type) {
        while (shouldDrawCard(type)) {
            giveCard(type);
            showTotalScore(type);
            showNewLine();
        }
    }

    private static void showResult() {
        switch (getGameResult()) {
        case PLAYER_BUST:
            showWithNewLine(Messages.PLAYER_BUST);
        case PLAYER_LOSE:
            showWithNewLine(Messages.LOSE);
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

    private static void giveCard(Type type) {
        int trumpNumber = getTrumpNumberByRandom();
        switch (type) {
        case PLAYER:
            addScore(PLAYER_INDEX, convertToScore(trumpNumber, TOTAL_SCORE[PLAYER_INDEX]));
            showFormattedMessage(Messages.PLAYER_GIVE_CARD, TRUMP_NUMBER[trumpNumber]);
            break;
        case DEALER:
            addScore(DEALER_INDEX, convertToScore(trumpNumber, TOTAL_SCORE[DEALER_INDEX]));
            showFormattedMessage(Messages.DEALER_GIVE_CARD, TRUMP_NUMBER[trumpNumber]);
            break;
        }
    }

    private static boolean shouldDrawCard(Type type) {
        switch (type) {
        case PLAYER:
            if (!isBust(TOTAL_SCORE[PLAYER_INDEX])) {
                return getPlayerInput();
            }
            return false;
        case DEALER:
            return TOTAL_SCORE[DEALER_INDEX] <= DEALER_DRAW_CARD_LIMIT;
        default:
            return false;
        }
    }

    private static void showTotalScore(Type type) {
        switch (type) {
        case PLAYER:
            showFormattedMessage(Messages.PLAYER_TOTAL_TRUMP_NUMBER, TOTAL_SCORE[PLAYER_INDEX]);
            break;
        case DEALER:
            showFormattedMessage(Messages.DEALER_TOTAL_TRUMP_NUMBER, TOTAL_SCORE[DEALER_INDEX]);
            break;
        }
    }

    private static void addScore(int index, int trumpNumber) {
        TOTAL_SCORE[index] += trumpNumber;
    }

    private static int getTrumpNumberByRandom() {
        return RANDOM.nextInt(TRUMP_NUMBER.length);
    }

    private static int convertToScore(int trumpNumber, int totalScore) {
        switch (trumpNumber) {
        case JACK_INDEX:
        case QUEEN_INDEX:
        case KING_INDEX:
            return TEN_POINT;
        case ACE_INDEX:
            if (totalScore <= MAX_A_AS_ELEVEN) {
                return ELEVEN_POINT;
            }
            return ONE_POINT;
        default:
            return trumpNumber + 1;
        }
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
        return str.equals(INPUT_YES) || str.equals(INPUT_NO);
    }

    private static boolean isYes(String str) {
        return str.equals(INPUT_YES);
    }

    private static Result getGameResult() {
        if (isBust(TOTAL_SCORE[PLAYER_INDEX])) {
            return Result.PLAYER_BUST;
        }
        if (isBust(TOTAL_SCORE[DEALER_INDEX])) {
            return Result.DEALER_BURST;
        }
        if (isPlayerWin()) {
            return Result.PLAYER_WIN;
        }
        if (isDraw()) {
            return Result.DRAW;
        }
        return Result.PLAYER_LOSE;
    }

    private static boolean isBust(int totalScore) {
        return BUST_NUMBER < totalScore;
    }

    private static boolean isPlayerWin() {
        return TOTAL_SCORE[DEALER_INDEX] < TOTAL_SCORE[PLAYER_INDEX];
    }

    private static boolean isDraw() {
        return TOTAL_SCORE[DEALER_INDEX] == TOTAL_SCORE[PLAYER_INDEX];
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
