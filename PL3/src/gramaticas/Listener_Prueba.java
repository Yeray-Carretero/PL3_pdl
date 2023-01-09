package gramaticas;

import org.antlr.runtime.ANTLRInputStream;
import java.util.ArrayList;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.TreeParser;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import gramaticas.GrammarpruebaParser.FuncionContext;
import gramaticas.GrammarpruebaParser.InitVariableContext;
import gramaticas.GrammarpruebaParser.NumeroContext;

import java.util.*;

public class Listener_Prueba extends GrammarpruebaParserBaseListener {
	
	private TSimbolos tablaSimbolos;
	
	public Listener_Prueba(TSimbolos tablaSimbolos)
	{
		this.tablaSimbolos = tablaSimbolos;
	}
	/**
	 * @param  ctx
	 * Metodo para almacenar los datos del fichero en bloques de funciones
	 */
	@Override
	public void enterFuncion(FuncionContext ctx) {
		ArrayList<String> lineas = new ArrayList<String>();
		for(int i=0;i<ctx.bloq_cod().getChildCount();i++) {
			lineas.add(ctx.bloq_cod().sent(i).getText());
		}
		Function fun = new Function(ctx.FUNCT().getText());
		tablaSimbolos.setFuncionActual(fun);
		tablaSimbolos.addFunction(fun);
	}


	@Override
	/**
	 * Metodo para almacenar los datos de una inicializacion del tipo let x = y
	 * @param ctx
	 */
	public void enterInitVariable(InitVariableContext ctx) {
		String valor=ctx.expr().getChild(0).getText();
		String nombre=ctx.VAR().getText();
		tablaSimbolos.getFuncionActual().addVariable(nombre, valor);
	}

	@Override
	public void exitBucle(GrammarpruebaParser.BucleContext ctx) {
		tablaSimbolos.getFuncionActual().addLineaCod("exit");
	}

	@Override
	public void enterSent(GrammarpruebaParser.SentContext ctx) {
		tablaSimbolos.getFuncionActual().addLineaCod(ctx.getText());
	}

	@Override
	public void enterRetorno(GrammarpruebaParser.RetornoContext ctx) {
		tablaSimbolos.getFuncionActual().addLineaCod(ctx.getText());
	}

	@Override
	public void exitPrograma(GrammarpruebaParser.ProgramaContext ctx) {
		tablaSimbolos.ejecutar(tablaSimbolos.getFuncionActual());
	}
}
