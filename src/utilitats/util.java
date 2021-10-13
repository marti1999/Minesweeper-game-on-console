package utilitats;

import java.util.Scanner;

/**
 *
 * @author marti
 */
public class util {

    /**
     * Demana un integer amb el String que se li passa. Evita crash si no
     * s'introdueix integer
     *
     * @param frase Frase que se li passa
     * @return Valor del integer
     */
    public static int integer(String frase) {
        int num;
        Scanner sc = new Scanner(System.in);
        System.out.print(frase);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Error, escriu un valor integer: ");
        }
        num = sc.nextInt();

        return num;
    }

    /**
     * Demana un integer amb el String que se li passa. Evita crash si no
     * s'introdueix integer. També hi ha restricció al posar un integer, ha de
     * ser entre un mínim i un màxim.
     *
     * @param frase Frase qeu se li pasa
     * @param min Valor mínim del integer a introduir
     * @param max Valor màxim del integer a introdueix
     * @return Valor del integer
     */
    public static int integerMinMax(String frase, int min, int max) {
        int num;
        Scanner sc = new Scanner(System.in);
        System.out.print(frase);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Error, escriu un valor integer: ");
        }
        num = sc.nextInt();

        while (num < min || num > max) {
            System.out.printf("Escriu un número més gran o igual a %d i més petit o igual a %d\n", min, max);
            System.out.print("Valor: ");
            while (!sc.hasNextInt()) {
                sc.next();
                System.out.print("Error, escriu un valor integer: ");
            }
            num = sc.nextInt();
        }

        return num;

    }

    /**
     * Demana un integer amb el String que se li passa. Evita crash si no
     * s'introdueix integer. També hi ha restricció al posar un integer, ha de
     * ser entre un mínim i un màxim. Versió modificada per acceptar el valor -1
     * encara que estigui fora dels límits
     *
     * @param frase Frase qeu se li pasa
     * @param min Valor mínim del integer a introduir
     * @param max Valor màxim del integer a introdueix
     * @return Valor del integer
     */
    public static int pirata(String frase, int min, int max) {
        int num;
        Scanner sc = new Scanner(System.in);
        System.out.print(frase);

        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Error, escriu un valor integer: ");
        }
        num = sc.nextInt();

        if (num == -1) {
            return num;
        } else {
            while (num < min || num > max) {
                System.out.printf("Escriu un número més gran o igual a %d i més petit o igual a %d\n", min, max);
                System.out.print("Valor: ");
                while (!sc.hasNextInt()) {
                    sc.next();
                    System.out.print("Error, escriu un valor integer: ");
                }
                num = sc.nextInt();
            }

            return num;
        }

    }

    /**
     * Demana un double amb el String que se li passa. Evita crash si no
     * s'introdueix double
     *
     * @param frase Frase que se li passa
     * @return Valor del double
     */
    public static double decimal(String frase) {
        double num;
        Scanner sc = new Scanner(System.in);
        System.out.print(frase);
        while (!sc.hasNextDouble()) {
            sc.next();
            System.out.print("Error, input a correct value: ");
        }
        num = sc.nextDouble();

        return num;
    }

    /**
     * Demana un String amb el String que se li passa.
     *
     * @param frase Frase que se li passa
     * @return Valor del String
     */
    public static String text(String frase) {
        String text;
        Scanner sc = new Scanner(System.in);
        System.out.print(frase);
        text = sc.nextLine();
        return text;
    } 
}
