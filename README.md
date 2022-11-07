# Enigma-Machine

## Some History:
The Enigma machine is a cipher device developed and used in the early- to mid-20th century to protect commercial, diplomatic, and military communication.
It was employed extensively by Nazi Germany during World War II, in all branches of the German military.
The Enigma machine was considered so secure that it was used to encipher the most top-secret messages.

## About the machine:
How did the Enigma Machine work? [20 min description video](https://www.youtube.com/watch?v=ybkkiGtJmkM).

The machine comes with a number of possible rotors for use, numbered with the digits 1..n, you must choose C rotors to use from them, and determine the order of the rotors. The initial position of each rotor must then be determined. Each rotor has the 26 letters of the ABC, so you simply determine which letter is the initial position of each rotor. Finally, choose which reflector to use (there are several of them. They are marked with Greek letters in Roman spelling)
To make the code even more difficult to decipher, the Enigma Machine used a simple (but malicious) trick: at each input signal arc, just before sending the current through the rotors in the "go" -> reflect -> "reverse" direction, the machine would advance the rightmost wheel one step One forward ("step"). When the right wheel would reach a certain position, a small notch (notch) fixed on the right rotor would cause the next wheel to also advance by one step. Similarly, when the central wheel reached a certain position, a small lug fixed on the central rotor would cause the next wheel to also move forward by one step. In fact, each typing of a letter advanced one (or more) of the wheels one step forward and thus actually changed the position of the wheels, and as a result the entire wiring for each encrypted letter (!)
That is, the Enigma machine encrypts with a cipher that is not a simple 'substitution cipher': the same letter that is typed several times in a row will be encrypted each time with a different letter (!!) This adds another dimension of difficulty in trying to decipher the ciphers using techniques that were known until then, based on letter frequency analysis, etc. .
The location of the lug on each wheel is arbitrary (that is, not all the lugs are located on the 26th step, as you might expect...), and with each movement of each rotor - its lug "moves" along with it (try saying that several times in a row and see what You get...)
Another detail to note is that due to the existence of the reflector, a signal could never be encrypted to itself (guess why?).
(In retrospect, this detail actually came out of the machine's hands since it helped (a little) the British forces in deciphering the code after all...)
Was it enough for you? oh not yet? Then...


The last component of the machine was the plug board:
The plug board contained an entry for every letter from the alphabet (26 letters of the ABC) and actually made it possible to define alternating pairs of letters. The operator would connect a cable with 2 pins into the plugs of a pair of letters (say K and D or for example Z and O) and this means that every time these letters reach the plug board - they change.
A signal reached the plug board twice:
In the "go", immediately after the operator pressed a letter (say K), it flowed to the plug board. If she was part of an alternating pair, now she would alternate according to her pair (in this case D). The letter D was the one that now reaches the bulk of the machine (rotors -> reflector -> rotors) and it was the one that was encrypted (let's say Z).
On its way "back" to the light bulb keyboard, it would go through the plug board again, and maybe potentially be changed again (for example in our case it is changed to O) and only then the result would flow to the light bulb keyboard.
A signal that reaches the plug board and is not part of the mapping - remains in my eyes and leaves it as it came.
 
Features of using the plug board:
• In the Enigma machine it was customary to use up to 10 pairs (out of 13 possible in total). We will use as many as we want.
• Unlike the other components - there is no obligation to use the plug board.
• A letter cannot appear in a pair with itself.
• A letter can appear in at most one pair (or not at all)

Simple and naive as it sounds - the plug board added a dimension that multiplied by several orders of magnitude (!) the difficulty of cracking the Enigma machine 
[video detailing the issue](https://www.youtube.com/watch?v=G2_Q9FoD-oQ).


**I have developed the projecct on Intellij IDEA and JavaFX Scene Builder - Gluon**
