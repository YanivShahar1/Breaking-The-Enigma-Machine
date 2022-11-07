package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class rotorsCountBiggerThanExist extends Exception {
    private int rotorsCount;
    private int rotorsExist;
    public rotorsCountBiggerThanExist(int rotorsCount, int rotorsExist) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("rotorsCount supplied: " + rotorsCount + "\n"
//        + "rotors exist: " + rotorsExist + "\n"
//        + "rotorsCount should be <= rotors exist");
//        a.setHeaderText("Rotors Count Bigger Than Exist");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.rotorsCount = rotorsCount;
        this.rotorsExist = rotorsExist;
    }
    @Override
    public String getMessage() {
        return "Rotors Count Bigger Than Exist\nrotorsCount supplied: " + rotorsCount + "\n"
                + "rotors exist: " + rotorsExist + "\n"
                + "rotorsCount should be <= rotors exist";
    }
}