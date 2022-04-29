import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;



@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;
	JScrollPane sp = null;
	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuItemCloseTicket;
	

	public Tickets(Boolean isAdmin) {

		chkIfAdmin = isAdmin;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);
		
		mnuItemCloseTicket = new JMenuItem("Close Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemCloseTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);
		
	
		

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
		mnuItemCloseTicket.addActionListener(this);
		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above*
		 */


	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(800, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
			//adding the start and end date
			String start_date = JOptionPane.showInputDialog(null, "Enter your starting date");
			String ticket_status = JOptionPane.showInputDialog(null, "What's the status for the ticket?");
			// insert ticket information to database
			int id = dao.insertRecords(ticketName, ticketDesc, start_date, ticket_status); //end_date);
			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else
				System.out.println("Ticket cannot be created!!!");
	
	}
		
		//implement actions for update the description by the ticket number
		else if (e.getSource() == mnuItemUpdate) {
			if(chkIfAdmin) {
			String ticketId = JOptionPane.showInputDialog(null, "Enter the id that you want to change");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
				int id = dao.updateRecords(Integer.parseInt(ticketId), ticketDesc);
					if (id != 0) {
								updateGUI();
								System.out.println("Ticket ID : " + ticketId + " changed successfully!!!");
								JOptionPane.showMessageDialog(null, "Ticket ticketId: " + id + " changed");
							} else {
								JOptionPane.showMessageDialog(null, "No ID or operation fail");
								System.out.println("Ticket cannot be changed!!!");
							}
					}
			else {
				JOptionPane.showMessageDialog(null, "Sorry, update ticket only admin account can do it!");
			}
		}
		else if (e.getSource() == mnuItemViewTicket) {
		// retrieve all tickets details for viewing in JTable
		try {

			// Use JTable built in functionality to build a table model and
			// display the table model off your result set!!!
			JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
			jt.setBounds(30, 40, 200, 400);
			JScrollPane sp = new JScrollPane(jt);
			add(sp);
			setVisible(true); // refreshes or repaints frame on screen

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		}
		//implement actions for delete the ticket by the number
		else if (e.getSource()== mnuItemDelete) {
			if (chkIfAdmin) {
			String ticketId = JOptionPane.showInputDialog(null, "Enter the id that you want to delete");
					int i = JOptionPane.showConfirmDialog(null, "If it is okay to delete the record?", "yes", JOptionPane.YES_NO_OPTION);
					if (i == JOptionPane.YES_OPTION) {
						int id = dao.deleteRecords(Integer.parseInt(ticketId));
						if (id != 0) {
							updateGUI();
							System.out.println("Ticket ID : " + ticketId + " deleted successfully!!!");
							JOptionPane.showMessageDialog(null, "Ticket ticketId: " + ticketId + " deleted");} 
						else{
							JOptionPane.showMessageDialog(null, "No ID or operation fail");
							System.out.println("Ticket cannot be deleted!!!");
						}}
					else {
						JOptionPane.showMessageDialog(null, "input empty or error");
						}
					}					
				else {
					JOptionPane.showMessageDialog(null, "Sorry, delete ticket only admin account can do it!");
						}
		}
					
		//implement actions for close the completed ticket by the ticket number
		else if (e.getSource() == mnuItemCloseTicket) {
			String ticketId = JOptionPane.showInputDialog(null, "Enter the ticket id that you want to close");
			String ticket_status = JOptionPane.showInputDialog(null, "What's the status for the ticket?");
			java.util.Date dt = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
				int id = dao.closeRecords(Integer.parseInt(ticketId),currentTime,ticket_status);
					if (id != 0){
						updateGUI();
						System.out.println("Ticket ID : " + ticketId + " closed successfully!!!");
						JOptionPane.showMessageDialog(null, "Ticket ticketId: " + ticketId + " closed");
						}
					else {
						JOptionPane.showMessageDialog(null, "No ID or operation fail");
						System.out.println("Ticket cannot be closed!!!");
						}
					}
		}
	void updateGUI() {
		try {
			if(sp!=null) {
				this.remove(sp);
			}
				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			
			
	}
	}
	}
