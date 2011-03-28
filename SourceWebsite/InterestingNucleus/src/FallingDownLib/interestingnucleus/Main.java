package FallingDownLib.interestingnucleus;

import FallingDownLib.interestingnucleus.threads.InterestingNucleusThread;

/**
 *
 * @author victork
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InterestingNucleusThread nucleusThread = new InterestingNucleusThread("Noyaux intéressants");
        nucleusThread.start();
    }

}
