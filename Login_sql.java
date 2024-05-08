import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStreamImpl;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.mysql.cj.log.Log;
import com.mysql.cj.protocol.Warning;
import com.mysql.cj.result.Row;
import com.mysql.cj.x.protobuf.MysqlxSql.StmtExecute;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.TileObserver;
import java.io.File;
import java.io.IOException;

public class Login_sql extends JFrame{
    JButton jb_login;
    JButton jb_exit;
    JLabel jl_account;
    JLabel jl_password;
    JTextField jt_account;
    JPasswordField jp_password;
    
    
    Login_sql(){
        this.setTitle("小侯登录界面");
        this.setSize(600,400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLayout(new GridLayout(3, 1));
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("Login_sql/image/OIP.jpg"));
        //this.getContentPane().setBackground(Color.white);
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("Login_sql/image/login.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        ImageIcon background = new ImageIcon("Login_sql/image/login.jpg");
        JLabel bgLabel = new JLabel(background);
        bgLabel.setBounds(0, 0, this.getWidth(), this.getHeight());
        bgLabel.setOpaque(false);
        this.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));


        jl_account = new JLabel("账号：");
        jl_password = new JLabel("密码：");
        jl_account.setFont(new Font("黑体", Font.BOLD, 20));
        jl_password.setFont(new Font("黑体", Font.BOLD, 20));

        jt_account = new JTextField();
        jt_account.setPreferredSize(new Dimension(300,30));
        jt_account.setFont(new Font("宋体", Font.PLAIN, 20));

        jp_password = new JPasswordField();
        jp_password.setPreferredSize(new Dimension(300,30));
        jp_password.setFont(new Font("宋体", Font.PLAIN, 20));

        jb_login = new JButton();
        jb_exit = new JButton();
        jb_login.setText("登录");
        jb_exit.setText("退出");
        jb_login.setFont(new Font("宋体", Font.BOLD, 25));
        jb_exit.setFont(new Font("宋体", Font.BOLD, 25));
        jb_login.setFocusPainted(false);
        jb_exit.setFocusPainted(false);

        Container container = this.getContentPane();
        JPanel account_JPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("Login_sql/image/f-0.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        account_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER,25,50));
        account_JPanel.add(jl_account);
        account_JPanel.add(jt_account);
        account_JPanel.setBackground(Color.white);
        JPanel password_JPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("Login_sql/image/f-1.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        password_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER,25,0));
        password_JPanel.add(jl_password);
        password_JPanel.add(jp_password);
        JPanel button_JPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("Login_sql/image/f-2.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };;
        button_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER,80,0));
        button_JPanel.add(jb_login);
        button_JPanel.add(jb_exit);
        container.add(account_JPanel);
        container.add(password_JPanel);
        container.add(button_JPanel);
        container.validate();

        account_JPanel.setOpaque(false);
        password_JPanel.setOpaque(false);
        button_JPanel.setOpaque(false);
        // jb_login.setOpaque(false);
        // jb_exit.setOpaque(false);
        jl_account.setOpaque(false);
        jl_password.setOpaque(false);
        

        login_listener lg_listener = new login_listener();
        jb_login.addActionListener(lg_listener);
        jb_login.setBackground(Color.white);
        this.setResizable(false);
        exit_listener ex_listener = new exit_listener();
        jb_exit.addActionListener(ex_listener);
        jb_exit.setBackground(Color.white);

    }
    void L_hide()
    {
        //this.setVisible(false);
        this.dispose();
    }

    class login_listener implements ActionListener{
    
        String real_account;
        String real_password;
        
        public void actionPerformed(ActionEvent e){
    
            real_account = jt_account.getText();
            real_password = new String(jp_password.getPassword());
            if(real_account.isEmpty()||real_password.isEmpty())
            {
                JOptionPane.showConfirmDialog(null, "账号或密码不能为空！", "登录失败", JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                return;
            }
            //System.out.println(real_account+" "+real_password);
            
            Connection conn = null;
            PreparedStatement stmt = null;
    
            try{
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            
            //第二步 获取连接
            String url="jdbc:mysql://localhost:3306/user_info";
            String user="root";
            String password="123456789hr??";
            
            conn= DriverManager.getConnection(url, user, password);
                
            System.out.println("数据库连接对象"+conn);
                
            //第三步获取数据库操作对象
            String sql = "SELECT * FROM user_login WHERE account = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,real_account);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
            String id = rs.getString("account");
            String name = rs.getString("name");
            String pass = rs.getString("password");
            if(pass.equals(real_password))
            {
            //System.out.println("找到" + id + ", " + pass + ", " + name);
            sucess_login sucess = new sucess_login(id);
            L_hide();
            }
            else{
                //JDialog JD_infor = new JDialog(,"登陆失败",true);
                JOptionPane.showConfirmDialog(null, "账号密码不匹配！","登陆失败",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                System.out.println("密码不匹配");
            }
            }else{
                JOptionPane.showConfirmDialog(null, "不存在此账号！","登陆失败",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                System.out.println("不存在此账号");
            }
            conn.close();
            }
            catch(SQLException e1){
                e1.printStackTrace();
            }
    
        }
    }
    
    class exit_listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }


    public static void main(String[] args) {
        new Login_sql();

    }
    
}

class sucess_login extends JFrame{
    String account;
    String nick;

    JMenuBar menuBar = new JMenuBar();
    JMenu hostMenu = new JMenu("主页");
    JMenu setMenu = new JMenu("设置"); 
    JMenu sysMenu = new JMenu("系统");
    JMenu loginoutMenu = new JMenu("注销");

    JLabel jl_welcome;
    JLabel jl_welcome2;
    
    DefaultMutableTreeNode hostNode_0;
    DefaultMutableTreeNode rootNode_1;
    DefaultMutableTreeNode select_Goods_2;
    DefaultMutableTreeNode classified_Goods_2;
    DefaultMutableTreeNode foodNode_3;
    DefaultMutableTreeNode toolNode_3;
    DefaultMutableTreeNode lifeNode_3;

    DefaultMutableTreeNode importNode_1;
    DefaultMutableTreeNode inventoryNode_1;
    DefaultMutableTreeNode sellNode_1;
    DefaultMutableTreeNode supplierNode_1;
    DefaultMutableTreeNode customerNode_1;
    JTree tree;
    JTree host;

    JPanel panel_welcome;
    JPanel paneright;
    JScrollPane panelLeft;
    JSplitPane paneDown;

    




