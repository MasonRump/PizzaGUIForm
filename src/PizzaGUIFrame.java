import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PizzaGUIFrame extends JFrame implements ActionListener {

    //Crust buttons
    private JRadioButton thinRB, regularRB, deepDishRB;
    private ButtonGroup crustGroup;

    //Size box
    private JComboBox<String> sizeCombo;

    //Toppings checkboxes
    private JCheckBox pepperoniCB, sausageCB, baconCB, mushroomsCB, pineappleCB, olivesCB;

    //Reciept area
    private JTextArea receiptArea;

    //Buttons
    private JButton orderBtn, clearBtn, quitBtn;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildOptionsPanel(), BorderLayout.CENTER);
        add(buildReceiptPanel(), BorderLayout.SOUTH);
        add(buildButtonPanel(), BorderLayout.PAGE_END);

        setVisible(true);
    }

    private JPanel buildOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.setBorder(new TitledBorder("Crust Type"));

        thinRB = new JRadioButton("Thin");
        regularRB = new JRadioButton("Regular");
        deepDishRB = new JRadioButton("Deep Dish");

        crustGroup = new ButtonGroup();
        crustGroup.add(thinRB);
        crustGroup.add(regularRB);
        crustGroup.add(deepDishRB);

        panel.add(thinRB);
        panel.add(regularRB);
        panel.add(deepDishRB);

        return panel;
    }

    private JPanel buildSizePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Pizza Size"));

        String[] sizes = {"Small - $8", "Medium - $12", "Large - $16, Super - $20"};
        sizeCombo = new JComboBox<>(sizes);
        sizeCombo.setSelectedIndex(-1);

        panel.add(sizeCombo);
        return panel;
    }

    private JPanel buildToppingsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(new TitledBorder("Toppings ($1 each)"));

        pepperoniCB = new JCheckBox("Pepperoni");
        sausageCB = new JCheckBox("Sausage");
        baconCB = new JCheckBox("Bacon");
        mushroomsCB = new JCheckBox("Mushrooms");
        pineappleCB = new JCheckBox("Pineapple");
        olivesCB = new JCheckBox("Olives");

        panel.add(pepperoniCB);
        panel.add(sausageCB);
        panel.add(baconCB);
        panel.add(mushroomsCB);
        panel.add(pineappleCB);
        panel.add(olivesCB);

        return panel;
    }

    private JPanel buildReceiptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("OrderReceipt"));

        receiptArea = new JTextArea(10, 60);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(receiptArea);
        panel.add(scrollPane);

        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();

        orderBtn = new JButton("Order");
        clearBtn = new JButton("Clear");
        quitBtn = new JButton("Quit");

        orderBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        quitBtn.addActionListener(this);

        panel.add(orderBtn);
        panel.add(clearBtn);
        panel.add(quitBtn);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if  (e.getSource() == quitBtn) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to quit?",
                    "Confirm Quit",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }

        if  (e.getSource() == clearBtn) {
            crustGroup.clearSelection();
            sizeCombo.setSelectedIndex(-1);
            pepperoniCB.setSelected(false);
            sausageCB.setSelected(false);
            baconCB.setSelected(false);
            mushroomsCB.setSelected(false);
            pineappleCB.setSelected(false);
            olivesCB.setSelected(false);
            receiptArea.setText("");
        }

        if  (e.getSource() == orderBtn) {
            processOrder();
        }
    }

    private void processOrder() {
        DecimalFormat df = new DecimalFormat("0.00");

        if (!thinRB.isSelected() && !regularRB.isSelected() && !deepDishRB.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a crust type.");
            return;
        }

        if (sizeCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a pizza size.");
            return;
        }

        ArrayList<String> toppings = new ArrayList<>();
        if (pepperoniCB.isSelected()) toppings.add("Pepperoni");
        if (sausageCB.isSelected()) toppings.add("Sausage");
        if (baconCB.isSelected()) toppings.add("Bacon");
        if (mushroomsCB.isSelected()) toppings.add("Mushrooms");
        if (pineappleCB.isSelected()) toppings.add("Pineapple");
        if (olivesCB.isSelected()) toppings.add("Olives");

        if (toppings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one topping.");
            return;
        }

        double basePrice = switch (sizeCombo.getSelectedIndex()) {
            case 0 -> 8.00;
            case 1 -> 12.00;
            case 2 -> 16.00;
            case 3 -> 20.00;
            default -> 0.00;
        };

        double toppingsCost = toppings.size();
        double subTotal = basePrice + toppingsCost;
        double tax = subTotal * 0.07;
        double total = subTotal + tax;

        String crust =
                thinRB.isSelected() ? "Thin" :
                regularRB.isSelected() ? "Regular" : "Deep Dish";

        StringBuilder receipt = new StringBuilder();
        receipt.append("===================================================\n");
        receipt.append("                 Pizza Order Receipt                \n");
        receipt.append("===================================================\n");
        receipt.append(String.format("Crust & Size\t\tPrice\n"));
        receipt.append(crust).append(" / ").append(sizeCombo.getSelectedItem()).append("\t$")
                .append(df.format(basePrice)).append("\n\n");

        receipt.append("Toppings\t\t\tPrice\n");
        for (String topping : toppings) {
            receipt.append(topping).append("\t\t\t$1.00\n");
        }

        receipt.append("\nSubtotal \t\t\t$").append(df.format(subTotal));
        receipt.append("\nTax (7%) \t\t\t$").append(df.format(tax))
                .append("\n---------------------------------------------------");
        receipt.append("\nTotal \t\t\t\t$").append(df.format(total))
            .append("\n===================================================\n");

        receiptArea.setText(receipt.toString());
    }

}
