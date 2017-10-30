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
     * @param juge
     * @return Juge
     * @throws SQLException
     * @throws IFT287Exception
     */
    public Juge getJuge(Juge juge) throws SQLException, IFT287Exception
    {
        stmtExiste.setInt(1, juge.getId());
        ResultSet rset = stmtExiste.executeQuery();

        if (rset.next())
            juge = new Juge(juge.getId(), rset.getString(2), rset.getString(3), rset.getInt(4));

        rset.close();
        return juge;
    }

    /**
     * Vérifie si le juge existe
     * 
     * @param juge
     * @return boolean
     * @throws SQLException
     */
    public boolean existe(Juge juge) throws SQLException
    {
        stmtExiste.setInt(1, juge.getId());
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
    public ArrayList<Juge> affichage() throws SQLException, IFT287Exception
    {
        ArrayList<Juge> listJuge = new ArrayList<Juge>();

        ResultSet rset = stmtSelect.executeQuery();

        if (rset.next())
        {
            do
            {
                // Ajout de chacun des juges dans la liste
                listJuge.add(getJuge(new Juge(rset.getInt(1))));
            }
            while (rset.next());
        }
        rset.close();
        return listJuge;
    }

    /**
     * Ajout d'un nouveau juge dans la base de données
     * 
     * @param juge
     * @throws SQLException
     */
    public void ajouter(Juge juge) throws SQLException
    {
        stmtInsert.setInt(1, juge.getId());
        stmtInsert.setString(2, juge.getPrenom());
        stmtInsert.setString(3, juge.getNom());
        stmtInsert.setInt(4, juge.getAge());
        stmtInsert.executeUpdate();
    }

    /**
     * Retirer le juge de la base de données
     * 
     * @param juge
     * 
     * @throws SQLException
     */
    public void retirer(Juge juge) throws SQLException
    {
        stmtRetirer.setInt(1, juge.getId());
        stmtRetirer.executeUpdate();
    }

    /**
     * Changer la disponibilite d'un juge
     * 
     * @param disponibilite
     * @param juge
     * @throws SQLException
     */
    public void changerDisponibilite(boolean disponibilite, Juge juge) throws SQLException
    {
        stmtChangeDisponibilite.setBoolean(1, disponibilite);
        stmtChangeDisponibilite.setInt(2, juge.getId());
        stmtChangeDisponibilite.executeUpdate();
    }
}