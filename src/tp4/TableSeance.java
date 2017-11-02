package tp4;

import java.util.List;

import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

/**
 * Permet d'effectuer les accès à la table seance.
 *
 */
public class TableSeance
{
    private Connexion cx;
    private TypedQuery<Seance> stmtExiste;
    private TypedQuery<Seance> stmtExisteProcesDansSeance;
    private TypedQuery<Seance> stmtSupprimerSeancesProcesTermine;
    private TypedQuery<Seance> stmtSeanceNonTerminee;

    /**
     * Constructeur de confort. Creation d'une instance. Précompilation
     * d'énoncés SQL.
     * 
     * @param cx
     */
    public TableSeance(Connexion cx)
    {
        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery("select s from Seance s where s.id = :idSeance", Seance.class);
        stmtExisteProcesDansSeance = cx.getConnection()
                .createQuery("select s from Seance s where s.proces.id = :idProces", Seance.class);
        stmtSupprimerSeancesProcesTermine = cx.getConnection().createQuery(
                "select s from Seance s, Proces p where p.id = :idProces and s.date > CURRENT_DATE", Seance.class);
        stmtSeanceNonTerminee = cx.getConnection()
                .createQuery("select s from Seance s where s.id = :idSeance and s.date < CURRENT_DATE", Seance.class);
    }

    /**
     * Affichage des seances lie a un proces
     * 
     * @param id
     * @return List<Seance>
     */
    public List<Seance> affichage(int id)
    {
        stmtExisteProcesDansSeance.setParameter("idProces", id);
        return stmtExisteProcesDansSeance.getResultList();
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
     * @param id
     * @return Seance
     */
    public Seance getSeance(int id)
    {
        stmtExiste.setParameter("idSeance", id);
        return stmtExiste.getSingleResult();
    }

    /**
     * Suppresion des seances prevues du proces
     * 
     * @param id
     * @throws Exception
     */
    public void supprimerSeancesProcesTermine(int id) throws Exception
    {
        stmtSupprimerSeancesProcesTermine.setParameter("idProces", id);

        for (Seance seance : stmtSupprimerSeancesProcesTermine.getResultList())
            supprimer(seance);
    }

    /**
     * Methode de traitement pour effectuerSupprimerSeance
     * 
     * @param seanceArg
     * @throws Exception
     */
    public void supprimer(Seance seanceArg) throws Exception
    {
        cx.getConnection().remove(seanceArg);
    }

    /**
     * Verification de l'existance d'un proces
     * 
     * @param id
     * @return boolean
     */
    public boolean existe(int id)
    {
        stmtExiste.setParameter("idSeance", id);
        return !stmtExiste.getResultList().isEmpty();
    }

    /**
     * Vérification que la seance n'est pas encore passee
     * 
     * @param id
     * @return boolean
     */
    public boolean seancePassee(int id)
    {
        stmtSeanceNonTerminee.setParameter("idSeance", id);
        return !stmtSeanceNonTerminee.getResultList().isEmpty();
    }

    /**
     * Ajout de la seance
     * 
     * @param seance
     * @return la nouvelle seance ajouté
     * @throws IllegalArgumentException
     * @throws TransactionRequiredException
     */
    public Seance ajout(Seance seance) throws IllegalArgumentException, TransactionRequiredException
    {
        cx.getConnection().persist(seance);
        return seance;
    }
}