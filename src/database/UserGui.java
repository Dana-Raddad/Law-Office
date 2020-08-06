/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.beans.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

@SuppressWarnings("serial")
public class UserGui extends JPanel implements ActionListener, PropertyChangeListener {
    static UserGui currentObj; 
    private JProgressBar progressBar;
    private JButton generateButton;
    private JButton exceptionButton;
    private JTextArea taskOutput;
    private Task task;
    DefaultTableModel model;
    JTable table;
    JScrollPane spane;
    int currentRow=-1;

    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            generateButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            taskOutput.append("Done!\n");
        }
    }

    public UserGui() {
        // super(null);

        JLabel headerLabel = new JLabel("", JLabel.LEADING); 
        headerLabel.setText("TEST APPLICATION");  
        headerLabel.setFont(new Font("Calibri",Font.ITALIC,25));

        //Create the demo's UI.
        generateButton = new JButton("button1");
        generateButton.setActionCommand("generate");
        generateButton.addActionListener(this);


        exceptionButton = new JButton("button2");
        exceptionButton.setActionCommand("exception");
        exceptionButton.addActionListener(this);
        // exceptionButton.setBounds(50, 300, 150, 30);

        progressBar = new JProgressBar(0, 100);
        // progressBar.setBounds(0, 0, HEIGHT, WIDTH);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        //progressBar.setBounds(x, y, width, height)

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        //LABEL PANEL
        JPanel labelpanel = new JPanel();
        //labelpanel.setBounds(0, 0, WIDTH, 80);
        labelpanel.setLayout(null);      
        ImageIcon icon = new ImageIcon("logo.jpg","hiii");
        JLabel imgl=new JLabel(icon);
        labelpanel.add(imgl);
        labelpanel.add(headerLabel);
        imgl.setBounds(10,10, 150, 70);
        headerLabel.setBounds(200, 0, 400, 70);
        //labelpanel.setBorder((BorderFactory.createEmptyBorder(200, 200, 200, 200)));

        //BUTTON PANEL
        JPanel buttonpanal=new JPanel();

        buttonpanal.setLayout(null);
        buttonpanal.add(generateButton);
        buttonpanal.add(exceptionButton);
        Dimension d1=generateButton.getPreferredSize();
        System.out.println(d1);
        generateButton.setBounds(10,10,133,26);
        exceptionButton.setBounds(10,66,133,26);


        //SCROLL PANE
        model=new DefaultTableModel();
        table=new JTable(model);
        model.addColumn("ID");
        model.addColumn("IEC");
        model.addColumn("IFSC");
        model.addColumn("SHB_NO");
        model.addColumn("STATUS");

        table.setFillsViewportHeight(true);
        spane=new JScrollPane(table);

        //PROGERSS BAR
        JPanel progbar=new JPanel();
        progbar.add(progressBar);
        //  progressBar.setBounds(x, y, width, height)

        //ADD PANELS TO JFRAME
        add(labelpanel);
        labelpanel.setBounds(0,0,600,100);

        add(buttonpanal);
        buttonpanal.setBounds(0,100,160,300);

        add(spane);
        spane.setBounds(160,110, 500, 300);


        progbar.setBackground(new Color(153,153,255));
        labelpanel.setBackground(new Color(153,153,255));
        buttonpanal.setBackground(new Color(153,153,255));
        headerLabel.setBackground(new Color(153,153,255));
        progressBar.setBackground(new Color(153,153,255));
    }

    public void adddata(Object[] data){
        if(model!=null){

            model.addRow(data);
            currentRow++;
        }
    }

    public void updateStatus(String status){
        if(table!=null){
            System.out.println("...currentRow:"+currentRow);
            table.setValueAt(status, currentRow, 4);

            System.out.println("out of......");
        }
    }

    /**
    * Invoked when the user presses the start button.
    */
    public void actionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        currentObj=this;      
        System.out.println("in action......");
        Properties prop = new Properties();
        InputStream input = null;

        setCursor(Cursor.getDefaultCursor());
    }

    /**
    * Invoked when task's progress property changes.
    */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            taskOutput.append(String.format(
                    "Completed %d%% of task.\n", task.getProgress()));
        } 
    }

    /**
    * Create the GUI and show it. As with all GUI code, this must run
    * on the event-dispatching thread.
    */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ProgressBarDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create and set up the content pane.
        JComponent newContentPane = new UserGui();
        newContentPane.setOpaque(true); //content panes must be opaque
        newContentPane.setLayout(null);
        frame.setContentPane(newContentPane);
        frame.setSize(700, 500);
        //frame.setLayout(null);
        //Display the window.
        //frame.pack();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setBackground(new Color(153,153,255));
        newContentPane.setBackground(new Color(153,153,255));
        }
        public static void main(String[] args)throws Exception {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}