package pyramidi.model;

/**
 * Dette grensesnittet brukes for å type de klassene som inneholder 
 * {@code Playable} og som kan spilles av fra 
 * {@code Player}. (Eksempelvis {@code Pyramid}, men ikke {@code PyramidLayer})
 */
public interface PlayableContainer extends Iterable<Playable> {

    /**
     * @return Antall {@code Playable} i {@code this}
     */
    public int getTotalPlayablesNumber();

    public double getTotalPlayTime();

}
