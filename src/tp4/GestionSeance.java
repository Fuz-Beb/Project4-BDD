package tp4;

import java.util.List;

/**
 * Gestion des transactions de la table seance.
 */
public class GestionSeance
{
    private TableSeance seance;
    private TableProces proces;
    private Connexion cx;

    /**
     * Constructeur de confort
     * 
     * @param seance
     * @param proces
     * @throws IFT287Exception
     */
    public GestionSeance(TableSeance seance, TableProces proces) throws IFT287Exception
    {
        this.cx = seance.getConnexion();
        if (seance.getConnexion() != proces.getConnexion())
            throw new IFT287Exception(
                    "Les instances de TableSeance et de TableProces n'utilisent pas la même connexion au serveur");
        this.seance = seance;
        this.proces = proces;
    }

    /**
     * Ajout d'une nouvelle seance dans la base de données. S'il existe déjà,
     * une exception est levée.
     * 
     * @param seanceArg
     * @throws Exception
     */
    public void ajout(Seance seanceArg) throws Exception
    {
        try
        {
            cx.getConnection().getTransaction().begin();

            // Vérification si la seance existe deja
            if (seance.existe(seanceArg.getId()))
                throw new IFT287Exception("La seance existe deja: " + seanceArg.getId());

            // Verification si le proces existe
            if (!proces.existe(seanceArg.getProces().getId()))
                throw new IFT287Exception("Le proces " + seanceArg.getProces().getId() + " n'existe pas.");

            // Verification si le proces specifie n'est pas termine
            if (!proces.verifierProcesTermine(new Proces(seanceArg.getProces().getId())))
                throw new IFT287Exception("Le proces " + seanceArg.getProces().getId() + " est termine.");

            seance.ajout(seanceArg);

            cx.getConnection().getTransaction().commit();
        }
        finally
        {
            if (cx.getConnection().getTransaction().isActive())
                cx.getConnection().getTransaction().rollback();
        }
    }

    /**
     * Supprimer une seance
     * 
     * @param seanceArg
     * @throws Exception
     */
    public void supprimer(Seance seanceArg) throws Exception
    {
        try
        {
            cx.getConnection().getTransaction().begin();

            // Vérification si la seance existe
            if (!seance.existe(seanceArg.getId()))
                throw new IFT287Exception("La seance n'existe pas : " + seanceArg.getId());

            // Vérification que la seance n'est pas encore passée
            if (seance.seancePassee(seanceArg.getId()))
                throw new IFT287Exception("La seance " + seanceArg.getId() + " est déjà passée.");

            seance.supprimer(seanceArg);

            cx.getConnection().getTransaction().commit();
        }
        finally
        {
            if (cx.getConnection().getTransaction().isActive())
                cx.getConnection().getTransaction().rollback();
        }
    }

    /**
     * Retourne la liste des seances liées à un proces pour affichage
     * 
     * @param procesArg
     * @return List<Seance>
     * @throws IFT287Exception
     */
    public List<Seance> affichage(Proces procesArg) throws IFT287Exception
    {
        List<Seance> list = null;
        try
        {
            cx.getConnection().getTransaction().begin();

            if (!proces.existe(procesArg.getId()))
                throw new IFT287Exception("Le proces " + procesArg.getId() + "n'existe pas");
            else
                list = seance.affichage(procesArg);

            cx.getConnection().getTransaction().commit();

            return list;
        }
        finally
        {
            if (cx.getConnection().getTransaction().isActive())
                cx.getConnection().getTransaction().rollback();
        }
    }
}