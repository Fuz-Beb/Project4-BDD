package tp4;

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
     * @param proces
     * @return Juge
     * @throws SQLException
     * @throws IFT287Exception
     */
    public Proces getProces(Proces proces) throws SQLException, IFT287Exception
    {
        stmtExisteProces.setInt(1, proces.getId());
        ResultSet rset = stmtExisteProces.executeQuery();

        if (rset.next())
            proces = new Proces(proces.getId(), rset.getInt(2), rset.getDate(3), rset.getInt(4),
                    rset.getInt(5), rset.getInt(6));

        // Si la decision a été prise
        if (rset.getObject(7) != null)
            proces.setDecision(rset.getInt(7));

        rset.close();
        return proces;
    }

    /**
     * Verification de l'existance d'un proces
     * 
     * @param proces
     * @return boolean
     * @throws SQLException
     */
    public boolean existe(Proces proces) throws SQLException
    {
        stmtExisteProces.setInt(1, proces.getId());
        ResultSet rset = stmtExisteProces.executeQuery();
        boolean procesExiste = rset.next();
        rset.close();
        return procesExiste;
    }

    /**
     * Affichage des elements de proces
     * 
     * @param proces
     * @return String
     * @throws SQLException
     * @throws IFT287Exception
     */
    public Proces affichage(Proces proces) throws SQLException, IFT287Exception
    {
        Proces tupleProcesReturn = null;

        stmtExisteProces.setInt(1, proces.getId());
        ResultSet rset = stmtExisteProces.executeQuery();

        if (rset.next())
        {
            tupleProcesReturn = getProces(new Proces(rset.getInt(1)));
        }

        rset.close();
        return tupleProcesReturn;
    }

    /**
     * Vérification que le proces a atteint sa date initiale
     * 
     * @param proces
     * @return boolean
     * @throws SQLException
     */
    public boolean compareDate(Proces proces) throws SQLException
    {
        stmtSelectProcesNonTermine.setInt(1, proces.getId());
        ResultSet rset = stmtSelectProcesNonTermine.executeQuery();
        boolean compareDate = rset.next();
        rset.close();
        return compareDate;
    }

    /**
     * Terminer le proces
     * 
     * @param decisionProces
     * @param proces
     * @throws SQLException
     */
    public void terminer(int decisionProces, Proces proces) throws SQLException
    {
        stmtTerminerProces.setInt(1, decisionProces);
        stmtTerminerProces.setInt(2, proces.getId());
        stmtTerminerProces.executeUpdate();
    }

    /**
     * Rendre le juge disponible si il n'a plus de proces en cours
     * 
     * @param proces
     * @return int
     * @throws SQLException
     */
    public int changeJugeStatut(Proces proces) throws SQLException
    {
        int idJuge = 0;

        stmtSelectJugeDansProces.setInt(1, proces.getId());
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
     * @param juge
     * @return boolean
     * @throws SQLException
     */
    public boolean jugeEnCours(Juge juge) throws SQLException
    {
        stmtProcesJugeEnCours.setInt(1, juge.getId());
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
     * @param proces
     * @throws SQLException
     */
    public void creer(Proces proces) throws SQLException
    {
        stmtInsertProces.setInt(1, proces.getId());
        stmtInsertProces.setInt(2, proces.getJuge_id());
        stmtInsertProces.setDate(3, proces.getDate());
        stmtInsertProces.setInt(4, proces.getDevantJury());
        stmtInsertProces.setInt(5, proces.getPartieDefenderesse_id());
        stmtInsertProces.setInt(6, proces.getPartiePoursuivant_id());
        stmtInsertProces.executeUpdate();
    }

    /**
     * Verification si le proces specifie n'est pas termine
     * 
     * @param proces
     * @return boolean
     * @throws SQLException
     */
    public boolean verifierProcesTermine(Proces proces) throws SQLException
    {
        stmtVerificationProcesDecision.setInt(1, proces.getId());
        ResultSet rset = stmtVerificationProcesDecision.executeQuery();
        boolean procesTermine = rset.next();
        rset.close();
        return procesTermine;
    }

    /**
     * Permet de savoir si un proces est devant un jury ou juge seul ou les deux
     * 
     * @param proces
     * @return boolean
     * @throws SQLException
     */
    public boolean devantJury(Proces proces) throws SQLException
    {
        stmtVerificationProcesDevantJury.setInt(1, proces.getId());
        ResultSet rset = stmtVerificationProcesDevantJury.executeQuery();
        boolean devantJury = rset.next();
        rset.close();
        return devantJury;
    }
}
