package gals;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

// Uso: java gals.Main programa.txt   (ou sem argumento para ler da entrada padrão)
// Se o programa usar "capturar", passe o programa por arquivo: o teclado fica livre para digitar os valores.
public class Main
{
    public static void main(String[] args) throws Exception
    {
        Reader entrada = args.length > 0
            ? new FileReader(args[0])
            : new InputStreamReader(System.in);

        Lexico lexico = new Lexico(entrada);
        Sintatico sintatico = new Sintatico();
        Semantico semantico = new Semantico();

        try
        {
            sintatico.parse(lexico, semantico);
        }
        catch (LexicalError e)
        {
            System.out.println("Erro léxico: " + e.getMessage() + " (posição " + e.getPosition() + ")");
        }
        catch (SyntacticError e)
        {
            System.out.println("Erro sintático: " + e.getMessage() + " (posição " + e.getPosition() + ")");
        }
        catch (SemanticError e)
        {
            System.out.println("Erro semântico: " + e.getMessage() + " (posição " + e.getPosition() + ")");
        }
    }
}
