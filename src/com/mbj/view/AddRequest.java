package com.mbj.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AddRequest  extends JPanel implements ActionListener {

    private CacheUnitView view;
    private String choosedAction;
    private JTextField dmIdT;
    private JTextField dmContentT;
    private StringBuilder dmArraySB;
    private JLabel errorMessage;
    private JFrame frame;


    public AddRequest(CacheUnitView view){
        this.view=view;
        this.choosedAction="";
        this.dmArraySB=new StringBuilder();
    }

    public void start() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public void createAndShowGUI(){
        //Create and set up the content pane.
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        }
        catch (Exception e) {
            // handle exception
            e.printStackTrace();
        }
        Color backGroundColor=new Color(203, 230, 230);
        Color buttonBackground=new Color(21, 183, 183);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        //Create and set up the window.
        frame = new JFrame("Add new request");


        //Create and set up the content pane.


        JLabel dmId=new JLabel("data model id: ");
        JLabel dmContent=new JLabel("data model content: ");
        JLabel selectReq=new JLabel("select request type: ");

        dmIdT=new JTextField();
        dmIdT.setColumns(20);

        dmContentT=new JTextField();
        dmContentT.setColumns(20);

        String[] actionList = { "GET", "UPDATE", "DELETE","select request type"};

        JComboBox actionChoose = new JComboBox(actionList);
        actionChoose.setActionCommand("actionChoose");
        actionChoose.setSelectedIndex(3);
        actionChoose.addActionListener(this);


        JButton addDmToArray = new JButton("add dataModel to array");
        addDmToArray.setMargin(new Insets(10, 7, 10, 7));

        addDmToArray.setActionCommand("addDmToArray");
        addDmToArray.addActionListener(this);
        addDmToArray.setBackground(buttonBackground);
        addDmToArray.setForeground(Color.WHITE);

        JButton sendRequest = new JButton("send request");
        sendRequest.setMargin(new Insets(10, 7, 10, 7));

        sendRequest.setActionCommand("sendRequest");
        sendRequest.addActionListener(this);
        sendRequest.setBackground(buttonBackground);
        sendRequest.setForeground(Color.WHITE);

        errorMessage=new JLabel("error: empty fields ");
        errorMessage.setVisible(false);
        JPanel jPanel1 = new JPanel(new GridLayout(0,1));
        JPanel jPanel2 = new JPanel(new GridLayout(0,1));


        jPanel1.add(dmId);
        jPanel1.add(dmContent);
        jPanel1.add(new JLabel(""));
        jPanel1.add(selectReq);

        jPanel2.add(dmIdT);
        jPanel2.add(dmContentT);
        jPanel2.add(actionChoose);



        JPanel jPanel3= new JPanel();
        jPanel3.add(jPanel1,BorderLayout.CENTER);
        jPanel3.add(jPanel2,BorderLayout.PAGE_END);
        jPanel3.add(addDmToArray,BorderLayout.PAGE_END);

        JPanel allFields = new JPanel(new GridLayout(0,1));

        allFields.add(jPanel3);

        allFields.add(errorMessage);
        allFields.add(sendRequest,BorderLayout.PAGE_END);

        jPanel1.setBackground(backGroundColor);
        jPanel2.setBackground(backGroundColor);
        jPanel3.setBackground(backGroundColor);
        allFields.setBackground(backGroundColor);

        frame.add(allFields);

        frame.getContentPane().setBackground(backGroundColor );
        JPanel contentPanel = (JPanel) frame.getContentPane();
        contentPanel.setBorder(padding);
        frame.setContentPane(contentPanel);

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //set the choosed action
        if(e.getActionCommand().equals("actionChoose")){
            JComboBox cb = (JComboBox)e.getSource();
            this.choosedAction = (String)cb.getSelectedItem();
        }

        //add dm to array
        if(e.getActionCommand().equals("addDmToArray")){
            if(dmIdT.getText().equals("")|| dmContentT.getText().equals("")){
                errorMessage.setVisible(true);
            }
            else {
                errorMessage.setVisible(false);
            }
            if(dmArraySB.length()==0) {
                dmArraySB.append(" { \"id\": ").append(dmIdT.getText())
                        .append(", \"content\": \"").append(dmContentT.getText())
                        .append("\" }");
            }
            else {
                dmArraySB.append(" ,{ \"id\": ").append(dmIdT.getText())
                        .append(", \"content\": \"").append(dmContentT.getText())
                        .append("\" }");
            }
            dmIdT.setText("");
            dmContentT.setText("");
        }

        //send the request to the view
        if(e.getActionCommand().equals("sendRequest")){
            if(dmArraySB.length()==0||choosedAction.equals("")||choosedAction.equals("select request type")){
                errorMessage.setVisible(true);
            }

            else{
                StringBuilder request=new StringBuilder();
                request.append("{\n" + "  \"headers\": { \"action\": \"").append(this.choosedAction).append("\" },\n").append("  \"body\": [");
                request.append(dmArraySB);
                request.append("]\n" +
                        "}");
                view.addRequestAction(request.toString());
                frame.setVisible(false);
            }
        }
    }


}
