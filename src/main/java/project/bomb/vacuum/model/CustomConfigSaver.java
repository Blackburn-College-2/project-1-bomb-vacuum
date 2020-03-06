package project.bomb.vacuum.model;

import java.io.*;
import project.bomb.vacuum.BoardConfiguration;

public class CustomConfigSaver {

    private final static String URL = "./src/main/java/project/bomb/vacuum/custom/save.txt";

    /**
     * @return the saved board configuration.
     */
    public BoardConfiguration getSavedConfig() {
        File file = new File(URL);
        BufferedReader input = null;
        String rawConfig = "";

        try {
            input = new BufferedReader(new FileReader(file));
            rawConfig = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (rawConfig == null || rawConfig.length() < 5) {
            return null;
        }

        String[] parts = rawConfig.split("\\|");

        return new BoardConfiguration(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2])
        );
    }

    /**
     * @param configuration the board configuration to save.
     */
    public void saveConfig(BoardConfiguration configuration) {
        File file = new File(URL);
        BufferedWriter output = null;
        String config = String.format("%d|%d|%d", configuration.rows, configuration.columns, configuration.bombs);

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(config);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
