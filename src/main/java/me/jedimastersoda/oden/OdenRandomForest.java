package me.jedimastersoda.oden;

import java.util.ArrayList;
import java.util.Random;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.tree.RandomForest;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class OdenRandomForest {

  private Dataset dataset;
  private Classifier randomForest;
  
  public OdenRandomForest(ArrayList<OdenCollectionData> dataArrayList) {
    this.createDataset(dataArrayList);
    this.createClassifier();
  }

  private void createDataset(ArrayList<OdenCollectionData> dataArrayList) {
    this.dataset = new DefaultDataset();

    dataArrayList.forEach(data -> {
      DenseInstance instance = new DenseInstance(data.conv(), data.isCheaterData());
      dataset.add(instance);
    });
  }

  private void createClassifier() {
    this.randomForest = new RandomForest(50, false, 4, new Random()); // TODO: Change these number thingies
    this.randomForest.buildClassifier(this.dataset);
  }

  public boolean test(OdenCollectionData data) {
    DenseInstance instance = new DenseInstance(data.conv());

    return (boolean) this.randomForest.classify(instance);
  }
}