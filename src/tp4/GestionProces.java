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
     * @param procesArg
     * @return Proces
     * @throws Exception
     */
    public Proces affichage(Proces procesArg) throws Exception
    {
        Proces tupleProcesReturn = null;

        try
        {
            if (!proces.existe(procesArg))
                throw new IFT287Exception("Le proces " + procesArg.getId() + "n'existe pas");

            tupleProcesReturn = proces.affichage(procesArg);

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
     * @param procesArg
     * @param decisionProces
     * @throws Exception
     */
    public void terminer(Proces procesArg, int decisionProces) throws Exception
    {
        try
        {
            int idJuge = 0;

            // Verification de la valeur de la decision
            if (decisionProces != 0 && decisionProces != 1)
                throw new IFT287Exception("Impossible de terminer le proces " + procesArg.getId()
                        + "car la valeur de la decision n'est ni 0 ni 1.");

            // Vérification que le proces existe
            if (!proces.existe(procesArg))
                throw new IFT287Exception("Le proces " + procesArg.getId() + "n'existe pas.");

            // Vérification que le proces a atteint sa date initiale
            if (!proces.compareDate(procesArg))
                throw new IFT287Exception("Le proces " + procesArg.getId() + "n'a pas atteint sa date initiale.");

            proces.terminer(decisionProces, procesArg);

            idJuge = proces.changeJugeStatut(procesArg);

            if (!proces.jugeEnCours(new Juge(idJuge)))
                juge.changerDisponibilite(true, new Juge(idJuge));

            seance.supprimerSeancesProcesTermine(procesArg.getId());

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
     * @param procesArg
     * @throws Exception
     */
    public void creer(Proces procesArg) throws Exception
    {
        try
        {
            if (procesArg.getDevantJury() != 0 && procesArg.getDevantJury() != 1)
                throw new IFT287Exception("Impossible de creer le proces " + procesArg.getId()
                        + "car le champ devantJury ne peut être que 0 ou 1");

            // Vérification que le proces n'existe pas déjà
            if (proces.existe(procesArg))
                throw new IFT287Exception("Le proces " + procesArg.getId() + "existe déjà.");
            // Vérification que l'id du juge est correcte
            if (!juge.existe(new Juge(procesArg.getJuge().getId())))
                throw new IFT287Exception("Le juge " + procesArg.getJuge().getId() + "n'existe pas.");
            if (!partie.existe(new Partie(procesArg.getPartieDefenderesse().getId())))
                throw new IFT287Exception(
                        "La partie defenderesse " + procesArg.getPartieDefenderesse().getId() + "n'existe pas.");
            if (!partie.existe(new Partie(procesArg.getPartiePoursuivant().getId())))
                throw new IFT287Exception(
                        "La partie poursuivante " + procesArg.getPartiePoursuivant().getId() + "n'existe pas.");

            proces.creer(procesArg);

            // Rendre le juge non disponible
            juge.changerDisponibilite(false, new Juge(procesArg.getJuge().getId()));
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}
