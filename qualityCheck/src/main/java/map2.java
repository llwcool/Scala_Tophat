import org.apache.spark.api.java.function.*;
import scala.Tuple2;

public class map2 implements PairFunction<String,String,String> {

	@Override
	public Tuple2<String, String> call(String x) throws Exception {
		String[] array=x.split("\t");
		if(array[2].equals("-"))
			return new Tuple2<String,String>(array[2]+" "+array[3].split("-")[0]+"\t",array[9]+" "+array[10]+" "+array[11]+" "+array[array.length-1]+" ");
		else return new Tuple2<String,String>(array[2]+" "+array[3].split(":")[0]+"\t",array[9]+" "+array[10]+" "+array[11]+" "+array[array.length-1]+" ");
		
	}
}
