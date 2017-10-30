package tp4;

/**
 * Gestion des transactions de la table proces
 */
public class GestionProces
{
    private Connexion cx;
    private TableProces proces;
    private TableSeance seance;
    private TableJuge juge;
    private TablePartie partie;

    /**
     * Constructeur de confort
     * 
     * @param proces
     * @param seance
     * @param juge
     * @param partie
     * @throws IFT287Exception
     */
    public GestionProces(TableProces proces, TableSeance seance, TableJuge juge, TablePartie partie)
            throws IFT287Exception
    {
        this.cx = proces.getConnexion();
        if (proces.getConnexion() != seance.getConnexion())
            throw new IFT287Exception(
                    "Les instances de TableProces et TableSeance n'utilisent pas la même connexion au serveur");
        if (proces.getConnexion() != juge.getConnexion())
            throw new IFT287Exception(
                    "Les instances de TableProces et TableJuge n'utilisent pas la même connexion au serveur");
        if (proces.getConnexion() != partie.getConnexion())
            throw new IFT287Exception(
                    "Les instances de TableProces et TablePartie n'utilisent pas la même connexion au serveur");

        this.proces = proces;
        this.seance = seance;
        this.juge = juge;
        this.partie = partie;
    }

    /**
     * Methode d'affichage d'un proces
     * 
     * @param proces
     * @return Proces
     * @throws Exception
     */
    public Proces affichage(Proces proces) throws Exception
    {
        Proces tupleProcesReturn = null;

        try
        {
            if (!proces.existe(proces))
                throw new IFT287Exception("Le proces " + proces.getId() + "n'existe pas");

            tupleProcesReturn = proces.affichage(proces);

            cx.commit();

            return tupleProcesReturn;
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * Methode de traitement pour effectuerTerminerProces
     * 
     * @param proces
     * @param decisionProces
     * @throws Exception
     */
    public void terminer(Proces proces, int decisionProces) throws Exception
    {
        try
        {
            int idJuge = 0;

            // Verification de la valeur de la decision
            if (decisionProces != 0 && decisionProces != 1)
                throw new IFT287Exception("Impossible de terminer le proces " + proces.getId()
                        + "car la valeur de la decision n'est ni 0 ni 1.");

            // Vérification que le proces existe
            if (!proces.existe(proces))
                throw new IFT287Exception("Le proces " + proces.getId() + "n'existe pas.");

            // Vérification que le proces a atteint sa date initiale
            if (!proces.compareDate(proces))
                throw new IFT287Exception("Le proces " + proces.getId() + "n'a pas atteint sa date initiale.");

            proces.terminer(decisionProces, proces);

            idJuge = proces.changeJugeStatut(proces);

            if (!proces.jugeEnCours(new Juge(idJuge)))
                juge.changerDisponibilite(true, new Juge(idJuge));

            seance.supprimerSeancesProcesTermine(proces.getId());

            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * Permet de creer un proces
     * 
     * @param proces
     * @throws Exception
     */
    public void creer(Proces proces) throws Exception
    {
        try
        {
            if (proces.getDevantJury() != 0 && proces.getDevantJury() != 1)
                throw new IFT287Exception("Impossible de creer le proces " + proces.getId()
                        + "car le champ devantJury ne peut être que 0 ou 1");

            // Vérification que le proces n'existe pas déjà
            if (proces.existe(proces))
                throw new IFT287Exception("Le proces " + proces.getId() + "existe déjà.");
            // Vérification que l'id du juge est correcte
            if (!juge.existe(new Juge(proces.getJuge_id())))
                throw new IFT287Exception("Le juge " + proces.getJuge_id() + "n'existe pas.");
            if (!partie.existe(new Partie(proces.getPartieDefenderesse_id())))
                throw new IFT287Exception(
                        "La partie defenderesse " + proces.getPartieDefenderesse_id() + "n'existe pas.");
            if (!partie.existe(new Partie(proces.getPartiePoursuivant_id())))
                throw new IFT287Exception(
                        "La partie poursuivante " + proces.getPartiePoursuivant_id() + "n'existe pas.");

            proces.creer(proces);

            // Rendre le juge non disponible
            juge.changerDisponibilite(false, new Juge(proces.getJuge_id()));
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}
