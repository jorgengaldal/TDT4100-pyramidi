package pyramidi.model;

import java.util.GregorianCalendar;
import java.util.Iterator;

public class PyramidLayerPlayableIterator implements Iterator<Playable> {

    private PyramidLayer layer;
    private int cursor;

    public PyramidLayerPlayableIterator(PyramidLayer layer) {
        this.layer = layer;
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        return layer.size() > 0;
    }

    @Override
    public Playable next() {
        Playable result = layer.get(cursor);
        if (cursor == layer.size() - 1) {
            cursor = 0;
        } else {
            cursor++;
        }
        return result;
    }
    
    public static void main(String[] args) {        
        Song phadThai = new Song("Phad Thai", "Klossmajor", "Klossmajor", new GregorianCalendar(2019, 11, 5), 211);
        Song detErJoBareKodd = new Song("Det er jo bare kødd - Album edition", "Klossmajor", "Alt jeg ikke har", null,
                178);
        Song hollywood = new Song("Hollywood", "Cezinando", "Et godt stup i et grunt vann", new GregorianCalendar(2020, 2, 23), 400);

        Pyramid pyramid = new Pyramid(0);
        pyramid.addLayer(20);
        pyramid.addLayer(10);

        pyramid.addPlayable(hollywood);
        pyramid.addPlayable(hollywood);
        pyramid.addPlayable(hollywood);
        pyramid.addPlayable(phadThai);
        pyramid.addPlayable(detErJoBareKodd);
        PyramidLayerPlayableIterator iter = new PyramidLayerPlayableIterator(pyramid.getLayer(0));

        int count = 0;
        while (iter.hasNext()) {
            System.out.println(iter.next().getTitle());
            count++;
            if (count == 10) {
                break;
            }
        }
    }
}
