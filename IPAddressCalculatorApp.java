import javax.swing.*;

public class IPAddressCalculatorApp{
    // Hauptmethode zum Starten der Anwendung
    public static void main(String[] args){
        // GUI-Komponenten in einem separaten Thread erstellen und anzeigen
        SwingUtilities.invokeLater(() -> {
            // Instanz der IPAddressCalculatorFrame-Klasse erstellen
            IPAddressCalculatorFrame calculatorFrame = new IPAddressCalculatorFrame();

            // Das Hauptfenster sichtbar machen
            calculatorFrame.setVisible(true);
        });
    }
}
