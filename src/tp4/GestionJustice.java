package tp4;

/**
 * Système de gestion d'un palais de justice
 */
public class GestionJustice
{
    private Connexion cx;

    private TableProces proces;
    private TablePartie partie;
    private TableSeance seance;
    private TableJury jury;
    private TableJuge juge;
    private TableAvocat avocat;

    private GestionProces gestionProces;
    private GestionPartie gestionPartie;
    private GestionSeance gestionSeance;
    private GestionJury gestionJury;
    private GestionJuge gestionJuge;
    private GestionAvocat gestionAvocat;

    /**
     * Ouvre une connexion avec la BD relationnelle et alloue les gestionnaires
     * de transactions et les tables
     * 
     * @param serveur
     * @param bd
     * @param user
     * @param password
     * @throws IFT287Exception
     */
    public GestionJustice(String serveur, String bd, String user, String password) throws IFT287Exception
    {
        cx = new Connexion(serveur, bd, user, password);

        proces = new TableProces(cx);
        partie = new TablePartie(cx);
        seance = new TableSeance(cx);
        jury = new TableJury(cx);
        juge = new TableJuge(cx);
        avocat = new TableAvocat(cx);

        gestionProces = new GestionProces(proces, seance, juge, partie, jury);
        gestionPartie = new GestionPartie(partie, avocat);
        gestionSeance = new GestionSeance(seance, proces);
        gestionJury = new GestionJury(jury, proces);
        gestionJuge = new GestionJuge(juge, proces);
        gestionAvocat = new GestionAvocat(avocat);
    }

    /**
     * Fermer la connexion
     */
    public void fermer()
    {
        cx.fermer();
    }

    /**
     * @return the cx
     */
    public Connexion getCx()
    {
        return cx;
    }

    /**
     * @return the gestionProces
     */
    public GestionProces getGestionProces()
    {
        return gestionProces;
    }

    /**
     * @param gestionProces
     *            the gestionProces to set
     */
    public void setGestionProces(GestionProces gestionProces)
    {
        this.gestionProces = gestionProces;
    }

    /**
     * @return the gestionPartie
     */
    public GestionPartie getGestionPartie()
    {
        return gestionPartie;
    }

    /**
     * @param gestionPartie
     *            the gestionPartie to set
     */
    public void setGestionPartie(GestionPartie gestionPartie)
    {
        this.gestionPartie = gestionPartie;
    }

    /**
     * @return the gestionSeance
     */
    public GestionSeance getGestionSeance()
    {
        return gestionSeance;
    }

    /**
     * @param gestionSeance
     *            the gestionSeance to set
     */
    public void setGestionSeance(GestionSeance gestionSeance)
    {
        this.gestionSeance = gestionSeance;
    }

    /**
     * @return the gestionJury
     */
    public GestionJury getGestionJury()
    {
        return gestionJury;
    }

    /**
     * @param gestionJury
     *            the gestionJury to set
     */
    public void setGestionJury(GestionJury gestionJury)
    {
        this.gestionJury = gestionJury;
    }

    /**
     * @return the gestionJuge
     */
    public GestionJuge getGestionJuge()
    {
        return gestionJuge;
    }

    /**
     * @param gestionJuge
     *            the gestionJuge to set
     */
    public void setGestionJuge(GestionJuge gestionJuge)
    {
        this.gestionJuge = gestionJuge;
    }

    /**
     * @return the gestionAvocat
     */
    public GestionAvocat getGestionAvocat()
    {
        return gestionAvocat;
    }

    /**
     * @param gestionAvocat
     *            the gestionAvocat to set
     */
    public void setGestionAvocat(GestionAvocat gestionAvocat)
    {
        this.gestionAvocat = gestionAvocat;
    }
}