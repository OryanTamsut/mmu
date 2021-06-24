package com.mbj.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CacheUnitView extends JPanel implements ActionListener {
    private final PropertyChangeSupport observer;
    private final JTextArea input;
    private String commandNowType;



    public CacheUnitView() {
        this.observer = new PropertyChangeSupport(this);
        input = new JTextArea(10, 30);
        this.commandNowType = "";
    }

    /**
     * add PropertyChangeListener to the observer
     *
     * @param pcl PropertyChangeListener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        observer.addPropertyChangeListener(pcl);
    }

    /**
     * remove PropertyChangeListener from the observer
     *
     * @param pcl PropertyChangeListener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        observer.removePropertyChangeListener(pcl);
    }

    private void createAndShowGUI() {
        Color backGroundColor=new Color(203, 230, 230);
        Color buttonBackground=new Color(21, 183, 183);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        //Create and set up the window.
        JFrame frame = new JFrame("Cache Unit View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500,300));

        //Create and set up the content pane.

        JPanel jPanel = new JPanel(new FlowLayout());

        JButton loadReq = new JButton("load request");

        loadReq.setMargin(new Insets(10, 7, 10, 7));

        loadReq.setActionCommand("loadReq");
        loadReq.addActionListener(this);
        loadReq.setBackground(buttonBackground);
        loadReq.setForeground(Color.WHITE);

        jPanel.add(loadReq);

        JButton showStat = new JButton("Show Statistics");

        showStat.setActionCommand("showStat");

        showStat.setMargin(new Insets(10, 7, 10, 7));

        showStat.addActionListener(this);
        showStat.setBackground(buttonBackground);
        showStat.setForeground(Color.WHITE);

        jPanel.add(showStat);

        JButton addReq = new JButton("add new request");

        addReq.setMargin(new Insets(10, 7, 10, 7));

        addReq.setActionCommand("addReq");
        addReq.addActionListener(this);
        addReq.setBackground(buttonBackground);
        addReq.setForeground(Color.WHITE);

        jPanel.add(addReq);

        jPanel.setBackground(backGroundColor);

        frame.add(jPanel, BorderLayout.PAGE_START);

        input.setVisible(true);
        input.setEditable(false);
        input.setColumns(40);
        input.setMargin(new Insets(10,10,10,10));

        frame.add(input, BorderLayout.PAGE_END);
        frame.getContentPane().setBackground(backGroundColor );
        JPanel contentPanel = (JPanel) frame.getContentPane();
        contentPanel.setBorder(padding);
        frame.setContentPane(contentPanel);

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void start() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("loadReq".equals(e.getActionCommand())) {
            this.commandNowType = "loadReq";
            loadRequestGui();
        } else if ("showStat".equals(e.getActionCommand())){
            this.commandNowType = "show";
            observer.firePropertyChange("show", 0, "{ \"headers\": { \"action\": \"STATISTICS\" } }");
        }
        else if("addReq".equals(e.getActionCommand())){
            this.commandNowType = "addReq";
            addRequestGui();
        }
    }

    public void addRequestAction(String req){
        this.commandNowType = "show";
        observer.firePropertyChange("show", 0, req);
    }

    public <T> void updateUIData(T t) {
        String text=((String)t);
        if (commandNowType.equals("show")) {
            if(text.charAt(0)=='\"') {
                text = text.substring(1,text.length()-1);
            }
        }
        String[] textRows = text.split(";");
        input.setText("");
        for (String textRow : textRows) {
            input.append(textRow + "\n");
        }
    }

    private void loadRequestGui() {

        //Create and set up the content pane.
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {
            // handle exception
            System.out.println(e.getMessage());
        }
        JFileChooser fc = new JFileChooser(new File("./src/main/resources/clientResource"));
        int returnVal = fc.showOpenDialog(CacheUnitView.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = new File(fc.getSelectedFile().getPath());
            Scanner sc = null;
            StringBuilder request = new StringBuilder();
            try {
                sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    request.append(sc.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("error: " + e.getMessage());
            }

            observer.firePropertyChange("load", 0, request.toString());
        }
    }

    private void addRequestGui(){
        AddRequest addRequest=new AddRequest(this);
        addRequest.start();
    }
}