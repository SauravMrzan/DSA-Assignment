// The Tetris game operates on a 10x20 grid with blocks measuring 30px,
// where different shapes in random colors descend from the top.
// Players use the arrow keys to shift, rotate, and position the blocks on the board.
// The game continues until a new block cannot be placed, which triggers a game-over condition.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Q3BTetrisGame extends JPanel implements ActionListener {
    // Game board settings: width, height, block size, and color options
    private static final int BOARD_WIDTH = 10; // Number of blocks horizontally
    private static final int BOARD_HEIGHT = 20; // Number of blocks vertically
    private static final int BLOCK_SIZE = 30; // Block dimension in pixels
    private static final Color[] COLORS = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE }; // Possible block colors

    private Timer timer; // Timer to regulate game speed
    private boolean isGameOver; // Flag to track if the game has ended
    private int[][] board; // 2D array to represent the game board
    private Block currentBlock; // The block currently falling on the board
    private Queue<Block> blockQueue; // Queue holding upcoming blocks

    // Constructor to initialize the game setup
    public Q3BTetrisGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE)); // Define panel size
        setBackground(Color.BLACK); // Set background to black
        setFocusable(true); // Make the panel focusable to capture key events
        addKeyListener(new KeyAdapter() { // Add listener for key events
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isGameOver) { // Only process inputs when the game is ongoing
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            moveBlockLeft(); // Move block left
                            break;
                        case KeyEvent.VK_RIGHT:
                            moveBlockRight(); // Move block right
                            break;
                        case KeyEvent.VK_DOWN:
                            moveBlockDown(); // Move block downward
                            break;
                        case KeyEvent.VK_UP:
                            rotateBlock(); // Rotate the block
                            break;
                    }
                    repaint(); // Refresh the screen after input
                }
            }
        });
        board = new int[BOARD_HEIGHT][BOARD_WIDTH]; // Initialize the game board
        blockQueue = new LinkedList<>(); // Set up the block queue
        timer = new Timer(500, this); // Create a timer with 500ms interval
        timer.start(); // Start the game timer
        generateNewBlock(); // Generate the first block
    }

    // Method to generate a new falling block
    private void generateNewBlock() {
        Random random = new Random(); // Instantiate random generator
        int shapeIndex = random.nextInt(COLORS.length); // Randomly select a color index
        int[][] shape = getRandomShape(); // Get a random block shape
        Block block = new Block(shape, COLORS[shapeIndex]); // Create the new block
        blockQueue.add(block); // Add the new block to the queue
        currentBlock = blockQueue.poll(); // Set the first block in the queue as the current block
    }

    // Method to retrieve a random shape for the block
    private int[][] getRandomShape() {
        int[][][] shapes = {
                { { 1, 1, 1, 1 } }, // I-shape
                { { 1, 1, 0 }, { 0, 1, 1 } }, // Z-shape
                { { 0, 1, 1 }, { 1, 1, 0 } }, // S-shape
                { { 1, 1, 1 }, { 0, 1, 0 } }, // T-shape
                { { 1, 1 }, { 1, 1 } } // O-shape
        };
        Random random = new Random(); // Initialize random generator
        return shapes[random.nextInt(shapes.length)]; // Return a randomly selected shape
    }

    // Called by the timer to update the game logic
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) { // Only run if the game is not over
            if (canMoveDown(currentBlock)) { // Check if block can move down
                currentBlock.row++; // Move the block down by one row
            } else {
                placeBlock(currentBlock); // Place block on the board
                checkCompletedRows(); // Clear completed rows if any
                generateNewBlock(); // Generate a new block for the next turn
                if (!canMoveDown(currentBlock)) { // Check if new block can be placed
                    isGameOver = true; // End the game if the new block cannot be placed
                }
            }
            repaint(); // Refresh the display
        }
    }

    // Check if the block can move down
    private boolean canMoveDown(Block block) {
        for (int row = 0; row < block.shape.length; row++) {
            for (int col = 0; col < block.shape[row].length; col++) {
                if (block.shape[row][col] != 0) { // Check occupied cells in block
                    int newRow = block.row + row + 1; // Calculate new row for block movement
                    int newCol = block.col + col; // Calculate new column for block movement
                    if (newRow >= BOARD_HEIGHT || (newRow >= 0 && board[newRow][newCol] != 0)) { // Check for collision or out of bounds
                        return false; // Cannot move down
                    }
                }
            }
        }
        return true; // Block can safely move down
    }

    // Place the current block on the board
    private void placeBlock(Block block) {
        for (int row = 0; row < block.shape.length; row++) {
            for (int col = 0; col < block.shape[row].length; col++) {
                if (block.shape[row][col] != 0) { // Check for occupied block cells
                    board[block.row + row][block.col + col] = block.colorIndex + 1; // Update the board with the block's color
                }
            }
        }
    }

    // Check and remove completed rows, shifting others down
    private void checkCompletedRows() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            boolean isComplete = true; // Assume row is full
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (board[row][col] == 0) { // If any cell is empty, the row is not full
                    isComplete = false;
                    break;
                }
            }
            if (isComplete) { // If row is full
                for (int r = row; r > 0; r--) { // Shift rows above the completed row down
                    board[r] = board[r - 1];
                }
                board[0] = new int[BOARD_WIDTH]; // Clear the top row
            }
        }
    }

    // Move the block left if possible
    private void moveBlockLeft() {
        if (canMoveLeft(currentBlock)) { // Check if block can move left
            currentBlock.col--; // Move the block left
        }
    }

    // Check if the block can move left
    private boolean canMoveLeft(Block block) {
        for (int row = 0; row < block.shape.length; row++) {
            for (int col = 0; col < block.shape[row].length; col++) {
                if (block.shape[row][col] != 0) { // Check occupied cells in the block
                    int newRow = block.row + row; // Calculate new row
                    int newCol = block.col + col - 1; // Calculate new column
                    if (newCol < 0 || (newRow >= 0 && board[newRow][newCol] != 0)) { // Check if the new position is out of bounds or occupied
                        return false; // Cannot move left
                    }
                }
            }
        }
        return true; // Block can move left
    }

    // Move the block right if possible
    private void moveBlockRight() {
        if (canMoveRight(currentBlock)) { // Check if block can move right
            currentBlock.col++; // Move the block right
        }
    }

    // Check if the block can move right
    private boolean canMoveRight(Block block) {
        for (int row = 0; row < block.shape.length; row++) {
            for (int col = 0; col < block.shape[row].length; col++) {
                if (block.shape[row][col] != 0) { // Check occupied cells in the block
                    int newRow = block.row + row; // Calculate new row
                    int newCol = block.col + col + 1; // Calculate new column
                    if (newCol >= BOARD_WIDTH || (newRow >= 0 && board[newRow][newCol] != 0)) { // Check if the new position is out of bounds or occupied
                        return false; // Cannot move right
                    }
                }
            }
        }
        return true; // Block can move right
    }

    // Move the block down one row
    private void moveBlockDown() {
        if (canMoveDown(currentBlock)) { // Check if block can move down
            currentBlock.row++; // Move the block down
        }
    }

    // Rotate the block 90 degrees clockwise
    private void rotateBlock() {
        currentBlock.rotate(); // Rotate the block
        if (!canMoveDown(currentBlock)) { // Check if the block can move down after rotation
            currentBlock.rotate(); // Rotate it back if it collides
            currentBlock.rotate();
            currentBlock.rotate();
        }
    }

    // Paint the game board and display the blocks
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call superclass to paint the background
        // Render the game board
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (board[row][col] != 0) { // Check if a block is present at this position
                    g.setColor(COLORS[board[row][col] - 1]); // Set the color for the block
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE); // Draw the block
                }
            }
        }
        // Render the current falling block
        if (currentBlock != null) {
            g.setColor(currentBlock.color); // Set the color for the current block
            for (int row = 0; row < currentBlock.shape.length; row++) {
                for (int col = 0; col < currentBlock.shape[row].length; col++) {
                    if (currentBlock.shape[row][col] != 0) { // Check if a cell in the block is occupied
                        g.fillRect((currentBlock.col + col) * BLOCK_SIZE, (currentBlock.row + row) * BLOCK_SIZE,
                                BLOCK_SIZE, BLOCK_SIZE); // Draw the block
                    }
                }
            }
        }
        // Display game over message if the game is over
        if (isGameOver) {
            g.setColor(Color.WHITE); // Set color for the game over message
            g.setFont(new Font("Arial", Font.BOLD, 36)); // Set the font for the message
            g.drawString("Game Over", 50, 300); // Display the message
        }
    }

    // Main method to initialize and start the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game"); // Create the main game window
        Q3BTetrisGame game = new Q3BTetrisGame(); // Instantiate the game
        frame.add(game); // Add the game panel to the window
        frame.pack(); // Adjust the window size based on the panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application closes on window close
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true); // Make the window visible
    }

    // Inner class representing a Tetris block
    static class Block {
        int[][] shape; // Shape of the block in a 2D array
        int row, col; // Position of the block on the board
        Color color; // Color of the block
        int colorIndex; // Index for the block color

        // Constructor to initialize the block
        public Block(int[][] shape, Color color) {
            this.shape = shape; // Set block shape
            this.row = 0; // Set initial row position
            this.col = BOARD_WIDTH / 2 - shape[0].length / 2; // Set initial column position
            this.color = color; // Set block color
            this.colorIndex = getColorIndex(color); // Find the color index
        }

        // Get the index of the color in the COLORS array
        private int getColorIndex(Color color) {
            for (int i = 0; i < COLORS.length; i++) {
                if (COLORS[i].equals(color)) { // Compare block color with color array
                    return i; // Return index
                }
            }
            return -1; // Return -1 if not found
        }

        // Rotate the block by 90 degrees
        public void rotate() {
            int rows = shape.length; // Get the number of rows in the block
            int cols = shape[0].length; // Get the number of columns in the block
            int[][] rotated = new int[cols][rows]; // Create a new array for the rotated shape
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    rotated[j][rows - i - 1] = shape[i][j]; // Rotate the shape
                }
            }
            shape = rotated; // Assign the rotated shape
        }
    }
}
