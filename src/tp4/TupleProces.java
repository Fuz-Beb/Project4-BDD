/**
 * Permet de représenter un tuple de la table proces.
 */
package tp4;

import java.sql.Date;

/**
 * @author Bebo
 *
 */
public class TupleProces
{
    private int id;
    private int juge_id;
    private Date date;
    private int devantJury;
    private int partieDefenderesse_id;
    private int partiePoursuivant_id;
    private int decision;

    /**
     * Constructeur par défaut
     */
    public TupleProces()
    {
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     */
    public TupleProces(int id)
    {
        this.id = id;
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     * @param juge_id
     * @param date
     * @param devantJury
     * @param partieDefenderesse_id
     * @param partiePoursuivant_id
     */
    public TupleProces(int id, int juge_id, Date date, int devantJury, int partieDefenderesse_id,
            int partiePoursuivant_id)
    {
        this(id);
        this.juge_id = juge_id;
        this.date = date;
        this.devantJury = devantJury;
        this.partieDefenderesse_id = partieDefenderesse_id;
        this.partiePoursuivant_id = partiePoursuivant_id;
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
     * @return the juge_id
     */
    public int getJuge_id()
    {
        return juge_id;
    }

    /**
     * @param juge_id
     *            the juge_id to set
     */
    public void setJuge_id(int juge_id)
    {
        this.juge_id = juge_id;
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
     * @return the partieDefenderesse_id
     */
    public int getPartieDefenderesse_id()
    {
        return partieDefenderesse_id;
    }

    /**
     * @param partieDefenderesse_id
     *            the partieDefenderesse_id to set
     */
    public void setPartieDefenderesse_id(int partieDefenderesse_id)
    {
        this.partieDefenderesse_id = partieDefenderesse_id;
    }

    /**
     * @return the partiePoursuivant_id
     */
    public int getPartiePoursuivant_id()
    {
        return partiePoursuivant_id;
    }

    /**
     * @param partiePoursuivant_id
     *            the partiePoursuivant_id to set
     */
    public void setPartiePoursuivant_id(int partiePoursuivant_id)
    {
        this.partiePoursuivant_id = partiePoursuivant_id;
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
