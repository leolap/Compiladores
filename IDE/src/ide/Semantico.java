package ide;

import java.util.ArrayList;
import java.util.List;

public class Semantico implements Constants
{
    private List<Var> matriz;
    private Var var = new Var();
    
    public Semantico() {
        this.matriz = new ArrayList();    
    }
    
    public void insereMatriz(){
        matriz.add(var);
        var = new Var();
    }
    
    public void executeAction(int action, Token token)	throws SemanticError
    {
        
        switch(action){
            case 1:
                if(var.getTipo().equals(" ")){
                    var.setTipo(token.getLexeme());
                }else{
                    System.out.println("erro");
                }
            break;
            case 2:
                if(var.getNome().equals(" ")){
                    var.setTipo(token.getLexeme());
                }else{
                    System.out.println("erro");
                }
            break;
                
        }
    }	
}
