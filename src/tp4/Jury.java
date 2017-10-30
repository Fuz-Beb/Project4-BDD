package tp4;

/**
 * Permet de représenter un tuple de la table jury.
 */
public class Jury
{
    private int nas;
    private String prenom;
    private String nom;
    private String sexe;
    private int age;
    private int proces_id;

    /**
     * Constructeur par défaut
     */
    public Jury()
    {
    }

    /**
     * Constructeur de confort
     * 
     * @param nas
     */
    public Jury(int nas)
    {
        this.nas = nas;
    }

    /**
     * Constructeur de confort
     * 
     * @param nas
     * @param prenom
     * @param nom
     * @param sexe
     * @param age
     */
    public Jury(int nas, String prenom, String nom, String sexe, int age)
    {
        this(nas);
        this.prenom = prenom;
        this.nom = nom;
        this.sexe = sexe;
        this.age = age;
    }

    /**
     * @return the nas
     */
    public int getNas()
    {
        return nas;
    }

    /**
     * @param nas
     *            the nas to set
     */
    public void setNas(int nas)
    {
        this.nas = nas;
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
     * @return the sexe
     */
    public String getSexe()
    {
        return sexe;
    }

    /**
     * @param sexe
     *            the sexe to set
     */
    public void setSexe(String sexe)
    {
        this.sexe = sexe;
    }

    /**
     * @return the age
     */
    public int getAge()
    {
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(int age)
    {
        this.age = age;
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
}