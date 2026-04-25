package VIEW;

import DTO.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarUI extends JFrame {
    private User currentUser;
    private YearMonth currentYearMonth;
    private LocalDate selectedDate;

    private JComboBox<DTO.Calendar> comboCalendars;
    private DTO.Calendar currentCalendar;

    private JComboBox<String> comboMonth;
    private JSpinner spinYear;
    private JPanel panelDays;

    private boolean isUpdatingUI = false;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private AppointmentListPanel appointmentListPanel;
    private ReminderListPanel centralPanel;

    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_HOVER = new Color(225, 238, 255);
    private final Color COLOR_SELECTED = new Color(190, 220, 255);
    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_BORDER = new Color(226, 232, 240);

    private RoundedButton btnCalendar;
    private RoundedButton btnListAppointments;
    private RoundedButton btnListReminders;
    private RoundedButton btnPrev;
    private RoundedButton btnNext;
    private RoundedButton btnToday;
    private RoundedButton btnAddAppointment;

    public CalendarUI(User user) {
        this.currentUser = user;
        this.currentYearMonth = YearMonth.now();
        this.selectedDate = LocalDate.now();

        setTitle("Appointment Calendar");
        setSize(1150, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        initComponents();
        refreshCalendarList();
    }

    private void initComponents() {
        // --- SIDEBAR ---
        JPanel panelSidebar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panelSidebar.setBackground(Color.WHITE);
        panelSidebar.setPreferredSize(new Dimension(240, 0));
        panelSidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER),
                new EmptyBorder(20, 10, 20, 10)
        ));

        JLabel lblMenu = new JLabel("MENU CHÍNH");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblMenu.setForeground(Color.GRAY);

        btnCalendar = createSidebarButton("Lịch của tôi", true);
        btnListAppointments = createSidebarButton("Danh sách Cuộc hẹn", false);
        btnListReminders = createSidebarButton("Trung tâm thông báo", false);

        panelSidebar.add(lblMenu);
        panelSidebar.add(btnCalendar);
        panelSidebar.add(btnListAppointments);
        panelSidebar.add(btnListReminders);

        add(panelSidebar, BorderLayout.WEST);

        // --- HEADER ---
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelHeader.setBackground(COLOR_BG);

        // Calendar Select Controls
        JPanel panelCalendarControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelCalendarControls.setBackground(COLOR_BG);

        comboCalendars = new JComboBox<>();
        comboCalendars.setPreferredSize(new Dimension(200, 42));
        comboCalendars.setFont(new Font("Segoe UI", Font.BOLD, 15));
        comboCalendars.setBackground(Color.WHITE);
        comboCalendars.setForeground(COLOR_TEXT_DARK);
        comboCalendars.setFocusable(false);

        comboCalendars.addActionListener(e -> {
            if (comboCalendars.getSelectedItem() != null) {
                this.currentCalendar = (DTO.Calendar) comboCalendars.getSelectedItem();
                int cid = currentCalendar.getCalendarId();

                if (appointmentListPanel != null) {
                    appointmentListPanel.loadData(cid);
                }
                if (centralPanel != null) {
                    centralPanel.refreshReminders(cid);
                }

                renderCalendar();
            }
        });

        RoundedButton btnAddCal = new RoundedButton("+", 10, COLOR_PRIMARY, 1);
        btnAddCal.setPreferredSize(new Dimension(45, 42));
        btnAddCal.setBackground(COLOR_PRIMARY);
        btnAddCal.setForeground(Color.WHITE);
        btnAddCal.setFont(new Font("Segoe UI", Font.BOLD, 26));
        btnAddCal.setFocusPainted(false);
        btnAddCal.setMargin(new Insets(0, 0, 0, 0));

        RoundedButton btnDelCal = new RoundedButton("×", 10, new Color(220, 53, 69), 1);
        btnDelCal.setPreferredSize(new Dimension(45, 42));
        btnDelCal.setBackground(new Color(220, 53, 69));
        btnDelCal.setForeground(Color.WHITE);
        btnDelCal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btnDelCal.setFocusPainted(false);
        btnDelCal.setMargin(new Insets(0, 0, 0, 0));

        panelCalendarControls.add(comboCalendars);
        panelCalendarControls.add(btnAddCal);
        panelCalendarControls.add(btnDelCal);

        btnAddCal.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Nhập tên lịch mới:");
            if (name != null && !name.trim().isEmpty()) {
                String res = BLL.CalendarManager.addCalendar(currentUser.getUserId(), name);
                if (res.equals("SUCCESS")) {
                    refreshCalendarList();
                } else {
                    JOptionPane.showMessageDialog(this, res);
                }
            }
        });

        btnDelCal.addActionListener(e -> {
            DTO.Calendar selected = (DTO.Calendar) comboCalendars.getSelectedItem();
            if (selected != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Xóa lịch '" + selected.getName() + "' sẽ xóa toàn bộ cuộc hẹn bên trong. Tiếp tục?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String res = BLL.CalendarManager.deleteCalendar(currentUser.getUserId(), selected.getCalendarId());
                    if (res.equals("SUCCESS")) {
                        refreshCalendarList();
                    } else {
                        JOptionPane.showMessageDialog(this, res);
                    }
                }
            }
        });

        // Navigation Controls
        btnPrev = createNavButton("<");
        btnNext = createNavButton(">");

        String[] months = new String[12];
        for (int i = 0; i < 12; i++) months[i] = "Tháng " + (i + 1);

        comboMonth = new JComboBox<>(months);
        comboMonth.setFont(new Font("Segoe UI", Font.BOLD, 16));
        comboMonth.setForeground(COLOR_TEXT_DARK);
        comboMonth.setBackground(Color.WHITE);
        comboMonth.setPreferredSize(new Dimension(130, 40));

        spinYear = new JSpinner(new SpinnerNumberModel(currentYearMonth.getYear(), 1900, 2100, 1));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinYear, "#");
        spinYear.setEditor(editor);
        spinYear.setFont(new Font("Segoe UI", Font.BOLD, 16));
        spinYear.setPreferredSize(new Dimension(90, 40));

        btnToday = new RoundedButton("Hôm nay", 10, COLOR_PRIMARY, 1);
        btnToday.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnToday.setBackground(COLOR_PRIMARY);
        btnToday.setForeground(Color.WHITE);
        btnToday.setPreferredSize(new Dimension(100, 40));

        panelHeader.add(panelCalendarControls);
        panelHeader.add(Box.createHorizontalStrut(10));
        panelHeader.add(btnPrev);
        panelHeader.add(comboMonth);
        panelHeader.add(spinYear);
        panelHeader.add(btnNext);
        panelHeader.add(Box.createHorizontalStrut(20));
        panelHeader.add(btnToday);

        // --- MAIN CONTENT AREA ---
        JPanel panelMainContent = new JPanel(new BorderLayout());
        panelMainContent.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(COLOR_BG);
        panelCenter.setBorder(new EmptyBorder(10, 30, 10, 30));

        panelDays = new JPanel(new GridLayout(0, 7, 10, 10));
        panelDays.setBackground(COLOR_BG);
        panelCenter.add(panelDays, BorderLayout.CENTER);

        panelMainContent.add(panelCenter, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        panelFooter.setBackground(COLOR_BG);

        btnAddAppointment = new RoundedButton("+ Thêm Cuộc Hẹn", 20, COLOR_PRIMARY, 1);
        btnAddAppointment.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAddAppointment.setBackground(COLOR_PRIMARY);
        btnAddAppointment.setForeground(Color.WHITE);
        btnAddAppointment.setPreferredSize(new Dimension(200, 45));

        panelFooter.add(btnAddAppointment);
        panelMainContent.add(panelFooter, BorderLayout.SOUTH);

        // --- CARD LAYOUT CONFIG ---
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        appointmentListPanel = new AppointmentListPanel(currentUser, 0);
        centralPanel = new ReminderListPanel(currentUser.getUserId(), 0);

        cardPanel.add(panelMainContent, "CALENDAR_VIEW");
        cardPanel.add(appointmentListPanel, "LIST_VIEW");
        cardPanel.add(centralPanel, "REMINDER_VIEW");

        add(cardPanel, BorderLayout.CENTER);

        // --- ACTIONS ---
        btnCalendar.addActionListener(e -> {
            cardLayout.show(cardPanel, "CALENDAR_VIEW");
            updateSidebarActive(btnCalendar);
            renderCalendar();
        });

        btnListAppointments.addActionListener(e -> {
            if (currentCalendar != null) {
                appointmentListPanel.loadData(currentCalendar.getCalendarId());
            }
            cardLayout.show(cardPanel, "LIST_VIEW");
            updateSidebarActive(btnListAppointments);
        });

        btnListReminders.addActionListener(e -> {
            if (currentCalendar != null) {
                centralPanel.refreshReminders(currentCalendar.getCalendarId());
            }
            cardLayout.show(cardPanel, "REMINDER_VIEW");
            updateSidebarActive(btnListReminders);
        });

        btnPrev.addActionListener(e -> { currentYearMonth = currentYearMonth.minusMonths(1); renderCalendar(); });
        btnNext.addActionListener(e -> { currentYearMonth = currentYearMonth.plusMonths(1); renderCalendar(); });

        btnToday.addActionListener(e -> {
            currentYearMonth = YearMonth.now();
            selectedDate = LocalDate.now();
            renderCalendar();
        });

        comboMonth.addActionListener(e -> jumpToSelectedDate());
        spinYear.addChangeListener(e -> jumpToSelectedDate());
        btnAddAppointment.addActionListener(e -> handleAddAppointment());
    }

    private void updateSidebarActive(RoundedButton activeBtn) {
        RoundedButton[] buttons = {btnCalendar, btnListAppointments, btnListReminders};
        for (RoundedButton btn : buttons) {
            if (btn == activeBtn) {
                btn.setBackground(COLOR_PRIMARY);
                btn.setForeground(Color.WHITE);
                btn.setCustomBorder(COLOR_PRIMARY, 1);
            } else {
                btn.setBackground(Color.WHITE);
                btn.setForeground(COLOR_TEXT_DARK);
                btn.setCustomBorder(Color.WHITE, 1);
            }
        }
    }

    private void jumpToSelectedDate() {
        if (isUpdatingUI) return;
        int month = comboMonth.getSelectedIndex() + 1;
        int year = (int) spinYear.getValue();
        currentYearMonth = YearMonth.of(year, month);
        renderCalendar();
    }

    private RoundedButton createSidebarButton(String text, boolean isActive) {
        RoundedButton btn = new RoundedButton(text, 12, isActive ? COLOR_PRIMARY : Color.WHITE, 1);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(210, 45));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));

        if (isActive) {
            btn.setBackground(COLOR_PRIMARY);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(COLOR_TEXT_DARK);
        }

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(COLOR_PRIMARY)) btn.setBackground(COLOR_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(COLOR_PRIMARY)) btn.setBackground(Color.WHITE);
            }
        });
        return btn;
    }

    private RoundedButton createNavButton(String text) {
        RoundedButton btn = new RoundedButton(text, 10, COLOR_PRIMARY, 1);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setPreferredSize(new Dimension(50, 40));
        return btn;
    }

    private void refreshCalendarList() {
        List<DTO.Calendar> list = DAL.CalendarDAL.getCalendarsByUserId(currentUser.getUserId());

        if (list.isEmpty()) {
            BLL.CalendarManager.addCalendar(currentUser.getUserId(), "Lịch mặc định");
            list = DAL.CalendarDAL.getCalendarsByUserId(currentUser.getUserId());
        }

        comboCalendars.setModel(new DefaultComboBoxModel<>(list.toArray(new DTO.Calendar[0])));
        if (comboCalendars.getItemCount() > 0) {
            comboCalendars.setSelectedIndex(0);
            this.currentCalendar = (DTO.Calendar) comboCalendars.getSelectedItem();
        }
        renderCalendar();
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

        for (int i = 0; i < paddingDays; i++) panelDays.add(new JLabel(""));

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            RoundedButton btnDay = new RoundedButton(String.valueOf(day), 15, COLOR_BORDER, 1);

            LocalDate thisButtonDate = currentYearMonth.atDay(day);
            boolean isSunday = ((paddingDays + day - 1) % 7 == 0);
            boolean isToday = thisButtonDate.equals(LocalDate.now());
            boolean isSelected = thisButtonDate.equals(selectedDate);

            if (isToday) {
                btnDay.setBackground(COLOR_PRIMARY);
                btnDay.setForeground(Color.WHITE);
                btnDay.setFont(new Font("Segoe UI", Font.BOLD, 22));
                btnDay.setCustomBorder(isSelected ? Color.BLACK : COLOR_PRIMARY, isSelected ? 2 : 1);
            }
            else if (isSelected) {
                btnDay.setBackground(COLOR_SELECTED);
                btnDay.setForeground(COLOR_PRIMARY);
                btnDay.setFont(new Font("Segoe UI", Font.BOLD, 20));
                btnDay.setCustomBorder(COLOR_PRIMARY, 2);
            }
            else {
                btnDay.setBackground(Color.WHITE);
                btnDay.setForeground(isSunday ? new Color(220, 53, 69) : COLOR_TEXT_DARK);
                btnDay.setFont(new Font("Segoe UI", Font.BOLD, 18));
                btnDay.setCustomBorder(COLOR_BORDER, 1);
            }

            btnDay.addActionListener(e -> {
                selectedDate = thisButtonDate;
                renderCalendar();
            });

            panelDays.add(btnDay);
        }

        panelDays.revalidate();
        panelDays.repaint();
    }

    private void handleAddAppointment() {
        if (selectedDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Không thể thêm cuộc hẹn cho quá khứ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        AppointmentDialog addForm = new AppointmentDialog(this, true, currentUser, selectedDate, currentCalendar.getCalendarId(), null);
        addForm.setVisible(true);
        renderCalendar();
    }
}