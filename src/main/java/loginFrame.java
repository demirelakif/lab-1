import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.MapViewOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.print.Doc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class loginFrame extends JFrame{
    private JPanel panel1;
    private JTextField userField;
    private JTextField pasField;
    private JButton girisButton;
    private JButton kayıtOlButton;
    private String username;
    private String password;
    private JFrame frame;


    public loginFrame(){
        frame = this;
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(400,300));
        this.setSize(400,300);
        this.setResizable(false);

        this.add(panel1);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        girisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = userField.getText();
                password = pasField.getText();

                ConnectionString connectionString = new ConnectionString("mongodb+srv://akif:akif@cluster0.w9eua.mongodb.net/CargoApp?retryWrites=true&w=majority");
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .build();
                MongoClient mongoClient = MongoClients.create(settings);
                MongoDatabase db = mongoClient.getDatabase("CargoApp");
                MongoCollection <Document> collection = db.getCollection("User");
                Document thisUser = new Document("username",username).append("password",password);
                FindIterable <Document> results = collection.find(thisUser);

                for(Document result:results){
                    if(!result.isEmpty()){

                        ArrayList<LatLng[]> test = new ArrayList<>();
                        LatLng start1 = new LatLng(4.7368340255485915,-74.04613494873047);
                        LatLng end1 = new LatLng(4.5911405957303835,-74.05471801757812);
                        LatLng end2 = new LatLng(40.762402,29.932949);
                        LatLng [] line1 = {start1,end1};
                        LatLng [] line2 = {end1,end2};


                        test.add(line1);
                        test.add(line2);
                        MapViewOptions options = new MapViewOptions();
                        options.setApiKey("AIzaSyDykHaI4eghR54r6_WeC1cNsbiooVeeu9g");

                        String objectId = result.get("_id").toString();

                        //new Mapz(test,options);
                        frame.setVisible(false);
                        new cargoFrame(username,objectId);
                    }else {
                        System.out.println("asd");
                        JOptionPane.showMessageDialog(frame, "Yanlış değer girişi");
                    }
                }


            }
        });

        kayıtOlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new registerFrame();
            }
        });
    }



}
