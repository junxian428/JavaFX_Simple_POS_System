import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PosController implements Initializable{

    public class Item{
        private final String ID;
        private final String Name;
        private final String Price;
       
        public Item(String ID, String Name, String Price) {
         this.ID = ID;
         this.Name = Name;
         this.Price = Price;
        }
       
        public String getID() {
         return ID;
        }
       
        public String getName() {
         return Name;
        }
       
        public String getPrice() {
         return Price;
        }
    
        
    }
    public int total_row = 0;

    @FXML
    private Button EXIT,Add_Transaction,DeleteItem, Pay;

    @FXML
    private Label JAVAFXSUM;

    @FXML
    private ComboBox ComboxBoxForSelection;

    @FXML
    private TextField Product_ID,Product_Name,Product_Price;

    @FXML
    private TableView<Item> tbData;
    @FXML
    public TableColumn<Item, String> Col_ID;

    @FXML
    public TableColumn<Item, String> Col_Name;

    @FXML
    public TableColumn<Item, String> Col_Price;

    private ObservableList<Item> ItemModels = FXCollections.observableArrayList();

    public double sum = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        try{
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/posjavafx","root","");  
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from products");  
            while(rs.next()){
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3) + "  "+rs.getString(4));  
                ComboxBoxForSelection.getItems().addAll(rs.getString(2));
            }
            con.close();  
        }catch(Exception e){
            System.out.println(e);
        }
      
        
    }
    public void Add_Transaction(ActionEvent event){
       //System.out.println(Product_ID.getText());
       //System.out.println(Product_Name.getText());
       //System.out.println(Product_Price.getText());
       //Table_Transaction.getColumns().addAll(Product_ID.getText(),Product_Name.getText(),Product_Price.getText(),Product_Price.getText());
       Col_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
       Col_Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
       Col_Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
       //add your data to the table here.
       ItemModels.add(new Item(Product_ID.getText(),Product_Name.getText(),Product_Price.getText()));
       sum = 0;
        ItemModels.forEach((Item) -> { 
            System.out.println("Price is "+Item.Price);
            sum += Double.parseDouble(Item.Price);
            System.out.println(sum);
            JAVAFXSUM.setText(String.valueOf(sum));
        });
       tbData.setItems(ItemModels);
    }
    public void DeleteItem_Transaction(ActionEvent event){
        ItemModels.remove(tbData.getSelectionModel().getSelectedItem());
        sum = 0;
        ItemModels.forEach((Item) -> { 
            System.out.println("Price is "+Item.Price);
            sum += Double.parseDouble(Item.Price);
            System.out.println(sum);
            JAVAFXSUM.setText(String.valueOf(sum));
        });
        tbData.setItems(ItemModels);
        tbData.refresh();

    }
    public void Payment(ActionEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Payment");
        try{
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/posjavafx","root","");  
            Statement stmt=con.createStatement();  
            stmt.executeUpdate("INSERT INTO transaction " + "VALUES ("+(total_row+1)+",'"+Double.toString(sum)+"')");
            total_row++;
        }catch(Exception e){
            System.out.println(e);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment");
        alert.setHeaderText("Your Total is RM "+JAVAFXSUM.getText());
        alert.setContentText("Thank you for shopping with us");
        alert.showAndWait();
        ItemModels.clear();
        sum = 0;
        JAVAFXSUM.setText(String.valueOf(sum));
        tbData.setItems(ItemModels);
        tbData.refresh();
     

    }
    public void EXIT(ActionEvent event) {
        // TODO Auto-generated method stub
        System.exit(1);
    }

    public void ComboxBoxDisplay(ActionEvent event) {
        // TODO Auto-generated method stub
        System.out.println(ComboxBoxForSelection.getValue());
        try{
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/posjavafx","root","");  
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from products where Product_Name='"+ComboxBoxForSelection.getValue()+"'"); 
            while(rs.next()){
                //System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3) + "  "+rs.getString(4));  
                //ComboxBoxForSelection.getItems().addAll(rs.getString(2));
                Product_ID.setText(rs.getString(1));
                Product_Name.setText(rs.getString(2));
                Product_Price.setText(rs.getString(3));
                total_row++;
            }
            con.close();  
        }catch(Exception e){
            System.out.println(e);
        }

    }


    
}
