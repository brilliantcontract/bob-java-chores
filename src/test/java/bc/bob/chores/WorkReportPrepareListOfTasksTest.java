package bc.bob.chores;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WorkReportPrepareListOfTasksTest {

    @Test
    public void prepareReportExtractsCompletedTasks() {
        // Initialization.
        final WorkReport workReport = new WorkReport();
        final String originalReportText = "Activity:\n"
                + "Sep 24 ‧ Yesterday ‧ Wednesday\n\n"
                + "    Bob\n\n"
                + "You completed a task:\n"
                + "Assign task\n"
                + "11:47 PM\n"
                + "Data\n"
                + "Bob\n"
                + "You completed a task:\n"
                + "Start discussion with Dragos\n"
                + "11:37 PM\n"
                + "MRO\n"
                + "Bob\n"
                + "You completed a task:\n"
                + "Assign task to Vaibhav\n"
                + "11:33 PM\n"
                + "Competitors\n"
                + "Bob\n"
                + "You completed a task:\n"
                + "Assign task to Vaibhav\n"
                + "11:33 PM\n"
                + "Competitors\n"
                + "Bob\n"
                + "You completed a task:\n"
                + "Send report to Anthony\n\n";
        final String expectedReport = "- Assign task\n"
                + "- Start discussion with Dragos\n"
                + "- Assign task to Vaibhav\n"
                + "- Assign task to Vaibhav\n"
                + "- Send report to Anthony";

        // Execution.
        final String updatedReport = workReport.prepareListOfTasks(originalReportText);

        // Assertion.
        assertThat(updatedReport, is(expectedReport));
    }
}
