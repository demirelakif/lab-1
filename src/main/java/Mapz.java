import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodedWaypoint;
import com.google.maps.model.PlaceDetails;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;
import com.teamdev.jxmaps.DirectionsResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

public class Mapz extends MapView {

    private static Map map;

    private CircleOptions circleOptions;

    private PolylineOptions polylineOptions;
    private com.google.maps.model.LatLng start1 = new com.google.maps.model.LatLng(41.015137,28.979530);
    //private LatLng end1 = new LatLng(4.5911405957303835,-74.05471801757812);
    private com.google.maps.model.LatLng start = new com.google.maps.model.LatLng(40.771150,30.008890);
    private LatLng end;
    private ArrayList<LatLng> latLngList;
    public LatLng destin ;
    public LatLng origin ;
    private String objectId;


    public void generateSimplePath(LatLng[] path){
        Polyline polyline = new Polyline(map);
        polyline.setPath(path);
        polyline.setOptions(polylineOptions);
    }

    public void generateCircle(LatLng center){
        Circle circle = new Circle(map);
        circle.setCenter(center);
        circle.setRadius(20);
        circle.setVisible(true);
        circle.setOptions(circleOptions);
    }

    public Mapz(ArrayList<LatLng> latLngListt, MapViewOptions viewOptions, final String objectId){
        super(viewOptions);
        this.objectId = objectId;
        latLngList = new ArrayList<>();
        latLngList.addAll(latLngListt);
        System.out.println(latLngList);
        JFrame frame = new JFrame("Path");
        Hashtable<String,Integer> vertexMap = new Hashtable<>();

        setOnMapReadyHandler(new MapReadyHandler() {
                                 @Override
                                 public void onMapReady(MapStatus status) {
                                     map = Mapz.this.getMap();
                                     MapOptions mapOptions = new MapOptions();
                                     MapTypeControlOptions mapTypeControlOptions = new MapTypeControlOptions();
                                     mapTypeControlOptions.setPosition(ControlPosition.BOTTOM_LEFT);
                                     mapOptions.setMapTypeControlOptions(mapTypeControlOptions);

                                     map.setOptions(mapOptions);
                                     map.setCenter(new LatLng(40.766666,29.916668));
                                     map.setZoom(13);





                                     map.addEventListener("click", new MapMouseEvent() {
                                         @Override
                                         public void onEvent(MouseEvent mouseEvent) {


                                             final Marker marker = new Marker(map);
                                             marker.setPosition(mouseEvent.latLng());
                                             latLngList.add(mouseEvent.latLng());

                                             ConnectionString connectionString = new ConnectionString("mongodb+srv://akif:akif@cluster0.w9eua.mongodb.net/CargoApp?retryWrites=true&w=majority");
                                             MongoClientSettings settings = MongoClientSettings.builder()
                                                     .applyConnectionString(connectionString)
                                                     .build();
                                             MongoClient mongoClient = MongoClients.create(settings);
                                             MongoDatabase db = mongoClient.getDatabase("CargoApp");
                                             MongoCollection<Document> collection = db.getCollection("Cargo");
                                             Document document = new Document("lat",mouseEvent.latLng().getLat()).append("lng",mouseEvent.latLng().getLng()).append("user",new ObjectId(objectId));
                                             collection.insertOne(document);

                                             getUpdateList();

                                             marker.addEventListener("click", new MapMouseEvent() {
                                                 @Override
                                                 public void onEvent(MouseEvent mouseEvent) {
                                                     marker.remove();
                                                 }
                                             });



                                         }
                                     });




                                 }
                             }

        );





        try {
            for (int i=0;i<5;i++){
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        frame.add(this, BorderLayout.CENTER);
        frame.setSize(1500,1000);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);






        //circle declare
        circleOptions = new CircleOptions();
        circleOptions.setStrokeColor("#FF0000");
        circleOptions.setRadius(30);
        circleOptions.setFillColor("#FF0000");
        circleOptions.setFillOpacity(0.7);

        //declare line settings
        polylineOptions = new PolylineOptions();
        polylineOptions.setGeodesic(true);
        polylineOptions.setStrokeColor("#4400CC");
        polylineOptions.setStrokeOpacity(1);
        polylineOptions.setStrokeWeight(2.0);

//        //draw the path
//        for (int i=0; i< edgeArray.size();i++){
//            double lat1 = edgeArray.get(i)[0].getLat();
//            double long1 = edgeArray.get(i)[0].getLng();
//
//            double lat2 = edgeArray.get(i)[1].getLat();
//            double long2 = edgeArray.get(i)[1].getLng();
//
//            if(!vertexMap.containsKey(lat1+"$"+long1)){
//                vertexMap.put(lat1+"$"+long1,0);
//                generateCircle(edgeArray.get(i)[0]);
//            }
//
//            generateSimplePath(edgeArray.get(i));
//
//            if(!vertexMap.containsKey(lat2+"$"+long2)){
//                vertexMap.put(lat2+"$"+long2,1);
//                generateCircle(edgeArray.get(i)[1]);
//            }
//
//
//
//        }


        getUpdateList();



    }

//    public DistanceMatrix distanceMatriks(){
//
////        com.google.maps.model.LatLng end1 = new com.google.maps.model.LatLng(40.763420,29.926400);
////        com.google.maps.model.LatLng[] starts = new com.google.maps.model.LatLng[1];
////        starts[0] = start;
////
//        com.google.maps.model.LatLng[] dests = new com.google.maps.model.LatLng[latLngList.size()];
//        int i = 0;
//        for(LatLng ltl:latLngList){
//            com.google.maps.model.LatLng temp = new com.google.maps.model.LatLng(ltl.getLat(),ltl.getLng());
//            dests[i] = temp;
//            i+=1;
//        }
////        dests[0] = end1;
////        dests[1] = start1;
////        dests[2] = new com.google.maps.model.LatLng(40.763420,29.926400);
//
//
//
//        String API_KEY = "AIzaSyDykHaI4eghR54r6_WeC1cNsbiooVeeu9g";
//        GeoApiContext.Builder geoApiContext = new GeoApiContext.Builder().apiKey(API_KEY);
//        GeoApiContext context = geoApiContext.build();
//        try {
//            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
//            DistanceMatrix trix = req.origins(new com.google.maps.model.LatLng(40.766666,29.916668))
//                    .destinations(dests)
//                    .mode(com.google.maps.model.TravelMode.DRIVING)
//                    .avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
//                    .await()
//                    ;
//            //Do something with result here
//            // ....
//            return trix;
//        } catch(ApiException e){
//            System.out.println();
//        } catch(Exception e){
//            System.out.println();
//        }
//
//            return null;
//    }


//    public com.google.maps.model.DirectionsResult get_response_google() {
//        com.google.maps.model.LatLng[] waypoints = new com.google.maps.model.LatLng[2];
//        waypoints[0] = new com.google.maps.model.LatLng(40.765,29.915);
//        waypoints[1] = new com.google.maps.model.LatLng(40.71037165944488,29.933561248779306);
//        String API_KEY = "AIzaSyDykHaI4eghR54r6_WeC1cNsbiooVeeu9g";
//        GeoApiContext.Builder geoApiContext = new GeoApiContext.Builder().apiKey(API_KEY);
//        GeoApiContext context = geoApiContext.build();
//
//        try {
//            DirectionsApiRequest directionsApiRequest = DirectionsApi.newRequest(context);
//            com.google.maps.model.DirectionsResult directionsApi = directionsApiRequest.mode(com.google.maps.model.TravelMode.DRIVING)
//                    .origin(new com.google.maps.model.LatLng(40.766666,29.916668))
//                    .waypoints(waypoints)
//                    .destination(new com.google.maps.model.LatLng(40.366666,29.316668))
//                    .avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
//                    .await();
//            return directionsApi ;
//        } catch (ApiException | InterruptedException | IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return null;
//    }

//    public void drawRoute(){
//
//        ArrayList<LatLng> pathh = new ArrayList<>();
//
//        final com.google.maps.model.DirectionsResult directionsResult = get_response_google();
//        int i = 0;
//        for (com.google.maps.model.DirectionsRoute route : directionsResult.routes) {
//            //System.out.println(route.bounds.northeast);
//            //System.out.println(route.bounds.southwest);
//            for (com.google.maps.model.DirectionsLeg leg:route.legs){
//                System.out.println(leg);
//                for(com.google.maps.model.DirectionsStep step:leg.steps){
//                    System.out.println(step.startLocation);
//                    LatLng latLng = new LatLng(step.startLocation.lat,step.startLocation.lng);
//                    LatLng latLng1 = new LatLng(step.endLocation.lat,step.endLocation.lng);
//                    pathh.add(latLng);
//                    pathh.add(latLng1);
//                }
//
//
//
//            }
//        }
//
//        Polyline polyline = new Polyline(map);
//        i = 0;
//        LatLng[] path = new LatLng[pathh.size()-1];
//        for(LatLng l:pathh){
//
//            if (i==pathh.size()-1){
//                continue;
//            }
//            path[i] = l;
//            i+=1;
//        }
//        polyline.setPath(path);
//        PolylineOptions options = new PolylineOptions();
//        options.setGeodesic(true);
//        options.setStrokeColor("#FF0000");
//        options.setStrokeOpacity(1.0);
//        options.setStrokeWeight(2.0);
//        polyline.setOptions(options);
//
//
//    }

    public void getUpdateList(){
        latLngList.clear();
        ConnectionString connectionString = new ConnectionString("mongodb+srv://akif:akif@cluster0.w9eua.mongodb.net/CargoApp?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase db = mongoClient.getDatabase("CargoApp");
        MongoCollection<Document> collection = db.getCollection("Cargo");
        BasicDBObject query = new BasicDBObject();
        query.put("user", new ObjectId(objectId));
        FindIterable<Document> results = collection.find(query);
        int i =0;
        for(Document result:results){
            String lat = result.get("lat").toString();
            String lng = result.get("lng").toString();

            LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
            latLngList.add(latLng);
            i+=1;
        }
        setMarkers();
        if (i>=1){
            calculateDirection();
        }

    }

    public void setMarkers(){
        Marker marker = new Marker(map);
        marker.setVisible(false);
        for (int j = 0;j<latLngList.size();j++){
            marker = new Marker(map);
            marker.setPosition(latLngList.get(j));

        }

    }

    public void calculateDirection() {
        DirectionsGeocodedWaypoint[] waypoints = new DirectionsGeocodedWaypoint[3];
        DirectionsGeocodedWaypoint waypoint = new DirectionsGeocodedWaypoint();


        DirectionsRequest request = new DirectionsRequest();
        request.setOrigin(new LatLng(40.777591705322266,29.923355102539062));
        request.setDestination(latLngList.get(latLngList.size()-1));



        //request.setWaypoints(waypoints);


        request.setTravelMode(TravelMode.DRIVING);
        getServices().getDirectionService().route(request, new DirectionsRouteCallback(map) {
            @Override
            public void onRoute(DirectionsResult result, DirectionsStatus status) {
                if (status == DirectionsStatus.OK) {
                    // Drawing the calculated route on the map
                    map.getDirectionsRenderer().setDirections(result);
                } else {
                    System.out.println("hata");
                }
            }


        });
    }
//    public static void main(String[] args) {
//        ArrayList<LatLng[]> test = new ArrayList<>();
//
//        MapViewOptions options = new MapViewOptions();
//        options.setApiKey("AIzaSyDykHaI4eghR54r6_WeC1cNsbiooVeeu9g");
//        new Mapz(test,options);
//    }
}
