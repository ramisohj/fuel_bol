package com.ramisohj.fuel_bol.model;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.LinkedHashMap;
import java.util.Map;


public class GeojsonPoint {

    Map<String, Object> feature = new LinkedHashMap<>();
    Map<String, Object> properties = new LinkedHashMap<>();
    Map<String, Object> geometry = new LinkedHashMap<>();

    public GeojsonPoint(){
        feature.put("type", "Feature");
    }

    public void addProperty(String property, Object value){
        properties.put(property, value);
    }

    public void addGeometry(double longitude, double latitude){
        geometry.put("type", "Point");
        geometry.put("coordinates", new double[]{longitude, latitude});
        feature.put("geometry", geometry);
    }

    public void addGeometry(Point point){
        geometry.put("type", "Point");
        geometry.put("coordinates", new double[]{point.getX(), point.getY()});
        feature.put("geometry", geometry);
    }

    public void addGeometry(String point) throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);
        Point geometryPoint = (Point) reader.read(point);
        addGeometry(geometryPoint);
    }
     public Map<String, Object> getGeojsonPoint(){
        feature.put("properties", properties);
        return feature;
     }
}
