package SystemExceptions;

public class BattleFieldNameIsAlreadyExists extends Exception {
    public BattleFieldNameIsAlreadyExists() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("ABC is odd! ABC should contain even number of letters!");
//        a.setHeaderText("ABC Is Odd");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }

    @Override
    public String getMessage() {
        return "BattleField Name Is Already Exists\nUse different battlefield name!";
    }
}
