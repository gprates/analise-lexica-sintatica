package gals;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Semantico implements Constants
{
    // Tabela de símbolos: nome da variável -> valor
    private Map<String, BigInteger> variaveis = new HashMap<>();

    // Pilha de valores usada no cálculo das expressões
    private Deque<BigInteger> pilha = new ArrayDeque<>();

    // Variável que vai receber o resultado da atribuição
    private String destino;

    // Leitura do teclado para o comando "capturar"
    private Scanner teclado = new Scanner(System.in);

    // Limite para o expoente, para não travar a JVM com números gigantes
    private static final int EXPOENTE_MAXIMO = 100000;

    public void executeAction(int action, Token token) throws SemanticError
    {
        BigInteger a, b;

        switch (action)
        {
            // ---------- Ações do exemplo do professor ----------

            case 1: // imprimir -> mostra o resultado do último cálculo
                if (pilha.isEmpty())
                    throw new SemanticError("Nada para imprimir", token.getPosition());
                System.out.println(pilha.pop().toString(2));
                break;

            case 2: // capturar -> lê um número binário do teclado
                System.out.print("capturar> ");
                if (!teclado.hasNextLine())
                    throw new SemanticError("Nenhum valor informado para capturar", token.getPosition());
                pilha.push(converteBinario(teclado.nextLine().trim(), token));
                break;

            case 3: // numeros -> empilha o número (precisa ser binário)
                pilha.push(converteBinario(token.getLexeme(), token));
                break;

            case 4: // soma
                b = pilha.pop();
                a = pilha.pop();
                pilha.push(a.add(b));
                break;

            case 5: // subtração (sem sinal: resultado não pode ser negativo)
                b = pilha.pop();
                a = pilha.pop();
                if (a.compareTo(b) < 0)
                    throw new SemanticError("Subtração resulta em número negativo", token.getPosition());
                pilha.push(a.subtract(b));
                break;

            // ---------- Ações acrescentadas no trabalho ----------

            case 6: // <destino> ::= id #6  -> guarda o nome da variável
                destino = token.getLexeme();
                break;

            case 7: // <atribuicao> ::= <destino> = <expressao> ; #7
                variaveis.put(destino, pilha.pop());
                break;

            case 8: // Show ( <expressao> ) ; #8
                System.out.println(pilha.pop().toString(2));
                break;

            case 9: // multiplicação
                b = pilha.pop();
                a = pilha.pop();
                pilha.push(a.multiply(b));
                break;

            case 10: // divisão inteira
                b = pilha.pop();
                a = pilha.pop();
                if (b.signum() == 0)
                    throw new SemanticError("Divisão por zero", token.getPosition());
                pilha.push(a.divide(b));
                break;

            case 11: // exponenciação
                b = pilha.pop();
                a = pilha.pop();
                if (b.compareTo(BigInteger.valueOf(EXPOENTE_MAXIMO)) > 0)
                    throw new SemanticError("Expoente muito grande", token.getPosition());
                pilha.push(a.pow(b.intValue()));
                break;

            case 12: // uso de variável
                if (!variaveis.containsKey(token.getLexeme()))
                    throw new SemanticError("Variável '" + token.getLexeme() + "' não declarada", token.getPosition());
                pilha.push(variaveis.get(token.getLexeme()));
                break;

            case 13: // Log ( <expressao> ) -> log na base 2, parte inteira
                a = pilha.pop();
                if (a.signum() == 0)
                    throw new SemanticError("Log de zero não é definido", token.getPosition());
                pilha.push(BigInteger.valueOf(a.bitLength() - 1));
                break;
        }
    }

    // O token "numeros" do professor aceita [0-9]+, então a checagem de binário fica aqui
    private BigInteger converteBinario(String texto, Token token) throws SemanticError
    {
        if (!texto.matches("[01]+"))
            throw new SemanticError("'" + texto + "' não é um número binário", token.getPosition());
        return new BigInteger(texto, 2);
    }
}
