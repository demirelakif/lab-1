import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.MapViewOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class cargoFrame extends JFrame{
    private JList latlngList;
    private JButton bitirButton;
    private JButton ekleButton;
    private JButton şifreDeğiştirButton;
    private JPanel panel1;
    private JLabel username_field;
    private JFrame frame;
    public String objectId;
    public List<String> cargoList;
    public Mapz mapz;
    public MongoDatabase db;

    public cargoFrame(String username, final String objectId) {
        this.objectId = objectId;
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(new Dimension(700,700));

        final Vector<String> cargoList = new Vector<>();
        this.cargoList = cargoList;

        this.add(panel1);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        frame = this;
        final MapViewOptions options = new MapViewOptions();
        options.setApiKey("AIzaSyDykHaI4eghR54r6_WeC1cNsbiooVeeu9g");
        final ArrayList<LatLng> latLngArrayList = new ArrayList<>();

        ConnectionString connectionString = new ConnectionString("mongodb+srv://akif:akif@cluster0.w9eua.mongodb.net/CargoApp?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        final MongoClient mongoClient = MongoClients.create(settings);


        final Thread mapThread = new Thread(){
            public void run(){
                db = mongoClient.getDatabase("CargoApp");
                MongoCollection<Document> collection = db.getCollection("Cargo");
                BasicDBObject query = new BasicDBObject();
                query.put("user", new ObjectId(objectId));
                FindIterable<Document> results = collection.find(query);
                for(Document result:results){
                    String lat = result.get("lat").toString();
                    String lng = result.get("lng").toString();

                    LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                    latLngArrayList.add(latLng);
                }
//                LatLng[] latLngs = new LatLng[latLngArrayList.size()];
//                int i = 0;
//                for (LatLng latLng:latLngArrayList){
//                    latLngs[i] = latLng;
//                    i+=1;
//                }
                System.out.println(latLngArrayList);
                mapz = new Mapz(latLngArrayList,options,objectId);
            }
        };
        mapThread.start();

        username_field.setText("Hoşgeldin "+username);
        setCargoList();


        şifreDeğiştirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new changePasFrame(objectId);
            }
        });
        ekleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new addCargoPanel(objectId,frame);
                setCargoList();
                mapz.getUpdateList();
                mapz.calculateDirection();


            }
        });
        bitirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index =latlngList.getSelectedIndex();

                MongoDatabase db = mongoClient.getDatabase("CargoApp");
                MongoCollection<Document> collection = db.getCollection("Cargo");
                BasicDBObject query = new BasicDBObject();
                query.put("user", new ObjectId(objectId));
                FindIterable<Document> results = collection.find(query);
                int i = 0 ;
                for(Document result:results){
                    if (index==i){
                        BasicDBObject query1 = new BasicDBObject();
                        Object obId = result.get("_id");
                        query1.put("_id", obId);
                        collection.deleteOne(query1);
                        setCargoList();
                        mapz.getUpdateList();



                    }
                    i+=1;
                }

            }
        });




    }

    public void setCargoList(){
        cargoList.clear();
        ConnectionString connectionString = new ConnectionString("mongodb+srv://akif:akif@cluster0.w9eua.mongodb.net/CargoApp?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        final MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase db = mongoClient.getDatabase("CargoApp");
        MongoCollection<Document> collection = db.getCollection("Cargo");
        BasicDBObject query = new BasicDBObject();
        query.put("user", new ObjectId(objectId));
        FindIterable<Document> results = collection.find(query);
        for(Document result:results){
            String lat = result.get("lat").toString();
            String lng = result.get("lng").toString();
            String listV = "lat = "+lat+", lng="+lng;
            cargoList.add(listV);
        }

        latlngList.setListData((Vector) cargoList);
        frame.repaint();

    }

}
