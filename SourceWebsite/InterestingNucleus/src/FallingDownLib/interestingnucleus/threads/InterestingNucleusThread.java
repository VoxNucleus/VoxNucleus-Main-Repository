package FallingDownLib.interestingnucleus.threads;

import FallingDownLib.interestingnucleus.ToResultsConverter;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CommonClasses.Categories;
import me.FallingDownLib.CommonClasses.SubCategories;

/**
 *
 * @author victork
 */
public class InterestingNucleusThread extends Thread {

    private static final long SLEEPING_TIME=1800 * 1000;


    public InterestingNucleusThread(String name){
        
    }

    @Override
    public void run(){
        for(;;){
            launchProcess();
            goToSleep();
        }
    }


    private void launchProcess() {
        for (int category_index = 0; category_index < Categories.MAIN_CATEGORIES.length; category_index++) {
            for (int sub_category_index = 0; sub_category_index < SubCategories.SUB_CATEGORIES[category_index].length; sub_category_index++) {
                ToResultsConverter converter = ToResultsConverter.getInstance(Categories.MAIN_CATEGORIES[category_index], SubCategories.SUB_CATEGORIES[category_index][sub_category_index]);
                converter.exportToCassandra();
            }
        }
    }

       /**
     * Function that is made in order for the thread to go to sleep for a period of time (given in the
     * constructor of the class)
     */
    private void goToSleep() {
        try {
            InterestingNucleusThread.sleep(SLEEPING_TIME);
        } catch (InterruptedException ex) {
            Logger.getLogger(InterestingNucleusThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
