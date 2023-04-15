package pyramidi;

import java.util.GregorianCalendar;
import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import pyramidi.model.Playable;
import pyramidi.model.Player;
import pyramidi.model.Pyramid;
import pyramidi.model.PyramidLayer;
import pyramidi.model.PyramidLayerIterator;
import pyramidi.model.Song;

public class pyramidiController {

    // TODO: Fix sånn at player er synkronisert ved endring i hvilke layers som er.
    
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
    @FXML private Button promote;
    @FXML private Button demote;
    @FXML private Button addLayerButton;
    @FXML private Button removeLayerButton;

    @FXML private TextField layerManagementField;

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

        pyramid.addLayer(2);
        pyramid.addPlayable(hollywood);
        pyramid.promotePlayable(hollywood, pyramid.getLayer(1));
    }

    @FXML
    void initialize() {
        this.pyramid = new Pyramid();
        addPlaceholderSongs();
        this.player = new Player(pyramid);
        
        demote.setDisable(true);
        remove.setDisable(true);
        promote.setDisable(true);
        add.setDisable(true);
        addLayerButton.setDisable(true);
        removeLayerButton.setDisable(true);

        this.pyramide.getSelectionModel().selectedItemProperty().addListener((property, oldValue, newValue) -> {
            updateLagView(newValue);
        });

        this.lag.getSelectionModel().selectedItemProperty().addListener((property, oldValue, newValue) -> {
            remove.setDisable(newValue == null);
            promote.setDisable(newValue == null || getSelectedLayer() == pyramid.getTopLayer());
            demote.setDisable(newValue == null || getSelectedLayer() == pyramid.getBottomLayer());
        });

        // Filtrerer slik at det bare kan være tall
        UnaryOperator<TextFormatter.Change> positiveIntegerFiler = (change) -> {
            return change.getControlNewText().matches("[1-9][0-9]*") || change.getControlNewText().equals("") ? change : null;
        };
        TextFormatter<Integer> numberFormatter = new TextFormatter<>(positiveIntegerFiler);
        layerManagementField.setTextFormatter(numberFormatter);

        this.layerManagementField.textProperty().addListener((property, oldValue, newValue) -> {
            if (newValue.equals("")) {
                removeLayerButton.setDisable(true);
                addLayerButton.setDisable(true);
                return;
            }

            updateLayerManagementButtons(!pyramid.containsLayer(Integer.valueOf(newValue)));
        });

        updatePlayer();
        updatePyramideView();
    }

    private void updateLayerManagementButtons(boolean enableAdd) {
        removeLayerButton.setDisable(enableAdd);
        addLayerButton.setDisable(!enableAdd);

    }

    private void updateLagView(String weight) {
        if (weight == null) {
            return;
        }
        int parsedWeight = Integer.parseInt(weight);
        lag.getItems().setAll(pyramid.getLayer(parsedWeight).getContents().stream().map(Playable::getTitle).toList());
    }

    private int getLayerManagementFieldValue() {
        String text = layerManagementField.getText();
        return Integer.parseInt(text);
    }

    @FXML
    void handleAddLayer() {
        pyramid.addLayer(getLayerManagementFieldValue());
        updatePyramideView();
        updateLayerManagementButtons(false);
        layerManagementField.setText("");
    }

    @FXML
    void handleRemoveLayer() {
        pyramid.removeLayer(getLayerManagementFieldValue());
        updatePyramideView();
        updateLayerManagementButtons(true);
        layerManagementField.setText("");

    }

    @FXML
    void skip() {
        if (pyramid.getTotalPlayablesNumber() == 0) {
            tittel.setText("[Ingen tilgjengelig]");
            artist.setText("");
            tid.setText("mm:ss");
            album.setText("");
            return;
        }
        
        player.next();
        updatePlayer();
    }

    private PyramidLayer getSelectedLayer() {
        return pyramid.getLayer(Integer.valueOf(pyramide.getSelectionModel().getSelectedItem()));
    }

    private Playable getCurrentlySelectedPlayable() {
        int selectedPlayableIndex = lag.getSelectionModel().getSelectedIndex();

        return getSelectedLayer().get(selectedPlayableIndex);
    }

    @FXML
    public void handlePromote() {
        pyramid.promotePlayable(getCurrentlySelectedPlayable(), getSelectedLayer());
        updateLagView(pyramide.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void handleDemote() {
        pyramid.demotePlayable(getCurrentlySelectedPlayable(), getSelectedLayer());
        updateLagView(pyramide.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void handleRemove() {
        getSelectedLayer().remove(getCurrentlySelectedPlayable());
        updateLagView(pyramide.getSelectionModel().getSelectedItem());
    }

    private void updatePyramideView() {
        // for (Integer weight : pyramid.getSortedWeights()) {
        //     String weightString = String.valueOf(weight);
        //     pyramide.getItems().add(weightString);
        // }
        pyramide.getItems().setAll(pyramid.getSortedWeights().stream().map((x) -> x.toString()).toList());
    }

    private void updatePlayer() {
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
