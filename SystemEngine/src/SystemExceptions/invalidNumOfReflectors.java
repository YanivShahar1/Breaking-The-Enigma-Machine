package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class invalidNumOfReflectors extends Exception {
    private int numOfReflectors;
    public invalidNumOfReflectors(int numOfReflectors) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Number of reflectors in XML must be (1 - 5) while supplied "
//                + numOfReflectors + " reflectors.");
//        a.setHeaderText("Invalid Num Of Reflectors");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.numOfReflectors = numOfReflectors;
    }
    @Override
    public String getMessage() {
        return "Invalid Num Of Reflectors\nNumber of reflectors in XML must be (1 - 5) while supplied "
                + numOfReflectors + " reflectors.";
    }
}