    sucess_login(String accountS){
        // Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        // for (Font font : fonts) {
        //     System.out.println(font.getFontName());
        // }
        account = new String(accountS);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("Login_sql/image/OIP.jpg"));
        
        user_info();

        initial_window();

        //menuBar.setAlignmentX(JMenuBar.RIGHT_ALIGNMENT);
        
        initial_menu();
        
        initial_welcome();
        
        initial_tree();

        initial_rightPane();

        initial_downPaneLayout();
        
        //Container pageContainer = this.getContentPane();
        BoxLayout v_BoxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        this.getContentPane().setLayout(v_BoxLayout);
        //this.getContentPane().setBackground(Color.white);
        this.getContentPane().add(panel_welcome);
        Box box_paneDown = Box.createHorizontalBox();
        box_paneDown.add(paneDown);
        this.getContentPane().add(box_paneDown);

        tree.addTreeSelectionListener(new TreeListener());
        hostMenu.addMenuListener(new MenuHSSLListener());
        setMenu.addMenuListener(new MenuHSSLListener());
        sysMenu.addMenuListener(new MenuHSSLListener());
        loginoutMenu.addMenuListener(new MenuHSSLListener());
        hostMenu.addMouseListener(new MenuMouseClickedListener());
        setMenu.addMouseListener(new MenuMouseClickedListener());
        sysMenu.addMouseListener(new MenuMouseClickedListener());
        loginoutMenu.addMouseListener(new MenuMouseClickedListener());
        
