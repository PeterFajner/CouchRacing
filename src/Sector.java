/** =======================================================================
 * Main Class:    CouchRacing      						Author: Peter Fajner
 * Version:  001                                        Date:  2015-06-01
 *
 * Purpose   Final Project - Couch Racing
 *
 * Course:   Computer Science 202                Teacher:  Ms Jones
 * School:   Sir Winston Churchill High School, Calgary, Alberta, Canada
 * Language: Java SE 8.0    Target Operating System: Java Virtual Machine
 * System:   Undefined running Undefined        IDE: IntelliJ IDEA
 * ========================================================================*/
import java.util.ArrayList;

/**
 * represents a collection of GameObjects within a particular 1000x1000 pixel area
 */
public class Sector
{
    public final int x;
    public final int y;
    public ArrayList<GameObject> gameObjects;

    public Sector(int x, int y, ArrayList<GameObject> gameObjects)
    {
        this.x = x;
        this.y = y;
        this.gameObjects = gameObjects;
    }
}
