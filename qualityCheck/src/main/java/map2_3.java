import org.apache.spark.api.java.function.*;
import scala.Tuple2;

public class map2_3 implements PairFunction<String,String,String> {

	@Override
	public Tuple2<String, String> call(String x) throws Exception {
		int i=0;		
		String stra = " ";
		String strb = " ";		
		try{
			String[] array=x.split("\t");
			if(array[2].equals("-")){
				stra = array[2]+" "+array[3].split("-")[0]+"\t";
				strb = "serial "+array[6]+" "+array[9]+" "+array[10]+" "+array[11]+" "+array[array.length-1]+" ";
			}
			else {
				stra = array[2]+" "+array[3].split(":")[0]+"\t";
				strb = "serial "+array[6]+" "+array[9]+" "+array[10]+" "+array[11]+" "+array[array.length-1]+" ";
			}
		} catch(ArrayIndexOutOfBoundsException e){
			i=1;}
	
		if(i == 0)
			return new Tuple2<String,String>(stra,strb);
		else return new Tuple2<String,String>(" "," ");
	}
}
