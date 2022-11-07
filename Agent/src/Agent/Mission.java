package Agent;

import AgentManager.AgentManagerController;
import DTO.ContestStatus;
import DTO.DTOCodeConfigurationDescriptor;
import DTO.DTODecryptionCandidate;
import Machine.Enigma;
import Machine.Rotor;

import java.util.*;

public class Mission implements Runnable{
    private Enigma enigma;
    private Enigma originalEnigma;
    private int missionSize;
    private int id;
    private String message;
    private Set<String> dictionary;
    private Map<String, Integer> ABC;
    private List<String> ABCList;
    private DecryptionsCandidatesQueue optionalDecryption;

    private Set<String> allDecryptionsTillNow;
    private String[] stateOfThreadPool;
    MissionsCounterListener missionsCounterListener;
    private String agentName;
    private String allyName;
    private AgentManagerController manager;
    public Mission(
            AgentManagerController manager, Enigma enigma, String allyName, int missionSize, String message, String agentName,
            Set<String> dictionary, DecryptionsCandidatesQueue decryptionsCandidatesQueue,
            Set<String> allDecryptionsTillNow,
            String[] stateOfThreadPool, MissionsCounterListener missionsCounterListener)
            throws CloneNotSupportedException {

        this.enigma = enigma.clone();
        this.allyName = allyName;
        this.originalEnigma = enigma.clone();
        this.missionSize = missionSize;
        this.message = message;
        this.dictionary = new HashSet<>(dictionary);
        this.ABC = new LinkedHashMap<>(this.enigma.getABC());
        this.ABCList = new ArrayList<>();
        this.ABCList.add(null); // dummy
        this.ABCList.addAll(ABC.keySet());
        this.allDecryptionsTillNow = allDecryptionsTillNow;
        this.stateOfThreadPool = stateOfThreadPool;
        this.missionsCounterListener = missionsCounterListener;
        this.agentName = agentName;
        this.optionalDecryption = decryptionsCandidatesQueue;
        this.manager = manager;
//        System.out.println();

    }
    @Override
    public void run() {
        try {
            bruteForce();
            this.missionsCounterListener.increaseByOneMissionsCounter();
            this.missionsCounterListener.increaseByOneMissionsDone();
        } catch (CloneNotSupportedException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void bruteForce() throws CloneNotSupportedException, InterruptedException {

        for (int i = 0; i < missionSize; i++) {
            if (this.stateOfThreadPool[0].equals("Shutdown")) {
                this.optionalDecryption.clear();
                break;
            }

//            System.out.print("rotors pos: " + originalEnigma.getRotors().get(3).getStartingPosition());
//            System.out.print(" " + originalEnigma.getRotors().get(2).getStartingPosition());
//            System.out.println(" " + originalEnigma.getRotors().get(1).getStartingPosition());

            this.enigma = this.originalEnigma.clone();
            String output = "";
            List<Rotor> firstRotorsConfiguration = this.enigma.cloneRotorList(this.enigma.getRotors());
            String s;
            for (int j = 0; j < message.length(); j++) {
                output += enigma.encodeDecode(Character.toString(message.charAt(j)));
            }
            if (this.stateOfThreadPool[0].equals("Shutdown")) {
                this.optionalDecryption.clear();
                break;
            }

            List<String> words = new ArrayList<String>(Arrays.asList(output.split(" ")));
            output = output.trim();

            if (dictionary.containsAll(words) && !allDecryptionsTillNow.contains(output)
            && this.stateOfThreadPool[0].equals("Running")) {

                if (!output.equals("")) {
                    DTODecryptionCandidate dtoDecryptionCandidate = new DTODecryptionCandidate(allyName, output, this.agentName,
                            new DTOCodeConfigurationDescriptor(firstRotorsConfiguration,
                                    this.enigma.getReflector(), null));
                    optionalDecryption.enqueue(dtoDecryptionCandidate);
                    allDecryptionsTillNow.add(output);
                    manager.getAgentController().updateDecryptionCandidatesTableView(dtoDecryptionCandidate);
                    manager.getAgentController().sendCandidatePerMission();
                    missionsCounterListener.increaseByOneNumOfCandidates();
                }
            }

            if (this.stateOfThreadPool[0].equals("Shutdown")) {
                this.optionalDecryption.clear();
                break;
            }
            if (!originalEnigma.updateAllNextPositions()) { // if we reached the limit of the mission, we quit
                break;
            }
//            if (firstRotorsConfiguration.get(1).getStartingPosition().equals("A") && firstRotorsConfiguration.get(2).getStartingPosition().equals("A") && firstRotorsConfiguration.get(3).getStartingPosition().equals("A")) {
////                System.out.println("reflector = " + this.enigma.getReflector().getId());
////                System.out.print("rotors id = " + this.enigma.getRotors().get(1).id);
////                System.out.print(" " + this.enigma.getRotors().get(2).id);
////                System.out.println(" " + this.enigma.getRotors().get(3).id);
////                System.out.println();
////                System.out.println("output = " + output);
//            }
        }
        //timer.stopTime();
        //this.missionsTimesQueue.enqueue(timer);
    }
}