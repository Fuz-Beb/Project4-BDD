package tp4;

import java.sql.SQLException;

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
     * @throws SQLException
     */
    public TableAvocat(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery("select m from Membre m where m.m_idMembre = :idMembre", Avocat.class);
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
     * Vérifie si l'avocat existe
     * 
     * @param avocat 
     * @return boolean
     * @throws SQLException
     */
    public boolean existe(Avocat avocat) throws SQLException
    {
        stmtExiste.setParameter(1, avocat.getId());
        return !stmtExiste.getResultList().isEmpty();
    }

    /**
     * Ajout d'un nouvelle avocat dans la base de données
     * 
     * @param avocat
     * @return Avocat
     * @throws SQLException
     */
    public Avocat ajouter(Avocat avocat) throws SQLException
    {
        cx.getConnection().persist(avocat);
        return avocat;
    }
}