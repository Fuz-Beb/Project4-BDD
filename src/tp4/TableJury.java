package tp4;

import java.util.List;

import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

/**
 * Permet d'effectuer les accès à la table jury.
 */
public class TableJury
{
    private Connexion cx;
    private TypedQuery<Jury> stmtExiste;
    private TypedQuery<Jury> stmtSelect;
    private TypedQuery<Jury> stmtSelectJuryProces;
    private TypedQuery<Jury> stmtChangeProces;
    private TypedQuery<Jury> stmtChangeProcesNullJury;

    /**
     * Création d'une instance. Des énoncés SQL pour chaque requête sont
     * précompilés.
     * 
     * @param cx
     */
    public TableJury(Connexion cx)
    {
        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery("select j from Jury j where j.nas = :nasJury", Jury.class);
        stmtSelect = cx.getConnection().createQuery("select j from Jury j where j.proces is null", Jury.class);
        stmtSelectJuryProces = cx.getConnection().createQuery("select j from Jury j where j.proces is not null and j.proces.id = :proces", Jury.class);
        stmtChangeProces = cx.getConnection().createQuery("update Jury j SET j.proces = :proces where j.nas = :nasJury",
                Jury.class);
        stmtChangeProcesNullJury = cx.getConnection().createQuery("update Jury j SET j.proces = null where j.proces is not null and j.nas = :nasJury",
                Jury.class);
    }

    /**
     * Retourne la commande associée
     * 
     * @return Connexion
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Objet jury associé à un jury de la base de données
     * 
     * @param id
     * @return Jury
     * @throws Exception
     */
    public Jury getJury(int id) throws Exception
    {
        stmtExiste.setParameter("nasJury", id);
        return stmtExiste.getSingleResult();
    }

    /**
     * Vérifie si le jury existe
     * 
     * @param jury
     * @return boolean
     */
    public boolean existe(Jury jury)
    {
        stmtExiste.setParameter("nasJury", jury.getNas());
        return !stmtExiste.getResultList().isEmpty();
    }

    /**
     * Affiche la liste des jurys
     * 
     * @return String
     */
    public List<Jury> affichage()
    {
        return stmtSelect.getResultList();
    }

    /**
     * Ajout d'un nouveau jury dans la base de données
     * 
     * @param jury
     * @return le juge qui vient d'être ajouté
     * @throws IllegalArgumentException
     * @throws TransactionRequiredException
     */
    public Jury ajouter(Jury jury) throws IllegalArgumentException, TransactionRequiredException
    {
        cx.getConnection().persist(jury);
        return jury;
    }

    /**
     * Assigner un proces à un jury
     * 
     * @param procesArg
     * @param idJury
     * @return boolean
     */
    public boolean assignerProces(int idJury, Proces procesArg)
    {
        stmtChangeProces.setParameter("proces", procesArg);
        stmtChangeProces.setParameter("nasJury", idJury);

        if (stmtChangeProces.executeUpdate() == 1)
            return true;
        return false;
    }

    /**
     * @param proces_id
     */
    public void retirer(int proces_id)
    {
        stmtSelectJuryProces.setParameter("proces", proces_id);
        List<Jury> listeJury = stmtSelectJuryProces.getResultList();
        
        for(Jury juryLigne : listeJury)
        {
            stmtChangeProcesNullJury.setParameter("proces", proces_id);
            stmtChangeProcesNullJury.setParameter("nasJury", juryLigne.getNas());
            stmtChangeProcesNullJury.executeUpdate();
        }
    }
}