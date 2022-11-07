package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class invalidABCLetter extends Exception {
    private List<String> badABCLetters;
    public invalidABCLetter(List<String> badABCLetters) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        if (badABCLetters.size()>1){
//            a.setContentText("The letters " + badABCLetters + " are not in ABC!");
//        }
//        else{
//            a.setContentText("The letter " + badABCLetters + " is not in ABC!");
//        }
//            a.setHeaderText("Invalid ABC Letter(s)");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.badABCLetters = new ArrayList<>(badABCLetters);
    }
    @Override
    public String getMessage() {
        if (badABCLetters.size() > 1) {
            return "Invalid ABC Letter(s)\nThe letters " + badABCLetters + " are not in ABC!";
        }
        else {
            return "Invalid ABC Letter(s)\nThe letter " + badABCLetters + " is not in ABC!";
        }
    }
}
