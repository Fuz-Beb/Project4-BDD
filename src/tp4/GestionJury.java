package tp4;

import java.util.List;

/**
 * Gestion des transactions de la table jury.
 */
public class GestionJury
{
    private TableJury jury;
    private TableProces proces;
    private Connexion cx;

    /**
     * Constructeur de confort
     * 
     * @param jury
     * @param proces
     * @throws IFT287Exception
     */
    public GestionJury(TableJury jury, TableProces proces) throws IFT287Exception
    {
        this.cx = jury.getConnexion();

        if (jury.getConnexion() != proces.getConnexion())
            throw new IFT287Exception(
                    "Les instances de juge et de proces n'utilisent pas la même connexion au serveur");

        this.jury = jury;
        this.proces = proces;
    }

    /**
     * Ajout d'une jury dans la base de données
     * 
     * @param juryArg
     * @throws Exception
     */
    public void ajouter(Jury juryArg) throws Exception
    {
        try
        {
            cx.getConnection().getTransaction().begin();

            if (jury.existe(juryArg))
                throw new IFT287Exception("Jury existe déjà : " + juryArg.getNas());
            jury.ajouter(juryArg);

            cx.getConnection().getTransaction().commit();
        }
        finally
        {
            if (cx.getConnection().getTransaction().isActive())
                cx.getConnection().getTransaction().rollback();
        }
    }

    /**
     * Afficher la liste des jurys
     * 
     * @return List<Jury>
     */
    public List<Jury> affichage()
    {
        List<Jury> list = null;
        try
        {
            cx.getConnection().getTransaction().begin();

            list = jury.affichage();

            cx.getConnection().getTransaction().commit();

            return list;
        }
        finally
        {
            if (cx.getConnection().getTransaction().isActive())
                cx.getConnection().getTransaction().rollback();
        }
    }

    /**
     * Assigner un proces à un jury
     * 
     * @param procesArg
     * @param juryArg
     * @throws IFT287Exception
     */
    public void assignerProces(Jury juryArg, Proces procesArg) throws IFT287Exception
    {
        try
        {
            cx.getConnection().getTransaction().begin();

            if (!proces.existe(procesArg.getId()))
                throw new IFT287Exception("Proces n'existe pas : " + procesArg.getId());
            if (!proces.devantJury(procesArg))
                throw new IFT287Exception("Le proces " + procesArg.getId() + "doit se tenir devant un juge seul");
            
            if (!jury.assignerProces(juryArg, procesArg))
                throw new IFT287Exception("L'assignation du proces " + procesArg.getId() + " au jury " + juryArg.getNas() + " a échoué");

            cx.getConnection().getTransaction().commit();
        }
        finally
        {
            if (cx.getConnection().getTransaction().isActive())
                cx.getConnection().getTransaction().rollback();
        }
    }

    /**
     * Retourne le jury demandé et reçu par TableJury
     * 
     * @param id
     * @return Jury
     * @throws Exception
     */
    public Jury getJury(int id) throws Exception
    {
        Jury list = null;
        try
        {
            cx.getConnection().getTransaction().begin();

            list = jury.getJury(id);

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