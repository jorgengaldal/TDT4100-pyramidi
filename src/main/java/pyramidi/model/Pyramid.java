package pyramidi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Pyramid implements PlayableContainer {

    /* TODO: Legg til mulighet for å enkelt endre vekt til et lag uten å måtte lage et nytt
     * TODO: Bytt over til TreeMap for å unngå å måtte sortere så weights så mange ganger
     */

    private Map<Integer, PyramidLayer> layers;

    public Pyramid(int baseLayerWeight) {
        layers = new HashMap<>();
        addLayer(baseLayerWeight);
    }

    public Pyramid() {
        this(1);
    }

    public void addLayer(int weight) {
        if (layers.keySet().stream().anyMatch((a) -> a == weight)) {
            throw new IllegalArgumentException("There is already a layer with weight " + weight);
        }
        if (weight < 1) {
            throw new IllegalArgumentException("Layer weight must be 1 or above");
        }

        PyramidLayer layer = new PyramidLayer();
        // Legger til layer på riktig plass (nivå) i pyramiden
        layers.put(weight, layer);
    }

    /**
     * @return Liste av vektene, sortert i synkende rekkefølge (øverste nivå først).
     */
    public List<Integer> getSortedWeights() {
        List<Integer> result = new ArrayList<>(layers.keySet());
        Collections.sort(result, (a, b) -> -(a - b));
        return result;
    }

    /**
     * @return Shallow copy av liste over {@code PyramidLayer}
     */
    protected Map<Integer, PyramidLayer> getLayers() {
        // TODO Prøv å finne en bedre måte å løse dette på.
        return Map.copyOf(layers);
    }

    public PyramidLayer getLayer(int weight) {
        return layers.get(weight);
    }

    public void removeLayer(int weight) {
        if (layers.size() == 1) {
            throw new IllegalStateException("Pyramid must have at least one layer, and there is only one left");
        }
        if (!layers.keySet().contains(weight)) {
            throw new IllegalArgumentException("No layer with weight " + weight);
        }

        // Hvis man prøver å fjerne det nederste nivået, flytter man sangene oppover,
        // ellers: nedover.
        List<Integer> weights = getSortedWeights();
        if (weights.get(weights.size() - 1) == weight) {
            int layerAboveWeight = weights.get(weights.size() - 2);
            layers.get(layerAboveWeight).addAll(layers.get(weight).getContents());
        } else {
            int layerBelowWeight = weights.get(weights.indexOf(weight) + 1);
            layers.get(layerBelowWeight).addAll(layers.get(weight).getContents());
        }

        layers.remove(weight);
    }

    // public void removeLayer(PyramidLayer layer) {
    // if (!layers.contains(layer)) {
    // throw new IllegalArgumentException("Layer " + layer + " not in this
    // pyramid");
    // }
    // layers.remove(layer);
    // }

    /**
     * Legger til {@code playable} til det laveste nivået.
     * 
     * @param playable
     */
    public void addPlayable(Playable playable) {
        List<Integer> weights = getSortedWeights();
        layers.get(weights.get(layers.size() - 1)).add(playable);
    }

    public void movePlayable(Playable playable, PyramidLayer from, PyramidLayer to) {
        if (!from.contains(playable)) {
            throw new IllegalArgumentException("Playable " + playable + " is not in layer " + from);
        }
        from.remove(playable);
        to.add(playable);
    }

    public void promotePlayable(Playable playable, PyramidLayer from) {
        List<Integer> weights = getSortedWeights();
        if (layers.get(weights.get(0)) == from) {
            throw new IllegalStateException("Cannot promote playable " + playable + ". Already at top layer");
        }
        int fromLayerWeight = layers.entrySet().stream().filter((a) -> a.getValue().equals(from)).map(Map.Entry::getKey).findFirst().get(); 
        int layerAboveWeight = weights.get(weights.indexOf(fromLayerWeight) - 1);
        movePlayable(playable, from, layers.get(layerAboveWeight));
    }

    public void demotePlayable(Playable playable, PyramidLayer from) {
        List<Integer> weights = getSortedWeights();
        if (layers.get(weights.get(weights.size() - 1)) == from) {
            throw new IllegalStateException("Cannot demote playable " + playable + ". Already at bottom layer");
        }
        int fromLayerWeight = layers.entrySet().stream().filter((a) -> a.getValue().equals(from)).map(Map.Entry::getKey).findFirst().get(); 
        int layerBelowWeight = weights.get(weights.indexOf(fromLayerWeight) + 1);
        movePlayable(playable, from, layers.get(layerBelowWeight));
    }

    public PyramidLayer getTopLayer() {
        return getLayer(getSortedWeights().get(0));
    }

    public PyramidLayer getBottomLayer() {
        List<Integer> sortedWeights = getSortedWeights();
        return getLayer(sortedWeights.get(sortedWeights.size() - 1));
    }

    public boolean containsLayer(int weight) {
        return layers.keySet().contains(weight);
    }

    public int getTotalLayerWeight() {
        return layers.entrySet().stream().mapToInt((a) -> a.getKey()).sum();
    }

    @Override
    public Iterator<Playable> iterator() {
        return new PyramidPlayableIterator(this);
    }

    @Override
    public int getTotalPlayablesNumber() {
        return layers.entrySet().stream().mapToInt((a) -> a.getValue().size()).sum();
    }

    @Override
    public double getTotalPlayTime() {
        return layers.entrySet().stream().mapToDouble((a) -> a.getValue().getTotalPlayTime()).sum();
    }

    public static void main(String[] args) {
        Song phadThai = new Song("Phad Thai", "Klossmajor", "Klossmajor", new GregorianCalendar(2019, 11, 5), 211);
        Song detErJoBareKodd = new Song("Det er jo bare kødd - Album edition", "Klossmajor", "Alt jeg ikke har", null,
                178);
        Song hollywood = new Song("Hollywood", "Cezinando", "Et godt stup i et grunt vann", new GregorianCalendar(2020, 2, 23), 400);

        Pyramid pyramidi = new Pyramid();
        pyramidi.addLayer(20);
        pyramidi.addPlayable(hollywood);
        pyramidi.addPlayable(hollywood);
        pyramidi.addPlayable(hollywood);
        pyramidi.addPlayable(phadThai);
        pyramidi.addPlayable(detErJoBareKodd);
        pyramidi.promotePlayable(hollywood, pyramidi.getLayer(0));
        pyramidi.promotePlayable(phadThai, pyramidi.getLayer(0));
        System.out.println(pyramidi);
    }
}
