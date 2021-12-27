import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {
  
    @FXML
    private Button Login;

    @FXML
    private TextField UserName,Password;

    public boolean login = false;
    public static String userName;
    
    @FXML
    public void Login(ActionEvent event) {  
        System.out.println(UserName.getText() + " " + Password.getText());
        try{
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/posjavafx","root","");  
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from users");  
            while(rs.next()){
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3) + "  "+rs.getString(4));  
                if(rs.getString(3).equals(UserName.getText()) && rs.getString(4).equals(Password.getText())){
                    System.out.println("Login Successful");
                    login = true;
                    userName = rs.getString(2);

                }
            }
            if(login == false){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText("Login Error");
                alert.setContentText("Your username or password is incorrect");
                alert.showAndWait();
            } else{
                Parent root = FXMLLoader.load(getClass().getResource("FXML/POS_SYSTEM.fxml"));
                Stage StockManagementWindows = (Stage)Login.getScene().getWindow();
                StockManagementWindows.setScene(new Scene(root,800,800));
            }
            con.close();  
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