        menuBar.setBackground(Color.white);
    }
    void user_info(){
        Connection conn = null;
        PreparedStatement stmt = null;

        try{
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        
        //第二步 获取连接
        String url="jdbc:mysql://localhost:3306/user_info";
        String user="root";
        String password="123456789hr??";
        
        conn= DriverManager.getConnection(url, user, password);
            
        System.out.println("数据库连接对象"+conn);
            
        //第三步获取数据库操作对象
        String sql = "SELECT * FROM user_login WHERE account = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1,account);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
        nick = new String(rs.getString("name"));
        
        conn.close();
        }
        }
        catch(SQLException e1){
            e1.printStackTrace();
        }
    }
    
    void initial_window(){
        this.setSize(1024,900);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("商店管理系统");
    }
    
    void initial_menu(){
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(hostMenu);
        menuBar.add(setMenu);
        menuBar.add(sysMenu);
        menuBar.add(loginoutMenu);
        hostMenu.setFont(new Font("微软黑体",Font.BOLD,18));
        setMenu.setFont(new Font("微软黑体",Font.BOLD,18));
        sysMenu.setFont(new Font("微软黑体",Font.BOLD,18));
        loginoutMenu.setFont(new Font("微软黑体",Font.BOLD,18));
        
        setJMenuBar(menuBar);
    }

    void initial_welcome(){
        jl_welcome = new JLabel("商店管理系统");
        jl_welcome.setFont(new Font("微软雅黑",Font.PLAIN,24));
        jl_welcome2 = new JLabel("下午好！系统管理员 "+nick+"，欢迎您！");
        jl_welcome2.setFont(new Font("华文行楷",Font.PLAIN,18));
        panel_welcome = new JPanel();
        panel_welcome.setSize(this.getWidth(),this.getHeight()/5);
        BoxLayout box_Jwelcome = new BoxLayout(panel_welcome, BoxLayout.X_AXIS);
        jl_welcome2.setSize(20,this.getHeight()/5);
        //jl_welcome2.setVerticalTextPosition(JLabel.BOTTOM);
        jl_welcome2.setVerticalAlignment(JLabel.BOTTOM);
        panel_welcome.setLayout(box_Jwelcome);

        panel_welcome.add(Box.createRigidArea(new Dimension(50,0)));
        panel_welcome.add(jl_welcome);
        panel_welcome.add(Box.createRigidArea(new Dimension(20,0)));
        panel_welcome.add(jl_welcome2);
        panel_welcome.add(Box.createRigidArea(new Dimension(this.getWidth()*3/5,0)));
        panel_welcome.add(Box.createHorizontalGlue());
        //jl_welcome2.setVerticalTextPosition(JLabel.BOTTOM);
        
        panel_welcome.setBackground(Color.white);
    }

    void initial_tree(){
        rootNode_1 = new DefaultMutableTreeNode("商品管理");
        hostNode_0 = new DefaultMutableTreeNode("商店主页");
        select_Goods_2 = new DefaultMutableTreeNode("商品查询");
        classified_Goods_2 = new DefaultMutableTreeNode("商品分类");
        foodNode_3 = new DefaultMutableTreeNode("食物");
        toolNode_3 = new DefaultMutableTreeNode("文具");
        lifeNode_3 = new DefaultMutableTreeNode("生活用品");
        importNode_1 = new DefaultMutableTreeNode("进货管理");
        inventoryNode_1 = new DefaultMutableTreeNode("库存管理");
        sellNode_1 = new DefaultMutableTreeNode("销售管理");
        supplierNode_1 = new DefaultMutableTreeNode("供应商管理");
        customerNode_1 = new DefaultMutableTreeNode("客户管理");
        

        rootNode_1.insert(select_Goods_2, rootNode_1.getChildCount());
        rootNode_1.insert(classified_Goods_2, rootNode_1.getChildCount());
        classified_Goods_2.insert(foodNode_3,classified_Goods_2.getChildCount());
        classified_Goods_2.insert(toolNode_3,classified_Goods_2.getChildCount());
        classified_Goods_2.insert(lifeNode_3,classified_Goods_2.getChildCount());
        //System.out.println(classified_Goods_2.getIndex(foodNode_3));
        hostNode_0.insert(rootNode_1, hostNode_0.getChildCount());
        hostNode_0.insert(importNode_1, hostNode_0.getChildCount());
        hostNode_0.insert(inventoryNode_1, hostNode_0.getChildCount());
        hostNode_0.insert(sellNode_1, hostNode_0.getChildCount());
        hostNode_0.insert(supplierNode_1, hostNode_0.getChildCount());
        hostNode_0.insert(customerNode_1, hostNode_0.getChildCount());
        tree = new JTree(hostNode_0);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRowHeight(20);
        tree.setVisible(true);
        DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer)tree.getCellRenderer();
        cellRenderer.setFont(new Font("Serif",Font.PLAIN,14));
        cellRenderer.setBackgroundNonSelectionColor(Color.white);
        cellRenderer.setBackgroundSelectionColor(Color.yellow);
        cellRenderer.setBorderSelectionColor(Color.red);
        cellRenderer.setTextNonSelectionColor(Color.black);
        cellRenderer.setTextSelectionColor(Color.blue);

        panelLeft = new JScrollPane(tree);
    }

    void initial_rightPane(){
        paneright = new Host_Page_Panel();
    }

    void initial_downPaneLayout(){
        paneDown = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panelLeft,paneright);
        //BoxLayout h_BoxLayout_down = new BoxLayout(paneDown, BoxLayout.X_AXIS);
        //paneDown.setLayout(h_BoxLayout_down);
        paneDown.setDividerSize(5);
        paneDown.setAlignmentX(Component.LEFT_ALIGNMENT);

    }

    void set_goods_select_Panel(){
        Goods_Select_Panel gs_panel = new Goods_Select_Panel();
        paneDown.setRightComponent(gs_panel);
    }

    void set_host_page_Panel(){
        paneDown.setRightComponent(new Host_Page_Panel());
    }

    class TreeListener implements TreeSelectionListener{
        public void valueChanged(TreeSelectionEvent e){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(node.equals(select_Goods_2))
            {
                set_goods_select_Panel();
            }else if(node.equals(hostNode_0)){
                set_host_page_Panel();
            }else if(node.equals(foodNode_3)){
                paneDown.setRightComponent(new Goods_Category_Panel("食物"));
            }else if(node.equals(toolNode_3)){
                paneDown.setRightComponent(new Goods_Category_Panel("文具"));
            }
            else if(node.equals(lifeNode_3)){
                paneDown.setRightComponent(new Goods_Category_Panel("生活用品"));
            }
            else if(node.equals(sellNode_1)){
                paneDown.setRightComponent(new Sales_Order_Panel());
            }
            else if(node.equals(supplierNode_1)){
                paneDown.setRightComponent(new Supplier_Panel());
            }
            else if(node.equals(importNode_1)){
                paneDown.setRightComponent(new Import_Panel());
            }
        }
    }

    class MenuHSSLListener implements MenuListener{
        public void menuSelected(MenuEvent e) {
            if(e.getSource().equals(hostMenu))
            {
                paneDown.setRightComponent(new Host_Page_Panel());
            }
            else if(e.getSource().equals(loginoutMenu))
            {
                dispose();
                new Login_sql();
            }else if(e.getSource().equals(setMenu))
            {
                new Setting_Jframe();
            }else if(e.getSource().equals(sysMenu))
            {
                JDialog jDialog = new Sys_Jframe();
                jDialog.setVisible(true);
                MenuSelectionManager.defaultManager().clearSelectedPath();
            }
        }
        public void menuDeselected(MenuEvent e) {
            
        }
        public void menuCanceled(MenuEvent e) {
            
        }
    }

    class MenuMouseClickedListener extends MouseAdapter{
        public void mouseClicked(MouseEvent e) {
            MenuSelectionManager.defaultManager().clearSelectedPath();
        }
    }
    
    class Host_Page_Panel extends JPanel{
        JLabel jLabel_welcomLabel = new JLabel("欢迎使用商店管理系统！");
        JButton jButton_goods = new JButton("商品管理");
        JButton jButton_imporButton = new JButton("进货管理");
        JButton jButton_inveButton = new JButton("库存管理");
        JButton jButton_sellButton = new JButton("销售管理");
        JButton jButton_supplierButton = new JButton("供应商管理");
        JButton jButton_customerButton = new JButton("客户管理");
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon background = new ImageIcon("Login_sql/image/sucess_login.jpg");
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
        }

        Host_Page_Panel(){
            jLabel_welcomLabel.setFont(new Font("微软雅黑",Font.BOLD,50));
            jButton_goods.setFont(new Font("微软雅黑",Font.BOLD,30));
            jButton_goods.setFocusPainted(false);
            jButton_imporButton.setFont(new Font("微软雅黑",Font.BOLD,30));
            jButton_imporButton.setFocusPainted(false);
            jButton_inveButton.setFont(new Font("微软雅黑",Font.BOLD,30));
            jButton_inveButton.setFocusPainted(false);
            jButton_sellButton.setFont(new Font("微软雅黑",Font.BOLD,30));
            jButton_sellButton.setFocusPainted(false);
            jButton_supplierButton.setFont(new Font("微软雅黑",Font.BOLD,30));
            jButton_supplierButton.setFocusPainted(false);
            jButton_customerButton.setFont(new Font("微软雅黑",Font.BOLD,30));
            jButton_customerButton.setFocusPainted(false);
            Box vBox = Box.createVerticalBox();
            Box hBox1 = Box.createHorizontalBox();
            Box hBox2 = Box.createHorizontalBox();
            Box hBox3 = Box.createHorizontalBox();
            hBox1.add(jLabel_welcomLabel);
            hBox1.add(Box.createHorizontalGlue());
            hBox2.add(jButton_goods);
            hBox2.add(Box.createHorizontalGlue());
            hBox2.add(jButton_imporButton);
            hBox2.add(Box.createHorizontalGlue());
            hBox2.add(jButton_inveButton);
            hBox2.add(Box.createHorizontalGlue());
            hBox3.add(jButton_sellButton);
            hBox3.add(Box.createHorizontalGlue());
            hBox3.add(jButton_supplierButton);
            hBox3.add(Box.createHorizontalGlue());
            hBox3.add(jButton_customerButton);
            hBox3.add(Box.createHorizontalGlue());
            vBox.add(Box.createVerticalStrut(50));
            vBox.add(hBox1);
            vBox.add(Box.createVerticalStrut(150));
            vBox.add(hBox2);
            vBox.add(Box.createVerticalStrut(100));
            vBox.add(hBox3);
            this.add(vBox);
            this.setBackground(Color.white);
            this.setVisible(true);

            jButton_goods.setBackground(Color.white);
            jButton_customerButton.setBackground(Color.white);
            jButton_imporButton.setBackground(Color.white);
            jButton_inveButton.setBackground(Color.white);
            jButton_sellButton.setBackground(Color.white);
            jButton_supplierButton.setBackground(Color.white);

    
            jButton_goods.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    paneDown.setRightComponent(new Goods_Select_Panel());
                }
            });
            jButton_sellButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    paneDown.setRightComponent(new Sales_Order_Panel());
                }
            });
            jButton_imporButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    paneDown.setRightComponent(new Import_Panel());
                }
            });
            jButton_supplierButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    paneDown.setRightComponent(new Supplier_Panel());
                }
            });
        }
    }


    class Setting_Jframe extends JFrame{

        Setting_Jframe(){
            this.setTitle("设置");
            this.setSize(600,200);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            this.setAlwaysOnTop(true);
            this.getContentPane().setBackground(Color.white);
            this.setIconImage(Toolkit.getDefaultToolkit().getImage("Login_sql/image/OIP.jpg"));
            Box box_h1 = Box.createHorizontalBox();
            Box box_h2 = Box.createHorizontalBox();
            Box box_h3 = Box.createHorizontalBox();
            Box box_v = Box.createVerticalBox();
            JLabel jLabel = new JLabel("修改管理员信息");
            jLabel.setFont(new Font("方正姚体",Font.BOLD,80));
    
            JComboBox<String> comboBox = new JComboBox<String>();
            String nameString = new String("管理员名称");
            String passString = new String("管理员密码");
            comboBox.addItem(nameString);
            comboBox.addItem(passString);
            comboBox.setFont(new Font("微软雅黑",Font.BOLD,20));
            comboBox.setMaximumSize(new Dimension(200, 40));
            JTextField jTextField = new JTextField();
            jTextField.setFont(new Font("微软雅黑",Font.BOLD,20));
            jTextField.setPreferredSize(new Dimension(200, 25));
            jTextField.setMaximumSize(new Dimension(200, 25));
            JButton jButton = new JButton("修改");
            jButton.setVisible(true);
            jButton.setFocusPainted(false);
            jButton.setFont(new Font("微软雅黑",Font.BOLD,20));
            jButton.setBackground(Color.white);
            box_h1.add(comboBox);
            box_h1.add(Box.createHorizontalStrut(10));
            box_h1.add(jTextField);
            box_h1.add(Box.createHorizontalStrut(20));
            box_h1.add(jButton);
            box_v.add(Box.createGlue());
            box_v.add(box_h1);
            box_v.add(Box.createGlue());
            this.add(box_v);
    
            jButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if(jTextField.getText().isEmpty())
                    {
                        JOptionPane.showConfirmDialog(null, "输入不能为空！","修改失败",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    Connection conn = null;
                    PreparedStatement stmt = null;
    
                    try{
                    DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                    
                    //第二步 获取连接
                    String url="jdbc:mysql://localhost:3306/user_info";
                    String user="root";
                    String password="123456789hr??";
                    
                    conn= DriverManager.getConnection(url, user, password);
                        
                    System.out.println("数据库连接对象"+conn);
                        
                    //第三步获取数据库操作对象
                    String sql1 = "UPDATE user_login SET name = ? WHERE account = ?";
                    String sql2 = "UPDATE user_login SET password = ? WHERE account = ?";
                    if(comboBox.getSelectedItem().equals(nameString))
                    {
                        stmt = conn.prepareStatement(sql1);
                        stmt.setString(1,jTextField.getText());
                    }else{
                        stmt = conn.prepareStatement(sql2);
                        stmt.setString(1,jTextField.getText());
                    }
                    stmt.setString(2,account);    
                    if (stmt.executeUpdate()!=0) {
                    PreparedStatement temp = conn.prepareStatement("select * from user_login where account = ?");
                    temp.setString(1, account);
                    ResultSet rs = temp.executeQuery();
                    rs.next();
                    nick = rs.getString(3);
                    //System.out.print(nick);
                    conn.close();
                    JOptionPane.showConfirmDialog(null, "修改成功，再次登录时生效！","修改成功",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    }
                    }
                    catch(SQLException e1){
                        e1.printStackTrace();
                    }
                        }
            });
        }
    }

    class Sys_Jframe extends JDialog{
        Sys_Jframe(){
            super(sucess_login.this, "关于系统", true);
            JPanel panel = new JPanel();
            panel.add(new JLabel("此系统由小侯同志一手打造，经历3周时间，504个小时，此系统终于有了雏形，但是在此系统中库存管理和客户管理功能还尚未完成，其余皆已完成，若用户在其中发现某些BUG，可以私信小侯同志！"));
            getContentPane().add(panel);

            pack();
            setLocationRelativeTo(sucess_login.this);
        }
    }

}




