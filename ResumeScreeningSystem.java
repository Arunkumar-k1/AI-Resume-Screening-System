import org.apache.pdfbox.Loader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ResumeScreeningSystem extends JFrame implements ActionListener {

    JTextArea jobArea;
    JButton uploadButton, calculateButton;
    JLabel resultLabel;

    String resumeText = "";

    ResumeScreeningSystem() {

    setTitle("AI Resume Screening System");
    setSize(700, 450);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));

    // Top Panel
    JPanel topPanel = new JPanel();

    uploadButton = new JButton("Upload Resume PDF");

    topPanel.add(uploadButton);

    // Center Panel
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BorderLayout());

    JLabel jobLabel = new JLabel("Enter Job Description:");
    jobLabel.setFont(new Font("Arial", Font.BOLD, 16));

    jobArea = new JTextArea(8, 40);

    JScrollPane scrollPane = new JScrollPane(jobArea);

    centerPanel.add(jobLabel, BorderLayout.NORTH);
    centerPanel.add(scrollPane, BorderLayout.CENTER);

    // Bottom Panel
    JPanel bottomPanel = new JPanel();

    calculateButton = new JButton("Calculate ATS Score");

    resultLabel = new JLabel("ATS Score: ");

    bottomPanel.add(calculateButton);
    bottomPanel.add(resultLabel);

    // Add Panels
    add(topPanel, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);

    // Actions
    uploadButton.addActionListener(this);
    calculateButton.addActionListener(this);

    setVisible(true);
}

    

       

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == uploadButton) {

            JFileChooser fileChooser = new JFileChooser();

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {

                File selectedFile = fileChooser.getSelectedFile();

                try {
                    resumeText = extractTextFromPDF(selectedFile);
                    JOptionPane.showMessageDialog(this, "Resume Uploaded Successfully!");

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error Reading PDF!");
                }
            }
        }

        if (e.getSource() == calculateButton) {

            String jobText = jobArea.getText().toLowerCase();

            String[] jobKeywords = jobText.split("\\W+");

            int matchedKeywords = 0;

            Set<String> uniqueKeywords = new HashSet<>();

            for (String word : jobKeywords) {

                if (word.length() > 2) {
                    uniqueKeywords.add(word);
                }
            }

            for (String keyword : uniqueKeywords) {

                if (resumeText.toLowerCase().contains(keyword)) {
                    matchedKeywords++;
                }
            }

            double score = ((double) matchedKeywords / uniqueKeywords.size()) * 100;

            resultLabel.setText("ATS Match Score: " + String.format("%.2f", score) + "%");
        }
    }

    // PDF Text Extraction
    private String extractTextFromPDF(File file) throws IOException {

        PDDocument document = Loader.loadPDF(file);

        PDFTextStripper pdfStripper = new PDFTextStripper();

        String text = pdfStripper.getText(document);

        document.close();

        return text;
    }

    public static void main(String[] args) {

        new ResumeScreeningSystem();
    }
}