/**
 * Superhero.java: Model class for Superhero object
 *
 * @author Cameron Colleran
 * @version 1.0
 */

package edu.miracosta.cs134.model;

public class Superhero
{
    /** Instance String variables */
    private String imageName, name, superpower, oneThing;

    /**
     * Constructor for Superhero Object
     *
     * @param imageName the name of the objects corresponding Image File Name
     * @param name the name of the hero
     * @param superpower the superpower of the hero
     * @param oneThing the one interesting fact of the hero
     */
    public Superhero(String imageName, String name, String superpower, String oneThing)
    {
        this.imageName = imageName;
        this.name = name;
        this.superpower = superpower;
        this.oneThing = oneThing;
    }

    /**
     * Getter for imageName
     *
     * @return the imageName
     */
    public String getImageName()
    {
        return imageName;
    }

    /**
     * Setter for image name
     *
     * @param imageName the name of the image file
     */
    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    /**
     * Getter for name
     *
     * @return the hero name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Setter for name of hero
     *
     * @param name the name of the hero
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Getter for superpower
     *
     * @return the hero's superpower
     */
    public String getSuperpower()
    {
        return superpower;
    }

    /**
     * Setter for power of hero
     *
     * @param superpower the power of the hero
     */
    public void setSuperpower(String superpower)
    {
        this.superpower = superpower;
    }

    /**
     * Getter for oneThing
     *
     * @return the hero's interesting fact
     */
    public String getOneThing()
    {
        return oneThing;
    }

    /**
     * Setter for the oneThing
     *
     * @param oneThing the interesting fact of the hero
     */
    public void setOneThing(String oneThing)
    {
        this.oneThing = oneThing;
    }

    /**
     * To String for Superhero
     *
     * @return all superhero data
     */
    @Override
    public String toString()
    {
        return "Superhero{" +
                "imageName='" + imageName + '\'' +
                ", name='" + name + '\'' +
                ", superpower='" + superpower + '\'' +
                ", oneThing='" + oneThing + '\'' +
                '}';
    }
}
