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
     * @param tupleProces
     * @return String
     * @throws SQLException
     */
    public ArrayList<TupleSeance> affichage(TupleProces tupleProces) throws SQLException
    {
        ArrayList<TupleSeance> listSeance = new ArrayList<TupleSeance>();

        stmtExisteProcesDansSeance.setInt(1, tupleProces.getId());
        ResultSet rset = stmtExisteProcesDansSeance.executeQuery();

        if (rset.next())
        {
            do
            {
                // Ajout de chacun des juges dans la liste
                listSeance.add(getSeance(new TupleSeance(rset.getInt(1))));
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
     * @param tupleSeance
     * @return TupleSeance
     * @throws SQLException
     */
    public TupleSeance getSeance(TupleSeance tupleSeance) throws SQLException
    {
        stmtExisteSeance.setInt(1, tupleSeance.getId());
        ResultSet rset = stmtExisteSeance.executeQuery();

        if (rset.next())
            tupleSeance = new TupleSeance(tupleSeance.getId(), rset.getInt(2), rset.getDate(3));

        rset.close();
        return tupleSeance;
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
            supprimer(new TupleSeance(rset.getInt(1)));
        }

        rset.close();
    }

    /**
     * Methode de traitement pour effectuerSupprimerSeance
     * 
     * @param tupleSeance
     * 
     * @throws IFT287Exception
     * @throws SQLException
     */
    public void supprimer(TupleSeance tupleSeance) throws IFT287Exception, SQLException
    {
        stmtSupprimerSeance.setInt(1, tupleSeance.getId());
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
     * @param tupleSeance
     * @throws SQLException
     */
    public void ajout(TupleSeance tupleSeance) throws SQLException
    {
        stmtInsertSeance.setInt(1, tupleSeance.getId());
        stmtInsertSeance.setInt(2, tupleSeance.getProces_id());
        stmtInsertSeance.setDate(3, tupleSeance.getDate());
        stmtInsertSeance.executeUpdate();
    }
}
