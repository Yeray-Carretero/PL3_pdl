package gramaticas;

import java.util.ArrayList;
import java.util.HashMap;

public class Function {
	
	private String variableRetorno;
	private String nombre;
	private ArrayList<String> argumentos;
	private HashMap<String,String> variables;
	private ArrayList<Tripleta> bloqueCodigo;
	//private HashMap<String,HashMap<String,String>> bloqueCodigo;
	
	/**
	 * Constructor principal
	 * @param nombre
	 */
	public Function(String nombre){
		this.nombre = nombre;
		this.variables = new HashMap<String, String>();
		bloqueCodigo = new ArrayList<Tripleta>();
		
		
	}
	/**
	 * Constructor auxiliar donde la funcion no consta ni de parametros ni de retorno
	 * @param nombre
	public Function(String nombre)
	{
		this("", nombre, null);
	}*/
	
	/**
	 * 
	 * @return
	 */
	public String getVariableRetorno()
	{
		return variableRetorno;
	}
	/**
	 * 
	 * @return
	 */
	public String getNombre()
	{
		return nombre;
	}
	/**
	 * 
	 * @return
	 */
	public int getArgumentosSize()
	{
		return argumentos.size();
	}
	/**
	 * 
	 * @param indice
	 * @return
	 */
	public String getArgumento(int indice)
	{
		return argumentos.get(indice);
	}
	/**
	 * Metodo para a�adir una nueva variable que ha aparecido en la lectura 
	 * @param variable
	 */
	public void addVariable(String variable, String valor)
	{
		variables.put(variable, valor);
	}
	/**
	 * 
	 * @return
	 */
	public int getVariablesSize()
	{
		return variables.size();
	}

	public String getVariables(String clave) {
		return variables.get(clave);
	}

	/**
	 * 
	 * @param linea
	 */
	public void addLineaCod(String linea) {
		String accion = "";
		String parametro1 ="";
		String parametro2 ="";

		if (linea.contains("print")){
			accion = "print";
			parametro1 = linea.substring(6,linea.indexOf(')'));
			bloqueCodigo.add(new Tripleta(accion,parametro1));
		} else if (linea.contains("while")) {
			accion = "while";
			parametro1 = linea.substring(6,linea.indexOf(')'));
			parametro2 = String.valueOf(bloqueCodigo.size()-1);
			bloqueCodigo.add(new Tripleta(accion,parametro1,parametro2));
		} else if (linea.contains("exit")) {
			accion = "exit";
			bloqueCodigo.add(new Tripleta(accion));
		} else if (linea.contains("if")) {
			accion = "if";
			parametro1 = linea.substring(3, linea.indexOf(')'));
			bloqueCodigo.add(new Tripleta(accion, parametro1));
		}else if(linea.contains("return")){
			accion = "return";
			parametro1 = linea.substring(6);
			bloqueCodigo.add(new Tripleta(accion, parametro1));
		} else if (linea.contains("=")) {
			accion = "asignar";
			parametro1 = linea.substring(0,linea.indexOf('='));
			if(linea.contains("+")){
				parametro2 = "operacion";
				bloqueCodigo.add(new Tripleta(accion,parametro1, parametro2));

				bloqueCodigo.add(new Tripleta("+",linea.substring(0,linea.indexOf('+')),linea.substring(linea.indexOf('+')+1)));

			}else if(linea.contains("-")){
				parametro2 = "operacion";
				bloqueCodigo.add(new Tripleta(accion,parametro1, parametro2));

				bloqueCodigo.add(new Tripleta("-",linea.substring(0,linea.indexOf('-')),linea.substring(linea.indexOf('-')+1)));
			}else if(linea.contains("*")){
				parametro2 = "operacion";
				bloqueCodigo.add(new Tripleta(accion,parametro1, parametro2));

				bloqueCodigo.add(new Tripleta("*",linea.substring(0,linea.indexOf('*')),linea.substring(linea.indexOf('*')+1)));
			}else if(linea.contains("/")){
				parametro2 = "operacion";
				bloqueCodigo.add(new Tripleta(accion,parametro1, parametro2));

				bloqueCodigo.add(new Tripleta("/",linea.substring(0,linea.indexOf('/')),linea.substring(linea.indexOf('/')+1)));
			}else if(linea.contains("%")){
				parametro2 = "operacion";
				bloqueCodigo.add(new Tripleta(accion,parametro1, parametro2));

				bloqueCodigo.add(new Tripleta("%",linea.substring(0,linea.indexOf('%')),linea.substring(linea.indexOf('&')+1)));
			}else if(linea.contains("^")){
				parametro2 = "operacion";
				bloqueCodigo.add(new Tripleta(accion,parametro1, parametro2));

				bloqueCodigo.add(new Tripleta("^",linea.substring(0,linea.indexOf('^')),linea.substring(linea.indexOf('^')+1)));
			} else{
				parametro2 = linea.substring(linea.indexOf("="+1,linea.length()));
				bloqueCodigo.add(new Tripleta(accion,parametro1, parametro2));
			}
		}else{
			accion = "llamada";
			parametro1 = linea.substring(0,linea.indexOf('('));
			bloqueCodigo.add(new Tripleta(accion,parametro1));
		}
	}
	public boolean variableExists(String variable){
		if(variables.containsKey(variable)){
			return true;
		}else {
			return false;
		}
	}
	public void updateVariable(String variable, String valor){
		variables.replace(variable,valor);
	}
	public int getTotalLineasCod(){
		return this.bloqueCodigo.size();
	}

	public ArrayList<Tripleta> getBloqueCodigo() {
		return bloqueCodigo;
	}
}