class Goods_Select_Panel extends JPanel{
    JLabel jLabel_Goods;
    JLabel jl_searchName;
    JTextField jt_searchName;
    JLabel jl_searchId;
    JTextField jt_searchId;
    JButton search_Button;
    JTable tJTable;
    ScrollPane scrollPane;
    JButton add_Button;
    JButton update_Button;
    JButton delete_Button;
    JButton flush_Button;

    Goods_Select_Panel(){
        jLabel_Goods = new JLabel("商品查询");
        jLabel_Goods.setFont(new Font("方正姚体",Font.BOLD,80));
        jl_searchName = new JLabel("物品名称：");
        jl_searchName.setFont(new Font("微软雅黑",Font.BOLD,20));
        jt_searchName = new JTextField();
        jt_searchName.setFont(new Font("微软雅黑",Font.PLAIN,18));
        jl_searchId = new JLabel("物品 ID: ");
        jl_searchId.setFont(new Font("微软雅黑",Font.BOLD,20));
        jt_searchId = new JTextField();
        jt_searchId.setFont(new Font("微软雅黑",Font.PLAIN,18));
        jt_searchId.setPreferredSize(new Dimension(200, 30));

        search_Button = new JButton("查询");
        search_Button.setVisible(true);
        search_Button.setFocusPainted(false);
        search_Button.setFont(new Font("微软雅黑",Font.BOLD,20));
        search_Button.setBackground(Color.white);

        add_Button = new JButton("添加商品");
        add_Button.setVisible(true);
        add_Button.setFocusPainted(false);
        add_Button.setFont(new Font("微软雅黑",Font.BOLD,20));
        add_Button.setBackground(Color.white);

        update_Button = new JButton("修改商品");
        update_Button.setVisible(true);
        update_Button.setFocusPainted(false);
        update_Button.setFont(new Font("微软雅黑",Font.BOLD,20));
        update_Button.setBackground(Color.white);

        delete_Button = new JButton("删除商品");
        delete_Button.setVisible(true);
        delete_Button.setFocusPainted(false);
        delete_Button.setFont(new Font("微软雅黑",Font.BOLD,20));
        delete_Button.setBackground(Color.white);

        flush_Button = new JButton("刷新");
        flush_Button.setVisible(true);
        flush_Button.setFocusPainted(false);
        flush_Button.setFont(new Font("微软雅黑",Font.BOLD,20));
        flush_Button.setBackground(Color.white);
        
        Box box_fun = Box.createHorizontalBox();
        box_fun.add(Box.createHorizontalGlue());
        box_fun.add(add_Button);
        box_fun.add(Box.createHorizontalGlue());
        box_fun.add(update_Button);
        box_fun.add(Box.createHorizontalGlue());
        box_fun.add(delete_Button);
        box_fun.add(Box.createHorizontalGlue());
        box_fun.add(flush_Button);
        box_fun.add(Box.createHorizontalGlue());

        Box boxV = Box.createVerticalBox();
        Box boxH1 = Box.createHorizontalBox();
        Box boxH2 = Box.createHorizontalBox();
        Box boxH3 = Box.createHorizontalBox();
        boxH1.add(jLabel_Goods);
        boxH1.add(Box.createHorizontalGlue());
        boxH2.add(jl_searchName);
        boxH2.add(jt_searchName);
        boxH2.add(Box.createHorizontalGlue());

        boxH3.add(Box.createHorizontalGlue());
        boxH3.add(jl_searchId);
        boxH3.add(Box.createHorizontalStrut(19));
        boxH3.add(jt_searchId);
        boxH3.add(Box.createHorizontalStrut(20));
        //boxH3.add(Box.createHorizontalStrut(80));
        boxH3.add(search_Button);
        boxH3.add(Box.createHorizontalGlue());
        boxV.add(boxH1);
        boxV.add(Box.createVerticalStrut(50));
        boxV.add(boxH2);
        boxV.add(Box.createVerticalStrut(20));
        boxV.add(boxH3);
        boxV.add(Box.createVerticalStrut(20));
        boxV.add(box_fun);
        boxV.add(Box.createVerticalStrut(10));
        jt_searchName.setPreferredSize(new Dimension(200, jt_searchId.getPreferredSize().height));
        jt_searchId.setPreferredSize(new Dimension(200, jt_searchId.getPreferredSize().height));
        
        this.add(boxV);
        this.setBackground(Color.white);
        this.setVisible(true);

        Object[][] contentObjects = null;
        String[] title = {"商品编号","商品名称","商品数量","供应商编号","商品类型","商品价格"};
        PreparedStatement stmt = null;
        String prepared_Sql = new String("SELECT * FROM commodities_management.goods");
        Connection_MySQL connection_MySQL = new Connection_MySQL();
        connection_MySQL.get_connected();
        try{
            stmt = connection_MySQL.get_Connection().prepareStatement(prepared_Sql);
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while(rs.next()){
                count++;
            }
            contentObjects = new Object[count][6];
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next()){
                for(int i=0;i<6;i++)
                {
                    if(i==0||i==1||i==3||i==4)
                    contentObjects[index][i] = rs.getString(i+1);
                    else if(i==2){
                        contentObjects[index][i] = rs.getInt(i+1);
                    }else if(i==5){
                        contentObjects[index][i] = rs.getFloat(i+1);
                    }
                }
                index++;
            }
            tJTable = new JTable(contentObjects,title);
            tJTable.setFont(new java.awt.Font("微软雅黑",java.awt.Font.BOLD,20));
            tJTable.setPreferredScrollableViewportSize(new Dimension(800, 400));
            //tJTable.setGridColor(Color.green);
            tJTable.setRowHeight(20);
            tJTable.setEnabled(false);
            JTextField jtemp = new JTextField();
            jtemp.setFont(new java.awt.Font("微软雅黑",java.awt.Font.BOLD,20));
            tJTable.setCellEditor(new DefaultCellEditor(jtemp));
            scrollPane = new ScrollPane();
            Box hBox = Box.createHorizontalBox();
            scrollPane.add(tJTable);
            scrollPane.setPreferredSize(new Dimension(800, 400));
            tJTable.getTableHeader().setFont(new Font("微软雅黑",Font.BOLD,25));
            tJTable.getTableHeader().setBackground(Color.white);
            hBox.add(scrollPane);
            boxV.add(tJTable.getTableHeader());
            boxV.add(hBox);


        }catch(SQLException e1){
            e1.printStackTrace();
        }
        connection_MySQL.get_closed();
        search_Button.addActionListener(new select_listener());
        add_Button.addActionListener(new add_Button_listener());
        update_Button.addActionListener(new update_Button_listener());
        delete_Button.addActionListener(new delete_Button_listener());
        flush_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e5){
            String nameString = jt_searchName.getText();
            String idsString = jt_searchId.getText();
            PreparedStatement stmt = null;
            String prepared_Sql = null;
            prepared_Sql = new String("SELECT * FROM commodities_management.goods");
            Connection_MySQL connection_MySQL = new Connection_MySQL();
            connection_MySQL.get_connected();
            try{
                stmt = connection_MySQL.get_Connection().prepareStatement(prepared_Sql);
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                while(rs.next()){
                    count++;
                }
                rs = stmt.executeQuery();
                int index = 0;
                Object[][] contentObjects = new Object[count][6];
                while(rs.next()){
                    for(int i=0;i<6;i++)
                    {
                        if(i==0||i==1||i==3||i==4)
                        contentObjects[index][i] = rs.getString(i+1);
                        else if(i==2){
                            contentObjects[index][i] = rs.getInt(i+1);
                        }else if(i==5){
                            contentObjects[index][i] = rs.getFloat(i+1);
                        }
                    }
                index++;
                }
                String[] title = {"商品编号","商品名称","商品数量","供应商编号","商品类型","商品价格"};
                tJTable = new JTable(contentObjects, title);
                tJTable.setFont(new java.awt.Font("微软雅黑",java.awt.Font.BOLD,20));
                tJTable.setPreferredScrollableViewportSize(new Dimension(800, 400));
                //tJTable.setGridColor(Color.green);
                tJTable.setRowHeight(20);
                tJTable.setEnabled(false);
                JTextField jtemp = new JTextField();
                jtemp.setFont(new java.awt.Font("微软雅黑",java.awt.Font.BOLD,20));
                tJTable.setCellEditor(new DefaultCellEditor(jtemp));
                scrollPane.add(tJTable);

            }catch(SQLException e1){
                e1.printStackTrace();
            }
            connection_MySQL.get_closed();
            }
        });
    }


    class select_listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String nameString = jt_searchName.getText();
            String idsString = jt_searchId.getText();
            PreparedStatement stmt = null;
            String prepared_Sql = null;
            if(idsString.isEmpty())
            prepared_Sql = new String("SELECT * FROM commodities_management.goods WHERE name like ?");
            else {
                prepared_Sql = new String("SELECT * FROM commodities_management.goods WHERE name like ? and id = ? ");
            }
            Connection_MySQL connection_MySQL = new Connection_MySQL();
            connection_MySQL.get_connected();
            try{
                stmt = connection_MySQL.get_Connection().prepareStatement(prepared_Sql);
                stmt.setString(1, "%"+nameString+"%");
                if(!idsString.isEmpty())
                {
                    stmt.setString(2, idsString);
                }
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                while(rs.next()){
                    count++;
                }
                rs = stmt.executeQuery();
                int index = 0;
                Object[][] contentObjects = new Object[count][6];
                while(rs.next()){
                    for(int i=0;i<6;i++)
                    {
                        if(i==0||i==1||i==3||i==4)
                        contentObjects[index][i] = rs.getString(i+1);
                        else if(i==2){
                            contentObjects[index][i] = rs.getInt(i+1);
                        }else if(i==5){
                            contentObjects[index][i] = rs.getFloat(i+1);
                        }
                    }
                index++;
                }
                String[] title = {"商品编号","商品名称","商品数量","供应商编号","商品类型","商品价格"};
                tJTable = new JTable(contentObjects, title);
                tJTable.setFont(new java.awt.Font("微软雅黑",java.awt.Font.BOLD,20));
                tJTable.setPreferredScrollableViewportSize(new Dimension(800, 400));
                //tJTable.setGridColor(Color.green);
                tJTable.setRowHeight(20);
                tJTable.setEnabled(false);
                JTextField jtemp = new JTextField();
                jtemp.setFont(new java.awt.Font("微软雅黑",java.awt.Font.BOLD,20));
                tJTable.setCellEditor(new DefaultCellEditor(jtemp));
                scrollPane.add(tJTable);

            }catch(SQLException e1){
                e1.printStackTrace();
            }
            connection_MySQL.get_closed();

        }
    }

    class add_Button_listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            JFrame addFrame = new JFrame();
            addFrame.setBackground(Color.white);
            addFrame.setTitle("添加商品");
            addFrame.setVisible(true);
            addFrame.setSize(new Dimension(600,400));
            addFrame.setLocationRelativeTo(null);
            addFrame.getContentPane().setBackground(Color.white);
            addFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Login_sql/image/OIP.jpg"));
            Box box_h1 = Box.createHorizontalBox();
            Box box_h2 = Box.createHorizontalBox();
            Box box_h3 = Box.createHorizontalBox();
            JLabel label_1 = new JLabel("商品编号:");
            JLabel label_2 = new JLabel("商品名称:");
            JLabel label_3 = new JLabel("商品数量:");
            JLabel label_4 = new JLabel("供应商编号:");
            JLabel label_5 = new JLabel("商品类型:");
            JLabel label_6 = new JLabel("商品价格:");
            label_1.setFont(new Font("微软雅黑",Font.BOLD,16));
            label_2.setFont(new Font("微软雅黑",Font.BOLD,16));
            label_3.setFont(new Font("微软雅黑",Font.BOLD,16));
            label_4.setFont(new Font("微软雅黑",Font.BOLD,16));
            label_5.setFont(new Font("微软雅黑",Font.BOLD,16));
            label_6.setFont(new Font("微软雅黑",Font.BOLD,16));
            JTextField tField_1 = new JTextField();
            JTextField tField_2 = new JTextField();
            JTextField tField_3 = new JTextField();
            JTextField tField_4 = new JTextField();
            JTextField tField_5 = new JTextField();
            JTextField tField_6 = new JTextField();
            tField_1.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_1.setMaximumSize(new Dimension(100, 22));
            tField_2.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_2.setMaximumSize(new Dimension(100, 22));
            tField_3.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_3.setMaximumSize(new Dimension(100, 22));
            tField_4.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_4.setMaximumSize(new Dimension(100, 22));
            tField_5.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_5.setMaximumSize(new Dimension(100, 22));
            tField_6.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_6.setMaximumSize(new Dimension(100, 22));
            box_h1.add(label_1);
            box_h1.add(tField_1);
            box_h1.add(label_2);
            box_h1.add(tField_2);
            box_h1.add(label_3);
            box_h1.add(tField_3);
            box_h2.add(label_4);
            box_h2.add(tField_4);
            box_h2.add(label_5);
            box_h2.add(tField_5);
            box_h2.add(label_6);
            box_h2.add(tField_6);
            JButton addButton = new JButton("添加");
            addButton.setBackground(Color.white);
            JButton resetButton = new JButton("重置");
            resetButton.setBackground(Color.white);
            addButton.setFocusPainted(false);
            addButton.setFont(new Font("微软雅黑",Font.BOLD,18));
            resetButton.setFocusPainted(false);
            resetButton.setFont(new Font("微软雅黑",Font.BOLD,18));
            box_h3.add(Box.createHorizontalGlue());
            box_h3.add(addButton);
            box_h3.add(Box.createHorizontalGlue());
            box_h3.add(resetButton);
            box_h3.add(Box.createHorizontalGlue());
            Box box_v = Box.createVerticalBox();
            box_v.add(Box.createVerticalGlue());
            box_v.add(box_h1);
            box_v.add(Box.createVerticalGlue());
            box_v.add(box_h2);
            box_v.add(Box.createVerticalGlue());
            box_v.add(box_h3);
            box_v.add(Box.createVerticalGlue());
            addFrame.add(box_v);

            class add_push_listener implements ActionListener{
                public void actionPerformed(ActionEvent ae)
                {
                    if(tField_1.getText().isEmpty())
                    {
                        JOptionPane.showConfirmDialog(null, "商品编号不能为空！","添加失败",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if(tField_1.getText().length()>5)
                    {
                        JOptionPane.showConfirmDialog(null, "商品编号长度至多为5！","添加失败",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    try{
                        Connection_MySQL conn = new Connection_MySQL();
                        conn.get_connected();
                        PreparedStatement stmt = null;
                        String sql = new String("select * from goods");
                        stmt = conn.get_Connection().prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery();
                        int flag = 0;
                        while(rs.next()){
                            if(tField_1.getText().equals(rs.getString("id")))
                            {
                                flag = 1;
                                JOptionPane.showConfirmDialog(null, "商品编号存在重复！","添加失败",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        String insert = new String("insert into goods values(?,?,?,?,?,?)");
                        stmt = conn.get_Connection().prepareStatement(insert);
                        stmt.setString(1, tField_1.getText());
                        stmt.setString(2, tField_2.getText());
                        stmt.setInt(3, Integer.parseInt(tField_3.getText()));
                        stmt.setString(4, tField_4.getText());
                        stmt.setString(5, tField_5.getText());
                        stmt.setFloat(6, Float.parseFloat(tField_6.getText()));
                        if(stmt.executeUpdate()!=0)
                        {
                            JOptionPane.showConfirmDialog(null, "商品添加成功！", "添加成功", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            JOptionPane.showConfirmDialog(null, "商品添加失败！", "添加失败", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                        }
                        conn.get_closed();
                        }
                        catch(SQLException e2){
                            e2.printStackTrace();
                        }
                }
            }

            addButton.addActionListener(new add_push_listener());
            resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e4){
                    tField_1.setText("");
                    tField_2.setText("");
                    tField_3.setText("");
                    tField_4.setText("");
                    tField_5.setText("");
                    tField_6.setText("");
                }
            });
            
            
            
        }
    }

    class update_Button_listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            new Goods_Update_Jframe();
        }
    }

    class delete_Button_listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            new Goods_Delete_Jframe();
        }
    }
}

