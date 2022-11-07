package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class tooFewRotors extends Exception {
    private int numOfRotors;
    public tooFewRotors(int numOfRotors) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Number of rotors supplied: " + numOfRotors + "\n"
//        + "Number of rotors needed: >= 2");
//        a.setHeaderText("Too Few Rotors");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.numOfRotors = numOfRotors;
    }
    @Override
    public String getMessage() {
        return "Too Few Rotors\nNumber of rotors supplied: " + numOfRotors + "\n"
                + "Number of rotors needed: >= 2";
    }
}
