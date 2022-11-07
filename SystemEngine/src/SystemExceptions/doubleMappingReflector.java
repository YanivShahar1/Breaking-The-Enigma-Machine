package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class doubleMappingReflector extends Exception {
    private String reflectorID;
    private int inputOutput;
    public doubleMappingReflector(String reflectorID, int inputOutput) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Rotor " + reflectorID + " has double mapping issue: the input/output "
//                + inputOutput + " appears more than once!");
//        a.setHeaderText("Double Mapping Reflector");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.reflectorID = reflectorID;
        this.inputOutput = inputOutput;
    }
    @Override
    public String getMessage() {
        return "Double Mapping Reflector\nReflector " + reflectorID + " has double mapping issue: the input/output "
                + inputOutput + " appears more than once!";
    }
}