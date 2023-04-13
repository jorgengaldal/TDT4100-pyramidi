package pyramidi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Pyramid implements PlayableContainer {

    private Map<Integer, PyramidLayer> layers;

    public Pyramid(int baseLayerWeight) {
        layers = new HashMap<>();
        PyramidLayer baseLayer = new PyramidLayer();
        layers.put(baseLayerWeight, baseLayer);
    }

    public Pyramid() {
        this(0);
    }

    
    public void addLayer(int weight) {
        if (layers.keySet().stream().anyMatch((a) -> a == weight)) {
            throw new IllegalArgumentException("There is already a layer with weight " + weight);
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

        // Hvis man prøver å fjerne det nederste nivået, flytter man sangene oppover, ellers: nedover.
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
    //     if (!layers.contains(layer)) {
    //         throw new IllegalArgumentException("Layer " + layer + " not in this pyramid");
    //     }
    //     layers.remove(layer);
    // }


    @Override
    public Iterator<Playable> iterator() {
        // TODO Skal returnere PyramidIterator
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public int getTotalPlayablesNumber() {
        return layers.entrySet().stream().mapToInt((a) -> a.getValue().size()).sum();
    }

    @Override
    public double getTotalPlayTime() {
        return layers.entrySet().stream().mapToDouble((a) -> a.getValue().getTotalPlayTime()).sum();
    }
    
}
