package tp4;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Gestionnaire d'une connexion avec une BD relationnelle via JDBC.<br>
 * <br>
 * 
 * Cette classe ouvre une connexion avec une BD via JDBC.<br>
 * La méthode serveursSupportes() indique les serveurs supportés.<br>
 * 
 * <pre>
 * Pré-condition
 *   Le driver JDBC approprié doit être accessible.
 * 
 * Post-condition
 *   La connexion est ouverte en mode autocommit false et sérialisable, 
 *   (s'il est supporté par le serveur).
 * </pre>
 * 
 * <br>
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * @author Marc Frappier - Université de Sherbrooke
 * @version Version 2.0 - 13 novembre 2004
 * 
 * 
 * @author Vincent Ducharme - Université de Sherbrooke
 * @version Version 3.0 - 21 mai 2016
 */
public class Connexion
{
    private EntityManager em;
    private EntityManagerFactory emf;

    /**
     * Ouverture d'une connexion
     * 
     * @param serveur : Le type de serveur SQL à utiliser (Valeur : local, dinf).
     * @param bd : nom de la base de données
     * @param user : userid sur le serveur SQL
     * @param pass : mot de passe sur le serveur SQL
     * @throws IFT287Exception 
     */
    public Connexion(String serveur, String bd, String user, String pass) throws IFT287Exception
    {
        if (serveur.equals("local"))
        {
            emf = Persistence.createEntityManagerFactory(bd);
        }
        else if (serveur.equals("dinf"))
        {
            Map<String, String> properties = new HashMap<String, String>();
              properties.put("javax.persistence.jdbc.user", user);
              properties.put("javax.persistence.jdbc.password", pass);
            emf = Persistence.createEntityManagerFactory("objectdb://hibou.dinf.fsci.usherbrooke.ca:6136/"+user+"/" + bd + ".odb", properties);
        }
        else
        {
            throw new IFT287Exception("Serveur inconnu");
        }
        
        em = emf.createEntityManager();
        
        System.out.println("Ouverture de la connexion :\n"
                + "Connecté sur la BD ObjectDB "
                + bd + " avec l'utilisateur " + user);
    }

    /**
     * Fermeture d'une connexion
     */
    public void fermer()
    {
        em.close();
        emf.close();
        System.out.println("Connexion fermée");
    }
    
    /**
     * Permet de commencer la transaction
     */
    public void demarreTransaction()
    {
        em.getTransaction().begin();
    }
    
    /**
     * commit
     */
    public void commit()
    {
        em.getTransaction().commit();
    }

    /**
     * rollback
     */
    public void rollback()
    {
        em.getTransaction().rollback();
    }

    /**
     * retourne la Connection ObjectDB
     * @return EntityManager
     */
    public EntityManager getConnection()
    {
        return em;
    }
}
