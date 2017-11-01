package tp4;

import javax.persistence.*;

/**
 * Permet de représenter un tuple de la table juge.
 */

@Entity
public class Juge
{
    @Id
    @GeneratedValue
    private long m_id;
    
    private int id;
    private String prenom;
    private String nom;
    private int age;
    private boolean disponible;
    private boolean quitterJustice;

    /**
     * Constructeur par défaut
     */
    public Juge()
    {
    }

    /**
     * Constructeur de confort
     * 
     * @param id
     * @param prenom
     * @param nom
     * @param age
     */
    public Juge(int id, String prenom, String nom, int age)
    {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.age = age;
        this.disponible = true;
        this.quitterJustice = false;
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
     * @return the disponible
     */
    public boolean isDisponible()
    {
        return disponible;
    }

    /**
     * @param disponible
     *            the disponible to set
     */
    public void setDisponible(boolean disponible)
    {
        this.disponible = disponible;
    }

    /**
     * @return the quitterJustice
     */
    public boolean isQuitterJustice()
    {
        return quitterJustice;
    }

    /**
     * @param quitterJustice
     *            the quitterJustice to set
     */
    public void setQuitterJustice(boolean quitterJustice)
    {
        this.quitterJustice = quitterJustice;
    }
}