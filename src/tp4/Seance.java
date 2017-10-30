/**
 * Permet de représenter un tuple de la table seance.
 */
package tp4;

import java.sql.Date;

/**
 * @author Bebo
 *
 */
public class Seance
{
    private int id;
    private int proces_id;
    private Date date;

    /**
     * Constructeur par défaut
     */
    public Seance()
    {
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     */
    public Seance(int id)
    {
        this.id = id;
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     * @param proces_id
     * @param date
     */
    public Seance(int id, int proces_id, Date date)
    {
        this(id);
        this.proces_id = proces_id;
        this.date = date;
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the proces_id
     */
    public int getProces_id()
    {
        return proces_id;
    }

    /**
     * @param proces_id
     *            the proces_id to set
     */
    public void setProces_id(int proces_id)
    {
        this.proces_id = proces_id;
    }

    /**
     * @return the date
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

}
