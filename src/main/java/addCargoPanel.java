import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class addCargoPanel extends JFrame{
    private JButton ekleButton;
    private JTextField latField;
    private JTextField lngField;
    private JPanel panel1;
    private JFrame frame;

    addCargoPanel(final String objectId, final JFrame frame1){
        this.setVisible(true);
        this.add(panel1);
        this.setSize(new Dimension(300,300));
        this.setPreferredSize(new Dimension(300,300));
        this.setLocationRelativeTo(null);
        frame = this;

        ekleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionString connectionString = new ConnectionString("mongodb+srv://akif:akif@cluster0.w9eua.mongodb.net/CargoApp?retryWrites=true&w=majority");
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .build();
                MongoClient mongoClient = MongoClients.create(settings);
                MongoDatabase db = mongoClient.getDatabase("CargoApp");
                MongoCollection<Document> collection = db.getCollection("Cargo");
                Document document = new Document("lat",Double.parseDouble(latField.getText())).append("lng",Double.parseDouble(lngField.getText())).append("user",new ObjectId(objectId));
                collection.insertOne(document);
                frame.setVisible(false);
            }
        });
    }
}
