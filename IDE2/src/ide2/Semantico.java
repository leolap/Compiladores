package ide2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    private String opLogico = "";
    private Temporario tempIndVetor;
    private String operacao = "";
    private int posEq = 0;
    private String nomeVetorP = "";
    private Temporario restoConta;
    private Temporario esquerda;
    private Temporario direita;
    private int countRot = 0;
    private Stack<String> rotulos;
    private String rot;
    private String rot2;
    private boolean para = false;
    //private String paraSto;
    private StringBuilder paraAtr;
    
    public Semantico() {
        this.matriz = new ArrayList();    
        this.temporarios = new ArrayList();
        this.rotulos = new Stack();
        
        this.data = new StringBuilder((".data\n"));
        this.text = new StringBuilder((".text\n"));
        this.paraAtr = new StringBuilder("");
        
    }    
    
    public String getRotulo(){
        countRot++;
        rotulos.push("R"+countRot);
        return "R"+countRot;
    }
    
    public void geraLogico(String op, String rotulo){
        switch(op){
                    case ">":
                        text.append("BLE ").append(rotulo).append(" \n");
                    break;
                    case "<":
                        text.append("BGE ").append(rotulo).append(" \n");
                    break;
                    case ">=":
                        text.append("BLT ").append(rotulo).append(" \n");
                    break;
                    case "<=":
                        text.append("BGT ").append(rotulo).append(" \n");
                    break;
                    case "==":
                        text.append("BNE ").append(rotulo).append(" \n");
                    break;
                    case "!=":
                        text.append("BEQ ").append(rotulo).append(" \n");
                    break;
        }
    }
    
    public Temporario getTemp(){
        for (Temporario t : this.temporarios) {
            if(t.isLivre()){
                t.setLivre(false);
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
                break;
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
                    if(!para){
                        text.append("STO ").append(sto).append("\n");
                    }else{
                        paraAtr.append("STO ").append(sto).append("\n");
                    }
                    posEq = 0;
                    sto = "";
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
                        stov = "";
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
                    if(!para){
                        text.append("LDI ").append(lex).append(" \n");
                    }else{
                        paraAtr.append("LDI ").append(lex).append(" \n");
                    }
                } else {
                    if (operacao.equals("+")){
                        if(!para){
                            text.append("ADDI ").append(lex).append(" \n");
                        }else{
                            paraAtr.append("ADDI ").append(lex).append(" \n");
                        }
                    }
                    if (operacao.equals("-")){
                        if(!para){
                            text.append("SUBI ").append(lex).append(" \n");
                        }else{
                            paraAtr.append("SUBI ").append(lex).append(" \n");
                        }
                    }
                    if(operacao.equals("|")){
                        text.append("ORI ").append(lex).append(" \n");
                    }
                    if(operacao.equals("&")){
                        text.append("ANDI ").append(lex).append(" \n");
                    }
                    if(operacao.equals("<<")){
                        text.append("SRL ").append(lex).append(" \n");
                    }
                    if(operacao.equals("<<")){
                        text.append("SLL ").append(lex).append(" \n");
                    }
                   operacao = "";
                }
                posEq++;
            }
            break;
            case 31:
                if(operacao.equals("")){
                        if(!para){
                            text.append("LD ").append(lex).append(" \n");
                        }else{
                            paraAtr.append("LD ").append(lex).append(" \n");
                        }
                } else {
                    if (operacao.equals("+")){
                        if(!para){
                            text.append("ADD ").append(lex).append(" \n");
                        }else{
                            paraAtr.append("ADD ").append(lex).append(" \n");
                        }
                    }
                    if (operacao.equals("-")){
                        if(!para){
                            text.append("SUB ").append(lex).append(" \n");
                        }else{
                            paraAtr.append("SUB ").append(lex).append(" \n");
                        }
                    }
                    if(operacao.equals("|")){
                        text.append("OR ").append(lex).append(" \n");
                    }
                    if(operacao.equals("&")){
                        text.append("AND ").append(lex).append(" \n");
                    }
                    if(operacao.equals("<<")){
                        text.append("SRL ").append(lex).append(" \n");
                    }
                    if(operacao.equals("<<")){
                        text.append("SLL ").append(lex).append(" \n");
                    }
                   operacao = "";
                }
                posEq++;
            break;
            case 32:
                nomeVetorP = lex;
                if(posEq != 0){
                    restoConta = this.getTemp();
                    text.append("STO ").append(restoConta.getNome()).append(" \n");
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
                    restoConta.setLivre(true);
                }
                if (operacao.equals("-")){
                    Temporario t = this.getTemp();
                    text.append("STO ").append(t.getNome()).append(" \n");
                    text.append("LD ").append(restoConta.getNome()).append(" \n");
                    text.append("SUB ").append(t.getNome()).append("\n ");
                    this.freeTemp(t.getNome());
                    restoConta.setLivre(true);
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
            case 36:
                opLogico = lex;
                esquerda = this.getTemp();
                text.append("STO ").append(esquerda.getNome()).append(" \n");
            break;
            case 37:
                direita = this.getTemp();
                text.append("STO ").append(direita.getNome()).append(" \n");
                text.append("LD ").append(esquerda.getNome()).append(" \n");
                text.append("SUB ").append(direita.getNome()).append(" \n");
                esquerda.setLivre(true);
                direita.setLivre(true);
            break;
            case 38:
                rot = this.getRotulo();
                geraLogico(opLogico, rot);
            break;
            case 39:
                rot = rotulos.pop();
                text.append(rot).append(":").append(" \n");
            break;
            case 40:
                rot2 = rotulos.pop();
                rot = getRotulo();
                text.append("JMP ").append(rot).append(" \n");
                text.append(rot2).append(": ").append(" \n");
            break;
            case 41:
                rot = getRotulo();
                text.append(rot).append(": ").append(" \n");
            break;
            case 42:
                rot = this.getRotulo();
                geraLogico(opLogico, rot);
            break;
            case 43:
                rot2 = rotulos.pop();
                rot = rotulos.pop();
                text.append("JMP ").append(rot).append(" \n");
                text.append(rot2).append(": ").append(" \n");
            break;
            case 44:
                para = true;
            break;
            case 45:
                para = false;
            break;
            case 46:
                text.append(paraAtr.toString());
                paraAtr = new StringBuilder("");
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
