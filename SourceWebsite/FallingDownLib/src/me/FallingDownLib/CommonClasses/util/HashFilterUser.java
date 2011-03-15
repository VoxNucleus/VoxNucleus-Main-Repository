/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.FallingDownLib.CommonClasses.util;

import java.util.HashMap;

/**
 * TODO
 * @author victork
 */
public class HashFilterUser {
    HashMap<String,String> work;
    




    public HashFilterUser(HashMap<String, String> in){
        work = in;

        filter();
    }

    private void filter(){
        

    }


    public HashMap<String,String> getHashBack(){
        return work;
    }

}
