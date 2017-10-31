package tp4;

import java.util.List;

import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

/**
 * Permet d'effectuer les accès à la table juge.
 */
public class TableJuge
{
    private Connexion cx;
    private TypedQuery<Juge> stmtExiste;
    private TypedQuery<Juge> stmtSelect;

    /**
     * Création d'une instance. Des énoncés SQL pour chaque requête sont
     * précompilés.
     * 
     * @param cx
     */
    public TableJuge(Connexion cx)
    {
        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery("select j from Juge j where j.id = :idJuge", Juge.class);
        stmtSelect = cx.getConnection().createQuery("select j from Juge j where j.disponible = true", Juge.class);
    }

    /**
     * Retourner la connexion associée
     * 
     * @return Connexion
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Objet juge associé à un juge de la base de données
     * 
     * @param id
     * @return Juge
     * @throws Exception
     */
    public Juge getJuge(int id) throws Exception
    {
        stmtExiste.setParameter("idJuge", id);
        return stmtExiste.getSingleResult();
    }

    /**
     * Vérifie si le juge existe
     * 
     * @param juge
     * @return boolean
     */
    public boolean existe(Juge juge)
    {
        stmtExiste.setParameter("idJuge", juge.getId());
        return !stmtExiste.getResultList().isEmpty();
    }

    /**
     * Afficher la liste des juges actifs et disponibles
     * 
     * @return List<Juge>
     */
    public List<Juge> affichage()
    {
        return stmtSelect.getResultList();
    }

    /**
     * Ajout d'un nouveau juge dans la base de données
     * 
     * @param juge
     * @return Le juge qui a été ajouté
     * @throws IllegalArgumentException
     * @throws TransactionRequiredException
     */
    public Juge ajouter(Juge juge) throws IllegalArgumentException, TransactionRequiredException
    {
        cx.getConnection().persist(juge);
        return juge;
    }

    /**
     * Retirer le juge de la base de données
     * 
     * @param juge
     * @return vrai si suppresion OK sinon faux
     */
    public boolean retirer(Juge juge)
    {
        if (juge != null)
        {
            juge.setQuitterJustice(true);
            juge.setDisponible(false);
            return true;
        }
        return false;
    }

    /**
     * Changer la disponibilite d'un juge
     * 
     * @param disponibilite
     * @param juge
     */
    public void changerDisponibilite(boolean disponibilite, Juge juge)
    {
        juge.setDisponible(disponibilite);
    }
}