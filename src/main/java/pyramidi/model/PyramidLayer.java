package pyramidi.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PyramidLayer implements Iterable<Playable> {

    private List<Playable> contents;
    // private int weight;
    // TODO (Egt NOTE): Fjerner weight siden layeret ikke trenger å vite om sin egen vekt.

    public PyramidLayer(/*int weight*/) {
        // setWeight(weight);

        contents = new ArrayList<>();
    }

    // private void setWeight(int weight) {
    //     this.weight = weight;
    // }

    // public int getWeight() {
    //     return weight;
    // }

    public int size() {
        return contents.size();
    }

    public double getTotalPlayTime() {
        return contents.stream().mapToDouble((a) -> a.getDuration()).sum();
    }

    public Playable get(int index) {
        return contents.get(index);
    }

    public List<Playable> getContents() {
        return List.copyOf(contents);
    }

    public void add(Playable playable) {
        contents.add(playable);
    }

    public void addAll(List<Playable> playables) {
        for (Playable playable : playables) {
            add(playable);
        }
    }

    public void remove(Playable playable) {
        if (contents.contains(playable)) {
            contents.remove(playable);
        }
    }

    @Override
    public Iterator<Playable> iterator() {
        // TODO Returner PyramidLayerIterator
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }
    
}
