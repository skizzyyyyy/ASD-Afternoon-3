package at.tugraz.asdafternoon3.ui;

import at.tugraz.asdafternoon3.FlatApplication;
import at.tugraz.asdafternoon3.businesslogic.FinanceFlatDAO;
import at.tugraz.asdafternoon3.businesslogic.FlatDAO;
import at.tugraz.asdafternoon3.data.FinanceFlat;
import at.tugraz.asdafternoon3.data.Flat;
import at.tugraz.asdafternoon3.database.DatabaseConnection;
import at.tugraz.asdafternoon3.ui.table.FinanceFlatTableModel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceFlatOverview {
    private JPanel contentPane;
    private JButton backButton;
    private JButton removeButton;
    private JButton addButton;
    private JSpinner spPrice;
    private JTable financeFlatTable;
    private JTextField tfTitle;
    private JLabel lTotalCosts;
    private JLabel lRoommateCosts;

    private final Flat activeFlat;
    private final FinanceFlatTableModel tableModel;

    private final List<FinanceFlat> financeItems;

    public FinanceFlatOverview(Flat flat) {
        this.activeFlat = flat;

        this.financeItems = new ArrayList<>();

        try {
            this.financeItems.addAll(DatabaseConnection.getInstance().createDao(FlatDAO.class).getFinanceFlat(flat));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(getContentPane(), "Finance items could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        tableModel = new FinanceFlatTableModel(financeItems);
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                calculateSum();
            }
        });
        financeFlatTable.setModel(tableModel);

        calculateSum();

        backButton.addActionListener(e -> {
            FlatApplication.get().setContentPane(new FlatOverview(activeFlat).getContentPane());
        });

        addButton.addActionListener(e -> {
            FinanceFlat financeFlat = createFinanceFlat();
            if (financeFlat != null) {
                tableModel.addFinanceFlat(financeFlat);
                financeItems.add(financeFlat);
                calculateSum();
            }
        });

        removeButton.addActionListener(e -> {
            int rowIndex = financeFlatTable.getSelectedRow();
            FinanceFlat financeFlat = tableModel.getFinanceFlatAtIndex(rowIndex);

            try {
                DatabaseConnection.getInstance().createDao(FinanceFlatDAO.class).delete(financeFlat);
                tableModel.removeFinanceFlat(rowIndex);
                financeItems.remove(financeFlat);
                System.out.println("finance Item: " + financeItems.toString());
                calculateSum();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(contentPane, "Could not remove object");
            }
        });
    }

    private FinanceFlat createFinanceFlat() {
        FinanceFlat newFinanceFlat = new FinanceFlat(tfTitle.getText(),
                (Integer) spPrice.getValue(),
                activeFlat);

        try {
            FinanceFlatDAO creator = DatabaseConnection.getInstance().createDao(FinanceFlatDAO.class);

            if (!creator.validate(newFinanceFlat)) {
                JOptionPane.showMessageDialog(contentPane, "Finance data is not valid");
            } else {
                return creator.create(newFinanceFlat);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(contentPane, "Could not create finance flat");
        }

        return null;
    }

    private void calculateSum() {
        double sum = 0;
        double roommateCosts = 0;
        for (int counter = 0; counter < financeItems.size(); counter++) {
            sum += financeItems.get(counter).getPrice();
        }
        lTotalCosts.setText(String.valueOf(sum));

        FlatDAO creator = new FlatDAO(DatabaseConnection.getInstance().getSessionFactory());
        if (creator.getRoommates(activeFlat).size() == 0) {
            lRoommateCosts.setText("No roommates");
        } else {
            roommateCosts = sum / creator.getRoommates(activeFlat).size();
            lRoommateCosts.setText(String.format("%.2f", roommateCosts));
        }
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Finance Flat Overview");
        panel1.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        panel1.add(backButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        removeButton = new JButton();
        removeButton.setText("Remove");
        panel2.add(removeButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addButton = new JButton();
        addButton.setText("Add");
        panel2.add(addButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spPrice = new JSpinner();
        panel2.add(spPrice, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tfTitle = new JTextField();
        panel2.add(tfTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        financeFlatTable = new JTable();
        contentPane.add(financeFlatTable, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Costs per Roommate:");
        panel3.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Total sum:");
        panel3.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lTotalCosts = new JLabel();
        lTotalCosts.setText("0");
        panel3.add(lTotalCosts, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lRoommateCosts = new JLabel();
        lRoommateCosts.setText("0");
        panel3.add(lRoommateCosts, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
