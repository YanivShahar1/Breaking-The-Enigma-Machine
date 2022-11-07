package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class invalidReflectorInputOutput extends Exception {
    private String reflectorID;
    private int inputOutput;
    private int ABCSize;
    public invalidReflectorInputOutput(String reflectorID, int inputOutput, int ABCSize) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Reflector " + reflectorID + " has input/output = "
//                + inputOutput + " but input/output should be >= 1 and <= " + ABCSize);
//        a.setHeaderText("Invalid Reflector Input / Output");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.inputOutput = inputOutput;
        this.ABCSize = ABCSize;
        this.reflectorID = reflectorID;
    }
    @Override
    public String getMessage() {
        return "Invalid Reflector Input / Output\nReflector " + reflectorID + " has input/output = "
                + inputOutput + " but input/output should be >= 1 and <= " + ABCSize;
    }
}