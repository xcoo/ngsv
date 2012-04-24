/*
 *   ngsv
 *   https://github.com/xcoo/ngsv
 *   Copyright (C) 2012, Xcoo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package genome.view;

import genome.data.Bed;
import genome.data.Sam;
import genome.db.Position;
import genome.db.SQLLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SamSelectionDialogBox extends JFrame implements ChangeListener, ActionListener {

    private static final long serialVersionUID = 1L;

    private List<JCheckBox> cbSamArray = new ArrayList<JCheckBox>();
    private List<JCheckBox> cbBedArray = new ArrayList<JCheckBox>();

    private Sam[] samFiles = null;
    private Bed[] bedFiles = null;

    private String chrName = null;
    private long start = 0;
    private long end = 900000;

    private Map<String, Long> chrLengthMap;

    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;

    private static final int CHECK_BOX_HEIGHT = 32;
    private static final int COMBOBOX_HEIGHT = 32;
    private static final int TEXTFIELD_HEIGHT = 32;

    private JComboBox chrBox = null;
    private JTextField startText, endText, searchText;
    private JLabel error;

    private static final long MAX_LENGTH = 5000000;

    interface SamSelectionDialongBoxListener {
        void finishedSamSelection();
    }

    private SamSelectionDialongBoxListener listener = null;

    public SamSelectionDialogBox(Sam[] samFiles, Bed[] bedFiles, Map<String, Long> chrLengthMap, final SQLLoader sqlLoader, SamSelectionDialongBoxListener listener) {

        this.samFiles = samFiles;
        this.bedFiles = bedFiles;

        this.setChrLengthMap(chrLengthMap);

        this.listener = listener;

        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(panel, BorderLayout.CENTER);

        panel.add( createSamFilePanel(samFiles) );
        panel.add( createBedFilePanel(bedFiles) );

        panel.add( createViewScalePanel(chrLengthMap) );
        
        panel.add( createGeneSearchPanel() );

        error = new JLabel("");
        error.setForeground(new Color(1.0f, 0.0f, 0.0f));
        error.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
        error.setMinimumSize(new Dimension(Short.MAX_VALUE, 20));
        error.setHorizontalAlignment(JLabel.CENTER);
        panel.add( error );

        JButton ok = new JButton("OK");

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                String chrName = (String) chrBox.getSelectedItem();
                setChrName( chrName );

                long start = 0, end = 0, length = 0, offset = 0;

                if(!searchText.getText().equalsIgnoreCase("")){
                    try{
                        Position pos = sqlLoader.getGenePosition(SQLEscape(searchText.getText()));
                        setChrName(pos.getChrName());
                        offset = (pos.getEnd() - pos.getStart()) * 2;
                        chrBox.setSelectedItem(pos.getChrName());
                        startText.setText(String.valueOf(pos.getStart()-offset));
                        endText.setText(String.valueOf(pos.getEnd()+offset));                    
                    }catch (Exception ex) {
                        error.setText("can not be found the input gene");
                        return;
                    }
                }

                try{
                    start = validateStart( chrName, Long.valueOf( startText.getText() ) );
                    end = validateEnd( chrName, Long.valueOf( endText.getText() ) );
                }catch (Exception ex) {
                    error.setText("over floaw the input value");
                    return;
                }

                if( start >= end ) {
                    error.setText("[start] should be smaller than [end]");
                    return;
                }

                try{
                    length = Math.abs(end - start);
                }catch (Exception ex) {
                    error.setText("over floaw the input [length]");
                    return;
                }

                if( length > MAX_LENGTH  ) {
                    error.setText("over flow the input length");
                } else if ( length == 0 ) {
                    error.setText("[length] should be greater than 0");
                } else {
                    setStart(start);
                    setEnd(end);

                    finish();
                    error.setText("");
                }
            }
        });

        getContentPane().add(ok, BorderLayout.SOUTH);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Select Sam and Bed Files");
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    private long validateStart(String chrName, long val) {

        if( val < 0 ) {
            return 0;
        }

        final long MAX = chrLengthMap.get(chrName);

        if( MAX < val ) {
            return MAX;
        }

        return val;
    }

    private long validateEnd(String chrName, long val) {

        if( val < 0 ) {
            return 0;
        }

        final long MAX = chrLengthMap.get(chrName);

        if( MAX < val ) {
            return MAX;
        }

        return val;
    }

    private void finish() {
        if( this.listener != null ) {
            this.listener.finishedSamSelection();
        }
    }

    private JPanel createSamFilePanel(Sam[] samFiles) {

        JPanel panel = new JPanel(); 
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        int i = 0;
        for (Sam s : this.samFiles) {
            JCheckBox cb = new JCheckBox(s.getFileName());
            if (i == 0) {
                cb.setSelected(true);
                s.setSelected(true);
            } else {
                s.setSelected(false);
            }
            cb.addChangeListener(this);
            cb.setMaximumSize(new Dimension(Short.MAX_VALUE, CHECK_BOX_HEIGHT));
            panel.add(cb);
            cbSamArray.add(cb);
            i++;
        }

        panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        return panel;
    }

    private JPanel createBedFilePanel(Bed[] bedFiles){

        JPanel panel = new JPanel(); 
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        int i = 0;
        for (Bed b : this.bedFiles) {
            JCheckBox cb = new JCheckBox(b.getFileName());
            if (i == 0) {
                cb.setSelected(true);
                b.setSelected(true);
            } else {
                b.setSelected(false);
            }
            cb.addChangeListener(this);
            cb.setMaximumSize(new Dimension(Short.MAX_VALUE, CHECK_BOX_HEIGHT));
            panel.add(cb);
            cbBedArray.add(cb);
            i++;
        }

        panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        return panel;
    }

    private JPanel createViewScalePanel(Map<String, Long> chrLengthMap) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        DefaultComboBoxModel chrModel = new DefaultComboBoxModel();

        List<String> chrs = new LinkedList<String>(chrLengthMap.keySet());

        Collections.sort(chrs, new Comparator<String>() {

            public int compare(String o1, String o2) {

                String c1 = o1.replace("chr", "");
                String c2 = o2.replace("chr", "");

                int i1 = 0, i2 = 0;

                try{
                    i1 = Integer.valueOf(c1);
                } catch (Exception e) {
                    i1 = Integer.MAX_VALUE;
                }

                try{
                    i2 = Integer.valueOf(c2);
                } catch (Exception e) {
                    i2 = Integer.MAX_VALUE;
                }

                if( i1 < Integer.MAX_VALUE && i2 < Integer.MAX_VALUE ) {
                    return i1 - i2;
                } else if ( i1 < Integer.MAX_VALUE ) {
                    return -1;
                } else if ( i2 < Integer.MAX_VALUE ) {
                    return 1;
                } else {
                    return c1.compareTo(c2);
                }
            }

        });

        this.chrName = chrs.get(0);

        for( String c : chrs ) {
            chrModel.addElement(c);
        }

        chrBox = new JComboBox(chrModel);
        chrBox.setMinimumSize(new Dimension(100, COMBOBOX_HEIGHT));
        chrBox.setMaximumSize(new Dimension(100, COMBOBOX_HEIGHT));
        chrBox.setAlignmentX(0);
        chrBox.setSelectedItem(chrName);
        panel.add(chrBox);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));

        startText = new JTextField(start + "");
        startText.setMinimumSize(new Dimension(300, TEXTFIELD_HEIGHT));
        startText.setMaximumSize(new Dimension(300, TEXTFIELD_HEIGHT));
        startText.setAlignmentX(0);
        startText.setMargin(new Insets(0, 0, 0, 0));
        startText.setHorizontalAlignment(JTextField.RIGHT);

        endText = new JTextField(end + "");
        endText.setMinimumSize(new Dimension(300, TEXTFIELD_HEIGHT));
        endText.setMaximumSize(new Dimension(300, TEXTFIELD_HEIGHT));
        endText.setAlignmentX(0);
        endText.setMargin(new Insets(0, 0, 0, 0));
        endText.setHorizontalAlignment(JTextField.RIGHT);

        innerPanel.add(startText);
        innerPanel.add(endText);

        innerPanel.setAlignmentX(0);

        panel.add(innerPanel);

        panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        return panel;
    }


    private JPanel createGeneSearchPanel(){

        JPanel panel = new JPanel(); 
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Search by gene name (optional): ");
        searchText = new JTextField("");
        searchText.setMinimumSize(new Dimension(240, TEXTFIELD_HEIGHT));
        searchText.setMaximumSize(new Dimension(240, TEXTFIELD_HEIGHT));
        searchText.setAlignmentX(0);
        searchText.setMargin(new Insets(0, 0, 0, 0));
        searchText.setHorizontalAlignment(JTextField.LEFT);

        panel.add(label);
        panel.add(searchText);
        panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        return panel;
    }

    // checking checkbox event
    public void stateChanged(ChangeEvent e) {
        JCheckBox cb = (JCheckBox)e.getSource();

        int i = 0;
        for(JCheckBox c : cbSamArray){
            if(cb == c){
                if(cb.isSelected()){
                    this.samFiles[i].setSelected(true);
                }else{
                    this.samFiles[i].setSelected(false);
                }
            }
            i++;
        }
        i = 0;

        for(JCheckBox c : cbBedArray){
            if(cb == c){
                if(cb.isSelected()){
                    this.bedFiles[i].setSelected(true);
                }else{
                    this.bedFiles[i].setSelected(false);
                }
            }
            i++;
        }
    }

    public String getChrName() {
        return chrName.replace("chr", "");
    }

    public void setChrName(String chrName) {
        this.chrName = chrName;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Map<String, Long> getChrLengthMap() {
        return chrLengthMap;
    }

    public void setChrLengthMap(Map<String, Long> chrLengthMap) {
        this.chrLengthMap = chrLengthMap;
    }

    public void actionPerformed(ActionEvent e) {

    }
    
    /**
     * SQL escape sequence process
     * ' -> ''
     * \ -> \\
     *
     * @param input characters
     * @return output characters after the process
     */
    static public String SQLEscape(String input) {
        input = substitute(input, "'", "''");
        input = substitute(input, "\\", "\\\\");
        return input;
    }
    
    /**
     * Replacement of characters
     *
     * @param input characters
     * @param pattern characters before replacement
     * @oaram replacement characters after replacement
     * @return output characters
     */
    static public String substitute(String input, String pattern, String replacement) {
        int index = input.indexOf(pattern);

        if(index == -1) {
            return input;
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append(input.substring(0, index) + replacement);

        if(index + pattern.length() < input.length()) {
            String rest = input.substring(index + pattern.length(), input.length());
            buffer.append(substitute(rest, pattern, replacement));
        }
        return buffer.toString();
    }
}
