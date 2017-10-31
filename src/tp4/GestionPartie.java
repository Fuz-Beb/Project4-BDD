package tp4;

/**
 * Gestion des transactions de la table partie.
 */
public class GestionPartie
{
    private TablePartie partie;
    private TableAvocat avocat;
    private Connexion cx;

    /**
     * Constructeur de confort
     * 
     * @param partie
     * @param avocat
     * @throws IFT287Exception
     */
    public GestionPartie(TablePartie partie, TableAvocat avocat) throws IFT287Exception
    {
        this.cx = partie.getConnexion();
        if (partie.getConnexion() != avocat.getConnexion())
            throw new IFT287Exception(
                    "Les instances de TablePartie et de TableAvocat n'utilisent pas la même connexion au serveur");
        this.partie = partie;
        this.avocat = avocat;
    }

    /**
     * Ajout d'un nouveau partie dans la base de données. S'il existe déjà, une
     * exception est levée.
     * 
     * @param partieArg
     * @throws IFT287Exception
     * @throws Exception
     */
    public void ajout(Partie partieArg) throws Exception
    {
        try
        {
            cx.getConnection().getTransaction().begin();

            // Vérifie si le partie existe déjà
            if (partie.existe(partieArg))
                throw new IFT287Exception("Partie existe déjà: " + partieArg.getId());

            // Vérifie si l'avocat existe
            if (!avocat.existe(new Avocat(partieArg.getAvocat().getId())))
                throw new IFT287Exception("L'avocat " + partieArg.getAvocat().getId() + "n'existe pas.");

            // Ajout du partie
            partie.ajout(partieArg);

            cx.getConnection().getTransaction().commit();
        }
        finally
        {
            if (cx.getConnection().getTransaction().isActive())
                cx.getConnection().getTransaction().rollback();
        }
    }

    /**
     * Retourne le partie demandé et reçu par TablePartie
     * 
     * @param id
     * @return Partie
     * @throws Exception
     */
    public Partie getPartie(int id) throws Exception
    {
        Partie list = null;
        try
        {
            cx.getConnection().getTransaction().begin();

            list = partie.getPartie(id);

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