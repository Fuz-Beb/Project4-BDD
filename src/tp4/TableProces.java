package tp4;

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
    private TypedQuery<Juge> stmtSelectJugeDansProces;
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
                .createQuery("select p from Proces p where p.id = :id and p.date < CURRENT_DATE", Proces.class);
        stmtVerificationProcesDecision = cx.getConnection()
                .createQuery("select p from Proces p where p.id = :id and p.decision is null", Proces.class);
        stmtProcesJugeEnCours = cx.getConnection()
                .createQuery("select p from Proces p where p.juge.id = :id and p.decision is null", Proces.class);
        stmtVerificationProcesDevantJury = cx.getConnection()
                .createQuery("select p from Proces p where p.id = :id and p.devantJury = 1", Proces.class);
        stmtSelectJugeDansProces = cx.getConnection().createQuery("select p.juge from Proces p where p.id = :id",
                Juge.class);
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
     * @param id
     * @return boolean
     */
    public boolean existe(int id)
    {
        stmtExiste.setParameter("id", id);
        return !stmtExiste.getResultList().isEmpty();
    }

    /**
     * Affichage des elements de proces
     * 
     * @param id
     * @return String
     * @throws IFT287Exception
     */
    public Proces affichage(int id) throws IFT287Exception
    {
        stmtExiste.setParameter("id", id);
        return stmtExiste.getSingleResult();
    }

    /**
     * Vérification que le proces a atteint sa date initiale
     * 
     * @param id
     * @return boolean
     */
    public boolean compareDate(int id)
    {
        stmtSelectProcesNonTermine.setParameter("id", id);
        return !stmtSelectProcesNonTermine.getResultList().isEmpty();
    }

    /**
     * Terminer le proces
     * 
     * @param decision
     * @param id
     * @return boolean
     */
    public boolean terminer(String decision, int id)
    {
        TypedQuery<Proces> changerDecision = cx.getConnection()
                .createQuery("update Proces p SET p.decision = :decision where p.id = :id", Proces.class);
        changerDecision.setParameter("id", id);
        changerDecision.setParameter("decision", decision);

        // Si on a bien effectué les modifications alors on retourne vrai
        if (changerDecision.executeUpdate() == 1)
        {
            return true;
        }

        return false;
    }

    /**
     * Permet de récuperer le juge du proces
     * 
     * @param id
     * @return int
     */
    public int getJugeProces(int id)
    {
        List<Juge> idJuge;

        stmtSelectJugeDansProces.setParameter("id", id);
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
     * @param id
     * @return boolean
     */
    public boolean jugeEnCours(int id)
    {
        stmtProcesJugeEnCours.setParameter("id", id);
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
     * @param idProces
     * @return boolean
     */
    public boolean devantJury(int idProces)
    {
        stmtVerificationProcesDevantJury.setParameter("id", idProces);
        return !stmtVerificationProcesDevantJury.getResultList().isEmpty();
    }
}
