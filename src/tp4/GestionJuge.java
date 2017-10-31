package tp4;

import java.util.ArrayList;

/**
 * Gestion des transaction de la table juge.
 */
public class GestionJuge
{
    private TableJuge juge;
    private TableProces proces;
    private Connexion cx;

    /**
     * Constructeur de confort
     * 
     * @param juge
     * @param proces
     * @throws IFT287Exception
     */
    public GestionJuge(TableJuge juge, TableProces proces) throws IFT287Exception
    {
        this.cx = juge.getConnexion();

        if (juge.getConnexion() != proces.getConnexion())
            throw new IFT287Exception(
                    "Les instances de juge et de proces n'utilisent pas la même connexion au serveur");

        this.juge = juge;
        this.proces = proces;
    }

    /**
     * Ajout d'un nouveau juge dans la base de données
     * 
     * @param jugeArg
     * 
     * @throws Exception
     */
    public void ajouter(Juge jugeArg) throws Exception
    {
        try
        {
            if (juge.existe(jugeArg))
                throw new IFT287Exception("Le juge existe déjà : " + jugeArg.getId());

            juge.ajouter(jugeArg);
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * Afficher la liste des juges actifs et disponibles
     * 
     * @return ArrayList<Juge>
     * @throws Exception
     */
    public ArrayList<Juge> affichage() throws Exception
    {
        try
        {
            return juge.affichage();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * Retirer un juge
     * 
     * @param jugeArg
     * @throws Exception
     */
    public void retirer(Juge jugeArg) throws Exception
    {
        try
        {
            if (!juge.existe(jugeArg))
                throw new IFT287Exception("Juge inexistant : " + jugeArg.getId());
            if (proces.jugeEnCours(jugeArg))
                throw new IFT287Exception("Le juge " + jugeArg.getId() + " n'a pas terminé tout ses procès");
            juge.retirer(jugeArg);
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}