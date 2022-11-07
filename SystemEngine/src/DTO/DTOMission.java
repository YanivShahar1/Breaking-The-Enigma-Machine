package DTO;

import Machine.Enigma;

import java.util.Set;

public class DTOMission {
    private Enigma enigma;
    private int missionSize;
    private String message;
    private int whichMission;
    private Set<String> dictionary;
    private String allyName;
    private ContestStatus contestStatus;

    public DTOMission(Enigma newEnigma, String allyName, int missionSize, String message,
                      int whichMission, Set<String> dictionary, ContestStatus contestStatus) throws CloneNotSupportedException {
        this.enigma = newEnigma.clone();
        this.allyName = allyName;
        this.missionSize = missionSize;
        this.message = message;
        this.whichMission = whichMission;
        this.dictionary = dictionary;
        this.contestStatus=contestStatus;
    }

    public String getAllyName() {
        return allyName;
    }

    public ContestStatus getContestStatus() {
        return contestStatus;
    }

    public Enigma getEnigma() {
        return enigma;
    }

    public int getMissionSize() {
        return missionSize;
    }

    public String getMessage() {
        return message;
    }

    public int getWhichMission() {
        return whichMission;
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    @Override
    public String toString() {
        return "DTOMission{" +
                "enigma=" + enigma +
                ", missionSize=" + missionSize +
                ", message='" + message + '\'' +
                ", whichMission=" + whichMission +
                ", dictionary=" + dictionary +
                ", allyName='" + allyName + '\'' +
                ", contestStatus=" + contestStatus +
                '}';
    }
}
