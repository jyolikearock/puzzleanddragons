package ui;

import game.Color;

import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ai.Configurations;

@SuppressWarnings("serial")
public class AttributeInputPanel extends JPanel {

    public static final Map<Color, String> ATTRIBUTES = new LinkedHashMap<Color, String>();
    static {
        ATTRIBUTES.put(Color.R, "Fire");
        ATTRIBUTES.put(Color.G, "Grass");
        ATTRIBUTES.put(Color.B, "Water");
        ATTRIBUTES.put(Color.L, "Light");
        ATTRIBUTES.put(Color.D, "Dark");
        ATTRIBUTES.put(Color.H, "RCV");
    }

    private static final int ROWS = 7;
    private static final int COLS = 1;

    JPanel attributeNames, attributeValues, attributeTPAs, attributeREs, attributeRowRequireds;
    Map<String, String> teamInfo;

    public AttributeInputPanel() {
        super();
        UIMaster.setAttributeInputPanel(this);
        teamInfo = Configurations.getTeamInfo();

        attributeNames = new JPanel(new GridLayout(ROWS, COLS));
        attributeValues = new JPanel(new GridLayout(ROWS, COLS));
        attributeTPAs = new JPanel(new GridLayout(ROWS, COLS));
        attributeREs = new JPanel(new GridLayout(ROWS, COLS));
        attributeRowRequireds = new JPanel(new GridLayout(ROWS, COLS));

        JLabel columnName;
        columnName = new JLabel("Attr");
        attributeNames.add(columnName);
        columnName = new JLabel("Damage");
        attributeValues.add(columnName);
        columnName = new JLabel("TPAs");
        attributeTPAs.add(columnName);
        columnName = new JLabel("REs");
        attributeREs.add(columnName);
        columnName = new JLabel("RE");
        attributeRowRequireds.add(columnName);

        JTextField attributeName, attributeValue, attributeTPA, attributeRE;
        JCheckBox attributeRowRequired;
        for (Color color : ATTRIBUTES.keySet()) {
            attributeName = new JTextField(ATTRIBUTES.get(color));
            attributeName.setEditable(false);
            attributeValue = new JTextField(teamInfo.get(color.toString()));
            attributeTPA = new JTextField(teamInfo.get(color.toString() + Configurations.TPA_KEY));
            attributeRE = new JTextField(teamInfo.get(color.toString() + Configurations.ROW_ENHANCE_KEY));
            attributeRowRequired = new JCheckBox();
            attributeRowRequired.setSelected(Configurations.isRowRequired(color));

            attributeNames.add(attributeName);
            attributeValues.add(attributeValue);
            attributeTPAs.add(attributeTPA);
            attributeREs.add(attributeRE);
            attributeRowRequireds.add(attributeRowRequired);
        }

        add(attributeNames);
        add(attributeValues);
        add(attributeTPAs);
        add(attributeREs);
        add(attributeRowRequireds);
    }

    public String getAttributeValue(Color color) {
        return ((JTextField) attributeValues.getComponents()[colorToIndex(color)]).getText();
    }

    public String getAttributeTPA(Color color) {
        return ((JTextField) attributeTPAs.getComponents()[colorToIndex(color)]).getText();
    }

    public String getAttributeRE(Color color) {
        return ((JTextField) attributeREs.getComponents()[colorToIndex(color)]).getText();
    }

    public boolean isRowRequired(Color color) {
        return ((JCheckBox) attributeRowRequireds.getComponents()[colorToIndex(color)]).isSelected();
    }

    private static int colorToIndex(Color color) {
        if (color.equals(Color.R)) return 1;
        else if (color.equals(Color.G)) return 2;
        else if (color.equals(Color.B)) return 3;
        else if (color.equals(Color.L)) return 4;
        else if (color.equals(Color.D)) return 5;
        else if (color.equals(Color.H)) return 6;
        return -1;
    }
}
