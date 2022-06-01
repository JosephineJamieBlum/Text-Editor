package com.mycompany.texteditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class PrimaryController {

    @FXML
    TextArea editor;
    public Stack<String> undo;
    public Stack<String> redo;

    /**
     * closes the program
     *
     * @param event
     */
    @FXML
    protected void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    /**
     * initializes variables and adds listener to editor
     */
    @FXML
    public void initialize() {
        undo = new Stack<>();
        redo = new Stack<>();
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            undo.push(newValue);

        });

    }

    /**
     * displays a filepicker
     *
     * @return the chosen file
     */
    private File filePicker() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose file");
        chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        return chooser.showOpenDialog(App.theStage);
    }

    /**
     * resets stacks
     */
    private void resetStacks() {
        undo = new Stack<>();
        redo = new Stack<>();
    }
    /**
     * opens a text file from project directory
     *
     * @param event
     */
    @FXML
    protected void open(ActionEvent event) {
        Scanner s;
        String txt;

        editor.clear();
        
        File file = filePicker();
        //loop to read rile to editor
        try {
            s = new Scanner(file);
            while (s.hasNext()) {
                txt = s.nextLine();
                editor.appendText(txt + "\n");
            }
            resetStacks();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * saves the text in editor to a text file
     *
     * @param event
     */
    @FXML
    protected void save(ActionEvent event) {
        File file = filePicker();
        try {
            FileWriter write = new FileWriter(file);
            //keeps line break
            String txt = editor.getText().replaceAll("\n", System.getProperty("line.separator"));;
            write.write(txt);
            write.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * undoes the last undo
     *
     * @param event
     */
    @FXML
    protected void redo(ActionEvent event) {
        try {
            String newTxt = redo.pop();
            //System.out.println(newTxt);
            changeEditor(newTxt);
        } catch (Exception ex) {
            //System.out.println("can't pop stack");
            return;
        }

    }

    /**
     * updates the editor's text
     *
     * @param newTxt
     */
    private void changeEditor(String newTxt) {
        editor.clear();
        //pop to get clear off undo stack
        undo.pop();
        editor.setText(newTxt);
        //pop to get set new txt off stack
        undo.pop();
    }

    /**
     * undoes the last input from textarea;
     *
     * @param event
     */
    @FXML
    protected void undo(ActionEvent event) {
        try {
            redo.push(undo.pop());
            //System.out.println(newTxt);
            
            changeEditor(undo.pop());
        } catch (Exception ex) {
           // System.out.println("can't pop stack");
            return;
        }

    }

}
