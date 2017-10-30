// Travail fait par :
// Pierrick BOBET - 17 131 792
// Rémy BOUTELOUP - 17 132 265

package tp3;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.sql.*;

/**
 * Interface du systeme de gestion d'une bibliothèque
 *
 * <pre>
 * 
 * Vincent Ducharme
 * Universite de Sherbrooke
 * Version 1.0 - 7 juillet 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'appeler des transactions d'un systeme
 * de gestion utilisant une base de donnees.
 *
 * Paramètres du programme
 * 0- site du serveur SQL ("local" ou "dinf")
 * 1- nom de la BD
 * 2- user id pour etablir une connexion avec le serveur SQL
 * 3- mot de passe pour le user id
 * 4- fichier de transaction [optionnel]
 *           si non spécifié, les transactions sont lues au
 *           clavier (System.in)
 *
 * Pré-condition
 *   - La base de donnees doit exister
 *
 * Post-condition
 *   - Le programme effectue les mises à jour associees à chaque
 *     transaction
 * </pre>
 */
public class Devoir3
{
    private static GestionJustice gestionJustice;

    /**
     * Ouverture de la BD, traitement des transacations et fermeture de la BD
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        Devoir3 instanceDevoir3 = null;

        if (args.length < 4)
        {
            System.out.println("Usage: java tp2.Devoir2 <serveur> <bd> <user> <password> [<fichier-transactions>]");
            return;
        }

        try
        {
            instanceDevoir3 = new Devoir3(args[0], args[1], args[2], args[3]);

            BufferedReader reader = ouvrirFichier(args);
            String transaction = lireTransaction(reader);
            while (!finTransaction(transaction))
            {
                executerTransaction(transaction);
                transaction = lireTransaction(reader);
            }
        }
        finally
        {
            if (instanceDevoir3 != null)
                instanceDevoir3.fermer();
        }
    }

    /**
     * Ouvre la connexion
     * 
     * @param serveur
     * @param bd
     * @param user
     * @param pass
     * @throws IFT287Exception
     * @throws SQLException
     */
    public Devoir3(String serveur, String bd, String user, String pass) throws IFT287Exception, SQLException
    {
        gestionJustice = new GestionJustice(serveur, bd, user, pass);
    }

    /**
     * Fermer la connexion
     * 
     * @throws SQLException
     */
    public void fermer() throws SQLException
    {
        gestionJustice.fermer();
    }

