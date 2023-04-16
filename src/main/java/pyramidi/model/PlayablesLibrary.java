package pyramidi.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PlayablesLibrary {
    private Set<Playable> playables;

    public PlayablesLibrary() throws ParseException, IOException {
        this.playables = loadFilesFromFolder("state/playables");
    }

    private Set<Playable> loadFilesFromFolder(String directory) throws ParseException, IOException {
        Set<Playable> result = new HashSet<>();
        try (Stream<Path> stream = Files.list(Paths.get(directory))) {
            Set<String> paths = stream.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString).collect(Collectors.toSet());
            for (String path : paths) {
                String longPath = directory + "/" + path;
                result.add(Song.loadFromFile(longPath));
            }
        }
        return result;
    }

    public Set<Playable> getPlayables() {
        return Set.copyOf(playables);
    }

    public static void main(String[] args) {
        try {
            PlayablesLibrary lib = new PlayablesLibrary();
            System.out.println(lib.getPlayables());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
