package pyramidi.model;

import java.util.Calendar;

public class Song implements Playable {

    private String title;
    private String artist;
    private String album;
    private Calendar publishDate; // Vurder å gjøre til Date-objekt
    private double duration;

    public Song(String title, String artist, String album, Calendar published, double duration) {
        setTitle(title);
        setArtist(artist);
        setAlbum(album);
        setPublishDate(published);
        setDuration(duration);
    }

    private void setTitle(String title) {
        validateTitle(title);
        this.title = title;
    }

    private void validateTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
    }

    private void setArtist(String artist) {
        this.artist = artist;
    }

    private void setAlbum(String album) {
        this.album = album;
    }

    private void setPublishDate(Calendar publishDate) {
        this.publishDate = publishDate;
    }

    private void setDuration(double duration) {
        validateDuration(duration);
        this.duration = duration;
    }

    private void validateDuration(double duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getDisplayedArtist() {
        String artist = getArtist();
        return artist != null ? artist : "Ukjent artist";
    }

    public String getAlbum() {
        return album;
    }

    public String getDisplayedAlbum() {
        String album = getAlbum();
        return album != null ? album : "Ukjent album";
    }

    public Calendar getPublishDate() {
        return publishDate;
    }

    public String getDisplayedPublishDate() {
        Calendar publishDate = getPublishDate();
        if (publishDate == null) {
            return "Ukjent publiseringsdato";
        } else {
            // Merk: +1 på måned fordi Calendar.MONTH er nullindeksert (smh)
            return String.format("%02d", publishDate.get(Calendar.DAY_OF_MONTH)) + "."
            + String.format("%02d", publishDate.get(Calendar.MONTH)+1) + "."
            + publishDate.get(Calendar.YEAR);
        }
    }

    public double getDuration() {
        return duration;
    }

    public String getDisplayedDuration() {
        int seconds = (int) Math.round(getDuration() % 60);
        int minutes = (int) Math.floor(getDuration() / 60);
        return minutes + ":" + seconds;

    }

    public static Song loadFromFile(String path) {
        // TODO: Implement
        return new Song(null, null, null, null, 0.5);
    }

    public void saveToFile(String path) {
        // TODO: Implement
    } 

    @Override
    public String toString() {
        return getTitle() + " (" + getDisplayedDuration() + ")" + " - " + getDisplayedAlbum() + " - " + getDisplayedArtist();
    }

    @Override
    public void play() {
        System.out.println("Playing " + this.getTitle());
    }

}
