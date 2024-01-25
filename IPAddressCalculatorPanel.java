import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

// Diese Klasse steht für das Hauptpanel der GUI-Anwendung
public class IPAddressCalculatorPanel extends JPanel{

    private JTextField ipAddressField, networkAddressField, broadcastAddressField, gatewayAddressField;
    private JComboBox<Integer> subnetMaskComboBox;
    private JButton calculateButton, resetButton, clearAllButton;  // Clear All Button hinzufügen
    private JTextArea resultTextArea;

    // Konstruktor für die GUI
    public IPAddressCalculatorPanel(){
        // GUI-Komponenten erstellen
        JLabel ipAddressLabel = new JLabel("IP-Adresse:");
        JLabel subnetMaskLabel = new JLabel("Subnetz-Maske:");

        ipAddressField = new JTextField(15);
        subnetMaskComboBox = new JComboBox<>(new Integer[]{24, 25, 26, 27, 28});
        networkAddressField = new JTextField(15);
        broadcastAddressField = new JTextField(15);
        gatewayAddressField = new JTextField(15);

        calculateButton = new JButton("Berechnen");
        calculateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                calculateAddresses();
            }
        });

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                resetFields();
            }
        });

        clearAllButton = new JButton("Clear All");  // Clear All Button erstellen
        clearAllButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                clearAllFields();
            }
        });

        // Textarea für die Anzeige von Ergebnissen
        resultTextArea = new JTextArea(10, 20);
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);

        // Scrollpane für die Textarea
        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        // Layout-Manager setzen
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // GUI-Komponenten im Layout anordnen
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(ipAddressLabel)
                        .addComponent(scrollPane)
                        .addComponent(subnetMaskLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(ipAddressField)
                        .addComponent(subnetMaskComboBox)
                        .addComponent(calculateButton)
                        .addComponent(resetButton)
                        .addComponent(clearAllButton))  // Clear All Button zum Layout hinzufügen
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(networkAddressField)
                        .addComponent(broadcastAddressField)
                        .addComponent(gatewayAddressField)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(ipAddressLabel)
                        .addComponent(ipAddressField)
                        .addComponent(networkAddressField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(subnetMaskLabel)
                        .addComponent(subnetMaskComboBox)
                        .addComponent(broadcastAddressField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(calculateButton)
                        .addComponent(gatewayAddressField))
                .addComponent(resetButton)
                .addComponent(clearAllButton)
                .addComponent(scrollPane));
    }

    // Methode zur Berechnung der IP-Adressen
    private void calculateAddresses(){
        try{
            // IP-Adresse und Subnetzmaske aus den GUI-Elementen abrufen
            String ipAddress = ipAddressField.getText();
            int subnetMask = (int) subnetMaskComboBox.getSelectedItem();

            // Überprüfen, ob die eingegebene IP-Adresse gültig ist
            if(isValidIPAddress(ipAddress)){
                // IP-Adresse und Subnetzmaske in Objekte konvertieren
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                InetAddress subnetAddress = calculateSubnetMask(subnetMask);

                // Netzwerk-, Broadcast- und Gateway-Adressen berechnen
                InetAddress networkAddress = calculateNetworkAddress(inetAddress, subnetAddress);
                InetAddress broadcastAddress = calculateBroadcastAddress(inetAddress, subnetAddress);
                InetAddress gatewayAddress = calculateGatewayAddress(networkAddress);

                // Ergebnisse in GUI-Elemente eintragen
                networkAddressField.setText(networkAddress.getHostAddress());
                broadcastAddressField.setText(broadcastAddress.getHostAddress());
                gatewayAddressField.setText(gatewayAddress.getHostAddress());

                // Mehrere IP-Adressen überprüfen und Meldung ausgeben
                String message = "Berechnung abgeschlossen für:\n";
                message += "IP-Adresse: " + ipAddress + "\n";
                message += "Subnetzmaske: /" + subnetMask + "\n";
                message += "Netzwerkadresse: " + networkAddress.getHostAddress() + "\n";
                message += "Broadcast-Adresse: " + broadcastAddress.getHostAddress() + "\n";
                message += "Gateway-Adresse: " + gatewayAddress.getHostAddress() + "\n\n";

                // Vorhandenen Text in der Textarea beibehalten
                resultTextArea.append(message);
            } else {
                // Fehlermeldung anzeigen, wenn die IP-Adresse ungültig ist
                JOptionPane.showMessageDialog(this, "Ungültiges IP-Adressenformat", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch(UnknownHostException e){
            // Fehlermeldung anzeigen, wenn es Probleme mit der IP-Adresse oder Subnetzmaske gibt
            JOptionPane.showMessageDialog(this, "Ungültige IP-Adresse oder Subnetz-Maske", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Methode zur Überprüfung des IP-Adressenformats mit Regex
    private boolean isValidIPAddress(String ipAddress){
        // Regex für einfache Überprüfung einer IPv4-Adresse
        String ipPattern = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";
        return ipAddress.matches(ipPattern);
    }

    // Methode zur Berechnung der Subnetzmaske
    private InetAddress calculateSubnetMask(int subnetMask) throws UnknownHostException{
        byte[] subnetBytes = new byte[4];

        // Setzt die entsprechende Anzahl von Bits in der Subnetzmaske auf 1
        for(int i = 0; i < subnetMask; i++){
            subnetBytes[i / 8] |= (1 << (7 - (i % 8)));
        }

        return InetAddress.getByAddress(subnetBytes);
    }

    // Methode zur Berechnung der Netzwerkadresse
    private InetAddress calculateNetworkAddress(InetAddress ipAddress,
                                                InetAddress subnetMask) throws UnknownHostException {
        byte[] ipBytes = ipAddress.getAddress();
        byte[] subnetBytes = subnetMask.getAddress();

        for(int i = 0; i < ipBytes.length; i++){
            ipBytes[i] = (byte) (ipBytes[i] & subnetBytes[i]);
        }

        return InetAddress.getByAddress(ipBytes);
    }

    // Methode zur Berechnung der Broadcast-Adresse
    private InetAddress calculateBroadcastAddress(InetAddress ipAddress,
                                                  InetAddress subnetMask) throws UnknownHostException {
        byte[] ipBytes = ipAddress.getAddress();
        byte[] subnetBytes = subnetMask.getAddress();

        for(int i = 0; i < ipBytes.length; i++){
            ipBytes[i] = (byte) (ipBytes[i] | ~subnetBytes[i]);
        }

        return InetAddress.getByAddress(ipBytes);
    }

    // Methode zur Berechnung der Gateway-Adresse
    private InetAddress calculateGatewayAddress(InetAddress networkAddress) throws UnknownHostException{
        byte[] ipBytes = networkAddress.getAddress();
        // Das letzte Byte inkrementieren, um die nächste gültige IP-Adresse zu erhalten
        ipBytes[ipBytes.length - 1]++;

        return InetAddress.getByAddress(ipBytes);
    }

    // Methode zum Zurücksetzen der GUI-Elemente
    private void resetFields(){
        ipAddressField.setText("");
        networkAddressField.setText("");
        broadcastAddressField.setText("");
        gatewayAddressField.setText("");
        subnetMaskComboBox.setSelectedIndex(0);
    }

    // Methode zum Löschen aller Ergebnisse
    private void clearAllFields(){
        resultTextArea.setText("");
    }
}
