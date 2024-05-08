import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.util.Date;



public class Supplier_Panel extends JPanel{
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

    Supplier_Panel(){
        jLabel_Goods = new JLabel("供应商查询");
        jLabel_Goods.setFont(new Font("方正姚体",Font.BOLD,80));
        jl_searchName = new JLabel("供应商名称：");
        jl_searchName.setFont(new Font("微软雅黑",Font.BOLD,20));
        jt_searchName = new JTextField();
        jt_searchName.setFont(new Font("微软雅黑",Font.PLAIN,18));
        jl_searchId = new JLabel("供应商 ID: ");
        jl_searchId.setFont(new Font("微软雅黑",Font.BOLD,20));
        jt_searchId = new JTextField();
        jt_searchId.setFont(new Font("微软雅黑",Font.PLAIN,18));
        jt_searchId.setPreferredSize(new Dimension(200, 30));

        search_Button = new JButton("查询");
        search_Button.setVisible(true);
        search_Button.setFocusPainted(false);
        search_Button.setFont(new Font("微软雅黑",Font.BOLD,20));
        search_Button.setBackground(Color.white);

        add_Button = new JButton("添加供应商信息");
        add_Button.setVisible(true);
        add_Button.setFocusPainted(false);
        add_Button.setFont(new Font("微软雅黑",Font.BOLD,20));
        add_Button.setBackground(Color.white);

        update_Button = new JButton("修改供应商信息");
        update_Button.setVisible(true);
        update_Button.setFocusPainted(false);
        update_Button.setFont(new Font("微软雅黑",Font.BOLD,20));
        update_Button.setBackground(Color.white);

        delete_Button = new JButton("删除供应商信息");
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
        String[] title = {"供应商编号","供应商名称","联系人","联系人电话"};
        PreparedStatement stmt = null;
        String prepared_Sql = new String("SELECT * FROM commodities_management.supplier");
        Connection_MySQL connection_MySQL = new Connection_MySQL();
        connection_MySQL.get_connected();
        try{
            stmt = connection_MySQL.get_Connection().prepareStatement(prepared_Sql);
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while(rs.next()){
                count++;
            }
            contentObjects = new Object[count][4];
            rs = stmt.executeQuery();
            int index = 0;
            while(rs.next()){
                for(int i=0;i<4;i++)
                {
                    contentObjects[index][i] = rs.getString(i+1);
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
            prepared_Sql = new String("SELECT * FROM commodities_management.supplier");
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
                Object[][] contentObjects = new Object[count][4];
                while(rs.next()){
                    for(int i=0;i<4;i++)
                    {
                        contentObjects[index][i] = rs.getString(i+1);
                    }
                index++;
                }
                String[] title = {"供应商编号","供应商名称","联系人","联系人电话"};
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
            prepared_Sql = new String("SELECT * FROM commodities_management.supplier WHERE name like ?");
            else {
                prepared_Sql = new String("SELECT * FROM commodities_management.supplier WHERE name like ? and id = ? ");
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
                    for(int i=0;i<4;i++)
                    {
                        contentObjects[index][i] = rs.getString(i+1);
                    }
                index++;
                }
                String[] title = {"供应商编号","供应商名称","联系人","联系人电话"};
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
            addFrame.setTitle("添加供应商");
            addFrame.setVisible(true);
            addFrame.setSize(new Dimension(600,400));
            addFrame.setLocationRelativeTo(null);
            addFrame.getContentPane().setBackground(Color.white);
            addFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Login_sql/image/OIP.jpg"));
            Box box_h1 = Box.createHorizontalBox();
            Box box_h2 = Box.createHorizontalBox();
            Box box_h3 = Box.createHorizontalBox();
            JLabel label_1 = new JLabel("供应商编号:");
            JLabel label_2 = new JLabel("供应商名称:");
            JLabel label_3 = new JLabel("联系人:");
            JLabel label_4 = new JLabel("联系人电话:");
            label_1.setFont(new Font("微软雅黑",Font.BOLD,16));
            label_2.setFont(new Font("微软雅黑",Font.BOLD,16));
            label_3.setFont(new Font("微软雅黑",Font.BOLD,16));
            label_4.setFont(new Font("微软雅黑",Font.BOLD,16));
            JTextField tField_1 = new JTextField();
            JTextField tField_2 = new JTextField();
            JTextField tField_3 = new JTextField();
            JTextField tField_4 = new JTextField();
            tField_1.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_1.setMaximumSize(new Dimension(100, 22));
            tField_2.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_2.setMaximumSize(new Dimension(100, 22));
            tField_3.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_3.setMaximumSize(new Dimension(100, 22));
            tField_4.setFont(new Font("微软雅黑",Font.BOLD,16));
            tField_4.setMaximumSize(new Dimension(150, 22));
            box_h1.add(label_1);
            box_h1.add(tField_1);
            box_h1.add(label_2);
            box_h1.add(tField_2);
            box_h1.add(label_3);
            box_h1.add(tField_3);
            box_h2.add(label_4);
            box_h2.add(tField_4);
            JButton addButton = new JButton("添加");
            JButton resetButton = new JButton("重置");
            addButton.setFocusPainted(false);
            addButton.setFont(new Font("微软雅黑",Font.BOLD,18));
            addButton.setBackground(Color.white);
            resetButton.setFocusPainted(false);
            resetButton.setFont(new Font("微软雅黑",Font.BOLD,18));
            resetButton.setBackground(Color.white);
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
                        JOptionPane.showConfirmDialog(null, "供应商编号不能为空！","添加失败",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if(tField_1.getText().length()>5)
                    {
                        JOptionPane.showConfirmDialog(null, "供应商编号长度至多为5！","添加失败",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    try{
                        Connection_MySQL conn = new Connection_MySQL();
                        conn.get_connected();
                        PreparedStatement stmt = null;
                        String sql = new String("select * from supplier");
                        stmt = conn.get_Connection().prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery();
                        int flag = 0;
                        while(rs.next()){
                            if(tField_1.getText().equals(rs.getString("id")))
                            {
                                flag = 1;
                                JOptionPane.showConfirmDialog(null, "供应商编号存在重复！","添加失败",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        String insert = new String("insert into supplier values(?,?,?,?)");
                        stmt = conn.get_Connection().prepareStatement(insert);
                        stmt.setString(1, tField_1.getText());
                        stmt.setString(2, tField_2.getText());
                        stmt.setString(3, tField_3.getText());
                        stmt.setString(4, tField_4.getText());
                        if(stmt.executeUpdate()!=0)
                        {
                            JOptionPane.showConfirmDialog(null, "供应商信息添加成功！", "添加成功", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            JOptionPane.showConfirmDialog(null, "供应商信息添加失败！", "添加失败", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
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
                }
            });
            
            
            
        }
    }

    class update_Button_listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            new Supplier_Update_Jframe();
        }
    }

    class delete_Button_listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            new Supplier_Delete_Jframe();
        }
    }
}

class Supplier_Update_Jframe extends JFrame{
    JLabel jLabel_goodsID;
    JTextField jTextField_goodsID;
    JLabel jLabel_goodsInfo;
    JComboBox<String> jComboBox;
    String name = new String("供应商名称");
    String contact = new String("联系人");
    String phone = new String("联系人电话");
    JButton updateButton = new JButton("更新");
    JTextField jTextField_updateinfo;
    Supplier_Update_Jframe(){
        this.setSize(500,300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setTitle("修改供应商信息");
        this.getContentPane().setBackground(Color.white);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("Login_sql/image/OIP.jpg"));
        Box box_h1 = Box.createHorizontalBox();
        Box box_h2 = Box.createHorizontalBox();
        Box box_v = Box.createVerticalBox();
        JLabel jLabel_goodsID = new JLabel("请输入供应商ID: ");
        jLabel_goodsID.setFont(new Font("微软雅黑",Font.BOLD,18));
        JTextField jTextField_goodsID = new JTextField();
        jTextField_goodsID.setFont(new Font("微软雅黑",Font.BOLD,20));
        jTextField_goodsID.setPreferredSize(new Dimension(200, 25));
        jTextField_goodsID.setMaximumSize(new Dimension(200, 25));
        JLabel jLabel_goodsInfo = new JLabel("修改供应商信息：");
        jLabel_goodsInfo.setFont(new Font("微软雅黑",Font.BOLD,20));
        jTextField_updateinfo = new JTextField();
        jTextField_updateinfo.setFont(new Font("微软雅黑",Font.BOLD,20));
        jTextField_updateinfo.setPreferredSize(new Dimension(200, 30));
        jTextField_updateinfo.setMaximumSize(new Dimension(150, 30));
        jComboBox = new JComboBox<String>();
        jComboBox.addItem(name);
        jComboBox.addItem(contact);
        jComboBox.addItem(phone);
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
                    String temp = jComboBox.getSelectedItem().toString();
                    if(temp.equals("供应商名称"))
                    {
                        sql = new String("UPDATE supplier SET name = ? WHERE id = ?");
                        stmt = connection_MySQL.get_Connection().prepareStatement(sql);
                    }
                    else if(temp.equals("联系人")){
                        sql = new String("UPDATE supplier SET contact = ? WHERE id = ?");
                        stmt = connection_MySQL.get_Connection().prepareStatement(sql);
                    }
                    else if(temp.equals("联系人电话")){
                        if(jTextField_updateinfo.getText().length()>11)
                        {
                            JOptionPane.showConfirmDialog(null, "联系人电话至多为11位, 修改失败! ", "修改信息失败", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        sql = new String("UPDATE supplier SET contact_phone_number = ? WHERE id = ?");
                        stmt = connection_MySQL.get_Connection().prepareStatement(sql);
                    }
                    stmt.setString(1, jTextField_updateinfo.getText());
                    stmt.setString(2, jTextField_goodsID.getText());
                    if(stmt.executeUpdate()!=0)
                    {
                        JOptionPane.showConfirmDialog(null, "供应商信息修改成功！", "修改信息", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showConfirmDialog(null, "供应商信息修改失败！", "修改信息", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    }
                }catch(SQLException e2){
                    e2.printStackTrace();
                }
                connection_MySQL.get_closed();

            }
        });
    }
}

class Supplier_Delete_Jframe extends JFrame{
    JLabel label_ID = new JLabel("供应商 ID: ");
    JTextField textField_ID = new JTextField();
    JButton button_delete = new JButton("删除供应商信息");
    Supplier_Delete_Jframe(){
        this.setSize(500,200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("删除供应商信息");
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
                String sql = new String("delete from supplier where id = ?");
                PreparedStatement stmt = conn.get_Connection().prepareStatement(sql);
                stmt.setString(1, textField_ID.getText());
                if(stmt.executeUpdate()!=0){
                    JOptionPane.showConfirmDialog(null, "删除供应商信息成功！","删除供应商信息",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showConfirmDialog(null, "删除供应商失败","删除供应商信息",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                }
                conn.get_closed();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        });
        
    }
}