package pyramidi.model;

public class Player {
    
    private Playable currentPlayable;
    private PlayableContainer currentContainer;

    public Playable getCurrentPlayable() {
        return currentPlayable;
    }

    private void setCurrentPlayable(Playable playable) {
        this.currentPlayable = playable;
    }

    public void next() {
        // TODO: Implement
        // setCurrentPlayable(currentPlayable);
    }
}
