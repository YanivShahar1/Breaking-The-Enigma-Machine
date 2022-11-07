package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class doubleLetterInPlugin extends Exception {
    private char letter;
    public doubleLetterInPlugin(char letter) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("The character '" + letter + "' appears more than once in plugins!");
//        a.setHeaderText("Double Letter In Plugin");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.letter = letter;
    }
    @Override
    public String getMessage() {
        return "Double Letter In Plugin\nThe character '" + letter + "' appears more than once in plugins!";
    }
}
