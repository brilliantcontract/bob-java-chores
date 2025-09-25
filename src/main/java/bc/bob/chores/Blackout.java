package bc.bob.chores;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

class Blackout {

    private static final Logger LOG = Logger.getLogger(Blackout.class.getName());

    boolean serverBlackout() {
        final String host = "server.loc";
        final int port = 22;
        final String username = "root";
        final String password = "RootSecret!";
        final String remoteCommand = "blackout";

        return executeCommandOverSsh(host, port, username, password, remoteCommand);
    }

    /**
     * Shutdown all computers.
     *
     * @return
     */
    boolean gloablBlackout() {
        final String host = "bastion.loc";
        final int port = 22;
        final String username = "root";
        final String password = "RootSecret!";
        final String remoteCommand = "blackout";

        return executeCommandOverSsh(host, port, username, password, remoteCommand);
    }

    boolean shoutdownNas() {
        final String host = "nas.loc";
        final int port = 22;
        final String username = "root";
        final String password = "RootSecret!";
        final String remoteCommand = "shutdown now";

        return executeCommandOverSsh(host, port, username, password, remoteCommand);
    }

    private boolean executeCommandOverSsh(
            final String host,
            final int port,
            final String username,
            final String password,
            final String remoteCommand
    ) {
        Session session = null;
        ChannelExec channel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.connect(10_000);
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(remoteCommand);

            // We won’t send input to the command
            channel.setInputStream(null);

            // (Optional) capture output/errors just in case
            InputStream stdout = channel.getInputStream();
            InputStream stderr = channel.getErrStream();

            channel.connect(5_000);

            // Wait for remote server execute command.
            long start = System.currentTimeMillis();
            while (!channel.isClosed() && (System.currentTimeMillis() - start) < 5_000) {
                Thread.sleep(100);
            }

            int status = channel.getExitStatus(); // -1 if unknown/not set
            return status == 0 || status == -1;
        } catch (JSchException | IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, "Cannot execute remote command over SSH.", ex);
            return false;
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}
