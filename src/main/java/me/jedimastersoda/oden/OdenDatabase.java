package me.jedimastersoda.oden;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

public class OdenDatabase {

  @Getter private final File folder;
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

  public OdenDatabase(Oden oden) {
    this.folder = new File(oden.getDataFolder(), "/data");

    this.getFolder().mkdirs();
  }

  public void createSave(ArrayList<OdenCollectionData> dataArrayList, String identifier) throws IOException {
    String fileName = simpleDateFormat.format(new Date()) + "." + identifier + ".oden";
    File file = new File(folder, fileName);

    String lines = dataArrayList.stream().map(data -> data.toString()).collect(Collectors.joining("\n"));

    Files.write(file.toPath(), lines.getBytes());
  }

  public ArrayList<OdenCollectionData> loadData(String fileName) throws IOException {
    File file = new File(folder, fileName);
    List<String> lines = Files.readAllLines(file.toPath());

    ArrayList<OdenCollectionData> dataArrayList = new ArrayList<>();

    lines.forEach(line -> dataArrayList.add(OdenCollectionData.fromString(line)));

    return dataArrayList;
  }

  public ArrayList<OdenCollectionData> loadAllData() throws IOException {
    File[] dataFiles = getFolder().listFiles();
    ArrayList<OdenCollectionData> data = new ArrayList<>();

    for(File dataFile : dataFiles) {
      ArrayList<OdenCollectionData> dataArrayList = this.loadData(dataFile.getName());
      data.addAll(dataArrayList);
    }

    return data;
  }
}