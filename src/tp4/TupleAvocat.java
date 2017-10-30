package tp4;

/**
 * Permet de représenter un tuple de la table avocat.
 */
public class TupleAvocat
{
    private int id;
    private String prenom;
    private String nom;
    private int type;

    /**
     * Constructeur par défaut
     */
    public TupleAvocat()
    {
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     * @param prenom
     * @param nom
     * @param type
     */
    public TupleAvocat(int id, String prenom, String nom, int type)
    {
        super();
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.type = type;
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     */
    public TupleAvocat(int id)
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
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }
}