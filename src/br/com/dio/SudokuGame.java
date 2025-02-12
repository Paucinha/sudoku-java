package br.com.dio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class SudokuGame {
    private final int[][] board;
    private final boolean[][] fixed;
    private final JTextField[][] cells;
    private final JFrame frame;
    private final Map<JTextField, String> notes;

    public SudokuGame(int[][] initialBoard) {
        this.board = new int[9][9];
        this.fixed = new boolean[9][9];
        this.cells = new JTextField[9][9];
        this.notes = new HashMap<>();
        this.frame = new JFrame("Sudoku");

        initializeBoard(initialBoard);
        createGUI();
    }

    private void initializeBoard(int[][] initialBoard) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = initialBoard[i][j];
                fixed[i][j] = initialBoard[i][j] != 0;
            }
        }
    }

    private void createGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.PLAIN, 20));

                if (fixed[i][j]) {
                    cell.setText(String.valueOf(board[i][j]));
                    cell.setEditable(false);
                    cell.setBackground(Color.LIGHT_GRAY);
                } else {
                    cell.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                addNoteToCell(cell);
                            }
                        }
                    });
                }

                cells[i][j] = cell;
                gridPanel.add(cell);
            }
        }

        JPanel menuPanel = new JPanel();
        JButton checkButton = new JButton("Verificar");
        checkButton.addActionListener(e -> checkGameStatus());
        menuPanel.add(checkButton);

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(menuPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void addNoteToCell(JTextField cell) {
        String note = JOptionPane.showInputDialog(frame, "Digite os números de rascunho separados por vírgula:");
        if (note != null) {
            notes.put(cell, note);
            cell.setText("(" + note + ")");
        }
    }

    private void checkGameStatus() {
        boolean complete = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText();
                if (text.isEmpty() || text.startsWith("(")) {
                    complete = false;
                } else {
                    try {
                        int value = Integer.parseInt(text);
                        if (value < 1 || value > 9) {
                            JOptionPane.showMessageDialog(frame, "Número inválido em (" + (i+1) + ", " + (j+1) + ")");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Valor inválido em (" + (i+1) + ", " + (j+1) + ")");
                        return;
                    }
                }
            }
        }

        if (complete) {
            JOptionPane.showMessageDialog(frame, "Jogo completo!");
        } else {
            JOptionPane.showMessageDialog(frame, "Jogo incompleto.");
        }
    }

    public static void main(String[] args) {
        int[][] initialBoard = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        new SudokuGame(initialBoard);
    }
}

