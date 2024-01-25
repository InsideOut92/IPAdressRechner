import javax.swing.*;

public class IPAddressCalculatorFrame extends JFrame{

    private IPAddressCalculatorPanel calculatorPanel;

    // Konstruktor für das Hauptfenster
    public IPAddressCalculatorFrame(){
        // Fenstertitel und Größe festlegen
        setTitle("Patrick's IP Addressen Rechner");
        setSize(400, 200);
        // Standardverhalten beim Schließen des Fensters setzen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Instanz der IPAddressCalculatorPanel-Klasse erstellen
        calculatorPanel = new IPAddressCalculatorPanel();
        // IPAddressCalculatorPanel zum Hauptfenster hinzufügen
        add(calculatorPanel);

        // Das Layout und die Größe des Fensters an die enthaltenen Komponenten anpassen
        pack();
        // Das Fenster in die Mitte des Bildschirms positionieren
        setLocationRelativeTo(null);
    }
}
