import org.apache.spark.api.java.function.*;
import scala.Tuple2;

public class map3 implements Function2<String,String,String> {

	@Override
	public String call(String x,String y) throws Exception {
		return x+" , "+y;
	}
}
