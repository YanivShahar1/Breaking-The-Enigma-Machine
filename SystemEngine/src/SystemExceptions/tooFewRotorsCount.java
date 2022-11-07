package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class tooFewRotorsCount extends Exception {
    private int rotorsCount;
    public tooFewRotorsCount(int rotorsCount) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("rotorsCount supplied: " + rotorsCount + "\n"
//        + "rotorsCount needed: >= 2");
//        a.setHeaderText("Too Few Rotors-Count");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.rotorsCount = rotorsCount;
    }
    @Override
    public String getMessage() {
        return "Too Few Rotors-Count\nrotorsCount supplied: " + rotorsCount + "\n"
                + "rotorsCount needed: >= 2";
    }
}

