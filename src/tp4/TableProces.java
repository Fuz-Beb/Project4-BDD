package tp4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.TransactionRequiredException;
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
    private Connexion cx;

    /**
     * Constructeur de confort. Creation d'une instance. Précompilation
     * d'énoncés SQL.
     * 
     * @param cx
     */
    public TableProces(Connexion cx)
    {
        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery("select p from Proces p where p.id = :id", Proces.class);
        stmtSelectProcesNonTermine = cx.getConnection()
                .createQuery("select p from Proces p where p.id = :id and p.date < :date", Proces.class);
        stmtVerificationProcesDecision = cx.getConnection()
                .createQuery("select p from Proces p where p.id = :id and p.decision = null", Proces.class);
        stmtProcesJugeEnCours = cx.getConnection()
                .createQuery("select p from Proces p where p.juge = :id and p.decision = null", Proces.class);
        stmtVerificationProcesDevantJury = cx.getConnection()
                .createQuery("select p from Proces p where p.id = :id and p.devantJury = 1", Proces.class);
        stmtSelectJugeDansProces = cx.getConnection().createQuery("select p.juge from Proces p where p.id = :id",
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
     * @param id
     * @return Proces
     * @throws Exception
     */
    public Proces getProces(int id) throws Exception
    {
        stmtExiste.setParameter("id", id);
        return stmtExiste.getSingleResult();
    }

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
    public Proces affichage(Proces proces) throws IFT287Exception
    {
        stmtExiste.setParameter("id", proces.getId());
        return stmtExiste.getSingleResult();
    }

    /**
     * Vérification que le proces a atteint sa date initiale
     * 
     * @param proces
     * @return boolean
     */
    public boolean compareDate(Proces proces)
    {
        stmtSelectProcesNonTermine.setParameter("id", proces.getId());
        stmtSelectProcesNonTermine.setParameter("date", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        return !stmtSelectProcesNonTermine.getResultList().isEmpty();
    }

    /**
     * Terminer le proces
     * 
     * @param decisionProces
     * @param proces
     */
    public void terminer(int decisionProces, Proces proces)
    {
        proces.setDecision(decisionProces);
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
     * @return Proces
     * @throws IllegalArgumentException
     * @throws TransactionRequiredException
     */
    public Proces creer(Proces proces) throws IllegalArgumentException, TransactionRequiredException
    {
        cx.getConnection().persist(proces);
        return proces;
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
