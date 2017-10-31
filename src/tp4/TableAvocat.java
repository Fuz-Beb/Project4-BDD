package tp4;

import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

/**
 * Permet d'effectuer les accès à la table avocat.
 */
public class TableAvocat
{
    private TypedQuery<Avocat> stmtExiste;
    private Connexion cx;

    /**
     * Création d'une instance. Des énoncés SQL pour chaque requête sont
     * précompilés.
     * 
     * @param cx
     */
    public TableAvocat(Connexion cx)
    {
        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery("select a from Avocat where a.id = :id", Avocat.class);
    }

    /**
     * Retourner la connexion associée
     * 
     * @return Connexion
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Retourne l'avocat demandé
     * 
     * @param id
     * @return Avocat
     * @throws Exception
     */
    public Avocat getAvocat(int id) throws Exception
    {
        stmtExiste.setParameter("id", id);
        return stmtExiste.getSingleResult();
    }

    /**
     * Vérifie si l'avocat existe
     * 
     * @param avocat
     * @return boolean
     */
    public boolean existe(Avocat avocat)
    {
        stmtExiste.setParameter("id", avocat.getId());
        return !stmtExiste.getResultList().isEmpty();
    }

    /**
     * Ajout d'un nouvelle avocat dans la base de données
     * 
     * @param avocat
     * @return Avocat
     * @throws IllegalArgumentException
     * @throws TransactionRequiredException
     */
    public Avocat ajouter(Avocat avocat) throws IllegalArgumentException, TransactionRequiredException
    {
        cx.getConnection().persist(avocat);
        return avocat;
    }
}