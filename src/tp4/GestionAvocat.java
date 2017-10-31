package tp4;

/**
 * Gestion des transactions de la table avocat.
 */
public class GestionAvocat
{
    private TableAvocat avocat;
    private Connexion cx;

    /**
     * Constructeur de confort
     * 
     * @param avocat
     */
    public GestionAvocat(TableAvocat avocat)
    {
        this.cx = avocat.getConnexion();
        this.avocat = avocat;
    }

    /**
     * Ajout d'un nouvelle avocat dans la base de données
     * 
     * @param avocatArg
     * @throws Exception
     */
    public void ajouter(Avocat avocatArg) throws Exception
    {
        try
        {
            // Vérifie si l'avocat existe déjà
            if (avocat.existe(avocatArg))
                throw new IFT287Exception("L'avocat existe déjà : " + avocatArg.getId());

            avocat.ajouter(avocatArg);

            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}