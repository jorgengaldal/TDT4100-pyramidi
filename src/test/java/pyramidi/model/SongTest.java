package pyramidi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.text.ParseException;
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

    @DisplayName("Tester konstruktør med tittel lik null")
    @Test
    public void testConstructorWithNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Song(null, null, null, null, 0);
        });
    }

    @DisplayName("Tester konstruktør med negativ tid")
    @Test
    public void testConstructorWithNegativeDuration() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Song("Dans på bordet", null, null, null, -2.4);
        });
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

    @DisplayName("Tester at lagring og henting fra fil gir samme sang")
    @Test
    public void testLoadFromFileEqualsSavedToFileSong() throws IOException, ParseException {
        Song laxx = new Song("Laxx", "Tim Ayre", "Laxx", new GregorianCalendar(2019, 11, 5) , 207);
        laxx.saveState();

        Song expectedLaxx = Song.loadFromFile(laxx.getStatePath());

        assertEquals(laxx, expectedLaxx);
    }


}