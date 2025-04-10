
// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.util.ArrayList;

// public class MainPanel extends JPanel {
//     private ArrayList<Room> rooms = new ArrayList<>();

//     public MainPanel() {
//         setLayout(new BorderLayout());
//         JButton addroom = new JButton("Add Room");
//         JButton addfurniture = new JButton("Add Furniture");
//         JPanel buttonpanel = new JPanel();
//         buttonpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//         buttonpanel.add(addroom);
//         buttonpanel.add(addfurniture);
//         this.add(buttonpanel, BorderLayout.WEST);

//         addroom.addActionListener(new ActionListener() {
//             public void actionPerformed(ActionEvent e) {
//                 JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(MainPanel.this), "Add Room",
//                         true);
//                 dialog.setSize(400, 250);
//                 dialog.setLocation(0, 0);
//                 dialog.setLayout(new GridBagLayout());

//                 GridBagConstraints gbc = new GridBagConstraints();
//                 gbc.insets = new Insets(10, 10, 10, 10); // padding
//                 gbc.fill = GridBagConstraints.HORIZONTAL;

//                 // Room Type
//                 JLabel typeLabel = new JLabel("Room Type:");
//                 String[] roomTypes = { "Bedroom", "Bathroom", "Living Room", "Kitchen" };
//                 JComboBox<String> typeBox = new JComboBox<>(roomTypes);

//                 // Width
//                 JLabel widthLabel = new JLabel("Width (in ft):");
//                 JTextField widthField = new JTextField();

//                 // Height
//                 JLabel lengthLabel = new JLabel("Length (in ft):");
//                 JTextField lengthField = new JTextField();

//                 // Submit Button
//                 JButton submitButton = new JButton("Add Room");

//                 // Layout
//                 gbc.gridx = 0;
//                 gbc.gridy = 0;
//                 dialog.add(typeLabel, gbc);
//                 gbc.gridx = 1;
//                 gbc.gridy = 0;
//                 dialog.add(typeBox, gbc);

//                 gbc.gridx = 0;
//                 gbc.gridy = 1;
//                 dialog.add(widthLabel, gbc);
//                 gbc.gridx = 1;
//                 gbc.gridy = 1;
//                 dialog.add(widthField, gbc);

//                 gbc.gridx = 0;
//                 gbc.gridy = 2;
//                 dialog.add(lengthLabel, gbc);
//                 gbc.gridx = 1;
//                 gbc.gridy = 2;
//                 dialog.add(lengthField, gbc);

//                 gbc.gridx = 0;
//                 gbc.gridy = 3;
//                 gbc.gridwidth = 2;
//                 dialog.add(submitButton, gbc);

//                 // Action when user clicks "Add Room"
//                 submitButton.addActionListener(new ActionListener() {
//                     public void actionPerformed(ActionEvent e) {
//                         try {
//                             String selectedRoom = (String) typeBox.getSelectedItem();
//                             int width = Integer.parseInt(widthField.getText());
//                             int length = Integer.parseInt(lengthField.getText());

//                             Room newRoom = new Room(selectedRoom, width, length, 50, 50);
//                             addRoom(newRoom); // adds to list and calls repaint()
//                             dialog.dispose();
//                         } catch (NumberFormatException ex) {
//                             JOptionPane.showMessageDialog(dialog, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
//                         } catch (Exception ex) {
//                             ex.printStackTrace(); // show any unexpected issue in the console
//                         }
//                     }
//                 });

//                 dialog.setVisible(true);
//             }
//         });
//         JPanel drawingPanel=new JPanel();
//         this.add(drawingPanel,BorderLayout.CENTER);
//     }

