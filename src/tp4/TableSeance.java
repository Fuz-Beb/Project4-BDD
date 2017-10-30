package tp4;

import java.text.SimpleDateFormat;
import java.util.Date;
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
                .createQuery("select s from Seance s, s.proces p where p.id = :idProces", Seance.class);
        stmtSupprimerSeancesProcesTermine = cx.getConnection().createQuery(
                "select s from Seance s, s.proces p where p.id = :idProces and s.date > :date", Seance.class);
        stmtSeanceNonTerminee = cx.getConnection()
                .createQuery("select s from Seance s where s.id = :idSeance and s.date < :date", Seance.class);
    }

    /**
     * Affichage des seances lie a un proces
     * 
     * @param proces
     * @return String
     * @throws SQLException
     */
    /*
     * public ArrayList<Seance> affichage(Proces proces) throws SQLException {
     * ArrayList<Seance> listSeance = new ArrayList<Seance>();
     * 
     * stmtExisteProcesDansSeance.setInt(1, proces.getId()); ResultSet rset =
     * stmtExisteProcesDansSeance.executeQuery();
     * 
     * if (rset.next()) { do { // Ajout de chacun des juges dans la liste
     * listSeance.add(getSeance(new Seance(rset.getInt(1)))); } while
     * (rset.next()); }
     * 
     * rset.close(); return listSeance; }
     */

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
        stmtExiste.setParameter(":idSeance", id);
        return stmtExiste.getSingleResult();
    }

    /**
     * Suppresion des seances prevues du proces
     * 
     * @param id
     * @throws IFT287Exception
     */
    public void supprimerSeancesProcesTermine(int id) throws IFT287Exception
    {
        stmtSupprimerSeancesProcesTermine.setParameter(":idProces", id);
        stmtSupprimerSeancesProcesTermine.setParameter(":date",
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

        for (Seance seance : stmtSupprimerSeancesProcesTermine.getResultList())
            supprimer(new Seance(seance.getId()));
    }

    /**
     * Methode de traitement pour effectuerSupprimerSeance
     * 
     * @param seance
     * @return le résultat de la suppression
     * 
     * @throws IFT287Exception
     */
    public boolean supprimer(Seance seance) throws IFT287Exception
    {
        if (seance != null)
        {
            cx.getConnection().remove(seance);
            return true;
        }
        return false;
    }

    /**
     * Verification de l'existance d'un proces
     * 
     * @param id
     * @return boolean
     */
    public boolean existe(int id)
    {
        stmtExiste.setParameter(":idSeance", id);
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
        stmtSeanceNonTerminee.setParameter(":idSeance", id);
        stmtSeanceNonTerminee.setParameter(":date", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        return !stmtSeanceNonTerminee.getResultList().isEmpty();
    }

    /**
     * Ajout de la seance
     * 
     * @param seance
     * @return la nouvelle seance ajouté
     */
    public Seance ajout(Seance seance)
    {
        cx.getConnection().persist(seance);
        return seance;
    }
}