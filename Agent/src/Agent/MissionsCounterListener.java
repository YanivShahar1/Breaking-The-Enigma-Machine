package Agent;

public class MissionsCounterListener {

    private int numOfMissionsPerRequest;
    private int missionsCounter;
    private int missionsDone;
    private boolean isFinishedAllMissionPerOneRequest;
    private boolean[] contestFinished;
    private int totalMissionPull;
    private int numOfCandidates;
    public MissionsCounterListener(boolean[] contestFinished){
        missionsCounter = 0;
        isFinishedAllMissionPerOneRequest = false;
        numOfMissionsPerRequest = 0;
        this.contestFinished = contestFinished;
        totalMissionPull = 0;
        missionsDone = 0;
        numOfCandidates = 0;
    }

    public int getNumOfCandidates() {
        return numOfCandidates;
    }

    public int getMissionsDone() {
        return missionsDone;
    }

    public int getTotalMissionPull() {
        return totalMissionPull;
    }

    public void setTotalMissionPull() {
        totalMissionPull += this.numOfMissionsPerRequest;
    }
    public synchronized void setNumOfMissionsPerRequest(int numOfMissionsPerRequest) {
        this.numOfMissionsPerRequest = numOfMissionsPerRequest;
    }
    public void increaseByOneMissionsDone() {
        this.missionsDone++;
    }
    public synchronized int getNumOfMissionsPerRequest() {
        return numOfMissionsPerRequest;
    }

    public synchronized void increaseByOneMissionsCounter() {
        this.missionsCounter++;

        if (this.missionsCounter == this.numOfMissionsPerRequest && numOfMissionsPerRequest != 0) {
            this.isFinishedAllMissionPerOneRequest = true;
            notifyAll();
        }
    }
    public void increaseByOneNumOfCandidates() {
        numOfCandidates++;
    }
    public synchronized void setContestFinished(boolean[] contestFinished) {
        this.contestFinished = contestFinished;
        notifyAll();
    }

    public synchronized void resetMissionsCounter() {
        this.isFinishedAllMissionPerOneRequest = false;
        this.missionsCounter = 0;
        numOfMissionsPerRequest = 0;
    }
    public void resetCounterListener() {
        numOfMissionsPerRequest = 0;
        missionsCounter = 0;
        missionsDone = 0;
        isFinishedAllMissionPerOneRequest = false;
        //contestFinished[0] = ;
        totalMissionPull = 0;
        numOfCandidates = 0;
    }
    public synchronized boolean waitUntilFinishAllMissions() throws InterruptedException {
        while (!isFinishedAllMissionPerOneRequest && !contestFinished[0] && numOfMissionsPerRequest != 0) {

            wait();

        }
        return numOfMissionsPerRequest != 0;
    }
}
