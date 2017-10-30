package tp4;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Permet d'effectuer les accès à la table juge.
 */
public class TableJuge
{
    private static PreparedStatement stmtExiste;
    private static PreparedStatement stmtInsert;
    private static PreparedStatement stmtSelect;
    private static PreparedStatement stmtRetirer;
    private static PreparedStatement stmtChangeDisponibilite;
    private Connexion cx;

    /**
     * Création d'une instance. Des énoncés SQL pour chaque requête sont
     * précompilés.
     * 
     * @param cx
     * @throws SQLException
     */
    public TableJuge(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtSelect = cx.getConnection().prepareStatement("select * from \"Juge\" where \"disponible\" = true");
        stmtExiste = cx.getConnection().prepareStatement("select * from \"Juge\" where \"id\" = ?");
        stmtInsert = cx.getConnection()
                .prepareStatement("insert into \"Juge\" (\"id\", \"prenom\", \"nom\", \"age\") values (?,?,?,?)");
        stmtRetirer = cx.getConnection().prepareStatement(
                "update \"Juge\" set \"quitterJustice\" = true, \"disponible\" = false where \"id\" = ?");
        stmtChangeDisponibilite = cx.getConnection()
                .prepareStatement("update \"Juge\" set \"disponible\" = ? where \"id\" = ?");
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
     * Objet juge associé à un juge de la base de données
     * 
     * @param tupleJuge
     * @return TupleJuge
     * @throws SQLException
     * @throws IFT287Exception
     */
    public TupleJuge getJuge(TupleJuge tupleJuge) throws SQLException, IFT287Exception
    {
        stmtExiste.setInt(1, tupleJuge.getId());
        ResultSet rset = stmtExiste.executeQuery();

        if (rset.next())
            tupleJuge = new TupleJuge(tupleJuge.getId(), rset.getString(2), rset.getString(3), rset.getInt(4));

        rset.close();
        return tupleJuge;
    }

    /**
     * Vérifie si le juge existe
     * 
     * @param tupleJuge
     * @return boolean
     * @throws SQLException
     */
    public boolean existe(TupleJuge tupleJuge) throws SQLException
    {
        stmtExiste.setInt(1, tupleJuge.getId());
        ResultSet rset = stmtExiste.executeQuery();
        boolean jugeExiste = rset.next();
        rset.close();
        return jugeExiste;
    }

    /**
     * Afficher la liste des juges actifs et disponibles
     * 
     * @return String
     * @throws SQLException
     * @throws IFT287Exception
     */
    public ArrayList<TupleJuge> affichage() throws SQLException, IFT287Exception
    {
        ArrayList<TupleJuge> listJuge = new ArrayList<TupleJuge>();

        ResultSet rset = stmtSelect.executeQuery();

        if (rset.next())
        {
            do
            {
                // Ajout de chacun des juges dans la liste
                listJuge.add(getJuge(new TupleJuge(rset.getInt(1))));
            }
            while (rset.next());
        }
        rset.close();
        return listJuge;
    }

    /**
     * Ajout d'un nouveau juge dans la base de données
     * 
     * @param tupleJuge
     * @throws SQLException
     */
    public void ajouter(TupleJuge tupleJuge) throws SQLException
    {
        stmtInsert.setInt(1, tupleJuge.getId());
        stmtInsert.setString(2, tupleJuge.getPrenom());
        stmtInsert.setString(3, tupleJuge.getNom());
        stmtInsert.setInt(4, tupleJuge.getAge());
        stmtInsert.executeUpdate();
    }

    /**
     * Retirer le juge de la base de données
     * 
     * @param tupleJuge
     * 
     * @throws SQLException
     */
    public void retirer(TupleJuge tupleJuge) throws SQLException
    {
        stmtRetirer.setInt(1, tupleJuge.getId());
        stmtRetirer.executeUpdate();
    }

    /**
     * Changer la disponibilite d'un juge
     * 
     * @param disponibilite
     * @param tupleJuge
     * @throws SQLException
     */
    public void changerDisponibilite(boolean disponibilite, TupleJuge tupleJuge) throws SQLException
    {
        stmtChangeDisponibilite.setBoolean(1, disponibilite);
        stmtChangeDisponibilite.setInt(2, tupleJuge.getId());
        stmtChangeDisponibilite.executeUpdate();
    }
}