package tp4;

import javax.persistence.*;

/**
 * Permet de représenter un tuple de la table partie.
 *
 */

@Entity
public class Partie
{
    @Id
    @GeneratedValue
    private long m_id;
    
    private int id;    
    private String prenom;
    private String nom;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Avocat avocat;

    /**
     * Constructeur par défaut
     */
    public Partie()
    {
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     * @param prenom
     * @param nom
     * @param avocat
     */
    public Partie(int id, String prenom, String nom, Avocat avocat)
    {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.avocat = avocat;
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     */
    public Partie(int id)
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
     * @return avocat
     */
    public Avocat getAvocat()
    {
        return avocat;
    }

    /**
     * @param avocat
     *            the avocat_id to set
     */
    public void setAvocat(Avocat avocat)
    {
        this.avocat = avocat;
    }
}
