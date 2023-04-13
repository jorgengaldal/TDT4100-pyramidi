package pyramidi.model;

import java.util.Iterator;
import java.util.List;

public class PyramidLayerIterator implements Iterator<PyramidLayer>{

    private Pyramid pyramid;
    private int currentIndex;
    private List<Integer> weights;

    public PyramidLayerIterator(Pyramid pyramid) {
        this.pyramid = pyramid;
        this.weights = this.pyramid.getSortedWeights();
    }

    @Override
    public boolean hasNext() {
        return currentIndex < weights.size();
    }

    @Override
    public PyramidLayer next() {
        PyramidLayer result = pyramid.getLayer(weights.get(currentIndex));
        currentIndex++;
        return result;
    }
    
}
