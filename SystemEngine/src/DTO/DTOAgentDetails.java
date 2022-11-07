package DTO;

public class DTOAgentDetails {

    private String agentName;
    private int numOfThreads;
    private int numOfMissionPerRequest;

    public DTOAgentDetails(String agentName, int numOfThreads,  int numOfMissionPerRequest){
       this.agentName = agentName;
       this.numOfThreads = numOfThreads;
       this.numOfMissionPerRequest = numOfMissionPerRequest;

    }

    public String getAgentName() {
        return agentName;
    }

    public int getNumOfThreads() {
        return numOfThreads;
    }

    public int getNumOfMissionPerRequest() {
        return numOfMissionPerRequest;
    }
}
