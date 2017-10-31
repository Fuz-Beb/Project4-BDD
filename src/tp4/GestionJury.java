package tp4;

import java.util.ArrayList;

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
            if (jury.existe(juryArg))
                throw new IFT287Exception("Jury existe déjà : " + juryArg.getNas());
            jury.ajouter(juryArg);
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * Afficher la liste des jurys
     * 
     * @return ArrayList<Jury>
     *
     * @throws Exception
     */
    public ArrayList<Jury> affichage() throws Exception
    {
        ArrayList<Jury> jury = null;

        try
        {
            jury = jury.affichage();

            cx.commit();

            return jury;
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * Assigner un proces à un jury
     * 
     * @param procesArg
     * @param juryArg
     * @throws Exception
     */
    public void assignerProces(Jury juryArg, Proces procesArg) throws Exception
    {
        try
        {
            if (!proces.existe(procesArg))
                throw new IFT287Exception("Proces n'existe pas : " + procesArg.getId());
            if (!proces.devantJury(procesArg))
                throw new IFT287Exception("Le proces " + procesArg.getId() + "doit se tenir devant un juge seul");
            jury.assignerProces(juryArg, procesArg);
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}