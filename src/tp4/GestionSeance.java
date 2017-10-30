package tp4;

import java.util.ArrayList;

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
     * @param seance
     * @throws Exception
     */
    public void ajout(Seance seance) throws Exception
    {
        try
        {
            // Vérification si la seance existe deja
            if (seance.existe(seance.getId()))
                throw new IFT287Exception("La seance existe deja: " + seance.getId());

            // Verification si le proces existe
            if (!proces.existe(new Proces(seance.getProces_id())))
                throw new IFT287Exception("Le proces " + seance.getProces_id() + " n'existe pas.");

            // Verification si le proces specifie n'est pas termine
            if (!proces.verifierProcesTermine(new Proces(seance.getProces_id())))
                throw new IFT287Exception("Le proces " + seance.getProces_id() + " est termine.");

            seance.ajout(seance);

            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * Supprimer une seance
     * 
     * @param seance
     * @throws Exception
     */
    public void supprimer(Seance seance) throws Exception
    {
        try
        {
            // Vérification si la seance existe
            if (!seance.existe(seance.getId()))
                throw new IFT287Exception("La seance n'existe pas : " + seance.getId());

            // Vérification que la seance n'est pas encore passée
            if (seance.seancePassee(seance.getId()))
                throw new IFT287Exception("La seance " + seance.getId() + " est déjà passée.");

            seance.supprimer(seance);

            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * @param proces
     * @return ArrayList<Seance>
     * @throws Exception
     */
    public ArrayList<Seance> affichage(Proces proces) throws Exception
    {
        ArrayList<Seance> listSeance = new ArrayList<Seance>();

        try
        {
            if (!proces.existe(proces))
                throw new IFT287Exception("Le proces " + proces.getId() + "n'existe pas");

            listSeance = seance.affichage(proces);

            cx.commit();

            return listSeance;
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}