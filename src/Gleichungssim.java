import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gleichungssim extends JFrame {
    private JTextField formulaField;
    private JTextArea outputField;
    private JButton calculateButton;
    private JComboBox<String> fileSelector;

    public Gleichungssim() {
        createUIComponents();
    }

    private void createUIComponents() {
        formulaField = new JTextField(20);
        calculateButton = new JButton("Calculate");
        outputField = new JTextArea(5, 32);
        outputField.setEditable(false);
        fileSelector = new JComboBox<>();
        fileSelector.addItem("standardInputs.txt");
        fileSelector.addItem("Add new...");
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        JMenuItem helpMenuItem = new JMenuItem("Help");

        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "This is a simulation program by Jonathan");
            }
        });

        helpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Choose an input file with the time steps and then input a formula using the following logical operators: \n" +
                        "and, or, not, nor, nand, xor, xnor. \n" +
                        "Example: Y = (E1 xor E2) or ((E4 nor E5) and E3)");
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String formula = formulaField.getText();
                    String selectedFile = (String) fileSelector.getSelectedItem();
                    Sim sim = new Sim(selectedFile, formula);
                    boolean[] output = sim.run();
                    outputField.setText("Y = " + sim);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        fileSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Add new...".equals(fileSelector.getSelectedItem())) {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
                        fileSelector.addItem(selectedFile);
                        fileSelector.setSelectedItem(selectedFile);
                    } else {
                        fileSelector.setSelectedItem("standardInputs.txt");
                    }
                }
            }
        });

        optionsMenu.add(aboutMenuItem);
        optionsMenu.add(helpMenuItem);

        menuBar.add(optionsMenu);

        setJMenuBar(menuBar);
        setLayout(new FlowLayout());
        add(fileSelector);
        add(formulaField);
        add(calculateButton);
        add(outputField);

        setSize(600, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Gleichungssim();
            }
        });
    }
}