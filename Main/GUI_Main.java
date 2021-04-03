package Main;
import Huffman.HuffCompress;
import LZW.LZWCompress;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GUI_Main extends JFrame implements ActionListener {

    public static File opened_file, other_file;
    static long past, future;
    static JLabel redLabel, blueLabel, redScore, blueScore;
    static JPanel buttonPanel, titlePanel, scorePanel;
    static JButton ZH, ZL;

    public JPanel createContentPane() {
        //bottom JPanel
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);

        titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setLocation(90, 20);
        titlePanel.setSize(170, 70);
        totalGUI.add(titlePanel);

        redLabel = new JLabel("File size (before): ");
        redLabel.setLocation(43, 0);
        redLabel.setSize(150, 30);
        redLabel.setHorizontalAlignment(0);
        titlePanel.add(redLabel);

        blueLabel = new JLabel("File size (after): ");
        blueLabel.setLocation(39, 30);
        blueLabel.setSize(170, 30);
        blueLabel.setHorizontalAlignment(0);
        titlePanel.add(blueLabel);

        scorePanel = new JPanel();
        scorePanel.setLayout(null);
        scorePanel.setLocation(270, 20);
        scorePanel.setSize(120, 60);
        totalGUI.add(scorePanel);

        redScore = new JLabel("");
        redScore.setLocation(0, 0);
        redScore.setSize(100, 30);
        redScore.setHorizontalAlignment(0);
        scorePanel.add(redScore);

        blueScore = new JLabel("");
        blueScore.setLocation(0, 30);
        blueScore.setSize(100, 30);
        blueScore.setHorizontalAlignment(0);
        scorePanel.add(blueScore);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setLocation(10, 130);
        buttonPanel.setSize(5200, 150);
        totalGUI.add(buttonPanel);

        ZH = new JButton("H_Compress");
        ZH.setLocation(0, 0);
        ZH.setSize(190, 30);
        ZH.addActionListener(this);
        buttonPanel.add(ZH);


        ZL = new JButton("LZW_Compress");
        ZL.setLocation(340, 0);
        ZL.setSize(190, 30);
        ZL.addActionListener(this);
        buttonPanel.add(ZL);


        totalGUI.setOpaque(true);
        return totalGUI;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ZH) {
            HuffCompress.huffCompressing(opened_file.getPath());
            JOptionPane.showMessageDialog(null, "File compressed with Huffman Compression.",
                    "Status", JOptionPane.PLAIN_MESSAGE);
            redScore.setText(opened_file.length() + "Bytes");
            other_file = new File(opened_file.getPath() + ".huff");
            future = other_file.length();
            blueScore.setText(future + "Bytes");
        }
         else if (e.getSource() == ZL) {
            LZWCompress.LZWCompressing(opened_file.getPath());
            JOptionPane.showMessageDialog(null, "File compressed with LZW Compression.",
                    "Status", JOptionPane.PLAIN_MESSAGE);
            redScore.setText(opened_file.length() + "Bytes");
            other_file = new File(opened_file.getPath() + ".lzw");
            future = other_file.length();
            blueScore.setText(future + "Bytes");
        }

    }

    private static void GUI() {


        JFrame frame = new JFrame("COMPRESSER LZW & HUFFMANN");


        GUI_Main demo = new GUI_Main();
        frame.setContentPane(demo.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(350, 170, 550, 300);

        frame.setVisible(true);

        JMenu fileMenu = new JMenu("File");
        JMenuBar bar = new JMenuBar();
        frame.setJMenuBar(bar);
        bar.add(fileMenu);

        JMenuItem openItem = new JMenuItem("Select file");
        fileMenu.add(openItem);
        openItem.addActionListener(

                new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        int result = fileChooser.showOpenDialog(null);
                        opened_file = fileChooser.getSelectedFile();
                        past = opened_file.length();
                        redScore.setText(past + "Bytes");
                        blueScore.setText("NotYetCalculated"); } } );
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI();
            }
        });
    }
}