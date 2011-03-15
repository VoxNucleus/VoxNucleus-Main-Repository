package me.FallingDownLib.functions.interestingposts;

import java.util.Comparator;

/**
 * Implements simple comparator
 * @author victork
 */
public class postIdComparator implements Comparator<InterestingPostEntry> {

    public int compare(InterestingPostEntry o1, InterestingPostEntry o2) {
        return o1.getPostId().compareTo(o2.getPostId());
    }

}
