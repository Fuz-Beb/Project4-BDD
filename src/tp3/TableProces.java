package tp3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Permet d'effectuer les accès à la table proces.
 */
public class TableProces
{
    private PreparedStatement stmtExisteProces;
    private PreparedStatement stmtInsertProces;
    private PreparedStatement stmtSelectProcesNonTermine;
    private PreparedStatement stmtTerminerProces;
    private PreparedStatement stmtVerificationProcesDecision;
    private PreparedStatement stmtProcesJugeEnCours;
    private PreparedStatement stmtVerificationProcesDevantJury;
    private PreparedStatement stmtSelectJugeDansProces;
    private Connexion cx;

    /**
     * Constructeur de confort. Creation d'une instance. Précompilation
     * d'énoncés SQL.
     * 
     * @param cx
     * @throws SQLException
     */
    public TableProces(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtExisteProces = cx.getConnection().prepareStatement("select * from \"Proces\" where \"id\" = ?");
        stmtSelectProcesNonTermine = cx.getConnection()
                .prepareStatement("select * from \"Proces\" where \"id\" = ? and \"date\" < current_date");
        stmtTerminerProces = cx.getConnection()
                .prepareStatement("update \"Proces\" set \"decision\" = ? where \"id\" = ?");
        stmtVerificationProcesDecision = cx.getConnection()
                .prepareStatement("select * from \"Proces\" where \"id\" = ? and \"decision\" is null");
        stmtInsertProces = cx.getConnection().prepareStatement(
                "insert into \"Proces\" (\"id\", \"Juge_id\", \"date\", \"devantJury\", \"PartieDefenderesse_id\", \"PartiePoursuivant_id\") "
                        + "values (?,?,?,?,?,?)");
        stmtProcesJugeEnCours = cx.getConnection()
                .prepareStatement("select * from \"Proces\" where \"Juge_id\" = ? and \"decision\" is null");
        stmtVerificationProcesDevantJury = cx.getConnection()
                .prepareStatement("select from \"Proces\" where \"id\" = ? and \"devantJury\" = 1");
        stmtSelectJugeDansProces = cx.getConnection()
                .prepareStatement("select \"Juge_id\" from \"Proces\" where \"id\" = ?");
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
     * Objet proces associé à un proces de la base de données
     * 
     * @param tupleProces
     * @return TupleJuge
     * @throws SQLException
     * @throws IFT287Exception
     */
    public TupleProces getProces(TupleProces tupleProces) throws SQLException, IFT287Exception
    {
        stmtExisteProces.setInt(1, tupleProces.getId());
        ResultSet rset = stmtExisteProces.executeQuery();

        if (rset.next())
            tupleProces = new TupleProces(tupleProces.getId(), rset.getInt(2), rset.getDate(3), rset.getInt(4),
                    rset.getInt(5), rset.getInt(6));

        // Si la decision a été prise
        if (rset.getObject(7) != null)
            tupleProces.setDecision(rset.getInt(7));

        rset.close();
        return tupleProces;
    }

    /**
     * Verification de l'existance d'un proces
     * 
     * @param tupleProces
     * @return boolean
     * @throws SQLException
     */
    public boolean existe(TupleProces tupleProces) throws SQLException
    {
        stmtExisteProces.setInt(1, tupleProces.getId());
        ResultSet rset = stmtExisteProces.executeQuery();
        boolean procesExiste = rset.next();
        rset.close();
        return procesExiste;
    }

    /**
     * Affichage des elements de proces
     * 
     * @param tupleProces
     * @return String
     * @throws SQLException
     * @throws IFT287Exception
     */
    public TupleProces affichage(TupleProces tupleProces) throws SQLException, IFT287Exception
    {
        TupleProces tupleProcesReturn = null;

        stmtExisteProces.setInt(1, tupleProces.getId());
        ResultSet rset = stmtExisteProces.executeQuery();

        if (rset.next())
        {
            tupleProcesReturn = getProces(new TupleProces(rset.getInt(1)));
        }

        rset.close();
        return tupleProcesReturn;
    }

    /**
     * Vérification que le proces a atteint sa date initiale
     * 
     * @param tupleProces
     * @return boolean
     * @throws SQLException
     */
    public boolean compareDate(TupleProces tupleProces) throws SQLException
    {
        stmtSelectProcesNonTermine.setInt(1, tupleProces.getId());
        ResultSet rset = stmtSelectProcesNonTermine.executeQuery();
        boolean compareDate = rset.next();
        rset.close();
        return compareDate;
    }

    /**
     * Terminer le proces
     * 
     * @param decisionProces
     * @param tupleProces
     * @throws SQLException
     */
    public void terminer(int decisionProces, TupleProces tupleProces) throws SQLException
    {
        stmtTerminerProces.setInt(1, decisionProces);
        stmtTerminerProces.setInt(2, tupleProces.getId());
        stmtTerminerProces.executeUpdate();
    }

    /**
     * Rendre le juge disponible si il n'a plus de proces en cours
     * 
     * @param tupleProces
     * @return int
     * @throws SQLException
     */
    public int changeJugeStatut(TupleProces tupleProces) throws SQLException
    {
        int idJuge = 0;

        stmtSelectJugeDansProces.setInt(1, tupleProces.getId());
        ResultSet rset = stmtSelectJugeDansProces.executeQuery();

        if (rset.next())
        {
            idJuge = rset.getInt(1);
        }

        rset.close();

        return idJuge;
    }

    /**
     * Verifier si un juge a des proces en cours
     * 
     * @param tupleJuge
     * @return boolean
     * @throws SQLException
     */
    public boolean jugeEnCours(TupleJuge tupleJuge) throws SQLException
    {
        stmtProcesJugeEnCours.setInt(1, tupleJuge.getId());
        ResultSet rset = stmtProcesJugeEnCours.executeQuery();

        if (rset.next())
        {
            return true;
        }

        rset.close();

        return false;
    }

    /**
     * Ajout du proces
     * 
     * @param tupleProces
     * @throws SQLException
     */
    public void creer(TupleProces tupleProces) throws SQLException
    {
        stmtInsertProces.setInt(1, tupleProces.getId());
        stmtInsertProces.setInt(2, tupleProces.getJuge_id());
        stmtInsertProces.setDate(3, tupleProces.getDate());
        stmtInsertProces.setInt(4, tupleProces.getDevantJury());
        stmtInsertProces.setInt(5, tupleProces.getPartieDefenderesse_id());
        stmtInsertProces.setInt(6, tupleProces.getPartiePoursuivant_id());
        stmtInsertProces.executeUpdate();
    }

    /**
     * Verification si le proces specifie n'est pas termine
     * 
     * @param tupleProces
     * @return boolean
     * @throws SQLException
     */
    public boolean verifierProcesTermine(TupleProces tupleProces) throws SQLException
    {
        stmtVerificationProcesDecision.setInt(1, tupleProces.getId());
        ResultSet rset = stmtVerificationProcesDecision.executeQuery();
        boolean procesTermine = rset.next();
        rset.close();
        return procesTermine;
    }

    /**
     * Permet de savoir si un proces est devant un jury ou juge seul ou les deux
     * 
     * @param tupleProces
     * @return boolean
     * @throws SQLException
     */
    public boolean devantJury(TupleProces tupleProces) throws SQLException
    {
        stmtVerificationProcesDevantJury.setInt(1, tupleProces.getId());
        ResultSet rset = stmtVerificationProcesDevantJury.executeQuery();
        boolean devantJury = rset.next();
        rset.close();
        return devantJury;
    }
}
