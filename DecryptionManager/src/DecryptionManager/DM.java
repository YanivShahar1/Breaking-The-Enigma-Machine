package DecryptionManager;


import DTO.ContestStatus;
import DTO.DTOAllyTeamDetails;
import DTO.DTOMission;
import DTO.TimeMeasure;
import Machine.Enigma;
import Machine.Reflector;
import Machine.Rotor;
import SystemExceptions.invalidNumOfAgents;

import java.util.*;

public class DM implements Runnable{
    private final int QUEUE_CAPACITY = 10;
    private Set<String> dictionary;
    private int numOfMaxAgents;
    private int numOfCurrentAgents;
    private Level level;
    private int missionSize;
    private Set<String> excludeChars;
    private Enigma enigma;
    private MissionsQueue missionsQueue;

    //private CustomThreadPoolExecutor threadPool;
    //private BlockingQueue<Runnable> blockingQueue;

    private String allyName;
    private Set<String> allDecryptionsTillNow;
    private String message;
    private List<Rotor> allPossibleRotors;
    private List<Reflector> allPossibleReflectors;
    private boolean nextReflector = false;
    private int rotorsCount;

    private String[] stateOfThreadPool;
    private TimeMeasure timer;
    private long averageMissionsTime = 0;
    private boolean isDoneCalculatingAVGMissionsTime = false;
    private boolean isDoneCalculatingTotalDecryptionTime = false;
    private boolean isRunning = false;
    private ContestStatus contestStatus;

    public DM (String allyName) throws CloneNotSupportedException, InterruptedException {

        this.numOfCurrentAgents = 0;
        this.dictionary = new HashSet<>();
        this.excludeChars = new HashSet<>();


        this.allDecryptionsTillNow = new HashSet<>();
        this.stateOfThreadPool = new String[1];
        this.stateOfThreadPool[0] = "Running";

        this.timer = new TimeMeasure();
        this.missionsQueue = new MissionsQueue();
        this.allyName = allyName;
        this.contestStatus = ContestStatus.WAITING;
    }


    public Enigma getEnigma() {
        return enigma;
    }

    public void setDictionary(Set<String> dictionary) {
        this.dictionary = dictionary;
    }

    public void setContestStatus(ContestStatus contestStatus) {
        this.contestStatus = contestStatus;
    }

