package gals;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Lexico lexico = new Lexico(new FileReader("src/teste.txt"));
        Sintatico sintatico = new Sintatico();
        Semantico semantico = new Semantico();
        sintatico.parse(lexico, semantico);

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
