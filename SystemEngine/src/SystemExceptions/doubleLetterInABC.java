package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class doubleLetterInABC extends Exception {
    private char letter;
    public doubleLetterInABC(char letter) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("The character '" + letter + "' appears more than once in ABC!");
//        a.setHeaderText("Double Letter In ABC");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.letter = letter;
    }
    @Override
    public String getMessage() {
        return "Double Letter In ABC\nThe character '" + letter + "' appears more than once in ABC!";
    }
}

