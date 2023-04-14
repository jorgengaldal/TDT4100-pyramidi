package pyramidi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PyramidTest {
    
    private Pyramid pyramid;
    Song phadThai = new Song("Phad Thai", "Klossmajor", "Klossmajor", new GregorianCalendar(2019, 11, 5), 211);
    Song detErJoBareKodd = new Song("Det er jo bare kødd - Album edition", "Klossmajor", "Alt jeg ikke har", null,
            178);
    Song hollywood = new Song("Hollywood", "Cezinando", "Et godt stup i et grunt vann", new GregorianCalendar(2020, 2, 23), 400);


    @BeforeEach
    public void setup() {
        pyramid = new Pyramid(0);
        pyramid.addLayer(20);
        pyramid.addLayer(10);

        pyramid.addPlayable(hollywood);
        pyramid.addPlayable(hollywood);
        pyramid.addPlayable(hollywood);
        pyramid.addPlayable(phadThai);
        pyramid.addPlayable(detErJoBareKodd);
    
    }

    @Test
    public void testConstructorWithZeroBaseWeight() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pyramid(0);
        });
    }

    @Test
    public void testPromotingSong() {
        pyramid.promotePlayable(phadThai, pyramid.getLayer(0));
        assertTrue(pyramid.getLayer(10).contains(phadThai));
        assertTrue(!pyramid.getLayer(0).contains(phadThai));
    }

    @Test
    public void testRemoveNonEmptyNotBottomLayerMovesPlayable() {
        pyramid.promotePlayable(detErJoBareKodd, pyramid.getLayer(0));
        pyramid.removeLayer(10);
        assertTrue(pyramid.getLayer(0).contains(detErJoBareKodd));
    }
    
    @Test
    public void testRemoveBottomLayerMovesPlayables() {
        pyramid.removeLayer(0);
        assertTrue(pyramid.getLayer(10).contains(detErJoBareKodd));
        assertTrue(pyramid.getLayer(10).contains(detErJoBareKodd));
        assertTrue(pyramid.getLayer(10).contains(hollywood));
    }

    @Test
    public void testRemoveLayerStillCorrectTotalWeight() {
        pyramid.removeLayer(10);
        assertEquals(20, pyramid.getTotalLayerWeight());
    }

    @Test
    public void testTotalPlayablesCorrect() {
        assertEquals(5, pyramid.getTotalPlayablesNumber());
    }

    @Test
    public void testTotalPlayTimeCorrect() {
        assertEquals(400*3+178+211, pyramid.getTotalPlayTime());
    }

}