class Goods_Update_Jframe extends JFrame{
    JLabel jLabel_goodsID;
    JTextField jTextField_goodsID;
    JLabel jLabel_goodsInfo;
    JComboBox<String> jComboBox;
    String name = new String("商品名称");
    String num = new String("商品数量");
    String sup = new String("供应商编号");
    String type = new String("商品类型");
    String price = new String("商品价格");
    JButton updateButton = new JButton("更新");
    JTextField jTextField_updateinfo;
    Goods_Update_Jframe(){
        this.setSize(500,300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setTitle("修改商品信息");
        this.getContentPane().setBackground(Color.white);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("Login_sql/image/OIP.jpg"));
        Box box_h1 = Box.createHorizontalBox();
        Box box_h2 = Box.createHorizontalBox();
        Box box_v = Box.createVerticalBox();
        JLabel jLabel_goodsID = new JLabel("请输入商品ID: ");
        jLabel_goodsID.setFont(new Font("微软雅黑",Font.BOLD,18));
        JTextField jTextField_goodsID = new JTextField();
        jTextField_goodsID.setFont(new Font("微软雅黑",Font.BOLD,20));
        jTextField_goodsID.setPreferredSize(new Dimension(200, 25));
        jTextField_goodsID.setMaximumSize(new Dimension(200, 25));
        JLabel jLabel_goodsInfo = new JLabel("修改商品信息：");
        jLabel_goodsInfo.setFont(new Font("微软雅黑",Font.BOLD,20));
        jTextField_updateinfo = new JTextField();
        jTextField_updateinfo.setFont(new Font("微软雅黑",Font.BOLD,20));
        jTextField_updateinfo.setPreferredSize(new Dimension(200, 30));
        jTextField_updateinfo.setMaximumSize(new Dimension(150, 30));
        jComboBox = new JComboBox<String>();
        jComboBox.addItem(name);
        jComboBox.addItem(num);
        jComboBox.addItem(sup);
        jComboBox.addItem(type);
        jComboBox.addItem(price);
        jComboBox.setMaximumSize(new Dimension(150, 40));
        jComboBox.setSelectedIndex(1);
        jComboBox.setFont(new Font("微软雅黑",Font.BOLD,20));
        updateButton.setFont(new Font("微软雅黑",Font.BOLD,20));
        updateButton.setFocusPainted(false);
        updateButton.setBackground(Color.white);

        box_h1.add(jLabel_goodsID);
        box_h1.add(jTextField_goodsID);
        box_h2.add(jLabel_goodsInfo);
        box_h2.add(jComboBox);
        box_h2.add(jTextField_updateinfo);
        box_h2.add(Box.createHorizontalStrut(10));
        box_h2.add(updateButton);
        box_v.add(Box.createVerticalGlue());
        box_v.add(box_h1);
        box_v.add(Box.createVerticalGlue());
        box_v.add(box_h2);
        box_v.add(Box.createVerticalGlue());
        this.add(box_v);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Connection_MySQL connection_MySQL = new Connection_MySQL();
                connection_MySQL.get_connected();
                PreparedStatement stmt = null;
                String sql = null;
                try{
                    if(jComboBox.getSelectedItem().toString().equals("商品数量"))
                    {   
                        sql = new String("UPDATE goods SET quantity = ? WHERE id = ?");
                        stmt = connection_MySQL.get_Connection().prepareStatement(sql);
                        stmt.setInt(1, Integer.parseInt(jTextField_updateinfo.getText()));
                    }
                    else if(jComboBox.getSelectedItem().toString().equals("商品价格"))
                    {
                        sql = new String("UPDATE goods SET price = ? WHERE id = ?");
                        stmt = connection_MySQL.get_Connection().prepareStatement(sql);
                        stmt.setFloat(1, Float.parseFloat(jTextField_updateinfo.getText()));
                    }
                    else{
                        String temp = jComboBox.getSelectedItem().toString();
                        if(temp.equals("商品名称"))
                        {
                            sql = new String("UPDATE goods SET name = ? WHERE id = ?");
                            stmt = connection_MySQL.get_Connection().prepareStatement(sql);
                        }
                        else if(temp.equals("供应商编号")){
                            sql = new String("UPDATE goods SET supplier_id = ? WHERE id = ?");
                            stmt = connection_MySQL.get_Connection().prepareStatement(sql);
                        }
                        else if(temp.equals("商品类型")){
                            sql = new String("UPDATE goods SET type = ? WHERE id = ?");
                            stmt = connection_MySQL.get_Connection().prepareStatement(sql);
                        }
                        stmt.setString(1, jTextField_updateinfo.getText());
                    }
                    stmt.setString(2, jTextField_goodsID.getText());
                    if(stmt.executeUpdate()!=0)
                    {
                        JOptionPane.showConfirmDialog(null, "商品信息修改成功！", "修改信息", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showConfirmDialog(null, "商品信息修改失败！", "修改信息", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    }
                }catch(SQLException e2){
                    e2.printStackTrace();
                }
                connection_MySQL.get_closed();

            }
        });
    }
}

