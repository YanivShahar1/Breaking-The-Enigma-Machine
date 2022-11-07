package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class invalidAmountOfRotors extends Exception {
    private int entered;
    private int rotorsCount;
    public invalidAmountOfRotors(int entered, int rotorsCount) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Num of rotors entered: " + entered + "\n"
//                + "Num of rotors needed: " + rotorsCount + "\n"
//                + "Make sure rotors entered = rotors needed!\n"
//                + "The IDs should be split with " + "," + " !!!");
//        a.setHeaderText("Invalid Amount Of Rotors");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.entered = entered;
        this.rotorsCount = rotorsCount;
    }
    @Override
    public String getMessage() {
        return "Invalid Amount Of Rotors\nNum of rotors entered: " + entered + "\n"
                + "Num of rotors needed: " + rotorsCount + "\n"
                + "Make sure rotors entered = rotors needed!\n"
                + "The IDs should be split with " + "," + " !!!";
    }
}
