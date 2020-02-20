package project.bomb.vacuum.view;

public class Util {

    /**
     * Takes in a time in milliseconds and return a String formatted
     * in the form of "hh:mm:ss" where "h" is hours, "m" is minutes,
     * and "s" is seconds.
     *
     * @param time a time in milliseconds.
     * @return a formatted String of the time.
     */
    static String formatTime(long time) {
        int millisInHour = 3600000;
        int millisInMinute = 60000;
        int millisInSecond = 1000;

        long hours = time / millisInHour;
        time = time % millisInHour;
        long minutes = time / millisInMinute;
        time = time % millisInMinute;
        long seconds = time / millisInSecond;

        String hoursText = String.format("%2d", hours).replace(' ', '0');
        String minutesText = String.format("%2d", minutes).replace(' ', '0');
        String secondsText = String.format("%2d", seconds).replace(' ', '0');

        return String.format("%s:%s:%s", hoursText, minutesText, secondsText);
    }
}
