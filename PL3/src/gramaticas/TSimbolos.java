package gramaticas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TSimbolos {
	
	private Function main;
	private HashMap<String,Function> funciones;

	private ArrayList<Integer> pilaWhile = new ArrayList<>();
	private Function funcionTemp;

	private String retorno = "";
	private String parametro1;


	/**
	 * Cosntructor estandar
	 */
	public TSimbolos()
	{
		this.main = null;
		this.funciones = new HashMap<>();
		this.funcionTemp = null;
		
		
	}
	/**
	 * Funcion para inicializar el programa
	 */
	public void setPrograma(Function main)
	{
		this.main = main;
	}
	
	/**
	 * A�adir la funcion a la estructura donde guardaremos todas las funciones del fichero
	 * @param funcion
	 */
	public void addFunction(Function funcion)
	{
		String nombre = funcion.getNombre();
		funciones.put(nombre,funcion);
	}
	/**
	 * Funcion para definir el main del fichero en cuestion
	 * @param main
	 */
	public void setMain(Function main)
	{
		this.main = main;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getFuncionesSize()
	{
		return funciones.size();
	}
	/**
	 *
	 * @return
	 */
	public Function getFuncion(String nombre)
	{
		return funciones.get(nombre);
	}
	/**
	 * 
	 * @param funcion
	 */
	public void setFuncionActual(Function funcion)
	{
		funcionTemp = funcion;
	}
	public Function getFuncionActual() {
		return funcionTemp;
	}
	/**
	 * 
	 * @param variable
	 */
	public void addVariable(String variable,String valor)
	{
		if (funcionTemp != null)
		{
			funcionTemp.addVariable(variable,valor);
		}
		else
		{
			main.addVariable(variable,valor);
		}
	}
	
	/**
	 * Inicializo variable sin valor asignado
	 * @param variable
	 */
	public void addVariable(String variable)
	{
		if (funcionTemp != null)
		{
			funcionTemp.addVariable(variable,"");
		}
		else
		{
			main.addVariable(variable,"");
		}
	}
	public void ejecutar(Function funcion){
		EvalBool evalBool = new EvalBool();
		boolean saleCond = true;
		boolean asignacionCompuesta =false;
		String variableAsignCompuesta = "";
		for (int i = 0; i<funcion.getBloqueCodigo().size();i++){
			Tripleta linea = funcion.getBloqueCodigo().get(i);
			if(!saleCond){
				if(linea.getAccion().equals("exit")){
					saleCond = true;
				}
			}else {
				if (linea.getAccion().equals("print")) {
					String valor = linea.getParametro1();
					if(valor.contains("val")){
						valor = valor.replaceAll(String.valueOf('('),"").replaceAll(String.valueOf(')')
								,"").replaceAll("\'","").replaceAll("val","");
						String[] valores = valor.split(",");
						Polinomio pol = new Polinomio(valores[0]);
						ArrayList<String> variables = new ArrayList<String>();
						variables.add(valores[1]);
						ArrayList<Integer> sustitutos = new ArrayList<Integer>();
						sustitutos.add(Integer.parseInt(valores[1]));
						valor = pol.evaluar( variables, sustitutos).toString();
					} else if (valor.contains("\"")) {
						valor = valor.replaceAll("\"", "");
					} else if (valor.contains("\'")) {
						valor = valor.replaceAll("\'", "");
					} else if (valor.matches("^[a-z]*$")) {
						if(funciones.containsKey(valor.replaceAll(String.valueOf('('),"").replaceAll(String.valueOf(')'),""))){
							ejecutar(funciones.get(valor.replaceAll(String.valueOf('('),"").replaceAll(String.valueOf(')'),"")));
							valor = retorno;
						}
						valor = funcion.getVariables(valor);
					}
					System.out.println(valor);
				} else if (linea.getAccion().equals("asignar")) { // a = 3; a = 3 +5; a = 3-5
					String nombreVar = linea.getParametro1(); //nombre de la variable
					String valor = linea.getParametro2(); //el otro lado del igual

					if (!valor.equals("operacion")) {
						//asignacion directa
						if (!funcion.variableExists(nombreVar)) {
							funcion.addVariable(nombreVar, valor); //añade la variable
						} else {
							funcion.updateVariable(nombreVar, valor);
						}
						//modificar su valor
					} else if (valor.equals("operacion")) {    // a = b + 3; se le asigna el resultado de una operacion
						asignacionCompuesta = true;
						variableAsignCompuesta = nombreVar;
					}

				} else if (linea.getAccion().equals("+")) {     // +, 3, 5; +, 3.0, 5   -
					String operando1 = linea.getParametro1();
					if (operando1.matches("^[a-z]*$")) {
						operando1 = funcion.getVariables(operando1);
					}

					String operando2 = linea.getParametro2();
					if (operando2.matches("^[a-z]*$")) {
						operando2 = funcion.getVariables(operando2);
					}
					float rdo = 0;
					if (!((operando1.contains(".")) && (operando2.contains(".")))) {    //si los 2 son enteros -> rdo es entero
						rdo = Integer.parseInt(operando1) + Integer.parseInt(operando2);
					} else { //alguno no es entero -> rdo float
						rdo = Float.parseFloat(operando1) + Float.parseFloat(operando2);
					}
					if (asignacionCompuesta) {
						if (!funcion.variableExists(variableAsignCompuesta)) {
							funcion.addVariable(variableAsignCompuesta, String.valueOf(rdo));
						} else {
							funcion.updateVariable(variableAsignCompuesta, String.valueOf(rdo));
						}
					}
				} else if (linea.getAccion().equals("-")) {
					String operando1 = linea.getParametro1();
					if (operando1.matches("^[a-z]*$")) {
						operando1 = funcion.getVariables(operando1);
					}

					String operando2 = linea.getParametro2();
					if (operando2.matches("^[a-z]*$")) {
						operando2 = funcion.getVariables(operando2);
					}
					float rdo = 0;
					if (!((operando1.contains(".")) && (operando2.contains(".")))) {    //si los 2 son enteros -> rdo es entero
						rdo = Integer.parseInt(operando1) - Integer.parseInt(operando2);
					} else { //alguno no es entero -> rdo float
						rdo = Float.parseFloat(operando1) - Float.parseFloat(operando2);
					}
					if (asignacionCompuesta) {
						if (!funcion.variableExists(variableAsignCompuesta)) {
							funcion.addVariable(variableAsignCompuesta, String.valueOf(rdo));
						} else {
							funcion.updateVariable(variableAsignCompuesta, String.valueOf(rdo));
						}
					}
				} else if (linea.getAccion().equals("*")) {
					String operando1 = linea.getParametro1();
					if (operando1.matches("^[a-z]*$")) {
						operando1 = funcion.getVariables(operando1);
					}

					String operando2 = linea.getParametro2();
					if (operando2.matches("^[a-z]*$")) {
						operando2 = funcion.getVariables(operando2);
					}
					float rdo = 0;
					if (!((operando1.contains(".")) && (operando2.contains(".")))) {    //si los 2 son enteros -> rdo es entero
						rdo = Integer.parseInt(operando1) * Integer.parseInt(operando2);
					} else { //alguno no es entero -> rdo float
						rdo = Float.parseFloat(operando1) * Float.parseFloat(operando2);
					}
					if (asignacionCompuesta) {
						if (!funcion.variableExists(variableAsignCompuesta)) {
							funcion.addVariable(variableAsignCompuesta, String.valueOf(rdo));
						} else {
							funcion.updateVariable(variableAsignCompuesta, String.valueOf(rdo));
						}
					}
				} else if (linea.getAccion().equals("/")) {
					String operando1 = linea.getParametro1();
					if (operando1.matches("^[a-z]*$")) {
						operando1 = funcion.getVariables(operando1);
					}

					String operando2 = linea.getParametro2();
					if (operando2.matches("^[a-z]*$")) {
						operando2 = funcion.getVariables(operando2);
					}

					float rdo = Float.parseFloat(operando1) / Float.parseFloat(operando2);

					if (asignacionCompuesta) {
						if (!funcion.variableExists(variableAsignCompuesta)) {
							funcion.addVariable(variableAsignCompuesta, String.valueOf(rdo));
						} else {
							funcion.updateVariable(variableAsignCompuesta, String.valueOf(rdo));
						}
					}

				} else if (linea.getAccion().equals("%")) {
					String operando1 = linea.getParametro1();
					if (operando1.matches("^[a-z]*$")) {
						operando1 = funcion.getVariables(operando1);
					}

					String operando2 = linea.getParametro2();
					if (operando2.matches("^[a-z]*$")) {
						operando2 = funcion.getVariables(operando2);
					}

					float rdo = 0;
					if (!((operando1.contains(".")) && (operando2.contains(".")))) {    //si los 2 son enteros -> rdo es entero
						rdo = Integer.parseInt(operando1) % Integer.parseInt(operando2);
					} else { //alguno no es entero -> rdo float
						rdo = Float.parseFloat(operando1) % Float.parseFloat(operando2);
					}
					if (asignacionCompuesta) {
						if (!funcion.variableExists(variableAsignCompuesta)) {
							funcion.addVariable(variableAsignCompuesta, String.valueOf(rdo));
						} else {
							funcion.updateVariable(variableAsignCompuesta, String.valueOf(rdo));
						}
					}

				} else if (linea.getAccion().equals("^")) {
					String operando1 = linea.getParametro1();
					if (operando1.matches("^[a-z]*$")) {
						operando1 = funcion.getVariables(operando1);
					}

					String operando2 = linea.getParametro2();
					if (operando2.matches("^[a-z]*$")) {
						operando2 = funcion.getVariables(operando2);
					}

					double rdo = 0;
					if (!((operando1.contains(".")) && (operando2.contains(".")))) {    //si los 2 son enteros
						rdo = Math.pow(Integer.parseInt(operando1), Integer.parseInt(operando2));
					} else { //alguno no es entero -> rdo float
						rdo = Math.pow(Float.parseFloat(operando1), Float.parseFloat(operando2));
					}
					if (asignacionCompuesta) {
						if (!funcion.variableExists(variableAsignCompuesta)) {
							funcion.addVariable(variableAsignCompuesta, String.valueOf(rdo));
						} else {
							funcion.updateVariable(variableAsignCompuesta, String.valueOf(rdo));
						}
					}
				} else if (linea.getAccion().equals("return")) {
					if (linea.getParametro1().contains("\"")) {
						String parametro1 = linea.getParametro1().replaceAll("\"", "");
						retorno = parametro1;
						i = funcion.getBloqueCodigo().size();
					} else if (linea.getParametro1().matches("^[a-z]*$")) {
						retorno = funcion.getVariables(linea.getParametro1());
						i = funcion.getBloqueCodigo().size();
					}
				} else if (linea.getAccion().equals("if")) {
					String parametro1 = linea.getParametro1();
					if (parametro1.contains("&&")) {
						String[] condiciones = parametro1.split("&&");
						String cond1 = condiciones[0];
						String cond2 = condiciones[1];

						cond1 = cond1.replaceAll(String.valueOf('('), "");
						Boolean condicion1 = false;
						if (cond1.contains("==")) {
							String[] aux = cond1.split("==");
							condicion1 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond1.contains("!=")) {
							String[] aux = cond1.split("!=");
							condicion1 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond1.contains("<")) {
							String[] aux = cond1.split("<");
							condicion1 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond1.contains(">")) {
							String[] aux = cond1.split(">");
							condicion1 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond1.contains("<=")) {
							String[] aux = cond1.split("<=");
							condicion1 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond1.contains(">=")) {
							String[] aux = cond1.split(">=");
							condicion1 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond1.charAt(0) == '!') {
							condicion1 = evalBool.not(condicion1);
						}

						cond2 = cond2.replaceAll(String.valueOf('('), "");
						Boolean condicion2 = false;
						if (cond2.contains("==")) {
							String[] aux = cond2.split("==");
							condicion2 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond2.contains("!=")) {
							String[] aux = cond2.split("!=");
							condicion2 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond2.contains("<")) {
							String[] aux = cond2.split("<");
							condicion2 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond2.contains(">")) {
							String[] aux = cond2.split(">");
							condicion2 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond2.contains("<=")) {
							String[] aux = cond2.split("<=");
							condicion2 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond2.contains(">=")) {
							String[] aux = cond2.split(">=");
							condicion2 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond2.charAt(0) == '!') {
							condicion2 = evalBool.not(condicion1);
						}
						saleCond = (condicion1 && condicion2);

					} else if (parametro1.contains("||")) {
						String[] condiciones = parametro1.split("||");
						String cond1 = condiciones[0];
						String cond2 = condiciones[1];
						cond1 = cond1.replaceAll(String.valueOf('('), "");
						Boolean condicion1 = false;
						if (cond1.contains("==")) {
							String[] aux = cond1.split("==");
							condicion1 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond1.contains("!=")) {
							String[] aux = cond1.split("!=");
							condicion1 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond1.contains("<")) {
							String[] aux = cond1.split("<");
							condicion1 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond1.contains(">")) {
							String[] aux = cond1.split(">");
							condicion1 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond1.contains("<=")) {
							String[] aux = cond1.split("<=");
							condicion1 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond1.contains(">=")) {
							String[] aux = cond1.split(">=");
							condicion1 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond1.charAt(0) == '!') {
							condicion1 = evalBool.not(condicion1);
						}

						cond2 = cond2.replaceAll(String.valueOf('('), "");
						Boolean condicion2 = false;
						if (cond2.contains("==")) {
							String[] aux = cond2.split("==");
							condicion2 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond2.contains("!=")) {
							String[] aux = cond2.split("!=");
							condicion2 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond2.contains("<")) {
							String[] aux = cond2.split("<");
							condicion2 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond2.contains(">")) {
							String[] aux = cond2.split(">");
							condicion2 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond2.contains("<=")) {
							String[] aux = cond2.split("<=");
							condicion2 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond2.contains(">=")) {
							String[] aux = cond2.split(">=");
							condicion2 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond2.charAt(0) == '!') {
							condicion2 = evalBool.not(condicion1);
						}

						saleCond = (condicion1 || condicion2);

					} else if (parametro1.contains("##")) {
						String[] condiciones = parametro1.split("##");
						String cond1 = condiciones[0];
						String cond2 = condiciones[1];
						cond1 = cond1.replaceAll(String.valueOf('('), "");
						Boolean condicion1 = false;
						if (cond1.contains("==")) {
							String[] aux = cond1.split("==");
							condicion1 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond1.contains("!=")) {
							String[] aux = cond1.split("!=");
							condicion1 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond1.contains("<")) {
							String[] aux = cond1.split("<");
							condicion1 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond1.contains(">")) {
							String[] aux = cond1.split(">");
							condicion1 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond1.contains("<=")) {
							String[] aux = cond1.split("<=");
							condicion1 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond1.contains(">=")) {
							String[] aux = cond1.split(">=");
							condicion1 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond1.charAt(0) == '!') {
							condicion1 = evalBool.not(condicion1);
						}


						cond2 = cond2.replaceAll(String.valueOf('('), "");
						Boolean condicion2 = false;
						if (cond2.contains("==")) {
							String[] aux = cond2.split("==");
							condicion2 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond2.contains("!=")) {
							String[] aux = cond2.split("!=");
							condicion2 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond2.contains("<")) {
							String[] aux = cond2.split("<");
							condicion2 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond2.contains(">")) {
							String[] aux = cond2.split(">");
							condicion2 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond2.contains("<=")) {
							String[] aux = cond2.split("<=");
							condicion2 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond2.contains(">=")) {
							String[] aux = cond2.split(">=");
							condicion2 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond2.charAt(0) == '!') {
							condicion2 = evalBool.not(condicion1);
						}

						saleCond = (condicion1 ^ condicion2);

					} else {

						parametro1 = parametro1.replaceAll(String.valueOf('('), "");
						Boolean condicion = false;
						if (parametro1.contains("==")) {
							String[] aux = parametro1.split("==");
							condicion = evalBool.iguales(aux[0], aux[1]);
						} else if (parametro1.contains("!=")) {
							String[] aux = parametro1.split("!=");
							condicion = evalBool.desiguales(aux[0], aux[1]);
						} else if (parametro1.contains("<")) {
							String[] aux = parametro1.split("<");
							condicion = evalBool.menorque(aux[0], aux[1]);
						} else if (parametro1.contains(">")) {
							String[] aux = parametro1.split(">");
							condicion = evalBool.mayorque(aux[0], aux[1]);
						} else if (parametro1.contains("<=")) {
							String[] aux = parametro1.split("<=");
							condicion = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (parametro1.contains(">=")) {
							String[] aux = parametro1.split(">=");
							condicion = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (parametro1.charAt(0) == '!') {
							condicion = evalBool.not(condicion);
						}
						saleCond = condicion;
					}
				} else if (linea.getAccion().equals("while")) {
					String parametro1 = linea.getParametro1();
					if (parametro1.contains("&&")) {
						String[] condiciones = parametro1.split("&&");
						String cond1 = condiciones[0];
						String cond2 = condiciones[1];

						cond1 = cond1.replaceAll(String.valueOf('('), "");
						Boolean condicion1 = false;
						if (cond1.contains("==")) {
							String[] aux = cond1.split("==");
							condicion1 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond1.contains("!=")) {
							String[] aux = cond1.split("!=");
							condicion1 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond1.contains("<")) {
							String[] aux = cond1.split("<");
							condicion1 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond1.contains(">")) {
							String[] aux = cond1.split(">");
							condicion1 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond1.contains("<=")) {
							String[] aux = cond1.split("<=");
							condicion1 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond1.contains(">=")) {
							String[] aux = cond1.split(">=");
							condicion1 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond1.charAt(0) == '!') {
							condicion1 = evalBool.not(condicion1);
						}

						cond2 = cond2.replaceAll(String.valueOf('('), "");
						Boolean condicion2 = false;
						if (cond2.contains("==")) {
							String[] aux = cond2.split("==");
							condicion2 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond2.contains("!=")) {
							String[] aux = cond2.split("!=");
							condicion2 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond2.contains("<")) {
							String[] aux = cond2.split("<");
							condicion2 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond2.contains(">")) {
							String[] aux = cond2.split(">");
							condicion2 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond2.contains("<=")) {
							String[] aux = cond2.split("<=");
							condicion2 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond2.contains(">=")) {
							String[] aux = cond2.split(">=");
							condicion2 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond2.charAt(0) == '!') {
							condicion2 = evalBool.not(condicion1);
						}
						saleCond = (condicion1 && condicion2);

					} else if (parametro1.contains("||")) {
						String[] condiciones = parametro1.split("||");
						String cond1 = condiciones[0];
						String cond2 = condiciones[1];
						cond1 = cond1.replaceAll(String.valueOf('('), "");
						Boolean condicion1 = false;
						if (cond1.contains("==")) {
							String[] aux = cond1.split("==");
							condicion1 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond1.contains("!=")) {
							String[] aux = cond1.split("!=");
							condicion1 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond1.contains("<")) {
							String[] aux = cond1.split("<");
							condicion1 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond1.contains(">")) {
							String[] aux = cond1.split(">");
							condicion1 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond1.contains("<=")) {
							String[] aux = cond1.split("<=");
							condicion1 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond1.contains(">=")) {
							String[] aux = cond1.split(">=");
							condicion1 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond1.charAt(0) == '!') {
							condicion1 = evalBool.not(condicion1);
						}

						cond2 = cond2.replaceAll(String.valueOf('('), "");
						Boolean condicion2 = false;
						if (cond2.contains("==")) {
							String[] aux = cond2.split("==");
							condicion2 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond2.contains("!=")) {
							String[] aux = cond2.split("!=");
							condicion2 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond2.contains("<")) {
							String[] aux = cond2.split("<");
							condicion2 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond2.contains(">")) {
							String[] aux = cond2.split(">");
							condicion2 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond2.contains("<=")) {
							String[] aux = cond2.split("<=");
							condicion2 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond2.contains(">=")) {
							String[] aux = cond2.split(">=");
							condicion2 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond2.charAt(0) == '!') {
							condicion2 = evalBool.not(condicion1);
						}

						saleCond = (condicion1 || condicion2);

					} else if (parametro1.contains("##")) {
						String[] condiciones = parametro1.split("##");
						String cond1 = condiciones[0];
						String cond2 = condiciones[1];
						cond1 = cond1.replaceAll(String.valueOf('('), "");
						Boolean condicion1 = false;
						if (cond1.contains("==")) {
							String[] aux = cond1.split("==");
							condicion1 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond1.contains("!=")) {
							String[] aux = cond1.split("!=");
							condicion1 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond1.contains("<")) {
							String[] aux = cond1.split("<");
							condicion1 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond1.contains(">")) {
							String[] aux = cond1.split(">");
							condicion1 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond1.contains("<=")) {
							String[] aux = cond1.split("<=");
							condicion1 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond1.contains(">=")) {
							String[] aux = cond1.split(">=");
							condicion1 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond1.charAt(0) == '!') {
							condicion1 = evalBool.not(condicion1);
						}


						cond2 = cond2.replaceAll(String.valueOf('('), "");
						Boolean condicion2 = false;
						if (cond2.contains("==")) {
							String[] aux = cond2.split("==");
							condicion2 = evalBool.iguales(aux[0], aux[1]);
						} else if (cond2.contains("!=")) {
							String[] aux = cond2.split("!=");
							condicion2 = evalBool.desiguales(aux[0], aux[1]);
						} else if (cond2.contains("<")) {
							String[] aux = cond2.split("<");
							condicion2 = evalBool.menorque(aux[0], aux[1]);
						} else if (cond2.contains(">")) {
							String[] aux = cond2.split(">");
							condicion2 = evalBool.mayorque(aux[0], aux[1]);
						} else if (cond2.contains("<=")) {
							String[] aux = cond2.split("<=");
							condicion2 = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (cond2.contains(">=")) {
							String[] aux = cond2.split(">=");
							condicion2 = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (cond2.charAt(0) == '!') {
							condicion2 = evalBool.not(condicion1);
						}

						saleCond = (condicion1 ^ condicion2);

					} else {

						parametro1 = parametro1.replaceAll(String.valueOf('('), "");
						Boolean condicion = false;
						if (parametro1.contains("==")) {
							String[] aux = parametro1.split("==");
							condicion = evalBool.iguales(aux[0], aux[1]);
						} else if (parametro1.contains("!=")) {
							String[] aux = parametro1.split("!=");
							condicion = evalBool.desiguales(aux[0], aux[1]);
						} else if (parametro1.contains("<")) {
							String[] aux = parametro1.split("<");
							condicion = evalBool.menorque(aux[0], aux[1]);
						} else if (parametro1.contains(">")) {
							String[] aux = parametro1.split(">");
							condicion = evalBool.mayorque(aux[0], aux[1]);
						} else if (parametro1.contains("<=")) {
							String[] aux = parametro1.split("<=");
							condicion = evalBool.menorIgualque(aux[0], aux[1]);
						} else if (parametro1.contains(">=")) {
							String[] aux = parametro1.split(">=");
							condicion = evalBool.mayorIgualque(aux[0], aux[1]);
						}
						if (parametro1.charAt(0) == '!') {
							condicion = evalBool.not(condicion);
						}
						if(condicion){
							pilaWhile.add(Integer.parseInt(linea.getParametro2()));
						} else{
							saleCond = condicion;
						}
					}
				} else if (linea.getAccion().equals("exit")) {
					if(!pilaWhile.isEmpty()){
						i = pilaWhile.get(pilaWhile.size()-1);
						pilaWhile.remove(pilaWhile.size()-1);
					}
				} else if(linea.getAccion().equals("llamada")){
					String parametro1 = linea.getParametro1();
					ejecutar(getFuncion(parametro1));
				} 
			}
		}
	}
	
	
	
	

}
