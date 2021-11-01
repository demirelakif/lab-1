import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class changePasFrame extends JFrame{
    private JTextField userField;
    private JPanel panel1;
    private JTextField pasFieldOld;
    private JTextField pasFieldNew;
    private JButton değiştirButton;
    private String objectid;

    public changePasFrame(final String objectid){
        this.setSize(new Dimension(500,500));
        this.setResizable(false);
        final JFrame frame = this;

        this.add(panel1);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        değiştirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionString connectionString = new ConnectionString("mongodb+srv://akif:akif@cluster0.w9eua.mongodb.net/CargoApp?retryWrites=true&w=majority");
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .build();
                MongoClient mongoClient = MongoClients.create(settings);
                MongoDatabase db = mongoClient.getDatabase("CargoApp");
                MongoCollection<Document> collection = db.getCollection("User");
                Document document = new Document("username",userField.getText()).append("password",pasFieldNew.getText());

                Bson filterId = Filters.eq("_id",new ObjectId(objectid));
                Bson filter = Filters.eq("username",userField.getText());
                Bson filterpas = Filters.eq("password",pasFieldOld.getText());


                collection.updateOne(Filters.and(filterId,filter,filterpas),document);
                frame.setVisible(false);

            }
        });
    }

}