    public ContestStatus getContestStatus() {
        return contestStatus;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    public boolean getIsRunning() {
        return this.isRunning;
    }
    public MissionsQueue getMissionsQueue() {
        return this.missionsQueue;
    }
    public void setStateOfThreadPool(String state) {
        this.stateOfThreadPool[0] = state;
    }




    public void setNumOfMaxAgents(int numOfMaxAgents) throws invalidNumOfAgents {
        if (numOfMaxAgents > 50 || numOfMaxAgents < 2) {
            throw new invalidNumOfAgents();
        }
        this.numOfMaxAgents = numOfMaxAgents;
    }

    public int getNumOfMaxAgents() {
        return numOfMaxAgents;
    }

    public int getNumOfCurrentAgents() {
        return numOfCurrentAgents;
    }

    public void setNumOfCurrentAgents(int numOfCurrentAgents) {
        this.numOfCurrentAgents = numOfCurrentAgents;
    }
    public void decreaseNumOfCurrentAgents() {
        this.numOfCurrentAgents -=1;
    }
    public void increaseNumOfCurrentAgents() {
        this.numOfCurrentAgents +=1;
    }


    @Override
    public void run() {
        try {
            this.contestStatus = ContestStatus.RUNNING;

            this.missionsQueue.clear();

//            this.timer.resetTime();
//            this.missionsTimesQueue.clear();
//            this.missionsTimesQueue.setStop(false);
//            this.missionsTimesQueue.setFinished(false);
//            this.progressCounter.reset();
//            this.progressCounter.setNumOfMissions((int)getTrueNumOfMissions());
            //this.progressCounter.setNumOfMissions((int)Math.ceil((double)calcNumOfOptions() / this.missionSize));
            this.nextReflector = false;
            this.isDoneCalculatingAVGMissionsTime = false;
            this.isDoneCalculatingTotalDecryptionTime = false;
            //timer.startTime();

            createMission(0);

            //timer.stopTime();
            this.isDoneCalculatingTotalDecryptionTime = true;
            this.isDoneCalculatingAVGMissionsTime = true;


        }catch(CloneNotSupportedException | InterruptedException ignored) {
        }
    }

    public boolean isDoneCalculatingAVGMissionsTime() {
        return this.isDoneCalculatingAVGMissionsTime;
    }
    public boolean isDoneCalculatingTotalDecryptionTime() {
        return this.isDoneCalculatingTotalDecryptionTime;
    }
    public long getAverageMissionsTime() {
        return this.averageMissionsTime;
    }
    public long getTotalDecryptionTime() {
        return this.timer.getElapsedTime() / 1_000_000; // ms
    }
    private void createMission(int whichMission) throws CloneNotSupportedException, InterruptedException {
        switch (this.level) {
            case EASY:
                createEasyMission(whichMission);
                break;
            case MEDIUM:
                createModerateMission(whichMission);
                break;
            case HARD:
                createHardMission(whichMission);
                break;
            case IMPOSSIBLE:
                createImpossibleMission(whichMission);
                break;
        }

    }
    private void createEasyMission(int whichMission) throws CloneNotSupportedException, InterruptedException {
        boolean finished = false;
        if (contestStatus != ContestStatus.RUNNING) {
            return;
        }

        while (!finished) {
            if (contestStatus != ContestStatus.RUNNING) {
                return;
            }
            Enigma newEnigma = this.enigma.clone();
            for (int j = 0; j < missionSize; j++) {
                if (contestStatus != ContestStatus.RUNNING) {
                    return;
                }

                if (!this.enigma.updateAllNextPositions()) {
                    this.nextReflector = true;

                    finished = true;
                    break;
                }
            }

            missionsQueue.enqueue(new DTOMission(newEnigma, allyName, missionSize, message, whichMission, dictionary, contestStatus));
        }
    }
    private void createModerateMission(int whichMission) throws CloneNotSupportedException, InterruptedException {
        Enigma originalEnigma = this.enigma.clone();
        for (int i = 1; i <= allPossibleReflectors.size() - 1; i++) {


            this.enigma = originalEnigma.clone();
            this.enigma.setReflector(allPossibleReflectors.get(i));
            createEasyMission(whichMission);
        }
    }
    private void createHardMission(int whichMission) throws CloneNotSupportedException, InterruptedException {
        Enigma originalEnigma = this.enigma.clone();
        List<List<Integer>> permutations = getPermutations();
        int i=0;
        for (List<Integer> permutation : permutations) {


            this.enigma = originalEnigma.clone();
            List<Rotor> rotors = buildRotorListFromID(permutation);
            initEnigmaRotorsPositions(rotors);
            this.enigma.setRotors(rotors);
            createModerateMission(whichMission);
        }
    }

    public long getTrueNumOfMissions(){
        long easyNumOfMissions =(long) Math.ceil( ((double)calcEasyLevelNumOfOptions() / missionSize));
        long res = 0;

        switch (this.level) {
            case EASY:
                res = easyNumOfMissions;
                break;
            case MEDIUM:
                res = easyNumOfMissions * (this.allPossibleReflectors.size()-1);
                break;
            case HARD:
                res = factorial(this.enigma.getRotors().size()-1) *
                        easyNumOfMissions *
                        (this.allPossibleReflectors.size()-1);
                break;
            case IMPOSSIBLE:
                res = nCr(this.allPossibleRotors.size()-1 ,this.enigma.getRotors().size()-1 ) *
                        factorial(this.enigma.getRotors().size()-1) *
                        easyNumOfMissions *
                        (this.allPossibleReflectors.size()-1);
                break;

        }

        return res;

    }
    public void createImpossibleMission(int whichMission) throws CloneNotSupportedException, InterruptedException {
        Enigma originalEnigma = this.enigma.clone();
        List<List<Integer>> optionalRotors = getOptionalRotors(allPossibleRotors.size() - 1, this.rotorsCount);
        int i = 0;
        for (List<Integer> optional : optionalRotors) {

            this.enigma = originalEnigma.clone();
            List<Rotor> rotors = buildRotorListFromID(optional);
            this.enigma.setRotors(rotors);
            createHardMission(whichMission);
        }
    }
    public List<Rotor> buildRotorListFromID(List<Integer> IDList) {
        List<Rotor> rotors = new ArrayList<>();
        rotors.add(null); // dummy
        for (Integer id : IDList) {
            for (Rotor rotor : allPossibleRotors) {
                if (rotor == null) {
                    continue;
                }
                if (rotor.id == id) {
                    rotors.add(rotor.clone());
                }
            }
        }
        return rotors;
    }

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }
    public static List<List<Integer>> getOptionalRotors(int n, int r) {
        List<List<Integer>> combinations = new ArrayList<>();
        List<Integer> combination = new ArrayList<>(r);

        // initialize with the lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination.add(i);
        }

