import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

class GUI implements ActionListener {
// All elements
    JFrame frame;
    JPanel panel;
    JButton UserButton, LibrarianButton, AddBook;
    JLabel WelcomeLabel, BlankLabel;
    JTable LibrarianTable;
    DefaultTableModel model;
    JTextField searchField;
    TableRowSorter<TableModel> sorter;

    public GUI() {
// Frame and panel setup
        frame = new JFrame("Library Interface");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(200, 200, 200, 200));
        panel.setLayout(new GridLayout(2, 2));
        panel.setBackground(Color.BLACK);

// Adding panel to frame
        frame.add(panel, BorderLayout.CENTER);

// Welcome label setup
        WelcomeLabel = new JLabel("Welcome to the HTLA Library Interface");
        WelcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        WelcomeLabel.setForeground(Color.WHITE);
        WelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

// Blank Label setup
        BlankLabel = new JLabel("");

//Blank Label setup
        panel.add(BlankLabel);

// Adding label to panel
        panel.add(WelcomeLabel);


// Adding blank label to panel
        panel.add(BlankLabel);

// Creating buttons
        UserButton = new JButton("Student");
        UserButton.setPreferredSize(new Dimension(200, 200));
        UserButton.setFont(new Font("Serif", Font.BOLD, 14));

// Making the button do something
        UserButton.addActionListener(this);

        LibrarianButton = new JButton("Librarian");
        LibrarianButton.setPreferredSize(new Dimension(200, 200));
        LibrarianButton.setFont(new Font("Serif", Font.BOLD, 14));

// Making this button do something
        LibrarianButton.addActionListener(this);

// Adding buttons to panel
        panel.add(LibrarianButton, BorderLayout.SOUTH);
        panel.add(UserButton, BorderLayout.SOUTH);

// Makes frame visible
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accesscode = "dinosaurs";

// Librarian Button Action
        if (e.getSource() == LibrarianButton) {
            String AccessCode = JOptionPane.showInputDialog(frame, "Please enter your code:", "Librarian Login", JOptionPane.QUESTION_MESSAGE);

// If a code is typed
            if (AccessCode != null && !AccessCode.trim().isEmpty()) {

/////---   LIBRARIAN ---
// If the code is correct
                if (AccessCode.equalsIgnoreCase(accesscode)) {
                    panel.removeAll();

// Switch to BorderLayout to manage the search panel on top and table underneath
                    panel.setLayout(new BorderLayout(10, 10));
                    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    //SEARCH BAR SECTION (TOP)
                    JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    searchGroup.setBackground(Color.BLACK);

                    JLabel SearchLabel = new JLabel("Search: ");
                    SearchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                    SearchLabel.setForeground(Color.WHITE);

                    searchField = new JTextField(22);
                    searchField.setCaretColor(Color.WHITE);
                    searchField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
                    searchField.setBackground(Color.BLACK);
                    searchField.setForeground(Color.WHITE);

                    searchGroup.add(SearchLabel);
                    searchGroup.add(searchField);
                    panel.add(searchGroup, BorderLayout.NORTH);

                    AddBook = new JButton("Add a Book");
                    AddBook.setPreferredSize(new Dimension(20, 20));
                    panel.add(AddBook, BorderLayout.CENTER);



                    //  TABLE DESIGN SECTION (CENTER)
                    String[] columns = {
                            "Title",
                            "Author",
                            "Genre",
                            "ISBN",
                            "Issue Date",
                            "Status"
                    };

                    // Initializing the model first, then the table, then the sorter
                    model = new DefaultTableModel(columns, 10);
                    LibrarianTable = new JTable(model);
                    sorter = new TableRowSorter<>(model);
                    LibrarianTable.setRowSorter(sorter);

                    // Enabling operational Filter functionality in the search bar
                    searchField.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String query = searchField.getText().trim();
                            if (query.isEmpty()) {
                                sorter.setRowFilter(null);
                            } else {
                                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
                            }
                        }
                    });

