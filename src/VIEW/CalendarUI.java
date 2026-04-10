/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

/**
 *
 * @author Admin
 */

import DTO.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarUI extends JFrame {
    private User currentUser;
    private YearMonth currentYearMonth;
    
    private JComboBox<String> comboMonth;
    private JSpinner spinYear;
    private JPanel panelDays;
    
    private boolean isUpdatingUI = false;

    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_HOVER = new Color(230, 240, 255);
    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_BORDER = new Color(226, 232, 240);

    public CalendarUI(User user) {
        this.currentUser = user;
        this.currentYearMonth = YearMonth.now();

        setTitle("Appointment Calendar");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        initComponents();
        renderCalendar();
    }

    private void initComponents() {
        JPanel panelSidebar = new JPanel();
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));
        panelSidebar.setBackground(Color.WHITE);
        panelSidebar.setPreferredSize(new Dimension(250, 0));
        panelSidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER),
                new EmptyBorder(20, 15, 20, 15)
        ));

        JLabel lblMenu = new JLabel("MENU CHÍNH");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMenu.setForeground(Color.GRAY);
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnCalendar = createSidebarButton("Lịch của tôi");
        btnCalendar.setBackground(COLOR_HOVER);
        btnCalendar.setForeground(COLOR_PRIMARY);

        JButton btnListAppointments = createSidebarButton("Danh sách Cuộc hẹn");
        JButton btnListReminders = createSidebarButton("Danh sách Nhắc nhở");

        panelSidebar.add(lblMenu);
        panelSidebar.add(Box.createVerticalStrut(20));
        panelSidebar.add(btnCalendar);
        panelSidebar.add(Box.createVerticalStrut(10));
        panelSidebar.add(btnListAppointments);
        panelSidebar.add(Box.createVerticalStrut(10));
        panelSidebar.add(btnListReminders);

        add(panelSidebar, BorderLayout.WEST);

        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        JButton btnPrev = createNavButton("<");
        JButton btnNext = createNavButton(">");

        String[] months = new String[12];
        for (int i = 0; i < 12; i++) months[i] = "Tháng " + (i + 1);
        
        comboMonth = new JComboBox<>(months);
        comboMonth.setFont(new Font("Segoe UI", Font.BOLD, 16));
        comboMonth.setForeground(COLOR_PRIMARY);
        comboMonth.setBackground(Color.WHITE);
        comboMonth.setPreferredSize(new Dimension(130, 40));

        spinYear = new JSpinner(new SpinnerNumberModel(currentYearMonth.getYear(), 1900, 2100, 1));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinYear, "#");
        spinYear.setEditor(editor);
        spinYear.setFont(new Font("Segoe UI", Font.BOLD, 16));
        spinYear.setPreferredSize(new Dimension(90, 40));

        JButton btnToday = new JButton("Hôm nay");
        btnToday.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnToday.setBackground(Color.WHITE);
        btnToday.setForeground(COLOR_PRIMARY);
        btnToday.setFocusPainted(false);
        btnToday.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToday.setPreferredSize(new Dimension(100, 40));
        btnToday.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 1));

        panelHeader.add(btnPrev);
        panelHeader.add(comboMonth);
        panelHeader.add(spinYear);
        panelHeader.add(btnNext);
        panelHeader.add(Box.createHorizontalStrut(20));
        panelHeader.add(btnToday);

        JPanel panelMainContent = new JPanel(new BorderLayout());
        panelMainContent.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(COLOR_BG);
        panelCenter.setBorder(new EmptyBorder(20, 30, 30, 30));

        panelDays = new JPanel(new GridLayout(0, 7, 10, 10)); 
        panelDays.setBackground(COLOR_BG);
        panelCenter.add(panelDays, BorderLayout.CENTER);

        panelMainContent.add(panelCenter, BorderLayout.CENTER);
        add(panelMainContent, BorderLayout.CENTER);

        btnPrev.addActionListener(e -> { currentYearMonth = currentYearMonth.minusMonths(1); renderCalendar(); });
        btnNext.addActionListener(e -> { currentYearMonth = currentYearMonth.plusMonths(1); renderCalendar(); });
        btnToday.addActionListener(e -> { currentYearMonth = YearMonth.now(); renderCalendar(); });

        comboMonth.addActionListener(e -> jumpToSelectedDate());
        spinYear.addChangeListener(e -> jumpToSelectedDate());

        btnListAppointments.addActionListener(e -> {
        });
        
        btnListReminders.addActionListener(e -> {
        });
    }

    private void jumpToSelectedDate() {
        if (isUpdatingUI) return;
        int month = comboMonth.getSelectedIndex() + 1;
        int year = (int) spinYear.getValue();
        currentYearMonth = YearMonth.of(year, month);
        renderCalendar();
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(COLOR_TEXT_DARK);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.getBackground() != COLOR_HOVER) {
                    btn.setBackground(new Color(240, 240, 240));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.getForeground() != COLOR_PRIMARY) {
                    btn.setBackground(Color.WHITE);
                }
            }
        });
        return btn;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(Color.WHITE);
        btn.setForeground(COLOR_PRIMARY);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        return btn;
    }

    private void renderCalendar() {
        panelDays.removeAll();

        isUpdatingUI = true;
        comboMonth.setSelectedIndex(currentYearMonth.getMonthValue() - 1);
        spinYear.setValue(currentYearMonth.getYear());
        isUpdatingUI = false;

        String[] daysOfWeek = {"Chủ Nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            JLabel lblDay = new JLabel(daysOfWeek[i], SwingConstants.CENTER);
            lblDay.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblDay.setForeground(i == 0 ? new Color(220, 53, 69) : COLOR_TEXT_DARK); 
            panelDays.add(lblDay);
        }

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue();
        int paddingDays = (dayOfWeekValue == 7) ? 0 : dayOfWeekValue;

        for (int i = 0; i < paddingDays; i++) {
            panelDays.add(new JLabel(""));
        }

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            JButton btnDay = new JButton(String.valueOf(day));
            btnDay.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btnDay.setFocusPainted(false);
            btnDay.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            boolean isSunday = ((paddingDays + day - 1) % 7 == 0);
            
            if (currentYearMonth.equals(YearMonth.now()) && day == LocalDate.now().getDayOfMonth()) {
                btnDay.setForeground(COLOR_PRIMARY);
                btnDay.setFont(new Font("Segoe UI", Font.BOLD, 22));
                btnDay.setBackground(new Color(240, 248, 255));
                
                btnDay.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARY, 3),
                        new EmptyBorder(5, 5, 5, 5)
                ));
            } else {
                btnDay.setForeground(isSunday ? new Color(220, 53, 69) : COLOR_TEXT_DARK);
                btnDay.setBackground(Color.WHITE);
                btnDay.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
                
                btnDay.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnDay.setBackground(COLOR_HOVER);
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnDay.setBackground(Color.WHITE);
                    }
                });
            }

            final int selectedDay = day;
            btnDay.addActionListener(e -> {
                LocalDate selectedDate = currentYearMonth.atDay(selectedDay);
                System.out.println("Chuẩn bị thêm lịch cho ngày: " + selectedDate);
            });

            panelDays.add(btnDay);
        }

        panelDays.revalidate();
        panelDays.repaint();
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception ex) { ex.printStackTrace(); }
        
        SwingUtilities.invokeLater(() -> {
            User mockUser = new User();
            mockUser.setUsername("Dev Test");
            new CalendarUI(mockUser).setVisible(true);
        });
    }
}
