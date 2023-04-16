package pyramidi.model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import pyramidi.util.FileHelper;

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

    public static Song loadFromFile(String path) throws ParseException, IOException {
        List<String> songFields = FileHelper.readLines(path, false);

        String title = songFields.get(0);
        String artist = songFields.get(1);
        String album = songFields.get(2);
        String publishedString = songFields.get(3);
        double duration = Double.parseDouble(songFields.get(4));

        // Tar hånd om ukjente verdier og setter dem til null.
        if (artist.equals("Ukjent artist")) {
            artist = null;
        }
        if (album.equals("Ukjent album")) {
            album = null;
        }
        Calendar published;
        if (publishedString.equals("Ukjent publiseringsdato")) {
            published = null;
        } else {
            // Sakset fra https://stackoverflow.com/questions/2331513/convert-a-string-to-a-gregoriancalendar
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            published = new GregorianCalendar();
            published.setTime(df.parse(publishedString));
        }

        return new Song(title, artist, album, published, duration);
    }

    public void saveToFile(String path) throws IOException {
        // String file = Song.class.getResource(path).getFile();

        List<String> songFields = new ArrayList<>();
        songFields.addAll(List.of(getTitle(), getArtist(), getAlbum(), getDisplayedPublishDate(), String.valueOf(getDuration())));
        FileHelper.writeLines(path, songFields);
        
    }

    public String getStatePath() {
        return "state/playables/" + hashCode() + ".play";
    }

    public void saveState() throws IOException {
        saveToFile(getStatePath());
    }

    @Override
    public String toString() {
        return getTitle() + " (" + getDisplayedDuration() + ")" + " - " + getDisplayedAlbum() + " - " + getDisplayedArtist();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((artist == null) ? 0 : artist.hashCode());
        result = prime * result + ((album == null) ? 0 : album.hashCode());
        result = prime * result + ((publishDate == null) ? 0 : publishDate.hashCode());
        long temp;
        temp = Double.doubleToLongBits(duration);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        Song other = (Song) obj;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (artist == null) {
            if (other.artist != null)
                return false;
        } else if (!artist.equals(other.artist))
            return false;
        if (album == null) {
            if (other.album != null)
                return false;
        } else if (!album.equals(other.album))
            return false;
        if (publishDate == null) {
            if (other.publishDate != null)
                return false;
        } else if (!publishDate.equals(other.publishDate))
            return false;
        if (Double.doubleToLongBits(duration) != Double.doubleToLongBits(other.duration))
            return false;
        return true;
    }

    @Override
    public void play() {
        System.out.println("Playing " + this.getTitle());
    }


    public static void main(String[] args) {
        try {
            Song fileTest = Song.loadFromFile("state/playables/hollywood.play");
            System.out.println(fileTest.getDisplayedPublishDate());
            fileTest.saveToFile("state/playables/hollywoody2.play");

            Song phadThai = new Song("Phad Thai", "Klossmajor", "Klossmajor", new GregorianCalendar(2019, 11, 5), 211);
            phadThai.saveState();
            Song phadThai2 = new Song("Phad Thai", "Klossmajor", "Klossmajor", new GregorianCalendar(2019, 11, 5), 211);


            Song newPhad = Song.loadFromFile(phadThai2.getStatePath());
            System.out.println(newPhad);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
