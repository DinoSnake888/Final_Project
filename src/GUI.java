import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

class GUI implements ActionListener {
    JFrame frame;
    JPanel panel;
    JButton UserButton, LibrarianButton, AddBook;
    JLabel WelcomeLabel, BlankLabel;
    JTable LibrarianTable;
    DefaultTableModel model;
    JTextField searchField;
    TableRowSorter<TableModel> sorter;

    public GUI() {
        BookManagement.books = SaveNLoad.load();

        frame = new JFrame("Library Interface");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                SaveNLoad.save(BookManagement.books);
                System.exit(0);
            }
        });

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setLayout(new GridLayout(2, 2));
        panel.setBackground(Color.BLACK);

        frame.add(panel, BorderLayout.CENTER);

        WelcomeLabel = new JLabel("Welcome to the HTLA Library Interface");
        WelcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        WelcomeLabel.setForeground(Color.WHITE);
        WelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        BlankLabel = new JLabel("");

        panel.add(BlankLabel);
        panel.add(WelcomeLabel);
        panel.add(BlankLabel);

        UserButton = new JButton("Student");
        UserButton.setPreferredSize(new Dimension(200, 200));
        UserButton.setFont(new Font("Serif", Font.BOLD, 14));
        UserButton.addActionListener(this);

        LibrarianButton = new JButton("Librarian");
        LibrarianButton.setPreferredSize(new Dimension(200, 200));
        LibrarianButton.setFont(new Font("Serif", Font.BOLD, 14));
        LibrarianButton.addActionListener(this);

        panel.add(LibrarianButton);
        panel.add(UserButton);

        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private void buildTable(boolean isLibrarian) {
        panel.removeAll();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(Color.BLACK);

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(ev -> buildHome());
        topBar.add(homeButton);

        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchLabel.setForeground(Color.WHITE);
        topBar.add(searchLabel);

        searchField = new JTextField(22);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        searchField.setBackground(Color.BLACK);
        searchField.setForeground(Color.WHITE);
        topBar.add(searchField);

        if (isLibrarian) {
            AddBook = new JButton("Add a Book");
            AddBook.addActionListener(this);
            topBar.add(AddBook);
        }

        panel.add(topBar, BorderLayout.NORTH);

        // --- TABLE ---
        String[] columns = {"Title", "Author", "ISBN", "Issue Date", "Status"};
        model = new DefaultTableModel(columns, 0);
        LibrarianTable = new JTable(model);
        sorter = new TableRowSorter<>(model);
        LibrarianTable.setRowSorter(sorter);

        // Populate from saved data
        for (Book b : BookManagement.books) {
            model.addRow(new Object[]{b.title, b.author, b.isbn, b.year, b.available ? "Available" : "Checked Out"});
        }

        // Search filter
        searchField.addActionListener(ev -> {
            String query = searchField.getText().trim();
            sorter.setRowFilter(query.isEmpty() ? null : RowFilter.regexFilter("(?i)" + query));
        });

        // Alternating row colors
        LibrarianTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                }
                return c;
            }
        });

        // Row click (librarian only)
        if (isLibrarian) {
            LibrarianTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = LibrarianTable.getSelectedRow();
                    if (row == -1) return;

                    Book selected = BookManagement.books.get(LibrarianTable.convertRowIndexToModel(row));
                    String[] buttons = selected.available
                            ? new String[]{"Edit", "Delete", "Check Out"}
                            : new String[]{"Edit", "Delete", "Return"};

                    int choice = JOptionPane.showOptionDialog(frame,
                            "Please select an action:", "Database Manager",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, buttons, buttons[0]);

                    if (choice == 0) { // Edit
                        JTextField titleField  = new JTextField(selected.title);
                        JTextField authorField = new JTextField(selected.author);
                        JTextField isbnField   = new JTextField(selected.isbn);
                        JTextField yearField   = new JTextField(selected.year);

                        JPanel editPanel = new JPanel(new GridLayout(4, 2, 5, 5));
                        editPanel.add(new JLabel("Title:"));  editPanel.add(titleField);
                        editPanel.add(new JLabel("Author:")); editPanel.add(authorField);
                        editPanel.add(new JLabel("ISBN:"));   editPanel.add(isbnField);
                        editPanel.add(new JLabel("Year:"));   editPanel.add(yearField);

                        int result = JOptionPane.showConfirmDialog(frame, editPanel,
                                "Edit Book", JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            selected.title  = titleField.getText().trim();
                            selected.author = authorField.getText().trim();
                            selected.isbn   = isbnField.getText().trim();
                            selected.year   = yearField.getText().trim();
                            refreshTable();
                        }

                    } else if (choice == 1) { // Delete
                        int confirm = JOptionPane.showConfirmDialog(frame,
                                "Delete \"" + selected.title + "\"?", "Confirm Delete",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            BookManagement.removeBook(selected);
                            refreshTable();
                        }

                    } else if (choice == 2) { // Check Out or Return
                        selected.available = !selected.available;
                        refreshTable();
                    }
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(LibrarianTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Book b : BookManagement.books) {
            model.addRow(new Object[]{b.title, b.author, b.isbn, b.year, b.available ? "Available" : "Checked Out"});
        }
    }

    private void buildHome() {
        panel.removeAll();
        panel.setLayout(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.BLACK);

        BlankLabel = new JLabel("");
        panel.add(BlankLabel);
        panel.add(WelcomeLabel);
        panel.add(BlankLabel);
        panel.add(LibrarianButton);
        panel.add(UserButton);

        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == LibrarianButton) {
            String accessCode = JOptionPane.showInputDialog(frame,
                    "Please enter your code:", "Librarian Login", JOptionPane.QUESTION_MESSAGE);
            if (accessCode == null || accessCode.trim().isEmpty()) return;

            if (accessCode.equalsIgnoreCase("dinosaurs")) {
                buildTable(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == UserButton) {
            buildTable(false);

        } else if (e.getSource() == AddBook) {
            JTextField isbnField = new JTextField(15);
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Enter ISBN:"));
            inputPanel.add(isbnField);

            int result = JOptionPane.showConfirmDialog(frame, inputPanel,
                    "Add a Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String isbn = isbnField.getText().trim();
                if (!isbn.isEmpty()) {
                    BookManagement.createBookWithISBN(isbn);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(frame, "ISBN cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}