import javax.swing.*;

public class IPAddressCalculatorFrame extends JFrame{

    private IPAddressCalculatorPanel calculatorPanel;

    public IPAddressCalculatorFrame(){
        setTitle("Patrick's IP Addressen Verunstalter");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        calculatorPanel = new IPAddressCalculatorPanel();
        add(calculatorPanel);

        pack();
        setLocationRelativeTo(null);
    }
}
