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

    private Central centralWindow = null;

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

    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_HOVER = new Color(225, 238, 255);
    private final Color COLOR_SELECTED = new Color(190, 220, 255);
    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_BORDER = new Color(226, 232, 240);

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
        renderCalendar();
    }

    private void initComponents() {
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

        RoundedButton btnCalendar = createSidebarButton("Lịch của tôi", true);
        RoundedButton btnListAppointments = createSidebarButton("Danh sách Cuộc hẹn", false);

        //i suppose this is my part?
        //purpose: show all reminders whose time remaining before their dates is less than 24 hours
        RoundedButton btnListReminders = createSidebarButton("Trung tâm thông báo", false);

        panelSidebar.add(lblMenu);
        panelSidebar.add(btnCalendar);
        panelSidebar.add(btnListAppointments);
        panelSidebar.add(btnListReminders);

        add(panelSidebar, BorderLayout.WEST);

        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelHeader.setBackground(COLOR_BG);

        List<DTO.Calendar> userCalendars = DAL.CalendarDAL.getCalendarsByUserId(currentUser.getUserId());
        
        if (userCalendars.isEmpty()) {
            userCalendars.add(new DTO.Calendar(1, currentUser.getUserId(), "Lịch Mặc Định", "Asia/Ho_Chi_Minh"));
        }

        comboCalendars = new JComboBox<>(userCalendars.toArray(new DTO.Calendar[0]));
        comboCalendars.setFont(new Font("Segoe UI", Font.BOLD, 16));
        comboCalendars.setForeground(COLOR_PRIMARY);
        comboCalendars.setBackground(Color.WHITE);
        comboCalendars.setPreferredSize(new Dimension(180, 40));

        currentCalendar = (DTO.Calendar) comboCalendars.getSelectedItem();

        comboCalendars.addActionListener(e -> {
            currentCalendar = (DTO.Calendar) comboCalendars.getSelectedItem();
            renderCalendar();
        });

        RoundedButton btnPrev = createNavButton("<");
        RoundedButton btnNext = createNavButton(">");

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

        RoundedButton btnToday = new RoundedButton("Hôm nay", 10, COLOR_PRIMARY, 1);
        btnToday.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnToday.setBackground(COLOR_PRIMARY);
        btnToday.setForeground(Color.WHITE);
        btnToday.setPreferredSize(new Dimension(100, 40));

        panelHeader.add(comboCalendars);
        panelHeader.add(Box.createHorizontalStrut(10));
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
        panelCenter.setBorder(new EmptyBorder(10, 30, 10, 30));

        panelDays = new JPanel(new GridLayout(0, 7, 10, 10));
        panelDays.setBackground(COLOR_BG);
        panelCenter.add(panelDays, BorderLayout.CENTER);

        panelMainContent.add(panelCenter, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        panelFooter.setBackground(COLOR_BG);

        RoundedButton btnAddAppointment = new RoundedButton("+ Thêm Cuộc Hẹn", 20, COLOR_PRIMARY, 1);
        btnAddAppointment.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAddAppointment.setBackground(COLOR_PRIMARY);
        btnAddAppointment.setForeground(Color.WHITE);
        btnAddAppointment.setPreferredSize(new Dimension(200, 45));

        panelFooter.add(btnAddAppointment);
        panelMainContent.add(panelFooter, BorderLayout.SOUTH);

        // --- SỰ KIỆN CLICK CHO MENU BÊN TRÁI ---
        btnCalendar.addActionListener(e -> {
            cardLayout.show(cardPanel, "CALENDAR_VIEW");
            renderCalendar(); // Refresh lại lịch

            // Set nút Lịch thành Active (Đổi nền, chữ, viền)
            btnCalendar.setBackground(COLOR_PRIMARY);
            btnCalendar.setForeground(Color.WHITE);
            btnCalendar.setCustomBorder(COLOR_PRIMARY, 1);

            // Reset nút Danh sách về bình thường
            btnListAppointments.setBackground(Color.WHITE);
            btnListAppointments.setForeground(COLOR_TEXT_DARK);
            btnListAppointments.setCustomBorder(Color.WHITE, 1);

            // Reset nút Thông báo về bình thường (nếu có dùng)
            btnListReminders.setBackground(Color.WHITE);
            btnListReminders.setForeground(COLOR_TEXT_DARK);
            btnListReminders.setCustomBorder(Color.WHITE, 1);
        });

        btnListAppointments.addActionListener(e -> {
            appointmentListPanel.loadData(); // Load data mới nhất từ Database
            cardLayout.show(cardPanel, "LIST_VIEW");

            // Set nút Danh sách thành Active (Đổi nền, chữ, viền)
            btnListAppointments.setBackground(COLOR_PRIMARY);
            btnListAppointments.setForeground(Color.WHITE);
            btnListAppointments.setCustomBorder(COLOR_PRIMARY, 1);

            // Reset nút Lịch về bình thường
            btnCalendar.setBackground(Color.WHITE);
            btnCalendar.setForeground(COLOR_TEXT_DARK);
            btnCalendar.setCustomBorder(Color.WHITE, 1);

            // Reset nút Thông báo về bình thường
            btnListReminders.setBackground(Color.WHITE);
            btnListReminders.setForeground(COLOR_TEXT_DARK);
            btnListReminders.setCustomBorder(Color.WHITE, 1);
        });

        //SUNNY'S PART
        Central centralPanel = new Central(currentUser.getUserId());
        btnListReminders.addActionListener(e -> {
            //refresh
            centralPanel.refreshReminders();
            //change view
            cardLayout.show(cardPanel, "REMINDER_VIEW");

            //set selected buttons as active
            btnListReminders.setBackground(COLOR_PRIMARY);
            btnListReminders.setForeground(Color.WHITE);
            btnListReminders.setCustomBorder(COLOR_PRIMARY, 1);

            //set unactive buttons
            btnCalendar.setBackground(Color.WHITE);
            btnCalendar.setForeground(COLOR_TEXT_DARK);
            btnCalendar.setCustomBorder(Color.WHITE, 1);

            btnListAppointments.setBackground(Color.WHITE);
            btnListAppointments.setForeground(COLOR_TEXT_DARK);
            btnListAppointments.setCustomBorder(Color.WHITE, 1);
        });

        // --- THAY THẾ BẰNG ĐOẠN SAU ---
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);


        appointmentListPanel = new AppointmentListPanel(currentUser);

        // Thêm cả panel Lịch và panel Danh sách vào CardLayout
        cardPanel.add(panelMainContent, "CALENDAR_VIEW");
        cardPanel.add(appointmentListPanel, "LIST_VIEW");
        cardPanel.add(centralPanel, "REMINDER_VIEW");


        // Đưa cardPanel ra giữa màn hình
        add(cardPanel, BorderLayout.CENTER);


        btnPrev.addActionListener(e -> { currentYearMonth = currentYearMonth.minusMonths(1); renderCalendar(); });
        btnNext.addActionListener(e -> { currentYearMonth = currentYearMonth.plusMonths(1); renderCalendar(); });

        btnToday.addActionListener(e -> {
            currentYearMonth = YearMonth.now();
            selectedDate = LocalDate.now();
            renderCalendar();
        });

        comboMonth.addActionListener(e -> jumpToSelectedDate());
        spinYear.addChangeListener(e -> jumpToSelectedDate());

        btnAddAppointment.addActionListener(e -> {
            AddAppointmentWindow addForm = new AddAppointmentWindow(CalendarUI.this, true, currentUser, selectedDate, currentCalendar.getCalendarId());
            addForm.setVisible(true);
            renderCalendar();
        });
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
                // Kiểm tra nếu nút đang KHÔNG được chọn (màu nền không phải xanh) thì mới hover màu nhạt
                if (!btn.getBackground().equals(COLOR_PRIMARY)) {
                    btn.setBackground(COLOR_HOVER);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                // Khi chuột rời đi, nếu nút đang KHÔNG được chọn thì trả về màu trắng
                if (!btn.getBackground().equals(COLOR_PRIMARY)) {
                    btn.setBackground(Color.WHITE);
                }
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
            RoundedButton btnDay = new RoundedButton(String.valueOf(day), 15, COLOR_BORDER, 1);

            LocalDate thisButtonDate = currentYearMonth.atDay(day);
            boolean isSunday = ((paddingDays + day - 1) % 7 == 0);
            boolean isToday = thisButtonDate.equals(LocalDate.now());
            boolean isSelected = thisButtonDate.equals(selectedDate);

            if (isToday) {
                btnDay.setBackground(COLOR_PRIMARY);
                btnDay.setForeground(Color.WHITE);
                btnDay.setFont(new Font("Segoe UI", Font.BOLD, 22));
                if (isSelected) {
                    btnDay.setCustomBorder(Color.BLACK, 2);
                } else {
                    btnDay.setCustomBorder(COLOR_PRIMARY, 1);
                }
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

            btnDay.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isToday && !isSelected) btnDay.setBackground(COLOR_HOVER);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isToday && !isSelected) btnDay.setBackground(Color.WHITE);
                }
            });

            btnDay.addActionListener(e -> {
                selectedDate = thisButtonDate;
                renderCalendar();
            });

            panelDays.add(btnDay);
        }

        panelDays.revalidate();
        panelDays.repaint();
    }
}