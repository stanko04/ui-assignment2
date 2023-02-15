import com.aspose.cells.*;
import com.sun.jdi.IntegerValue;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class Main {

    // method for create 2D array 7x7, with empty spaces and with five treasures
    public static String[][] createArray() {
        String[][] array = {{"*", "*", "*", "*", "*", "*", "*"}, {"*", "*", "*", "*", "P", "*", "*"},
                {"*", "*", "P", "*", "*", "*", "*"}, {"*", "*", "*", "H", "*", "*", "P"},
                {"*", "P", "*", "*", "*", "*", "*"}, {"*", "*", "*", "*", "P", "*", "*"},
                {"*", "*", "*", "*", "*", "*", "*"}};
        return array;
    }

    // method for print 2D array
    public static void printArray2D(String[][] array) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(array[i][j] + "  ");
            }
            System.out.print("\n");
        }
    }

    // method for increment address
    public static byte[] increment(byte[] address) {
        byte[] newArr = new byte[address.length];
        System.arraycopy(address, 0, newArr, 0, address.length);
        boolean carry = true;
        for (int i = (newArr.length - 1); i >= 0; i--) {
            if (carry) {
                if (newArr[i] == 0) {
                    newArr[i] = 1;
                    carry = false;
                } else {
                    newArr[i] = 0;
                    carry = true;
                }
            }
        }
        return newArr;
    }

    // method for decrement address
    public static byte[] decrement(byte[] address) {
        if(toBinString(address).equals("00000000")) {
            return new byte[]{1,1,1,1,1,1,1,1};
        }
        byte[] newArr = new byte[address.length];
        System.arraycopy(address, 0, newArr, 0, address.length);
        boolean carry = true;
        for (int i = (newArr.length - 1); i >= 0; i--) {
            if (carry) {
                if (newArr[i] == 1) {
                    newArr[i] = 0;
                    carry = false;
                }
                else {
                    newArr[i] = 1;
                    carry = true;
                }
            }
        }
        return newArr;
    }

    // method for convert binary array into string
    private static String toBinString(byte[] address) {
        StringBuilder string = new StringBuilder();
        for (byte b : address) {
            string.append(b == 0 ? "0" : "1");
        }
        return string.toString();
    }

    // method for create virtual machine addresses and values
    public static DataCell[] createGenesForPlayer() {
        // initialize addresses for virtual machine from 000000(0) to 111111(63)
        byte[] address = {0, 0, 0, 0, 0, 0};
        byte[][] addressesArray = new byte[64][];   // array of 64 addresses
        DataCell[] virtualAddresses = new DataCell[64];     // array of 64 data cells
        byte[][] firstTwoBits = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};   // first two bits from value of data cell
        int min = 0;
        int max = 3;
        int max2 = 63;    // minimum and maximum for random numbers

        // initialize 64 addresses for virtual machine
        for (int i = 0; i < 64; i++) {
            if (i == 0) {
                addressesArray[i] = address;
                virtualAddresses[i] = new DataCell(addressesArray[i], null);
            } else {
                addressesArray[i] = increment(addressesArray[i - 1]);
                virtualAddresses[i] = new DataCell(addressesArray[i], null);
            }
        }

        // initialize 64 values for addresses in virtual machine
        for (int i = 0; i < 64; i++) {
            int randomForFirstTwoBits = (int) (Math.random() * (max - min + 1) + min);
            int randomForNextBits = (int) (Math.random() * (max2 - min + 1) + min);
            byte[] valueOfDataCell = new byte[8];
            int index = 0;
            for (int j = 0; j < 2; j++) {
                valueOfDataCell[index] = firstTwoBits[randomForFirstTwoBits][j];
                index++;
            }
            for (int j = 0; j < 6; j++) {
                valueOfDataCell[index] = addressesArray[randomForNextBits][j];
                index++;
            }
            virtualAddresses[i].value = valueOfDataCell;
        }
        return virtualAddresses;
    }

    // method for copy 2D array
    public static String[][] copy(String[][] src) {
        if (src == null) {
            return null;
        }
        return Arrays.stream(src).map(String[]::clone).toArray(String[][]::new);
    }

    // method for move up
    public static String[][] moveUp(String[][] array) {
        String[][] copy = copy(array);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (array[i][j].equals("H")) {
                    if (i != 0) {
                        if (!(copy[i - 1][j].equals("P"))) {
                            String tmp = copy[i][j];
                            copy[i][j] = copy[i - 1][j];
                            copy[i - 1][j] = tmp;
                            return copy;
                        }
                        if (copy[i - 1][j].equals("P")) { // inkrementovat pocet najdenyc pokladov
                            String tmp = copy[i][j];
                            copy[i][j] = "*";
                            copy[i - 1][j] = tmp;
                            return copy;
                        }
                    }
                    if (i == 0) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // method for move down
    public static String[][] moveDown(String[][] array) {
        String[][] copy = copy(array);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (copy[i][j].equals("H")) {
                    if (i != 6) {
                        if (!(copy[i + 1][j].equals("P"))) {
                            String tmp = copy[i][j];
                            copy[i][j] = copy[i + 1][j];
                            copy[i + 1][j] = tmp;
                            return copy;
                        }
                        if (copy[i + 1][j].equals("P")) { // inkrementovat pocet najdenyc pokladov
                            String tmp = copy[i][j];
                            copy[i][j] = "*";
                            copy[i + 1][j] = tmp;
                            return copy;
                        }
                    }
                    if (i == 6) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // method for move right
    public static String[][] moveRight(String[][] array) {
        String[][] copy = copy(array);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (copy[i][j].equals("H")) {
                    if (j != 6) {
                        if (!(copy[i][j + 1].equals("P"))) {
                            String tmp = copy[i][j];
                            copy[i][j] = copy[i][j + 1];
                            copy[i][j + 1] = tmp;
                            return copy;
                        }
                        if (copy[i][j + 1].equals("P")) {
                            String tmp = copy[i][j];
                            copy[i][j] = "*";
                            copy[i][j + 1] = tmp;
                            return copy;
                        }

                    }
                    if (j == 6) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // method for move left
    public static String[][] moveLeft(String[][] array) {
        String[][] copy = copy(array);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (copy[i][j].equals("H")) {
                    if (j != 0) {
                        if (!(copy[i][j - 1].equals("P"))) {
                            String tmp = copy[i][j];
                            copy[i][j] = copy[i][j - 1];
                            copy[i][j - 1] = tmp;
                            return copy;
                        }
                        if (copy[i][j - 1].equals("P")) {
                            String tmp = copy[i][j];
                            copy[i][j] = "*";
                            copy[i][j - 1] = tmp;
                            return copy;
                        }

                    }
                    if (j == 0) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // method for do one game for one subject
    public static void doGame(Population population, int numberOfGeneration, Worksheet worksheet, Workbook workbook,
                              int generationNumber) throws Exception {


        // loop across population
        mainLoop:
        for (int x = 0; x < population.players.length; x++) {

            if(population.players[x].isBest == true) {
                continue  mainLoop;
            }

            String[][] array = createArray();
            int numberOfInstructions = 0;
            int numberOfMoves = 0;
            int numberOfFoundTreasures = 0;
            int index = 0;

            DataCell[] temp = new DataCell[64];
            for(int i = 0; i < temp.length; i++) {
                temp[i] = new DataCell(population.players[x].originalGenes[i].address, population.players[x].originalGenes[i].value);
            }

            // loop for 500 instructions, do virtual machine, find treasures, do moves
            for (int k = 0; k < 500; k++) {


                if (index > 63) {
                    index = 0;
                }

                // incrementing case
                if (temp[index].value[0] == 0 && temp[index].value[1] == 0) {
                    numberOfInstructions++;
                    // create array for which address looking for
                    byte[] desiredAddress = {temp[index].value[2], temp[index].value[3], temp[index].value[4],
                            temp[index].value[5], temp[index].value[6], temp[index].value[7]};
                    // find address which is equal to desired address
                    for (int j = 0; j < temp.length; j++) {
                        if (Arrays.equals(temp[j].address, desiredAddress)) {
                            temp[j].value = increment(temp[j].value);
                            break;
                        }
                    }
                    index++;
                    continue;
                }
                // decrementing case
                if (temp[index].value[0] == 0 && temp[index].value[1] == 1) {
                    numberOfInstructions++;
                    byte[] desiredAddress = {temp[index].value[2], temp[index].value[3], temp[index].value[4],
                            temp[index].value[5], temp[index].value[6], temp[index].value[7]};
                    for (int j = 0; j < temp.length; j++) {
                        if (Arrays.equals(temp[j].address, desiredAddress)) {
                            temp[j].value = decrement(temp[j].value);
                            break;
                        }
                    }
                    index++;
                    continue;
                }
                // jump case
                if (temp[index].value[0] == 1 && temp[index].value[1] == 0) {
                    numberOfInstructions++;
                    byte[] desiredAddress = {temp[index].value[2], temp[index].value[3], temp[index].value[4],
                            temp[index].value[5], temp[index].value[6], temp[index].value[7]};
                    for (int j = 0; j < temp.length; j++) {
                        if (Arrays.equals(temp[j].address, desiredAddress)) {
                            index = j;
                            break;
                        }
                    }
                    continue;
                }
                // move case
                if (temp[index].value[0] == 1 && temp[index].value[1] == 1) {
                    numberOfInstructions++;
                    byte[] desiredAddress = {temp[index].value[2], temp[index].value[3], temp[index].value[4],
                            temp[index].value[5], temp[index].value[6], temp[index].value[7]};
                    for (int j = 0; j < temp.length; j++) {
                        if (Arrays.equals(temp[j].address, desiredAddress)) {
                            String[][] pomArray = copy(array);
                            array = doMove(array, temp[j].value, population.players[x]);
                            numberOfMoves++;
                            if (array == null) {
                                numberOfFoundTreasures = 5 - checkFoundTreasures(pomArray);
                                population.players[x].fitness = numberOfFoundTreasures;
                                population.players[x].numberOfMoves = numberOfMoves;
                                continue mainLoop;
                            }
                            break;
                        }
                    }
                    index++;
                }
            }
            numberOfFoundTreasures = 5 - checkFoundTreasures(array);
            population.players[x].fitness = numberOfFoundTreasures;
            population.players[x].numberOfMoves = numberOfMoves;
        }

        Player bestPlayer = new Player(findTheBestPlayer(population));
        System.out.println("Najlepsi jedinec " + generationNumber + ". generacie: " +
                "pocet najedenych pokladov: " + bestPlayer.fitness + ", " +
                "pocet krokov hraca: " + bestPlayer.numberOfMoves + ", " +
                "Postupnost krokov: " + bestPlayer.moves);

        if(bestPlayer.fitness == 5) {
            worksheet.getCells().get("A" + String.valueOf(generationNumber)).putValue(String.valueOf(generationNumber));
            worksheet.getCells().get("B" + String.valueOf(generationNumber)).putValue(bestPlayer.fitness);
            generationNumber++;
            int chartIndex = worksheet.getCharts().add(ChartType.LINE, 5, 5, 15, 10);
            Chart chart = worksheet.getCharts().get(chartIndex);
            chart.setChartDataRange("A1:B" + generationNumber, true);
            workbook.save("Line-Chart.xls", SaveFormat.XLSX);
            return;
        }

        if(numberOfGeneration > 0) {
            numberOfGeneration--;
        }


        if(numberOfGeneration != 0) {
            worksheet.getCells().get("A" + String.valueOf(generationNumber)).putValue(String.valueOf(generationNumber));
            worksheet.getCells().get("B" + String.valueOf(generationNumber)).putValue(bestPlayer.fitness);
            generationNumber++;
            Player[] newPlayers = doCrossAndMutation(population, bestPlayer);
//            Player[] newPlayers = doCrossAndMutationWithElitism(population, bestPlayer);
            Population newPopulation = new Population(newPlayers);
            doGame(newPopulation, numberOfGeneration, worksheet, workbook, generationNumber);
        }

        if(numberOfGeneration == 0) {
            Scanner myObj = new Scanner(System.in);
            System.out.println("Prajes si vytvorit dalsiu generaciu (y-ano,n-nie): ");
            String string = myObj.nextLine();
            if(string.equals("y")) {
                generationNumber++;
                Player[] newPlayers = doCrossAndMutation(population, bestPlayer);
//                Player[] newPlayers = doCrossAndMutationWithElitism(population, bestPlayer);
                Population newPopulation = new Population(newPlayers);
                doGame(newPopulation, numberOfGeneration, worksheet, workbook, generationNumber);
            }
            else{
                int chartIndex = worksheet.getCharts().add(ChartType.LINE, 5, 5, 15, 10);
                Chart chart = worksheet.getCharts().get(chartIndex);
                chart.setChartDataRange("A1:B" + generationNumber, true);
                workbook.save("Line-Chart.xls", SaveFormat.XLSX);
                return;
            }
        }

//        if(numberOfGeneration == 0) {
//            int chartIndex = worksheet.getCharts().add(ChartType.LINE, 5, 5, 15, 10);
//            Chart chart = worksheet.getCharts().get(chartIndex);
//            chart.setChartDataRange("A1:B50", true);
//            workbook.save("Line-Chart.xls", SaveFormat.XLSX);
//        }

    }

    // method for do move
    public static String[][] doMove(String[][] array, byte[] value, Player player) {
        if (value[6] == 1 && value[7] == 1) {
            player.moves = player.moves.concat(" H");
            return moveUp(array);
        }
        if (value[6] == 0 && value[7] == 0) {
            player.moves = player.moves.concat(" D");
            return moveDown(array);
        }
        if (value[6] == 1 && value[7] == 0) {
            player.moves = player.moves.concat(" L");
            return moveLeft(array);
        }
        if (value[6] == 0 && value[7] == 1) {
            player.moves = player.moves.concat(" P");
            return moveRight(array);
        }
        return null;
    }

    // method for check how many treasures player found
    public static int checkFoundTreasures(String[][] startArray) {
        int numberOfFoundTreasures = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (startArray[i][j].equals("P")) {
                    numberOfFoundTreasures++;
                }
            }
        }
        return numberOfFoundTreasures;
    }

    public static Player findTheBestPlayer(Population population) {
        Player bestPlayer = new Player(population.players[0]);
        int index = 0;
        for (int i = 1; i < population.players.length; i++) {
            if (population.players[i].fitness > bestPlayer.fitness) {
                bestPlayer = new Player(population.players[i]);
                index = i;
            }
            if (population.players[i].fitness == bestPlayer.fitness) {
                if (population.players[i].numberOfMoves < bestPlayer.numberOfMoves) {
                    bestPlayer = new Player(population.players[i]);
                    index = i;
                }
            }
        }
//        System.arraycopy(population.players, index + 1, population.players, index, population.players.length - 1 - index);
        population.players = ArrayUtils.remove(population.players, index);
        return bestPlayer;
    }

    // method for make new population
    public static Player[] doCrossAndMutation(Population population, Player bestPlayer) {
        Player[] newPlayers = new Player[101];

        int index = 0;
        int num = 0;
        for (int i = 0; i < 50; i++) {
            Player[] fatherAndMother = getFatherAndMother(population);


            int min = 0;
            int max = 63;
            int random = (int) (Math.random() * (max - min + 1) + min);
            DataCell[] temp1 = new DataCell[64];
            for (int j = 0; j < random; j++) {
                temp1[j] = fatherAndMother[0].originalGenes[j];
            }
            for (int j = random; j < 64; j++) {
                temp1[j] = fatherAndMother[1].originalGenes[j];
            }
            DataCell[] temp1Mutated = doMutation(temp1);
            newPlayers[index] = new Player(temp1Mutated);

            num = num + 1;


            index = index + 1;

            DataCell[] temp2 = new DataCell[64];
            for (int j = 0; j < random; j++) {
                temp2[j] = fatherAndMother[1].originalGenes[j];
            }
            for (int j = random; j < 64; j++) {
                temp2[j] = fatherAndMother[0].originalGenes[j];
            }
            DataCell[] temp2Mutated = doMutation(temp2);
            newPlayers[index] = new Player(temp2Mutated);

            num = num + 1;

            index = index + 1;
        }



        DataCell[] temp = new DataCell[64];
        for(int i = 0; i < temp.length; i++) {
            temp[i] = new DataCell(bestPlayer.originalGenes[i].address, bestPlayer.originalGenes[i].value);
        }
        newPlayers[100] = new Player(temp);
        newPlayers[100].moves = bestPlayer.getMoves();
        newPlayers[100].fitness = bestPlayer.getFitness();
        newPlayers[100].numberOfMoves = bestPlayer.getNumberOfMoves();
        newPlayers[100].isBest = true;


        return newPlayers;
    }

    // method for make mutation
    public static DataCell[] doMutation(DataCell[] genes) {
        int min = 0; int max = 63;
        int[] arrayOfRandomNumbers = new int[8];
        for(int i = 0; i < arrayOfRandomNumbers.length; i++) {
            int random = (int) (Math.random() * (max - min + 1) + min);
            arrayOfRandomNumbers[i] = random;
        }

        DataCell[] temp = new DataCell[64];
        for(int i = 0; i < temp.length; i++) {
            temp[i] = new DataCell(genes[i].address, genes[i].value);
        }

        for(int i = 0; i < temp.length; i++) {
            for(int j = 0; j < arrayOfRandomNumbers.length; j++) {
                if(i == arrayOfRandomNumbers[j]) {
                    temp[i].value = changeZerosAndOnes(temp[i].value);
                }
            }
        }

        return temp;

    }

    public static Player[] getFatherAndMother(Population population) {

        List<Player> list = new ArrayList<Player>(population.players.length);
        Collections.addAll(list, population.players);

        // get first couple for father
        Collections.shuffle(list);

        Player[] firstCouple = new Player[2];
        for (int i = 0; i < 2; i++) {
            firstCouple[i] = list.get(i);
            list.remove(i);
        }

        // get second couple for mother
        Collections.shuffle(list);
        Player[] secondCouple = new Player[2];
        for (int i = 0; i < 2; i++) {
            secondCouple[i] = list.get(i);
            list.remove(i);
        }


        Player[] twoBest = new Player[2];

        twoBest[0] = new Player(firstCouple[0].originalGenes);
        if (firstCouple[1].fitness > firstCouple[0].fitness) {
            twoBest[0] = firstCouple[1];
        }

        twoBest[1] = new Player(secondCouple[0].originalGenes);
        if (secondCouple[1].fitness > secondCouple[0].fitness) {
            twoBest[1] = secondCouple[1];
        }

        return twoBest;
    }

    public static byte[] changeZerosAndOnes(byte[] originalGens) {
        for(int i = 0; i < originalGens.length; i++) {
            if(originalGens[i] == 1) {
                originalGens[i] = 0;
            }
            else if(originalGens[i] == 0) {
                originalGens[i] = 1;
            }
        }
        return originalGens;
    }

    public static Player[] doCrossAndMutationWithElitism(Population population, Player bestPlayer) {
        Player[] newPlayers = new Player[101];

        ArrayList<Player> list = new ArrayList<Player>(Arrays.asList(population.players));
        sort(list);

        Collections.reverse(list);

        int index = 0;
        for(int i = 0; i < 20; i++) {
            DataCell[] temp = new DataCell[64];
            for(int j = 0; j < 64; j++) {
                temp[j] = list.get(i).originalGenes[j];
            }
            DataCell[] tempMutated = doMutation(temp);
            newPlayers[index] = new Player(tempMutated);
            index++;
            list.remove(i);
        }

        Player[] players = list.toArray(new Player[list.size()]);
        Population newPopulation = new Population(players);

        for(int i = 0; i < 40; i++) {
            Player[] fatherAndMother = getFatherAndMother(newPopulation);


            int min = 0;
            int max = 63;
            int random = (int) (Math.random() * (max - min + 1) + min);
            DataCell[] temp1 = new DataCell[64];
            for (int j = 0; j < random; j++) {
                temp1[j] = fatherAndMother[0].originalGenes[j];
            }
            for (int j = random; j < 64; j++) {
                temp1[j] = fatherAndMother[1].originalGenes[j];
            }
            DataCell[] temp1Mutated = doMutation(temp1);
            newPlayers[index] = new Player(temp1Mutated);


            index = index + 1;

            DataCell[] temp2 = new DataCell[64];
            for (int j = 0; j < random; j++) {
                temp2[j] = fatherAndMother[1].originalGenes[j];
            }
            for (int j = random; j < 64; j++) {
                temp2[j] = fatherAndMother[0].originalGenes[j];
            }
            DataCell[] temp2Mutated = doMutation(temp2);
            newPlayers[index] = new Player(temp2Mutated);

            index = index + 1;
        }

        DataCell[] temp = new DataCell[64];
        for(int i = 0; i < temp.length; i++) {
            temp[i] = new DataCell(bestPlayer.originalGenes[i].address, bestPlayer.originalGenes[i].value);
        }
        newPlayers[100] = new Player(temp);
        newPlayers[100].moves = bestPlayer.getMoves();
        newPlayers[100].fitness = bestPlayer.getFitness();
        newPlayers[100].numberOfMoves = bestPlayer.getNumberOfMoves();
        newPlayers[100].isBest = true;

        return newPlayers;
    }

    public static void sort(ArrayList<Player> list) {

        list.sort(Comparator.comparing(Player::getFitness));
    }

    // main method
    public static void main(String[] args) throws Exception {
        String[][] array = createArray();

        System.out.println("Zaciatocna pozicia pokladov a hraca, je pre kazdeho jedinca rovnaka: ");
        printArray2D(array);
        System.out.println("--------------");
        System.out.println("--------------");

        System.out.println("Kazda generacia obsahuje 101 jedincov.");

        Scanner myObj = new Scanner(System.in);
        System.out.println("Zadaj kolko generacii chces vytvorit: ");
        String number = myObj.nextLine();



        int min = 1;
        int max = 1000;
        int num = 0;


        Workbook workbook = new Workbook();
        Worksheet worksheet = workbook.getWorksheets().get(0);


        Player[] players = new Player[101];
        for (int i = 0; i < players.length; i++) {
            DataCell[] originalGenes = createGenesForPlayer();
            players[i] = new Player(originalGenes);
            num = num + 1;
        }


        Population population = new Population(players);


        doGame(population, Integer.valueOf(number), worksheet, workbook, 1);

    }
}




