/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author WAWA ESON
 */
public class EsonDatePicker extends javax.swing.JPanel {

    /**
     * Creates new form EsonDatePicker
     */
    private JLabel[] DAYS = null, WEEKS = null, MONTHS = null, YEARS = null;
    private Color SELECTED_FOREGROUND, 
            SELECTED_BACKGROUND,
            NORMAL_FOREGROUND,
            NORMAL_BACKGROUND,
            WEEKS_BACKGROUND,
            WEEKS_FOREGROUND,
            HEADER_BACKGROUND,
            HEADER_FOREGROUND;
    private Font DAYS_FONT = null, WEEKS_FONT;
    private int VIEW_INDEX = 1, TODAY_INDEX = 0;
    private Calendar CALENDAR = null;
    
    public EsonDatePicker() {
        initComponents();
        initConfiguration();
    }
    
    public void setDaysSelectedForeground(Color color){
        SELECTED_FOREGROUND = color;
        updateDays();
    }
    
    public Color getDaysSelectedForeground(){
        return SELECTED_FOREGROUND;
    }
    
    public void setDaysSelectedBackground(Color color){
        SELECTED_BACKGROUND = color;
        updateDays();
    }
    
    public Color getDaysSelectedBackground(){
        return SELECTED_BACKGROUND;
    }
    
    public void setDaysNormalForeground(Color color){
        NORMAL_FOREGROUND = color;
        updateDays();
    }
    
    public Color getDaysNormalForeground(){
        return NORMAL_FOREGROUND;
    }
    
    public void setDaysNormalBackground(Color color){
        NORMAL_BACKGROUND = color;
        updateDays();
    }
    
    public Color getDaysNormalBackground(){
        return NORMAL_BACKGROUND;
    }
    
    public void setHeaderBackground(Color color){
        HEADER_BACKGROUND = color;
        headerNormal(title);
        headerNormal(prev);
        headerNormal(next);
    }
    
    public Color getHeaderBackground(){
        return HEADER_BACKGROUND;
    }
    
    public void setHeaderForeground(Color color){
        HEADER_FOREGROUND = color;
        headerNormal(title);
        headerNormal(prev);
        headerNormal(next);
    }
    
    public Color getHeaderForeground(){
        return HEADER_FOREGROUND;
    }
    
    public void setHeaderFont(Font font){
        title.setFont(font);
    }
    
    public Font getHeaderFont(){
        return title.getFont();
    }
    
    public void setDaysFont(Font font){
        DAYS_FONT = font;
        updateDays();
    }
    
    public Font getDaysFont(){
        return DAYS_FONT;
    }
    
    public void setWeeksFont(Font font){
        WEEKS_FONT = font;
        updateWeeks();
    }
    
    public Font getWeeksFont(){
        return WEEKS_FONT;
    }
    
    public void setWeeksBackground(Color color){
        WEEKS_BACKGROUND = color;
        updateWeeks();
    }
    
    public Color getWeeksBackground(){
        return WEEKS_BACKGROUND;
    }
    
    public void setWeeksForeground(Color color){
        WEEKS_FOREGROUND = color;
        updateWeeks();
    }
    
    public Color getWeeksForeground(){
        return WEEKS_FOREGROUND;
    }
    
    private void updateWeeks(){
        for(JLabel row:WEEKS){
            row.setFont(WEEKS_FONT);
            row.getParent().setBackground(WEEKS_BACKGROUND);
            row.setForeground(WEEKS_FOREGROUND);
        }
    }
    
    private void updateDays(){
        for(JLabel row:DAYS){
            row.setFont(DAYS_FONT);
            stateNormal(row);
        }
    }
    
    public void showPicker(){
        CALENDAR = Calendar.getInstance(Locale.getDefault());
        refreshMonth();
        setVisible(true);
    }
    
    private void changeView(){
        switch(VIEW_INDEX){
            case 0: VIEW_INDEX=1; show(weekView); refreshMonth(); break;
            case 1: VIEW_INDEX=2; show(monthView); title.setText(""+CALENDAR.get(Calendar.YEAR)); resetLink(MONTHS); break;
            case 2: VIEW_INDEX=0; show(yearView); title.setText("Select Year"); resetYearValues(); break;
        }
    }
    
    private void resetYearValues(){
        int yA = 1931, yB = 1940;
        for(JLabel y:YEARS){
            y.setText(yA+"-"+yB);
            yA+=10;yB+=10;
        }
        resetLink(YEARS);
    }
    
    private void show(JPanel panel){
        datePickerContainer.removeAll();
        panel.setSize(datePickerContainer.getSize());
        datePickerContainer.add(panel);
        panel.setVisible(true);
        datePicker.revalidate();
        datePicker.updateUI();
        datePicker.repaint();
        repaint();
    }
    
    private void moveNav(boolean isNext){
        if(weekView.isShowing()){
            if(CALENDAR.get(Calendar.MONTH)==0){
                if(!isNext){
                    CALENDAR.set(Calendar.MONTH, 11);
                    CALENDAR.set(Calendar.YEAR, CALENDAR.get(Calendar.YEAR)-1);
                }else{CALENDAR.set(Calendar.MONTH, 1);}
            }else if(CALENDAR.get(Calendar.MONTH)==11){
                if(isNext){
                    CALENDAR.set(Calendar.MONTH, 0);
                    CALENDAR.set(Calendar.YEAR, CALENDAR.get(Calendar.YEAR)+1);
                }else{CALENDAR.set(Calendar.MONTH, 10);}
            }else{
                CALENDAR.set(Calendar.MONTH, isNext?CALENDAR.get(Calendar.MONTH)+1:CALENDAR.get(Calendar.MONTH)-1);
            }
            refreshMonth();
        }else if(monthView.isShowing()){
            CALENDAR.set(Calendar.YEAR, isNext?CALENDAR.get(Calendar.YEAR)+1:CALENDAR.get(Calendar.YEAR)-1);
            title.setText(CALENDAR.get(Calendar.YEAR)+"");
        }
    }
    
    private void refreshMonth(){
        int CURRENT_MONTH = CALENDAR.get(Calendar.MONTH);
        int CURRENT_YEAR = CALENDAR.get(Calendar.YEAR);
        switch(CURRENT_MONTH){
            case 0: title.setText("JANUARY "+CURRENT_YEAR); break;
            case 1: title.setText("FEBRUARY "+CURRENT_YEAR); break;
            case 2: title.setText("MARCH "+CURRENT_YEAR); break;
            case 3: title.setText("APRIL "+CURRENT_YEAR); break;
            case 4: title.setText("MAY "+CURRENT_YEAR); break;
            case 5: title.setText("JUNE "+CURRENT_YEAR); break;
            case 6: title.setText("JULY "+CURRENT_YEAR); break;
            case 7: title.setText("AUGUST "+CURRENT_YEAR); break;
            case 8: title.setText("SEPTEMBER "+CURRENT_YEAR); break;
            case 9: title.setText("OCTOBER "+CURRENT_YEAR); break;
            case 10: title.setText("NOVEMBER "+CURRENT_YEAR); break;
            case 11: title.setText("DECEMBER "+CURRENT_YEAR); break;
        }
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int today = calendar.get(Calendar.DATE);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayYear = calendar.get(Calendar.YEAR);
        calendar.set(CURRENT_YEAR, CURRENT_MONTH, 1);
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int i = 0;
        for(int t=0;t<DAYS.length;t++){
            if(t>=index){
                if(i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
                    DAYS[t].setText((i+1)+"");i++;
                    if(i==today){
                        TODAY_INDEX = CURRENT_MONTH==todayMonth && CURRENT_YEAR==todayYear?t:DAYS.length;
                    }
                }else{DAYS[t].setText("0");}
            }else{DAYS[t].setText("0");}
        }resetRows();
    }
    
    private boolean isShadowShown = false;
    public void setShadowShown(boolean flag){
        isShadowShown = flag;
//        DropShadowBorder shadow = new DropShadowBorder();
//        shadow.setShowBottomShadow(true);
//        shadow.setShowTopShadow(true);
//        shadow.setShowLeftShadow(true);
//        shadow.setShowRightShadow(true);
//        shadow.setShadowColor(Color.BLACK);
//        datePickerShadow.setBorder(flag?shadow:null);
    }
    
    public boolean isShadowShown(){
        return isShadowShown;
    }
    
