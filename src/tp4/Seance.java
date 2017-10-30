package tp4;

import java.sql.Date;
import javax.persistence.*;

/**
 * Permet de représenter un tuple de la table seance.
 */

@Entity
public class Seance
{
    @Id
    @GeneratedValue
    private long m_id;
    
    private int id;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Proces proces;
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
     * @param proces
     * @param date
     */
    public Seance(int id, Proces proces, Date date)
    {
        this(id);
        this.proces = proces;
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
     * @return Proces
     */
    public Proces getProces()
    {
        return proces;
    }

    /**
     * @param proces
     */
    public void setProces(Proces proces)
    {
        this.proces = proces;
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