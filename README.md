# Enigma-Machine

## Some History:
The Enigma machine is a cipher device developed and used in the early- to mid-20th century to protect commercial, diplomatic, and military communication.
It was employed extensively by Nazi Germany during World War II, in all branches of the German military.
The Enigma machine was considered so secure that it was used to encipher the most top-secret messages.

## About the machine:
How did the Enigma Machine work? [20 min description video](https://www.youtube.com/watch?v=ybkkiGtJmkM).

The difficulty of cracking the Enigma machine:  
[video detailing the issue](https://www.youtube.com/watch?v=G2_Q9FoD-oQ).

## The entities:
#### • UBoat 
A German submarine responsible for determining an initial code for the machine, distributing an encrypted message and deciding which of the different teams was able to decode the message accurately (winner of the competition).

#### • Allies  
Decoding teams of the allies competing among themselves to be the first to succeed in deciphering the encrypted message transmitted by the UBoat.

#### • Agent
Agents who are members of the various decoding teams and are responsible for performing the decoding tasks themselves. Each agent is a member of exactly one team.


## The course of the competition
![image](https://user-images.githubusercontent.com/103381319/200266224-a92e5e16-657f-43ef-94b2-2879d915ed3d.png)

The competition starts when all players (Allies, Uboat) announce that they are ready. The UBoat will choose a code and encrypt a certain message (a valid message, from words in the dictionary). The encrypted message is transmitted and distributed to all the Allies teams participating in the competition. Each team assigns the various tasks to the agents it works with. The agents in turn start the decoding process and send possible candidates back to DM and he in turn sends them back to UBoat.
The UBoat receives a collection of candidates from the various Allies teams and only he will be the one to determine who the winning candidate is (since only he knows which string matches the source string).

Each team differs from one another in the amount of (dynamic) agents it can assign and of course the size of the task.
The agents in this exercise will be implemented as a separate process, which will (ostensibly) run from other computer(s).
The DM itself runs inside the server (as part of the DM engine). The agents (associated with each DM) also work independently in front of the server (identifying which DM team they belong to).

When a DM (=Allies) registers for the game, he sets a unique name for himself. This name will be used by him to communicate with his agents. When the agent processes are established, they will be configured to work with/against a certain DM and in all their requests to the server will bear the name of the DM they are communicating with on their back.

