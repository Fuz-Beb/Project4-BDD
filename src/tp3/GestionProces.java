package tp3;

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
     * @param tupleProces
     * @return TupleProces
     * @throws Exception
     */
    public TupleProces affichage(TupleProces tupleProces) throws Exception
    {
        TupleProces tupleProcesReturn = null;

        try
        {
            if (!proces.existe(tupleProces))
                throw new IFT287Exception("Le proces " + tupleProces.getId() + "n'existe pas");

            tupleProcesReturn = proces.affichage(tupleProces);

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
     * @param tupleProces
     * @param decisionProces
     * @throws Exception
     */
    public void terminer(TupleProces tupleProces, int decisionProces) throws Exception
    {
        try
        {
            int idJuge = 0;

            // Verification de la valeur de la decision
            if (decisionProces != 0 && decisionProces != 1)
                throw new IFT287Exception("Impossible de terminer le proces " + tupleProces.getId()
                        + "car la valeur de la decision n'est ni 0 ni 1.");

            // Vérification que le proces existe
            if (!proces.existe(tupleProces))
                throw new IFT287Exception("Le proces " + tupleProces.getId() + "n'existe pas.");

            // Vérification que le proces a atteint sa date initiale
            if (!proces.compareDate(tupleProces))
                throw new IFT287Exception("Le proces " + tupleProces.getId() + "n'a pas atteint sa date initiale.");

            proces.terminer(decisionProces, tupleProces);

            idJuge = proces.changeJugeStatut(tupleProces);

            if (!proces.jugeEnCours(new TupleJuge(idJuge)))
                juge.changerDisponibilite(true, new TupleJuge(idJuge));

            seance.supprimerSeancesProcesTermine(tupleProces.getId());

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
     * @param tupleProces
     * @throws Exception
     */
    public void creer(TupleProces tupleProces) throws Exception
    {
        try
        {
            if (tupleProces.getDevantJury() != 0 && tupleProces.getDevantJury() != 1)
                throw new IFT287Exception("Impossible de creer le proces " + tupleProces.getId()
                        + "car le champ devantJury ne peut être que 0 ou 1");

            // Vérification que le proces n'existe pas déjà
            if (proces.existe(tupleProces))
                throw new IFT287Exception("Le proces " + tupleProces.getId() + "existe déjà.");
            // Vérification que l'id du juge est correcte
            if (!juge.existe(new TupleJuge(tupleProces.getJuge_id())))
                throw new IFT287Exception("Le juge " + tupleProces.getJuge_id() + "n'existe pas.");
            if (!partie.existe(new TuplePartie(tupleProces.getPartieDefenderesse_id())))
                throw new IFT287Exception(
                        "La partie defenderesse " + tupleProces.getPartieDefenderesse_id() + "n'existe pas.");
            if (!partie.existe(new TuplePartie(tupleProces.getPartiePoursuivant_id())))
                throw new IFT287Exception(
                        "La partie poursuivante " + tupleProces.getPartiePoursuivant_id() + "n'existe pas.");

            proces.creer(tupleProces);

            // Rendre le juge non disponible
            juge.changerDisponibilite(false, new TupleJuge(tupleProces.getJuge_id()));
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}
