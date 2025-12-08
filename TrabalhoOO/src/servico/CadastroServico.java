// classe parametrizada 
package servico;

public class CadastroServico <T>{
    
    private Object[] pessoas;
    
    public CadastroServico(){
        pessoas = new Object[0];
}
    public boolean adicionar (T t){
       Object[] temp = new Object[pessoas.length + 1];
       
       for (int i = 0; i < pessoas.length ;i++){
           temp[i] = pessoas[i];
       }
       
       temp[pessoas.length] = t;
       
       pessoas = temp;
       
       if(pessoas[pessoas.length - 1] == t){
           return true;
       }else{
           return false;
       }
       
    }
    
   public boolean remover(T t){
    Object[] temp = new Object[pessoas.length - 1];
    boolean removeu = false;
    int j = 0;

    for (int i = 0; i < pessoas.length; i++) {
        if (!pessoas[i].equals(t)) { 
            temp[j] = pessoas[i];
            j++;
        } else {
            removeu = true;
        }
    }

    if (removeu) {
        pessoas = temp; 
    }

    return removeu;
}
  
    public T pesquisar(int n){
        return(T)pessoas[n];
    }
    
    public int getLength() {
    return pessoas.length;
}

    
}
