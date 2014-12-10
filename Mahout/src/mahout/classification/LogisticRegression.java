package mahout.classification;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.mahout.classifier.sgd.CsvRecordFactory;
import org.apache.mahout.classifier.sgd.LogisticModelParameters;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

public class LogisticRegression {

	private static LogisticModelParameters lmp;
	private static PrintWriter output;

	public static void main(String[] args) throws IOException {
		// 1: new
		lmp = new LogisticModelParameters();
		output = new PrintWriter(new OutputStreamWriter(System.out,
				Charsets.UTF_8), true);

		// 2: init params
		lmp.setLambda(0.001);
		lmp.setLearningRate(50);
		lmp.setMaxTargetCategories(3);
		lmp.setNumFeatures(8);
		List<String> targetCategories = Lists.newArrayList("Iris-setosa", "Iris-versicolor", "Iris-virginica");
		lmp.setTargetCategories(targetCategories);
		lmp.setTargetVariable("class"); // 需要进行预测的是class属性
		List<String> typeList = Lists.newArrayList("numeric", "numeric", "numeric", "numeric",
				"numeric", "numeric", "numeric", "numeric");
		List<String> predictorList = Lists.newArrayList("sepallength", "sepalwidth", "petallength", "petalwidth",
				"sepallength2", "sepalwidth2", "petallength2", "petalwidth2");
		lmp.setTypeMap(predictorList, typeList);

		// 3. load data
		List<String> raw = FileUtils.readLines(new File(
				"D:\\iris\\iris.csv")); //使用common-io进行文件读取
		String header = raw.get(0);
		System.out.println("header:"+header);
		List<String> content = raw.subList(1, raw.size());
		/*for(String str : raw){
			System.out.println(str);
		}*/
		// parse data
		CsvRecordFactory csv = lmp.getCsvRecordFactory();
		csv.firstLine(header); // !!!Note: this is a initialize step, do not
								// skip this step

		// 4. begin to train
		OnlineLogisticRegression lr = lmp.createRegression();
		for(int i = 0; i < 100; i++) {
			for (String line : content) {
				Vector input = new RandomAccessSparseVector(lmp.getNumFeatures());
				int targetValue = csv.processLine(line, input);
				System.out.println("targetValue:"+targetValue);
				lr.train(targetValue, input);
			}
		}

		// 5. show model performance: show classify score
		double correctRate = 0;
		double sampleCount = content.size();
		
		for (String line : content) {
			Vector v = new SequentialAccessSparseVector(lmp.getNumFeatures());
			int target = csv.processLine(line, v);
			int score = lr.classifyFull(v).maxValueIndex();  // 分类核心语句!!!
			System.out.println("Target:" + target + "\tReal:" + score);
			if(score == target) {
				correctRate++;
			}
		}
		output.printf(Locale.ENGLISH, "Rate = %.2f%n", correctRate / sampleCount);
	}

}
