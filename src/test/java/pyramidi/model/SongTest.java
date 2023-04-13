package pyramidi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SongTest {
    

    @DisplayName("Tester konstruktøren med alle verdier som null (men duration=0)")
    @Test
    public void testConstructorWithoutArguments() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Song(null, null, null, null, 0);
        });
    }

    @DisplayName("Tester minimal konstruktør med duration og title satt, skal gi ukjent på resten")
    @Test
    public void testConstructorWithMinimalArguments() {
        Song minisong = new Song("Dans på bordet", null, null, null, 152);

        assertEquals("Ukjent artist", minisong.getDisplayedArtist());
        assertEquals("Ukjent album", minisong.getDisplayedAlbum());
        assertEquals("Ukjent publiseringsdato", minisong.getDisplayedPublishDate());
        // TODO: Test om artist, album og publishdate gir ukjent
    }

    @DisplayName("Tester konstruktør med alt satt til lovlige verdier samt Displayed-gettersene")
    @Test
    public void testConstructorWithValidArgumentsAndDisplayedMethods() {
        Calendar femtedesembertjuenitten = new GregorianCalendar(2019, 11, 5);
        Song laxx = new Song("Laxx", "Tim Ayre", "Laxx", femtedesembertjuenitten , 207);
        assertEquals("Laxx", laxx.getTitle());
        assertEquals("Laxx", laxx.getDisplayedAlbum());
        assertEquals("Tim Ayre", laxx.getDisplayedArtist());
        assertEquals("3:27", laxx.getDisplayedDuration());
        assertEquals("05.12.2019", laxx.getDisplayedPublishDate());
    }
}

// Song phadThai = new Song("Phad Thai", "Klossmajor", "Klossmajor", null, 211);
// Song detErJoBareKodd = new Song("Det er jo bare kødd - Album edition", "Klossmajor", "Alt jeg ikke har", null, 178);
// Song hollywood = new Song("", "Klossmajor", "Alt jeg ikke har", null, 178);