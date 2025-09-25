package bc.bob.chores;

import javax.swing.JTextArea;

public class WorkReport {

    String prepareReport(final Boolean enabled, final String originalReportText) {
        if (!Boolean.TRUE.equals(enabled)) {
            if (originalReportText == null) {
                return "";
            }
            return originalReportText;
        }

        if (originalReportText == null) {
            return "";
        }

        final String taskPrefix = "You completed a task:";
        final String[] lines = originalReportText.split("\\r?\\n");
        final StringBuilder builder = new StringBuilder();
        boolean waitingForTaskDescription = false;

        for (int index = 0; index < lines.length; index++) {
            final String trimmedLine = lines[index].trim();

            if (waitingForTaskDescription) {
                if (!trimmedLine.isEmpty()) {
                    if (builder.length() > 0) {
                        builder.append('\n');
                    }
                    builder.append("- ");
                    builder.append(trimmedLine);
                    waitingForTaskDescription = false;
                }
                continue;
            }

            if (taskPrefix.equals(trimmedLine)) {
                waitingForTaskDescription = true;
            }
        }

        return builder.toString();
    }

}
