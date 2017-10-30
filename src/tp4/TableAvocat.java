package tp4;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.TypedQuery;

/**
 * Permet d'effectuer les accès à la table avocat.
 */
public class TableAvocat
{
    private TypedQuery<TupleAvocat> stmtExiste;
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
        stmtExiste = cx.getConnection().createQuery("select m from Membre m where m.m_idMembre = :idMembre", Membre.class);
                prepareStatement("select * from \"Avocat\" where \"id\" = ?");
        stmtInsert = cx.getConnection()
                .prepareStatement("insert into \"Avocat\" (id, prenom, nom, type) values (?,?,?,?)");
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
     * @param tupleAvocat 
     * @return boolean
     * @throws SQLException
     */
    public boolean existe(TupleAvocat tupleAvocat) throws SQLException
    {
        stmtExiste.setInt(1, tupleAvocat.getId());
        ResultSet rset = stmtExiste.executeQuery();
        boolean avocatExiste = rset.next();
        rset.close();
        return avocatExiste;
    }

    /**
     * Ajout d'un nouvelle avocat dans la base de données
     * 
     * @param tupleAvocat
     * @throws SQLException
     */
    public void ajouter(TupleAvocat tupleAvocat) throws SQLException
    {
        stmtInsert.setInt(1, tupleAvocat.getId());
        stmtInsert.setString(2, tupleAvocat.getPrenom());
        stmtInsert.setString(3, tupleAvocat.getNom());
        stmtInsert.setInt(4, tupleAvocat.getType());
        stmtInsert.executeUpdate();
    }
}