/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com_market;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.table.TableModel;

/**
 *
 * @author godfr
 */
public class main_dashboard extends javax.swing.JFrame {

    /**
     * Creates new form test
     */
    int xMouse;
    int yMouse;
    int active = 1;
    
    int[][] product_store = new int[50][2];
    double totalPrice = 0;
    int totalCount = 0;
    
    int row_selected = -1;
    
    int product_num = 0;
    int customer_store = -1;
    int manage_store = -1;
    
    double total_price_report = 0;
    
    public main_dashboard() {
        initComponents();
        centerFrame();
        initTableSales();
        initTableCustomer();
        initTableReport();
        initTableManage();
        
        total_count.setText(0 + "");
        total_price.setText(0 + "");
        
        dash_sales.setVisible(true);
        dash_customer.setVisible(false);
        dash_report.setVisible(false);
        dash_manage.setVisible(false);
        
        //String xett = "<html><img src='"+getClass().getResource("/assets/cog.png")+"'></img><br>hi<br>bro<br><br>here</html>"; 
        //initListProduct();
    }
    
    private void initTableSales(){
        DefaultTableModel model = (DefaultTableModel) table_sales.getModel();
        
        Connection conn = new dbConnect().dbcon();
        Statement st;
        ResultSet rs;
        PreparedStatement pt = null;
        
        model.setRowCount(0);
        addComboBox();
        
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT * FROM product";
            rs = st.executeQuery(sql);
            while(rs.next()){
                if(rs.getInt("amount") <= 0){
                    sql = "DELETE FROM  product WHERE id=" + rs.getInt("id") + "";
                    pt = conn.prepareStatement(sql);
                    pt.execute();
                }
            }
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String pList = "";
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT * FROM product";
            rs = st.executeQuery(sql);
            int i = 0;
            while(rs.next()){
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("amount"), rs.getString("price"), rs.getString("insurance")});
                product_store[i][0] = 0;
                product_store[i][1] = rs.getInt("price");
                i++;
                product_num++;
            }
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Header column
        JTableHeader theader = table_sales.getTableHeader();
        theader.setBackground(new Color(7,23,35));
        theader.setForeground(Color.white);
        theader.setFont(new Font("Sarabun", Font.PLAIN, 18));
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table_sales.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table_sales.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table_sales.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table_sales.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table_sales.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        table_sales.setRowHeight(30);
        table_sales.setFont(new Font("Sarabun", Font.PLAIN, 16));
        table_sales.setBackground(Color.white);
    }
    
    private void initTableCustomer(){
        DefaultTableModel model = (DefaultTableModel) table_customer.getModel();
        model.setRowCount(0);
        
        Connection conn = new dbConnect().dbcon();
        Statement st;
        ResultSet rs;
        
        String pList = "";
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT * FROM customer";
            rs = st.executeQuery(sql);
            int i = 0;
            while(rs.next()){
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("surename"), rs.getString("address"), rs.getString("phone_number")});
                /*product_store[i][0] = 0;
                product_store[i][1] = rs.getInt("price");
                i++;
                product_num++;*/
            }
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Header column
        JTableHeader theader = table_customer.getTableHeader();
        theader.setBackground(new Color(7,23,35));
        theader.setForeground(Color.white);
        theader.setFont(new Font("Sarabun", Font.PLAIN, 18));
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table_customer.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table_customer.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table_customer.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table_customer.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table_customer.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        table_customer.setRowHeight(30);
        table_customer.setFont(new Font("Sarabun", Font.PLAIN, 16));
        table_customer.setBackground(Color.white);
    }
    
    private void initTableReport(){
        DefaultTableModel model = (DefaultTableModel) table_report.getModel();
        model.setRowCount(0);
        
        Connection conn = new dbConnect().dbcon();
        Statement st;
        ResultSet rs;
        
        String pList = "";
        total_price_report = 0;
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT * FROM report_total";
            rs = st.executeQuery(sql);
            int i = 0;
            while(rs.next()){
                model.addRow(new Object[]{rs.getString("id"), rs.getString("customer_name"), rs.getString("total_price"), rs.getString("date")});
                total_price_report += rs.getInt("total_price");
                /*product_store[i][0] = 0;
                product_store[i][1] = rs.getInt("price");
                i++;
                product_num++;*/
            }
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        price_report.setText(total_price_report + "");
        
        //Header column
        JTableHeader theader = table_report.getTableHeader();
        theader.setBackground(new Color(7,23,35));
        theader.setForeground(Color.white);
        theader.setFont(new Font("Sarabun", Font.PLAIN, 18));
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table_report.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table_report.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table_report.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table_report.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        table_report.setRowHeight(30);
        table_report.setFont(new Font("Sarabun", Font.PLAIN, 16));
        table_report.setBackground(Color.white);
    }
    
    private void initTableManage(){
        DefaultTableModel model = (DefaultTableModel) table_manage.getModel();
        model.setRowCount(0);
        
        Connection conn = new dbConnect().dbcon();
        Statement st;
        ResultSet rs;
        
        String pList = "";
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT * FROM product";
            rs = st.executeQuery(sql);
            int i = 0;
            while(rs.next()){
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("amount"), rs.getString("price"), rs.getString("insurance")});
                /*product_store[i][0] = 0;
                product_store[i][1] = rs.getInt("price");
                i++;
                product_num++;*/
            }
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Header column
        JTableHeader theader = table_manage.getTableHeader();
        theader.setBackground(new Color(7,23,35));
        theader.setForeground(Color.white);
        theader.setFont(new Font("Sarabun", Font.PLAIN, 18));
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table_manage.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table_manage.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table_manage.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table_manage.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table_manage.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        table_manage.setRowHeight(30);
        table_manage.setFont(new Font("Sarabun", Font.PLAIN, 16));
        table_manage.setBackground(Color.white);
    }
    
    public void centerFrame() {

            Dimension windowSize = getSize();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Point centerPoint = ge.getCenterPoint();

            int dx = centerPoint.x - windowSize.width / 2;
            int dy = centerPoint.y - windowSize.height / 2;    
            setLocation(dx, dy);
            
            jPanel1.requestFocusInWindow();
    }
    
    
    private void addComboBox(){
        combo.removeAllItems();
        combo.addItem("");
        Connection conn = new dbConnect().dbcon();
        Statement st;
        ResultSet rs;
        PreparedStatement pt = null;
        
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT name FROM customer";
            rs = st.executeQuery(sql);
            while(rs.next()){
                combo.addItem(rs.getString("name"));
            }
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        j_sales = new javax.swing.JPanel();
        btn_sales = new javax.swing.JLabel();
        j_manage = new javax.swing.JPanel();
        btn_manage = new javax.swing.JLabel();
        j_report = new javax.swing.JPanel();
        btn_report = new javax.swing.JLabel();
        j_customer = new javax.swing.JPanel();
        btn_customer = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dash_manage = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_manage = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        manage_price = new javax.swing.JTextField();
        manage_name = new javax.swing.JTextField();
        manage_amount = new javax.swing.JTextField();
        manage_insurance = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        dash_report = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        price_report = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_report = new javax.swing.JTable();
        dash_sales = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_sales = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        total_price = new javax.swing.JTextField();
        btn_p_add = new javax.swing.JButton();
        btn_p_reduce = new javax.swing.JButton();
        btn_p_clear = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        total_count = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        value_count = new javax.swing.JTextField();
        btn_pay = new javax.swing.JButton();
        combo = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        dash_customer = new javax.swing.JPanel();
        panel_customer = new javax.swing.JPanel();
        btn_delete = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        surename = new javax.swing.JTextField();
        address = new javax.swing.JTextField();
        phone_number = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_customer = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(35, 119, 164));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("STORE");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_add_shopping_cart_50px.png"))); // NOI18N
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, -1, 50));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 50));

        jPanel2.setBackground(new java.awt.Color(27, 88, 121));
        jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
        });
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel2MousePressed(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Impact", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("DASHBOARD");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/cross_dashboard.png"))); // NOI18N
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 10, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/minimize_dashboard.png"))); // NOI18N
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 10, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 50));

        jPanel3.setBackground(new java.awt.Color(7, 23, 35));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/computer_exam.png"))); // NOI18N
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, -1, 170));

        j_sales.setBackground(new java.awt.Color(35, 119, 164));
        j_sales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        j_sales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                j_salesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                j_salesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                j_salesMouseExited(evt);
            }
        });
        j_sales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_sales.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        btn_sales.setForeground(new java.awt.Color(210, 229, 244));
        btn_sales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_sales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_shop_64px.png"))); // NOI18N
        btn_sales.setText("  หน้าขาย");
        btn_sales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        j_sales.add(btn_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel3.add(j_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 220, 70));

        j_manage.setBackground(new java.awt.Color(7, 23, 35));
        j_manage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        j_manage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                j_manageMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                j_manageMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                j_manageMouseExited(evt);
            }
        });
        j_manage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_manage.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        btn_manage.setForeground(new java.awt.Color(210, 229, 244));
        btn_manage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_manage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_maintenance_40px.png"))); // NOI18N
        btn_manage.setText("  จัดการสินค้า");
        btn_manage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        j_manage.add(btn_manage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel3.add(j_manage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 220, 70));

        j_report.setBackground(new java.awt.Color(7, 23, 35));
        j_report.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        j_report.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                j_reportMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                j_reportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                j_reportMouseExited(evt);
            }
        });
        j_report.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_report.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        btn_report.setForeground(new java.awt.Color(210, 229, 244));
        btn_report.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_report.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_business_report_100px.png"))); // NOI18N
        btn_report.setText("  รายงานการขาย");
        btn_report.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        j_report.add(btn_report, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel3.add(j_report, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 220, 70));

        j_customer.setBackground(new java.awt.Color(7, 23, 35));
        j_customer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        j_customer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                j_customerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                j_customerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                j_customerMouseExited(evt);
            }
        });
        j_customer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_customer.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        btn_customer.setForeground(new java.awt.Color(210, 229, 244));
        btn_customer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_customer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_user_100px.png"))); // NOI18N
        btn_customer.setText("  ลูกค้า");
        btn_customer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        j_customer.add(btn_customer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel3.add(j_customer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 220, 70));

        jPanel7.setBackground(new java.awt.Color(3, 15, 20));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(35, 119, 164));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 5, -1));

        jLabel1.setFont(new java.awt.Font("Sarabun", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_menu_50px.png"))); // NOI18N
        jLabel1.setText("   เมนู");
        jPanel7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, 50));

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 230, 70));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 700));

        dash_manage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table_manage.setFont(new java.awt.Font("Sarabun", 0, 11)); // NOI18N
        table_manage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัส", "ชื่อสินค้า", "ราคา", "จำนวนคงเหลือ", "ประกัน"
            }
        ));
        table_manage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        table_manage.setFocusable(false);
        table_manage.setSelectionBackground(new java.awt.Color(170, 225, 255));
        table_manage.setShowHorizontalLines(false);
        table_manage.setShowVerticalLines(false);
        table_manage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_manageMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(table_manage);

        dash_manage.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 920, 310));

        jPanel9.setBackground(new java.awt.Color(7, 23, 35));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        manage_price.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        manage_price.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel9.add(manage_price, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 259, 49));

        manage_name.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        manage_name.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        manage_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manage_nameActionPerformed(evt);
            }
        });
        jPanel9.add(manage_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 259, 49));

        manage_amount.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        manage_amount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel9.add(manage_amount, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 259, 50));

        manage_insurance.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        manage_insurance.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel9.add(manage_insurance, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 259, 49));

        jLabel18.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("ประกัน");
        jPanel9.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 92, 49));

        jLabel19.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("ชื่อสินค้า");
        jPanel9.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 92, 68));

        jLabel20.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("จำนวน");
        jPanel9.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 92, 49));

        jLabel21.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("ราคา");
        jPanel9.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 92, 49));

        jButton4.setBackground(new java.awt.Color(0, 255, 255));
        jButton4.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/update.png"))); // NOI18N
        jButton4.setText("  Update");
        jButton4.setBorderPainted(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 70, 136, 70));

        jButton5.setBackground(new java.awt.Color(0, 204, 0));
        jButton5.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_add_48px.png"))); // NOI18N
        jButton5.setText("  Add");
        jButton5.setBorderPainted(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 136, 70));

        jButton6.setBackground(new java.awt.Color(255, 0, 0));
        jButton6.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jButton6.setText("  Delete");
        jButton6.setBorderPainted(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 190, 136, 68));

        dash_manage.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 920, 340));

        jPanel1.add(dash_manage, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 920, 650));

        dash_report.setBackground(new java.awt.Color(255, 255, 255));
        dash_report.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Sarabun", 0, 24)); // NOI18N
        jLabel22.setText("ยอดขายทั้งหมด");
        dash_report.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, -1, -1));

        price_report.setBackground(new java.awt.Color(240, 240, 240));
        price_report.setFont(new java.awt.Font("Sarabun", 0, 24)); // NOI18N
        dash_report.add(price_report, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, 220, -1));

        jLabel17.setFont(new java.awt.Font("Sarabun", 0, 24)); // NOI18N
        jLabel17.setText("บาท");
        dash_report.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 40, -1, -1));

        table_report.setFont(new java.awt.Font("Sarabun", 0, 11)); // NOI18N
        table_report.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัส", "ลูกค้า", "ยอดขาย", "วันที่"
            }
        ));
        table_report.setAutoscrolls(false);
        table_report.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table_report.setFocusable(false);
        table_report.setGridColor(new java.awt.Color(255, 255, 255));
        table_report.setRequestFocusEnabled(false);
        table_report.setRowMargin(0);
        table_report.setRowSelectionAllowed(false);
        table_report.setSelectionBackground(new java.awt.Color(170, 225, 255));
        table_report.setShowHorizontalLines(false);
        table_report.setShowVerticalLines(false);
        jScrollPane3.setViewportView(table_report);

        dash_report.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 920, 540));

        jPanel1.add(dash_report, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 920, 650));

        dash_sales.setBackground(new java.awt.Color(255, 255, 255));
        dash_sales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table_sales.setBackground(new java.awt.Color(255, 51, 51));
        table_sales.setFont(new java.awt.Font("Sarabun", 0, 11)); // NOI18N
        table_sales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัส", "ชื่อสินค้า", "คงเหลือ", "ราคา", "ประกัน"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_sales.setAlignmentX(0.0F);
        table_sales.setAlignmentY(0.0F);
        table_sales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        table_sales.setFocusable(false);
        table_sales.setGridColor(new java.awt.Color(7, 23, 35));
        table_sales.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table_sales.setSelectionBackground(new java.awt.Color(170, 225, 255));
        table_sales.setShowGrid(false);
        table_sales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_salesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_sales);
        if (table_sales.getColumnModel().getColumnCount() > 0) {
            table_sales.getColumnModel().getColumn(1).setResizable(false);
            table_sales.getColumnModel().getColumn(2).setResizable(false);
            table_sales.getColumnModel().getColumn(3).setResizable(false);
            table_sales.getColumnModel().getColumn(4).setResizable(false);
        }

        dash_sales.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 650));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(7, 23, 35));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 220, 5));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/counter_services.png"))); // NOI18N
        jLabel8.setToolTipText("");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, -1, 130));

        jLabel9.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel9.setText("ลูกค้า");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));

        total_price.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jPanel6.add(total_price, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 180, 30));

        btn_p_add.setBackground(new java.awt.Color(35, 119, 164));
        btn_p_add.setFont(new java.awt.Font("Sarabun", 0, 12)); // NOI18N
        btn_p_add.setForeground(new java.awt.Color(255, 255, 255));
        btn_p_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_add_48px.png"))); // NOI18N
        btn_p_add.setText("   เพิ่มจำนวนสินค้า");
        btn_p_add.setBorder(null);
        btn_p_add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_p_add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_p_addMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_p_addMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_p_addMouseExited(evt);
            }
        });
        jPanel6.add(btn_p_add, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 160, -1));

        btn_p_reduce.setBackground(new java.awt.Color(35, 119, 164));
        btn_p_reduce.setFont(new java.awt.Font("Sarabun", 0, 12)); // NOI18N
        btn_p_reduce.setForeground(new java.awt.Color(255, 255, 255));
        btn_p_reduce.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_minus_64px.png"))); // NOI18N
        btn_p_reduce.setText("   ลดจำนวนสินค้า  ");
        btn_p_reduce.setBorder(null);
        btn_p_reduce.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_p_reduce.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_p_reduceMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_p_reduceMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_p_reduceMouseExited(evt);
            }
        });
        jPanel6.add(btn_p_reduce, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 160, -1));

        btn_p_clear.setBackground(new java.awt.Color(35, 119, 164));
        btn_p_clear.setFont(new java.awt.Font("Sarabun", 0, 12)); // NOI18N
        btn_p_clear.setForeground(new java.awt.Color(255, 255, 255));
        btn_p_clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_clear_symbol_100px.png"))); // NOI18N
        btn_p_clear.setText("    ลบสินค้า            ");
        btn_p_clear.setBorder(null);
        btn_p_clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_p_clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_p_clearMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_p_clearMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_p_clearMouseExited(evt);
            }
        });
        jPanel6.add(btn_p_clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 160, -1));

        jLabel10.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel10.setText("ราคารวม");
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, -1, -1));

        total_count.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jPanel6.add(total_count, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 180, 30));

        jLabel11.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        jLabel11.setText("จำนวนที่เลือก");
        jPanel6.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        value_count.setFont(new java.awt.Font("Sarabun", 0, 18)); // NOI18N
        value_count.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        value_count.setBorder(null);
        jPanel6.add(value_count, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 50, 40));

        btn_pay.setBackground(new java.awt.Color(92, 184, 92));
        btn_pay.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        btn_pay.setForeground(new java.awt.Color(255, 255, 255));
        btn_pay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com_market_img/icons8_refund_48px.png"))); // NOI18N
        btn_pay.setText("  ชำระเงิน");
        btn_pay.setBorder(null);
        btn_pay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_pay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_payMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_payMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_payMouseExited(evt);
            }
        });
        jPanel6.add(btn_pay, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 180, 40));

        combo.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        combo.setBorder(null);
        jPanel6.add(combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 180, 30));

        jLabel12.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel12.setText("จำนวนสินค้า");
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, -1, -1));

        dash_sales.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, 220, 650));

        jPanel1.add(dash_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 920, 650));

        dash_customer.setBackground(new java.awt.Color(255, 255, 255));
        dash_customer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_customer.setBackground(new java.awt.Color(7, 23, 35));
        panel_customer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_delete.setBackground(new java.awt.Color(255, 0, 0));
        btn_delete.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        btn_delete.setText("  DELETE");
        btn_delete.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.black, null, null));
        btn_delete.setBorderPainted(false);
        btn_delete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_deleteMouseClicked(evt);
            }
        });
        panel_customer.add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(649, 197, 139, 57));

        btn_update.setBackground(new java.awt.Color(0, 255, 255));
        btn_update.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        btn_update.setForeground(new java.awt.Color(255, 255, 255));
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/update.png"))); // NOI18N
        btn_update.setText("  UPDATE");
        btn_update.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.black, null, null));
        btn_update.setBorderPainted(false);
        btn_update.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_updateMouseClicked(evt);
            }
        });
        panel_customer.add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(649, 122, 139, 57));

        btn_add.setBackground(new java.awt.Color(0, 204, 0));
        btn_add.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        btn_add.setForeground(new java.awt.Color(255, 255, 255));
        btn_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/insert.png"))); // NOI18N
        btn_add.setText("  ADD       ");
        btn_add.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.black, null, null));
        btn_add.setBorderPainted(false);
        btn_add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_addMouseClicked(evt);
            }
        });
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        panel_customer.add(btn_add, new org.netbeans.lib.awtextra.AbsoluteConstraints(649, 47, 139, 57));

        jLabel13.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("ชื่อ");
        panel_customer.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 112, 37));

        jLabel14.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("ที่อยู่");
        panel_customer.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 112, 37));

        jLabel15.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("เบอร์โทรศัพท์");
        panel_customer.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 122, 37));

        jLabel16.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("นามสกุล");
        panel_customer.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 112, 37));

        name.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        name.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panel_customer.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 259, 37));

        surename.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        surename.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        surename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                surenameActionPerformed(evt);
            }
        });
        panel_customer.add(surename, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 259, 37));

        address.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        address.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panel_customer.add(address, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 259, 37));

        phone_number.setFont(new java.awt.Font("Sarabun", 0, 16)); // NOI18N
        phone_number.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panel_customer.add(phone_number, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 220, 259, 37));

        dash_customer.add(panel_customer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 920, 290));

        table_customer.setFont(new java.awt.Font("Sarabun", 0, 18)); // NOI18N
        table_customer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "รหัส", "ชื่อ", "นามสกุล", "ที่อยู่", "เบอร์โทรศัพท์"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_customer.setAlignmentX(0.0F);
        table_customer.setAlignmentY(0.0F);
        table_customer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        table_customer.setFocusable(false);
        table_customer.setGridColor(new java.awt.Color(255, 255, 255));
        table_customer.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table_customer.setSelectionBackground(new java.awt.Color(170, 225, 255));
        table_customer.setShowHorizontalLines(false);
        table_customer.setShowVerticalLines(false);
        table_customer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_customerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_customer);

        dash_customer.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 920, 360));

        jPanel1.add(dash_customer, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 920, 650));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        this.setState(Frame.ICONIFIED);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y-yMouse);
    }//GEN-LAST:event_jPanel2MouseDragged

    private void jPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jPanel2MousePressed

    private void j_salesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_salesMouseEntered
        onHover("หน้าขาย", btn_sales, j_sales, 1);
    }//GEN-LAST:event_j_salesMouseEntered

    private void j_salesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_salesMouseExited
        onMouseOut("หน้าขาย", btn_sales, j_sales, 1);
    }//GEN-LAST:event_j_salesMouseExited

    private void j_manageMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_manageMouseEntered
        onHover("จัดการสินค้า", btn_manage, j_manage, 3);
    }//GEN-LAST:event_j_manageMouseEntered

    private void j_manageMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_manageMouseExited
        onMouseOut("จัดการสินค้า", btn_manage, j_manage, 3);
    }//GEN-LAST:event_j_manageMouseExited

    private void j_customerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_customerMouseEntered
        onHover("ลูกค้า", btn_customer, j_customer, 2);
    }//GEN-LAST:event_j_customerMouseEntered

    private void j_customerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_customerMouseExited
        onMouseOut("ลูกค้า", btn_customer, j_customer, 2);
    }//GEN-LAST:event_j_customerMouseExited

    private void j_salesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_salesMouseClicked
        onActive(1, btn_sales);
        active = 1;
        
        dash_sales.setVisible(true);
        dash_customer.setVisible(false);
        dash_report.setVisible(false);
        dash_manage.setVisible(false);
    }//GEN-LAST:event_j_salesMouseClicked

    private void j_customerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_customerMouseClicked
        onActive(2, btn_customer);
        active = 2;
        
        dash_sales.setVisible(false);
        dash_customer.setVisible(true);
        dash_report.setVisible(false);
        dash_manage.setVisible(false);
    }//GEN-LAST:event_j_customerMouseClicked

    private void j_manageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_manageMouseClicked
        onActive(3, btn_manage);
        active = 3;
        
        dash_sales.setVisible(false);
        dash_customer.setVisible(false);
        dash_report.setVisible(false);
        dash_manage.setVisible(true);
    }//GEN-LAST:event_j_manageMouseClicked

    private void table_salesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_salesMouseClicked
        int selected = table_sales.getSelectedRow();
        
        value_count.setText(product_store[selected][0] + "");
        row_selected = selected;
    }//GEN-LAST:event_table_salesMouseClicked

    private void btn_p_addMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_p_addMouseEntered
        btn_p_add.setBackground(new Color(27,88,121));
    }//GEN-LAST:event_btn_p_addMouseEntered

    private void btn_p_addMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_p_addMouseExited
        btn_p_add.setBackground(new Color(35,119,164));
    }//GEN-LAST:event_btn_p_addMouseExited

    private void btn_p_reduceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_p_reduceMouseEntered
        btn_p_reduce.setBackground(new Color(27,88,121));
    }//GEN-LAST:event_btn_p_reduceMouseEntered

    private void btn_p_reduceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_p_reduceMouseExited
        btn_p_reduce.setBackground(new Color(35,119,164));
    }//GEN-LAST:event_btn_p_reduceMouseExited

    private void btn_p_clearMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_p_clearMouseEntered
        btn_p_clear.setBackground(new Color(27,88,121));
    }//GEN-LAST:event_btn_p_clearMouseEntered

    private void btn_p_clearMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_p_clearMouseExited
        btn_p_clear.setBackground(new Color(35,119,164));
    }//GEN-LAST:event_btn_p_clearMouseExited

    private void btn_payMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_payMouseEntered
        btn_pay.setBackground(new Color(0, 166, 90));
    }//GEN-LAST:event_btn_payMouseEntered

    private void btn_payMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_payMouseExited
        btn_pay.setBackground(new Color(92,184,92));
    }//GEN-LAST:event_btn_payMouseExited

    private void btn_p_addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_p_addMouseClicked
        if(row_selected != -1){
            //System.out.println(row_selected);
            value_count.setText(++product_store[row_selected][0] + "");
            totalCount++;
            totalPrice += product_store[row_selected][1];
            total_count.setText(totalCount + "");
            total_price.setText(totalPrice + "");
        }
    }//GEN-LAST:event_btn_p_addMouseClicked

    private void btn_p_reduceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_p_reduceMouseClicked
        if(row_selected != -1){
            if(product_store[row_selected][0] > 0){
                value_count.setText(--product_store[row_selected][0] + "");
                totalCount--;
                totalPrice -= product_store[row_selected][1];
                total_count.setText(totalCount + "");
                total_price.setText(totalPrice + "");
            }
        }
    }//GEN-LAST:event_btn_p_reduceMouseClicked

    private void btn_p_clearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_p_clearMouseClicked
        if(row_selected != -1){
            totalPrice -= product_store[row_selected][0] * product_store[row_selected][1];
            totalCount -= product_store[row_selected][0];
            product_store[row_selected][0] = 0;

            value_count.setText(product_store[row_selected][0] + "");
            total_count.setText(totalCount + "");
            total_price.setText(totalPrice + "");
        }
    }//GEN-LAST:event_btn_p_clearMouseClicked

    private void btn_payMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_payMouseClicked
        if(totalPrice > 0){
            Connection conn = new dbConnect().dbcon();
            PreparedStatement pt = null;

            try{
                String sql = "UPDATE totalPrice SET price = " + totalPrice + " WHERE id=1";
                pt = conn.prepareStatement(sql);
                pt.execute();
            } catch(SQLException ex){
                Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                LocalDateTime now = LocalDateTime.now(); 
                
                String sql = "INSERT INTO report_total (customer_name, total_price, date) VALUES ('" + combo.getSelectedItem().toString() + "', " + totalPrice + ", '" + now + "')";
                pt = conn.prepareStatement(sql);
                pt.execute();
            } catch(SQLException ex){
                Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                String sql = "DELETE FROM report WHERE 1=1";
                pt = conn.prepareStatement(sql);
                pt.execute();
                
                Statement st;
                ResultSet rs;
                st = (Statement) conn.createStatement();
                
                sql = "SELECT id FROM product";
                rs = st.executeQuery(sql);
                int i=0;
                while(rs.next()){
                    sql = "INSERT INTO report (id, num) VALUES (" + rs.getInt("id") + ", "+ product_store[i][0] +")";
                    pt = conn.prepareStatement(sql);
                    pt.execute();
                    i++;
                }
                
                st = (Statement) conn.createStatement();
                sql = "SELECT id, amount FROM product";
                rs = st.executeQuery(sql);
                i=0;
                while(rs.next()){
                    sql = "UPDATE product SET amount = "+ (rs.getInt("amount") - product_store[i][0]) +" WHERE id=" + rs.getInt("id") + "";
                    pt = conn.prepareStatement(sql);
                    pt.execute();
                    i++;
                }
                initTableSales();
                initTableManage();
                
                totalCount = 0;
                totalPrice = 0;
                total_price.setText(totalPrice + "");
                total_count.setText(totalCount + "");
                
                for(i=0;i<product_store.length;i++){
                    product_store[i][0] = 0;
                }
                
            } catch(SQLException ex){
                Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
            }

            new pay().setVisible(true);
        }
    }//GEN-LAST:event_btn_payMouseClicked

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_addActionPerformed

    private void surenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surenameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_surenameActionPerformed

    private void btn_addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_addMouseClicked
        Connection conn = new dbConnect().dbcon();
        PreparedStatement pt = null;

        try{
            String sql = "INSERT INTO customer (name, surename, address, phone_number) VALUES ('"
                    + name.getText() + "', '" + surename.getText() + "', '" + address.getText() + "', '" + phone_number.getText() + "'"
                    + ") ";
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableCustomer();
            addComboBox();
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_addMouseClicked

    private void table_customerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_customerMouseClicked
        int selected = (int) table_customer.getValueAt(table_customer.getSelectedRow(), 0);
        customer_store = selected;
        
        Connection conn = new dbConnect().dbcon();
        Statement st;
        ResultSet rs;
        
        try{
            st = (Statement) conn.createStatement();

            String sql = "SELECT * FROM customer WHERE id=" + (selected) + "";
            rs = st.executeQuery(sql);
            int i=0;
            while(rs.next()){
                name.setText(rs.getString("name"));
                surename.setText(rs.getString("surename"));
                address.setText(rs.getString("address"));
                phone_number.setText(rs.getString("phone_number"));
            }
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_table_customerMouseClicked

    private void btn_updateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_updateMouseClicked
        Connection conn = new dbConnect().dbcon();
        PreparedStatement pt = null;

        try{
            String sql = "UPDATE customer SET name='" + name.getText() + "', surename='" + surename.getText() + "',"
                    + " address='" + address.getText() + "', phone_number='"+ phone_number.getText() +"' WHERE id="+ (customer_store) +" ";
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableCustomer();
            addComboBox();
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_updateMouseClicked

    private void btn_deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMouseClicked
        Connection conn = new dbConnect().dbcon();
        PreparedStatement pt = null;

        try{
            String sql = "DELETE FROM customer WHERE id="+ (customer_store) +" ";
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableCustomer();
            addComboBox();
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_deleteMouseClicked

    private void j_reportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_reportMouseExited
        onMouseOut("รายงานการขาย", btn_report, j_report, 4);
    }//GEN-LAST:event_j_reportMouseExited

    private void j_reportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_reportMouseEntered
        onHover("รายงานการขาย", btn_report, j_report, 4);
    }//GEN-LAST:event_j_reportMouseEntered

    private void j_reportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j_reportMouseClicked
        onActive(4, btn_report);
        active = 4;
        initTableReport();
        
        dash_sales.setVisible(false);
        dash_customer.setVisible(false);
        dash_report.setVisible(true);
        dash_manage.setVisible(false);
    }//GEN-LAST:event_j_reportMouseClicked

    private void manage_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manage_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_manage_nameActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Connection conn = new dbConnect().dbcon();
        PreparedStatement pt = null;

        try{
            String sql = "UPDATE `product` SET `name`='" + manage_name.getText() + "',`amount`='"+ manage_amount.getText() +"',`price`='"+ manage_price.getText() +"' WHERE id = '" +manage_insurance.getText()+"'";
            pt = conn.prepareStatement(sql);
            pt.execute();
        } catch(SQLException ex){
            System.out.print("error");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Connection conn = new dbConnect().dbcon();
        PreparedStatement pt = null;
        try {
            String sql = "INSERT INTO `product`(`name`, `amount`, `price`) VALUES('"
            + manage_name.getText() + "', '" + manage_amount.getText() + "', '" + manage_price.getText() + "'  "+") ";
            pt = conn.prepareStatement(sql);
            pt.execute();
        } catch (SQLException ex) {
            System.out.println("error");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Connection conn = new dbConnect().dbcon();

        PreparedStatement pt = null;
        try {
            String sql = "DELETE FROM `product` WHERE id  = '" + manage_insurance.getText() + "' ";
            pt = conn.prepareStatement(sql);
            pt.execute();
        } catch (SQLException ex) {
            System.out.println("error");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        Connection conn = new dbConnect().dbcon();
        PreparedStatement pt = null;

        try{
            String sql = "INSERT INTO product (name, amount, price, insurance) VALUES ('"
                    + manage_name.getText() + "', " + manage_amount.getText() + ", " + manage_price.getText() + ", '" + manage_insurance.getText() + "'"
                    + ") ";
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableManage();
            initTableSales();
            addComboBox();
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        Connection conn = new dbConnect().dbcon();
        PreparedStatement pt = null;

        try{
            String sql = "DELETE FROM product WHERE id="+ (manage_store) +" ";
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableManage();
            initTableSales();
            addComboBox();
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6MouseClicked

    private void table_manageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_manageMouseClicked
        int selected = (int) table_manage.getValueAt(table_manage.getSelectedRow(), 0);
        manage_store = selected;
        
        Connection conn = new dbConnect().dbcon();
        Statement st;
        ResultSet rs;
        
        try{
            st = (Statement) conn.createStatement();

            String sql = "SELECT * FROM product WHERE id=" + selected + "";
            rs = st.executeQuery(sql);
            while(rs.next()){
                manage_name.setText(rs.getString("name"));
                manage_amount.setText(rs.getString("amount"));
                manage_price.setText(rs.getString("price"));
                manage_insurance.setText(rs.getString("insurance"));
            }
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_table_manageMouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        Connection conn = new dbConnect().dbcon();
        PreparedStatement pt = null;

        try{
            String sql = "UPDATE product SET name='" + manage_name.getText() + "', amount=" + manage_amount.getText() + ","
                    + " price=" + manage_price.getText() + ", insurance='"+ manage_insurance.getText() +"' WHERE id="+ (manage_store) +" ";
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableManage();
            initTableSales();
            addComboBox();
        } catch(SQLException ex){
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4MouseClicked

    private void onActive(int bActive, javax.swing.JLabel btn){
        btn.setForeground(Color.white);
        
        if(bActive == 1){
            j_sales.setBackground(new Color(35,119,164));
            j_customer.setBackground(new Color(7,23,35));
            j_manage.setBackground(new Color(7,23,35));
            j_report.setBackground(new Color(7,23,35));
        } else if(bActive == 2){
            j_sales.setBackground(new Color(7,23,35));
            j_customer.setBackground(new Color(35,119,164));
            j_manage.setBackground(new Color(7,23,35));
            j_report.setBackground(new Color(7,23,35));
        } else if(bActive == 3){
            j_sales.setBackground(new Color(7,23,35));
            j_customer.setBackground(new Color(7,23,35));
            j_manage.setBackground(new Color(35,119,164));
            j_report.setBackground(new Color(7,23,35));
        } else if(bActive == 4){
            j_sales.setBackground(new Color(7,23,35));
            j_customer.setBackground(new Color(7,23,35));
            j_manage.setBackground(new Color(7,23,35));
            j_report.setBackground(new Color(35,119,164));
        }
    }
    
    private void onHover(String txt, javax.swing.JLabel btn, javax.swing.JPanel j, int a){
        if(a != active){
            j.setBackground(new Color(12, 36, 54));
            btn.setForeground(new Color(35,119,164));
        }
        
        String xett = "<html>&nbsp;&nbsp;<u>"+txt+"</u></html>";
        btn.setText(xett);
    }
    
    private void onMouseOut(String txt, javax.swing.JLabel btn, javax.swing.JPanel j, int a){      
        if(a != active) j.setBackground(new Color(7,23,35));
        String xett = "<html>&nbsp;&nbsp;"+txt+"</html>";
        btn.setText(xett);
        btn.setForeground(new Color(210,229,244)); 
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField address;
    private javax.swing.JButton btn_add;
    private javax.swing.JLabel btn_customer;
    private javax.swing.JButton btn_delete;
    private javax.swing.JLabel btn_manage;
    private javax.swing.JButton btn_p_add;
    private javax.swing.JButton btn_p_clear;
    private javax.swing.JButton btn_p_reduce;
    private javax.swing.JButton btn_pay;
    private javax.swing.JLabel btn_report;
    private javax.swing.JLabel btn_sales;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JPanel dash_customer;
    private javax.swing.JPanel dash_manage;
    private javax.swing.JPanel dash_report;
    private javax.swing.JPanel dash_sales;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel j_customer;
    private javax.swing.JPanel j_manage;
    private javax.swing.JPanel j_report;
    private javax.swing.JPanel j_sales;
    private javax.swing.JTextField manage_amount;
    private javax.swing.JTextField manage_insurance;
    private javax.swing.JTextField manage_name;
    private javax.swing.JTextField manage_price;
    private javax.swing.JTextField name;
    private javax.swing.JPanel panel_customer;
    private javax.swing.JTextField phone_number;
    private javax.swing.JTextField price_report;
    private javax.swing.JTextField surename;
    private javax.swing.JTable table_customer;
    private javax.swing.JTable table_manage;
    private javax.swing.JTable table_report;
    private javax.swing.JTable table_sales;
    private javax.swing.JTextField total_count;
    private javax.swing.JTextField total_price;
    private javax.swing.JTextField value_count;
    // End of variables declaration//GEN-END:variables
}