                    // Alternating background row rendering logic
                    LibrarianTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(
                                JTable table, Object value, boolean isSelected,
                                boolean hasFocus, int row, int column) {

                            Component c = super.getTableCellRendererComponent(
                                    table, value, isSelected, hasFocus, row, column);

                            if (!isSelected) {
                                if (row % 2 == 0) {
                                    c.setBackground(Color.WHITE);
                                } else {
                                    c.setBackground(Color.LIGHT_GRAY);
                                }
                            }
                            return c;
                        }
                    });

                    // Mouse listening header action
                    LibrarianTable.getTableHeader().addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int column = LibrarianTable.columnAtPoint(e.getPoint());
                            String title = LibrarianTable.getColumnName(column);

                            JFrame newFrame = new JFrame(title);
                            JPanel internalPanel = new JPanel();
                            newFrame.add(internalPanel);
                            newFrame.setSize(400, 300);
                            newFrame.setLocationRelativeTo(null);
                            newFrame.setVisible(true);
                        }
                    });

                    // Row selection pop-up notice
                    LibrarianTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int row = LibrarianTable.getSelectedRow();
                            if (row != -1) {
//FIGURE OUT HOW TO DO THIS    if row.status(equals >0 )
//                                have checkout, update, delete
//                             else { row status = 0
//                                update, delete, and checkout gives you error message
                                String[] myCustomButtons = {"Edit", "Delete", "Check Out"};

                                int choice = JOptionPane.showOptionDialog(
                                        null,
                                        "Please select an action:",
                                        "Database Manager",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.PLAIN_MESSAGE,
                                        null,
                                        myCustomButtons,
                                        myCustomButtons[0]  );
                            }
                        }
                    });

                    // Placing the custom JTable inside a JScrollPane
                    JScrollPane scrollPane = new JScrollPane(LibrarianTable);
                    panel.add(scrollPane, BorderLayout.SOUTH);

                    // Update layouts and view states
                    panel.revalidate();
                    panel.repaint();
                }

                // If the code is wrong
                else if (!AccessCode.equals(accesscode)) {
                    JOptionPane.showMessageDialog(null, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        }

/////---   GUEST ---
// Guest Button Action
        else if (e.getSource() == UserButton) {
            panel.removeAll();

// Switch to BorderLayout to manage the search panel on top and table underneath
            panel.setLayout(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            //SEARCH BAR SECTION (TOP)
            JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT));
            searchGroup.setBackground(Color.BLACK);

            JLabel SearchLabel = new JLabel("Search: ");
            SearchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            SearchLabel.setForeground(Color.WHITE);

            searchField = new JTextField(22);
            searchField.setCaretColor(Color.WHITE);
            searchField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
            searchField.setBackground(Color.BLACK);
            searchField.setForeground(Color.WHITE);

            searchGroup.add(SearchLabel);
            searchGroup.add(searchField);
            panel.add(searchGroup, BorderLayout.NORTH);

            //  TABLE DESIGN SECTION (CENTER)
            String[] columns = {
                    "Title",
                    "Author",
                    "Genre",
                    "ISBN",
                    "Issue Date",
                    "Status"
            };

            // Initializing the model first, then the table, then the sorter
            model = new DefaultTableModel(columns, 10);
            LibrarianTable = new JTable(model);
            sorter = new TableRowSorter<>(model);
            LibrarianTable.setRowSorter(sorter);

            // Enabling operational Filter functionality in the search bar
            searchField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String query = searchField.getText().trim();
                    if (query.isEmpty()) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
                    }
                }
            });

            // Alternating background row rendering logic
            LibrarianTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {

                    Component c = super.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);

                    if (!isSelected) {
                        if (row % 2 == 0) {
                            c.setBackground(Color.WHITE);
                        } else {
                            c.setBackground(Color.LIGHT_GRAY);
                        }
                    }
                    return c;
                }
            });

            // Mouse listening header action
            LibrarianTable.getTableHeader().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int column = LibrarianTable.columnAtPoint(e.getPoint());
                    String title = LibrarianTable.getColumnName(column);

                    JFrame newFrame = new JFrame(title);
                    JPanel internalPanel = new JPanel();
                    newFrame.add(internalPanel);
                    newFrame.setSize(400, 300);
                    newFrame.setLocationRelativeTo(null);
                    newFrame.setVisible(true);
                }
            });

            // Placing the custom JTable inside a JScrollPane
            JScrollPane scrollPane = new JScrollPane(LibrarianTable);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Update layouts and view states
            panel.revalidate();
            panel.repaint();
        }
    }
}


//Also add back to home page button to each library and an add book button to the librarians page