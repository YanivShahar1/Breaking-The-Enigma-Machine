package DTO;


public class DTOAllyTeamDetails {
    private String allyName; // team name == ally name
    private int numOfAgents;
    private int missionSize;

    public DTOAllyTeamDetails(String allyName, int numOfAgents, int missionSize){
        this.allyName = allyName;
        this.numOfAgents = numOfAgents;
        this.missionSize = missionSize;
    }

    public String getAllyName() {
        return allyName;
    }

    public int getNumOfAgents() {
        return numOfAgents;
    }

    public int getMissionSize() {
        return missionSize;
    }

}
