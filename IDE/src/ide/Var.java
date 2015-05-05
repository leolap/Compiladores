/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ide;

/**
 *
 * @author 4416970
 */
public class Var {
    private String nome;
    private String tipo;
    private boolean iniciado;
    private boolean usado;
    private String escopo;
    private boolean parametro;
    private int posicao;
    private boolean vetor;
    private boolean func;

    public Var() {
        this.nome = " ";
        this.tipo = " ";
        this.iniciado = false;
        this.usado = false;
        this.escopo = "global";
        this.parametro = false;
        this.posicao = 0;
        this.vetor = false;
        this.func = false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public void setIniciado(boolean iniciado) {
        this.iniciado = iniciado;
    }

    public String getEscopo() {
        return escopo;
    }

    public void setEscopo(String escopo) {
        this.escopo = escopo;
    }

    public boolean isParametro() {
        return parametro;
    }

    public void setParametro(boolean parametro) {
        this.parametro = parametro;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public boolean isVetor() {
        return vetor;
    }

    public void setVetor(boolean vetor) {
        this.vetor = vetor;
    }

    public boolean isFunc() {
        return func;
    }

    public void setFunc(boolean func) {
        this.func = func;
    }
    
    
}
