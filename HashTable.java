import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class Frame extends JFrame implements ActionListener {
    private final JTextArea tableSize = new JTextArea(1, 10);
    private final JTextArea values = new JTextArea(1, 20);
    private final JTextArea hxFormula = new JTextArea(1, 30);
    private final JButton hashify = new JButton("Hashify!");

    public Frame() {
        super("Hashify!");
        this.setLayout(new FlowLayout());
        this.setVisible(true);
        this.setResizable(false);

        JLabel inputTableSized = new JLabel("Input HashTable size (mod value): ");
        add(inputTableSized);
        add(tableSize);

        JLabel warning = new JLabel("Separate each value with comma and leave a white space -> 111, 222, 333", SwingConstants.CENTER);
        add(warning);

        JLabel inputValueList = new JLabel("Input values into HashTable according to question: ");
        add(inputValueList);
        add(values);

        JLabel warning2 = new JLabel("Separate each value with comma and leave a white space -> 2, x, -1, mod5, +1", SwingConstants.CENTER);
        add(warning2);
        JLabel inputHX = new JLabel("Input h(x') formula (2nd Hash function) in: ");
        add(inputHX);
        add(hxFormula);

        JLabel info = new JLabel("                   h(x) = value mod HashTableSize                           ", SwingConstants.CENTER);
        add(info);
        JLabel info2 = new JLabel("H(x) = (h(x) + i * h'(x)) mod HashTableSize, where i = 0, 1, 2, 3, ...     ", SwingConstants.CENTER);
        add(info2);
        add(hashify);
        hashify.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) throws NumberFormatException {
        try {
            StringBuilder sb = new StringBuilder();
            ArrayList<Integer> valuesList = new ArrayList<>();
            ArrayList<Integer> QuadProbList = new ArrayList<>();
            ArrayList<Integer> doubleHashList = new ArrayList<>();
            String[] hxValue = values.getText().split(", ");

            int modValue = Integer.parseInt(tableSize.getText());
            for (String s : hxValue) valuesList.add(Integer.parseInt(s));
            sb.append("Values: ").append(valuesList).append("\n").append("h(x): ").append("      [");
            valuesList.forEach(value -> sb.append(value % modValue).append(", "));
            sb.setLength(sb.length() - 2);
            sb.append("]\n*********************************************************************\n");

            if (hxFormula.getText().length() == 0) {
                sb.append("Quadratic Probing\n");
                sb.append("==================================================\n");
                valuesList.forEach(number -> {
                    int value = number % modValue;
                    if (!QuadProbList.isEmpty()) {
                        boolean gg = QuadProbList.contains(value);
                        int multiplier = 1;
                        while (gg) {
                            sb.append(number).append(" collided with ").append(number % modValue).append(". Quadratic Probing value: ").append(multiplier).append(" ^2\n");
                            value = (int) (number + Math.pow(multiplier, 2)) % modValue;
                            multiplier++;
                            gg = QuadProbList.contains(value);
                            if (multiplier >= 10) {
                                sb.append("WARNING!!! Unable to solve using quadratic probing. Endless loop WARNING!!!\n");
                                break;
                            }
                        }
                    }
                    QuadProbList.add(value);
                    sb.append(number).append(" is in Array Index: ").append(value).append("\n");
                });
                sb.append("==================================================\n");
                sb.append("Quad probing final answer: ").append(QuadProbList);
                sb.append("\n==================================================\n");
            } else {
                sb.append("Double Hashing\n").append("==================================================\n");
                valuesList.forEach(number -> {
                    int value = number % modValue;
                    int valuePrime = 0;
                    int valueMega = 0;
                    int valueMultiplier = 0;
                    if (doubleHashList.isEmpty()) {
                        doubleHashList.add(value);
                    } else if (doubleHashList.contains(value)) {
                        String[] formula = hxFormula.getText().split(", ");
                        int parser = 0;
                        int multiplication = 0;
                        for (String s : formula) {
                            try {
                                parser = Integer.parseInt(s);
                            } catch (NumberFormatException nfe) {
                                if (s.contains("x")) {
                                    multiplication = parser * number;
                                } else if (s.contains("-")) {
                                    multiplication -= Integer.parseInt(s.substring(1));
                                } else if (s.contains("+")) {
                                    multiplication += Integer.parseInt(s.substring(1));
                                } else if (s.contains("mod")) {
                                    multiplication %= Integer.parseInt(s.substring(3));
                                }
                            }
                            valuePrime = multiplication;
                        }
                        sb.append("h'(x): ").append("of ").append(number).append(": ").append(valuePrime).append("\n");
                        sb.append("*********************************************************************\n");

                        boolean fk = doubleHashList.contains(valuePrime);
                        while (fk) {
                            valueMega = (value + valueMultiplier * valuePrime) % modValue;
                            valueMultiplier++;
                            fk = doubleHashList.contains(valueMega);
                            sb.append("H(x): ").append("of ").append(number).append(": ").append(valueMega).append(", where i is ").append(valueMultiplier - 1).append("\n");
                            sb.append("*********************************************************************\n");
                        }
                        if (!doubleHashList.contains(valuePrime)) {
                            doubleHashList.add(valuePrime);
                        } else if (!doubleHashList.contains(valueMega)) {
                            doubleHashList.add(valueMega);
                        }
                    } else if (!doubleHashList.contains(value)) {
                        doubleHashList.add(value);
                    }

                });
                sb.append("==================================================\n");
                sb.append("Final answer using double hashing is: \n");
                sb.append(valuesList).append("\n").append(doubleHashList);
                sb.append("\n").append("==================================================\n");
            }

            if (e.getSource() == hashify)
                JOptionPane.showMessageDialog(null, sb, "Hashify!", JOptionPane.PLAIN_MESSAGE);
        } catch (NumberFormatException err) {
            err.printStackTrace();
        }
    }
}

/**
 * Values to test
 */
// 7254, 8312, 7310, 7809
// 237, 428, 261, 374, 783, 346, 409
// 29, 40, 51, 16, 22, 34, 49
// 2, x, -1, mod5, +1
public class HashTable {
    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.setSize(500, 250);
        frame.setLocationRelativeTo(null);
    }
}
