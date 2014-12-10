package weka.test.classification;

import java.io.File;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class NBClassifier {

	public static void main(String[] args) throws Exception {
		
		long start = System.currentTimeMillis();
		System.out.println("Start to train model!");
		//train a new classifier model
		File file = new File("D://weka_user.arff");
		ArffLoader loader = new ArffLoader();
		
		loader.setFile(file);
		Instances train_ins = loader.getDataSet();
		train_ins.setClassIndex(2);
		long end = System.currentTimeMillis();
		System.out.println("File loading complete! Time:"+(end-start)/1000);

		start = end;
		
		Classifier cfs = (Classifier) Class.forName("weka.classifiers.bayes.NaiveBayes").newInstance();
//		Classifier cfs = (Classifier) Class.forName("weka.classifiers.functions.Logistic").newInstance();
//		Classifier cfs = (Classifier) Class.forName("weka.classifiers.functions.LibSVM").newInstance();
//		Classifier cfs = (Classifier) Class.forName("weka.classifiers.meta.AdaBoostM1").newInstance();
//		Classifier cfs = (Classifier) Class.forName("weka.classifiers.trees.RandomForest").newInstance();
		cfs.buildClassifier(train_ins);
		
		end = System.currentTimeMillis();
		System.out.println("Model has been trained completed! Time:"+(end-start)/1000);

		//create a test instance from String value
		String line = "1,5,0";

		Instance pre_inst = new Instance(line.split(",").length);
		for(int i=0; i<line.split(",").length; i++){
			System.out.println(i+":"+train_ins.attribute(i));
			pre_inst.setValue(train_ins.attribute(i), 
					line.split(",")[i]);
			
			
		}
		pre_inst.setDataset(train_ins);
		
		pre_inst.setClassValue(1);
		
		Instance testInst;
		Evaluation testingEvaluation = new Evaluation(train_ins);
		int length = train_ins.numInstances();
		for(int i=0; i<length; i++){
			testInst = train_ins.instance(i);
			System.out.println("pre:"+cfs.classifyInstance(testInst)+"fact:"+testInst.classValue());
			testingEvaluation.evaluateModelOnceAndRecordPrediction(cfs, testInst);
		}
		System.out.println("分类正确率："+(1-testingEvaluation.errorRate()));
		/*System.out.println(pre_inst.numAttributes());
		System.out.println(pre_inst.classIndex());
		System.out.println(pre_inst);*/
		
		
		System.out.println(cfs.classifyInstance(pre_inst));
		
	}

}
