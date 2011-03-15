package me.FallingDownLib.interfaces.www;

/**
 * New Interface : first send the head, and then sends the body
 * @author victork
 */
public interface ToHTML2Converter {

    public String toHTML_GetHead();
    public String toHTML_GetBody();

}
