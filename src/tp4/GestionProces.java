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
     * @param id
     * @return Proces
     * @throws IFT287Exception
     */
    public Proces affichage(int id) throws IFT287Exception
    {
        Proces list = null;
        try
        {
            cx.getConnection().getTransaction().begin();

            if (!proces.existe(id))
                throw new IFT287Exception("Le proces " + id + " n'existe pas");
            else
                list = proces.affichage(id);

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
     * Methode de traitement pour effectuerTerminerProces
     * 
     * @param id
     * @param decisionProces
     * @throws Exception
     */
    public void terminer(int id, int decisionProces) throws Exception
    {
        try
        {
            cx.getConnection().getTransaction().begin();

            int idJuge = 0;

            // Verification de la valeur de la decision
            if (decisionProces != 0 && decisionProces != 1)
                throw new IFT287Exception(
                        "Impossible de terminer le proces " + id + " car la valeur de la decision n'est ni 0 ni 1.");

            // Vérification que le proces existe
            if (!proces.existe(id))
                throw new IFT287Exception("Le proces " + id + " n'existe pas.");

            // Vérification que le proces a atteint sa date initiale
            if (!proces.compareDate(id))
                throw new IFT287Exception("Le proces " + id + " n'a pas atteint sa date initiale.");

            proces.terminer(decisionProces, id);

            idJuge = proces.changeJugeStatut(id);

            if (!proces.jugeEnCours(idJuge))
                juge.changerDisponibilite(true, idJuge);

            seance.supprimerSeancesProcesTermine(id);

            cx.getConnection().getTransaction().commit();

        }
        finally
        {
            if (cx.getConnection().getTransaction().isActive())
                cx.getConnection().getTransaction().rollback();
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
            cx.getConnection().getTransaction().begin();

            if (procesArg.getDevantJury() != 0 && procesArg.getDevantJury() != 1)
                throw new IFT287Exception("Impossible de creer le proces " + procesArg.getId()
                        + "car le champ devantJury ne peut être que 0 ou 1");

            // Vérification que le proces n'existe pas déjà
            if (proces.existe(procesArg.getId()))
                throw new IFT287Exception("Le proces " + procesArg.getId() + " existe déjà.");
            // Vérification que l'id du juge est correcte
            if (!juge.existe(procesArg.getJuge().getId()))
                throw new IFT287Exception("Le juge " + procesArg.getJuge().getId() + " n'existe pas.");
            if (!partie.existe(new Partie(procesArg.getPartieDefenderesse().getId())))
                throw new IFT287Exception(
                        "La partie defenderesse " + procesArg.getPartieDefenderesse().getId() + " n'existe pas.");
            if (!partie.existe(new Partie(procesArg.getPartiePoursuivant().getId())))
                throw new IFT287Exception(
                        "La partie poursuivante " + procesArg.getPartiePoursuivant().getId() + " n'existe pas.");

            proces.creer(procesArg);

            // Rendre le juge non disponible
            if (!juge.changerDisponibilite(false, procesArg.getJuge().getId()))
                throw new IFT287Exception("Erreur dans le changement");

            cx.getConnection().getTransaction().commit();
        }
        finally
        {
            if (cx.getConnection().getTransaction().isActive())
                cx.getConnection().getTransaction().rollback();
        }
    }

    /**
     * Retourne le proces demandé et reçu par TableProces
     * 
     * @param id
     * @return Partie
     * @throws Exception
     */
    public Proces getProces(int id) throws Exception
    {
        Proces list = null;
        try
        {
            cx.getConnection().getTransaction().begin();

            list = proces.getProces(id);

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
