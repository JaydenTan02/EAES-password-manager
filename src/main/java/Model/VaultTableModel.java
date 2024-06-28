/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import Model.VaultEntry;

public class VaultTableModel extends AbstractTableModel {
    private List<VaultEntry> data;
    private final String[] columnNames = {"Application Name", "Application ID", "Application Password String", "Date created"};
    
    public VaultTableModel() {
        data = new ArrayList<>();
    }
    
    public void setData(List<VaultEntry> newData) {
        data = newData;
        fireTableDataChanged(); // Notify the table that the data has changed
    }
    
    public void addEntry(VaultEntry entry) {
        data.add(entry);
        int rowIndex = data.size() - 1;
        fireTableRowsInserted(rowIndex, rowIndex); // Notify the table of the new row
    }
    
    public void removeEntry(int rowIndex) {
        data.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex); // Notify the table of the removed row
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        VaultEntry entry = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> entry.getApplicationName();
            case 1 -> entry.getApplicationID();
            case 2 -> entry.getApplicationPasswordString();
            case 3 -> entry.getDateCreated();
            default -> null;
        };
    }
}

