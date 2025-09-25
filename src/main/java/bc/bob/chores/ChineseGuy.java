package bc.bob.chores;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class ChineseGuy {

    private static final Logger LOG = Logger.getLogger(ChineseGuy.class.getName());

    void emulateScrolling() {
        try {
            AdvRobot advRobot = new AdvRobot();
            while (true) {
                int ms = Utils.generateRandomInteger(200, 1500);
                Utils.sleep(ms);
                switch (Utils.generateRandomInteger(1, 4)) {
                    case 1:
                        advRobot.type(KeyEvent.VK_DOWN);
                        Utils.sleep(ms);
                        advRobot.moveMouseCursorToArbitraryLocation();
                        break;

                    case 2:
                        advRobot.type(KeyEvent.VK_UP);
                        Utils.sleep(ms);
                        advRobot.type(KeyEvent.VK_DOWN);
                        break;

                    case 3:
                        advRobot.type(KeyEvent.VK_LEFT);
                        Utils.sleep(ms);
                        advRobot.type(KeyEvent.VK_RIGHT);
                        break;

                    case 4:
                        advRobot.type(KeyEvent.VK_RIGHT);
                        Utils.sleep(ms);
                        advRobot.type(KeyEvent.VK_LEFT);
                        break;
                }
            }
        } catch (InterruptedException | AWTException ex) {
            LOG.log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    void emulateTabbing() {
        try {
            AdvRobot advRobot = new AdvRobot();
            while (true) {
                int ms = Utils.generateRandomInteger(200, 1500);
                Utils.sleep(ms);
                switch (Utils.generateRandomInteger(1, 8)) {
                    case 1:
                        advRobot.type(KeyEvent.VK_DOWN);
                        Utils.sleep(ms);
                        advRobot.moveMouseCursorToArbitraryLocation();
                        break;

                    case 2:
                        advRobot.type(KeyEvent.VK_UP);
                        Utils.sleep(ms);
                        advRobot.type(KeyEvent.VK_DOWN);
                        break;

                    case 3:
                        advRobot.type(KeyEvent.VK_LEFT);
                        Utils.sleep(ms);
                        advRobot.type(KeyEvent.VK_RIGHT);
                        break;

                    case 4:
                        advRobot.type(KeyEvent.VK_RIGHT);
                        Utils.sleep(ms);
                        advRobot.type(KeyEvent.VK_LEFT);
                        break;

                    case 5:
                        advRobot.hotkey(KeyEvent.VK_ALT, KeyEvent.VK_TAB);
                        break;

                    case 6:
                        advRobot.hotkey(KeyEvent.VK_ALT, KeyEvent.VK_TAB, KeyEvent.VK_TAB);
                        break;

                    case 7:
                        advRobot.hotkey(KeyEvent.VK_ALT, KeyEvent.VK_TAB, KeyEvent.VK_TAB, KeyEvent.VK_TAB);
                        break;

                    case 8:
                        advRobot.hotkey(KeyEvent.VK_ALT, KeyEvent.VK_TAB, KeyEvent.VK_TAB, KeyEvent.VK_TAB, KeyEvent.VK_TAB);
                        break;
                }
            }
        } catch (InterruptedException | AWTException ex) {
            LOG.log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    private static class Utils {

        static void sleep(int ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, "Sleep method failed.", ex);
            }
        }

        static int generateRandomInteger(int min, int max) {
            int randomNumber = (int) (Math.random() * (max - min + 1) + min);
            return randomNumber;
        }
    }

    private class AdvRobot {

        private final Robot robot;
        private final Random random = new Random();
        private final Integer eventsRate = 1;

        public AdvRobot() throws AWTException {
            this.robot = new Robot();
        }

        public AdvRobot(Robot robot) {
            this.robot = robot;
        }

        public void moveMouseCursorToArbitraryLocation() {
            robot.mouseMove(
                    generateArbitraryMouseXPosition(),
                    generateArbitraryMouseYPosition()
            );
        }

        public int generateArbitraryMouseXPosition() {
            final Integer MAX_X = Toolkit.getDefaultToolkit().getScreenSize().width;
            return ThreadLocalRandom.current().nextInt(200, MAX_X - 200);
        }

        public int generateArbitraryMouseYPosition() {
            final Integer MAX_Y = Toolkit.getDefaultToolkit().getScreenSize().width;
            return ThreadLocalRandom.current().nextInt(200, MAX_Y - 200);
        }

        public void beep(int hz, int msec, double volume) throws LineUnavailableException {
            float SAMPLE_RATE = 8000f;

            byte[] buf = new byte[1];
            AudioFormat af
                    = new AudioFormat(
                            SAMPLE_RATE, // sampleRate
                            8, // sampleSizeInBits
                            1, // channels
                            true, // signed
                            false);      // bigEndian
            try (SourceDataLine sdl = AudioSystem.getSourceDataLine(af)) {
                sdl.open(af);
                sdl.start();
                for (int i = 0; i < msec * 8; i++) {
                    double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
                    buf[0] = (byte) (Math.sin(angle) * 127.0 * volume);
                    sdl.write(buf, 0, 1);
                }
                sdl.drain();
                sdl.stop();
            }
        }

        public void type(int... keys) throws InterruptedException {
            for (int key : keys) {
                robot.keyPress(key);
                Thread.sleep(random.nextInt(60) + 10);
                robot.keyRelease(key);
                Thread.sleep(random.nextInt(60) + 10);
            }
        }

        public void pressMouseButton(int key) throws InterruptedException {
            robot.mousePress(key);
            Thread.sleep(random.nextInt(60) + 10);
            robot.mouseRelease(key);
            Thread.sleep(random.nextInt(60) + 10);
        }

        public void hotkey(int... keys) throws InterruptedException {
            for (int key : keys) {
                robot.keyPress(key);
                Thread.sleep(random.nextInt(60) + 10);
            }

            for (int key : keys) {
                robot.keyRelease(key);
                Thread.sleep(random.nextInt(60) + 10);
            }
        }

        public void typeRandomAlpahaNumericCharacter() throws InterruptedException {
            switch (random.nextInt(36)) {
                case 0:
                    type(KeyEvent.VK_0);
                    break;

                case 1:
                    type(KeyEvent.VK_1);
                    break;

                case 2:
                    type(KeyEvent.VK_2);
                    break;

                case 3:
                    type(KeyEvent.VK_3);
                    break;

                case 4:
                    type(KeyEvent.VK_4);
                    break;

                case 5:
                    type(KeyEvent.VK_5);
                    break;

                case 6:
                    type(KeyEvent.VK_6);
                    break;

                case 7:
                    type(KeyEvent.VK_7);
                    break;

                case 8:
                    type(KeyEvent.VK_8);
                    break;

                case 9:
                    type(KeyEvent.VK_9);
                    break;

                case 10:
                    type(KeyEvent.VK_A);
                    break;

                case 11:
                    type(KeyEvent.VK_B);
                    break;

                case 12:
                    type(KeyEvent.VK_C);
                    break;

                case 13:
                    type(KeyEvent.VK_D);
                    break;

                case 14:
                    type(KeyEvent.VK_E);
                    break;

                case 15:
                    type(KeyEvent.VK_F);
                    break;

                case 16:
                    type(KeyEvent.VK_G);
                    break;

                case 17:
                    type(KeyEvent.VK_H);
                    break;

                case 18:
                    type(KeyEvent.VK_I);
                    break;

                case 19:
                    type(KeyEvent.VK_J);
                    break;

                case 20:
                    type(KeyEvent.VK_K);
                    break;

                case 21:
                    type(KeyEvent.VK_L);
                    break;

                case 22:
                    type(KeyEvent.VK_M);
                    break;

                case 23:
                    type(KeyEvent.VK_N);
                    break;

                case 24:
                    type(KeyEvent.VK_O);
                    break;

                case 25:
                    type(KeyEvent.VK_P);
                    break;

                case 26:
                    type(KeyEvent.VK_Q);
                    break;

                case 27:
                    type(KeyEvent.VK_R);
                    break;

                case 28:
                    type(KeyEvent.VK_S);
                    break;

                case 29:
                    type(KeyEvent.VK_T);
                    break;

                case 30:
                    type(KeyEvent.VK_U);
                    break;

                case 31:
                    type(KeyEvent.VK_V);
                    break;

                case 32:
                    type(KeyEvent.VK_W);
                    break;

                case 33:
                    type(KeyEvent.VK_X);
                    break;

                case 34:
                    type(KeyEvent.VK_Y);
                    break;

                case 35:
                    type(KeyEvent.VK_Z);
                    break;
            }
        }
    }

}
