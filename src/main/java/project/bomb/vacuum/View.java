package project.bomb.vacuum;

/**
 * Handles displaying the state of the game to the user.
 */
public interface View {

    /**
     * Stores the instance of the currently used View.
     */
    ViewContainer container = new ViewContainer();

    /**
     * Gets the stored view.
     *
     * @return the stored view.
     */
    static View getView() {
        return container.getView();
    }

    /**
     * Stores the given view.
     *
     * @param view the view to store.
     */
    static void setView(View view) {
        container.setView(view);
    }

    /**
     * Creates a new board of tiles using the specified size.
     *
     * @param rows    number of rows in the tile grid.
     * @param columns number of columns in the tile grid.
     */
    void initializeBoard(int rows, int columns);

    /**
     * The user has won or lost, let them know!
     */
    void gameOver(GameOverState gameOverState, long time, boolean newHighScore);

    /**
     * Set the displayed time.
     *
     * @param time the time to display.
     */
    void setTime(long time);

}
