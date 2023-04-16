package pyramidi.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

public class PyramidPlayableIterator implements Iterator<Playable> {

    // TODO: Implementer dithering og mer randomness
    // TODO/NOTE: Den velger nå bare hvert nivå hver x-te gang, ikke basert på
    // antall sanger.

    private Pyramid pyramid;
    private Map<Integer, Integer> intervals;
    private Map<Integer, PyramidLayerPlayableIterator> iterators;

    private Random random;
    private int upperRandomBound;

    /**
     * OBS! Merk at denne ikke er lagt for å iterere over en {@code Pyramid} som
     * endrer seg
     * 
     * @param pyramid
     */
    public PyramidPlayableIterator(Pyramid pyramid) {
        this.pyramid = pyramid;
        this.random = new Random();

        List<Integer> sortedWeights = this.pyramid.getSortedWeights();
        // Lager Map over iterators.
        this.iterators = new HashMap<>();
        for (Integer weight : sortedWeights) {
            this.iterators.put(weight, new PyramidLayerPlayableIterator(pyramid.getLayer(weight)));
        }

        syncToPyramid();
    }

    private void syncToPyramid() {
        this.intervals = new TreeMap<>();

        // Legger inn vekter i this.intervals
        int runningSum = 0;
        List<Integer> sortedWeights = this.pyramid.getSortedWeights();
        for (Integer weight : sortedWeights) {

            // Ganger weight med antall sanger i gitt lag for at vektforholdene er mellom
            // hver sang i nivåene, ikke mellom nivåene selv.
            int bound = runningSum + weight * pyramid.getLayer(weight).size();
            intervals.put(bound, weight);
            runningSum = bound;
            // Går gjennom og legger til eventuelt nye lag
            if (!iterators.keySet().contains(weight)) {
                this.iterators.put(weight, new PyramidLayerPlayableIterator(pyramid.getLayer(weight)));
            }
        }
        ;
        // Går gjennom og fjerner eventuelt fjernede lag
        for (Integer weight : iterators.keySet()) {
            if (!sortedWeights.contains(weight)) {
                this.iterators.remove(weight);
            }
        }

        this.upperRandomBound = runningSum; // Setter maks-indeks for random valg til den øverste verdien i intervals.
                                            // (unngår IOOB)


    }

    @Override
    public boolean hasNext() {
        return pyramid.getTotalPlayablesNumber() > 0;
    }

    @Override
    public Playable next() {
        syncToPyramid();
        int r = random.nextInt(this.upperRandomBound); // Fra null til høyeste indeks i intervals.keys
        for (Entry<Integer, Integer> entrySet : intervals.entrySet()) {
            if (r < entrySet.getKey()) {
                PyramidLayerPlayableIterator layerIter = iterators.get(entrySet.getValue());
                if (!layerIter.hasNext()) {
                    // I tilfelle dette laget er tomt.
                    // NB!/TODO: I sjeldne tilfeller vil dette kunne skape et problem med recursion
                    // depth og gi StackOverflowError
                    return next();
                } else {
                    return iterators.get(entrySet.getValue()).next();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // Song phadThai = new Song("Phad Thai", "Klossmajor", "Klossmajor", new
        // GregorianCalendar(2019, 11, 5), 211);
        // Song detErJoBareKodd = new Song("Det er jo bare kødd - Album edition",
        // "Klossmajor", "Alt jeg ikke har", null,
        // 178);
        // Song hollywood = new Song("Hollywood", "Cezinando", "Et godt stup i et grunt
        // vann", new GregorianCalendar(2020, 2, 23), 400);

        // Pyramid pyramid = new Pyramid();
        // pyramid.addLayer(3);
        // pyramid.addLayer(2);

        // pyramid.addPlayable(hollywood);
        // pyramid.promotePlayable(hollywood, pyramid.getLayer(1));
        // pyramid.addPlayable(phadThai);
        // pyramid.addPlayable(detErJoBareKodd);
        // PyramidPlayableIterator iter = new PyramidPlayableIterator(pyramid);

        // int count = 0;
        // while (iter.hasNext()) {
        // System.out.println(iter.next().getTitle());
        // count++;
        // if (count == 10) {
        // break;
        // }
        // }

        try {
            PyramidPlayableIterator iterator = new PyramidPlayableIterator(
                    Pyramid.loadFromFile("state/minesanger.pyra"));
            int count = 0;
            while (iterator.hasNext()) {
                System.out.println(iterator.next().getTitle());
                count++;
                if (count == 10) {
                    break;
                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