        while (combination.get(r - 1) < n) {
            combinations.add(new ArrayList<>(combination));

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination.get(t) == n - r + t) {
                t--;
            }
            combination.set(t, combination.get(t) + 1);
            for (int i = t + 1; i < r; i++) {
                combination.set(i, combination.get(i - 1) + 1);
            }
        }

        for (List<Integer> lst : combinations) {
            lst.replaceAll(integer -> integer + 1);
        }

        return combinations;
    }
    private List<List<Integer>> getPermutations() {
        List<List<Integer>> lst = new ArrayList<>();
        int n = this.enigma.getRotors().size() - 1;
        List<Integer> elements = new ArrayList<>();

        for (Rotor rotor : this.enigma.getRotors()) {
            if (rotor == null) {
                continue;
            }
            elements.add(rotor.id);
        }
        lst.add(elements);

        int[] indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }

        int i = 0;
        while (i < n) {
            if (indexes[i] < i) {
                elements = new ArrayList<>(elements);
                Collections.swap(elements, i % 2 == 0 ?  0: indexes[i], i);
                lst.add(elements);
                indexes[i]++;
                i = 0;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }
        return lst;
    }

    public Level getLevel() {
        return level;
    }

    public long calcEasyLevelNumOfOptions(){

        return (long)Math.pow((double)enigma.getABC().size(), (double)(enigma.getRotors().size() - 1));
    }
    public long calcNumOfOptions() {
        long returnedVal = 0;
        switch (this.level) {
            case EASY:
                returnedVal = (long)Math.pow((double)enigma.getABC().size(), (double)(enigma.getRotors().size() - 1));
                break;
            case MEDIUM:
                returnedVal = (long)Math.pow((double)enigma.getABC().size(), (double)(enigma.getRotors().size() - 1))
                        * (allPossibleReflectors.size() - 1);
                break;
            case HARD:
                returnedVal = (long)Math.pow((double)enigma.getABC().size(), (double)(enigma.getRotors().size() - 1))
                        * (allPossibleReflectors.size() - 1) * factorial(enigma.getRotors().size() - 1);
                break;
            case IMPOSSIBLE:
                returnedVal = (long)Math.pow((double)enigma.getABC().size(), (double)(enigma.getRotors().size() - 1))
                        * (allPossibleReflectors.size() - 1) * factorial(enigma.getRotors().size() - 1)
                        * nCr(allPossibleRotors.size() - 1, enigma.getRotors().size() - 1);
        }
        return returnedVal;
    }
    public long factorial(int num) {
        int fact = 1;
        for (int i = 1; i <= num; i++) {
            fact = fact * i;
        }
        return fact;
    }
    public long nCr(int n, int r) {
        return factorial(n) / (factorial(n - r) * factorial(r));
    }
    public void initEnigmaRotorsPositions(List<Rotor> rotorList) {
        for (Rotor rotor : rotorList) {
            if (rotor == null) {
                continue;
            }
            rotor.setStartingPosition(this.enigma.getABCList().get(1));
        }
    }

    public List<Rotor> randomRotors() {
        Random random = new Random();
        List<Integer> randomRotorsIDList = new ArrayList<>();
        boolean[] randomIDs = new boolean[this.allPossibleRotors.size()];
        Arrays.fill(randomIDs, false);
        int id;
        while (randomRotorsIDList.size() != rotorsCount) {
            id = random.nextInt(this.allPossibleRotors.size() - 1) + 1;
            if (!randomIDs[id]) {
                randomRotorsIDList.add(id);
                randomIDs[id] = true;
            }
        }
        return createUsedRotorsList(randomRotorsIDList);
    }
    public List<Rotor> createUsedRotorsList(List<Integer> rotorsIDList) {
        //usedRotorsList = new ArrayList<>();
        // usedRotorsList.add(null); // dummy
        List<Rotor> tempUsedRotorList = new ArrayList<>();
        tempUsedRotorList.add(null); // dummy
        for (Integer ID : rotorsIDList) {

            for (Rotor rotor : this.allPossibleRotors) {
                if (rotor == null) {
                    continue;
                }
                if (rotor.id == ID) {
                    //usedRotorsList.add(1, rotor);
                    tempUsedRotorList.add(1, rotor);
                }
            }
        }
        return tempUsedRotorList;
    }
    public Reflector randomReflector() throws CloneNotSupportedException {
        Random random = new Random();
        int id = random.nextInt(this.allPossibleReflectors.size() - 1) + 1;
        String romanID = convertIntLettersToRoman(String.valueOf(id));

        for (Reflector reflector : this.allPossibleReflectors) {
            if (reflector == null) {
                continue;
            }
            if (reflector.getId().equals(romanID)) {
                 return reflector.clone();
            }
        }
        return null;
    }
    private String convertIntLettersToRoman(String integer) {
        String roman = integer;
        switch (integer) {
            case "1":
                roman = "I";
                break;
            case "2":
                roman = "II";
                break;
            case "3":
                roman = "III";
                break;
            case "4":
                roman = "IV";
                break;
            case "5":
                roman = "V";
                break;
        }
        return roman;
    }
    public void randomStartingRotorsPositions(Map<String, Integer> ABC, List<Rotor> usedRotorsList) {
        List<String> ABCList = new ArrayList<>();
        for (String key : ABC.keySet()) {
            ABCList.add(key);
        }
        Random random = new Random();
        for (Rotor rotor : usedRotorsList) {
            if (rotor == null) {
                continue;
            }
            rotor.setStartingPosition(ABCList.get(random.nextInt(ABCList.size())));
        }
    }

    public Enigma randomEnigma(Map<String, Integer> ABC, Map<String, String> plugins) throws CloneNotSupportedException {
        List<Rotor> rotorList = randomRotors();
        Reflector reflector = randomReflector();
        randomStartingRotorsPositions(ABC, rotorList);
        return new Enigma(rotorList, reflector, ABC, plugins);
    }

    public void setMissionSize(int missionSize) {
       // this.missionSize = (int)Math.min((long)missionSize, calcEasyLevelNumOfOptions());
        this.missionSize = missionSize;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public void setEnigma(Enigma enigma) throws CloneNotSupportedException {
        this.enigma = enigma.clone();
        this.missionSize = (int)Math.min(this.missionSize, calcEasyLevelNumOfOptions());
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void clearAllDecryptionsTillNow() {
        this.allDecryptionsTillNow.clear();
    }
    public Set<String> getDictionary() {
        return dictionary;
    }

    public void setAllPossibleRotors(List<Rotor> allPossibleRotors) throws CloneNotSupportedException {
        Enigma tempEnigma = new Enigma();
        this.allPossibleRotors = tempEnigma.cloneRotorList(allPossibleRotors);
    }
    public void setAllPossibleReflectors(List<Reflector> allPossibleReflectors) throws CloneNotSupportedException {
        Enigma tempEnigma = new Enigma();
        this.allPossibleReflectors = tempEnigma.cloneReflectorList(allPossibleReflectors);
    }

    public DTOAllyTeamDetails getAllyTeam(){
        return new DTOAllyTeamDetails(this.allyName, this.numOfCurrentAgents, this.missionSize);
    }
}
