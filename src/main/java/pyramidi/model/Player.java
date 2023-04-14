package pyramidi.model;

import java.util.GregorianCalendar;
import java.util.Iterator;

public class Player {
    
    private Playable currentPlayable;
    private PlayableContainer currentContainer;
    private Iterator<Playable> iterator;

    public Player(PlayableContainer container) {
        this.currentContainer = container;
        this.iterator = currentContainer.iterator();
        next();
    }

    public Playable getCurrentPlayable() {
        return currentPlayable;
    }

    public PlayableContainer getCurrentContainer() {
        return currentContainer;
    }

    private void setCurrentPlayable(Playable playable) {
        this.currentPlayable = playable;
    }

    public void next() {
        if (!iterator.hasNext()) {
            throw new IllegalStateException("No Playable in iterator");
        }
        setCurrentPlayable(iterator.next());
    }

    public static void main(String[] args) {
        Pyramid pyramid = new Pyramid();
        Song phadThai = new Song("Phad Thai", "Klossmajor", "Klossmajor", new GregorianCalendar(2019, 11, 5), 211);
        Song detErJoBareKodd = new Song("Det er jo bare kødd - Album edition", "Klossmajor", "Alt jeg ikke har", null,
                178);
        Song hollywood = new Song("Hollywood", "Cezinando", "Et godt stup i et grunt vann", new GregorianCalendar(2020, 2, 23), 400);

        pyramid.addLayer(2);
        pyramid.addPlayable(phadThai);
        pyramid.addPlayable(detErJoBareKodd);
        pyramid.addPlayable(hollywood);
        pyramid.promotePlayable(hollywood, pyramid.getLayer(1));
        Player player = new Player(pyramid);

        for (int index = 0; index < 20; index++) {
            System.out.println(player.getCurrentPlayable());
            player.next();
        }
    }
}
