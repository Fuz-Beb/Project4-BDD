package tp4;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Permet d'effectuer les accès à la table jury.
 */
public class TableJury
{
    private static PreparedStatement stmtExiste;
    private static PreparedStatement stmtInsert;
    private static PreparedStatement stmtSelect;
    private static PreparedStatement stmtInsertProcesDansJury;
    private Connexion cx;

    /**
     * Création d'une instance. Des énoncés SQL pour chaque requête sont
     * précompilés.
     * 
     * @param cx
     * @throws SQLException
     */
    public TableJury(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtSelect = cx.getConnection().prepareStatement("select * from \"Jury\" where \"Proces_id\" is null");
        stmtInsertProcesDansJury = cx.getConnection()
                .prepareStatement("update \"Jury\" set \"Proces_id\" = ? where \"nas\" = ?");
        stmtExiste = cx.getConnection().prepareStatement("select * from \"Jury\" where \"nas\" = ?");
        stmtInsert = cx.getConnection().prepareStatement(
                "insert into \"Jury\" (\"nas\", \"prenom\", \"nom\", \"sexe\", \"age\", \"Proces_id\") "
                        + "values (?,?,?,?,?,null)");
    }

    /**
     * Retourne la commande associée
     * 
     * @return Connexion
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Objet jury associé à un jury de la base de données
     * 
     * @param jury 
     * @return Jury
     * @throws SQLException
     * @throws IFT287Exception
     */
    public Jury getJury(Jury jury) throws SQLException, IFT287Exception
    {

        stmtExiste.setInt(1, jury.getNas());
        ResultSet rset = stmtExiste.executeQuery();

        if (rset.next())
            jury = new Jury(jury.getNas(), rset.getString(2), rset.getString(3), rset.getString(4), rset.getInt(5));

        // Si la decision a été prise
        if (rset.getObject(6) != null)
            jury.setProces_id(rset.getInt(6));

        rset.close();
        return jury;
    }

    /**
     * Vérifie si le jury existe
     * 
     * @param jury 
     * @return boolean
     * @throws SQLException
     */
    public boolean existe(Jury jury) throws SQLException
    {
        stmtExiste.setInt(1, jury.getNas());
        ResultSet rset = stmtExiste.executeQuery();
        boolean juryExiste = rset.next();
        rset.close();
        return juryExiste;
    }

    /**
     * Affiche la liste des jurys
     * 
     * @return String
     * @throws SQLException
     * @throws IFT287Exception
     */
    public ArrayList<Jury> affichage() throws SQLException, IFT287Exception
    {
        ArrayList<Jury> listJury = new ArrayList<Jury>();

        ResultSet rset = stmtSelect.executeQuery();

        if (rset.next())
        {
            do
            {
                // Ajout de chacun des juges dans la liste
                listJury.add(getJury(new Jury(rset.getInt(1))));
            }
            while (rset.next());
        }
        rset.close();
        return listJury;
    }

    /**
     * Ajout d'un nouveau jury dans la base de données
     * 
     * @param jury
     * @throws SQLException
     */
    public void ajouter(Jury jury) throws SQLException
    {
        stmtInsert.setInt(1, jury.getNas());
        stmtInsert.setString(2, jury.getPrenom());
        stmtInsert.setString(3, jury.getNom());
        stmtInsert.setObject(4, jury.getSexe());
        stmtInsert.setInt(5, jury.getAge());
        stmtInsert.executeUpdate();
    }

    /**
     * Assigner un proces à un jury
     * 
     * @param proces
     * @param jury
     * @throws SQLException
     */
    public void assignerProces(Jury jury, Proces proces) throws SQLException
    {
        stmtInsertProcesDansJury.setInt(1, proces.getId());
        stmtInsertProcesDansJury.setInt(2, jury.getNas());
        stmtInsertProcesDansJury.executeUpdate();
    }
}