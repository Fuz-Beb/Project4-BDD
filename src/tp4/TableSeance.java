package tp4;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Permet d'effectuer les accès à la table seance.
 *
 */
public class TableSeance
{
    private PreparedStatement stmtExisteSeance;
    private PreparedStatement stmtInsertSeance;
    private PreparedStatement stmtExisteProcesDansSeance;
    private PreparedStatement stmtSupprimerSeancesProcesTermine;
    private PreparedStatement stmtSeanceNonTerminee;
    private PreparedStatement stmtSupprimerSeance;
    private Connexion cx;

    /**
     * Constructeur de confort. Creation d'une instance. Précompilation
     * d'énoncés SQL.
     * 
     * @param cx
     * @throws SQLException
     */
    public TableSeance(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtExisteSeance = cx.getConnection().prepareStatement("select * from \"Seance\" where id = ?");
        stmtExisteProcesDansSeance = cx.getConnection()
                .prepareStatement("select * from \"Seance\" where \"Proces_id\" = ?");
        stmtSupprimerSeancesProcesTermine = cx.getConnection()
                .prepareStatement("select * from \"Seance\" where \"Proces_id\" = ? and date > current_date");
        stmtSeanceNonTerminee = cx.getConnection()
                .prepareStatement("select * from \"Seance\" where \"id\" = ? and \"date\" < current_date");
        stmtSupprimerSeance = cx.getConnection().prepareStatement("delete from \"Seance\" where \"id\" = ?");
        stmtInsertSeance = cx.getConnection()
                .prepareStatement("insert into \"Seance\" (\"id\", \"Proces_id\", \"date\") values (?,?,?)");

    }

    /**
     * Affichage des seances lie a un proces
     * 
     * @param proces
     * @return String
     * @throws SQLException
     */
    public ArrayList<Seance> affichage(Proces proces) throws SQLException
    {
        ArrayList<Seance> listSeance = new ArrayList<Seance>();

        stmtExisteProcesDansSeance.setInt(1, proces.getId());
        ResultSet rset = stmtExisteProcesDansSeance.executeQuery();

        if (rset.next())
        {
            do
            {
                // Ajout de chacun des juges dans la liste
                listSeance.add(getSeance(new Seance(rset.getInt(1))));
            }
            while (rset.next());
        }

        rset.close();
        return listSeance;
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
     * Objet seance associé à une seance de la base de données
     * 
     * @param seance
     * @return Seance
     * @throws SQLException
     */
    public Seance getSeance(Seance seance) throws SQLException
    {
        stmtExisteSeance.setInt(1, seance.getId());
        ResultSet rset = stmtExisteSeance.executeQuery();

        if (rset.next())
            seance = new Seance(seance.getId(), rset.getInt(2), rset.getDate(3));

        rset.close();
        return seance;
    }

    /**
     * Suppresion des seances prevues du proces
     * 
     * @param id
     * @throws SQLException
     * @throws IFT287Exception
     */
    public void supprimerSeancesProcesTermine(int id) throws SQLException, IFT287Exception
    {
        stmtSupprimerSeancesProcesTermine.setInt(1, id);
        ResultSet rset = stmtSupprimerSeancesProcesTermine.executeQuery();

        // Suppression des seances une a une
        while (rset.next())
        {
            supprimer(new Seance(rset.getInt(1)));
        }

        rset.close();
    }

    /**
     * Methode de traitement pour effectuerSupprimerSeance
     * 
     * @param seance
     * 
     * @throws IFT287Exception
     * @throws SQLException
     */
    public void supprimer(Seance seance) throws IFT287Exception, SQLException
    {
        stmtSupprimerSeance.setInt(1, seance.getId());
        stmtSupprimerSeance.executeUpdate();
    }

    /**
     * Verification de l'existance d'un proces
     * 
     * @param id
     * @return boolean
     * @throws SQLException
     */
    public boolean existe(int id) throws SQLException
    {
        stmtExisteSeance.setInt(1, id);
        ResultSet rset = stmtExisteSeance.executeQuery();
        boolean seanceExiste = rset.next();
        rset.close();
        return seanceExiste;
    }

    /**
     * Vérification que la seance n'est pas encore passee
     * 
     * @param id
     * @return boolean
     * @throws SQLException
     */
    public boolean seancePassee(int id) throws SQLException
    {
        stmtSeanceNonTerminee.setInt(1, id);
        ResultSet rset = stmtSeanceNonTerminee.executeQuery();
        boolean seancePassee = rset.next();
        rset.close();
        return seancePassee;
    }

    /**
     * Ajout de la seance
     * 
     * @param seance
     * @throws SQLException
     */
    public void ajout(Seance seance) throws SQLException
    {
        stmtInsertSeance.setInt(1, seance.getId());
        stmtInsertSeance.setInt(2, seance.getProces_id());
        stmtInsertSeance.setDate(3, seance.getDate());
        stmtInsertSeance.executeUpdate();
    }
}
