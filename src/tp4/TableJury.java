package tp4;

import java.sql.SQLException;
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
     * @throws IFT287Exception
     */

    public Jury getJury(int id) throws IFT287Exception
    {
        stmtExiste.setParameter(":idJury", id);
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
        stmtExiste.setParameter(":nasJury", jury.getNas());
        return !stmtExiste.getResultList().isEmpty();
    }

    /**
     * Affiche la liste des jurys
     * 
     * @return String
     * @throws SQLException
     * @throws IFT287Exception
     */
    /*
     * public ArrayList<Jury> affichage() throws SQLException, IFT287Exception {
     * ArrayList<Jury> listJury = new ArrayList<Jury>();
     * 
     * ResultSet rset = stmtSelect.executeQuery();
     * 
     * if (rset.next()) { do { // Ajout de chacun des juges dans la liste
     * listJury.add(getJury(new Jury(rset.getInt(1)))); } while (rset.next()); }
     * rset.close(); return listJury; }
     */

    /**
     * Ajout d'un nouveau jury dans la base de données
     * 
     * @param jury
     * @return le juge qui vient d'être ajouté
     */
    public Jury ajouter(Jury jury)
    {
        cx.getConnection().persist(jury);
        return jury;
    }

    /**
     * Assigner un proces à un jury
     * 
     * @param proces
     * @param jury
     * @throws SQLException
     */
    public void assignerProces(Jury jury, Proces proces) throws SQLException
    {
        jury.setProces(proces);
    }
}