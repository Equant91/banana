package com.banana.repository;

import com.banana.model.LabelAnnotation;
import com.banana.model.Picture;
import com.google.inject.Singleton;
import org.javalite.activejdbc.Base;
@Singleton
public class PictureRepository {
    public void getDbConnection() {
//        DB open = new DB("Banana").open(
//                "org.postgresql.Driver",
//                "jdbc:postgresql://localhost:5432/banana",
//                "postgres",
//                "example");
//        Base.open("org.postgresql.Driver",
//                "jdbc:postgresql://localhost:5432/banana",
//                "postgres",
//                "example");
//       Base.open();
//        Base.openTransaction();
        Picture picture = new Picture();
        //      picture.insert();
        LabelAnnotation label = new LabelAnnotation();
        label.setName("qweqweqwe");
        label.setScore(123123.0112312312312323);
        picture.saveIt();
        picture.set("url", "eqweqweqweqweqweqweqweqweqewq");
        picture.addLabel(label);
        picture.saveIt();
        picture.saveIt();
        //open.openTransaction();
//       Picture picture = new Picture();
//picture.setId(1);
//picture.set("name", "name");
//       picture.saveIt();
//       open.commitTransaction();
//       open.close();
    }
}
