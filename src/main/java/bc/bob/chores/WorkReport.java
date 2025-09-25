package bc.bob.chores;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WorkReport {

    private static final Logger LOG = Logger.getLogger(WorkReport.class.getName());

    String prepareReport(final Boolean isForYesterday, final String originalReportText) {
        final String reportHeader = prepareReportHeader(isForYesterday);
        final String listOfTasks = prepareListOfTasks(originalReportText);

        final String updatedReportText = reportHeader + "\n\n" + listOfTasks;

        return updatedReportText;
    }

    /**
     * Prepare report header with date and working hours.
     *
     * @param isForYesterday
     * @return
     */
    String prepareReportHeader(final Boolean isForYesterday) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH);

        String reportDate = today.format(formatter);
        if(isForYesterday) {
            reportDate = yesterday.format(formatter);
        }

        final String reportWorkingHours = "Working time: 8 hours";

        final String reportHeader = reportDate + "\n" + reportWorkingHours;

        return reportHeader;
    }

    /**
     * Convert text copied from Todoist application to a list of tasks.
     *
     * @param originalReportText
     * @return
     */
    String prepareListOfTasks(final String originalReportText) {
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

    String fetchClipboardText() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);

            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (IllegalStateException | HeadlessException | java.io.IOException | UnsupportedFlavorException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return "";
    }

    /**
     * Make sure that clipboard has text related to report. It should be copied
     * from Todoist application.
     *
     * @param originalReportText
     * @return
     */
    Boolean validateTextInClipboard(final String originalReportText) {
        if (originalReportText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There is nothing in clipboard! Copy original text to convert it to a report.", "Warning", JOptionPane.WARNING_MESSAGE);
            return Boolean.FALSE;
        }
        if (!originalReportText.contains("You completed a task:")) {
            JOptionPane.showMessageDialog(null, "There is not related text in clipboard to a report!", "Warning", JOptionPane.WARNING_MESSAGE);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    void saveReportToClipboard(final String updatedReport) {
        if (updatedReport == null || updatedReport.isEmpty()) {
            return;
        }

        try {
            StringSelection selection = new StringSelection(updatedReport);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        } catch (IllegalStateException | HeadlessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
