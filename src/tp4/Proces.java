/**
 * Permet de représenter un tuple de la table proces.
 */
package tp4;

import java.sql.Date;

import javax.persistence.*;

/**
 * Permet de représenter un tuple de la table proces.
 *
 */

@Entity
public class Proces
{
    @Id
    @GeneratedValue
    private long m_id;
    
    private int id;    
    @OneToOne(cascade = CascadeType.PERSIST)
    private Juge juge;
    private Date date;
    private int devantJury;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Partie partieDefenderesse;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Partie partiePoursuivant;
    private String decision;

    /**
     * Constructeur par défaut
     */
    public Proces()
    {
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     */
    public Proces(int id)
    {
        this.id = id;
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     * @param juge
     * @param date
     * @param devantJury
     * @param partieDefenderesse
     * @param partiePoursuivant
     */
    public Proces(int id, Juge juge, Date date, int devantJury, Partie partieDefenderesse,
            Partie partiePoursuivant)
    {
        this(id);
        this.juge = juge;
        this.date = date;
        this.devantJury = devantJury;
        this.partieDefenderesse = partieDefenderesse;
        this.partiePoursuivant = partiePoursuivant;
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return the juge
     */
    public Juge getJuge()
    {
        return juge;
    }
    
    /**
     * @return the date
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * @return the devantJury
     */
    public int getDevantJury()
    {
        return devantJury;
    }

    /**
     * @return the partieDefenderesse
     */
    public Partie getPartieDefenderesse()
    {
        return partieDefenderesse;
    }

    /**
     * @return the partiePoursuivant
     */
    public Partie getPartiePoursuivant()
    {
        return partiePoursuivant;
    }

    /**
     * @return the decision
     */
    public String getDecision()
    {
        return decision;
    }
}
