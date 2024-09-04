package com.example.project.measurementsexplorer;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import static com.example.project.alert.showMymsg;
import static com.example.project.sqlConnector.mySqlConnector.doConnect;

public class itemsRecordsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField mNo;

    @FXML
    private ComboBox<String> orderid;

    @FXML
    private TableView<measurementBean> tableid;

    @FXML
    private ComboBox<String> workerName;

    String fileName;

    @FXML
    void doExport(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Input Excel File Name");
        dialog.setContentText("Fill Excel File Name");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String fileName = result.get().trim();
            if (fileName.isEmpty()) {
                showMymsg("Invalid File Name");
                return;
            }

            try {
                writeExcel(fileName);
                showMymsg("Exported to Excel!!!");
            } catch (Exception ex) {
                ex.printStackTrace();
                showMymsg("Error exporting to Excel: " + ex.getMessage());
            }
        } else {
            showMymsg("Don't Have File");
        }
    }

    public void writeExcel(String fileName) throws Exception {
        try (Writer writer = new BufferedWriter(new FileWriter(fileName + ".csv"))) {
            String header = "Order Id,Mobile,Dress,Img path,Date of Delivery,Qty,Bill,Measurement,Worker,Status,Date of Order\n";
            writer.write(header);

            // Get the items currently displayed in the TableView
            ObservableList<measurementBean> items = tableid.getItems();

            for (measurementBean p : items) {
                String text = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%d\",\"%d\",\"%s\",\"%s\",\"%d\",\"%s\"\n",
                        p.getOrderid(), p.getMobile(), p.getDress(), p.getPic(), p.getDodel(), p.getQty(),
                        p.getBill(), p.getMeasurement(), p.getWname(), p.getStatus(), p.getDoorder());
                writer.write(text);
            }
        } catch (IOException ex) {
            throw new Exception("Error writing to Excel file: " + ex.getMessage(), ex);
        }
    }


    @FXML
    void doShow(ActionEvent event) {

        tableid.getColumns().clear();
        TableColumn<measurementBean, Integer> OrderID = new TableColumn<measurementBean, Integer>("Order ID");
        OrderID.setCellValueFactory(new PropertyValueFactory<>("orderid"));
        OrderID.setMinWidth(100);

        TableColumn<measurementBean, String> Mobile = new TableColumn<measurementBean, String>("Mobile");
        Mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        Mobile.setMinWidth(100);

        TableColumn<measurementBean, String> dress = new TableColumn<measurementBean, String>("Dress");
        dress.setCellValueFactory(new PropertyValueFactory<>("dress"));
        dress.setMinWidth(100);

        TableColumn<measurementBean, String> pic = new TableColumn<measurementBean, String>("Img path");
        pic.setCellValueFactory(new PropertyValueFactory<>("pic"));
        pic.setMinWidth(100);

        TableColumn<measurementBean, Date> dodel = new TableColumn<measurementBean, Date>("Date of Delivery");
        dodel.setCellValueFactory(new PropertyValueFactory<>("dodel"));
        dodel.setMinWidth(100);

        TableColumn<measurementBean, Integer> qty = new TableColumn<measurementBean, Integer>("Quantity");
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qty.setMinWidth(100);

        TableColumn<measurementBean, Integer> bill = new TableColumn<measurementBean, Integer>("Bill");
        bill.setCellValueFactory(new PropertyValueFactory<>("bill"));
        bill.setMinWidth(100);

        TableColumn<measurementBean, String> measure = new TableColumn<measurementBean, String>("Measurement");
        measure.setCellValueFactory(new PropertyValueFactory<>("measurement"));
        measure.setMinWidth(100);

        TableColumn<measurementBean, String> wname = new TableColumn<measurementBean, String>("Worker");
        wname.setCellValueFactory(new PropertyValueFactory<>("wname"));
        wname.setMinWidth(100);

        TableColumn<measurementBean, String> status = new TableColumn<measurementBean, String>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        status.setMinWidth(100);

        TableColumn<measurementBean, Date> doorder = new TableColumn<measurementBean, Date>("Date of Order");
        doorder.setCellValueFactory(new PropertyValueFactory<>("doorder"));
        doorder.setMinWidth(100);

        tableid.getColumns().addAll(OrderID,Mobile, dress,pic, dodel, qty, bill, measure,wname, status, doorder);
        tableid.setItems(getAllRecords());

    }
    ObservableList<measurementBean> getAllRecords()
    {
        ObservableList<measurementBean> records = FXCollections.observableArrayList();
        try
        {
            stmt = con.prepareStatement("select * from measurements");
            ResultSet res = stmt.executeQuery();
            while(res.next())
            {
                int orderr = res.getInt("orderid");
                String mob = res.getString("mobile");
                String dress = res.getString("dress");
                String pic = res.getString("pic");
                Date dodel = res.getDate("dodel");
                int qty = res.getInt("qty");
                int bill = res.getInt("bill");
                String measure = res.getString("measurement");
                String worker = res.getString("wname");
                int status = res.getInt("status");
                Date dorder = res.getDate("doorder");

                records.add(new measurementBean(orderr,mob,dress,pic,dodel,qty,bill,measure,worker,status,dorder));
            }
        }catch(Exception exp)
        {
            exp.printStackTrace();
        }
        return records;
    }

    @FXML
    void fetchOrders(ActionEvent event) {
        tableid.getColumns().clear();
        TableColumn<measurementBean, Integer> OrderID = new TableColumn<measurementBean, Integer>("Order ID");
        OrderID.setCellValueFactory(new PropertyValueFactory<>("orderid"));
        OrderID.setMinWidth(100);

        TableColumn<measurementBean, String> dress = new TableColumn<measurementBean, String>("Dress");
        dress.setCellValueFactory(new PropertyValueFactory<>("dress"));
        dress.setMinWidth(100);

        TableColumn<measurementBean, Date> dodel = new TableColumn<measurementBean, Date>("Date of Delivery");
        dodel.setCellValueFactory(new PropertyValueFactory<>("dodel"));
        dodel.setMinWidth(100);

        TableColumn<measurementBean, Integer> qty = new TableColumn<measurementBean, Integer>("Quantity");
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qty.setMinWidth(100);

        TableColumn<measurementBean, Integer> bill = new TableColumn<measurementBean, Integer>("Bill");
        bill.setCellValueFactory(new PropertyValueFactory<>("bill"));
        bill.setMinWidth(100);

        TableColumn<measurementBean, String> wname = new TableColumn<measurementBean, String>("Worker");
        wname.setCellValueFactory(new PropertyValueFactory<>("wname"));
        wname.setMinWidth(100);

        TableColumn<measurementBean, Date> doorder = new TableColumn<measurementBean, Date>("Date of Order");
        doorder.setCellValueFactory(new PropertyValueFactory<>("doorder"));
        doorder.setMinWidth(100);

        tableid.getColumns().addAll(OrderID,dress, dodel, qty, bill, wname,doorder);
        tableid.setItems(getRecordsCustomer());



    }
    ObservableList<measurementBean> getRecordsCustomer()
    {
        ObservableList<measurementBean> records = FXCollections.observableArrayList();
        String mobile = mNo.getText();
        try
        {
            stmt = con.prepareStatement("select orderid, dress, dodel, qty, bill, wname, doorder from measurements where mobile = ?");
            stmt.setString(1,mobile);
            ResultSet res = stmt.executeQuery();
            while(res.next())
            {
                int orderr = res.getInt("orderid");

                String dress = res.getString("dress");

                Date dodel = res.getDate("dodel");
                int qty = res.getInt("qty");

                int bill = res.getInt("bill");
                String workerr = res.getString("wname");
                Date dorder = res.getDate("doorder");
                measurementBean mb = new measurementBean();
                mb.setOrderid(orderr);
                mb.setDress(dress);
                mb.setDodel(dodel);
                mb.setQty(qty);
                mb.setBill(bill);
                mb.setWname(workerr);
                mb.setDoorder(dorder);
                records.add(mb);
            }
        }catch(Exception exp)

        {
        exp.printStackTrace();
        }
        return records;
    }

    @FXML
    void orderTable(ActionEvent event) {
        tableid.getColumns().clear();
        TableColumn<measurementBean, Integer> OrderID = new TableColumn<measurementBean, Integer>("Order ID");
        OrderID.setCellValueFactory(new PropertyValueFactory<>("orderid"));
        OrderID.setMinWidth(100);

        TableColumn<measurementBean, String> Mobile = new TableColumn<measurementBean, String>("Mobile");
        Mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        Mobile.setMinWidth(100);

        TableColumn<measurementBean, String> dress = new TableColumn<measurementBean, String>("Dress");
        dress.setCellValueFactory(new PropertyValueFactory<>("dress"));
        dress.setMinWidth(100);

        TableColumn<measurementBean, String> pic = new TableColumn<measurementBean, String>("Img path");
        pic.setCellValueFactory(new PropertyValueFactory<>("pic"));
        pic.setMinWidth(100);

        TableColumn<measurementBean, Date> dodel = new TableColumn<measurementBean, Date>("Date of Delivery");
        dodel.setCellValueFactory(new PropertyValueFactory<>("dodel"));
        dodel.setMinWidth(100);

        TableColumn<measurementBean, Integer> qty = new TableColumn<measurementBean, Integer>("Quantity");
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qty.setMinWidth(100);

        TableColumn<measurementBean, Integer> bill = new TableColumn<measurementBean, Integer>("Bill");
        bill.setCellValueFactory(new PropertyValueFactory<>("bill"));
        bill.setMinWidth(100);

        TableColumn<measurementBean, String> measure = new TableColumn<measurementBean, String>("Measurement");
        measure.setCellValueFactory(new PropertyValueFactory<>("measurement"));
        measure.setMinWidth(100);

        TableColumn<measurementBean, String> worker = new TableColumn<measurementBean, String>("Worker");
        worker.setCellValueFactory(new PropertyValueFactory<>("wname"));
        worker.setMinWidth(100);

        TableColumn<measurementBean, String> status = new TableColumn<measurementBean, String>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        status.setMinWidth(100);

        TableColumn<measurementBean, Date> dorder = new TableColumn<measurementBean, Date>("Date of Order");
        dorder.setCellValueFactory(new PropertyValueFactory<>("doorder"));
        dorder.setMinWidth(100);

        tableid.getColumns().addAll(OrderID,Mobile, dress,pic, dodel, qty, bill, measure,worker, status, dorder);
        tableid.setItems(getRecordsFromOrder());

    }

    @FXML
    void workerTable(ActionEvent event) {
        tableid.getColumns().clear();
        TableColumn<measurementBean, Integer> OrderID = new TableColumn<measurementBean, Integer>("Order ID");
        OrderID.setCellValueFactory(new PropertyValueFactory<>("orderid"));
        OrderID.setMinWidth(100);

        TableColumn<measurementBean, String> dress = new TableColumn<measurementBean, String>("Dress");
        dress.setCellValueFactory(new PropertyValueFactory<>("dress"));
        dress.setMinWidth(100);

        TableColumn<measurementBean, Date> dodel = new TableColumn<measurementBean, Date>("Date of Delivery");
        dodel.setCellValueFactory(new PropertyValueFactory<>("dodel"));
        dodel.setMinWidth(100);

        TableColumn<measurementBean, Integer> qty = new TableColumn<measurementBean, Integer>("Quantity");
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qty.setMinWidth(100);

        TableColumn<measurementBean, String> worker = new TableColumn<measurementBean, String>("Worker");
        worker.setCellValueFactory(new PropertyValueFactory<>("wname"));
        worker.setMinWidth(100);

        tableid.getColumns().addAll(OrderID,dress,dodel,qty,worker);
        tableid.setItems(getRecordsWorker());

    }

    ObservableList<measurementBean> getRecordsWorker()
    {
        ObservableList<measurementBean> records = FXCollections.observableArrayList();
        String worker = workerName.getSelectionModel().getSelectedItem();
        try
        {
            stmt = con.prepareStatement("select orderid, dress,dodel, qty,wname from measurements where wname = ? and status = ?");
            stmt.setString(1,workerName.getSelectionModel().getSelectedItem());
            int orderS=0;
            if(orderid.getSelectionModel().getSelectedIndex()==0)
                orderS =1;
            else if(orderid.getSelectionModel().getSelectedIndex()==1)
                orderS = 2;
            else
                orderS =3;
            stmt.setInt(2,orderS);
            ResultSet res = stmt.executeQuery();
            while(res.next())
            {
                int orderr = res.getInt("orderid");

                String dress = res.getString("dress");

                Date dodel = res.getDate("dodel");
                int qty = res.getInt("qty");


                String workerr = res.getString("wname");
                measurementBean mb = new measurementBean();
                mb.setOrderid(orderr);
                mb.setDress(dress);
                mb.setDodel(dodel);
                mb.setQty(qty);
                mb.setWname(workerr);
                records.add(mb);

            }


        }catch(Exception exp)
        {
            exp.printStackTrace();
        }
        return records;
    }
    ObservableList<measurementBean> getRecordsFromOrder()
    {
        ObservableList<measurementBean> records = FXCollections.observableArrayList();
        String order = orderid.getSelectionModel().getSelectedItem();
        try
        {
            stmt = con.prepareStatement("select * from measurements where status = ?");
            int orderS=0;
            if(orderid.getSelectionModel().getSelectedIndex()==0)
                orderS =1;
            else if(orderid.getSelectionModel().getSelectedIndex()==1)
                orderS = 2;
            else
                orderS =3;

            stmt.setInt(1,orderS);
            ResultSet res = stmt.executeQuery();
            while(res.next())
            {
                int orderr = res.getInt("orderid");
                String mob = res.getString("mobile");
                String dress = res.getString("dress");
                String pic = res.getString("pic");
                Date dodel = res.getDate("dodel");
                int qty = res.getInt("qty");
                int bill = res.getInt("bill");
                String measure = res.getString("measurement");
                String worker = res.getString("wname");
                Date dorder = res.getDate("doorder");
                int status = res.getInt("status");

                records.add(new measurementBean(orderr,mob,dress,pic,dodel,qty,bill,measure,worker,status,dorder));
            }

        }catch(Exception exp)
        {
            exp.printStackTrace();
        }
        return records;
    }
    PreparedStatement stmt;
    Connection con;
    @FXML
    void initialize() {
        con = doConnect();
        if(con==null)
            System.out.println("Database not connected....");
        else
            System.out.println("Connected to database successfully.......");
        String []orders = {"In Progress","Order recieved from worker","Order delivered to Customer"};
        orderid.getItems().addAll(orders);
        try {
            stmt = con.prepareStatement("select distinct wname from measurements");
            ResultSet res = stmt.executeQuery();
            ArrayList<String> ary = new ArrayList<>();
            while(res.next())
            {
                ary.add(res.getString("wname"));
            }
            workerName.getItems().addAll(ary);
        }catch(Exception exp)
        {
            exp.printStackTrace();
        }

    }

}
