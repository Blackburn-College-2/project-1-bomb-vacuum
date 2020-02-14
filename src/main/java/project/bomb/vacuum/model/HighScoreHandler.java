package project.bomb.vacuum.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import project.bomb.vacuum.DefaultBoard;
import project.bomb.vacuum.HighScore;

public class HighScoreHandler {

    private final static String SCORES_URL = "./src/main/java/project/bomb/vacuum/scores/";
    private final static String EASY_SCORES_URL = SCORES_URL + "easy.txt";
    private final static String INTERMEDIATE_SCORES_URL = SCORES_URL + "intermediate.txt";
    private final static String EXPERT_SCORES_URL = SCORES_URL + "expert.txt";

    public void saveHighScore(DefaultBoard board, String name, long time) {
        String URL = this.selectScoreURL(board);
        this.saveHighScore(URL, name, time);
    }

    public List<HighScore> loadSortedScores(DefaultBoard board) {
        List<HighScore> scores;
        String URL = this.selectScoreURL(board);
        scores = this.loadScores(URL);
        this.sortScores(scores);
        return scores;
    }

    private void saveHighScore(String scoreURL, String name, long time) {
        File file = new File(scoreURL);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            String score = this.formatScore(name, time);
            writer.append('\n').append(score);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String selectScoreURL(DefaultBoard board) {
        String URL = null;
        switch (board) {
            case EASY:
                URL = HighScoreHandler.EASY_SCORES_URL;
                break;
            case INTERMEDIATE:
                URL = HighScoreHandler.INTERMEDIATE_SCORES_URL;
                break;
            case EXPERT:
                URL = HighScoreHandler.EXPERT_SCORES_URL;
                break;
        }
        return URL;
    }

    private String formatScore(String name, long time) {
        return String.format("%s|%d", name, time);
    }

    private String[] parseScore(String rawScore) {
        return rawScore.split("\\|");
    }

    /**
     * Returns a list of HighScore objects loaded from a file.
     * <p>
     * Each line in the specified file will be a returned String.
     *
     * @param URL location of the file to read from.
     * @return a list of HighScore objects loaded from the URL.
     */
    private List<HighScore> loadScores(String URL) {
        return this.loadScores(URL, true);
    }

    private List<HighScore> loadScores(String URL, boolean createFileIfNotFound) {
        List<HighScore> scores = null;
        List<String> rawScores = new ArrayList<>();
        File file = new File(URL);
        BufferedReader input = null;
        String score;

        try {
            input = new BufferedReader(new FileReader(file));
            while ((score = input.readLine()) != null) {
                rawScores.add(score);
            }
            scores = parseScores(rawScores);
        } catch (FileNotFoundException e) {
            if (createFileIfNotFound) {
                try {
                    new FileWriter(file);
                    scores = loadScores(URL, false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
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

        return scores;
    }

    private List<HighScore> parseScores(List<String> rawScores) {
        List<HighScore> scores = new ArrayList<>();

        String[] parts;
        for (String rawScore : rawScores) {
            parts = this.parseScore(rawScore);
            scores.add(new SimpleHighScore(parts[0], Long.parseLong(parts[1])));
        }

        return scores;
    }

    private void sortScores(List<HighScore> scores) {
        scores.sort((o1, o2) -> {
            long diff = o1.getTime() - o2.getTime();
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            } else {
                return 0;
            }
        });
    }
}
