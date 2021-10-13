package buscaminas_marticaixal;

import java.util.Random;
import utilitats.util;

/**
 *
 * @author marti
 */
public class Buscaminas2_MartiCaixal {

    /**
     * Main Main
     *
     * @param 
     */
    public static void main(String[] args) {
        int bombs, numeroMines;
        String[][] taulell;
        int posicions[];
        int posicionsOriginals[] = new int[2];
        int opcio = menu();
        boolean hit = false;

        while (opcio != 5) {
            long startTime = System.nanoTime();
            boolean play = true;
            int cont = 0;

            if (opcio == 4) {
                int x = util.integerMinMax("Línies: ", 2, 200);
                int y = util.integerMinMax("Columnes: ", 2, 200);
                bombs = util.integerMinMax("bombes: ", 1, (x * y) - 1);
                taulell = new String[x][y];
                taulell = omplirTaulell(taulell, bombs);
            } else {
                bombs = bomb(opcio);
                taulell = crearTaulell(opcio, bombs);
            }

            String[][] taulellNumeros;
            taulellNumeros = numeros(taulell);
            String[][] taulellJoc = taulellJoc(taulell);

            while (play) {
                mostraTaulell(taulellJoc);
                temps(startTime);
                posicions = posicio(taulell, taulellJoc);
                posicionsOriginals[0] = -1;
                posicionsOriginals[1] = -1;
                numeroMines = mines(taulell, posicions);
                taulellJoc = asignPosicio(taulellJoc, posicions, numeroMines);
                hit = checkTurn(taulell, posicions);

                if (!hit) {
                    taulell = asignPosicio(taulell, posicions, numeroMines);
                    taulellJoc = destapar(taulellJoc, taulellNumeros, posicions, posicionsOriginals);
                    cont = contador(taulellJoc);
                }
                play = checkFinish(taulell, taulellJoc, hit, bombs, cont);
            }
            resultats(taulell, startTime, hit, cont);
            opcio = menu();
        }
    }

    /**
     * Mostra el menú i demana el mode de joc
     *
     * @return Tipus de mode de joc
     */
    public static int menu() {
        int menu;
        System.out.println("Escollir mode de joc:");
        System.out.println("1. 8x8 - 10 bombes");
        System.out.println("2. 16x14 - 40 bombes");
        System.out.println("3. 16x30 - 99 bombes");
        System.out.println("4. Personalitzat");
        System.out.println("5. Finalitzar joc");
        menu = util.integerMinMax("\nIntrodueix número: ", 1, 5);

        return menu;
    }

    /**
     * Defineix una variable amb el número de bombes segons el mode de joc, és
     * utilitzada el mètode checkFinish
     *
     * @param num Variable que defineix el mode de joc
     * @return Número de bombes de la partida actual
     */
    public static int bomb(int num) {
        int bombs = 0;
        switch (num) {
            case 1:
                bombs = 10;
                break;
            case 2:
                bombs = 40;
                break;
            case 3:
                bombs = 99;
                break;
        }
        return bombs;

    }

    /**
     * Crea taulell i defineix el número de bombes segons el mode de joc
     *
     * @param opcio Mode de joc
     * @param bombs Número de bombes a assignar al taulell
     * @return taulell El taulell omplert
     */
    public static String[][] crearTaulell(int opcio, int bombs) {
        String taulell[][] = null;

        switch (opcio) {
            case 1:
                taulell = new String[8][8];
                taulell = omplirTaulell(taulell, bombs);
                break;
            case 2:
                taulell = new String[16][16];
                taulell = omplirTaulell(taulell, bombs);
                break;
            case 3:
                taulell = new String[16][30];
                taulell = omplirTaulell(taulell, bombs);
                break;

            default:
                System.out.print("Error;");
        }

        return taulell;
    }

