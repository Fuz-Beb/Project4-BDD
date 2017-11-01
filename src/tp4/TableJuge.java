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
    private TypedQuery<Juge> quitterJusticeJuge;

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
     * @param id
     * @return boolean
     */
    public boolean existe(int id)
    {
        stmtExiste.setParameter("idJuge", id);
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
     * @param id
     * @return vrai si suppresion OK sinon faux
     */
    public boolean retirer(int id)
    {
        quitterJusticeJuge = cx.getConnection().createQuery("update Juge j SET quitterJustice = true, disponible = false where j.id = :id", Juge.class);        
        quitterJusticeJuge.setParameter("id", id);  
        
        // Si on a bien effectué les modifications alors on retourne vrai
        if (quitterJusticeJuge.executeUpdate() == 1)
            return true;

        return false;
    }

    /**
     * Changer la disponibilite d'un juge
     * 
     * @param disponible
     * @param id
     * @return boolean
     */
    public boolean changerDisponibilite(boolean disponible, int id)
    {
        TypedQuery<Juge> changerDisponibiliteJuge = cx.getConnection().createQuery("update Juge j SET j.disponible = :disponibilite where j.id = :id", Juge.class);        
        changerDisponibiliteJuge.setParameter("id", id);
        changerDisponibiliteJuge.setParameter("disponibilite", disponible);
        
        // Si on a bien effectué les modifications alors on retourne vrai
        if (changerDisponibiliteJuge.executeUpdate() == 1) {
            System.out.println("test");
            return true;
        }

        return false;
    }
}