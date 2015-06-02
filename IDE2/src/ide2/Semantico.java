package ide2;

import java.util.ArrayList;
import java.util.List;

public class Semantico implements Constants
{
    private List<Var> matriz;
    private Var var = new Var();
    private boolean comecaParametros = false;
    private String escopo = "global";
    private int posicao = 0;
    private int tamanhoMatriz = 0;
    private String nomeVet = " ";
    private List<Temporario> temporarios;
    private int countTemp = 0;
    private StringBuilder data;
    private StringBuilder text;
    private boolean inic = false;
    private String sto = "";
    private String stov = "";
    private boolean logico = false;
    private Temporario tempIndVetor;
    private String operacao = "";
    private int posEq = 0;
    private String nomeVetorP = "";
    private Temporario restoConta;
    private boolean treta = false;
    private Temporario exp;
    
    public Semantico() {
        this.matriz = new ArrayList();    
        this.temporarios = new ArrayList();
        
        this.data = new StringBuilder((".data\n"));
        this.text = new StringBuilder((".text\n"));
        
    }    
    
    public Temporario getTemp(){
        for (Temporario t : this.temporarios) {
            if(t.isLivre()){
                return t;
            }
        }
        Temporario novoTemporario = new Temporario();
        countTemp++;
        novoTemporario.setLivre(false);
        novoTemporario.setNome("temp"+countTemp);
        data.append(novoTemporario.getNome()).append(" : 0 \n");
        temporarios.add(novoTemporario);
        return novoTemporario;
    }
    
    public void freeTemp(String temp){
       for (Temporario t : this.temporarios) {
            if(t.getNome().equals(temp)){
                t.setLivre(true);
            }
        } 
    }
    
    public void insereMatriz(){
        tamanhoMatriz++;
        matriz.add(var);
        var = new Var();
    }
    
