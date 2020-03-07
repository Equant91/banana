package com.banana.repository;

import com.banana.model.Picture;
import org.javalite.activejdbc.DB;

public class PictureRepository {
   public void getDbConnection(){
        DB open = new DB("Banana").open(
                "org.postgresql.Driver",
                "jdbc:postgresql://localhost:5432/banana",
                "postgres",
                "postgres");
        //open.openTransaction();
//       Picture picture = new Picture();
//picture.setId(1);
//picture.set("name", "name");
//       picture.saveIt();
//       open.commitTransaction();
//       open.close();
   }
}
