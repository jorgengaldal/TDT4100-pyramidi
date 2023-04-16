package pyramidi.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PyramidLayer implements Iterable<Playable> {

    private List<Playable> contents;

    public PyramidLayer() {
        contents = new ArrayList<>();
    }

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

    public boolean contains(Playable playable) {
        return contents.contains(playable);
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
        return new PyramidLayerPlayableIterator(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contents == null) ? 0 : contents.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PyramidLayer other = (PyramidLayer) obj;
        if (contents == null) {
            if (other.contents != null)
                return false;
        } else if (!contents.equals(other.contents))
            return false;
        return true;
    }
    
    
}
