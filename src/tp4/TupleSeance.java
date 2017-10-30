package tp4;

import java.sql.Date;
import javax.persistence.*;

/**
 * Permet de représenter un tuple de la table seance.
 */

@Entity
public class TupleSeance
{
    @Id
    @GeneratedValue
    private long m_id;
    
    private int id;
    @OneToOne(cascade = CascadeType.PERSIST)
    private TupleProces proces;
    private Date date;

    /**
     * Constructeur par défaut
     */
    public TupleSeance()
    {
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     */
    public TupleSeance(int id)
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
    public TupleSeance(int id, TupleProces proces, Date date)
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
     * @return TupleProces
     */
    public TupleProces getProces()
    {
        return proces;
    }

    /**
     * @param proces
     */
    public void setProces(TupleProces proces)
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