package tp3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Permet d'effectuer les accès à la table partie.
 */
public class TablePartie
{
    private PreparedStatement stmtExistePartie;
    private PreparedStatement stmtInsertPartie;
    private Connexion cx;

    /**
     * Constructeur de confort. Creation d'une instance. Précompilation
     * d'énoncés SQL.
     * 
     * @param cx
     * @throws SQLException
     */
    public TablePartie(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtExistePartie = cx.getConnection().prepareStatement("select * from \"Partie\" where \"id\" = ?");
        stmtInsertPartie = cx.getConnection().prepareStatement(
                "insert into \"Partie\" (\"id\", \"prenom\", \"nom\", \"Avocat_id\") values (?,?,?,?)");
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
     * @param tuplePartie
     * @return boolean
     * @throws SQLException
     */
    public boolean existe(TuplePartie tuplePartie) throws SQLException
    {
        stmtExistePartie.setInt(1, tuplePartie.getId());
        ResultSet rset = stmtExistePartie.executeQuery();
        boolean partieExiste = rset.next();
        rset.close();
        return partieExiste;
    }

    /**
     * Ajout d'un nouveau partie
     * 
     * @param tuplePartie
     * @throws SQLException
     */
    public void ajout(TuplePartie tuplePartie) throws SQLException
    {
        /* Ajout du partie. */
        stmtInsertPartie.setInt(1, tuplePartie.getId());
        stmtInsertPartie.setString(2, tuplePartie.getPrenom());
        stmtInsertPartie.setString(3, tuplePartie.getNom());
        stmtInsertPartie.setInt(4, tuplePartie.getAvocat_id());
        stmtInsertPartie.executeUpdate();
    }
}
