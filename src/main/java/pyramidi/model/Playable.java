package pyramidi.model;

public interface Playable {
    

    /**
     * Metode for å spille av sangen.
     * 
     * @apiNote Foreløpig ikke implementert skikkelig.
     */
    public void play();

    public double getDuration();

    public String getDisplayedDuration();

    public String getTitle();

    public String getDisplayedArtist();

}
