package pyramidi;

import java.io.IOException;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import pyramidi.model.Playable;
import pyramidi.model.PlayablesLibrary;
import pyramidi.model.Player;
import pyramidi.model.Pyramid;
import pyramidi.model.PyramidLayer;
import pyramidi.model.Song;

public class pyramidiController {

    // TODO: Fix sånn at player er synkronisert ved endring i hvilke layers som er.
    // TODO: Sammensett alle update-funksjonene til én
    
    private static final String STATEPATH = "state/minesanger.pyra";
    @FXML private Text tittel;
    @FXML private Text artist;
    @FXML private Text album;
    @FXML private Text tid;

    @FXML private ListView<String> pyramide;
    @FXML private ListView<String> lag;
    @FXML private ListView<Playable> bibliotek;

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
    private PlayablesLibrary library;

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

    void loadState() throws ParseException, IOException {
        this.pyramid = Pyramid.loadFromFile("state/minesanger.pyra");
        this.library = new PlayablesLibrary();
        this.bibliotek.getItems().setAll(library.getPlayables());
    }

    @FXML
    void initialize() {
        try {
            loadState();
            // TODO Lag ordentlige warnings her.
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.player = new Player(pyramid);
        
        // Initially disables all buttons
        demote.setDisable(true);
        remove.setDisable(true);
        promote.setDisable(true);
        add.setDisable(true);
        addLayerButton.setDisable(true);
        removeLayerButton.setDisable(true);

        this.pyramide.getSelectionModel().selectedItemProperty().addListener((property, oldValue, newValue) -> {
            updateLagView(newValue);
            add.setDisable(newValue == null || getSelectedLayer() == null);
        });

        this.lag.getSelectionModel().selectedItemProperty().addListener((property, oldValue, newValue) -> {
            remove.setDisable(newValue == null);
            promote.setDisable(newValue == null || getSelectedLayer() == pyramid.getTopLayer());
            demote.setDisable(newValue == null || getSelectedLayer() == pyramid.getBottomLayer());
        });

        this.bibliotek.getSelectionModel().selectedItemProperty().addListener((property, oldValue, newValue) -> {
            add.setDisable(newValue == null || getSelectedLayer() == null);
        });

        // Filtrerer slik at det bare kan være tall i nivå-velger
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

    private void updateLagView(PyramidLayer layer) {
        lag.getItems().setAll(layer.getContents().stream().map(Playable::getTitle).toList());
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
        saveState();
    }

    @FXML
    void handleAddPlayableToLayer() {
        Playable selectedItem = bibliotek.getSelectionModel().getSelectedItem();
        getSelectedLayer().add(selectedItem);
        updateLagView(getSelectedLayer());
        saveState();
    }

    @FXML
    void handleRemoveLayer() {
        pyramid.removeLayer(getLayerManagementFieldValue());
        updatePyramideView();
        updateLayerManagementButtons(true);
        layerManagementField.setText("");
        String newLayerWeight = String.valueOf(pyramid.getSortedWeights().get(0));
        pyramide.getSelectionModel().select(newLayerWeight);
        updateLagView(newLayerWeight);
        saveState();

    }

    @FXML
    void skip() {  
        player.next();
        updatePlayer();
    }

    private PyramidLayer getSelectedLayer() {
        String result = pyramide.getSelectionModel().getSelectedItem();
        if (result == null) {
            return null;
        }
        return pyramid.getLayer(Integer.valueOf(result));
    }

    private Playable getCurrentlySelectedPlayable() {
        int selectedPlayableIndex = lag.getSelectionModel().getSelectedIndex();

        return getSelectedLayer().get(selectedPlayableIndex);
    }

    @FXML
    public void handlePromote() {
        pyramid.promotePlayable(getCurrentlySelectedPlayable(), getSelectedLayer());
        updateLagView(pyramide.getSelectionModel().getSelectedItem());
        saveState();
    }

    private void saveState() {
        try {
            pyramid.saveToFile(pyramidiController.STATEPATH);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // TODO Håndter
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDemote() {
        pyramid.demotePlayable(getCurrentlySelectedPlayable(), getSelectedLayer());
        updateLagView(pyramide.getSelectionModel().getSelectedItem());
        saveState();
    }

    @FXML
    public void handleRemove() {
        getSelectedLayer().remove(getCurrentlySelectedPlayable());
        updateLagView(pyramide.getSelectionModel().getSelectedItem());
        saveState();
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
        if (currentPlayable == null) {
            tittel.setText("[Ingen tilgjengelig]");
            artist.setText("");
            tid.setText("mm:ss");
            album.setText("");
        } else {
            tittel.setText(currentPlayable.getTitle());
            artist.setText(currentPlayable.getDisplayedArtist());
            tid.setText(currentPlayable.getDisplayedDuration());
            if (currentPlayable instanceof Song) {
                Song currentSong = (Song) currentPlayable;
                album.setText(currentSong.getDisplayedAlbum());
            }
        }
    }
}
