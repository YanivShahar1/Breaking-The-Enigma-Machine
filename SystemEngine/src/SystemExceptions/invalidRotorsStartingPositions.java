package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class invalidRotorsStartingPositions extends Exception {
    private int entered;
    private int rotorsCount;
    public invalidRotorsStartingPositions(int entered, int rotorsCount) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Num of positions entered: " + entered + "\n"
//        + "Num of positions needed: " + rotorsCount + "\n"
//        + "Make sure positions entered = positions needed!");
//        a.setHeaderText("Invalid Rotors Starting Positions");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.rotorsCount = rotorsCount;
        this.entered = entered;
    }
    @Override
    public String getMessage() {
        return "Invalid Rotors Starting Positions\nNum of positions entered: " + entered + "\n"
                + "Num of positions needed: " + rotorsCount + "\n"
                + "Make sure positions entered = positions needed!";
    }
}

