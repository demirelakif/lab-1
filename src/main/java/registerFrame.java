import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registerFrame extends JFrame {
    private JTextField textField1;
    private JTextField pasField;
    private JButton kayıtOlButton;
    private JLabel userField;
    private JPanel panel1;
    private String username, password;
    private JFrame frame;


    public registerFrame() {
        frame = this;
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(400, 300));
        this.setResizable(false);

        this.add(panel1);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        kayıtOlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = textField1.getText();
                password = pasField.getText();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Lütfen geçerli bir değer giriniz");
                } else {
                    ConnectionString connectionString = new ConnectionString("mongodb+srv://akif:akif@cluster0.w9eua.mongodb.net/CargoApp?retryWrites=true&w=majority");
                    MongoClientSettings settings = MongoClientSettings.builder()
                            .applyConnectionString(connectionString)
                            .build();
                    MongoClient mongoClient = MongoClients.create(settings);
                    MongoDatabase db = mongoClient.getDatabase("CargoApp");
                    MongoCollection<Document> collection = db.getCollection("User");
                    Document thisUser = new Document("username", username).append("password", password);
                    collection.insertOne(thisUser);
                    registerFrame.this.setVisible(false);
                }
            }
        });

    }
}
