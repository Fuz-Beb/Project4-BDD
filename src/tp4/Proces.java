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
    private int id;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    private Juge juge;
    private Date date;
    private int devantJury;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Partie partieDefenderesse;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Partie partiePoursuivant;
    private int decision;

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
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the juge
     */
    public Juge getJuge()
    {
        return juge;
    }

    /**
     * @param juge
     *            the juge to set
     */
    public void setJuge(Juge juge)
    {
        this.juge = juge;
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

    /**
     * @return the devantJury
     */
    public int getDevantJury()
    {
        return devantJury;
    }

    /**
     * @param devantJury
     *            the devantJury to set
     */
    public void setDevantJury(int devantJury)
    {
        this.devantJury = devantJury;
    }

    /**
     * @return the partieDefenderesse
     */
    public Partie getPartieDefenderesse()
    {
        return partieDefenderesse;
    }

    /**
     * @param partieDefenderesse
     *            the partieDefenderesse to set
     */
    public void setPartieDefenderesse(Partie partieDefenderesse)
    {
        this.partieDefenderesse = partieDefenderesse;
    }

    /**
     * @return the partiePoursuivant
     */
    public Partie getPartiePoursuivant()
    {
        return partiePoursuivant;
    }

    /**
     * @param partiePoursuivant
     *            the partiePoursuivant to set
     */
    public void setPartiePoursuivant(Partie partiePoursuivant)
    {
        this.partiePoursuivant = partiePoursuivant;
    }

    /**
     * @return the decision
     */
    public int getDecision()
    {
        return decision;
    }

    /**
     * @param decision
     *            the decision to set
     */
    public void setDecision(int decision)
    {
        this.decision = decision;
    }
}
