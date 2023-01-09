package gramaticas;

public class EvalBool {

    public Boolean iguales(String parametro1, String parametro2){
        if(parametro1.equals(parametro2)){
            return true;
        } else {
            return false;
        }
    }
    public Boolean desiguales(String parametro1, String parametro2){
        if(parametro1.equals(parametro2)){
            return false;
        } else {
            return true;
        }
    }
    public Boolean not(boolean bool){
        return !bool;
    }
    public Boolean menorque(String parametro1, String parametro2){
        if(Float.parseFloat(parametro1) < Float.parseFloat(parametro2)){
            return true;
        } else{
            return false;
        }
    }
    public Boolean mayorque(String parametro1, String parametro2){
        if(Float.parseFloat(parametro1) > Float.parseFloat(parametro2)){
            return true;
        } else{
            return false;
        }
    }
    public Boolean menorIgualque(String parametro1, String parametro2){
        if(Float.parseFloat(parametro1) <= Float.parseFloat(parametro2)){
            return true;
        } else{
            return false;
        }
    }
    public Boolean mayorIgualque(String parametro1, String parametro2){
        if(Float.parseFloat(parametro1) >= Float.parseFloat(parametro2)){
            return true;
        } else{
            return false;
        }
    }
}
