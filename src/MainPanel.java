import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MainPanel extends JPanel {
    private DrawingPanel drawingPanel;
    private static final int SCALE = 10;
    private int nextX = 50;
    private int nextY = 50;
    private static final int GAP = 0;
    private static final int MAX_WIDTH = 800;
    private File lastFile;

    private void stylebutton(JButton button) {
        button.setBackground(new Color(45, 45, 45));
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    public MainPanel() {
        setLayout(new BorderLayout());

        JButton addroom = new JButton("Add Room");
        JButton removeRoom = new JButton("Remove Room");
        JButton saveButton = new JButton("Save");
        JButton saveasButton = new JButton("Save As");
        JButton loadButton = new JButton("Load");
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonpanel.setBackground(new Color(30, 30, 30));
        stylebutton(addroom);
        stylebutton(removeRoom);
        stylebutton(saveButton);
        stylebutton(saveasButton);
        stylebutton(loadButton);
        buttonpanel.add(addroom);
        buttonpanel.add(removeRoom);
        buttonpanel.add(saveButton);
        buttonpanel.add(saveasButton);
        buttonpanel.add(loadButton);

        this.add(buttonpanel, BorderLayout.NORTH);

        drawingPanel = new DrawingPanel();
        this.add(drawingPanel, BorderLayout.CENTER);

        addroom.addActionListener(e -> openAddRoomDialog());

        removeRoom.addActionListener(e -> drawingPanel.startRemoveRoom());

        saveButton.addActionListener(e -> saveFloorPlan(false));

        saveasButton.addActionListener(e -> saveFloorPlan(true));

        loadButton.addActionListener(e -> loadFloorPlan());
    }

    private void saveFloorPlan(boolean saveAs) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".fpl");
            }

            public String getDescription() {
                return "Floor Plan Files (*.fpl)";
            }
        });
        if (!saveAs && lastFile != null && lastFile.exists()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(lastFile))) {
                oos.writeObject(drawingPanel.rooms);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".fpl")) {
                    file = new File(file.getAbsolutePath() + ".fpl");
                }
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    oos.writeObject(drawingPanel.rooms);
                    lastFile = file;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Save Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void loadFloorPlan() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".fpl");
            }

            public String getDescription() {
                return "Floor Plan Files (*.fpl)";
            }
        });
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof java.util.List) {
                    drawingPanel.rooms = (java.util.List<Room>) obj;
                    nextX = 50;
                    nextY = 50;
                    for (Room room : drawingPanel.rooms) {
                        int pixelWidth = room.width * SCALE;
                        int pixelLength = room.length * SCALE;
                        if (nextX + pixelWidth > MAX_WIDTH) {
                            nextX = 50;
                            nextY += pixelLength + GAP;
                        }
                        nextX += pixelWidth + GAP;
                    }
                    drawingPanel.repaint();
                    lastFile = file;
                }
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(), "Load Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openAddRoomDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Room", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        dialog.setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel typeLabel = new JLabel("Room Type:");
        String[] roomTypes = { "Bedroom", "Bathroom", "Living Room", "Kitchen" };
        JComboBox<String> typeBox = new JComboBox<>(roomTypes);

        JLabel widthLabel = new JLabel("Width (in ft):");
        JTextField widthField = new JTextField();

        JLabel lengthLabel = new JLabel("Length (in ft):");
        JTextField lengthField = new JTextField();

        JButton submitButton = new JButton("Add Room");

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
                        nextX = 50;
                        nextY += pixelLength + GAP;
                    }

                    Room newRoom = new Room(selectedRoom, width, length, nextX, nextY, roomColor);
                    if (drawingPanel.doesCollide(newRoom)) {
                        JOptionPane.showMessageDialog(dialog, "This room overlaps with an existing one.",
                                "Collision Detected", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    nextX += pixelWidth + GAP;

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

    class DrawingPanel extends JPanel {
        private Room selectedRoom = null;
        private int offsetX, offsetY;
        private boolean removeMode = false;

        private java.util.List<Room> rooms = new java.util.ArrayList<>();

        private boolean collidesWithOtherRooms(Room draggedRoom, int newX, int newY) {
            Rectangle newBounds = new Rectangle(newX, newY, draggedRoom.width * SCALE, draggedRoom.length * SCALE);
            for (Room other : rooms) {
                if (other == draggedRoom)
                    continue;
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
                    return true;
                }
            }
            return false;
        }

        public void startRemoveRoom() {
            removeMode = true;
            JOptionPane.showMessageDialog(this, "Click a room to remove it.", "Remove Room",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        private void removeRoom(Room room) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Remove " + room.type + " (" + room.width + "x" + room.length + " ft)?", "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                rooms.remove(room);
                recalculateNextPosition();
                repaint();
            }
            removeMode = false;
        }

        private void recalculateNextPosition() {
            nextX = 50;
            nextY = 50;
            for (Room room : rooms) {
                int pixelWidth = room.width * SCALE;
                int pixelLength = room.length * SCALE;
                if (nextX + pixelWidth > MAX_WIDTH) {
                    nextX = 50;
                    nextY += pixelLength + GAP;
                }
                nextX += pixelWidth + GAP;
            }
        }

        public DrawingPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (removeMode) {
                        for (Room room : rooms) {
                            int roomX = room.x;
                            int roomY = room.y;
                            int roomW = room.width * SCALE;
                            int roomH = room.length * SCALE;

                            if (e.getX() >= roomX && e.getX() <= roomX + roomW &&
                                    e.getY() >= roomY && e.getY() <= roomY + roomH) {
                                removeRoom(room);
                                break;
                            }
                        }
                    } else {
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
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!removeMode) {
                        selectedRoom = null;
                    }
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (selectedRoom != null && !removeMode) {
                        int proposedX = e.getX() - offsetX;
                        int proposedY = e.getY() - offsetY;

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

            g2d.setColor(new Color(45, 45, 45));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);
            int gridSpacing = 20;
            for (int x = 0; x < getWidth(); x += gridSpacing) {
                for (int y = 0; y < getHeight(); y += gridSpacing) {
                    g2d.fillRect(x, y, 1, 1);
                }
            }

            for (Room r : rooms) {
                g2d.setColor(r.color);
                g2d.fillRect(r.x, r.y, r.width * SCALE, r.length * SCALE);

                g2d.setColor(Color.WHITE);
                g2d.drawRect(r.x, r.y, r.width * SCALE, r.length * SCALE);
                g2d.drawString(r.type + " (" + r.width + "x" + r.length + " ft)", r.x + 5, r.y + 15);
            }
        }
    }

    private Color getColorForRoomType(String type) {
        switch (type) {
            case "Bedroom":
                return new Color(70, 130, 180);
            case "Bathroom":
                return new Color(100, 149, 237);
            case "Living Room":
                return new Color(60, 179, 113);
            case "Kitchen":
                return new Color(255, 165, 0);
            default:
                return Color.GRAY;
        }
    }
}