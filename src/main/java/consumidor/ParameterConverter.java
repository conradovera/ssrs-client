package consumidor;

import java.util.Map;

import wsdl.ReportExecutionServiceStub.ArrayOfParameterValue;
import wsdl.ReportExecutionServiceStub.ParameterValue;

public class ParameterConverter {
	
	public static ArrayOfParameterValue convertirParametros(Map<?, ?> parametros){
		ArrayOfParameterValue parametrosReporte = new ArrayOfParameterValue();
		
		if(parametros==null||parametros.isEmpty()){
			return parametrosReporte;
		}
		
		for(Object llave : parametros.keySet()){
			String valor = String.valueOf(parametros.get(llave));
			ParameterValue parametroReporte = new ParameterValue();
			
			parametroReporte.setName(String.valueOf(llave));
			
			parametroReporte.setValue(valor);
			
			parametrosReporte.addParameterValue(parametroReporte);
		}
		
		return parametrosReporte;
	}
}
