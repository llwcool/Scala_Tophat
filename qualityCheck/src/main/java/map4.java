import org.apache.spark.api.java.function.*;
import scala.Tuple2;

public class map4 implements PairFunction<String,String,String> {

	@Override
	public Tuple2<String, String> call(String x) throws Exception {
		return new Tuple2<String,String>(x," ");
		
	}
}