    /**
     * Decodage et traitement d'une transaction
     */
    static void executerTransaction(String transaction) throws Exception, IFT287Exception
    {
        // Nécessaire pour l'affichage
        ArrayList<TupleJuge> tupleJuge;
        ArrayList<TupleJury> tupleJury;
        ArrayList<TupleSeance> tupleSeance;
        TupleProces tupleProces;

        try
        {
            System.out.println(transaction);
            // Decoupage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens())
            {
                String command = tokenizer.nextToken();
                // Vous devez remplacer la chaine "commande1" et "commande2" par
                // les commandes de votre programme. Vous pouvez ajouter autant
                // de else if que necessaire. Vous n'avez pas a traiter la
                // commande "quitter".
                if (command.equals("ajouterJuge"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionJuge().ajouter(new TupleJuge(readInt(tokenizer), readString(tokenizer),
                            readString(tokenizer), readInt(tokenizer)));
                }
                else if (command.equals("retirerJuge"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionJuge().retirer(new TupleJuge(readInt(tokenizer)));
                }
                else if (command.equals("ajouterAvocat"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionAvocat().ajouter(new TupleAvocat(readInt(tokenizer), readString(tokenizer),
                            readString(tokenizer), readInt(tokenizer)));
                }
                else if (command.equals("ajouterPartie"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionPartie().ajout(new TuplePartie(readInt(tokenizer), readString(tokenizer),
                            readString(tokenizer), readInt(tokenizer)));
                }
                else if (command.equals("creerProces"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionProces().creer(new TupleProces(readInt(tokenizer), readInt(tokenizer),
                            readDate(tokenizer), readInt(tokenizer), readInt(tokenizer), readInt(tokenizer)));
                }
                else if (command.equals("inscrireJury"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionJury().ajouter(new TupleJury(readInt(tokenizer), readString(tokenizer),
                            readString(tokenizer), readString(tokenizer), readInt(tokenizer)));
                }
                else if (command.equals("assignerJury"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionJury().assignerProces(new TupleJury(readInt(tokenizer)),
                            new TupleProces(readInt(tokenizer)));
                }
                else if (command.equals("ajouterSeance"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionSeance()
                            .ajout(new TupleSeance(readInt(tokenizer), readInt(tokenizer), readDate(tokenizer)));
                }
                else if (command.equals("supprimerSeance"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionSeance().supprimer(new TupleSeance(readInt(tokenizer)));
                }
                else if (command.equals("terminerProces"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    gestionJustice.getGestionProces().terminer(new TupleProces(readInt(tokenizer)), readInt(tokenizer));
                }
                else if (command.equals("afficherJuges"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    tupleJuge = gestionJustice.getGestionJuge().affichage();

                    System.out.println("\nListe des juges actifs et disponibles :");

                    // Affichage des juges un à un
                    for (int boucle = 0; boucle < tupleJuge.size(); boucle++)
                    {
                        System.out.println(tupleJuge.get(boucle).getId() + "\t" + tupleJuge.get(boucle).getPrenom()
                                + "\t" + tupleJuge.get(boucle).getNom() + "\t" + tupleJuge.get(boucle).getAge());
                    }
                }
                else if (command.equals("afficherProces"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    tupleProces = gestionJustice.getGestionProces().affichage(new TupleProces(readInt(tokenizer)));

                    System.out.println("Affichage du proces " + tupleProces.getId());

                    System.out.println(tupleProces.getId() + "\t" + tupleProces.getJuge_id() + "\t"
                            + tupleProces.getDate() + "\t" + tupleProces.getDevantJury() + "\t"
                            + tupleProces.getPartieDefenderesse_id() + "\t" + tupleProces.getPartiePoursuivant_id());

                    tupleSeance = gestionJustice.getGestionSeance().affichage(tupleProces);

                    System.out.println("\nListe des séances liées au proces " + tupleProces.getId());

                    // Affichage des séances une à une
                    for (int boucle = 0; boucle < tupleSeance.size(); boucle++)
                    {
                        System.out.println(tupleSeance.get(boucle).getId() + "\t"
                                + tupleSeance.get(boucle).getProces_id() + "\t" + tupleSeance.get(boucle).getDate());
                    }
                }
                else if (command.equals("afficherJurys"))
                {
                    // Appel de la methode qui traite la transaction specifique
                    tupleJury = gestionJustice.getGestionJury().affichage();

                    System.out.println("\nListe des jurys disponibles :");

                    for (int boucle = 0; boucle < tupleJury.size(); boucle++)
                    {
                        System.out.println(tupleJury.get(boucle).getNas() + "\t" + tupleJury.get(boucle).getPrenom()
                                + "\t" + tupleJury.get(boucle).getNom() + "\t" + tupleJury.get(boucle).getSexe() + "\t"
                                + tupleJury.get(boucle).getAge() + "\t" + tupleJury.get(boucle).getProces_id());
                    }
                }
                else
                {
                    System.out.println(" : Transaction non reconnue");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(" " + e.toString());
            gestionJustice.getCx().rollback();
        }
    }

    // ****************************************************************
    // * Les methodes suivantes n'ont pas besoin d'etre modifiees *
    // ****************************************************************

    /**
     * Ouvre le fichier de transaction, ou lit à partir de System.in
     * 
     * @param args
     * @return BufferedReader
     * @throws FileNotFoundException
     */
    public static BufferedReader ouvrirFichier(String[] args) throws FileNotFoundException
    {
        if (args.length < 5)
            // Lecture au clavier
            return new BufferedReader(new InputStreamReader(System.in));
        else
            // Lecture dans le fichier passe en parametre
            return new BufferedReader(new InputStreamReader(new FileInputStream(args[4])));
    }

    /**
     * Lecture d'une transaction
     */
    static String lireTransaction(BufferedReader reader) throws IOException
    {
        return reader.readLine();
    }

    /**
     * Verifie si la fin du traitement des transactions est atteinte.
     */
    static boolean finTransaction(String transaction)
    {
        // fin de fichier atteinte
        return (transaction == null || transaction.equals("quitter"));
    }

    /** Lecture d'une chaine de caracteres de la transaction entree a l'ecran */
    static String readString(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
            return tokenizer.nextToken();
        else
            throw new Exception("Autre parametre attendu");
    }

    /**
     * Lecture d'un int java de la transaction entree a l'ecran
     */
    static int readInt(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Integer.valueOf(token).intValue();
            }
            catch (NumberFormatException e)
            {
                throw new Exception("Nombre attendu a la place de \"" + token + "\"");
            }
        }
        else
            throw new Exception("Autre parametre attendu");
    }

    static Date readDate(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Date.valueOf(token);
            }
            catch (IllegalArgumentException e)
            {
                throw new Exception("Date dans un format invalide - \"" + token + "\"");
            }
        }
        else
            throw new Exception("Autre parametre attendu");
    }

}