    private void initConfiguration(){
        setOpaque(false);
        datePickerShadow.setOpaque(false);
        CALENDAR = Calendar.getInstance(Locale.getDefault());
        NORMAL_FOREGROUND = Color.BLACK;
        NORMAL_BACKGROUND = Color.LIGHT_GRAY;
        SELECTED_FOREGROUND = Color.WHITE;
        SELECTED_BACKGROUND = Color.DARK_GRAY;
        setHeaderBackground(Color.DARK_GRAY);
        setHeaderForeground(Color.white);
        initArrow(prev);
        initArrow(next);
        DAYS = new JLabel[]{r1,r2,r3,r4,r5,r6,r7,
                            r8,r9,r10,r11,r12,r13,r14,
                            r15,r16,r17,r18,r19,r20,r21,
                            r22,r23,r24,r25,r26,r27,r28,
                            r29,r30,r31,r32,r33,r34,r35,
                            r36,r37,r38,r39,r40,r41,r42};
        for(JLabel row:DAYS){
            stateNormal(row);
            row.getParent().addMouseListener(new MouseListener() {
                @Override public void mouseClicked(MouseEvent e) {
                }
                @Override public void mousePressed(MouseEvent e) {}
                @Override public void mouseReleased(MouseEvent e) {
                    if(e.getClickCount()==1 && e.getButton()==1){
                        if (!row.getText().equals("0")) {
                            resetRows();
                            stateSelected(row);
                        }
                    }else if(e.getClickCount()==2 && e.getButton()==1){
                        selectDate(row.getText());
                    }
                }
                @Override public void mouseEntered(MouseEvent e) {}
                @Override public void mouseExited(MouseEvent e) {}
            });
        }
        WEEKS = new JLabel[]{day1,day2,day3,day4,day5,day6,day7};
        for(JLabel row:WEEKS){
            row.getParent().setBackground(Color.GRAY);
            row.setForeground(Color.black);
        }
        MONTHS = new JLabel[]{ml1,ml2,ml3,ml5,ml6,ml4,ml9,ml7,ml8,ml11,ml12,ml10};
        for(JLabel row:MONTHS){
            normalLink(row);
            row.getParent().addMouseListener(new MouseListener() {
                @Override public void mouseClicked(MouseEvent e) {
                }
                @Override public void mousePressed(MouseEvent e) {
                    if(e.getButton()==1){
                        pressedLink(row);
                    }
                }
                @Override public void mouseReleased(MouseEvent e) {
                    if(e.getButton()==1){
                        normalLink(row);
                        selectMonth(row.getText());
                    }
                }
                @Override public void mouseEntered(MouseEvent e) {hoverLink(row);}
                @Override public void mouseExited(MouseEvent e) {normalLink(row);}
            });
        }
        YEARS = new JLabel[]{y1,y2,y3,y4,y5,y6,y7,y8,y9,y10,y11,y12};
        for(JLabel row:YEARS){
            normalLink(row);
            row.getParent().addMouseListener(new MouseListener() {
                @Override public void mouseClicked(MouseEvent e) {
                }
                @Override public void mousePressed(MouseEvent e) {
                    if(e.getButton()==1){
                        pressedLink(row);
                    }
                }
                @Override public void mouseReleased(MouseEvent e) {
                    if(e.getButton()==1){
                        normalLink(row);
                        yearAction(row.getText());
                    }
                }
                @Override public void mouseEntered(MouseEvent e) {hoverLink(row);}
                @Override public void mouseExited(MouseEvent e) {normalLink(row);}
            });
        }
        refreshMonth();
    }
    
    private void yearAction(String selection){
        if(selection.contains("-")){
            int i = 0, day = Integer.parseInt(selection.split("-")[0]);
            for(JLabel row:YEARS){
                if(i>0 && i<11){
                    row.setText(""+day);
                    day++;
                }else{row.setText("");}
                i++;
            }
        }else{
            CALENDAR.set(Calendar.YEAR, Integer.parseInt(selection.trim()));
            VIEW_INDEX = 1;
            changeView();
        }
    }
    
    private void selectMonth(String month){
        int selected = 0;
        switch(month){
            case "JANUARY": selected=0; break;
            case "FEBRUARY": selected=1; break;
            case "MARCH": selected=2; break;
            case "APRIL": selected=3; break;
            case "MAY": selected=4; break;
            case "JUNE": selected=5; break;
            case "JULY": selected=6; break;
            case "AUGUST": selected=7; break;
            case "SEPTEMBER": selected=8; break;
            case "OCTOBER": selected=9; break;
            case "NOVEMBER": selected=10; break;
            case "DECEMBER": selected=11; break;
        }
        CALENDAR.set(Calendar.MONTH, selected);
        VIEW_INDEX = 0;
        changeView();
    }
    
    private void selectDate(String day){
        CALENDAR.set(Calendar.DATE, Integer.parseInt(day));
        action();
    }
    
    public void reset(){
        CALENDAR = Calendar.getInstance(Locale.getDefault());
        refreshMonth();
        for(JLabel row:DAYS){
            if(row.getText().equals(CALENDAR.get(Calendar.DATE)+"")){
                resetRows();
                stateSelected(row);
            }
        } 
    }
    
    public Date getSelectedDate(){
        return CALENDAR.getTime();
    }
    
    public String getFormattedDate(){
        int cmonth = CALENDAR.get(Calendar.MONTH);
        int cday = CALENDAR.get(Calendar.DATE);
        String sMonth = (cmonth + 1)<10?"0"+(cmonth + 1):(cmonth + 1)+"";
        String sDay = cday<10?"0"+cday:cday+"";
        return CALENDAR.get(Calendar.YEAR)+"-"+sMonth+"-"+sDay;
    }
    
    public String getFormattedDate(String format){
        return new SimpleDateFormat(format).format(CALENDAR.getTime());
    }
    
    public void addActionListener(ActionListener l){
        listenerList.add(ActionListener.class, l);
    }
    
    public void removeActionListener(ActionListener l){
        listenerList.remove(ActionListener.class, l);
    }
    
    private void action(){
        ActionListener[] action = listenerList.getListeners(ActionListener.class);
        if(action.length>0){
            for(ActionListener a:action){
                a.actionPerformed(null);
            }
        }
    }
    
    private void resetLink(JLabel[] link){
        for(JLabel row:link){
            normalLink(row);
        }
    }
    
    private void resetRows(){
        int i = 0;
        for(JLabel row:DAYS){
            stateNormal(row);
            if(i==TODAY_INDEX){
                row.setBorder(BorderFactory.createMatteBorder(1,1,1,1,NORMAL_FOREGROUND));
//                DropShadowBorder shadow = new DropShadowBorder();
//                shadow.setShadowColor(Color.BLACK);
//                shadow.setShadowSize(4);
//                ((JPanel) row.getParent()).setBorder(shadow);
            }
            i++;
        }
        i = 0;
        for(JLabel row:MONTHS){
            normalLink(row);
        }
    }
    
    private void normalLink(JLabel row){
        row.setOpaque(true);
        ((JPanel)row.getParent()).setOpaque(false);
        row.setBackground(NORMAL_BACKGROUND);
        row.setForeground(NORMAL_FOREGROUND);
        String txt = row.getText().replace("<html>", "");
        txt = txt.replace("<u>", "");
        txt = txt.replace("</u>", "");
        txt = txt.replace("</html>", "");
        row.setText(txt);
    }
    
    private void pressedLink(JLabel row){
        row.setBackground(NORMAL_BACKGROUND.darker());
        row.setForeground(NORMAL_FOREGROUND);
    }
    
    private void hoverLink(JLabel row){
        row.setText("<html><u>"+row.getText()+"</u></html>");
        row.setBackground(NORMAL_BACKGROUND);
        row.setForeground(NORMAL_FOREGROUND);
    }
    
    private void stateNormal(JLabel row){
        weekView.setBackground(NORMAL_BACKGROUND);
        monthView.setBackground(NORMAL_BACKGROUND);
        row.setOpaque(true);((JPanel)row.getParent()).setOpaque(false);
        row.setForeground(row.getText().equals("0")?NORMAL_BACKGROUND:NORMAL_FOREGROUND);
        row.setBackground(NORMAL_BACKGROUND);
//        DropShadowBorder shadow = new DropShadowBorder();
//        shadow.setShadowColor(NORMAL_BACKGROUND);
//        shadow.setShadowOpacity(0);
//        shadow.setShadowSize(4);
//        ((JPanel)row.getParent()).setBorder(shadow);
        row.setBorder(BorderFactory.createMatteBorder(1,1,1,1,NORMAL_BACKGROUND));
    }
    
    private void stateSelected(JLabel row){
        row.setOpaque(true);((JPanel)row.getParent()).setOpaque(false);
        row.setForeground(SELECTED_FOREGROUND);
        row.setBackground(SELECTED_BACKGROUND);
//        DropShadowBorder shadow = new DropShadowBorder();
//        shadow.setShadowColor(Color.BLACK);
//        shadow.setShadowSize(4);
//        ((JPanel)row.getParent()).setBorder(shadow);
        row.setBorder(BorderFactory.createMatteBorder(1,1,1,1,NORMAL_BACKGROUND));
    }
    
    private void initArrow(JLabel arrow){
        headerNormal(arrow);
        arrow.addMouseListener(new MouseListener(){
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {
                if(e.getButton()==1){headerPressed(arrow);}}
            @Override public void mouseReleased(MouseEvent e) {
                if(e.getButton()==1){
                    headerNormal(arrow);
                    moveNav(arrow==next);
                }
            }
            @Override public void mouseEntered(MouseEvent e) {headerHover(arrow);}
            @Override public void mouseExited(MouseEvent e) {headerNormal(arrow);}
        });
    }
    
