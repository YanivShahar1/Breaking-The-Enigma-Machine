package DTO;

import DecryptionManager.Level;

import java.io.Serializable;

public class DTOContestData implements Serializable {
    private String battleFieldName;
    private String uboatName;
    private String status;
    private String level;
    private Integer numOfRegisteredAllies;
    private Integer numOfAlliesNeeded;
    private ContestStatus contestStatus;

    public DTOContestData(String battleFieldName, String uboatName, ContestStatus status, Level level,
                          int numOfRegisteredAllies, int numOfAlliesNeeded  ){
        this.battleFieldName = battleFieldName;
        this.uboatName = uboatName;
        this.status = statusToString(status);
        this.level = levelToString(level);
        this.numOfRegisteredAllies = numOfRegisteredAllies;
        this.numOfAlliesNeeded = numOfAlliesNeeded;
        this.contestStatus = status;
    }
    private String statusToString(ContestStatus status){
        if(status == ContestStatus.WAITING){
            return "Waiting";
        } else if (status == ContestStatus.RUNNING) {
            return "Running";
        }
        else {
            return "Finished";
        }
    }

    private String levelToString(Level level){
        if(level.equals(Level.EASY)){
            return "Easy";
        } else if (level.equals(Level.MEDIUM)) {
            return "Moderate";
        } else if (level.equals(Level.HARD)) {
            return "Hard";
        } else if (level.equals(Level.IMPOSSIBLE)) {
            return "Impossible";
        }
        else {
            return "";
        }
    }
    public String getStatus() {
        return status;
    }
    public ContestStatus getStatusAsContestStatus() {
        return contestStatus;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public int getNumOfAlliesNeeded() {
        return numOfAlliesNeeded;
    }

    public String getLevel() {
        return level;
    }

    public String getUboatName() {
        return uboatName;
    }

    public int getNumOfRegisteredAllies() {
        return numOfRegisteredAllies;
    }

    public void setStatus(ContestStatus status) {
        this.status = statusToString(status);
    }

    public void setNumOfRegisteredAllies(int numOfRegisteredAllies) {
        this.numOfRegisteredAllies = numOfRegisteredAllies;
    }
}
