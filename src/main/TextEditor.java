package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class TextEditorModel {
	private List<String> lines;

	public TextEditorModel() {
		lines = new ArrayList<>();
	}

	public void addLine(String line) {
		lines.add(line);
	}

	public List<String> getLines() {
		return lines;
	}

	public void loadFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();
	}

	public void saveToFile(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (String line : lines) {
			writer.write(line);
			writer.newLine();
		}
		writer.close();
	}

	public void clear() {
		lines.clear();
	}
}

class TextEditorView extends JFrame {
	private JTextArea textArea;
	private JButton saveButton;
	private JButton loadButton;
	private JButton clearButton;

	public TextEditorView() {
		setTitle("Text Editor");
		setSize(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		saveButton = new JButton("Save");
		loadButton = new JButton("Load");
		clearButton = new JButton("Clear");
		buttonPanel.add(saveButton);
		buttonPanel.add(loadButton);
		buttonPanel.add(clearButton);
		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	public void setSaveButtonListener(ActionListener listener) {
		saveButton.addActionListener(listener);
	}

	public void setLoadButtonListener(ActionListener listener) {
		loadButton.addActionListener(listener);
	}

	public void setClearButtonListener(ActionListener listener) {
		clearButton.addActionListener(listener);
	}

	public String getText() {
		return textArea.getText();
	}

	public void setText(String text) {
		textArea.setText(text);
	}
}

class TextEditorController {
	private TextEditorModel model;
	private TextEditorView view;

	public TextEditorController(TextEditorModel model, TextEditorView view) {
		this.model = model;
		this.view = view;

		view.setSaveButtonListener(new SaveButtonListener());
		view.setLoadButtonListener(new LoadButtonListener());
		view.setClearButtonListener(new ClearButtonListener());
	}

	class SaveButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showSaveDialog(view);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					model.saveToFile(file);
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(view, "Error saving file!");
				}
			}
		}
	}

	class LoadButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(view);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					model.loadFile(file);
					updateView();
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(view, "Error loading file!");
				}
			}
		}
	}

	class ClearButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			model.clear();
			updateView();
		}
	}

	private void updateView() {
		StringBuilder sb = new StringBuilder();
		for (String line : model.getLines()) {
			sb.append(line).append("\n");
		}
		view.setText(sb.toString());
	}
}

public class TextEditor {
	public static void main(String[] args) {
		TextEditorModel model = new TextEditorModel();
		TextEditorView view = new TextEditorView();
		TextEditorController controller = new TextEditorController(model, view);
	}
}
