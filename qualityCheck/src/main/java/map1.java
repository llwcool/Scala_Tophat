import org.apache.spark.api.java.function.*;
import scala.Tuple2;
import java.lang.String.*;

public class map1 implements Function<String,Boolean> {

	@Override
	public Boolean call(String x) throws Exception {
		String[] array=x.split("\t");
		return array[array.length-1].contains("yes");
	}
}

