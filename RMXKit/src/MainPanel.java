

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel main, searchPanel;
	  JTable SR;
//	  AddCustomerDetails ACD;
//	  EditPrefs EP;
//	  PropertyDetails PD;
	  //PropertySearchPanel PSP;
//	  searchCustomer CS;
	  JTabbedPane tabs;
	  JScrollPane SP;
	  
	  
	  public MainPanel()
	  {
	    arrangeComponents();
	  }
	  
	  public void arrangeComponents() 
	  {
//	    ACD = new AddCustomerDetails();
//	    EP = new EditPrefs();
//	    PD = new PropertyDetails();
	    //PSP = new PropertySearchPanel();
//	    CS = new searchCustomer();
	    tabs = new JTabbedPane();
	    searchPanel = new JPanel();
	    //SR = new JTable(20,1);
	    //SR.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    //SP = new JScrollPane(SR);
	    //searchPanel.add(SP);
	    testTable();
	    
//	    tabs.addTab("Customer Details",ACD);
//	    tabs.addTab("Preferences",EP);
//	    tabs.addTab("Property Details",PD);
	    //tabs.addTab("Property Search Panel",PSP);
	    
	    setLayout(new BorderLayout());
//	    add(CS,BorderLayout.NORTH);
	    add(searchPanel, BorderLayout.WEST);
	    add(tabs, BorderLayout.CENTER);
	   
	  }

	  public void testTable()
	  {
//	    searchPanel = new JPanel();    
	    
//	    TableModel dataModel = new AbstractTableModel() {
//	      public int getColumnCount() { return 1; }
//	      public int getRowCount() { return 100;}
//	      public Object getValueAt(int row, int col) { return new Integer(row*col); }
//	    };
//	    JTable searchTable = new JTable(dataModel);
//	    searchTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//	    JScrollPane sp = new JScrollPane(searchTable);
//	    searchPanel.add(sp);
	    
	  }


	  
	public static MainPanel test() {
		MainPanel mp = new MainPanel();
		JFrame view = new JFrame();
		view.add(mp);
		view.setVisible(true);
		view.pack();
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		return mp;
	}

}