class Goods_Delete_Jframe extends JFrame{
    JLabel label_ID = new JLabel("商品 ID: ");
    JTextField textField_ID = new JTextField();
    JButton button_delete = new JButton("删除商品信息");
    Goods_Delete_Jframe(){
        this.setSize(500,200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("删除商品信息");
        this.getContentPane().setBackground(Color.white);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("Login_sql/image/OIP.jpg"));
        label_ID.setFont(new Font("微软雅黑",Font.BOLD,20));
        textField_ID.setFont(new Font("微软雅黑",Font.BOLD,20));
        textField_ID.setMaximumSize(new Dimension(200, 30));
        button_delete.setFont(new Font("微软雅黑",Font.BOLD,20));
        button_delete.setFocusPainted(false);
        button_delete.setBackground(Color.white);
        Box box_h1 = Box.createHorizontalBox();
        Box box_h2 = Box.createHorizontalBox();
        Box box_v = Box.createVerticalBox();
        box_h1.add(label_ID);
        box_h1.add(textField_ID);
        box_h2.add(button_delete);
        box_v.add(Box.createVerticalStrut(20));
        box_v.add(box_h1);
        box_v.add(Box.createVerticalStrut(20));
        box_v.add(box_h2);
        this.add(box_v);
        button_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e2)
            {
                Connection_MySQL conn = new Connection_MySQL();
                try{
                conn.get_connected();
                String sql = new String("delete from goods where id = ?");
                PreparedStatement stmt = conn.get_Connection().prepareStatement(sql);
                stmt.setString(1, textField_ID.getText());
                if(stmt.executeUpdate()!=0){
                    JOptionPane.showConfirmDialog(null, "删除商品信息成功！","删除商品",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showConfirmDialog(null, "删除商品失败","删除商品",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                }
                conn.get_closed();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        });
        
    }
}

class Connection_MySQL{

