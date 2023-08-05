import javax.smartcardio.*;
import java.util.List;

public class CardReader {
    public static void main(String[] args) {
        try {
            // Obtain a TerminalFactory
            TerminalFactory factory = TerminalFactory.getDefault();

            // Get the list of available readers
            List<CardTerminal> terminals = factory.terminals().list();
            System.out.println("Terminals: " + terminals);

            // Use the first available terminal
            CardTerminal terminal = terminals.get(0);

            // Wait for a card to be presented
            System.out.println("Waiting for a card..");
            terminal.waitForCardPresent(0);

            // Connect to the card using T=1 protocol (commonly used)
            Card card = terminal.connect("T=1");

            // Obtain the CardChannel to send APDU commands
            CardChannel channel = card.getBasicChannel();

            // Command APDU for selecting the master file (commonly '3F00')
            // CommandAPDU command = new CommandAPDU(new byte[] { (byte) 0x00, (byte) 0xA4,
            // (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x3F, (byte) 0x00 });

            byte[] apdu = { (byte) 0x00, (byte) 0xA4, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x3F, (byte) 0x00 };
            CommandAPDU command = new CommandAPDU(apdu);

            ResponseAPDU response = channel.transmit(command);
            System.out.println("Response: " + response);

            // Get and print the status word
            int statusWord = response.getSW();
            System.out.println("Status Word: " + Integer.toHexString(statusWord));

            // Convert response data bytes to hexadecimal and print
            byte[] responseData = response.getData();
            StringBuilder sb = new StringBuilder();
            for (byte b : responseData) {
                sb.append(String.format("%02X ", b));
            }
            System.out.println("Response Data: " + sb.toString());

            // Disconnect the card
            card.disconnect(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
