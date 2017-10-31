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
        stmtSelect = cx.getConnection().createQuery("select j from Jury j where j.proces = null", Jury.class);
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
        stmtExiste.setParameter("idJury", id);
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
     * @param proces
     * @param jury
     */
    public void assignerProces(Jury jury, Proces proces)
    {
        jury.setProces(proces);
    }
}