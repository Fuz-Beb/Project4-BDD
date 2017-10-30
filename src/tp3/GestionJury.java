package tp3;

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
     * @param tupleJury
     * @throws Exception
     */
    public void ajouter(TupleJury tupleJury) throws Exception
    {
        try
        {
            if (jury.existe(tupleJury))
                throw new IFT287Exception("Jury existe déjà : " + tupleJury.getNas());
            jury.ajouter(tupleJury);
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
     * @return ArrayList<TupleJury>
     *
     * @throws Exception
     */
    public ArrayList<TupleJury> affichage() throws Exception
    {
        ArrayList<TupleJury> tupleJury = null;

        try
        {
            tupleJury = jury.affichage();

            cx.commit();

            return tupleJury;
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
     * @param tupleProces
     * @param tupleJury
     * @throws Exception
     */
    public void assignerProces(TupleJury tupleJury, TupleProces tupleProces) throws Exception
    {
        try
        {
            if (!proces.existe(tupleProces))
                throw new IFT287Exception("Proces n'existe pas : " + tupleProces.getId());
            if (!proces.devantJury(tupleProces))
                throw new IFT287Exception("Le proces " + tupleProces.getId() + "doit se tenir devant un juge seul");
            jury.assignerProces(tupleJury, tupleProces);
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}