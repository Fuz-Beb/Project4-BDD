package tp4;

import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

/**
 * Permet d'effectuer les accès à la table partie.
 */
public class TablePartie
{
    private TypedQuery<Partie> stmtExiste;
    private Connexion cx;

    /**
     * Constructeur de confort. Creation d'une instance. Précompilation
     * d'énoncés SQL.
     * 
     * @param cx
     */
    public TablePartie(Connexion cx)
    {
        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery("select p from Partie where id = p.id = :id", Partie.class);
    }

    /**
     * Retourner la connexion associée.
     * 
     * @return Connexion
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Vérifie si un partie existe.
     * 
     * @param partie
     * @return boolean
     */
    public boolean existe(Partie partie)
    {
        stmtExiste.setParameter("id", partie.getId());
        return !stmtExiste.getResultList().isEmpty();
    }

    /**
     * Ajout d'un nouveau partie
     * 
     * @param partie
     * @return Partie
     * @throws IllegalArgumentException
     * @throws TransactionRequiredException
     */
    public Partie ajout(Partie partie) throws IllegalArgumentException, TransactionRequiredException
    {
        cx.getConnection().persist(partie);
        return partie;
    }
}