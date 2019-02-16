package morethanhidden.restrictedportals;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class AdvancementHelper {

    //Add Advancement via DataPack
    public static void AddCustomAdvancement(String title, String description, String item, String shortname, String path) {

        //Advancement Json
        JsonObject jitem = new JsonObject();
        jitem.add("item", new JsonPrimitive(item));

        JsonObject display = new JsonObject();
        display.add("icon", jitem);
        display.add("title",  new JsonPrimitive(title));
        display.add("description",  new JsonPrimitive(description));

        JsonArray itemarray = new JsonArray();
        itemarray.add(jitem);

        JsonObject items = new JsonObject();
        items.add("items", itemarray);

        JsonObject crit1 = new JsonObject();
        crit1.add("trigger", new JsonPrimitive("minecraft:inventory_changed"));
        crit1.add("conditions", items);

        JsonObject criteria = new JsonObject();
        criteria.add(shortname, crit1);

        JsonObject jo = new JsonObject();
        jo.add("display", display);
        jo.add("parent", new JsonPrimitive("restrictedportals:root"));
        jo.add("criteria", criteria);

        //Create Json Advancement File
        File file = new File(path + "/restrictedportals/data/restrictedportals/advancements/" + shortname + ".json");
        try {
            if(file.createNewFile()){
                RestrictedPortals.LOGGER.info("Created Json File: " + file.getPath());
            }else{
                RestrictedPortals.LOGGER.info("File already Exists: " + file.getPath());
            }
        } catch (IOException e) {
            RestrictedPortals.LOGGER.info("Creating file failed: " + file.getPath());
        }

        try {
            FileWriter fileWriter = new FileWriter(file.getPath());
            fileWriter.write(jo.toString());
            fileWriter.close();

        } catch (IOException e) {
            RestrictedPortals.LOGGER.info("Editing File Failed: " + file.getPath());
        }

    }

    public static void ClearCustomAdvancements(String path){
        try {
            FileUtils.cleanDirectory(new File(path + "/restrictedportals/data/restrictedportals/advancements"));
        } catch (Exception e) {
            RestrictedPortals.LOGGER.info("Failed to clean Advancements Folder");
        }
    }

    public static void CreateDatapack(String path){

        //Make DataPack Folders
        File folder = new File(path + "/restrictedportals");
        if(folder.mkdir()){
            RestrictedPortals.LOGGER.info("Created DataPack Folder: " + folder.getPath());
        }else{
            RestrictedPortals.LOGGER.info("DataPack Already Exists or Failed: " + folder.getPath());
        }

        //Pack MCMeta(Json)
        JsonObject pack = new JsonObject();
        pack.add("description",  new JsonPrimitive("restrictedportals resources"));
        pack.add("pack_format",  new JsonPrimitive(4));

        JsonObject mcmeta = new JsonObject();
        mcmeta.add("pack", pack);

        File file = new File(path + "/restrictedportals/pack.mcmeta");
        try {
            if(file.createNewFile()){
                RestrictedPortals.LOGGER.info("Created Data Pack mcmeta: " + file.getPath());
            }else{
                RestrictedPortals.LOGGER.info("File already Exists: " + file.getPath());
            }
        } catch (IOException e) {
            RestrictedPortals.LOGGER.info("Creating file failed: " + file.getPath());
        }

        try {
            FileWriter fileWriter = new FileWriter(file.getPath());
            fileWriter.write(mcmeta.toString());
            fileWriter.close();

        } catch (IOException e) {
            RestrictedPortals.LOGGER.info("Editing File Failed: " + file.getPath());
        }


        folder = new File(path + "/restrictedportals/data");
        if(folder.mkdir()){
            RestrictedPortals.LOGGER.info("Created DataPack Folder: " + folder.getPath());
        }else{
            RestrictedPortals.LOGGER.info("DataPack Already Exists or Failed: " + folder.getPath());
        }

        folder = new File(path + "/restrictedportals/data/restrictedportals");
        if(folder.mkdir()){
            RestrictedPortals.LOGGER.info("Created DataPack Folder: " + folder.getPath());
        }else{
            RestrictedPortals.LOGGER.info("DataPack Already Exists or Failed: " + folder.getPath());
        }

        folder = new File(path + "/restrictedportals/data/restrictedportals/advancements");
        if(folder.mkdir()){
            RestrictedPortals.LOGGER.info("Created DataPack Folder: " + folder.getPath());
        }else{
            RestrictedPortals.LOGGER.info("DataPack Already Exists or Failed: " + folder.getPath());
        }

    }

    public static String[] GetCustomAdvancementList(String path) {

        File file = new File(path);

        return file.list();
    }

}
