package tp4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

/**
 * Permet d'effectuer les accès à la table proces.
 */
public class TableProces
{
    private TypedQuery<Proces> stmtExiste;
    private TypedQuery<Proces> stmtSelectProcesNonTermine;
    private TypedQuery<Proces> stmtVerificationProcesDecision;
    private TypedQuery<Proces> stmtProcesJugeEnCours;
    private TypedQuery<Proces> stmtVerificationProcesDevantJury;
    private TypedQuery<Proces> stmtSelectJugeDansProces;
    private TypedQuery<Proces> stmtTerminerProces;
    private TypedQuery<Proces> stmtInsertProces;
    private Connexion cx;

    /**
     * Constructeur de confort. Creation d'une instance. Précompilation
     * d'énoncés SQL.
     * 
     * @param cx
     */
    public TableProces(Connexion cx)
    {
        // Formater la date
        SimpleDateFormat formatAMJ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatAMJ.setLenient(false);
        Date currentDate = null;

        try
        {
            currentDate = formatAMJ.parse(LocalDateTime.now().toString());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery("select p from Proces where p.id = :id", Proces.class);
        stmtSelectProcesNonTermine = cx.getConnection()
                .createQuery("select p from Proces where p.id = :id and p.date < " + currentDate, Proces.class);
        stmtVerificationProcesDecision = cx.getConnection()
                .createQuery("select p from Proces where p.id = :id and p.decision = null", Proces.class);
        stmtProcesJugeEnCours = cx.getConnection()
                .createQuery("select p from Proces where p.Juge_id = :id and p.decision = null", Proces.class);
        stmtVerificationProcesDevantJury = cx.getConnection()
                .createQuery("select p from Proces where p.id = :id and p.devantJury = 1", Proces.class);
        stmtSelectJugeDansProces = cx.getConnection().createQuery("select p.juge from Proces where p.id = :id",
                Proces.class);
        stmtTerminerProces = cx.getConnection()
                .createQuery("update Proces set p.decision = :decisionProces where p.id = :id", Proces.class);
        stmtInsertProces = cx.getConnection().createQuery(
                "insert into Proces (p.id, p.Juge_id, p.date, p.devantJury, p.PartieDefenderesse, p.PartiePoursuivant values (:id,:juge,:date,:devantJury,:partieDefenderesse,:partiePoursuivant)",
                Proces.class);
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
     * @throws IFT287Exception
     */
    // public Proces getProces(Proces proces) throws IFT287Exception
    // {
    // stmtExiste.setParameter("id", proces.getId());
    // List<Proces> procesList = stmtExiste.getResultList();
    // if(!procesList.isEmpty())
    // return procesList.get(0);
    // else
    // return null;
    //
    // // Si la decision a été prise
    // if (proces.getDecision() != null)
    // proces.setDecision(rset.getInt(7));
    //
    // rset.close();
    // return proces;
    // }

    /**
     * Verification de l'existance d'un proces
     * 
     * @param proces
     * @return boolean
     */
    public boolean existe(Proces proces)
    {
        stmtExiste.setParameter("id", proces.getId());
        return !stmtExiste.getResultList().isEmpty();
    }

    /**
     * Affichage des elements de proces
     * 
     * @param proces
     * @return String
     * @throws IFT287Exception
     */
    // public Proces affichage(Proces proces) throws IFT287Exception
    // {
    // Proces tupleProcesReturn = null;
    //
    // stmtExiste.setInt(1, proces.getId());
    // ResultSet rset = stmtExiste.executeQuery();
    //
    // if (rset.next())
    // {
    // tupleProcesReturn = getProces(new Proces(rset.getInt(1)));
    // }
    //
    // rset.close();
    // return tupleProcesReturn;
    // }

    /**
     * Vérification que le proces a atteint sa date initiale
     * 
     * @param proces
     * @return boolean
     */
    public boolean compareDate(Proces proces)
    {
        stmtSelectProcesNonTermine.setParameter("id", proces.getId());
        return !stmtSelectProcesNonTermine.getResultList().isEmpty();
    }

    /**
     * Terminer le proces
     * 
     * @param decisionProces
     * @param proces
     * @return int
     */
    public int terminer(int decisionProces, Proces proces)
    {
        stmtTerminerProces.setParameter("decisionProces", decisionProces);
        stmtTerminerProces.setParameter("id", proces.getId());
        return stmtTerminerProces.executeUpdate();
    }

    /**
     * Rendre le juge disponible si il n'a plus de proces en cours
     * 
     * @param proces
     * @return int
     */
    public int changeJugeStatut(Proces proces)
    {
        List<Proces> idJuge;

        stmtSelectJugeDansProces.setParameter("id", proces.getId());
        idJuge = stmtSelectJugeDansProces.getResultList();

        if (!idJuge.isEmpty())
        {
            return idJuge.get(0).getId();
        }
        
        return -1;
    }

    /**
     * Verifier si un juge a des proces en cours
     * 
     * @param juge
     * @return boolean
     */
    public boolean jugeEnCours(Juge juge)
    {
        stmtProcesJugeEnCours.setParameter("id", juge.getId());
        return !stmtProcesJugeEnCours.getResultList().isEmpty();
    }

    /**
     * Ajout du proces
     * 
     * @param proces
     * @return int
     */
    public int creer(Proces proces)
    {
        stmtInsertProces.setParameter("id", proces.getId());
        stmtInsertProces.setParameter("juge", proces.getJuge());
        stmtInsertProces.setParameter("date", proces.getDate());
        stmtInsertProces.setParameter("devantJury", proces.getDevantJury());
        stmtInsertProces.setParameter("partieDefenderesse", proces.getPartieDefenderesse());
        stmtInsertProces.setParameter("partiePoursuivante", proces.getPartiePoursuivant());
        return stmtInsertProces.executeUpdate();
    }

    /**
     * Verification si le proces specifie n'est pas termine
     * 
     * @param proces
     * @return boolean
     */
    public boolean verifierProcesTermine(Proces proces)
    {
        stmtVerificationProcesDecision.setParameter("id", proces.getId());
        return !stmtVerificationProcesDecision.getResultList().isEmpty();
    }

    /**
     * Permet de savoir si un proces est devant un jury ou juge seul ou les deux
     * 
     * @param proces
     * @return boolean
     */
    public boolean devantJury(Proces proces)
    {
        stmtVerificationProcesDevantJury.setParameter("id", proces.getId());
        return !stmtVerificationProcesDevantJury.getResultList().isEmpty();
    }
}
