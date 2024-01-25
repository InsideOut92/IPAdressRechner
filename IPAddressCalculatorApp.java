import javax.swing.*;

public class IPAddressCalculatorApp{
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            IPAddressCalculatorFrame calculatorFrame = new IPAddressCalculatorFrame();
            calculatorFrame.setVisible(true);
        });
    }
}