    /**
     * Posa les mines en el taulell
     *
     * @param taulell taulell a omplir
     * @param bombs número de bombes a assignar al taulell
     * @return taulell amb les bombes
     */
    public static String[][] omplirTaulell(String taulell[][], int bombs) {
        Random r = new Random();
        String none = "   -", mina = "   X";
        int x, y, cont = 0;

        for (int i = 0; i < taulell.length; i++) {
            for (int j = 0; j < taulell[i].length; j++) {
                taulell[i][j] = none;
            }
        }

        while (cont < bombs) {
            x = r.nextInt(taulell.length);
            y = r.nextInt(taulell[1].length);
            if (!taulell[x][y].equals(mina)) {
                taulell[x][y] = mina;
                cont++;
            }
        }

        return taulell;
    }

    /**
     * Mostra un array bidimensional, junt amb el número de les files i columnes
     *
     * @param taulell Taulell a mostrar
     */
    public static void mostraTaulell(String taulell[][]) {
        System.out.println("");
        String space = "   ";
        int c[] = new int[taulell[1].length];
        System.out.print(space);
        for (int i = 0; i < c.length; i++) {
            System.out.printf(" %3d", i + 1);
        }

        System.out.println("");
        for (int i = 0; i < taulell.length; i++) {
            System.out.printf("%3d", i + 1);
            for (String item : taulell[i]) {
                System.out.printf("%s", item);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    /**
     * crea i omple el taulell buit, el que el jugador veurà
     *
     * @param taulell taulell que es fa servir com a referència per crear un
     * altre amb les mateixes mides
     * @return taulell amb els valors assignats
     */
    public static String[][] taulellJoc(String taulell[][]) {
        int x = taulell.length;
        int y = taulell[1].length;
        String taulellJoc[][] = new String[x][y];
        String none = "   -";
        for (int i = 0; i < taulellJoc.length; i++) {
            for (int j = 0; j < taulellJoc[1].length; j++) {
                taulellJoc[i][j] = none;
            }
        }
        return taulellJoc;
    }

    /**
     * Demana posició i comprova que no s'hagi fet servir prèviament
     *
     * @param taulell Mostra les mines en cas d'escriure un valor -1
     * @param taulellJoc Serveix per comprovar si ja s'ha fet servir una
     * coordenada
     * @return Retorna coordenades vàlides
     */
    public static int[] posicio(String taulell[][], String taulellJoc[][]) {
        int x, y;
        String none = "   -";

        x = util.pirata("Fila: ", 1, taulellJoc.length);
        y = util.pirata("Columna: ", 1, taulellJoc[1].length);

        if (x == -1 || y == -1) {
            mostraTaulell(taulell);
            return posicio(taulell, taulellJoc);
        } else {
            x--;
            y--;
            if (!taulellJoc[x][y].equals(none)) {
                System.out.println("Aquesta posició ja està descoberta\n");
                return posicio(taulell, taulellJoc);

            } else {
                int posicions[] = new int[2];
                posicions[0] = x;
                posicions[1] = y;
                return posicions;
            }
        }
    }

    /**
     * Assigna les posicions al taulell del jugador
     *
     * @param taulellJoc taulell on s'assignen les noves posicions
     * @param posicions coordenades
     * @param numeroMines Número de mines al voltant de la posició
     * @return Taulell amb els nous valors
     */
    public static String[][] asignPosicio(String taulellJoc[][], int posicions[], int numeroMines) {
        String hit = "   0";
        String num = "   " + Integer.toString(numeroMines);

        int x = posicions[0];
        int y = posicions[1];

        if (numeroMines != 0) {
            taulellJoc[x][y] = num;
        } else {
            taulellJoc[x][y] = hit;
        }

        return taulellJoc;
    }

    /**
     * Busca les mines al voltant d'una posició
     *
     * @param taulell taulell el qual té les mines assignades
     * @param posicions posicions en les que es buscaran les mines al voltant
     * @return Número de mines al voltant
     */
    public static int mines(String taulell[][], int posicions[]) {
        String mina = "   X";
        int cont = 0;
        int x = posicions[0];
        int y = posicions[1];

        if (x != 0 && y != 0) {
            if (taulell[x - 1][y - 1].equals(mina)) {
                cont++;
            }
        }

        if (x != 0) {
            if (taulell[x - 1][y].equals(mina)) {
                cont++;
            }
        }

        if (x != 0 && y != taulell[1].length - 1) {
            if (taulell[x - 1][y + 1].equals(mina)) {
                cont++;
            }
        }

        if (y != 0) {
            if (taulell[x][y - 1].equals(mina)) {
                cont++;
            }
        }

        if (y != taulell[1].length - 1) {
            if (taulell[x][y + 1].equals(mina)) {
                cont++;
            }
        }

        if (x != taulell.length - 1 && y != 0) {
            if (taulell[x + 1][y - 1].equals(mina)) {
                cont++;
            }
        }
        if (x != taulell.length - 1) {
            if (taulell[x + 1][y].equals(mina)) {
                cont++;
            }
        }

        if (x != taulell.length - 1 && y != taulell[1].length - 1) {
            if (taulell[x + 1][y + 1].equals(mina)) {
                cont++;
            }
        }
        return cont;
    }

    /**
     * comproba el resultat del torn
     *
     * @param taulell taulell que s'utilitza per comprovar la posició i veure si
     * hi ha una mina
     * @param posicions Posició en la que s'ha jugat
     * @return Boolean que diu si hi ha mina o no
     */
    public static boolean checkTurn(String taulell[][], int posicions[]) {
        String mina = "   X";
        int x = posicions[0];
        int y = posicions[1];
        boolean hit;

        hit = taulell[x][y].equals(mina);
        return hit;
    }

    /**
     * comprova si s'ha acabat la partida
     *
     * @param taulell taulell per comprovar
     * @param taulellJoc taulell a comrrovar
     * @param hit boolea que diu s'hi previament s'ha clicat en una mina
     * @param cont Número de caselles destapades, si és més gran que el número
     * de caselles sense mina s'acaba la partida
     * @param bombs nombre de bombes per calcular els espais sense bomba
     * @return boolean que decideix si es segueix jugant o s'acaba la partida
     */
    public static boolean checkFinish(String taulell[][], String taulellJoc[][], boolean hit, int bombs, int cont) {
        //public static boolean checkFinish(objecte info) {

        int total = taulell.length * taulell[1].length;
        int turns = total - bombs;
        boolean play = true;

        if (hit || cont >= turns) {
            play = false;
        }
        return play;
    }

    /**
     * Mostra els resultats de la partida
     *
     * @param taulell Taulell que mostra el taulell on en veuen les posicions
     * destapades i les mines
     * @param startTime Variable que serveix per conèixer el temps de partida
     * @param hit Boolea que mostra victòria o derrota
     * @param cont Comptador que mostra la puntuació de la partida
     */
    public static void resultats(String[][] taulell, long startTime, boolean hit, int cont) {
        mostraTaulell(taulell);
        temps(startTime);
        System.out.println("Fi del joc!!");
        if (hit) {
            System.out.print("Has perdut la partida\n");
        } else {
            System.out.print("Has guanyat la partida\n");
        }
        System.out.println("Puntuació: " + cont);
        System.out.println("\n\n");
    }

    /**
     * Calcula el temps de joc
     *
     * @param startTime Es fa servir per restar el temps actual menys aquest
     */
    public static void temps(long startTime) {
        System.out.println("Temps: " + (System.nanoTime() - startTime) / 1000000000 + "\n");
    }

    /**
     * Troba el número de bombes al voltant de cada casella
     *
     * @param taulell Taulell en el qual hi ha assignades les bombes
     * @return Taulell amb els números assignats a cada posició
     */
    public static String[][] numeros(String taulell[][]) {
        String mina = "   X";
        int cont;
        String num;

        for (int x = 0; x < taulell.length; x++) {
            for (int y = 0; y < taulell[x].length; y++) {
                cont = 0;
                if (!taulell[x][y].equals(mina)) {
                    if (x != 0 && y != 0) {
                        if (taulell[x - 1][y - 1].equals(mina)) {
                            cont++;
                        }
                    }

                    if (x != 0) {
                        if (taulell[x - 1][y].equals(mina)) {
                            cont++;
                        }
                    }

                    if (x != 0 && y != taulell[1].length - 1) {
                        if (taulell[x - 1][y + 1].equals(mina)) {
                            cont++;
                        }
                    }

                    if (y != 0) {
                        if (taulell[x][y - 1].equals(mina)) {
                            cont++;
                        }
                    }

                    if (y != taulell[1].length - 1) {
                        if (taulell[x][y + 1].equals(mina)) {
                            cont++;
                        }
                    }

                    if (x != taulell.length - 1 && y != 0) {
                        if (taulell[x + 1][y - 1].equals(mina)) {
                            cont++;
                        }
                    }
                    if (x != taulell.length - 1) {
                        if (taulell[x + 1][y].equals(mina)) {
                            cont++;
                        }
                    }

                    if (x != taulell.length - 1 && y != taulell[1].length - 1) {
                        if (taulell[x + 1][y + 1].equals(mina)) {
                            cont++;
                        }
                    }
                    num = "   " + Integer.toString(cont);
                    taulell[x][y] = num;
                }
            }
        }

        return taulell;
    }

    /**
     * Destapa totes les caselles seguides que no tinguin ninguna bomba al
     * voltant
     *
     * @param taulellJoc taulell en el que s'assignes els nous valors que verurà
     * l'usuari
     * @param taulellNumeros Taulell amb el que es comparen les posicions i d'on
     * s'agafen els valors
     * @param posicions Posicions a comprovar
     * @param posicionsOriginals Posicions originals per no tornar-hi i entrar
     * en loop
     * @return Taulell amb les caselles destapades
     */
    public static String[][] destapar(String taulellJoc[][], String taulellNumeros[][], int posicions[], int posicionsOriginals[]) {
        int x = posicions[0];
        int y = posicions[1];
        int posicions2[] = new int[2];
        String zero = "   0";

        if (taulellNumeros[x][y].equals("   0")) {
            taulellJoc[x][y] = "   #";
            taulellNumeros[x][y] = "   #";

            if (x != 0) {

                if (!taulellNumeros[x - 1][y].equals("   0")) {
                    taulellJoc[x - 1][y] = taulellNumeros[x - 1][y];
                } else {
                    posicions2[0] = posicions[0] - 1;
                    posicions2[1] = posicions[1];
                    if (posicionsOriginals[0] != posicions2[0] || posicionsOriginals[1] != posicions2[1]) {

                        posicionsOriginals = posicions;
                        taulellJoc[x][y] = "   #";
                        taulellNumeros[x][y] = "   #";
                        taulellJoc = destapar(taulellJoc, taulellNumeros, posicions2, posicionsOriginals);
                    }
                }
            }

            if (x != 0 && y != 0) {
                if (!taulellNumeros[x - 1][y - 1].equals(zero)) {
                    taulellJoc[x - 1][y - 1] = taulellNumeros[x - 1][y - 1];
                } else {
                    posicions2[0] = posicions[0] - 1;
                    posicions2[1] = posicions[1] - 1;
                    if (posicionsOriginals[0] != posicions2[0] || posicionsOriginals[1] != posicions2[1]) {

                        posicionsOriginals = posicions;
                        taulellJoc[x][y] = "   #";
                        taulellNumeros[x][y] = "   #";
                        taulellJoc = destapar(taulellJoc, taulellNumeros, posicions2, posicionsOriginals);
                    }
                }
            }

            if (y != 0) {

                if (!taulellNumeros[x][y - 1].equals("   0")) {
                    taulellJoc[x][y - 1] = taulellNumeros[x][y - 1];
                } else {
                    posicions2[0] = posicions[0];
                    posicions2[1] = posicions[1] - 1;
                    if (posicionsOriginals[0] != posicions2[0] || posicionsOriginals[1] != posicions2[1]) {
                        posicionsOriginals = posicions;
                        taulellJoc[x][y] = "   #";
                        taulellNumeros[x][y] = "   #";
                        taulellJoc = destapar(taulellJoc, taulellNumeros, posicions2, posicionsOriginals);
                    }

                }
            }

            if (x < taulellJoc.length - 1 && y > 0) {
                if (!taulellNumeros[x + 1][y - 1].equals(zero)) {
                    taulellJoc[x + 1][y - 1] = taulellNumeros[x + 1][y - 1];
                } else {
                    posicions2[0] = posicions[0] + 1;
                    posicions2[1] = posicions[1] - 1;
                    if (posicionsOriginals[0] != posicions2[0] || posicionsOriginals[1] != posicions2[1]) {
                        posicionsOriginals = posicions;
                        taulellJoc[x][y] = "   #";
                        taulellNumeros[x][y] = "   #";
                        taulellJoc = destapar(taulellJoc, taulellNumeros, posicions2, posicionsOriginals);
                    }
                }
            }

            if (x != taulellJoc.length - 1) {

                if (!taulellNumeros[x + 1][y].equals("   0")) {
                    taulellJoc[x + 1][y] = taulellNumeros[x + 1][y];
                } else {
                    posicions2[0] = posicions[0] + 1;
                    posicions2[1] = posicions[1];
                    if (posicionsOriginals[0] != posicions2[0] || posicionsOriginals[1] != posicions2[1]) {
                        posicionsOriginals = posicions;
                        taulellJoc[x][y] = "   #";
                        taulellNumeros[x][y] = "   #";
                        taulellJoc = destapar(taulellJoc, taulellNumeros, posicions2, posicionsOriginals);
                    }

                }
            }

            if (x != taulellJoc.length - 1 && y < taulellJoc[1].length - 1) {
                if (!taulellNumeros[x + 1][y + 1].equals(zero)) {
                    taulellJoc[x + 1][y + 1] = taulellNumeros[x + 1][y + 1];
                } else {
                    posicions2[0] = posicions[0] + 1;
                    posicions2[1] = posicions[1] + 1;
                    if (posicionsOriginals[0] != posicions2[0] || posicionsOriginals[1] != posicions2[1]) {
                        posicionsOriginals = posicions;
                        taulellJoc[x][y] = "   #";
                        taulellNumeros[x][y] = "   #";
                        taulellJoc = destapar(taulellJoc, taulellNumeros, posicions2, posicionsOriginals);
                    }
                }
            }

            if (y != taulellJoc[1].length - 1) {

                if (!taulellNumeros[x][y + 1].equals("   0")) {
                    taulellJoc[x][y + 1] = taulellNumeros[x][y + 1];
                } else {
                    posicions2[0] = posicions[0];
                    posicions2[1] = posicions[1] + 1;
                    if (posicionsOriginals[0] != posicions2[0] || posicionsOriginals[1] != posicions2[1]) {
                        posicionsOriginals = posicions;
                        taulellJoc[x][y] = "   #";
                        taulellNumeros[x][y] = "   #";
                        taulellJoc = destapar(taulellJoc, taulellNumeros, posicions2, posicionsOriginals);
                    }

                }
            }

            if (x != 0 && y < taulellJoc[1].length - 1) {
                if (!taulellNumeros[x - 1][y + 1].equals(zero)) {
                    taulellJoc[x - 1][y + 1] = taulellNumeros[x - 1][y + 1];
                } else {
                    posicions2[0] = posicions[0] - 1;
                    posicions2[1] = posicions[1] + 1;
                    if (posicionsOriginals[0] != posicions2[0] || posicionsOriginals[1] != posicions2[1]) {
                        posicionsOriginals = posicions;
                        taulellJoc[x][y] = "   #";
                        taulellNumeros[x][y] = "   #";
                        taulellJoc = destapar(taulellJoc, taulellNumeros, posicions2, posicionsOriginals);
                    }
                    taulellJoc[x][y] = taulellNumeros[x][y];
                }
            }
        }
        return taulellJoc;

    }

    /**
     * Contra el número de caselles destapades en cada torn
     * @param taulellJoc Taulell el qual té les caselles destapades
     * @return Número de caselles destapades
     */
    public static int contador(String taulellJoc[][]) {
        int cont = 0;
        String dash = "   -";

        for (String[] taulellJoc1 : taulellJoc) {
            for (String taulellJoc11 : taulellJoc1) {
                if (!taulellJoc11.equals(dash)) {
                    cont++;
                }
            }
        }

        return cont;
    }

}
