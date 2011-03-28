/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FallingDown.cassandracrawler;

import FallingDown.cassandracrawler.threads.DataWriter;

/**
 *
 * @author victork
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       DataWriter dataWrite= new DataWriter("DataWriter");
       dataWrite.run();
    }


}
