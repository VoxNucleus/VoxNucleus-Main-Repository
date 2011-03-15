package me.FallingDownLib.functions.interestingposts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author victork
 */
public class InterestingPostEntry implements Comparable<InterestingPostEntry>, Serializable {

    private String postId;
    private float score;
    private long lastUpdateTimestamp;
    private long timestampCreation;
    private ArrayList<Long> last12HoursNbVotes;


    private static long FOUR_HOURS_IN_MILLI=4l*3600l*1000l;
    private static long TWELVE_HOURS_IN_MILLI=4l*3600l*1000l;


    protected InterestingPostEntry(){
        last12HoursNbVotes = new ArrayList<Long>();
    }

    /**
     * Create new item
     */

    public InterestingPostEntry(String id){
        this();
        postId=id;
        Calendar cal = Calendar.getInstance();
        long timestamp =cal.getTimeInMillis();
        lastUpdateTimestamp=timestamp;
        timestampCreation = timestamp;
        last12HoursNbVotes.add(timestamp);
    }

    /**
     * Generate a very simple InterestingPost
     * @param id
     * @param simpleInterestingPost
     */

    public InterestingPostEntry(float score, boolean simpleInterestingPost){
        this.score=score;
    }

    public void refreshScore(int listSize){
        withdrawToScore(listSize);
        setlastUpdateTimestamp();
        deleteOldVotes();
    }

    private void setlastUpdateTimestamp(){
        lastUpdateTimestamp=Calendar.getInstance().getTimeInMillis();
    }


    /**
     * Must be called when a vote is done
     * @param listSize
     */

    public void addVote(int listSize){
        deleteOldVotes();
        last12HoursNbVotes.add(Calendar.getInstance().getTimeInMillis());
        addToScore(listSize);
        withdrawToScore(listSize);
        setlastUpdateTimestamp();

    }

    /**
     * Update positive part of the score
     * @param listSize
     */

    private void addToScore(int listSize){
        score = score + (float) (
                Math.exp((double)last12HoursNbVotes.size())
                /
                ((double)listSize+1));
    }

    /**
     * Update negative part of the score
     * @param listSize
     */

    public void withdrawToScore(int listSize){
        Calendar cal = Calendar.getInstance();
        long NOW_timestamp=cal.getTimeInMillis();
        score = score - (float) (ageDecay(NOW_timestamp)*((double)( NOW_timestamp -lastUpdateTimestamp
                )/FOUR_HOURS_IN_MILLI)
                * (Math.log10(1d + (double) listSize)));
        
    }

    /**
     * introduce an age decaying factor 
     * @param NOW_timestamp
     * @return
     */

    private double ageDecay(long NOW_timestamp){
        return Math.exp( (double)(NOW_timestamp-timestampCreation)/TWELVE_HOURS_IN_MILLI);
    }



    /**
     *
     * @return score
     */

    public float getScore(){
        return this.score;
    }
    /**
     *
     * @return Id of the post
     */

    public String getPostId(){
        return this.postId;
    }

    /**
     * For ordering
     * @param o
     * @return 0 if score are equal; 1 if passed object has a smaller score; -1 if passed object has higher score
     */

    public int compareTo(InterestingPostEntry o) {
        if(this.score==o.getScore())
            return 0;
        else if(this.score > o.getScore())
            return 1;
        else
            return -1;

    }

    /**
     * Delete old votes in the list.
     * They are ordered by date so it doesn't need to be sorted
     */

    private void deleteOldVotes(){
        long NOW = Calendar.getInstance().getTimeInMillis();
        for(int index=last12HoursNbVotes.size()-1; index>=0;index--){
            if((NOW-last12HoursNbVotes.get(index))>TWELVE_HOURS_IN_MILLI)
                last12HoursNbVotes.remove(index);
            else
                break;
        }    
    }
}
