package tp3;

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
     * @param tupleSeance
     * @throws Exception
     */
    public void ajout(TupleSeance tupleSeance) throws Exception
    {
        try
        {
            // Vérification si la seance existe deja
            if (seance.existe(tupleSeance.getId()))
                throw new IFT287Exception("La seance existe deja: " + tupleSeance.getId());

            // Verification si le proces existe
            if (!proces.existe(new TupleProces(tupleSeance.getProces_id())))
                throw new IFT287Exception("Le proces " + tupleSeance.getProces_id() + " n'existe pas.");

            // Verification si le proces specifie n'est pas termine
            if (!proces.verifierProcesTermine(new TupleProces(tupleSeance.getProces_id())))
                throw new IFT287Exception("Le proces " + tupleSeance.getProces_id() + " est termine.");

            seance.ajout(tupleSeance);

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
     * @param tupleSeance
     * @throws Exception
     */
    public void supprimer(TupleSeance tupleSeance) throws Exception
    {
        try
        {
            // Vérification si la seance existe
            if (!seance.existe(tupleSeance.getId()))
                throw new IFT287Exception("La seance n'existe pas : " + tupleSeance.getId());

            // Vérification que la seance n'est pas encore passée
            if (seance.seancePassee(tupleSeance.getId()))
                throw new IFT287Exception("La seance " + tupleSeance.getId() + " est déjà passée.");

            seance.supprimer(tupleSeance);

            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * @param tupleProces
     * @return ArrayList<TupleSeance>
     * @throws Exception
     */
    public ArrayList<TupleSeance> affichage(TupleProces tupleProces) throws Exception
    {
        ArrayList<TupleSeance> listSeance = new ArrayList<TupleSeance>();

        try
        {
            if (!proces.existe(tupleProces))
                throw new IFT287Exception("Le proces " + tupleProces.getId() + "n'existe pas");

            listSeance = seance.affichage(tupleProces);

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