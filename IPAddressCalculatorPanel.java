import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPAddressCalculatorPanel extends JPanel{

    private JTextField ipAddressField, networkAddressField, broadcastAddressField, gatewayAddressField;
    private JComboBox<Integer> subnetMaskComboBox;
    private JButton calculateButton, resetButton;

    public IPAddressCalculatorPanel(){
        JLabel ipAddressLabel = new JLabel("IP-Addresse:");
        JLabel subnetMaskLabel = new JLabel("Subnetz-Maske:");

        ipAddressField = new JTextField(15);
        // Subnetzmaskenoptionen als Integer
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

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(ipAddressLabel)
                        .addComponent(subnetMaskLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(ipAddressField)
                        .addComponent(subnetMaskComboBox)
                        .addComponent(calculateButton)
                        .addComponent(resetButton))
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
                .addComponent(resetButton));
    }

    private void calculateAddresses(){
        try {
            String ipAddress = ipAddressField.getText();
            int subnetMask = (int) subnetMaskComboBox.getSelectedItem();

            if (!ipAddress.isEmpty()){
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                InetAddress subnetAddress = calculateSubnetMask(subnetMask);

                InetAddress networkAddress = calculateNetworkAddress(inetAddress, subnetAddress);
                InetAddress broadcastAddress = calculateBroadcastAddress(inetAddress, subnetAddress);
                InetAddress gatewayAddress = calculateGatewayAddress(networkAddress);

                networkAddressField.setText(networkAddress.getHostAddress());
                broadcastAddressField.setText(broadcastAddress.getHostAddress());
                gatewayAddressField.setText(gatewayAddress.getHostAddress());

            } else{
                JOptionPane.showMessageDialog(this, "IP-Addresse wird benötigt", "Error 404", JOptionPane.ERROR_MESSAGE);
            }
        } catch(UnknownHostException e){
            JOptionPane.showMessageDialog(this, "Ungültige IP-Addresse oder Subnetz-Maske", "Error 404", JOptionPane.ERROR_MESSAGE);
        }
    }

    private InetAddress calculateSubnetMask(int subnetMask) throws UnknownHostException{
        byte[] subnetBytes = new byte[4];

        for(int i = 0; i < subnetMask; i++){
            subnetBytes[i / 8] |= (1 << (7 - (i % 8)));
        }

        return InetAddress.getByAddress(subnetBytes);
    }

    private InetAddress calculateNetworkAddress(InetAddress ipAddress,
                                                InetAddress subnetMask) throws UnknownHostException{
        byte[] ipBytes = ipAddress.getAddress();
        byte[] subnetBytes = subnetMask.getAddress();

        for (int i = 0; i < ipBytes.length; i++) {
            ipBytes[i] = (byte) (ipBytes[i] & subnetBytes[i]);
        }

        return InetAddress.getByAddress(ipBytes);
    }

    private InetAddress calculateBroadcastAddress(InetAddress ipAddress,
                                                  InetAddress subnetMask) throws UnknownHostException{
        byte[] ipBytes = ipAddress.getAddress();
        byte[] subnetBytes = subnetMask.getAddress();

        for (int i = 0; i < ipBytes.length; i++){
            ipBytes[i] = (byte) (ipBytes[i] | ~subnetBytes[i]);
        }

        return InetAddress.getByAddress(ipBytes);
    }

    private InetAddress calculateGatewayAddress(InetAddress networkAddress) throws UnknownHostException{
        byte[] ipBytes = networkAddress.getAddress();
        
        ipBytes[ipBytes.length - 1]++; 

        return InetAddress.getByAddress(ipBytes);
    }

    private void resetFields(){
        ipAddressField.setText("");
        networkAddressField.setText("");
        broadcastAddressField.setText("");
        gatewayAddressField.setText("");
        subnetMaskComboBox.setSelectedIndex(0);
    }
}
