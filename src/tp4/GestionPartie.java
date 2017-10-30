package tp4;

import java.sql.SQLException;

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
     * @param partie
     * @throws SQLException
     * @throws IFT287Exception
     * @throws Exception
     */
    public void ajout(Partie partie) throws SQLException, IFT287Exception, Exception
    {
        try
        {
            // Vérifie si le partie existe déjà
            if (partie.existe(partie))
                throw new IFT287Exception("Partie existe déjà: " + partie.getId());

            // Vérifie si l'avocat existe
            if (!avocat.existe(new Avocat(partie.getAvocat_id())))
                throw new IFT287Exception("L'avocat " + partie.getAvocat_id() + "n'existe pas.");

            // Ajout du partie
            partie.ajout(partie);

            // Commit
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}