//     public void addRoom(Room room) {
//         rooms.add(room);
//         System.out.println("Room added: " + room.type);
//         repaint();
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         for (Room r : rooms) {
//             g.drawRect(r.x, r.y, r.width, r.length);
//             g.drawString(r.type, r.x + 5, r.y + 15); // draw type label inside room
//         }
//     }
// }
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPanel extends JPanel {
    private DrawingPanel drawingPanel;
    private static final int SCALE = 10; // 1 foot = 10 pixels
    private int nextX = 50;
    private int nextY = 50;
    private static final int GAP = 2; // space between rooms
    private static final int MAX_WIDTH = 800; // max width before wrapping
    private void stylebutton(JButton button){
        button.setBackground(new Color(45,45,45));
        button.setFont(new Font("SansSerif",Font.PLAIN,12));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

    }
    public MainPanel() {

        setLayout(new BorderLayout());

        // Create buttons
        JButton addroom = new JButton("Add Room");
        JButton addfurniture = new JButton("Add Furniture");
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonpanel.setBackground(new Color(30, 30, 30)); 
        stylebutton(addroom);
        stylebutton(addfurniture);

        buttonpanel.add(addroom);
        buttonpanel.add(addfurniture);

        this.add(buttonpanel, BorderLayout.WEST);

        // Create and add drawing panel
        drawingPanel = new DrawingPanel();
        this.add(drawingPanel, BorderLayout.CENTER);

        // Add Room button functionality
        addroom.addActionListener(e -> openAddRoomDialog());
    }

    private void openAddRoomDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Room", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this); // center the dialog
        dialog.setLayout(new GridBagLayout());
        dialog.setBackground(new Color(30,30,30));
        

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Room Type
        JLabel typeLabel = new JLabel("Room Type:");
        String[] roomTypes = { "Bedroom", "Bathroom", "Living Room", "Kitchen" };
        JComboBox<String> typeBox = new JComboBox<>(roomTypes);

        // Width
        JLabel widthLabel = new JLabel("Width (in ft):");
        JTextField widthField = new JTextField();

        // Length
        JLabel lengthLabel = new JLabel("Length (in ft):");
        JTextField lengthField = new JTextField();

        // Submit Button
        JButton submitButton = new JButton("Add Room");

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(typeLabel, gbc);
        gbc.gridx = 1;
        dialog.add(typeBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(widthLabel, gbc);
        gbc.gridx = 1;
        dialog.add(widthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(lengthLabel, gbc);
        gbc.gridx = 1;
        dialog.add(lengthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(submitButton, gbc);

        // Add Room action
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String selectedRoom = (String) typeBox.getSelectedItem();
                    int width = Integer.parseInt(widthField.getText());
                    int length = Integer.parseInt(lengthField.getText());

                    Color roomColor = getColorForRoomType(selectedRoom);
                    int pixelWidth = width * SCALE;
                    int pixelLength = length * SCALE;

                    if (nextX + pixelWidth > MAX_WIDTH) {
                        // Wrap to next row
                        nextX = 50;
                        nextY += pixelLength + GAP;
                    }

                    Room newRoom = new Room(selectedRoom, width, length, nextX, nextY, roomColor);
                    // 🧱 Check for collision before adding
                    if (drawingPanel.doesCollide(newRoom)) {
                        JOptionPane.showMessageDialog(dialog, "This room overlaps with an existing one.",
                                "Collision Detected", JOptionPane.ERROR_MESSAGE);
                        return; // Don't add the room
                    }
                    nextX += pixelWidth + GAP;

                    // default position
                    drawingPanel.addRoom(newRoom);
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid numbers.", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.setVisible(true);
    }

    // Inner DrawingPanel class
    // class DrawingPanel extends JPanel {
    // private Room selectedRoom = null;
    // private int offsetX, offsetY;

    // private java.util.List<Room> rooms = new java.util.ArrayList<>();

    // public void addRoom(Room room) {
    // rooms.add(room);
    // repaint();
    // }

    // @Override
    // protected void paintComponent(Graphics g) {
    // super.paintComponent(g);
    // Graphics2D g2d = (Graphics2D) g;

    // // Fill background with light grey
    // g2d.setColor(new Color(45, 45, 45)); // light grey
    // g2d.fillRect(0, 0, getWidth(), getHeight());

    // // Draw white dots for grid
    // g2d.setColor(Color.WHITE);
    // int gridSpacing = 20; // distance between dots in pixels
    // for (int x = 0; x < getWidth(); x += gridSpacing) {
    // for (int y = 0; y < getHeight(); y += gridSpacing) {
    // g2d.fillRect(x, y, 1, 1); // tiny white dot
    // }
    // }

    // // Draw rooms
    // g2d.setColor(Color.BLACK); // room outlines in black
    // for (Room r : rooms) {
    // g2d.setColor(r.color); // fill color
    // g2d.fillRect(r.x, r.y, r.width * SCALE, r.length * SCALE);

    // g2d.setColor(Color.WHITE); // outline and text
    // g2d.drawRect(r.x, r.y, r.width * SCALE, r.length * SCALE);
    // g2d.drawString(r.type + " (" + r.width + "x" + r.length + " ft)", r.x + 5,
    // r.y + 15);
    // }

    // }

    // }
    // Inner DrawingPanel class
    class DrawingPanel extends JPanel {
        private Room selectedRoom = null;
        private int offsetX, offsetY;

        private java.util.List<Room> rooms = new java.util.ArrayList<>();

        private boolean collidesWithOtherRooms(Room draggedRoom, int newX, int newY) {
            Rectangle newBounds = new Rectangle(newX, newY, draggedRoom.width * SCALE, draggedRoom.length * SCALE);
            for (Room other : rooms) {
                if (other == draggedRoom)
                    continue; // Skip self
                Rectangle otherBounds = new Rectangle(other.x, other.y, other.width * SCALE, other.length * SCALE);
                if (newBounds.intersects(otherBounds)) {
                    return true;
                }
            }
            return false;
        }

        public boolean doesCollide(Room newRoom) {
            Rectangle newRect = new Rectangle(newRoom.x, newRoom.y, newRoom.width * SCALE, newRoom.length * SCALE);
            for (Room room : rooms) {
                Rectangle existingRect = new Rectangle(room.x, room.y, room.width * SCALE, room.length * SCALE);
                if (newRect.intersects(existingRect)) {
                    return true; // Collision found
                }
            }
            return false; // No collision
        }

        public DrawingPanel() {
            // Mouse Pressed: Check if a room is selected
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    for (Room room : rooms) {
                        int roomX = room.x;
                        int roomY = room.y;
                        int roomW = room.width * SCALE;
                        int roomH = room.length * SCALE;

                        if (e.getX() >= roomX && e.getX() <= roomX + roomW &&
                                e.getY() >= roomY && e.getY() <= roomY + roomH) {
                            selectedRoom = room;
                            offsetX = e.getX() - roomX;
                            offsetY = e.getY() - roomY;
                            break;
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    selectedRoom = null;
                }
            });

            // Mouse Dragged: Move the selected room
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (selectedRoom != null) {
                        int proposedX = e.getX() - offsetX;
                        int proposedY = e.getY() - offsetY;

                        // Only update position if it doesn't collide
                        if (!collidesWithOtherRooms(selectedRoom, proposedX, proposedY)) {
                            selectedRoom.x = proposedX;
                            selectedRoom.y = proposedY;
                            repaint();
                        }
                    }
                }
            });

        }

        public void addRoom(Room room) {
            rooms.add(room);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Fill background
            g2d.setColor(new Color(45, 45, 45));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Draw grid
            g2d.setColor(Color.WHITE);
            int gridSpacing = 20;
            for (int x = 0; x < getWidth(); x += gridSpacing) {
                for (int y = 0; y < getHeight(); y += gridSpacing) {
                    g2d.fillRect(x, y, 1, 1);
                }
            }

            // Draw rooms
            for (Room r : rooms) {
                g2d.setColor(r.color); // room color
                g2d.fillRect(r.x, r.y, r.width * SCALE, r.length * SCALE);

                g2d.setColor(Color.WHITE); // outline and label
                g2d.drawRect(r.x, r.y, r.width * SCALE, r.length * SCALE);
                g2d.drawString(r.type + " (" + r.width + "x" + r.length + " ft)", r.x + 5, r.y + 15);
            }
        }
    }

    private Color getColorForRoomType(String type) {
        switch (type) {
            case "Bedroom":
                return new Color(70, 130, 180); // Steel Blue
            case "Bathroom":
                return new Color(100, 149, 237); // Cornflower Blue
            case "Living Room":
                return new Color(60, 179, 113); // Medium Sea Green
            case "Kitchen":
                return new Color(255, 165, 0); // Orange
            default:
                return Color.GRAY; // Fallback color
        }
    }

}
