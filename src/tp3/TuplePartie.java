/**
 * Permet de représenter un tuple de la table partie.
 */
package tp3;

/**
 * @author Bebo
 *
 */
public class TuplePartie
{
    private int id;
    private String prenom;
    private String nom;
    private int avocat_id;

    /**
     * Constructeur par défaut
     */
    public TuplePartie()
    {
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     * @param prenom
     * @param nom
     * @param avocat_id
     */
    public TuplePartie(int id, String prenom, String nom, int avocat_id)
    {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.avocat_id = avocat_id;
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     */
    public TuplePartie(int id)
    {
        this.id = id;
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
     * @return the prenom
     */
    public String getPrenom()
    {
        return prenom;
    }

    /**
     * @param prenom
     *            the prenom to set
     */
    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }

    /**
     * @return the nom
     */
    public String getNom()
    {
        return nom;
    }

    /**
     * @param nom
     *            the nom to set
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * @return the avocat_id
     */
    public int getAvocat_id()
    {
        return avocat_id;
    }

    /**
     * @param avocat_id
     *            the avocat_id to set
     */
    public void setAvocat_id(int avocat_id)
    {
        this.avocat_id = avocat_id;
    }
}