    public void executeAction(int action, Token token)	throws SemanticError
    {
        String lex = token.getLexeme();
        switch(action){
            case 1:
                if(var.getTipo().equals(" ")){
                    var.setTipo(token.getLexeme());
                }else{
                    throw new SemanticError("Syntax error", token.getPosition());
                }
            break;
            case 2:
                
                for (Var variavel : matriz) {
                    if((variavel.getEscopo().equals(escopo) || variavel.getEscopo().equals("global")) && variavel.getNome().equals(lex) ){
                        throw new SemanticError(variavel.getNome()+" já utilizado", token.getPosition());
                    }
                }
                if(var.getNome().equals(" ")){
                    var.setNome(lex);
                    var.setParametro(comecaParametros);
                    if(comecaParametros){
                        posicao++;
                    }
                    var.setPosicao(posicao);
                    var.setEscopo(escopo);
                    insereMatriz();
                }else{
                    throw new SemanticError("Syntax error", token.getPosition());
                }
            break;
            case 3:
                for (Var variavel : matriz) {
                    if((variavel.getEscopo().equals(escopo) || variavel.getEscopo().equals("global")) && variavel.getNome().equals(lex) ){
                        throw new SemanticError(variavel.getNome()+" já utilizado", token.getPosition());
                    }
                }
                if(var.getNome().equals(" ")){
                    var.setTipo("funcao");
                    var.setIniciado(true);
                    var.setNome(token.getLexeme());
                    var.setFunc(true);
                    var.setEscopo(escopo);
                    comecaParametros = true;
                    escopo = var.getNome();
                    insereMatriz();
                }else{
                    throw new SemanticError("Nunca cai aqui", token.getPosition());
                }
            break;
            case 4:
                boolean usado = false;
                for (Var variavel : matriz) {
                    if(variavel.getNome().equals(token.getLexeme()) && variavel.getEscopo().equals(escopo)){
                        if(!variavel.isIniciado()){
                            throw new SemanticError(variavel.getNome()+" não inicializada", token.getPosition());
                        }
                        usado = true;
                        variavel.setUsado(true);
                    }
                }
                if(!usado){
                    throw new SemanticError(lex+" não existe nesse escopo", token.getPosition());
                }
            break;
            case 5:
                comecaParametros = false;
                posicao = 0;
            break;
            case 6:
                escopo = "global";
            break;
            case 7:
                for (Var variavel : matriz) {
                    if((variavel.getEscopo().equals(escopo) || variavel.getEscopo().equals("global")) && variavel.getNome().equals(lex) ){
                        throw new SemanticError(variavel.getNome()+" já utilizado", token.getPosition());
                    }
                }
                if(var.getNome().equals(" ")){
                    var.setNome(token.getLexeme());
                    if(comecaParametros){
                        var.setParametro(comecaParametros);
                        posicao++;
                    }
                    var.setPosicao(posicao);
                    var.setEscopo(escopo);
                    var.setVetor(true);
                }else{
                    throw new SemanticError("Syntax Error", token.getPosition());
                }
            break;
            case 8:
                var.setTamanhoVetor(Integer.parseInt(token.getLexeme()));
                var.setUsado(true);
                insereMatriz();
            break;
            case 9:
                for (Var variavel : matriz) {
                    if(variavel.getNome().equals(token.getLexeme())){
                        variavel.setIniciado(true);
                    }
                }
            break;
            case 10:
                for (Var variavel : matriz) {
                    if(variavel.getNome().equals(token.getLexeme()) && variavel.getEscopo().equals(escopo)){
                        variavel.setUsado(true);
                        if(!variavel.isIniciado()){
                            throw new SemanticError(variavel.getNome()+" não inicializada", token.getPosition());
                        }
                    }
                }
            break;
            case 11:
                text.append("HLT 0 \n");
                for (Var variavel : matriz) {
                    if(!(variavel.isUsado()) && !(variavel.getTipo().equals("funcao"))){
                        throw new SemanticError(variavel.getNome()+" não utilizada", token.getPosition());
                    }
                }
            break;
            case 12:
                for (Var variavel : matriz) {
                    if((variavel.getEscopo().equals(escopo) || variavel.getEscopo().equals("global")) && variavel.getNome().equals(lex) ){
                        throw new SemanticError(variavel.getNome()+" já utilizado", token.getPosition());
                    }
                }
                if(var.getNome().equals(" ")){
                    var.setNome(lex);
                    var.setParametro(comecaParametros);
                    if(comecaParametros){
                        posicao++;
                    }
                    var.setPosicao(posicao);
                    var.setIniciado(true);
                    var.setEscopo(escopo);
                    insereMatriz();
                }else{
                    throw new SemanticError("Syntax error", token.getPosition());
                }
            break;
            case 13:
                boolean declarado = false;
                for (Var variavel : matriz) {
                    if(variavel.getNome().equals(token.getLexeme())){
                       declarado = true; 
                        variavel.setIniciado(true);
                    }
                }
                if(!declarado){
                    throw new SemanticError(lex+" não declarado", token.getPosition());
                }
            break;
            case 14:
                for (Var variavel : matriz) {
                    if(variavel.getNome().equals(lex)){
                        variavel.setIniciado(true);
                    }
                }
                nomeVet = lex;
            break;
            case 15:
                for (Var variavel : matriz) {
                    if(variavel.getNome().equals(nomeVet)){
                        if(variavel.getTamanhoVetor() <= Integer.parseInt(lex)){
                            throw new SemanticError(nomeVet+" posição inválida", token.getPosition());
                        }
                    }
                }
                nomeVet = "";    
            break;
            case 21:
                data.append(lex).append(": 0 \n");
            break;
            case 22:
                 data.append(lex).append(" : 0");
            break;
            case 23:
                int qtd = Integer.parseInt(lex);
                for (int i = 0; i < qtd-1; i++) {
                    data.append(", 0");
                }
                data.append("\n");
            break;
            case 24:
                data.append(lex);
                inic = true;
            break;
            case 25:
                sto = lex;
            break;
            case 26:
                stov = lex;
            break;
            case 27:
                text.append("LDI ").append(lex).append("\n");
                tempIndVetor = this.getTemp();
                text.append("STO ").append(tempIndVetor.getNome()).append("\n");
            break;
            case 28:
                if(!sto.equals("")){
                    text.append("STO ").append(sto).append("\n");
                    posEq = 0;
                }
                if(!stov.equals("")){
                        Temporario t = this.getTemp();
                        text.append("STO ").append(t.getNome()).append("\n ");
                        text.append("LD ").append(tempIndVetor.getNome()).append("\n ");
                        text.append("STO $indr \n");
                        text.append("LD ").append(t.getNome()).append("\n ");
                        text.append("STOV ").append(stov).append("\n");
                        this.freeTemp(t.getNome());
                        this.freeTemp(tempIndVetor.getNome());
                        posEq = 0;
                }
            break;
            case 29:
                operacao = lex;
            break;
            case 30:
            if(inic){
                    data.append(" : ").append(lex).append(" \n");
                    inic = false;
            }else{
                if(operacao.equals("")){
                        text.append("LDI ").append(lex).append(" \n");
                } else {
                    if (operacao.equals("+")){
                        text.append("ADDI ").append(lex).append(" \n");
                    }
                    if (operacao.equals("-")){
                        text.append("SUBI ").append(lex).append(" \n");
                    }
                   operacao = "";
                }
                posEq++;
            }
            break;
            case 31:
                if(operacao.equals("")){
                        text.append("LD ").append(lex).append(" \n");
                } else {
                    if (operacao.equals("+")){
                        text.append("ADD ").append(lex).append(" \n");
                    }
                    if (operacao.equals("-")){
                        text.append("SUB ").append(lex).append(" \n");
                    }
                   operacao = "";
                }
                posEq++;
            break;
            case 32:
                if(posEq != 0){
                    restoConta = this.getTemp();
                    text.append("STO ").append(restoConta.getNome()).append(" \n");
                    nomeVetorP = lex;
                }else{
                    posEq++;
                }
            break;
            case 33:
                text.append("LDI ").append(lex).append(" \n");
                text.append("STO $indr \n");
                text.append("LDV ").append(nomeVetorP).append(" \n");
                nomeVetorP = "";
                if (operacao.equals("+")){
                    Temporario t = this.getTemp();
                    text.append("STO ").append(t.getNome()).append(" \n");
                    text.append("LD ").append(restoConta.getNome()).append(" \n");
                    text.append("ADD ").append(t.getNome()).append(" \n");
                    this.freeTemp(t.getNome());
                    this.freeTemp(restoConta.getNome());
                }
                if (operacao.equals("-")){
                    Temporario t = this.getTemp();
                    text.append("STO ").append(t.getNome()).append(" \n");
                    text.append("LD ").append(restoConta.getNome()).append(" \n");
                    text.append("SUB ").append(t.getNome()).append("\n ");
                    this.freeTemp(t.getNome());
                    this.freeTemp(restoConta.getNome());
                }
                operacao = "";
            break;
            case 34:
                text.append("LD ").append(" $in_port").append(" \n");
                text.append("STO ").append(lex).append(" \n");
            break;
            case 35:
                text.append("LD ").append(lex).append(" \n");
                text.append("STO ").append("$out_port").append(" \n");
            break;
                
        }
    }

    public List<Var> getMatriz() {
        return matriz;
    }

    public int getTamanhoMatriz() {
        return tamanhoMatriz;
    }

    public StringBuilder getData() {
        return data;
    }

    public StringBuilder getText() {
        return text;
    }
    
    
    
    
}