    Connection conn = null;
    public void get_connected(){
        try{
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            
            String url="jdbc:mysql://localhost:3306/commodities_management";
            String user="root";
            String password="123456789hr??";
            
            conn= DriverManager.getConnection(url, user, password);
                
            System.out.println("数据库连接对象"+conn);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void get_closed(){
        try{
            conn.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Connection get_Connection(){
        return conn;
    }
}

class Goods_Category_Panel extends JPanel{
    JLabel jLabel_Category;
    JLabel[] jLabel_goods_image;
    JLabel[] jLabel_goods_info;
    JPanel jPanel;
    Goods_Category_Panel(String t){
        jLabel_Category = new JLabel("商品分类");
        jLabel_Category.setFont(new Font("方正姚体",Font.BOLD,80));
        this.setBackground(Color.white);
        Connection_MySQL conn = new Connection_MySQL();
        conn.get_connected();
        try{
            int count = 0;
            String sql = new String("select * from goods where type = ?");
            PreparedStatement stmt = conn.get_Connection().prepareStatement(sql);
            stmt.setString(1, t);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                count++;
            }
            jLabel_goods_image = new JLabel[count];
            jLabel_goods_info = new JLabel[count];
            jPanel = new JPanel();
            jPanel.setLayout(new GridLayout(count/3+1, 3, 20, 20));
            Box[] box_h = new Box[count];
            for(int i=0;i<count;i++)
            {
                box_h[i] = Box.createVerticalBox();
            }
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next())
            {
                String name = rs.getString("name");
                Float price = rs.getFloat("price");
                Image scaledImg = null;
                if(name.equals("普通苹果"))
                scaledImg = ImageIO.read(new File("Login_sql/image/putongpg.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("富士苹果"))
                scaledImg = ImageIO.read(new File("Login_sql/image/fushipg.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("青苹果"))
                scaledImg = ImageIO.read(new File("Login_sql/image/shatangpg.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("普通西瓜"))
                scaledImg = ImageIO.read(new File("Login_sql/image/putongxg.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("无籽西瓜"))
                scaledImg = ImageIO.read(new File("Login_sql/image/wuzixg.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("砂糖西瓜"))
                scaledImg = ImageIO.read(new File("Login_sql/image/shatangxg.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("卫龙辣条"))
                scaledImg = ImageIO.read(new File("Login_sql/image/weilonglt.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("麻辣小王子"))
                scaledImg = ImageIO.read(new File("Login_sql/image/malaxiaowangzilt.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("黑人牙膏"))
                scaledImg = ImageIO.read(new File("Login_sql/image/heirenyg.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("海飞丝洗发水"))
                scaledImg = ImageIO.read(new File("Login_sql/image/haifeisixfs.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("中华铅笔"))
                scaledImg = ImageIO.read(new File("Login_sql/image/zhonghuaqb.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("英雄钢笔"))
                scaledImg = ImageIO.read(new File("Login_sql/image/yingxionggb.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("手工剪刀"))
                scaledImg = ImageIO.read(new File("Login_sql/image/shougongjd.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("手工卡纸"))
                scaledImg = ImageIO.read(new File("Login_sql/image/shougongkz.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else if(name.equals("羊巴皮本子"))
                scaledImg = ImageIO.read(new File("Login_sql/image/yangbapibz.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                else{
                    scaledImg = ImageIO.read(new File("Login_sql/image/default.jpg")).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                }
                jLabel_goods_image[index] = new JLabel(new ImageIcon(scaledImg));
                jLabel_goods_image[index].setMaximumSize(new Dimension(120, 100));
                jLabel_goods_info[index] = new JLabel(name+"  价格: "+price);
                jLabel_goods_info[index].setFont(new Font("微软雅黑",Font.BOLD,20));
                box_h[index].add(jLabel_goods_image[index]);
                box_h[index].add(jLabel_goods_info[index]);
                jPanel.add(box_h[index]);
                index++;
            }
            conn.get_closed();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        Box b1 = Box.createHorizontalBox();
        b1.setAlignmentX(Box.LEFT_ALIGNMENT);
        b1.add(Box.createHorizontalStrut(10));
        b1.add(jLabel_Category);
        b1.add(Box.createHorizontalGlue());
        Box b2 = Box.createVerticalBox();
        ScrollPane temp = new ScrollPane();
        temp.add(jPanel);
        jPanel.setBackground(Color.white);
        temp.setPreferredSize(new Dimension(800, 600));;
        b2.add(b1);
        b2.add(Box.createVerticalStrut(30));
        b2.add(temp);
        this.add(b2);
    }
}
