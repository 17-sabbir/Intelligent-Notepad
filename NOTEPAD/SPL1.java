import Mynotepad.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Design_implement implements ActionListener, KeyListener, MouseListener {
    private JFrame F;
    private JTextPane T;
    private String currentfilepath = "";
    private String sug_Word = "";
    private String clickedWord = "";
    private String lastText = "";
    private boolean FullscreenView = false;
    Spell_check sc = new Spell_check();
    Prediction p = new Prediction();
    JPopupMenu pm;
    JMenu char_count = new JMenu("Character: 0");
    JMenu word_count = new JMenu("Word: 0");

    public Design_implement() {
        frame();
    }

    public void frame() {
        F = new JFrame("Intelligent-Nodepad");
        T = new JTextPane();
        Font font = new Font("Arial", Font.PLAIN, 16);
        T.setFont(font);
        T.addKeyListener(this);
        T.addMouseListener(this);

        JScrollPane sp = new JScrollPane(T);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        F.add(sp);

        JMenuBar MB = new JMenuBar();
        JMenu M1 = new JMenu("FILE");
        JMenuItem MI1 = new JMenuItem("New file");
        JMenuItem MI2 = new JMenuItem("Open file");
        JMenuItem MI3 = new JMenuItem("Save file");
        JMenuItem MI4 = new JMenuItem("Save as file...");
        JMenuItem MI5 = new JMenuItem("Exit");
        MI1.addActionListener(this);
        MI2.addActionListener(this);
        MI3.addActionListener(this);
        MI4.addActionListener(this);
        MI5.addActionListener(this);
        M1.add(MI1);
        M1.add(MI2);
        M1.add(MI3);
        M1.add(MI4);
        M1.add(MI5);
        MB.add(M1);
        JMenu M2 = new JMenu("Edit");
        JMenuItem MI21 = new JMenuItem("Cut");
        JMenuItem MI22 = new JMenuItem("Copy");
        JMenuItem MI23 = new JMenuItem("Paste");
        JMenuItem MI24 = new JMenuItem("Select all");
        JMenuItem MI25 = new JMenuItem("Find and Replace");
        MI21.addActionListener(this);
        MI22.addActionListener(this);
        MI23.addActionListener(this);
        MI24.addActionListener(this);
        MI25.addActionListener(this);
        M2.add(MI21);
        M2.add(MI22);
        M2.add(MI23);
        M2.add(MI24);
        M2.add(MI25);
        MB.add(M2);
        JMenu M3 = new JMenu("Insert");
        JMenuItem MI31 = new JMenuItem("Special chars");
        MI31.addActionListener(this);
        M3.add(MI31);
        MB.add(M3);
        JMenu M4 = new JMenu("View");
        JMenuItem MI41 = new JMenuItem("Fullscreen");
        MI41.addActionListener(this);
        M4.add(MI41);
        MB.add(M4);

        JMenuBar count = new JMenuBar();

        count.add(word_count);
        count.add(char_count);
        F.add(count, BorderLayout.SOUTH);

        F.setJMenuBar(MB);
        F.setSize(600, 500);
        F.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "New file":
                T.setText("");
                currentfilepath = "";
                break;
            case "Open file":
                openFile();
                break;
            case "Save file":
                saveFile();
                break;
            case "Save as file...":
                saveAs();
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Cut":
                T.cut();
                break;
            case "Copy":
                T.copy();
                break;
            case "Paste":
                T.paste();
                break;
            case "Select all":
                T.selectAll();
                break;
            case "Find and Replace":
                findReplace();
                break;
            case "Special chars":
                specialCharacter();
                break;
            case "Fullscreen":
                Fullscreen();
                break;
        }
    }

    private void openFile() {
        JFileChooser FC = new JFileChooser("E:\\");
        FileNameExtensionFilter FNEF = new FileNameExtensionFilter("txt and code file", "txt", "java", "cpp", "c");
        FC.addChoosableFileFilter(FNEF);
        if (FC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = FC.getSelectedFile();
                currentfilepath = f.getAbsolutePath();
                FileReader ff = new FileReader(f);
                BufferedReader br = new BufferedReader(ff, 65536);
                T.read(br, null);
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        handleKeyAction();
    }

    private void saveFile() {
        if (!currentfilepath.isEmpty()) {
            try {
                FileWriter fw = new FileWriter(currentfilepath);
                T.write(fw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            saveAs();
        }
    }

    private void saveAs() {
        JFileChooser fc = new JFileChooser("E:\\");
        int isopen = fc.showOpenDialog(null);
        if (isopen == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            currentfilepath = f.getAbsolutePath();
            try {
                FileWriter fw = new FileWriter(currentfilepath);
                T.write(fw);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void Fullscreen() {
        try {
            FullscreenView = true;
            if (FullscreenView) {
                F.setExtendedState(JFrame.MAXIMIZED_BOTH);
                F.setUndecorated(true);
                FullscreenView = false;
                F.setVisible(true);
            }
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    private void findReplace() {
        try {
            JTextField find = new JTextField(20);
            JTextField replace = new JTextField(20);
            JCheckBox matchCaseCheck = new JCheckBox("Match case");

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel("Find:"));
            panel.add(find);
            panel.add(new JLabel("Replace with:"));
            panel.add(replace);
            panel.add(matchCaseCheck);

            int option = JOptionPane.showConfirmDialog(F, panel, "Find and Replace", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String findText = find.getText();
                String replaceText = replace.getText();

                if (findText != null && !findText.isEmpty()) {
                    String text = T.getText();

                    if (matchCaseCheck.isSelected()) {
                        text = text.replace(findText, replaceText);
                    } else {
                        // case-insensitive replacement
                        text = text.toLowerCase().replace(findText.toLowerCase(), replaceText);
                    }

                    T.setText(text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        handleKeyAction();
    }

    private void specialCharacter() {
        String[] Chars = {
                "¢", "€", "£", "¥", "©", "®", "™", "µ", "ß", "«", "»", "‘", "’",
                "“", "”", "≤", "≥", "¯", "‾", "¤", "¦", "¨", "¡", "¿", "ˆ", "˜",
                "°", "±", "×", "ƒ", "∫", "∑", "∞", "√", "∼", "≅", "≈", "≠", "≡",
                "∈", "∉", "∋", "∏", "∧", "∨", "¬", "∩", "∪", "∂", "∀", "∃", "∅",
                "∇", "∝", "∠", "º", "†", "‡", "æ", "ç", "ð", "ø", "ō", "œ", "þ",
                "Δ", "Θ", "Λ", "Ξ", "Π", "Σ", "Φ", "Ψ", "Ω", "α", "β", "γ", "δ",
                "η", "θ", "ι", "κ", "λ", "π", "ρ", "σ", "τ", "φ", "χ", "ψ", "ω",
                "ϒ", "←", "↑", "→", "↓", "↔", "↵", "⇐", "⇑", "⇒", "⇓", "⇔", "∴",
                "⊂", "⊃", "⊄", "⊆", "⊇", "⊕", "⊗", "⊥", "⌈", "⌉", "⌊", "⌋",
                "Γ", "ε", "◊", "♠", "♣", "♥", "♦"
        };

        JPanel panel = new JPanel(new GridLayout(0, 10));// GridLayout is a layout manager that arranges components in a
                                                         // rectangular //0 er jonne row dynamically allocation hobe but
                                                         // col 10 fix
        for (String specialChar : Chars) {
            JButton button = new JButton(specialChar);// add button for each char
            button.addActionListener(new ActionListener() {// add actionlistener for each button
                public void actionPerformed(ActionEvent e) {
                    int caretPosition = T.getCaretPosition();
                    try {
                        T.getDocument().insertString(caretPosition, specialChar, null);// null means no additional
                                                                                       // formatting for the inserting.
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            panel.add(button);

        }

        JScrollPane scrollPane = new JScrollPane(panel);
        JOptionPane.showMessageDialog(F, scrollPane, "Select Special Character", JOptionPane.PLAIN_MESSAGE);
        handleKeyAction();
    }

    public void keyReleased(KeyEvent ke) {
        handleKeyAction();
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {
        if (pm != null && pm.isVisible()) {
            if (ke.getKeyCode() != KeyEvent.VK_UP && ke.getKeyCode() != KeyEvent.VK_DOWN
                    && ke.getKeyCode() != KeyEvent.VK_ENTER) {
                pm.setVisible(false);
                pm.removeAll();
                handleKeyAction();
            } else {

                pm.requestFocusInWindow();
            }
        }

        handleKeyAction();
        Prediction(ke);
        Counter();
    }

    private void Prediction(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_SPACE || ke.getKeyCode() == KeyEvent.VK_ENTER) {
            String allText = T.getText();
            String[] sentence = allText.split("[.!?]\\s*");
            String s = sentence[sentence.length - 1];

            ArrayList<Map.Entry<String, Integer>> top1_3 = p.predictNextWord(s);

            pm = new JPopupMenu();

            for (int i = 0; i < top1_3.size(); i++) {
                Map.Entry<String, Integer> entry = top1_3.get(i);
                JMenuItem jme = new JMenuItem(entry.getKey());
                if (entry.getKey() != null) {

                    jme.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String updateText = T.getText() + "" + entry.getKey();
                            T.setText(updateText);
                            T.setCaretPosition(updateText.length());
                        }
                    });

                    pm.add(jme);
                }
            }

            Point caretPosition = T.getCaret().getMagicCaretPosition();
            if (caretPosition != null) {
                pm.show(T, caretPosition.x, caretPosition.y);
            }
            T.requestFocusInWindow();
        }
    }

    private void handleKeyAction() {
        StyledDocument doc = T.getStyledDocument();
        String text;
        try {
            text = doc.getText(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
            return;
        }

        if (!text.equals(lastText)) {
            removeHighlight();
            String[] words = text.replaceAll("[,.:;!?]", " ").split("\\s+");
            for (String word : words) {
                if (!word.isEmpty()) {
                    int h = sc.checkword(word.toLowerCase());
                    if (h == 0) {
                        highlightWord(word);
                    }
                }
            }
            lastText = text;
        }
    }

    public void highlightWord(String word) {

        try {
            StyledDocument doc = T.getStyledDocument();
            String text = doc.getText(0, doc.getLength());
            int start = text.indexOf(word);

            Style highlightStyle = doc.addStyle("HighlightStyle", null);
            StyleConstants.setForeground(highlightStyle, Color.RED);

            while (start >= 0) {
                int end = start + word.length();
                doc.setCharacterAttributes(start, word.length(), highlightStyle, false);
                start = text.indexOf(word, end);
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void removeHighlight() {
        try {
            StyledDocument doc = T.getStyledDocument();
            Style normalStyle = doc.addStyle("NormalStyle", null);
            StyleConstants.setForeground(normalStyle, Color.BLACK);
            doc.setCharacterAttributes(0, doc.getLength(), normalStyle, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mouseClicked(MouseEvent me) {
        pm = new JPopupMenu();
        int clickPos = T.viewToModel2D(me.getPoint());
        try {
           
            int start = Utilities.getWordStart(T, clickPos);// starting index of the word
            int end = Utilities.getWordEnd(T, clickPos);// end
            clickedWord = T.getDocument().getText(start, end - start);
            if (!clickedWord.isEmpty() && sc.checkword(clickedWord) != 1) {
                sug_Word = sc.suggest_word(clickedWord);
                pm.removeAll();
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        if (me.getButton() == MouseEvent.BUTTON3) {
            if (!sug_Word.isEmpty()) {
                JMenuItem jme = new JMenuItem(sug_Word);
                jme.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        replaceWord(clickedWord, sug_Word);
                    }
                });
                pm.add(jme);
                pm.show(T, me.getX(), me.getY());
            }
        }
        Counter();
    }

    private void replaceWord(String oldWord, String newWord) {
        try {
            int caret_Position = T.getCaretPosition();
            String text = T.getText();
            String updatedText = text.replace(oldWord, newWord);
            T.setText(updatedText);
            // Restore the caret to its original position//if else na korle update korar por
            // last a cholay jay
            if (caret_Position < updatedText.length()) {
                T.setCaretPosition(caret_Position);
            } else {
                T.setCaretPosition(updatedText.length());
            }
            handleKeyAction();
            sug_Word = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {
    }

    public void mouseReleased(MouseEvent me) {
    }

    private void Counter() {
        String text = T.getText();
        int Char = text.length();
        int word = 0;
        if (!text.trim().isEmpty()) {
            word = text.split("\\s+").length;
        }

        char_count.setText("Character: " + Char);
        word_count.setText("Word: " + word);
    }

}

public class SPL1 {
    public static void main(String[] args) {
        Design_implement D = new Design_implement();
    }
}
