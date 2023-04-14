package pyramidi;

import java.util.GregorianCalendar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import pyramidi.model.Playable;
import pyramidi.model.Player;
import pyramidi.model.Pyramid;
import pyramidi.model.Song;

public class pyramidiController {
    
    @FXML private Text tittel;
    @FXML private Text artist;
    @FXML private Text album;
    @FXML private Text tid;

    @FXML private ListView<String> pyramide;
    @FXML private ListView<String> lag;
    @FXML private ListView<String> bibliotek;

    @FXML private Button skip;
    @FXML private Button add;
    @FXML private Button remove;

    private Pyramid pyramid;
    private Player player;

    private void addPlaceholderSongs() {
        Song phadThai = new Song("Phad Thai", "Klossmajor", "Klossmajor", new GregorianCalendar(2019, 11, 5), 211);
        Song detErJoBareKodd = new Song("Det er jo bare kødd - Album edition", "Klossmajor", "Alt jeg ikke har", null,
                178);
        Song hollywood = new Song("Hollywood", "Cezinando", "Et godt stup i et grunt vann", new GregorianCalendar(2020, 2, 23), 400);

        pyramid.addPlayable(hollywood);
        pyramid.addPlayable(detErJoBareKodd);
        pyramid.addPlayable(phadThai);
    }

    @FXML
    void initialize() {
        this.pyramid = new Pyramid();
        addPlaceholderSongs();
        this.player = new Player(pyramid);
        updateView();
    }

    @FXML
    void skip() {
        player.next();
        updateView();
    }

    private void updateView() {
        Playable currentPlayable = player.getCurrentPlayable();
        tittel.setText(currentPlayable.getTitle());
        artist.setText(currentPlayable.getDisplayedArtist());
        tid.setText(currentPlayable.getDisplayedDuration());
        if (currentPlayable instanceof Song) {
            Song currentSong = (Song) currentPlayable;
            album.setText(currentSong.getDisplayedAlbum());
        }
    }
}
