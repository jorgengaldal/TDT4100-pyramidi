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
        this.title = title;
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
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public Calendar getPublishDate() {
        return publishDate;
    }

    public double getDuration() {
        return duration;
    }

    public static Song loadFromFile(String path) {
        return new Song(null, null, null, null, 0.5);
    }

    @Override
    public void play() {
        System.out.println("Playing " + this.getTitle());
    }

}