    private void headerHover(JLabel arrow){
        arrow.setForeground(HEADER_FOREGROUND);
        arrow.setBackground(HEADER_BACKGROUND.brighter());
    }
    
    private void headerPressed(JLabel arrow){
        arrow.setForeground(HEADER_FOREGROUND.brighter());
        arrow.setBackground(HEADER_BACKGROUND.darker());
    }
    
    private void headerNormal(JLabel arrow){
        arrow.setForeground(HEADER_FOREGROUND);
        arrow.setBackground(HEADER_BACKGROUND);
        arrow.setBorder(BorderFactory.createMatteBorder(2,arrow==prev || arrow==title?2:0,2,arrow==next?2:0, HEADER_BACKGROUND));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        monthView = new javax.swing.JPanel();
        mb1 = new javax.swing.JPanel();
        ml1 = new javax.swing.JLabel();
        mb2 = new javax.swing.JPanel();
        ml2 = new javax.swing.JLabel();
        mb3 = new javax.swing.JPanel();
        ml3 = new javax.swing.JLabel();
        mb4 = new javax.swing.JPanel();
        ml5 = new javax.swing.JLabel();
        mb5 = new javax.swing.JPanel();
        ml6 = new javax.swing.JLabel();
        mb6 = new javax.swing.JPanel();
        ml4 = new javax.swing.JLabel();
        mb7 = new javax.swing.JPanel();
        ml9 = new javax.swing.JLabel();
        mb8 = new javax.swing.JPanel();
        ml7 = new javax.swing.JLabel();
        mb9 = new javax.swing.JPanel();
        ml8 = new javax.swing.JLabel();
        mb10 = new javax.swing.JPanel();
        ml11 = new javax.swing.JLabel();
        mb11 = new javax.swing.JPanel();
        ml12 = new javax.swing.JLabel();
        mb12 = new javax.swing.JPanel();
        ml10 = new javax.swing.JLabel();
        yearView = new javax.swing.JPanel();
        mb13 = new javax.swing.JPanel();
        y1 = new javax.swing.JLabel();
        mb14 = new javax.swing.JPanel();
        y2 = new javax.swing.JLabel();
        mb15 = new javax.swing.JPanel();
        y3 = new javax.swing.JLabel();
        mb16 = new javax.swing.JPanel();
        y5 = new javax.swing.JLabel();
        mb17 = new javax.swing.JPanel();
        y6 = new javax.swing.JLabel();
        mb18 = new javax.swing.JPanel();
        y4 = new javax.swing.JLabel();
        mb19 = new javax.swing.JPanel();
        y9 = new javax.swing.JLabel();
        mb20 = new javax.swing.JPanel();
        y7 = new javax.swing.JLabel();
        mb21 = new javax.swing.JPanel();
        y8 = new javax.swing.JLabel();
        mb22 = new javax.swing.JPanel();
        y11 = new javax.swing.JLabel();
        mb23 = new javax.swing.JPanel();
        y12 = new javax.swing.JLabel();
        mb24 = new javax.swing.JPanel();
        y10 = new javax.swing.JLabel();
        datePickerShadow = new javax.swing.JPanel();
        datePicker = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        next = new javax.swing.JLabel();
        prev = new javax.swing.JLabel();
        datePickerContainer = new javax.swing.JPanel();
        weekView = new javax.swing.JPanel();
        column1 = new javax.swing.JPanel();
        dayBackground1 = new javax.swing.JPanel();
        day1 = new javax.swing.JLabel();
        rowBackgroun1 = new javax.swing.JPanel();
        r1 = new javax.swing.JLabel();
        rowBackgroun2 = new javax.swing.JPanel();
        r8 = new javax.swing.JLabel();
        rowBackgroun3 = new javax.swing.JPanel();
        r15 = new javax.swing.JLabel();
        rowBackgroun4 = new javax.swing.JPanel();
        r22 = new javax.swing.JLabel();
        rowBackgroun5 = new javax.swing.JPanel();
        r29 = new javax.swing.JLabel();
        rowBackgroun6 = new javax.swing.JPanel();
        r36 = new javax.swing.JLabel();
        column2 = new javax.swing.JPanel();
        dayBackground2 = new javax.swing.JPanel();
        day2 = new javax.swing.JLabel();
        rowBackgroun7 = new javax.swing.JPanel();
        r2 = new javax.swing.JLabel();
        rowBackgroun8 = new javax.swing.JPanel();
        r9 = new javax.swing.JLabel();
        rowBackgroun9 = new javax.swing.JPanel();
        r16 = new javax.swing.JLabel();
        rowBackgroun10 = new javax.swing.JPanel();
        r23 = new javax.swing.JLabel();
        rowBackgroun11 = new javax.swing.JPanel();
        r30 = new javax.swing.JLabel();
        rowBackgroun12 = new javax.swing.JPanel();
        r37 = new javax.swing.JLabel();
        column3 = new javax.swing.JPanel();
        dayBackground3 = new javax.swing.JPanel();
        day3 = new javax.swing.JLabel();
        rowBackgroun13 = new javax.swing.JPanel();
        r3 = new javax.swing.JLabel();
        rowBackgroun14 = new javax.swing.JPanel();
        r10 = new javax.swing.JLabel();
        rowBackgroun15 = new javax.swing.JPanel();
        r17 = new javax.swing.JLabel();
        rowBackgroun16 = new javax.swing.JPanel();
        r24 = new javax.swing.JLabel();
        rowBackgroun17 = new javax.swing.JPanel();
        r31 = new javax.swing.JLabel();
        rowBackgroun18 = new javax.swing.JPanel();
        r38 = new javax.swing.JLabel();
        column4 = new javax.swing.JPanel();
        dayBackground4 = new javax.swing.JPanel();
        day4 = new javax.swing.JLabel();
        rowBackgroun19 = new javax.swing.JPanel();
        r4 = new javax.swing.JLabel();
        rowBackgroun20 = new javax.swing.JPanel();
        r11 = new javax.swing.JLabel();
        rowBackgroun21 = new javax.swing.JPanel();
        r18 = new javax.swing.JLabel();
        rowBackgroun22 = new javax.swing.JPanel();
        r25 = new javax.swing.JLabel();
        rowBackgroun23 = new javax.swing.JPanel();
        r32 = new javax.swing.JLabel();
        rowBackgroun24 = new javax.swing.JPanel();
        r39 = new javax.swing.JLabel();
        column5 = new javax.swing.JPanel();
        dayBackground5 = new javax.swing.JPanel();
        day5 = new javax.swing.JLabel();
        rowBackgroun25 = new javax.swing.JPanel();
        r5 = new javax.swing.JLabel();
        rowBackgroun26 = new javax.swing.JPanel();
        r12 = new javax.swing.JLabel();
        rowBackgroun27 = new javax.swing.JPanel();
        r19 = new javax.swing.JLabel();
        rowBackgroun28 = new javax.swing.JPanel();
        r26 = new javax.swing.JLabel();
        rowBackgroun29 = new javax.swing.JPanel();
        r33 = new javax.swing.JLabel();
        rowBackgroun30 = new javax.swing.JPanel();
        r40 = new javax.swing.JLabel();
        column6 = new javax.swing.JPanel();
        dayBackground6 = new javax.swing.JPanel();
        day6 = new javax.swing.JLabel();
        rowBackgroun31 = new javax.swing.JPanel();
        r6 = new javax.swing.JLabel();
        rowBackgroun32 = new javax.swing.JPanel();
        r13 = new javax.swing.JLabel();
        rowBackgroun33 = new javax.swing.JPanel();
        r20 = new javax.swing.JLabel();
        rowBackgroun34 = new javax.swing.JPanel();
        r27 = new javax.swing.JLabel();
        rowBackgroun35 = new javax.swing.JPanel();
        r34 = new javax.swing.JLabel();
        rowBackgroun36 = new javax.swing.JPanel();
        r41 = new javax.swing.JLabel();
        column8 = new javax.swing.JPanel();
        dayBackground7 = new javax.swing.JPanel();
        day7 = new javax.swing.JLabel();
        rowBackgroun37 = new javax.swing.JPanel();
        r7 = new javax.swing.JLabel();
        rowBackgroun38 = new javax.swing.JPanel();
        r14 = new javax.swing.JLabel();
        rowBackgroun39 = new javax.swing.JPanel();
        r21 = new javax.swing.JLabel();
        rowBackgroun40 = new javax.swing.JPanel();
        r28 = new javax.swing.JLabel();
        rowBackgroun41 = new javax.swing.JPanel();
        r35 = new javax.swing.JLabel();
        rowBackgroun42 = new javax.swing.JPanel();
        r42 = new javax.swing.JLabel();

        mb1.setPreferredSize(new java.awt.Dimension(80, 30));

        ml1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml1.setText("JANUARY");
        ml1.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb1Layout = new javax.swing.GroupLayout(mb1);
        mb1.setLayout(mb1Layout);
        mb1Layout.setHorizontalGroup(
            mb1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb1Layout.setVerticalGroup(
            mb1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml1, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb2.setPreferredSize(new java.awt.Dimension(80, 30));

        ml2.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml2.setText("FEBRUARY");
        ml2.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb2Layout = new javax.swing.GroupLayout(mb2);
        mb2.setLayout(mb2Layout);
        mb2Layout.setHorizontalGroup(
            mb2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb2Layout.setVerticalGroup(
            mb2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb3.setPreferredSize(new java.awt.Dimension(80, 30));

        ml3.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml3.setText("MARCH");
        ml3.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb3Layout = new javax.swing.GroupLayout(mb3);
        mb3.setLayout(mb3Layout);
        mb3Layout.setHorizontalGroup(
            mb3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml3, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb3Layout.setVerticalGroup(
            mb3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb4.setPreferredSize(new java.awt.Dimension(80, 30));

        ml5.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml5.setText("MAY");
        ml5.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb4Layout = new javax.swing.GroupLayout(mb4);
        mb4.setLayout(mb4Layout);
        mb4Layout.setHorizontalGroup(
            mb4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb4Layout.setVerticalGroup(
            mb4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb5.setPreferredSize(new java.awt.Dimension(80, 30));

        ml6.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml6.setText("JUNE");
        ml6.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb5Layout = new javax.swing.GroupLayout(mb5);
        mb5.setLayout(mb5Layout);
        mb5Layout.setHorizontalGroup(
            mb5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb5Layout.setVerticalGroup(
            mb5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb6.setPreferredSize(new java.awt.Dimension(80, 30));

        ml4.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml4.setText("APRIL");
        ml4.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb6Layout = new javax.swing.GroupLayout(mb6);
        mb6.setLayout(mb6Layout);
        mb6Layout.setHorizontalGroup(
            mb6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb6Layout.setVerticalGroup(
            mb6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml4, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb7.setPreferredSize(new java.awt.Dimension(80, 30));

        ml9.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml9.setText("SEPTEMBER");
        ml9.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb7Layout = new javax.swing.GroupLayout(mb7);
        mb7.setLayout(mb7Layout);
        mb7Layout.setHorizontalGroup(
            mb7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb7Layout.setVerticalGroup(
            mb7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb8.setPreferredSize(new java.awt.Dimension(80, 30));

        ml7.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml7.setText("JULY");
        ml7.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb8Layout = new javax.swing.GroupLayout(mb8);
        mb8.setLayout(mb8Layout);
        mb8Layout.setHorizontalGroup(
            mb8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb8Layout.setVerticalGroup(
            mb8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml7, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb9.setPreferredSize(new java.awt.Dimension(80, 30));

        ml8.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml8.setText("AUGUST");
        ml8.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb9Layout = new javax.swing.GroupLayout(mb9);
        mb9.setLayout(mb9Layout);
        mb9Layout.setHorizontalGroup(
            mb9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb9Layout.setVerticalGroup(
            mb9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb10.setPreferredSize(new java.awt.Dimension(80, 30));

        ml11.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml11.setText("NOVEMBER");
        ml11.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb10Layout = new javax.swing.GroupLayout(mb10);
        mb10.setLayout(mb10Layout);
        mb10Layout.setHorizontalGroup(
            mb10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml11, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb10Layout.setVerticalGroup(
            mb10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb11.setPreferredSize(new java.awt.Dimension(80, 30));

        ml12.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml12.setText("DECEMBER");
        ml12.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb11Layout = new javax.swing.GroupLayout(mb11);
        mb11.setLayout(mb11Layout);
        mb11Layout.setHorizontalGroup(
            mb11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb11Layout.setVerticalGroup(
            mb11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb12.setPreferredSize(new java.awt.Dimension(80, 30));

        ml10.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ml10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ml10.setText("OCTOBER");
        ml10.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb12Layout = new javax.swing.GroupLayout(mb12);
        mb12.setLayout(mb12Layout);
        mb12Layout.setHorizontalGroup(
            mb12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml10, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb12Layout.setVerticalGroup(
            mb12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ml10, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout monthViewLayout = new javax.swing.GroupLayout(monthView);
        monthView.setLayout(monthViewLayout);
        monthViewLayout.setHorizontalGroup(
            monthViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monthViewLayout.createSequentialGroup()
                .addGroup(monthViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb12, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(mb8, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(mb1, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(mb6, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(monthViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb10, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb9, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb2, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb4, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                .addGroup(monthViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb3, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb5, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb7, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb11, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)))
        );
        monthViewLayout.setVerticalGroup(
            monthViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monthViewLayout.createSequentialGroup()
                .addGroup(monthViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb1, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb3, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb2, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(monthViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb6, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb5, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb4, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addGroup(monthViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb8, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb7, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb9, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addGroup(monthViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb12, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(mb11, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(mb10, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)))
        );

        mb13.setPreferredSize(new java.awt.Dimension(80, 30));

        y1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y1.setText("1931-1940");
        y1.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb13Layout = new javax.swing.GroupLayout(mb13);
        mb13.setLayout(mb13Layout);
        mb13Layout.setHorizontalGroup(
            mb13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb13Layout.setVerticalGroup(
            mb13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y1, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb14.setPreferredSize(new java.awt.Dimension(80, 30));

        y2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2.setText("1941-1950");
        y2.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb14Layout = new javax.swing.GroupLayout(mb14);
        mb14.setLayout(mb14Layout);
        mb14Layout.setHorizontalGroup(
            mb14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb14Layout.setVerticalGroup(
            mb14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb15.setPreferredSize(new java.awt.Dimension(80, 30));

        y3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y3.setText("1951-1960");
        y3.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb15Layout = new javax.swing.GroupLayout(mb15);
        mb15.setLayout(mb15Layout);
        mb15Layout.setHorizontalGroup(
            mb15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb15Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y3, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb15Layout.setVerticalGroup(
            mb15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb15Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb16.setPreferredSize(new java.awt.Dimension(80, 30));

        y5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y5.setText("1971-1980");
        y5.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb16Layout = new javax.swing.GroupLayout(mb16);
        mb16.setLayout(mb16Layout);
        mb16Layout.setHorizontalGroup(
            mb16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb16Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb16Layout.setVerticalGroup(
            mb16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb16Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb17.setPreferredSize(new java.awt.Dimension(80, 30));

        y6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y6.setText("1981-1990");
        y6.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb17Layout = new javax.swing.GroupLayout(mb17);
        mb17.setLayout(mb17Layout);
        mb17Layout.setHorizontalGroup(
            mb17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb17Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb17Layout.setVerticalGroup(
            mb17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb17Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb18.setPreferredSize(new java.awt.Dimension(80, 30));

        y4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y4.setText("1961-1970");
        y4.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb18Layout = new javax.swing.GroupLayout(mb18);
        mb18.setLayout(mb18Layout);
        mb18Layout.setHorizontalGroup(
            mb18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb18Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb18Layout.setVerticalGroup(
            mb18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb18Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y4, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb19.setPreferredSize(new java.awt.Dimension(80, 30));

        y9.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y9.setText("2011-2020");
        y9.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb19Layout = new javax.swing.GroupLayout(mb19);
        mb19.setLayout(mb19Layout);
        mb19Layout.setHorizontalGroup(
            mb19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb19Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb19Layout.setVerticalGroup(
            mb19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb19Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb20.setPreferredSize(new java.awt.Dimension(80, 30));

        y7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y7.setText("1991-2000");
        y7.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb20Layout = new javax.swing.GroupLayout(mb20);
        mb20.setLayout(mb20Layout);
        mb20Layout.setHorizontalGroup(
            mb20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb20Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb20Layout.setVerticalGroup(
            mb20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb20Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y7, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb21.setPreferredSize(new java.awt.Dimension(80, 30));

        y8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y8.setText("2001-2010");
        y8.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb21Layout = new javax.swing.GroupLayout(mb21);
        mb21.setLayout(mb21Layout);
        mb21Layout.setHorizontalGroup(
            mb21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb21Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb21Layout.setVerticalGroup(
            mb21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb21Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb22.setPreferredSize(new java.awt.Dimension(80, 30));

        y11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y11.setText("2031-2040");
        y11.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb22Layout = new javax.swing.GroupLayout(mb22);
        mb22.setLayout(mb22Layout);
        mb22Layout.setHorizontalGroup(
            mb22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb22Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y11, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb22Layout.setVerticalGroup(
            mb22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb22Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb23.setPreferredSize(new java.awt.Dimension(80, 30));

        y12.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y12.setText("2041-2050");
        y12.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb23Layout = new javax.swing.GroupLayout(mb23);
        mb23.setLayout(mb23Layout);
        mb23Layout.setHorizontalGroup(
            mb23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb23Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb23Layout.setVerticalGroup(
            mb23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb23Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mb24.setPreferredSize(new java.awt.Dimension(80, 30));

        y10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        y10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y10.setText("2021-2030");
        y10.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout mb24Layout = new javax.swing.GroupLayout(mb24);
        mb24.setLayout(mb24Layout);
        mb24Layout.setHorizontalGroup(
            mb24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb24Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y10, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        mb24Layout.setVerticalGroup(
            mb24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mb24Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(y10, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout yearViewLayout = new javax.swing.GroupLayout(yearView);
        yearView.setLayout(yearViewLayout);
        yearViewLayout.setHorizontalGroup(
            yearViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(yearViewLayout.createSequentialGroup()
                .addGroup(yearViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb24, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(mb20, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(mb13, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(mb18, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(yearViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb22, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb21, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb14, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb16, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                .addGroup(yearViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb15, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb17, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb19, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(mb23, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)))
        );
        yearViewLayout.setVerticalGroup(
            yearViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(yearViewLayout.createSequentialGroup()
                .addGroup(yearViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb13, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb15, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb14, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(yearViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb18, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb17, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb16, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addGroup(yearViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb20, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb19, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(mb21, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addGroup(yearViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mb24, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(mb23, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(mb22, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)))
        );

        header.setBackground(new java.awt.Color(153, 153, 255));

        title.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        title.setForeground(new java.awt.Color(0, 0, 0));
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("DECEMBER 2020");
        title.setOpaque(true);
        title.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                titleMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                titleMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                titleMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                titleMouseReleased(evt);
            }
        });

        next.setForeground(new java.awt.Color(0, 0, 0));
        next.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        next.setText(">");
        next.setOpaque(true);

        prev.setForeground(new java.awt.Color(0, 0, 0));
        prev.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        prev.setText("<");
        prev.setOpaque(true);

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(prev, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(prev, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
            .addComponent(next, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        weekView.setBackground(new java.awt.Color(51, 255, 51));

        column1.setBackground(new java.awt.Color(255, 153, 51));
        column1.setOpaque(false);

        dayBackground1.setPreferredSize(new java.awt.Dimension(30, 20));

        day1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        day1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day1.setText("Sun");

        javax.swing.GroupLayout dayBackground1Layout = new javax.swing.GroupLayout(dayBackground1);
        dayBackground1.setLayout(dayBackground1Layout);
        dayBackground1Layout.setHorizontalGroup(
            dayBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        dayBackground1Layout.setVerticalGroup(
            dayBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day1, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        rowBackgroun1.setPreferredSize(new java.awt.Dimension(30, 25));

        r1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r1.setText("1");

        javax.swing.GroupLayout rowBackgroun1Layout = new javax.swing.GroupLayout(rowBackgroun1);
        rowBackgroun1.setLayout(rowBackgroun1Layout);
        rowBackgroun1Layout.setHorizontalGroup(
            rowBackgroun1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun1Layout.setVerticalGroup(
            rowBackgroun1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        rowBackgroun2.setPreferredSize(new java.awt.Dimension(30, 25));

        r8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r8.setText("8");

        javax.swing.GroupLayout rowBackgroun2Layout = new javax.swing.GroupLayout(rowBackgroun2);
        rowBackgroun2.setLayout(rowBackgroun2Layout);
        rowBackgroun2Layout.setHorizontalGroup(
            rowBackgroun2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r8, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun2Layout.setVerticalGroup(
            rowBackgroun2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r8, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        rowBackgroun3.setPreferredSize(new java.awt.Dimension(30, 25));

        r15.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r15.setText("15");

        javax.swing.GroupLayout rowBackgroun3Layout = new javax.swing.GroupLayout(rowBackgroun3);
        rowBackgroun3.setLayout(rowBackgroun3Layout);
        rowBackgroun3Layout.setHorizontalGroup(
            rowBackgroun3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r15, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun3Layout.setVerticalGroup(
            rowBackgroun3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r15, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        rowBackgroun4.setPreferredSize(new java.awt.Dimension(30, 25));

        r22.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r22.setText("22");

        javax.swing.GroupLayout rowBackgroun4Layout = new javax.swing.GroupLayout(rowBackgroun4);
        rowBackgroun4.setLayout(rowBackgroun4Layout);
        rowBackgroun4Layout.setHorizontalGroup(
            rowBackgroun4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r22, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun4Layout.setVerticalGroup(
            rowBackgroun4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r22, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        rowBackgroun5.setPreferredSize(new java.awt.Dimension(30, 25));

        r29.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r29.setText("29");

        javax.swing.GroupLayout rowBackgroun5Layout = new javax.swing.GroupLayout(rowBackgroun5);
        rowBackgroun5.setLayout(rowBackgroun5Layout);
        rowBackgroun5Layout.setHorizontalGroup(
            rowBackgroun5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r29, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun5Layout.setVerticalGroup(
            rowBackgroun5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r29, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        rowBackgroun6.setPreferredSize(new java.awt.Dimension(30, 25));

        r36.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r36.setText("36");

        javax.swing.GroupLayout rowBackgroun6Layout = new javax.swing.GroupLayout(rowBackgroun6);
        rowBackgroun6.setLayout(rowBackgroun6Layout);
        rowBackgroun6Layout.setHorizontalGroup(
            rowBackgroun6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r36, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun6Layout.setVerticalGroup(
            rowBackgroun6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r36, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout column1Layout = new javax.swing.GroupLayout(column1);
        column1.setLayout(column1Layout);
        column1Layout.setHorizontalGroup(
            column1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dayBackground1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        column1Layout.setVerticalGroup(
            column1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(column1Layout.createSequentialGroup()
                .addComponent(dayBackground1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        column2.setBackground(new java.awt.Color(255, 153, 51));
        column2.setOpaque(false);

        dayBackground2.setPreferredSize(new java.awt.Dimension(30, 20));

        day2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        day2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day2.setText("Mon");

        javax.swing.GroupLayout dayBackground2Layout = new javax.swing.GroupLayout(dayBackground2);
        dayBackground2.setLayout(dayBackground2Layout);
        dayBackground2Layout.setHorizontalGroup(
            dayBackground2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        dayBackground2Layout.setVerticalGroup(
            dayBackground2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day2, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        rowBackgroun7.setPreferredSize(new java.awt.Dimension(30, 25));

        r2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r2.setText("2");

        javax.swing.GroupLayout rowBackgroun7Layout = new javax.swing.GroupLayout(rowBackgroun7);
        rowBackgroun7.setLayout(rowBackgroun7Layout);
        rowBackgroun7Layout.setHorizontalGroup(
            rowBackgroun7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun7Layout.setVerticalGroup(
            rowBackgroun7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun8.setPreferredSize(new java.awt.Dimension(30, 25));

        r9.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r9.setText("9");

        javax.swing.GroupLayout rowBackgroun8Layout = new javax.swing.GroupLayout(rowBackgroun8);
        rowBackgroun8.setLayout(rowBackgroun8Layout);
        rowBackgroun8Layout.setHorizontalGroup(
            rowBackgroun8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r9, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun8Layout.setVerticalGroup(
            rowBackgroun8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun9.setPreferredSize(new java.awt.Dimension(30, 25));

        r16.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r16.setText("16");

        javax.swing.GroupLayout rowBackgroun9Layout = new javax.swing.GroupLayout(rowBackgroun9);
        rowBackgroun9.setLayout(rowBackgroun9Layout);
        rowBackgroun9Layout.setHorizontalGroup(
            rowBackgroun9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r16, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun9Layout.setVerticalGroup(
            rowBackgroun9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun10.setPreferredSize(new java.awt.Dimension(30, 25));

        r23.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r23.setText("23");

        javax.swing.GroupLayout rowBackgroun10Layout = new javax.swing.GroupLayout(rowBackgroun10);
        rowBackgroun10.setLayout(rowBackgroun10Layout);
        rowBackgroun10Layout.setHorizontalGroup(
            rowBackgroun10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r23, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun10Layout.setVerticalGroup(
            rowBackgroun10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun11.setPreferredSize(new java.awt.Dimension(30, 25));

        r30.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r30.setText("30");

        javax.swing.GroupLayout rowBackgroun11Layout = new javax.swing.GroupLayout(rowBackgroun11);
        rowBackgroun11.setLayout(rowBackgroun11Layout);
        rowBackgroun11Layout.setHorizontalGroup(
            rowBackgroun11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r30, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun11Layout.setVerticalGroup(
            rowBackgroun11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun12.setPreferredSize(new java.awt.Dimension(30, 25));

        r37.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r37.setText("37");

        javax.swing.GroupLayout rowBackgroun12Layout = new javax.swing.GroupLayout(rowBackgroun12);
        rowBackgroun12.setLayout(rowBackgroun12Layout);
        rowBackgroun12Layout.setHorizontalGroup(
            rowBackgroun12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r37, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun12Layout.setVerticalGroup(
            rowBackgroun12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout column2Layout = new javax.swing.GroupLayout(column2);
        column2.setLayout(column2Layout);
        column2Layout.setHorizontalGroup(
            column2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dayBackground2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        column2Layout.setVerticalGroup(
            column2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(column2Layout.createSequentialGroup()
                .addComponent(dayBackground2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        column3.setBackground(new java.awt.Color(255, 153, 51));
        column3.setOpaque(false);

        dayBackground3.setPreferredSize(new java.awt.Dimension(30, 20));

        day3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        day3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day3.setText("Tue");

        javax.swing.GroupLayout dayBackground3Layout = new javax.swing.GroupLayout(dayBackground3);
        dayBackground3.setLayout(dayBackground3Layout);
        dayBackground3Layout.setHorizontalGroup(
            dayBackground3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day3, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        dayBackground3Layout.setVerticalGroup(
            dayBackground3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day3, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        rowBackgroun13.setPreferredSize(new java.awt.Dimension(30, 25));

        r3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r3.setText("3");

        javax.swing.GroupLayout rowBackgroun13Layout = new javax.swing.GroupLayout(rowBackgroun13);
        rowBackgroun13.setLayout(rowBackgroun13Layout);
        rowBackgroun13Layout.setHorizontalGroup(
            rowBackgroun13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r3, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun13Layout.setVerticalGroup(
            rowBackgroun13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun14.setPreferredSize(new java.awt.Dimension(30, 25));

        r10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r10.setText("10");

        javax.swing.GroupLayout rowBackgroun14Layout = new javax.swing.GroupLayout(rowBackgroun14);
        rowBackgroun14.setLayout(rowBackgroun14Layout);
        rowBackgroun14Layout.setHorizontalGroup(
            rowBackgroun14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r10, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun14Layout.setVerticalGroup(
            rowBackgroun14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun15.setPreferredSize(new java.awt.Dimension(30, 25));

        r17.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r17.setText("17");

        javax.swing.GroupLayout rowBackgroun15Layout = new javax.swing.GroupLayout(rowBackgroun15);
        rowBackgroun15.setLayout(rowBackgroun15Layout);
        rowBackgroun15Layout.setHorizontalGroup(
            rowBackgroun15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r17, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun15Layout.setVerticalGroup(
            rowBackgroun15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun16.setPreferredSize(new java.awt.Dimension(30, 25));

        r24.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r24.setText("24");

        javax.swing.GroupLayout rowBackgroun16Layout = new javax.swing.GroupLayout(rowBackgroun16);
        rowBackgroun16.setLayout(rowBackgroun16Layout);
        rowBackgroun16Layout.setHorizontalGroup(
            rowBackgroun16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r24, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun16Layout.setVerticalGroup(
            rowBackgroun16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun17.setPreferredSize(new java.awt.Dimension(30, 25));

        r31.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r31.setText("31");

        javax.swing.GroupLayout rowBackgroun17Layout = new javax.swing.GroupLayout(rowBackgroun17);
        rowBackgroun17.setLayout(rowBackgroun17Layout);
        rowBackgroun17Layout.setHorizontalGroup(
            rowBackgroun17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r31, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun17Layout.setVerticalGroup(
            rowBackgroun17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun18.setPreferredSize(new java.awt.Dimension(30, 25));

        r38.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r38.setText("38");

        javax.swing.GroupLayout rowBackgroun18Layout = new javax.swing.GroupLayout(rowBackgroun18);
        rowBackgroun18.setLayout(rowBackgroun18Layout);
        rowBackgroun18Layout.setHorizontalGroup(
            rowBackgroun18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r38, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun18Layout.setVerticalGroup(
            rowBackgroun18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout column3Layout = new javax.swing.GroupLayout(column3);
        column3.setLayout(column3Layout);
        column3Layout.setHorizontalGroup(
            column3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dayBackground3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        column3Layout.setVerticalGroup(
            column3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(column3Layout.createSequentialGroup()
                .addComponent(dayBackground3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        column4.setBackground(new java.awt.Color(255, 153, 51));
        column4.setOpaque(false);

        dayBackground4.setPreferredSize(new java.awt.Dimension(30, 20));

        day4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        day4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day4.setText("Wed");

        javax.swing.GroupLayout dayBackground4Layout = new javax.swing.GroupLayout(dayBackground4);
        dayBackground4.setLayout(dayBackground4Layout);
        dayBackground4Layout.setHorizontalGroup(
            dayBackground4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        dayBackground4Layout.setVerticalGroup(
            dayBackground4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day4, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        rowBackgroun19.setPreferredSize(new java.awt.Dimension(30, 25));

        r4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r4.setText("4");

        javax.swing.GroupLayout rowBackgroun19Layout = new javax.swing.GroupLayout(rowBackgroun19);
        rowBackgroun19.setLayout(rowBackgroun19Layout);
        rowBackgroun19Layout.setHorizontalGroup(
            rowBackgroun19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun19Layout.setVerticalGroup(
            rowBackgroun19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun20.setPreferredSize(new java.awt.Dimension(30, 25));

        r11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r11.setText("11");

        javax.swing.GroupLayout rowBackgroun20Layout = new javax.swing.GroupLayout(rowBackgroun20);
        rowBackgroun20.setLayout(rowBackgroun20Layout);
        rowBackgroun20Layout.setHorizontalGroup(
            rowBackgroun20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r11, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun20Layout.setVerticalGroup(
            rowBackgroun20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun21.setPreferredSize(new java.awt.Dimension(30, 25));

        r18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r18.setText("18");

        javax.swing.GroupLayout rowBackgroun21Layout = new javax.swing.GroupLayout(rowBackgroun21);
        rowBackgroun21.setLayout(rowBackgroun21Layout);
        rowBackgroun21Layout.setHorizontalGroup(
            rowBackgroun21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r18, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun21Layout.setVerticalGroup(
            rowBackgroun21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun22.setPreferredSize(new java.awt.Dimension(30, 25));

        r25.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r25.setText("25");

        javax.swing.GroupLayout rowBackgroun22Layout = new javax.swing.GroupLayout(rowBackgroun22);
        rowBackgroun22.setLayout(rowBackgroun22Layout);
        rowBackgroun22Layout.setHorizontalGroup(
            rowBackgroun22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r25, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun22Layout.setVerticalGroup(
            rowBackgroun22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun23.setPreferredSize(new java.awt.Dimension(30, 25));

        r32.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r32.setText("32");

        javax.swing.GroupLayout rowBackgroun23Layout = new javax.swing.GroupLayout(rowBackgroun23);
        rowBackgroun23.setLayout(rowBackgroun23Layout);
        rowBackgroun23Layout.setHorizontalGroup(
            rowBackgroun23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r32, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun23Layout.setVerticalGroup(
            rowBackgroun23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun24.setPreferredSize(new java.awt.Dimension(30, 25));

        r39.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r39.setText("39");

        javax.swing.GroupLayout rowBackgroun24Layout = new javax.swing.GroupLayout(rowBackgroun24);
        rowBackgroun24.setLayout(rowBackgroun24Layout);
        rowBackgroun24Layout.setHorizontalGroup(
            rowBackgroun24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r39, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun24Layout.setVerticalGroup(
            rowBackgroun24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout column4Layout = new javax.swing.GroupLayout(column4);
        column4.setLayout(column4Layout);
        column4Layout.setHorizontalGroup(
            column4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dayBackground4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        column4Layout.setVerticalGroup(
            column4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(column4Layout.createSequentialGroup()
                .addComponent(dayBackground4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        column5.setBackground(new java.awt.Color(255, 153, 51));
        column5.setOpaque(false);

        dayBackground5.setPreferredSize(new java.awt.Dimension(30, 20));

        day5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        day5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day5.setText("Thu");

        javax.swing.GroupLayout dayBackground5Layout = new javax.swing.GroupLayout(dayBackground5);
        dayBackground5.setLayout(dayBackground5Layout);
        dayBackground5Layout.setHorizontalGroup(
            dayBackground5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day5, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        dayBackground5Layout.setVerticalGroup(
            dayBackground5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day5, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        rowBackgroun25.setPreferredSize(new java.awt.Dimension(30, 25));

        r5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r5.setText("5");

        javax.swing.GroupLayout rowBackgroun25Layout = new javax.swing.GroupLayout(rowBackgroun25);
        rowBackgroun25.setLayout(rowBackgroun25Layout);
        rowBackgroun25Layout.setHorizontalGroup(
            rowBackgroun25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r5, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun25Layout.setVerticalGroup(
            rowBackgroun25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun26.setPreferredSize(new java.awt.Dimension(30, 25));

        r12.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r12.setText("12");

        javax.swing.GroupLayout rowBackgroun26Layout = new javax.swing.GroupLayout(rowBackgroun26);
        rowBackgroun26.setLayout(rowBackgroun26Layout);
        rowBackgroun26Layout.setHorizontalGroup(
            rowBackgroun26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun26Layout.setVerticalGroup(
            rowBackgroun26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun27.setPreferredSize(new java.awt.Dimension(30, 25));

        r19.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r19.setText("19");

        javax.swing.GroupLayout rowBackgroun27Layout = new javax.swing.GroupLayout(rowBackgroun27);
        rowBackgroun27.setLayout(rowBackgroun27Layout);
        rowBackgroun27Layout.setHorizontalGroup(
            rowBackgroun27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r19, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun27Layout.setVerticalGroup(
            rowBackgroun27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun28.setPreferredSize(new java.awt.Dimension(30, 25));

        r26.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r26.setText("26");

        javax.swing.GroupLayout rowBackgroun28Layout = new javax.swing.GroupLayout(rowBackgroun28);
        rowBackgroun28.setLayout(rowBackgroun28Layout);
        rowBackgroun28Layout.setHorizontalGroup(
            rowBackgroun28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r26, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun28Layout.setVerticalGroup(
            rowBackgroun28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun29.setPreferredSize(new java.awt.Dimension(30, 25));

        r33.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r33.setText("33");

        javax.swing.GroupLayout rowBackgroun29Layout = new javax.swing.GroupLayout(rowBackgroun29);
        rowBackgroun29.setLayout(rowBackgroun29Layout);
        rowBackgroun29Layout.setHorizontalGroup(
            rowBackgroun29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r33, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun29Layout.setVerticalGroup(
            rowBackgroun29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun30.setPreferredSize(new java.awt.Dimension(30, 25));

        r40.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r40.setText("40");

        javax.swing.GroupLayout rowBackgroun30Layout = new javax.swing.GroupLayout(rowBackgroun30);
        rowBackgroun30.setLayout(rowBackgroun30Layout);
        rowBackgroun30Layout.setHorizontalGroup(
            rowBackgroun30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r40, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun30Layout.setVerticalGroup(
            rowBackgroun30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout column5Layout = new javax.swing.GroupLayout(column5);
        column5.setLayout(column5Layout);
        column5Layout.setHorizontalGroup(
            column5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dayBackground5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        column5Layout.setVerticalGroup(
            column5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(column5Layout.createSequentialGroup()
                .addComponent(dayBackground5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        column6.setBackground(new java.awt.Color(255, 153, 51));
        column6.setOpaque(false);

        dayBackground6.setPreferredSize(new java.awt.Dimension(30, 20));

        day6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        day6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day6.setText("Fri");

        javax.swing.GroupLayout dayBackground6Layout = new javax.swing.GroupLayout(dayBackground6);
        dayBackground6.setLayout(dayBackground6Layout);
        dayBackground6Layout.setHorizontalGroup(
            dayBackground6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day6, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        dayBackground6Layout.setVerticalGroup(
            dayBackground6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day6, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        rowBackgroun31.setPreferredSize(new java.awt.Dimension(30, 25));

        r6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r6.setText("6");

        javax.swing.GroupLayout rowBackgroun31Layout = new javax.swing.GroupLayout(rowBackgroun31);
        rowBackgroun31.setLayout(rowBackgroun31Layout);
        rowBackgroun31Layout.setHorizontalGroup(
            rowBackgroun31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r6, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun31Layout.setVerticalGroup(
            rowBackgroun31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun32.setPreferredSize(new java.awt.Dimension(30, 25));

        r13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r13.setText("13");

        javax.swing.GroupLayout rowBackgroun32Layout = new javax.swing.GroupLayout(rowBackgroun32);
        rowBackgroun32.setLayout(rowBackgroun32Layout);
        rowBackgroun32Layout.setHorizontalGroup(
            rowBackgroun32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r13, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun32Layout.setVerticalGroup(
            rowBackgroun32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun33.setPreferredSize(new java.awt.Dimension(30, 25));

        r20.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r20.setText("20");

        javax.swing.GroupLayout rowBackgroun33Layout = new javax.swing.GroupLayout(rowBackgroun33);
        rowBackgroun33.setLayout(rowBackgroun33Layout);
        rowBackgroun33Layout.setHorizontalGroup(
            rowBackgroun33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r20, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun33Layout.setVerticalGroup(
            rowBackgroun33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun34.setPreferredSize(new java.awt.Dimension(30, 25));

        r27.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r27.setText("27");

        javax.swing.GroupLayout rowBackgroun34Layout = new javax.swing.GroupLayout(rowBackgroun34);
        rowBackgroun34.setLayout(rowBackgroun34Layout);
        rowBackgroun34Layout.setHorizontalGroup(
            rowBackgroun34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r27, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun34Layout.setVerticalGroup(
            rowBackgroun34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun35.setPreferredSize(new java.awt.Dimension(30, 25));

        r34.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r34.setText("34");

        javax.swing.GroupLayout rowBackgroun35Layout = new javax.swing.GroupLayout(rowBackgroun35);
        rowBackgroun35.setLayout(rowBackgroun35Layout);
        rowBackgroun35Layout.setHorizontalGroup(
            rowBackgroun35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r34, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun35Layout.setVerticalGroup(
            rowBackgroun35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun36.setPreferredSize(new java.awt.Dimension(30, 25));

        r41.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r41.setText("41");

        javax.swing.GroupLayout rowBackgroun36Layout = new javax.swing.GroupLayout(rowBackgroun36);
        rowBackgroun36.setLayout(rowBackgroun36Layout);
        rowBackgroun36Layout.setHorizontalGroup(
            rowBackgroun36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r41, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun36Layout.setVerticalGroup(
            rowBackgroun36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout column6Layout = new javax.swing.GroupLayout(column6);
        column6.setLayout(column6Layout);
        column6Layout.setHorizontalGroup(
            column6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dayBackground6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        column6Layout.setVerticalGroup(
            column6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(column6Layout.createSequentialGroup()
                .addComponent(dayBackground6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        column8.setBackground(new java.awt.Color(255, 153, 51));
        column8.setOpaque(false);

        dayBackground7.setPreferredSize(new java.awt.Dimension(30, 20));

        day7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        day7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day7.setText("Sat");

        javax.swing.GroupLayout dayBackground7Layout = new javax.swing.GroupLayout(dayBackground7);
        dayBackground7.setLayout(dayBackground7Layout);
        dayBackground7Layout.setHorizontalGroup(
            dayBackground7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day7, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        dayBackground7Layout.setVerticalGroup(
            dayBackground7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day7, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        rowBackgroun37.setPreferredSize(new java.awt.Dimension(30, 25));

        r7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r7.setText("7");

        javax.swing.GroupLayout rowBackgroun37Layout = new javax.swing.GroupLayout(rowBackgroun37);
        rowBackgroun37.setLayout(rowBackgroun37Layout);
        rowBackgroun37Layout.setHorizontalGroup(
            rowBackgroun37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r7, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun37Layout.setVerticalGroup(
            rowBackgroun37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun38.setPreferredSize(new java.awt.Dimension(30, 25));

        r14.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r14.setText("14");

        javax.swing.GroupLayout rowBackgroun38Layout = new javax.swing.GroupLayout(rowBackgroun38);
        rowBackgroun38.setLayout(rowBackgroun38Layout);
        rowBackgroun38Layout.setHorizontalGroup(
            rowBackgroun38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r14, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun38Layout.setVerticalGroup(
            rowBackgroun38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun39.setPreferredSize(new java.awt.Dimension(30, 25));

        r21.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r21.setText("21");

        javax.swing.GroupLayout rowBackgroun39Layout = new javax.swing.GroupLayout(rowBackgroun39);
        rowBackgroun39.setLayout(rowBackgroun39Layout);
        rowBackgroun39Layout.setHorizontalGroup(
            rowBackgroun39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r21, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun39Layout.setVerticalGroup(
            rowBackgroun39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun40.setPreferredSize(new java.awt.Dimension(30, 25));

        r28.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r28.setText("28");

        javax.swing.GroupLayout rowBackgroun40Layout = new javax.swing.GroupLayout(rowBackgroun40);
        rowBackgroun40.setLayout(rowBackgroun40Layout);
        rowBackgroun40Layout.setHorizontalGroup(
            rowBackgroun40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r28, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun40Layout.setVerticalGroup(
            rowBackgroun40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun41.setPreferredSize(new java.awt.Dimension(30, 25));

        r35.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r35.setText("35");

        javax.swing.GroupLayout rowBackgroun41Layout = new javax.swing.GroupLayout(rowBackgroun41);
        rowBackgroun41.setLayout(rowBackgroun41Layout);
        rowBackgroun41Layout.setHorizontalGroup(
            rowBackgroun41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r35, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun41Layout.setVerticalGroup(
            rowBackgroun41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rowBackgroun42.setPreferredSize(new java.awt.Dimension(30, 25));

        r42.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        r42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r42.setText("42");

        javax.swing.GroupLayout rowBackgroun42Layout = new javax.swing.GroupLayout(rowBackgroun42);
        rowBackgroun42.setLayout(rowBackgroun42Layout);
        rowBackgroun42Layout.setHorizontalGroup(
            rowBackgroun42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r42, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        rowBackgroun42Layout.setVerticalGroup(
            rowBackgroun42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(r42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout column8Layout = new javax.swing.GroupLayout(column8);
        column8.setLayout(column8Layout);
        column8Layout.setHorizontalGroup(
            column8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dayBackground7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(rowBackgroun42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        column8Layout.setVerticalGroup(
            column8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(column8Layout.createSequentialGroup()
                .addComponent(dayBackground7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rowBackgroun42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout weekViewLayout = new javax.swing.GroupLayout(weekView);
        weekView.setLayout(weekViewLayout);
        weekViewLayout.setHorizontalGroup(
            weekViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weekViewLayout.createSequentialGroup()
                .addComponent(column1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(column2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(column3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(column4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(column5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(column6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(column8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        weekViewLayout.setVerticalGroup(
            weekViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(column1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(column2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(column3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(column4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(column5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(column6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(column8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout datePickerContainerLayout = new javax.swing.GroupLayout(datePickerContainer);
        datePickerContainer.setLayout(datePickerContainerLayout);
        datePickerContainerLayout.setHorizontalGroup(
            datePickerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 210, Short.MAX_VALUE)
            .addGroup(datePickerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(datePickerContainerLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(weekView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        datePickerContainerLayout.setVerticalGroup(
            datePickerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 170, Short.MAX_VALUE)
            .addGroup(datePickerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(datePickerContainerLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(weekView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout datePickerLayout = new javax.swing.GroupLayout(datePicker);
        datePicker.setLayout(datePickerLayout);
        datePickerLayout.setHorizontalGroup(
            datePickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(datePickerContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        datePickerLayout.setVerticalGroup(
            datePickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datePickerLayout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(datePickerContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout datePickerShadowLayout = new javax.swing.GroupLayout(datePickerShadow);
        datePickerShadow.setLayout(datePickerShadowLayout);
        datePickerShadowLayout.setHorizontalGroup(
            datePickerShadowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datePickerShadowLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        datePickerShadowLayout.setVerticalGroup(
            datePickerShadowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datePickerShadowLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(datePickerShadow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(datePickerShadow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void titleMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMouseEntered
        headerHover(title);
    }//GEN-LAST:event_titleMouseEntered

    private void titleMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMouseExited
        headerNormal(title);
    }//GEN-LAST:event_titleMouseExited

    private void titleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMousePressed
        if(evt.getButton()==1){
            headerPressed(title);
        }
    }//GEN-LAST:event_titleMousePressed

    private void titleMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMouseReleased
        if(evt.getButton()==1){
            headerNormal(title);
            changeView();
        }
    }//GEN-LAST:event_titleMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel column1;
    private javax.swing.JPanel column2;
    private javax.swing.JPanel column3;
    private javax.swing.JPanel column4;
    private javax.swing.JPanel column5;
    private javax.swing.JPanel column6;
    private javax.swing.JPanel column8;
    private javax.swing.JPanel datePicker;
    private javax.swing.JPanel datePickerContainer;
    private javax.swing.JPanel datePickerShadow;
    private javax.swing.JLabel day1;
    private javax.swing.JLabel day2;
    private javax.swing.JLabel day3;
    private javax.swing.JLabel day4;
    private javax.swing.JLabel day5;
    private javax.swing.JLabel day6;
    private javax.swing.JLabel day7;
    private javax.swing.JPanel dayBackground1;
    private javax.swing.JPanel dayBackground2;
    private javax.swing.JPanel dayBackground3;
    private javax.swing.JPanel dayBackground4;
    private javax.swing.JPanel dayBackground5;
    private javax.swing.JPanel dayBackground6;
    private javax.swing.JPanel dayBackground7;
    private javax.swing.JPanel header;
    private javax.swing.JPanel mb1;
    private javax.swing.JPanel mb10;
    private javax.swing.JPanel mb11;
    private javax.swing.JPanel mb12;
    private javax.swing.JPanel mb13;
    private javax.swing.JPanel mb14;
    private javax.swing.JPanel mb15;
    private javax.swing.JPanel mb16;
    private javax.swing.JPanel mb17;
    private javax.swing.JPanel mb18;
    private javax.swing.JPanel mb19;
    private javax.swing.JPanel mb2;
    private javax.swing.JPanel mb20;
    private javax.swing.JPanel mb21;
    private javax.swing.JPanel mb22;
    private javax.swing.JPanel mb23;
    private javax.swing.JPanel mb24;
    private javax.swing.JPanel mb3;
    private javax.swing.JPanel mb4;
    private javax.swing.JPanel mb5;
    private javax.swing.JPanel mb6;
    private javax.swing.JPanel mb7;
    private javax.swing.JPanel mb8;
    private javax.swing.JPanel mb9;
    private javax.swing.JLabel ml1;
    private javax.swing.JLabel ml10;
    private javax.swing.JLabel ml11;
    private javax.swing.JLabel ml12;
    private javax.swing.JLabel ml2;
    private javax.swing.JLabel ml3;
    private javax.swing.JLabel ml4;
    private javax.swing.JLabel ml5;
    private javax.swing.JLabel ml6;
    private javax.swing.JLabel ml7;
    private javax.swing.JLabel ml8;
    private javax.swing.JLabel ml9;
    private javax.swing.JPanel monthView;
    private javax.swing.JLabel next;
    private javax.swing.JLabel prev;
    private javax.swing.JLabel r1;
    private javax.swing.JLabel r10;
    private javax.swing.JLabel r11;
    private javax.swing.JLabel r12;
    private javax.swing.JLabel r13;
    private javax.swing.JLabel r14;
    private javax.swing.JLabel r15;
    private javax.swing.JLabel r16;
    private javax.swing.JLabel r17;
    private javax.swing.JLabel r18;
    private javax.swing.JLabel r19;
    private javax.swing.JLabel r2;
    private javax.swing.JLabel r20;
    private javax.swing.JLabel r21;
    private javax.swing.JLabel r22;
    private javax.swing.JLabel r23;
    private javax.swing.JLabel r24;
    private javax.swing.JLabel r25;
    private javax.swing.JLabel r26;
    private javax.swing.JLabel r27;
    private javax.swing.JLabel r28;
    private javax.swing.JLabel r29;
    private javax.swing.JLabel r3;
    private javax.swing.JLabel r30;
    private javax.swing.JLabel r31;
    private javax.swing.JLabel r32;
    private javax.swing.JLabel r33;
    private javax.swing.JLabel r34;
    private javax.swing.JLabel r35;
    private javax.swing.JLabel r36;
    private javax.swing.JLabel r37;
    private javax.swing.JLabel r38;
    private javax.swing.JLabel r39;
    private javax.swing.JLabel r4;
    private javax.swing.JLabel r40;
    private javax.swing.JLabel r41;
    private javax.swing.JLabel r42;
    private javax.swing.JLabel r5;
    private javax.swing.JLabel r6;
    private javax.swing.JLabel r7;
    private javax.swing.JLabel r8;
    private javax.swing.JLabel r9;
    private javax.swing.JPanel rowBackgroun1;
    private javax.swing.JPanel rowBackgroun10;
    private javax.swing.JPanel rowBackgroun11;
    private javax.swing.JPanel rowBackgroun12;
    private javax.swing.JPanel rowBackgroun13;
    private javax.swing.JPanel rowBackgroun14;
    private javax.swing.JPanel rowBackgroun15;
    private javax.swing.JPanel rowBackgroun16;
    private javax.swing.JPanel rowBackgroun17;
    private javax.swing.JPanel rowBackgroun18;
    private javax.swing.JPanel rowBackgroun19;
    private javax.swing.JPanel rowBackgroun2;
    private javax.swing.JPanel rowBackgroun20;
    private javax.swing.JPanel rowBackgroun21;
    private javax.swing.JPanel rowBackgroun22;
    private javax.swing.JPanel rowBackgroun23;
    private javax.swing.JPanel rowBackgroun24;
    private javax.swing.JPanel rowBackgroun25;
    private javax.swing.JPanel rowBackgroun26;
    private javax.swing.JPanel rowBackgroun27;
    private javax.swing.JPanel rowBackgroun28;
    private javax.swing.JPanel rowBackgroun29;
    private javax.swing.JPanel rowBackgroun3;
    private javax.swing.JPanel rowBackgroun30;
    private javax.swing.JPanel rowBackgroun31;
    private javax.swing.JPanel rowBackgroun32;
    private javax.swing.JPanel rowBackgroun33;
    private javax.swing.JPanel rowBackgroun34;
    private javax.swing.JPanel rowBackgroun35;
    private javax.swing.JPanel rowBackgroun36;
    private javax.swing.JPanel rowBackgroun37;
    private javax.swing.JPanel rowBackgroun38;
    private javax.swing.JPanel rowBackgroun39;
    private javax.swing.JPanel rowBackgroun4;
    private javax.swing.JPanel rowBackgroun40;
    private javax.swing.JPanel rowBackgroun41;
    private javax.swing.JPanel rowBackgroun42;
    private javax.swing.JPanel rowBackgroun5;
    private javax.swing.JPanel rowBackgroun6;
    private javax.swing.JPanel rowBackgroun7;
    private javax.swing.JPanel rowBackgroun8;
    private javax.swing.JPanel rowBackgroun9;
    private javax.swing.JLabel title;
    private javax.swing.JPanel weekView;
    private javax.swing.JLabel y1;
    private javax.swing.JLabel y10;
    private javax.swing.JLabel y11;
    private javax.swing.JLabel y12;
    private javax.swing.JLabel y2;
    private javax.swing.JLabel y3;
    private javax.swing.JLabel y4;
    private javax.swing.JLabel y5;
    private javax.swing.JLabel y6;
    private javax.swing.JLabel y7;
    private javax.swing.JLabel y8;
    private javax.swing.JLabel y9;
    private javax.swing.JPanel yearView;
    // End of variables declaration//GEN-END:variables
}
